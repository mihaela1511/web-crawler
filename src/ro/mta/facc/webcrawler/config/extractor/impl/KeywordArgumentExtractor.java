package ro.mta.facc.webcrawler.config.extractor.impl;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.config.extractor.ArgumentExtractor;

/**
 * Aceasta clasa se ocupa de extragerea listei de cuvinte cautate din lista de argumente ale programului
 */
public class KeywordArgumentExtractor implements ArgumentExtractor {
    
     /*
    crawler search[lista de cuvinte cheie separate prin spatiu]
     */
    
    @Override
    public void extractConfigArgument(String[] args, WebCrawlerConfig crawlerConfig) {
        
        //Transforma toate argumentele primite intr-o lista
        List<String> words = Arrays.asList(args);

        //Scoate cuvantul "search" din lista.
        words.remove(0);
        
        crawlerConfig.setKeywords(words);
    }
}
