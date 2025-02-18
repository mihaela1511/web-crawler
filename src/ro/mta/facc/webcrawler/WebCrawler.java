package ro.mta.facc.webcrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.LogManager;

public class WebCrawler {
    public static void main(String[] args) {
        try {
            InputStream stream = new FileInputStream(new File("src/logging.properties"));
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            System.out.println("Fisierul de configurare pentru logare nu a fost gasit");
        }
        Supervisor supervisor = new Supervisor();
        String command = "";
        Scanner commandReader = new Scanner(System.in);

        while (!command.equalsIgnoreCase("exit")) {
            System.out.println("Introduceti comanda: ");
            command = commandReader.nextLine();
            String[] commandParts = command.split(" ");
            if ("crawler".equalsIgnoreCase(commandParts[0])) {
                switch (commandParts[1]) {
                    case "crawl" -> supervisor.setConfiguration(commandParts);
                    case "download" -> supervisor.downloadAllURLs(commandParts[2]);
                    case "list" -> supervisor.setFileTypeArgumentExtractor(commandParts);
                    case "maxDim" -> supervisor.setMaxFileSize(commandParts);
                    case "search" -> supervisor.setKeywords(commandParts);
                    case "localSiteDirectory" -> supervisor.setLocalSiteDirectory(commandParts[2]);
                    default -> System.out.println("Comanda necunoscuta!");
                }

            } else if ("exit".equalsIgnoreCase(commandParts[0])) {
                System.out.println("Crawler a fost oprit!");
            } else {
                System.out.println("Comanda necunoscuta!");
            }
        }
    }
}
