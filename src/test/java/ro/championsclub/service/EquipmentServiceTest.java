package ro.championsclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.constant.EquipmentCategoryEnum;
import ro.championsclub.dto.request.EquipmentRequest;
import ro.championsclub.dto.request.EquipmentUpdateRequest;
import ro.championsclub.dto.response.EquipmentAdminView;
import ro.championsclub.dto.response.EquipmentView;
import ro.championsclub.entity.Equipment;
import ro.championsclub.entity.Image;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.repository.EquipmentRepository;
import ro.championsclub.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class EquipmentServiceTest {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ImageRepository imageRepository;

    private MockMultipartFile file;
    private Image image;
    private Image image1;

    @BeforeEach
    void setup() throws IOException {
        equipmentRepository.deleteAll();

        // mock multipart file
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);

        file = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                os.toByteArray()
        );

        // save images
        image = Image.builder()
                .name("image")
                .width(315)
                .height(315)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .size(2124L)
                .build();

        image1 = Image.builder()
                .name("image 1")
                .width(315)
                .height(315)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .size(2124L)
                .build();

        imageRepository.saveAll(List.of(image, image1));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void saveEquipmentTest() {
        EquipmentRequest request = new EquipmentRequest();
        request.setName("name");
        request.setCategory("CARDIO");

        equipmentService.saveEquipment(request, file);

        // success
        assertThat(equipmentRepository.existsByName("name")).isTrue();

        // when throws ResourceConflictException
        assertThatThrownBy(() -> equipmentService.saveEquipment(request, file))
                .isInstanceOf(ResourceConflictException.class);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void disableEquipmentTest() {
        var name = "test";

        var equipment = Equipment.builder()
                .name(name)
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image)
                .build();

        equipmentRepository.save(equipment);

        equipmentService.disableEquipment(name);

        Throwable thrown = catchThrowable(() -> equipmentRepository.getByName(name));

        assertThat(thrown).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Equipment with name: " + name + " not found");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateEquipmentTest() {
        var name = "test";

        var equipment = Equipment.builder()
                .name(name)
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image)
                .build();

        equipmentRepository.save(equipment);

        var request = new EquipmentUpdateRequest();
        request.setName("Updated Equipment");

        var equipmentAdminView = equipmentService.updateEquipment(name, null, request);

        assertThat(equipmentAdminView).isNotNull();
        assertThat(equipmentAdminView.getEquipmentName()).isEqualTo(request.getName());
    }

    @Test
    void getAllEquipmentsTest() {
        var equipment = Equipment.builder()
                .name("test")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image)
                .build();

        equipmentRepository.save(equipment);

        List<EquipmentView> equipments = equipmentService.getAllEquipments();

        assertThat(equipments).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllActiveEquipmentsTest() {
        var equipment = Equipment.builder()
                .name("test")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image)
                .isActive(false)
                .build();

        var equipment1 = Equipment.builder()
                .name("test 1")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image1)
                .build();

        equipmentRepository.saveAll(List.of(equipment, equipment1));

        List<EquipmentAdminView> equipments = equipmentService.getAllActiveEquipments();

        assertThat(equipments).hasSize(1);
        assertThat(equipments.getFirst().getEquipmentName()).isEqualTo(equipment1.getName());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllInactiveEquipmentsTest() {
        var equipment = Equipment.builder()
                .name("test")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image)
                .isActive(false)
                .build();

        var equipment1 = Equipment.builder()
                .name("test 1")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image1)
                .build();

        equipmentRepository.saveAll(List.of(equipment, equipment1));

        List<EquipmentAdminView> equipments = equipmentService.getAllInactiveEquipments();

        assertThat(equipments).hasSize(1);
        assertThat(equipments.getFirst().getEquipmentName()).isEqualTo(equipment.getName());
    }

}
