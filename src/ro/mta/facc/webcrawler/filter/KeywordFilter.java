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
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de o lista de cuvinte cheie
 */
public class KeywordFilter {
    private static Logger logger = Logger.getLogger(KeywordFilter.class.getName());

    public static void filterLocal(String directoryPath, WebCrawlerConfig crawlerConfig) {
        BaseFilter.emptyDefaultDir(directoryPath, "KeywordFilteredFiles");

        final List<String> keywords = crawlerConfig.getKeywords();

        Path startPath = Path.of(directoryPath);

        List<Path> filteredFiles = null;
        try {
            filteredFiles = Files.find(startPath, 99, (path, attrs) -> {

                try {
                    if (Files.isDirectory(path)) {
                        return false;
                    }
                    List<String> lines = Files.readAllLines(path);

                    String text = String.join("\n", lines);

                    lines.clear();

                    return keywords.stream().anyMatch(text::contains);

                } catch (IOException e) {
                    logger.severe(String.format("Continutul fisierului % s nu a putut fi parsat", path.toString()));
                    return false;
                }

            }, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
        } catch (IOException e) {
            logger.severe(String.format("Un fisier din directorul %s nu a putut fi deschis", directoryPath));
        }


        BaseFilter.moveFilteredFiles(filteredFiles, directoryPath, "KeywordFilteredFiles");
    }
}
