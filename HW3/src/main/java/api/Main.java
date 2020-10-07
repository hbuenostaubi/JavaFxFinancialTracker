//package api;
//
//
//import stockPkg.DateClass;
//import stockPkg.StockClass;
//
//import java.io.BufferedReader;
//
//
//public class Main{
//    public static void main(String[] args){
//
//        finAPI obj1=new finAPI("monthly", "FB", System.getenv("API-KEY"));
//
//
//        try{
//            BufferedReader buff=obj1.getConnection();
//            obj1.setTxt(buff);
//
//            String txt= obj1.getText();
//            System.out.println("json:");
//
//            System.out.println("The File Below");
//            String fileData = obj1.jsonDates(txt);
//
//            String file="datesData.txt";
//            WriterString dir = new WriterString(file, fileData);
//            dir.printPath();
//            System.out.println("testing1");
//            StockClass finData = new StockClass(obj1.stockParser(txt));
//
//            System.out.println(finData.getKeys());
//
//
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }
//
//
//
//    }
//}