package at.platzer.train.bulkthumbnailcreator.services;

import at.platzer.train.bulkthumbnailcreator.api.Config;
import at.platzer.train.bulkthumbnailcreator.api.Logger;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ResizeRunnable implements Runnable {

    private final String sourceFile;
    private final Logger logger;
    private final Config config;

    public ResizeRunnable(Logger logger, Config config, String sourceFile) {
        assert !sourceFile.isBlank() : "sourceFile cannot be empty";
        assert logger != null : "logger cannot be null";
        assert config != null : "config cannot be null";

        this.sourceFile = sourceFile;
        this.logger = logger;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            //logger.Info("processing file: " + sourceFile);

            File inputFile = new File(sourceFile);
            BufferedImage image = ImageIO.read(inputFile);

            //logger.Debug(String.format("resizing file: " + inputFile.getName()));
            BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.QUALITY, 500, 500);

            String extension = FilenameUtils.getExtension(inputFile.getName());
            String filename = FilenameUtils.getBaseName(inputFile.getName());

            String targetFile = Paths.get(config.getTargetThumbnailFolderPath(), filename + ".thumb." + extension).toString();

            File thumbnailFile = new File(targetFile);
            //logger.Debug(String.format("writing file: " + targetFile));
            ImageIO.write(thumbnail, extension, thumbnailFile);
        } catch (IOException e) {
            logger.Error(String.format("could not resize: %s", sourceFile), e);
        }
    }
}
