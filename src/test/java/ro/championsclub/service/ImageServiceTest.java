package ro.championsclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.entity.Image;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.exception.TechnicalException;
import ro.championsclub.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Mock
    private MultipartFile multipartFile;

    private byte[] validImageData;
    private BufferedImage validBufferedImage;

    @Value("${image.max-width}")
    private int maxWidth;

    @Value("${image.max-height}")
    private int maxHeight;

    @Value("${image.max-size}")
    private long maxSize;

    @BeforeEach
    public void setup() throws IOException {
        imageRepository.deleteAll();

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4}
        );

        imageService.saveImage(image);
        
        // Initialize test data
        validImageData = new byte[1024]; // Some dummy data
        validBufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // A valid image

        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getBytes()).thenReturn(validImageData);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(validImageData));
        when(ImageIO.read(any(ByteArrayInputStream.class))).thenReturn(validBufferedImage);
    }

    @Test
    public void saveImage_Success() {


        when(imageRepository.existsByName("test.jpg")).thenReturn(false);

        Image savedImage = Image.builder()
                .name("test.jpg")
                .data(validImageData)
                .width(100)
                .height(100)
                .size(1024L)
                .build();

        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        Image result = imageService.saveImage(multipartFile);

        assertNotNull(result);
        assertEquals("test.jpg", result.getName());
        assertEquals(100, result.getWidth());
        assertEquals(100, result.getHeight());
        assertEquals(1024L, result.getSize());

        verify(imageRepository, times(1)).existsByName("test.jpg");
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void testSaveImage_AlreadyExists() {
        when(imageRepository.existsByName("test.jpg")).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> {
            imageService.saveImage(multipartFile);
        });

        verify(imageRepository, times(1)).existsByName("test.jpg");
        verify(imageRepository, times(0)).save(any(Image.class));
    }

    @Test
    public void testSaveImage_InvalidFormat() {
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");

        assertThrows(TechnicalException.class, () -> {
            imageService.saveImage(multipartFile);
        });

        verify(imageRepository, times(0)).save(any(Image.class));
    }

    @Test
    public void testSaveImage_ExceedsSizeOrDimensions() {
        when(multipartFile.getSize()).thenReturn(maxSize + 1); // Simulating a file exceeding the max size

        assertThrows(TechnicalException.class, () -> {
            imageService.saveImage(multipartFile);
        });

        verify(imageRepository, times(0)).save(any(Image.class));
    }

    @Test
    public void testUpdateImage_Success() throws IOException {
        Image oldImage = Image.builder().name("oldImage.jpg").build();

        when(imageRepository.existsByName("test.jpg")).thenReturn(false);
        when(imageRepository.save(any(Image.class))).thenReturn(
                Image.builder()
                        .name("test.jpg")
                        .data(validImageData)
                        .width(100)
                        .height(100)
                        .size(1024L)
                        .build()
        );

        Image result = imageService.updateImage(multipartFile, oldImage);

        assertNotNull(result);
        assertEquals("test.jpg", result.getName());

        verify(imageRepository, times(1)).delete(oldImage);
        verify(imageRepository, times(1)).save(any(Image.class));
    }
}