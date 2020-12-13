package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de tip
 */
public class FileTypeFilter {
    private static Logger logger = Logger.getLogger(KeywordFilter.class.getName());

    public static void filterLocal(String directoryPath, WebCrawlerConfig crawlerConfig) {
        BaseFilter.emptyDefaultDir(directoryPath, "FileTypeFilteredFiles");
        Path startPath = Path.of(directoryPath);

        List<Path> filteredFiles = null;
        try {
            filteredFiles = Files.find(startPath, 99, (path, attrs) -> {
                if (Files.isDirectory(path)) {
                    return false;
                }
                if (crawlerConfig.getAcceptedFileTypes().isEmpty()) {
                    return true;
                }
                return filter(path.toString(), crawlerConfig);
            }, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
        } catch (IOException e) {
            logger.severe(String.format("Un fisier din directorul %s nu a putut fi deschis", directoryPath));
        }

        BaseFilter.moveFilteredFiles(filteredFiles, directoryPath, "FileTypeFilteredFiles");
    }

    public static boolean filter(String directoryPath, WebCrawlerConfig crawlerConfig) {
        List<String> fileTypes = crawlerConfig.getAcceptedFileTypes();
        if (fileTypes == null || fileTypes.isEmpty()) {
            return true;
        }
        boolean checkedType = false;
        String extension;

        if (directoryPath.lastIndexOf(".") != -1 && directoryPath.lastIndexOf(".") != 0) {
            extension = directoryPath.substring(directoryPath.lastIndexOf(".") + 1);
        } else {
            extension = "";
        }


        for (int i = 0; i < fileTypes.size(); i++) {
            if (extension.equals(fileTypes.get(i))) {
                checkedType = true;
                break;
            }
        }


        return checkedType;
    }
}
