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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Aceasta clasa va controla modul in care crowler-ul efectueaza actiuni
 */
public class Supervisor {
    private static Logger logger = null;

    private WebCrawlerConfig webCrawlerConfig;
    private ConcurrentLinkedQueue<UrlData> urlList = new ConcurrentLinkedQueue<>();
    private ConfigFileParser configFileParser;
    private FileTypeArgumentExtractor fileTypeArgumentExtractor;
    private KeywordArgumentExtractor keywordArgumentExtractor;
    private FileDimensionArgumentExtractor fileDimensionArgumentExtractor;
    private FileDownloader fileDownloader;
    private LinkExtractor linkExtractor;

    public Supervisor() {
        logger = Logger.getLogger(Supervisor.class.getName());

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
    public void setLocalSiteDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            System.out.println("Calea catre directorului site-ului local nu a fost specificata!");
        } else {
            webCrawlerConfig.setLocalSiteDirectory(directoryPath);
        }
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
                        UrlData urlInfo = urlList.poll();
                        if (urlInfo != null && !urlInfo.getUrl().isEmpty()) {
                            String localPath = fileDownloader.downloadFile(urlInfo.getUrl(), webCrawlerConfig);
                            if (localPath != null && urlInfo.getLevel() <= webCrawlerConfig.getLevel()) {
                                linkExtractor.setFilePath(localPath);
                                List<String> webpageUrlList = linkExtractor.extractLinksFromFile(urlInfo.getUrl(), localPath, webCrawlerConfig);
                                if (webpageUrlList != null) {
                                    for (int counter = 0; counter < webpageUrlList.size(); counter++) {
                                        UrlData urlData = new UrlData();
                                        urlData.setLevel(urlInfo.getLevel() + 1);
                                        urlData.setUrl(webpageUrlList.get(counter));
                                        urlList.add(urlData);
                                    }
                                }
                            }
                            isNotFinished = true;
                        }
                    }
                });
                thread.start();
            }
        }
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
            Files.readAllLines(p).forEach(url -> {
                UrlData urlData = new UrlData();
                urlData.setLevel(0);
                urlData.setUrl(url);
                urlList.add(urlData);
            });
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
        for (int i = 2; i < args.length; i++) {
            fileExtensions[i - 2] = args[i];
        }

        this.fileTypeArgumentExtractor.extractConfigArgument(fileExtensions, webCrawlerConfig);
        if (webCrawlerConfig.getLogLevel() >= 3) {
            logger.info("Configurarea pentru tipul de fisiere acceptate a fost salvata");
        }
        if (webCrawlerConfig.getLocalSiteDirectory() != null && !webCrawlerConfig.getLocalSiteDirectory().isEmpty()) {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Se incepe parsarea site-ului local pe baza tipului de fisiere acceptate");
            }
            FileTypeFilter.filterLocal(webCrawlerConfig.getLocalSiteDirectory(), webCrawlerConfig);
        } else {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Nu se va efectua parsarea unui site local pe baza tipului de fisiere acceptate pentru ca nu a fost setat nici un director pentru acesta");
            }
        }
    }

    /**
     * Aceasta metoda seteaza dimensiunea maxima admisa pentru descarcarea unui fisier
     *
     * @param maxDim este dimensiunea maxima a fisierului in MB.
     */
    public void setMaxFileSize(String[] maxDim) {
        fileDimensionArgumentExtractor.extractConfigArgument(maxDim, webCrawlerConfig);
        if (webCrawlerConfig.getLogLevel() >= 3) {
            logger.info("Configurarea pentru dimensiunea maxima a fisierului a fost salvata");
        }
        if (webCrawlerConfig.getLocalSiteDirectory() != null && !webCrawlerConfig.getLocalSiteDirectory().isEmpty()) {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Se incepe parsarea site-ului local pe baza dimensiunii maxime a unui fisier");
            }
            FileDimensionFilter.filterLocal(webCrawlerConfig.getLocalSiteDirectory(), webCrawlerConfig);
        } else {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Nu se va efectua parsarea unui site local pe baza dimensiunii maxime a unui fisier pentru ca nu a fost setat nici un director pentru acesta");
            }
        }
    }

    /**
     * Aceasta metoda seteaza cuvintele cheie ce vor fi cautate in fisier
     *
     * @param args argumentele care contin lista de cuvinte cheie
     */
    public void setKeywords(String[] args) {
        String[] fileExtensions = new String[args.length - 2];
        for (int i = 2; i < args.length; i++) {
            fileExtensions[i - 2] = args[i];
        }
        keywordArgumentExtractor.extractConfigArgument(args, webCrawlerConfig);
        if (webCrawlerConfig.getLogLevel() >= 3) {
            logger.info("Configurarea pentru lista de cuvinte cheie a fost salvata");
        }
        if (webCrawlerConfig.getLocalSiteDirectory() != null && !webCrawlerConfig.getLocalSiteDirectory().isEmpty()) {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Se incepe parsarea site-ului local pe baza listei de cuvinte cheie");
            }
            KeywordFilter.filterLocal(webCrawlerConfig.getLocalSiteDirectory(), webCrawlerConfig);
        } else {
            if (webCrawlerConfig.getLogLevel() >= 3) {
                logger.info("Nu se va efectua parsarea unui site local pe baza listei de cuvinte cheie pentru ca nu a fost setat nici un director pentru acesta");
            }
        }
    }

    private class UrlData {
        int level;
        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
