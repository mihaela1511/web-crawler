package ro.mta.facc.webcrawler.filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Logger;

/**
 * Aceasta clasa se va ocupa de scrierea fisierelor filtrate din directorul ce contine un site anterior
 * descarcat intr-un director ce contine fisierele filtrate
 */
public class BaseFilter {
    private static Logger logger = Logger.getLogger(BaseFilter.class.getName());

    private static void emptyDirectory(File dir) {

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                emptyDirectory(file);
            }
            file.delete();
        }
    }

    public static void emptyDefaultDir(String root, String filteredPath) {

        File dir = new File(Paths.get(root, filteredPath).toString());

        if (!dir.exists()) {
            return;
        }

        emptyDirectory(dir);

        dir.delete();
    }


    public static void moveFilteredFiles(List<Path> filteredFiles, String baseDir, String filteredPath) {

        final Path filteredDirPath = Path.of(baseDir, filteredPath);

        if (!Files.exists(filteredDirPath)) {
            try {
                Files.createDirectory(filteredDirPath);
            } catch (IOException e) {
                logger.severe("Eroare la crearea directoarelor pentru salvarea site-ului");
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
                logger.severe("Eroare la mutarea fisierelor filtrate in diretorul de fisiere filtrate");
            }
        });

    }
}
