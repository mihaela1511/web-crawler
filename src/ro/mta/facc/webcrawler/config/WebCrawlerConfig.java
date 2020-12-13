package ro.mta.facc.webcrawler.config;

import ro.mta.facc.webcrawler.filter.FileDimensionFilter;

import java.util.List;

/**
 * Aceasta clasa contine configurarile webcrowler-ului
 */
public class WebCrawlerConfig {

    private List<String> acceptedFileTypes;
    private List<String> keywords;
    private int maxDimension;
    private int numberThreads = 1;
    private int logLevel = 3;
    private String rootDir = System.getProperty("user.dir");
    private String delay = "100ms";
    private FileDimensionFilter dimFilter;
    private String localSiteDirectory;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public int getMaxDimension() {
        return maxDimension;
    }

    public void setMaxDimension(int maxDimension) {
        this.maxDimension = maxDimension;
        this.dimFilter = new FileDimensionFilter();
    }

    public int getNumberThreads() {
        return numberThreads;
    }

    public void setNumberThreads(int numberThreads) {
        this.numberThreads = numberThreads;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public List<String> getAcceptedFileTypes() {
        return this.acceptedFileTypes;
    }

    public void setAcceptedFileTypes(List<String> acceptedFileTypes) { this.acceptedFileTypes = acceptedFileTypes; }

    public FileDimensionFilter getFileDimensionFilter() {
        return this.dimFilter;
    }

    public String getLocalSiteDirectory() {
        return localSiteDirectory;
    }

    public void setLocalSiteDirectory(String localSiteDirectory) {
        this.localSiteDirectory = localSiteDirectory;
    }
}
