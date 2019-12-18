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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Object>> runnables = new ArrayList<>();

        for(int i = 0; i < sourceImagePaths.length; i++) {
            String fullPath = Paths.get(sourcePath, sourceImagePaths[i]).toString();
            runnables.add(Executors.callable(new ResizeRunnable(logger, config, fullPath)));
        }

        try {
            executor.invokeAll(runnables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        logger.Info(String.format("Execution took %d ms", (endTime - startTime)/1000000));
    }
}
