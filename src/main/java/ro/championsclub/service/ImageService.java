package ro.championsclub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.entity.Image;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.exception.TechnicalException;
import ro.championsclub.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${image.max-width}")
    private int maxWidth;

    @Value("${image.max-height}")
    private int maxHeight;

    @Value("${image.max-size}")
    private long maxSize;

    public Image saveImage(MultipartFile file) {
        String name = file.getOriginalFilename();

        if (imageRepository.existsByName(name)) {
            throw new ResourceConflictException("Image: " + name + " already exists");
        }

        if (name != null && !name.isBlank() && name.endsWith(".jpg")) {
            byte[] data;
            BufferedImage bufferedImage;

            try {
                data = file.getBytes();

                bufferedImage = ImageIO.read(file.getInputStream());
            } catch (IOException e) {
                throw new TechnicalException("Failed to get image bytes");
            }

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            long size = file.getSize();

            if (width <= maxWidth && height <= maxHeight && size < maxSize) {
                var image = Image.builder()
                        .name(file.getOriginalFilename())
                        .data(data)
                        .width(bufferedImage.getWidth())
                        .height(bufferedImage.getHeight())
                        .size(file.getSize())
                        .build();

                return imageRepository.save(image);
            }

            log.debug("Image width: {}, height: {}, size: {}", width, height, size);
            throw new TechnicalException("Image exceeded width, height or size");
        }

        log.debug("Invalid image format: {} for: {}", file.getContentType(), name);
        throw new TechnicalException("Invalid file format");
    }

    @Transactional
    public Image updateImage(MultipartFile file, Image oldImage) {
        var image = saveImage(file);

        imageRepository.delete(oldImage);

        return image;
    }

}
