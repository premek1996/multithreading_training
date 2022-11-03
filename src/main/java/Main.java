import service.OtodomService;

public class Main {

    public static void main(String[] args) {
        var otodomWebsiteURL = "https://www.otodom.pl/pl/wyszukiwanie/sprzedaz/mieszkanie/cala-polska";
        var otodomService = new OtodomService(otodomWebsiteURL);
        var start = System.currentTimeMillis();
        otodomService.downloadAdvertisements();
        var end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
        otodomService.downloadAdvertisementsAsync();
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
