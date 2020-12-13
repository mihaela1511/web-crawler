package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de dimensiunea acestora
 */
public class FileDimensionFilter {

    private static Logger logger = Logger.getLogger(FileDimensionFilter.class.getName());

    public static void filterLocal(String directoryPath, WebCrawlerConfig crawlerConfig) {
        BaseFilter.emptyDefaultDir(directoryPath, "DimensionFilteredFiles");
        Path startPath = Path.of(directoryPath);

        List<Path> filteredFiles = null;
        try {
            filteredFiles = Files.find(startPath, 99, (path, attrs) -> {
                if (Files.isDirectory(path)) {
                    return false;
                }
                return filter(new File(path.toString()).length(), crawlerConfig);
            }, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
        } catch (IOException e) {
            logger.severe(String.format("Un fisier din directorul %s nu a putut fi deschis", directoryPath));
        }

        BaseFilter.moveFilteredFiles(filteredFiles, directoryPath, "DimensionFilteredFiles");
    }

    public static boolean filter(long dimension, WebCrawlerConfig crawlerConfig) {
        long maxDim = crawlerConfig.getMaxDimension();
        maxDim *= 1000000; // conversie in MB

        if (maxDim <= 0) {
            return true;
        }

        if (dimension <= maxDim) {
            return true;
        } else {
            return false;
        }
    }

}
