package ro.mta.facc.webcrawler.parse;

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
     * @return lista de link-uri gasite
     */
    LinkedList<String> extractLinksFromFile(String filePath);
    void setFilePath(String path);
    String getFilePath();

}
