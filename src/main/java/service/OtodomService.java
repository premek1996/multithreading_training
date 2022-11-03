package service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class OtodomService {

    private static final String ADVERTISEMENT_LINK_PART = "/pl/oferta/";
    private static final String ADVERTISEMENT_LINK_END = "\"";
    private static final String ADVERTISEMENT_LINK_FIRST_PART = "https://www.otodom.pl";
    private static final String ADVERTISEMENT_FILE_NAME_FORMAT = "advertisement_%d.html";
    private final String otodomWebsiteURL;

    public void downloadAdvertisements() {
        var websiteContent = getWebsiteContent(otodomWebsiteURL);
        var advertisementsLinks = extractAdvertisementsLinks(websiteContent);
        var fileNumber = 1;
        for (var advertisementLink : advertisementsLinks) {
            var fileName = ADVERTISEMENT_FILE_NAME_FORMAT.formatted(fileNumber);
            writeWebsiteContentToFile(advertisementLink, fileName);
            fileNumber++;
        }
    }

    public void downloadAdvertisementsAsync() {
        var websiteContent = getWebsiteContent(otodomWebsiteURL);
        var advertisementsLinks = extractAdvertisementsLinks(websiteContent);
        var executorService = Executors.newFixedThreadPool(10);
        var fileNumber = 1;
        for (var advertisementLink : advertisementsLinks) {
            var fileName = ADVERTISEMENT_FILE_NAME_FORMAT.formatted(fileNumber);
            executorService.submit(() -> writeWebsiteContentToFile(advertisementLink, fileName));
            fileNumber++;
        }
        executorService.shutdown();
    }

    @SneakyThrows
    private static String getWebsiteContent(String websiteURL) {
        var url = new URL(websiteURL);
        try (var in = new BufferedReader(
                new InputStreamReader(url.openStream()))) {
            String inputLine;
            var stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        }
    }

    private static List<String> extractAdvertisementsLinks(String websiteContent) {
        var extractedLinks = new ArrayList<String>();
        String link;
        int linkIndex;
        do {
            linkIndex = websiteContent.indexOf(ADVERTISEMENT_LINK_PART);
            if (linkIndex > 0) {
                link = extractAdvertisementLink(websiteContent, linkIndex);
                extractedLinks.add(ADVERTISEMENT_LINK_FIRST_PART + link);
                websiteContent = websiteContent.substring(linkIndex + link.length());
            }
        } while (linkIndex > 0);
        return extractedLinks;
    }

    private static String extractAdvertisementLink(String websiteContent, int linkIndex) {
        return websiteContent.substring(linkIndex)
                .split(ADVERTISEMENT_LINK_END)[0];
    }

    @SneakyThrows
    private static void writeWebsiteContentToFile(String websiteURL, String fileName) {
        var websiteContent = getWebsiteContent(websiteURL);
        try (var fileWriter = new FileWriter(fileName);
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(websiteContent);
        }
    }

}
