package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de o lista de cuvinte cheie
 */
public class KeywordFilter {

    private static void emptyDirectory(File dir) {

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                emptyDirectory(file);
            }
            file.delete();
        }
    }

    private static void emptyDefaultDir(String root) {

        File dir = new File(Paths.get(root, "KeywordFilteredFiles").toString());

        if (!dir.exists()) {
            return;
        }

        emptyDirectory(dir);

        dir.delete();
    }


    private static void moveFilteredFiles(List<Path> filteredFiles, String baseDir) {

        final Path filteredDirPath = Path.of(baseDir, "KeywordFilteredFiles");

        if (!Files.exists(filteredDirPath)) {
            try {
                Files.createDirectory(filteredDirPath);
            } catch (IOException e) {
                System.out.println("Eroare la crearea directoarelor pentru salvarea site-ului");
            }
        }

        filteredFiles.forEach(filePath -> {

            String absPath = filePath.toAbsolutePath().toString();

            absPath = absPath.replace(baseDir, filteredDirPath.toString());

            try {
                Path p = Paths.get(absPath);


                if (!Files.exists(p.getParent())) {
                    new File(p.toString()).mkdirs();
                }

                Files.copy(filePath, p, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    public static void filter(String directoryPath, WebCrawlerConfig crawlerConfig) {
        emptyDefaultDir(directoryPath);

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
                    e.printStackTrace();
                    return false;
                }

            }, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Calea catre fisier nu a putut fi gasita");
        }


        moveFilteredFiles(filteredFiles, directoryPath);
    }
}
