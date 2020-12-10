package ro.mta.facc.webcrawler.download;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Aceasta clasa defineste modul in care este efectuata descarcarea unui fisier
 */
public class FileDownloaderImpl implements FileDownloader {
    @Override
    public void downloadFile(String downloadUrl, WebCrawlerConfig crawlerConfig) {

        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream())){

            String rootDir = crawlerConfig.getRootDir();

            String aux = downloadUrl.substring(0,downloadUrl.indexOf("/"));

            String downloadLocation = downloadUrl.replace(aux,rootDir);

            Path p = Path.of(downloadLocation);

            Path parentDir = p.getParent();

            if (!parentDir.toFile().exists()){
                parentDir.toFile().mkdirs();
            }


            FileOutputStream fos = new FileOutputStream(downloadLocation);

            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            while((bytesRead = in.read(dataBuffer,0,1024))!=-1){
                fos.write(dataBuffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
