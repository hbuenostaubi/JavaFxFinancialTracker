package views;

import api.FinAPI;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import stockpkg.Stock;
import stockpkg.StockDate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class StockDisplay {
    private ComboBox<Stock> categories;
    private ComboBox<StockDate> stockClose;
    private Text textBox;
    private Map<Stock, List<StockDate>> stockMap;
    private final ObservableList<Stock> stocks;
    private final List<StockDate> stockDates;


    public StockDisplay(String series, String ipo) throws Exception {
        FinAPI obj = new FinAPI(series, ipo);
        stockMap = obj.getAPI();
        stocks = observableArrayList(stockMap.keySet());
        textBox= new Text();
        setUpCategories();
        setUpStockWk();
        this.stockDates=obj.getAllStocks();
    }

    private class StockDateConverter extends StringConverter<StockDate>{
        private final String SEP = ", closing price: ";
        @Override
        public String toString(StockDate stock) {
            if (stock ==null)
                return null;
            else
                return stock.getDate()+SEP+stock.getClose();
        }

        @Override
        public StockDate fromString(String string) {
            String dateLocate = string.split(SEP)[0];
            List<StockDate> listStockDates = stockDates.stream()
                    .filter(date -> date.getDate().equals(dateLocate))
                    .limit(1)
                    .collect(Collectors.toList());
            return listStockDates.isEmpty() ? null : listStockDates.get(0);
        }
    }

    private void setUpCategories() {
        categories = new ComboBox<>();
        categories.getItems().addAll(sortClose());  //anonymous, nested class below for sorting close date
        categories.setPromptText("--Select a value--");
        categories.valueProperty().addListener((observable, oldValue, newValue) -> {
            textBox.setVisible(false);  //make visible after selection
            stockClose.getItems().clear();
            stockClose.getItems().addAll(stockMap.get(newValue));
            stockClose.setVisible(true);
        });
    }
    private ObservableList<Stock> sortClose(){
        return stocks.sorted((o1, o2) -> {
            if(o1.getMin()<o2.getMin())
                return -1;
            else if (o1.getMin()>o2.getMin())
                return 1;
            else
                return 0;
        });

    }
    private void setUpStockWk(){
        stockClose = new ComboBox<>();
        stockClose.setPromptText("--Select a value--");
        stockClose.setConverter(new StockDateConverter());
        stockClose.setVisible(false);
        createDateSelectorListener();
        handleStockDateComboUpdate();
    }

    private void handleStockDateComboUpdate(){
        stockClose.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(StockDate obj, boolean ckr){
                super.updateItem(obj, ckr);
                if(ckr || obj==null)
                    setText("--Select a value--");
                else{
                    StockDateConverter converter = new StockDateConverter();
                    setText(converter.toString(obj));
                }
            }
        });
    }
    private void createDateSelectorListener(){
        stockClose.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                String txt = newValue.getDate() + "\n" +
                        "Volume: " +newValue.getVolume() +"\n"+
                        "Opening Price: " + newValue.getOpen() +"\n"
                        +"High Price: " +newValue.getHigh();
                textBox.setText(txt);
                textBox.setVisible(true);
            }
        });
    }

    public double getCount(String stockVal){
        if(stockVal.equals("LOWEST"))
            return this.stockMap.get(Stock.LOWEST).size();
        else if(stockVal.equals("LOW"))
            return this.stockMap.get(Stock.LOW).size();
        else if(stockVal.equals("MID"))
            return this.stockMap.get(Stock.MID).size();
        else if(stockVal.equals("HIGH"))
            return this.stockMap.get(Stock.HIGH).size();
        else
            return 0;
        }

    public double getVolumeTrades(String stockVal){
        if(stockVal.equals("LOWEST"))
            return getReduce(Stock.LOWEST);
        else if(stockVal.equals("LOW"))
            return getReduce(Stock.LOW);
        else if(stockVal.equals("MID"))
            return getReduce(Stock.MID);
        else if(stockVal.equals("HIGH"))
            return getReduce(Stock.HIGH);
        else
            return 0;
    }

    private Integer getReduce(Object obj) {
        return this.stockMap.get(obj).stream()
                .map(n -> {
                    System.out.println(n.getVolume() / 100);
                    return n.getVolume() / 100;
                })
                .reduce(0, (a, b) -> a + b);
    }


    public ComboBox<Stock> getCategories() {
        return categories;
    }

    public ComboBox<StockDate> getStockClose() {
        return stockClose;
    }

    public Text getTextBox() {
        return textBox;
    }
}
