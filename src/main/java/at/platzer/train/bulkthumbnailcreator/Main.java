package at.platzer.train.bulkthumbnailcreator;

import at.platzer.train.bulkthumbnailcreator.api.Config;
import at.platzer.train.bulkthumbnailcreator.api.Logger;
import at.platzer.train.bulkthumbnailcreator.services.PrintLineLogger;
import at.platzer.train.bulkthumbnailcreator.services.PropertiesConfig;
import at.platzer.train.bulkthumbnailcreator.services.ResizeRunnable;
import org.apache.commons.io.FileUtils;
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

        FileUtils.cleanDirectory(new File(config.getTargetThumbnailFolderPath()));

        String[] sourceImagePaths = sourceFolder.list();

        long startTime = System.nanoTime();

        Thread[] resizeThreads = new Thread[sourceImagePaths.length];
        for(int i = 0; i < resizeThreads.length; i++) {
            String fullPath = Paths.get(sourcePath, sourceImagePaths[i]).toString();
            resizeThreads[i] = new Thread(new ResizeRunnable(logger, config, fullPath));
            resizeThreads[i].start();
        }

        for(int i = 0; i < resizeThreads.length; i++) {
            try {
                resizeThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();

        logger.Info(String.format("Execution took %d ms", (endTime - startTime)/1000000));
    }
}
