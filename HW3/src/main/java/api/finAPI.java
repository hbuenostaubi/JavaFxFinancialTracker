package api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.HttpMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.lang.*;
import java.util.*;

import org.json.simple.parser.ParseException;
import stockPkg.DateClass;
import stockPkg.StockClass;


public class finAPI{
    private String urlString;
    private String key;
    private String txt;

    public finAPI(String time, String symbol, String key){
        this.key=key;
        this.urlString="https://www.alphavantage.co/query?"+
                getSeries(time)+"&symbol="+symbol+
                "&apikey="+key;
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
        final int conTimeout = 1000;
        HttpURLConnection connection=null;

        final URL url=new URL(this.urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(conTimeout);
        connection.setRequestMethod(HttpMethod.GET);

        if(connection.getResponseCode()==200){
            System.out.println("Connected");
            final InputStream inputStream =connection.getInputStream();  //data inputstream? Trana doesn't know type
            final BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader;
        }else{
            System.out.println("Not Connected");
            return null;}
    }

    public String getText(){
        return this.txt;
    }
    public void resetText(String file){
        this.txt=file;
    }


    public Map stockParser(String txt) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(txt);
        JSONObject mntly= (JSONObject) response.get("Monthly Time Series");

        Map<String, List<DateClass>> hMap = new HashMap<String, List<DateClass>>();
        IterMap(mntly, hMap);

        return hMap;
    }

    private void IterMap(JSONObject mntly, Map<String, List<DateClass>> hMap) {
        for(Object key: mntly.keySet()){
            String keyString=(String)key;   ///date string is a key for all other objects
            JSONObject dateString= (JSONObject) mntly.get(keyString);
            String date = keyString;
            double open = Double.valueOf((String) dateString.get("1. open"));
            double high = Double.valueOf((String) dateString.get("2. high"));
            double low = Double.valueOf((String) dateString.get("3. low"));
            double close = Double.valueOf((String) dateString.get("4. close"));
            int volume = Integer.valueOf((String)dateString.get("5. volume"));

            DateClass objTemp =new DateClass(date,open, high, low, close, volume);
            if(objTemp.getClose()<=75)
                addToMap("Lowest", objTemp, hMap);
            else if(objTemp.getClose()<=125)
                addToMap("Low", objTemp, hMap);
            else if(objTemp.getClose()<=175)
                addToMap("Mid", objTemp, hMap);
            else
                addToMap("High", objTemp, hMap);
        }
    }

    public void addToMap(String key, DateClass obj1, Map<String, List<DateClass>> map){
        if(!map.containsKey(key)){
            map.put(key, new ArrayList<>(Arrays.asList(obj1)));
        }else{
            map.get(key).add(obj1);
        }
    }

    public Map getAPI(finAPI obj1) throws Exception {
        BufferedReader buff=obj1.getConnection();

        obj1.setTxt(buff);

        String txt= obj1.getText();

        Map<String, List<DateClass>> finData = obj1.stockParser(txt);

        return finData;

    }


}  ///END