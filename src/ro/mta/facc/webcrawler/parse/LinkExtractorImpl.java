package ro.mta.facc.webcrawler.parse;

import javax.xml.parsers.SAXParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aceasta clasa defineste modul in care sunt extrase link-urile aflate intr-un fisier
 */
public class LinkExtractorImpl implements LinkExtractor {

    private String defaultPath = "./downloads/defaultpage.html";
    private String PathToFile;
    private List<String> extractedURLs;
    private File webpage;
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public LinkExtractorImpl()
    {
        this.PathToFile = defaultPath;
        extractedURLs = new LinkedList<String>();
        webpage = new File(this.PathToFile);
    }

    @Override
    public void setFilePath(String path)
    {
        this.webpage = null;
        this.PathToFile = path;
        this.webpage = new File(this.PathToFile);
    }

    @Override
    public String getFilePath()
    {
        return this.PathToFile;
    }

    private boolean existFile()
    {
        if (webpage.exists())
        {
            return true;
        }

        return false;
    }


    @Override
    public LinkedList<String> extractLinksFromFile(String filePath) {
        if (existFile())
        {
            List<String> AllLinks = new LinkedList<String>();
            try {
                Scanner myReader = new Scanner(webpage);
                while(myReader.hasNextLine())
                {
                    String data = myReader.nextLine();

                    Matcher matcher = urlPattern.matcher(data);
                    while (matcher.find()) {
                        int matchStart = matcher.start(1);
                        int matchEnd = matcher.end();
                        String r= matcher.group(0);

                        if (r.indexOf("\"")!=-1)
                        {
                            r = r.substring(1,r.length());
                        }
                        System.out.println(r);

                    }



                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("FileNotFoundException, error: " + e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }
}
