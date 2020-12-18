package ro.mta.facc.webcrawler.parse;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aceasta clasa defineste modul in care sunt extrase link-urile aflate intr-un fisier
 */
public class LinkExtractorImpl implements LinkExtractor {
    private static Logger logger = Logger.getLogger(LinkExtractorImpl.class.getName());

    private String defaultPath = "./downloads/defaultpage.html";
    private String PathToFile;
    private List<String> extractedURLs;
    private File webpage;
    private static final Pattern urlPattern = Pattern.compile(
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public LinkExtractorImpl() {
        this.PathToFile = defaultPath;
        extractedURLs = new LinkedList<String>();
        webpage = new File(this.PathToFile);
    }

    @Override
    public void setFilePath(String path) {
        this.webpage = null;
        this.PathToFile = path;
        this.webpage = new File(this.PathToFile);
    }

    @Override
    public String getFilePath() {
        return this.PathToFile;
    }

    private boolean existFile() {
        if (webpage.exists()) {
            return true;
        }

        return false;
    }


    @Override
    public LinkedList<String> extractLinksFromFile(String stringUrl, String filePath, WebCrawlerConfig crawlerConfig) {
        if (existFile()) {
            LinkedList<String> AllLinks = new LinkedList<String>();
            Map<String, String> replaceMap = new HashMap<>();
            URL url;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                if (crawlerConfig.getLogLevel() >= 2) {
                    logger.warning(String.format("Url-ul %s nu este valid!", stringUrl));
                }
                return null;
            }
            try {
                Scanner myReader = new Scanner(webpage);
                if (crawlerConfig.getLogLevel() >= 3) {
                    logger.info(String.format("Parsare fisier %s pentru a cautarea si inlocuirea link-urilor", filePath));
                }
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();

                    Matcher matcher = urlPattern.matcher(data);
                    while (matcher.find()) {
                        String r = matcher.group(0);
                        r = r.replaceAll("'", "");
                        r = r.replaceAll("\"", "");
                        r = r.replaceAll("href=", "");
                        r = r.replaceAll(" ", "");
                        if (r.charAt(0) != '#') {
                            if (r.charAt(0) == '/') {
                                r = url.getProtocol().concat("://").concat(url.getHost()).concat(r);
                            }
                            String downloadLocation;
                            String rootDir = crawlerConfig.getRootDir();
                            String tempLocation = r.replaceAll("[\\\\/:*?\"<>|]", "/");
                            if (!r.contains("/")) {
                                downloadLocation = rootDir.concat("\\").concat(tempLocation).concat("\\index.html");
                            } else {
                                String aux = tempLocation.substring(0, tempLocation.indexOf("/"));
                                downloadLocation = tempLocation.replace(aux, rootDir);
                                String fileName = tempLocation.substring(tempLocation.lastIndexOf('/') + 1, tempLocation.length());
                                if (!fileName.isEmpty()) {
                                    if (fileName.lastIndexOf('.') < 0) {
                                        downloadLocation = downloadLocation.concat(".html");
                                    }
                                } else {
                                    downloadLocation = downloadLocation.concat("\\index.html");
                                }
                            }

                            Path p = Path.of(downloadLocation);

                            replaceMap.put(r, p.toString().replace("\\", "\\\\"));
                            AllLinks.add(r);
                        }
                    }
                }
                myReader.close();
                boolean successful = true;
                try {
                    String content = Files.readString(Path.of(filePath));
                    for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                        content = content.replaceAll(entry.getKey(), entry.getValue());
                    }
                    Files.writeString(Path.of(filePath), content, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    successful = false;
                    if (crawlerConfig.getLogLevel() >= 2) {
                        logger.warning(String.format("Link-urile nu au putut fi inlocuite cu cai locale pentru fisierul %s!", filePath));
                    }
                }
                if (crawlerConfig.getLogLevel() >= 3 && successful) {
                    logger.info(String.format("Link-urile au fost extrase si inlocuite pentru fisierul %s", filePath));
                }
                return AllLinks;
            } catch (FileNotFoundException e) {
                if (crawlerConfig.getLogLevel() >= 2) {
                    logger.warning(String.format("Fisierul %s nu a putut fi gasit pentru parsarea link-urilor!", filePath));
                }
            }
        }
        return null;
    }
}
