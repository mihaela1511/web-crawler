package ro.mta.facc.webcrawler.download;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

/**
 * Aceasta interfata gestioneaza descarcare de fisiere de la un anumit URL
 */
public interface FileDownloader {

    /**
     * Aceasta metoda descarca un fisier de la un anumit URL tinand cont de configurarile crowler-ului
     *
     * @param downloadUrl   URL-ul de la care va fi descarcat fisierul
     * @param crawlerConfig configurarile crowler-ului
     * @return calea la care fisierul a fost salvat pe disc
     */
    String downloadFile(String downloadUrl, WebCrawlerConfig crawlerConfig);

}
