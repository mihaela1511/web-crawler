package ro.mta.facc.webcrawler.config.extractor;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

/**
 * Interfata utilizata pentru a parsa configurarile aplicatiei trimise in linia de comanda
 */
public interface ArgumentExtractor {

    /**
     * Metoda folosita pentru a extrage o anumita configurare din linia de comanda
     *
     * @param crawlerConfig obiect ce contine configurarile crawler-ului
     * @param args lista de argumente a programului
     */
    void extractConfigArgument(String[] args, WebCrawlerConfig crawlerConfig);

}
