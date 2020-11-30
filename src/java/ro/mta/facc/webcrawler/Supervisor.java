package java.ro.mta.facc.webcrawler;

import java.ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import java.ro.mta.facc.webcrawler.config.extractor.impl.ConfigFileParser;
import java.ro.mta.facc.webcrawler.config.extractor.impl.FileDimensionArgumentExtractor;
import java.ro.mta.facc.webcrawler.config.extractor.impl.FileTypeArgumentExtractor;
import java.ro.mta.facc.webcrawler.config.extractor.impl.KeywordArgumentExtractor;
import java.ro.mta.facc.webcrawler.download.FileDownloader;
import java.ro.mta.facc.webcrawler.download.FileDownloaderImpl;
import java.ro.mta.facc.webcrawler.filter.FileDimensionFilter;
import java.ro.mta.facc.webcrawler.filter.FileTypeFilter;
import java.ro.mta.facc.webcrawler.filter.KeywordFilter;
import java.ro.mta.facc.webcrawler.parse.LinkExtractor;
import java.ro.mta.facc.webcrawler.parse.LinkExtractorImpl;
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
     * Aceasta metoda se ocupa de setarea configuratiei crowler-ului pe baza argumentelor programului
     *
     * @param args lista de argumente a programului
     */
    public void constructConfiguration(String[] args) {

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

}
