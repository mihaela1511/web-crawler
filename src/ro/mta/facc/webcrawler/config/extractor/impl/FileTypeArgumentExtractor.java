package ro.mta.facc.webcrawler.config.extractor.impl;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.config.extractor.ArgumentExtractor;

import java.util.LinkedList;

/**
 * Aceasta clasa se ocupa de extragerea tipului de fisiere acceptate din lista de argumente ale programului
 */
public class FileTypeArgumentExtractor implements ArgumentExtractor {


    @Override
    public void extractConfigArgument(String[] args, WebCrawlerConfig crawlerConfig) {
        LinkedList<String> extraction = new LinkedList<>();
        for (int i=0; i< args.length; i++) {
            extraction.add(args[i]);
        }

        crawlerConfig.setAcceptedFileTypes(extraction);

    }
}
