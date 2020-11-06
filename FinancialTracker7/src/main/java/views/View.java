package views;

import api.FinAPI;
import stockpkg.Stock;
import stockpkg.StockDate;

import java.util.List;
import java.util.Map;

public class View {
    public static void main(String[] args) throws Exception {
        FinAPI obj = new FinAPI("monthly", "FB");
        Map<Stock, List<StockDate>> stockMap = obj.getAPI();
        System.out.println("See Below:");

        int sum =stockMap.get(Stock.LOWEST).stream()
                .map(n -> {
                    System.out.println(n.getVolume()/100);
                    return n.getVolume()/100;
                })
                .reduce(0,(a,b) -> a+b);
        System.out.println(sum);
    }
}
