package views;

import api.FinAPI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import stockpkg.Stock;
import stockpkg.StockDate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static javafx.collections.FXCollections.observableArrayList;

public class StockDisplay {
    private ComboBox<Stock> categories;
    private ComboBox<StockDate> stockWk;
    private Text textBox;
    private final Map<Stock, List<StockDate>> stockMap;
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
            String date = string.split(SEP)[0];
            for (StockDate date2: stockDates){
                if(date2.getDate().equals(date))
                    return date2;
            }
             return null;
        }
    }

    private void setUpCategories() {
        categories = new ComboBox<>();
        categories.getItems().addAll(sortClose());  //anonymous, nested class below for sorting close date
        categories.setPromptText("--Select a value--");
        categories.valueProperty().addListener(new ChangeListener<Stock>() {
            @Override
            public void changed(ObservableValue<? extends Stock> observable, Stock oldValue, Stock newValue) {
                textBox.setVisible(false);  //make visible after selection
                stockWk.getItems().clear();
                stockWk.getItems().addAll(stockMap.get(newValue));
                stockWk.setVisible(true);
            }
        });
    }
    private ObservableList<Stock> sortClose(){
        return stocks.sorted(new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                if(o1.getMin()<o2.getMin())
                    return -1;
                else if (o1.getMin()>o2.getMin())
                    return 1;
                else
                    return 0;
            }
        });

    }
    private void setUpStockWk(){
        stockWk = new ComboBox<>();
        stockWk.setPromptText("--Select a value--");
        stockWk.setConverter(new StockDateConverter());
        stockWk.setVisible(false);
        createDateSelectorListener();
        handleStockDateComboUpdate();
    }

    private void handleStockDateComboUpdate(){
        stockWk.setButtonCell(new ListCell<>(){
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
        stockWk.valueProperty().addListener(new ChangeListener<StockDate>() {
            @Override
            public void changed(ObservableValue<? extends StockDate> observable, StockDate oldValue, StockDate newValue) {
                if(newValue!=null){
                    String txt = newValue.getDate() + "\n" +
                            "Volume: " +newValue.getVolume() +"\n"+
                            "Opening Price: " + newValue.getOpen() +"\n"
                            +"High Price: " +newValue.getHigh();
                    textBox.setText(txt);
                    textBox.setVisible(true);
                }
            }
        });
    }

    public ComboBox<Stock> getCategories() {
        return categories;
    }

    public ComboBox<StockDate> getStockWk() {
        return stockWk;
    }

    public Text getTextBox() {
        return textBox;
    }
}
