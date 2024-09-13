package ro.championsclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.exception.TechnicalException;
import ro.championsclub.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class ImageServiceIT {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    private MultipartFile multipartFile;

    private final String imageNameValid = "images/184_elite_block-valid.jpg";

    @BeforeEach
    void setup() throws IOException {
        Path path = Path.of("src/test/resources/images/184_elite_block-valid.jpg");

        byte[] content = Files.readAllBytes(path);

        multipartFile = new MockMultipartFile("file", imageNameValid, "image/jpeg", content);
    }

    @Test
    void saveImageSuccessTest() {
        var image = imageService.saveImage(multipartFile);

        assertThat(image).isNotNull();
        assertThat(imageNameValid).isEqualTo(image.getName());
        assertThat(imageRepository.existsById(image.getId())).isTrue();
    }

    @Test
    void saveImageThrowsConflictExceptionTest() {
        imageService.saveImage(multipartFile);

        assertThatThrownBy(() -> imageService.saveImage(multipartFile))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Image: " + imageNameValid + " already exists");
    }

    @Test
    void saveImageExceedsMaxSizeTest() throws IOException {
        File file = new File("src/test/resources/images/184_elite_block-exceeds-size.jpg");

        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile largeMultipartFile = new MockMultipartFile(
                "file",
                "images/184_elite_block-exceeds-size.jpg",
                "image/jpeg",
                content
        );

        assertThatThrownBy(() -> imageService.saveImage(largeMultipartFile))
                .isInstanceOf(TechnicalException.class)
                .hasMessageContaining("Image exceeded width, height or size");
    }

    @Test
    void saveImageInvalidFormatTest() throws IOException {
        File file = new File("src/test/resources/images/184_elite_block.png");

        byte[] content = Files.readAllBytes(file.toPath());

        MultipartFile pngMultipartFile = new MockMultipartFile(
                "file",
                "184_elite_block.png",
                "image/png",
                content
        );

        assertThatThrownBy(() -> imageService.saveImage(pngMultipartFile))
                .isInstanceOf(TechnicalException.class)
                .hasMessageContaining("Invalid file format");
    }

    @Test
    void updateImageSuccessTest() throws IOException {
        var oldImage = imageService.saveImage(multipartFile);

        Path path = Path.of("src/test/resources/images/184_elite_block-update.jpg");

        byte[] content = Files.readAllBytes(path);

        MultipartFile updatedMultipartFile = new MockMultipartFile(
                "file",
                "184_elite_block-update.jpg",
                "image/jpeg",
                content
        );

        var updatedImage = imageService.updateImage(updatedMultipartFile, oldImage);

        assertThat(updatedImage).isNotNull();
        assertThat("184_elite_block-update.jpg").isEqualTo(updatedImage.getName());
        assertThat(imageRepository.existsById(oldImage.getId())).isFalse();
    }

}
