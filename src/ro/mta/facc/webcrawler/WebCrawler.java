package ro.mta.facc.webcrawler;

import java.util.Scanner;

public class WebCrawler {
    public static void main(String[] args) {
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
