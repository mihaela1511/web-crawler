package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

import java.util.List;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de tip
 */
public class FileTypeFilter {

    public static boolean filter(String directoryPath, WebCrawlerConfig crawlerConfig) {
        List<String> fileTypes = crawlerConfig.getAcceptedFileTypes();
        boolean checkedType = false;
        String extension;

        if (directoryPath.lastIndexOf(".") != -1 && directoryPath.lastIndexOf(".")!=0)
        {
            extension =directoryPath.substring(directoryPath.lastIndexOf(".")+1);
        }
        else
        {
            extension = "";
        }


        for (int i=0; i<fileTypes.size(); i++)
        {
            if (extension == fileTypes.get(i))
            {
                checkedType = true;
                break;
            }
        }


        return checkedType;
    }
}
