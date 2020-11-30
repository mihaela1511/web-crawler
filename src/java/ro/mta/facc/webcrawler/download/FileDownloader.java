package java.ro.mta.facc.webcrawler.download;

import java.ro.mta.facc.webcrawler.config.WebCrawlerConfig;

/**
 * Aceasta interfata gestioneaza descarcare de fisiere de la un anumit URL
 */
public interface FileDownloader {

    /**
     * Aceasta metoda descarca un fisier de la un anumit URL tinand cont de configurarile crowler-ului
     *
     * @param downloadUrl   URL-ul de la care va fi descarcat fisierul
     * @param crawlerConfig configurarile crowler-ului
     */
    void downloadFile(String downloadUrl, WebCrawlerConfig crawlerConfig);

}
