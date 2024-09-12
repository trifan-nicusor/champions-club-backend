package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.constant.EquipmentCategoryEnum;
import ro.championsclub.dto.request.EquipmentRequest;
import ro.championsclub.dto.request.EquipmentUpdateRequest;
import ro.championsclub.dto.response.EquipmentAdminView;
import ro.championsclub.dto.response.EquipmentView;
import ro.championsclub.entity.Equipment;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.EquipmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ImageService imageService;

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveEquipment(EquipmentRequest request, MultipartFile file) {
        String name = request.getName();

        if (equipmentRepository.existsByName(name)) {
            throw new ResourceConflictException("Equipment with name: " + name + " already exists");
        }

        var image = imageService.saveImage(file);

        var equipment = Equipment.builder()
                .name(name)
                .category(EquipmentCategoryEnum.getCategory(request.getCategory()))
                .image(image)
                .build();

        equipmentRepository.save(equipment);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void disableEquipment(String name) {
        equipmentRepository.disable(name);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateEquipment(
            String name,
            MultipartFile file,
            EquipmentUpdateRequest request
    ) {
        if (name.equals(request.getName())) {
            throw new ResourceConflictException("Equipment with name: " + name + " already exists");
        }

        var equipment = equipmentRepository.getByName(name);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            if (equipment.getImage().getName().equals(fileName)) {
                throw new ResourceConflictException("Image with name: " + fileName + " already exists");
            }

            var image = imageService.updateImage(file, equipment.getImage());

            equipment.setImage(image);
        }

        ModelMapper.map(request, equipment);

        equipmentRepository.save(equipment);
    }

    public List<EquipmentView> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAllByIsActiveTrue();

        return ModelMapper.mapAll(equipments, EquipmentView.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EquipmentAdminView> getAllActiveEquipments() {
        List<Equipment> equipments = equipmentRepository.findAllByIsActiveTrue();

        return ModelMapper.mapAll(equipments, EquipmentAdminView.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EquipmentAdminView> getAllInactiveEquipments() {
        List<Equipment> equipments = equipmentRepository.findAllByIsActiveFalse();

        return ModelMapper.mapAll(equipments, EquipmentAdminView.class);
    }

}
