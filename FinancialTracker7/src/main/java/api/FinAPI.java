package api;

import org.json.JSONObject;
import stockpkg.Stock;
import stockpkg.StockDate;

import javax.ws.rs.HttpMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FinAPI {
    private final String urlString;
    private final String KEY = System.getenv("API-KEY");
    private JSONObject jsonIpo;
    private HttpURLConnection connection=null;
    private final List<StockDate> stockList;
    private final String series;


    public FinAPI(String time, String symbol){
        this.series=(time.equals("weekly")) ? "Weekly Time Series" : "Monthly Time Series";
        this.urlString="https://www.alphavantage.co/query?"+
                getSeries(time)+"&symbol="+symbol+
                "&apikey="+KEY;
        this.stockList=new ArrayList<>();
    }
    public void setTxt(BufferedReader buff){
        Stream<String> lines = buff.lines();
        String temp= lines.map(Object::toString)
                .collect(Collectors.joining());

        this.jsonIpo=new JSONObject(temp).getJSONObject(this.series);
    }

    private String getSeries(String time){
        if(time.equals("weekly"))
            return "function=TIME_SERIES_WEEKLY";
        else
            return "function=TIME_SERIES_MONTHLY";
    }

    public BufferedReader getConnection() throws Exception{
        this.setConnection();

        if(connection.getResponseCode()==200){
            System.out.println("Connected");
            final InputStream inputStream =connection.getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream));
        }else{
            System.out.println("Not Connected");
            return null;}
    }

    private void setConnection() throws IOException {
        final int conTimeout = 1000;
        final URL url=new URL(this.urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(conTimeout);
        connection.setRequestMethod(HttpMethod.GET);
    }

    public JSONObject getJsonIpo(){
        return this.jsonIpo;
    }


    public Map<Stock, List<StockDate>> stockParser() {  ///return type fixed?
        Map<Stock, List<StockDate>> hMap = new HashMap<>();
        iterMap(this.getJsonIpo(), hMap);

        return hMap;
    }

    public void iterMap(JSONObject jObj, Map<Stock, List<StockDate>> hMap) {
        jObj.keySet().forEach( key ->{
            StockDate objTemp = placeClasses(jObj, key, stockList);
            Arrays.asList(Stock.values()).forEach(closeCategory -> {
                if(objTemp.getClose()>=closeCategory.getMin() && objTemp.getClose()<=closeCategory.getMax())
                    addToMap(closeCategory, objTemp, hMap);
            });
        });
    }

    private static StockDate placeClasses(JSONObject jObj, String keyString, List<StockDate> stockList) {
        JSONObject dateString= (JSONObject) jObj.get(keyString);
        double open = Double.parseDouble((String) dateString.get("1. open"));
        double high = Double.parseDouble((String) dateString.get("2. high"));
        double low = Double.parseDouble((String) dateString.get("3. low"));
        double close = Double.parseDouble((String) dateString.get("4. close"));
        int volume = Integer.parseInt((String)dateString.get("5. volume"));

        StockDate objTemp =new StockDate(keyString,open, high, low, close, volume);
        stockList.add(objTemp);
        return objTemp;
    }

    public void addToMap(Stock key, StockDate obj1, Map<Stock, List<StockDate>> map){
        if(!map.containsKey(key)){
            map.put(key, new ArrayList<>(Arrays.asList(obj1)));  ///key
        }else{
            map.get(key).add(obj1);
        }
    }

    public Map<Stock, List<StockDate>> getAPI() throws Exception {   //getValues
        BufferedReader buff=this.getConnection();
        this.setTxt(buff);
        return this.stockParser();
    }

    public List<StockDate> getAllStocks(){
        return stockList;
    }

}  ///END