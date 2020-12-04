package views;

import api.FinAPI;
import stockpkg.Stock;
import stockpkg.StockDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class View {
    public static void main(String[] args) throws Exception {
        FinAPI obj = new FinAPI("monthly", "FB");
        Map<Stock, List<StockDate>> stockMap = obj.getAPI();
        System.out.println("See Below:");
        List<StockDate> lstStockDate = obj.getAllStocks();

        //really cool
        Map<LocalDate, Double> timeSeries = lstStockDate
                .stream()
                .collect(Collectors.toMap(
                        n -> LocalDate.parse(n.getDate()),
                        n-> n.getClose()
                ));

        timeSeries.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(n ->{
                    System.out.println(n.getKey());
                        }
                );

//        int sum =stockMap.get(Stock.LOWEST).stream()
//                .map(n -> {
//                    return n.getVolume()/100;
//                })
//                .reduce(0,(a,b) -> a+b);
//        System.out.println(sum);
    }
}
