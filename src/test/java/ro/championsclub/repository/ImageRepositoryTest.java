package ro.championsclub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.entity.Image;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    private Image image;

    @BeforeEach
    void setup() {
        image = Image.builder()
                .name("image")
                .width(315)
                .height(315)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .size(2124L)
                .build();

        imageRepository.save(image);
    }

    @Test
    void existsByNameReturnsTrueTest() {
        boolean exists = imageRepository.existsByName(image.getName());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameReturnsFalseTest() {
        boolean exists = imageRepository.existsByName("random");

        assertThat(exists).isFalse();
    }

}
