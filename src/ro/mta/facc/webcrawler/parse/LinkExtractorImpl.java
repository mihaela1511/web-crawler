package ro.mta.facc.webcrawler.parse;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
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
    public LinkedList<String> extractLinksFromFile(String filePath, WebCrawlerConfig crawlerConfig) {
        if (existFile()) {
            LinkedList<String> AllLinks = new LinkedList<String>();
            Map<String, String> replaceMap = new HashMap<>();
            try {
                Scanner myReader = new Scanner(webpage);
                if (crawlerConfig.getLogLevel() >= 3) {
                    logger.info(String.format("Parsare fisier %s pentru a cautarea si inlocuirea link-urilor", filePath));
                }
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();

                    Matcher matcher = urlPattern.matcher(data);
                    while (matcher.find()) {
                        int matchStart = matcher.start(1);
                        int matchEnd = matcher.end();
                        String r = matcher.group(0);

                        if (r.indexOf("\"") != -1) {
                            r = r.substring(1, r.length());
                        }
                        String rootDir = crawlerConfig.getRootDir();

                        String aux = r.substring(0, r.indexOf("/"));

                        String downloadLocation = r.replace(aux, rootDir);

                        Path p = Path.of(downloadLocation.replaceAll("\\?", "/"));

                        replaceMap.put(r, p.toString().replace("\\", "\\\\"));
                        AllLinks.add(r);
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
