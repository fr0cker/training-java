package at.platzer.train.bulkthumbnailcreator;

import at.platzer.train.bulkthumbnailcreator.api.Config;
import at.platzer.train.bulkthumbnailcreator.api.Logger;
import at.platzer.train.bulkthumbnailcreator.services.PrintLineLogger;
import at.platzer.train.bulkthumbnailcreator.services.PropertiesConfig;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting thumbnail creator");

        Logger logger = new PrintLineLogger();
        PropertiesConfig configProps = new PropertiesConfig();
        URL origPathz = Main.class.getClassLoader().getResource("config.properties");
        String origPath = origPathz.getPath();
        configProps.LoadConfig(origPath);

        Config config = configProps;

        String sourcePath = config.getSourceImageFolderPath();

        File sourceFolder = new File(sourcePath);

        String[] sourceImagePaths = sourceFolder.list();

        long startTime = System.nanoTime();

        for(String sourceImagePath : sourceImagePaths){
            String fullPath = Paths.get(sourcePath, sourceImagePath).toString();
            File inputFile = new File(fullPath);

            BufferedImage image = ImageIO.read(inputFile);

            BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.QUALITY, 500, 500);

            String extension = FilenameUtils.getExtension(inputFile.getName());
            String filename = FilenameUtils.getBaseName(inputFile.getName());

            String targetFile = Paths.get(config.getTargetThumbnailFolderPath(), filename + ".thumb." + extension).toString();
            File thumbnailFile = new File(targetFile);
            ImageIO.write(thumbnail, extension, thumbnailFile);
        }

        long endTime = System.nanoTime();

        logger.Info(String.format("Execution took %d ms", (endTime - startTime)/1000000));
    }
}
