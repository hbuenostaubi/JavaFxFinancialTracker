package stockPkg;

import api.finAPI;

import java.io.BufferedReader;
import java.util.*;

public class StockClass {

    private Map<String, List<DateClass>> ipo;

    public StockClass(Map<String, List<DateClass>>  ipo) {
        this.ipo = ipo;
    }

    public Set getKeys(){
        return this.ipo.keySet();
    }
//
//    public void getValues(){
//        for(String date: this.ipo.keySet()){
//            DateClass dClass=(DateClass) this.ipo.get(date);
//            System.out.println(dClass.getClose());
//        }
//    }
 Map getAPI(finAPI obj1) throws Exception {
    BufferedReader buff=obj1.getConnection();

    obj1.setTxt(buff);

    String txt= obj1.getText();

    Map<String, List<DateClass>> finData = obj1.stockParser(txt);

    return finData;

}

}
