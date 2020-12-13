package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de dimensiunea acestora
 */
public class FileDimensionFilter {

    public static void filter(String directoryPath, WebCrawlerConfig crawlerConfig) {

    }

    public static boolean filter(int dimension, WebCrawlerConfig crawlerConfig) {
        int maxDim = crawlerConfig.getMaxDimension();

        if (maxDim == 0)
        {
            System.out.println("max dimension is not set!!");
            return false;
        }


        if (dimension <= maxDim)  {return true;}
        else{return false;}
    }

}
