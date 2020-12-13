package ro.mta.facc.webcrawler.download;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.filter.FileDimensionFilter;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Aceasta clasa defineste modul in care este efectuata descarcarea unui fisier
 */
public class FileDownloaderImpl implements FileDownloader {
    private static Logger logger = Logger.getLogger(FileDownloaderImpl.class.getName());

    @Override
    public String downloadFile(String downloadUrl, WebCrawlerConfig crawlerConfig) {
        URL url;
        Path p = null;
        try {
            url = new URL(downloadUrl);
        } catch (MalformedURLException e) {
            if (crawlerConfig.getLogLevel() >= 2) {
                logger.warning(String.format("Url-ul %s nu este valid!", downloadUrl));
            }
            return null;
        }
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {

            String rootDir = crawlerConfig.getRootDir();

            String aux = downloadUrl.substring(0, downloadUrl.indexOf("/"));

            String downloadLocation = downloadUrl.replace(aux, rootDir);

            p = Path.of(downloadLocation);

            Path parentDir = p.getParent();

            if (!parentDir.toFile().exists()) {
                parentDir.toFile().mkdirs();
            }

            if (crawlerConfig.getLogLevel() >= 3) {
                logger.info(String.format("Fisierul %s a fost creat pe disc", p.toString()));
            }
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(p.toString());
            } catch (FileNotFoundException e) {
                logger.severe(String.format("Calea %s pentru salvarea paginii pe disc nu a putut fi creata!", p.toString()));
                return null;
            }

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            if (crawlerConfig.getLogLevel() >= 3) {
                logger.info(String.format("Se descarca %s", downloadUrl));
            }
            boolean downloadSuccessful = true;
            try {
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    try {
                        //Filtru Dimensiune
                        FileDimensionFilter dimFilter = crawlerConfig.getFileDimensionFilter();
                        if (dimFilter.filter(totalBytes, crawlerConfig) == true)
                        {
                            fos.write(dataBuffer, 0, bytesRead);
                            totalBytes+=bytesRead;
                        }
                        else
                        {
                            System.out.println("Marime fisier depaseste limita maxima admisa de filtru!!!");
                            throw new IOException("Marime fisier peste limita admisa de filtru!!!");
                        }
                    } catch (IOException e) {
                        logger.severe(String.format("O parte din continutul fisierului %s nu a putut fi scrisa pe disc!", p.toString()));
                        logger.severe(String.format("Exceptie: ", e.getMessage()));
                        e.printStackTrace();

                        downloadSuccessful = false;
                        break; //sparge while
                    }
                }
                if (crawlerConfig.getLogLevel() >= 3 && downloadSuccessful) {
                    logger.info(String.format("Descarcarea pentru %s a fost finalizata cu succes", downloadUrl));
                }
            } catch (IOException e) {
                if (crawlerConfig.getLogLevel() >= 2) {
                    logger.warning(String.format("O parte din continutul fisierului aflat la url-ul %s nu a putut fi citita!", downloadUrl));
                }
            }

        } catch (IOException e) {
            if (crawlerConfig.getLogLevel() >= 2) {
                logger.warning(String.format("Conexiunea catre url-ul %s nu a putut fi stabilita!", downloadUrl));
            }
        }
        if (p != null) {
            return p.toString();
        } else {
            return null;
        }
    }
}
