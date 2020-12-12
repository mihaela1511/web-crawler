package ro.mta.facc.webcrawler.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Aceasta clasa defineste modul in care sunt extrase link-urile aflate intr-un fisier
 */
public class LinkExtractorImpl implements LinkExtractor {

    private String defaultPath = "./downloads/defaultpage.html";
    private String PathToFile;
    private List<String> extractedURLs;
    private File webpage;

    public LinkExtractorImpl()
    {
        this.PathToFile = defaultPath;
        extractedURLs = new LinkedList<String>();
        webpage = new File(this.PathToFile);
    }

    public void setFilePath(String path)
    {
        this.PathToFile = path;
        this.webpage = new File(this.PathToFile);
    }

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
            try {
                Scanner myReader = new Scanner(webpage);
                while(myReader.hasNextLine())
                {
                    String data = myReader.nextLine();
                    String[] parts = data.split("\\s+"); //separare input prin spatii

                    for (String item:parts)
                    {
                        if (item.contains("href="))
                        {
                            //item.
                        }
                        try{
                            URL url = new URL(item);
                            System.out.print("<a href=\"" + url + "\">"+url+"</a> ");
                        }
                        catch (MalformedURLException e)
                        {
                            System.out.println(item + " ");
                        }
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
