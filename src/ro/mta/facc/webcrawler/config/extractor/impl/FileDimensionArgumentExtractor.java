package ro.mta.facc.webcrawler.config.extractor.impl;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.config.extractor.ArgumentExtractor;

/**
 * Aceasta clasa se ocupa de extragerea dimensiunii maxime acceptate pentru un fisier din lista de argumente ale
 * programului
 */
public class FileDimensionArgumentExtractor implements ArgumentExtractor {
    @Override
    public void extractConfigArgument(String[] args, WebCrawlerConfig crawlerConfig) {
        crawlerConfig.setMaxDimension(Integer.parseInt(args[2]));
    }
}
