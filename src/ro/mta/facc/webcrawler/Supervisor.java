package ro.mta.facc.webcrawler;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.config.extractor.impl.ConfigFileParser;
import ro.mta.facc.webcrawler.config.extractor.impl.FileDimensionArgumentExtractor;
import ro.mta.facc.webcrawler.config.extractor.impl.FileTypeArgumentExtractor;
import ro.mta.facc.webcrawler.config.extractor.impl.KeywordArgumentExtractor;
import ro.mta.facc.webcrawler.download.FileDownloader;
import ro.mta.facc.webcrawler.download.FileDownloaderImpl;
import ro.mta.facc.webcrawler.filter.FileDimensionFilter;
import ro.mta.facc.webcrawler.filter.FileTypeFilter;
import ro.mta.facc.webcrawler.filter.KeywordFilter;
import ro.mta.facc.webcrawler.parse.LinkExtractor;
import ro.mta.facc.webcrawler.parse.LinkExtractorImpl;

import java.util.List;

/**
 * Aceasta clasa va controla modul in care crowler-ul efectueaza actiuni
 */
public class Supervisor {
    private WebCrawlerConfig webCrawlerConfig;
    private List<String> linkList;
    private List<String> pathList;
    private ConfigFileParser configFileParser;
    private FileTypeArgumentExtractor fileTypeArgumentExtractor;
    private KeywordArgumentExtractor keywordArgumentExtractor;
    private FileDimensionArgumentExtractor fileDimensionArgumentExtractor;
    private FileDownloader fileDownloader;
    private LinkExtractor linkExtractor;

    public Supervisor() {
        webCrawlerConfig = new WebCrawlerConfig();
        configFileParser = new ConfigFileParser();
        fileTypeArgumentExtractor = new FileTypeArgumentExtractor();
        keywordArgumentExtractor = new KeywordArgumentExtractor();
        fileDimensionArgumentExtractor = new FileDimensionArgumentExtractor();
        fileDownloader = new FileDownloaderImpl();
        linkExtractor = new LinkExtractorImpl();
    }

    /**
     * Aceasta metoda se ocupa de parsarea fisierelor unui site descarcat anterior pe baza configuratiei crowler-ului
     */
    public void parseLocalSite(String directoryPath) {
        FileTypeFilter.filter(directoryPath, webCrawlerConfig);
        FileDimensionFilter.filter(directoryPath, webCrawlerConfig);
        KeywordFilter.filter(directoryPath, webCrawlerConfig);
    }

    /**
     * Aceasta metoda se ocupa de descarcarea unui fisier
     */
    public void downloadFile() {

    }

    /**
     * Aceasta metoda se ocupa de parsarea link-urilor dintr-un fisier
     */
    public void parseLink() {

    }

    /**
     * Aceasta metoda seteaza configuratia web-crowler-ului
     *
     * @param args lista de argumente pentru configuratia web-crawler-ului
     */
    public void setConfiguration(String[] args) {
        configFileParser.extractConfigArgument(args, webCrawlerConfig);
    }

}
