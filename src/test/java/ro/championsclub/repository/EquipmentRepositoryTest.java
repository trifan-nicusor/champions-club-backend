package ro.championsclub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.constant.EquipmentCategoryEnum;
import ro.championsclub.entity.Equipment;
import ro.championsclub.entity.Image;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ImageRepository imageRepository;

    private Equipment equipment1;
    private Equipment equipment2;

    @BeforeEach
    void setup() {
        var image1 = Image.builder()
                .name("image 1")
                .width(315)
                .height(315)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .size(2124L)
                .build();

        var image2 = Image.builder()
                .name("image 2")
                .width(315)
                .height(315)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .size(2124L)
                .build();

        imageRepository.saveAll(List.of(image1, image2));

        equipment1 = Equipment.builder()
                .name("equipment 1")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image1)
                .isActive(true)
                .build();

        equipment2 = Equipment.builder()
                .name("equipment 2")
                .category(EquipmentCategoryEnum.CARDIO)
                .image(image2)
                .isActive(false)
                .build();

        equipmentRepository.saveAll(List.of(equipment1, equipment2));
    }

    @Test
    void existsByNameTest() {
        boolean exists = equipmentRepository.existsByName(equipment1.getName());

        assertThat(exists).isTrue();
    }

    @Test
    void findByIdAndIsActiveTrueTest() {
        var name = equipment1.getName();

        Optional<Equipment> equipment = equipmentRepository.findByNameAndIsActiveTrue(name);

        assertThat(equipment).isPresent();
    }

    @Test
    void findAllByIsActiveTrueTest() {
        List<Equipment> equipments = equipmentRepository.findAllByIsActiveTrue();

        assertThat(equipments).isNotEmpty();
        assertThat(equipments.getFirst().getName()).isEqualTo(equipment1.getName());
    }

    @Test
    void findAllByIsActiveFalseTest() {
        List<Equipment> equipments = equipmentRepository.findAllByIsActiveFalse();

        assertThat(equipments).isNotEmpty();
        assertThat(equipments.getFirst().getName()).isEqualTo(equipment2.getName());
    }

    @Test
    void getByIdTest() {
        var name = equipment2.getName();

        Throwable thrown = catchThrowable(() -> equipmentRepository.getByName(name));

        assertThat(thrown).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Equipment with name: " + name + " not found");
    }

}
