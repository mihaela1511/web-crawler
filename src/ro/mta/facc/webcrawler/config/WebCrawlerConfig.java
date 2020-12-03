package ro.mta.facc.webcrawler.config;

import java.util.List;

/**
 * Aceasta clasa contine configurarile webcrowler-ului
 */
public class WebCrawlerConfig {

    private List<String> acceptedFileTypes;
    private List<String> keywords;
    private int maxDimension;
    private int numberThreads;
    private int logLevel;
    private String rootDir;
    private String delay;

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
        return acceptedFileTypes;
    }

    public void setAcceptedFileTypes(List<String> acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }
}
