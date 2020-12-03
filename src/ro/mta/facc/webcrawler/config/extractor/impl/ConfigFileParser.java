package ro.mta.facc.webcrawler.config.extractor.impl;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;
import ro.mta.facc.webcrawler.config.extractor.ArgumentExtractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Aceasta clasa va parsa fisierul de configurare trimis ca argument programului
 */
public class ConfigFileParser implements ArgumentExtractor {

    private static final String EMPTY_STRING = "";

    @Override
    public void extractConfigArgument(String[] args, WebCrawlerConfig crawlerConfig) {
        if (args.length < 3) {
            System.out.println("Fiserul de configurare nu a fost introdus!");
            return;
        }
        String filePath = args[2];
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(filePath);
            Properties properties = new Properties();
            properties.load(inputStream);
            Object delay = properties.get("delay");
            Object n_threads = properties.get("n_threads");
            Object root_dir = properties.get("root_dir");
            Object log_level = properties.get("log_level");
            crawlerConfig.setDelay(delay != null ? delay.toString() : EMPTY_STRING);
            crawlerConfig.setRootDir(root_dir != null ? root_dir.toString() : EMPTY_STRING);
            int casted_log_level = 0;
            if (log_level != null) {
                try {
                    casted_log_level = Integer.parseInt(log_level.toString());
                } catch (NumberFormatException ex) {
                    System.out.println("Valoarea log_level nu este corecta!");
                }
            }
            crawlerConfig.setLogLevel(casted_log_level);
            int casted_n_threads = 1;
            if (n_threads != null) {
                try {
                    casted_n_threads = Integer.parseInt(n_threads.toString());
                } catch (NumberFormatException ex) {
                    System.out.println("Valoarea n_threads nu este corecta!");
                }
            }
            crawlerConfig.setNumberThreads(casted_n_threads);
            System.out.println("Configuratia a fost setata!");
        } catch (FileNotFoundException ex) {
            System.out.println("Fisierul de configurare nu a putut fi gasit!");
        } catch (IOException e) {
            System.out.println("Continutul fisierului de configurare nu a putut fi incarcat!");
        }
    }


}
