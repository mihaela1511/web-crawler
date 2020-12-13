package ro.mta.facc.webcrawler.parse;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * Aceasta interfata se ocupa de extragerea link-urilor din interiorul unui fisier
 */
public interface LinkExtractor {

    /**
     * Aceasta metoda extrage toate link-urile dintr-un fisier
     *
     * @param filePath calea catre fisier
     * @param crawlerConfig configuratia webcrawler-ului
     * @return lista de link-uri gasite
     */
    LinkedList<String> extractLinksFromFile(String filePath, WebCrawlerConfig crawlerConfig);
    void setFilePath(String path);
    String getFilePath();

}
