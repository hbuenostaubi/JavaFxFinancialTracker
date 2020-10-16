package api;

import java.lang.Double;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.HttpMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.lang.*;
import java.util.*;

import org.json.simple.parser.ParseException;
import stockpkg.StockDate;
import stockpkg.Stock;


public class FinAPI {
    private String urlString;
    private final String KEY = System.getenv("API-KEY");
    private String txt;
    private HttpURLConnection connection=null;
    private List<StockDate> stockList;

    public FinAPI(String time, String symbol){

        this.urlString="https://www.alphavantage.co/query?"+
                getSeries(time)+"&symbol="+symbol+
                "&apikey="+KEY;
        this.stockList=new ArrayList<>();
    }
    public void setTxt(BufferedReader buff) throws Exception{
        String line;
        StringBuilder whole= new StringBuilder();
        while(((line = buff.readLine()) != null)){
            whole.append(line.strip());
        }
        this.txt=whole.toString();
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
            final BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader;
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

    public String getText(){
        return this.txt;
    }


    public Map<Stock, List<StockDate>> stockParser(String txt)  throws ParseException{  ///return type fixed?
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(txt);
        JSONObject mntly= (JSONObject) response.get("Monthly Time Series");

        Map<Stock, List<StockDate>> hMap = new HashMap<>();
        iterMap(mntly, hMap);

        return hMap;
    }

    public void iterMap(JSONObject mntly, Map<Stock, List<StockDate>> hMap) {
        for(Object key: mntly.keySet()){
            String keyString=(String)key;
            JSONObject dateString= (JSONObject) mntly.get(keyString);
            double open = Double.parseDouble((String) dateString.get("1. open"));
            double high = Double.parseDouble((String) dateString.get("2. high"));
            double low = Double.parseDouble((String) dateString.get("3. low"));
            double close = Double.parseDouble((String) dateString.get("4. close"));
            int volume = Integer.parseInt((String)dateString.get("5. volume"));

            StockDate objTemp =new StockDate(keyString,open, high, low, close, volume);
            stockList.add(objTemp);
            for(Stock closeCategory: Stock.values()){
                if(objTemp.getClose()>=closeCategory.getMin() && objTemp.getClose()<=closeCategory.getMax()){
                    addToMap(closeCategory, objTemp, hMap);
                }
            }
        }
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

        String txt= this.getText();
        Map<Stock, List<StockDate>> finData = this.stockParser(txt);
        return finData;
    }

    public List<StockDate> getAllStocks(){
        return stockList;
    }

}  ///END