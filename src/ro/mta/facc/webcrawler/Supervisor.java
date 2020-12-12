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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Aceasta clasa va controla modul in care crowler-ul efectueaza actiuni
 */
public class Supervisor {
    private static Logger logger = null;

    private WebCrawlerConfig webCrawlerConfig;
    private ConcurrentLinkedQueue<String> linkList = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> urlList = new ConcurrentLinkedQueue<>();
    private ConfigFileParser configFileParser;
    private FileTypeArgumentExtractor fileTypeArgumentExtractor;
    private KeywordArgumentExtractor keywordArgumentExtractor;
    private FileDimensionArgumentExtractor fileDimensionArgumentExtractor;
    private FileDownloader fileDownloader;
    private LinkExtractor linkExtractor;

    private Map<String, String> webpageMap;

    public Supervisor() {
            logger = Logger.getLogger(Supervisor.class.getName());

        webCrawlerConfig = new WebCrawlerConfig();
        configFileParser = new ConfigFileParser();
        fileTypeArgumentExtractor = new FileTypeArgumentExtractor();
        keywordArgumentExtractor = new KeywordArgumentExtractor();
        fileDimensionArgumentExtractor = new FileDimensionArgumentExtractor();
        fileDownloader = new FileDownloaderImpl();
        linkExtractor = new LinkExtractorImpl();
        webpageMap = new HashMap<>();
    }

    /**
     * Aceasta metoda se ocupa de parsarea fisierelor unui site descarcat anterior pe baza configuratiei crowler-ului
     */
    public void parseLocalSite(String directoryPath) throws IOException {
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
     * Descarca toate fisierele din lista de url-uri
     */
    public void downloadAllURLs(String filePath) {
        readUrlsFile(filePath);
        if (!urlList.isEmpty()) {
            for (int i = 0; i < webCrawlerConfig.getNumberThreads(); i++) {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(Integer.parseInt(webCrawlerConfig.getDelay().split("ms")[0]));
                    } catch (InterruptedException e) {
                        if (webCrawlerConfig.getLogLevel() >= 2) {
                            logger.warning("Thread.sleep a fost intrerupt!");
                        }
                    }
                    boolean isNotFinished = true;
                    while (isNotFinished) {
                        isNotFinished = false;
                        String url = urlList.poll();
                        if (url != null && !url.isEmpty()) {
                            String localPath = fileDownloader.downloadFile(url, webCrawlerConfig);

                            linkExtractor.setFilePath(localPath);
                            List<String> webpageUrlList = linkExtractor.extractLinksFromFile(localPath);

                            if (localPath != null) {
                                linkList.add(localPath); //lista de cai locale
                                for (int counter=0; counter<webpageUrlList.size(); counter++)
                                {
                                    linkList.add(webpageUrlList.get(counter));
                                }
                                isNotFinished = true;
                            }
                        }
                    }
                });
                thread.start();
            }
        }
        linkList.forEach(link -> fileDownloader.downloadFile(link, webCrawlerConfig));
    }

    /**
     * Aceasta metoda seteaza configuratia web-crowler-ului
     *
     * @param args lista de argumente pentru configuratia web-crawler-ului
     */
    public void setConfiguration(String[] args) {
        configFileParser.extractConfigArgument(args, webCrawlerConfig);
    }

    /**
     * Aceasta metoda citeste fisierul ce contine url-urile si le salveaza in supervizor
     *
     * @param filePath calea catre fisierul ce contine url-urile
     */
    private void readUrlsFile(String filePath) {

        if (filePath == null || filePath.isEmpty()) {
            System.out.println("Fisierul cu lista de url-uri nu a fost dat ca parametru");
            return;
        }
        Path p = Path.of(filePath);
        try {
            urlList.addAll(Files.readAllLines(p));
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului " + filePath);
        }
    }

    /**
     * Aceasta metoda retine tipurile de fisier pentru configuratia web-crowler-ului
     *
     * @param args lista de extensii fisier pentru configuratia web-crawler-ului
     */
    public void setFileTypeArgumentExtractor(String[] args) {
        //copiez doar informatia relevanta (doar extensiile)
        String[] fileExtensions = new String[args.length - 2];
        for (int i=2; i<args.length; i++)
        {
            fileExtensions[i-2] = args[i];
        }

        this.fileTypeArgumentExtractor.extractConfigArgument(fileExtensions, webCrawlerConfig);

    }


}
