package ro.mta.facc.webcrawler.download;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.filter.FileDimensionFilter;
import ro.mta.facc.webcrawler.filter.FileTypeFilter;

import java.io.*;
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
            String downloadLocation;

            String tempLocation = downloadUrl.replaceAll("[\\\\/:*?\"<>|]", "/");
            if (!downloadUrl.contains("/")) {
                downloadLocation = rootDir.concat("\\").concat(tempLocation);
            } else {
                String aux = tempLocation.substring(0, tempLocation.indexOf("/"));
                downloadLocation = tempLocation.replace(aux, rootDir);
            }

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
            long totalBytes = 0;

            if (crawlerConfig.getLogLevel() >= 3) {
                logger.info(String.format("Se descarca %s", downloadUrl));
            }
            boolean downloadSuccessful = true;
            try {
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    try {
                        String fpath = p.toString();
                        String dirPath = parentDir.toString();
                        String fileName = fpath.substring(dirPath.length() + 1, fpath.length());

                        // Filtru Tip Fisier
                        FileTypeFilter typeFilter = new FileTypeFilter();
                        if (typeFilter.filter(fileName, crawlerConfig) == true) {
                            //Filtru Dimensiune
                            FileDimensionFilter dimFilter = crawlerConfig.getFileDimensionFilter();
                            if (dimFilter.filter(totalBytes, crawlerConfig) == true) {
                                fos.write(dataBuffer, 0, bytesRead);
                                totalBytes += bytesRead;
                            } else {
                                fos.close();
                                File downloadedFileParts = new File(p.toString());
                                if (downloadedFileParts.delete()) {
                                    if (crawlerConfig.getLogLevel() >= 3) {
                                        logger.info(String.format("Fisierul %s a fost sters pentru ca a depasit dimensiunea maxim admisa", p.toString()));
                                    }
                                } else {
                                    logger.severe(String.format("Fisierul % nu a putut fi sters!!", p.toString()));
                                }
                                if (crawlerConfig.getLogLevel() >= 2) {
                                    logger.warning(String.format("Marimea pentru fisierul %s depaseste limita maxima admisa de filtru!!!", p.toString()));
                                }
                                return null;
                            }
                        } else {
                            if (crawlerConfig.getLogLevel() >= 2) {
                                logger.warning(String.format("Extensia pentru fisierul %s nu se afla in lista de fisiere admise", p.toString()));
                            }
                            return null;
                        }

                    } catch (IOException e) {
                        logger.severe(String.format("O parte din continutul fisierului %s nu a putut fi scrisa pe disc!", p.toString()));
                        downloadSuccessful = false;
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
