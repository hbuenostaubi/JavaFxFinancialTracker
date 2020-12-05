import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockpkg.Stock;
import stockpkg.StockDate;
import views.StockDisplay;

import java.time.LocalDate;
import java.util.Map;

public class DisplayStockApp extends Application {

    private StockDisplay boxDisplay;
    Stage stage;
    Scene scene1;
    final static String Lowest= "Lowest";
    final static String Low = "Low";
    final static String Mid = "Mid";
    final static String High = "High";

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {   ///stage var is here!

        stage=primaryStage;

        boxDisplay = new StockDisplay("monthly", "FB");

        Map<LocalDate, Double> seriesClose = boxDisplay.getStockMap();


        BorderPane pane = new BorderPane();

        setUpBorder(pane);


        Scene scene = new Scene(pane, 600, 300);

        VBox paneForRadioButtons = new VBox(20);
        paneForRadioButtons.setPadding(new Insets(5, 5,5,5));
        ToggleGroup group = new ToggleGroup();

        RadioButton button1 = new RadioButton("Data Split");
        RadioButton button2 = new RadioButton("Charts");
        createButtonG1(group, button1, button2);
        paneForRadioButtons.getChildren().addAll(button1,button2);
        pane.setLeft(paneForRadioButtons);

        VBox paneForRadioButtons2 = new VBox(20);
        paneForRadioButtons2.setPadding(new Insets(5, 5,5,5));
        VBox paneForRadioButtons3 = new VBox(20);
        paneForRadioButtons3.setPadding(new Insets(5, 5,5,5));
        ToggleGroup group1 = new ToggleGroup();
        ToggleGroup group2 = new ToggleGroup();
        RadioButton button3 = new RadioButton("Data Split");
        RadioButton button4 = new RadioButton("Charts");
        RadioButton button5 = new RadioButton("Count of dates");
        RadioButton button6 = new RadioButton("Total Trades");
        RadioButton button7 = new RadioButton("Time Series");
        createButtonG2(paneForRadioButtons2, paneForRadioButtons3, group1, group2, button3,
                button4, button5, button6, button7);

        //Bar Chart-----
        BorderPane pane2 = new BorderPane();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc =
                new BarChart<>(xAxis, yAxis);
        XYChart.Series series1 = new XYChart.Series();
        bChart1(xAxis, yAxis, bc, series1);


        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Total Trades (scaled down)");
        bChart2(series2);


        final CategoryAxis xAxis2 = new CategoryAxis();
        final NumberAxis yAxis2 = new NumberAxis();

        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis2, yAxis2);
        XYChart.Series series3 = new XYChart.Series();
        reduceLineChart(seriesClose, yAxis2, lineChart, series3);

        ///setting a pane for bar chart
        bc.getData().addAll(series1);
        pane2.setCenter(bc);
        pane2.setLeft(paneForRadioButtons2);
        pane2.setRight(paneForRadioButtons3);
        scene1 = new Scene(pane2, 800, 600);

        ///button data page and button2 barchart
        actionButtons(scene, button1, button2, button3, button4, button5, button6, button7, pane2, yAxis, bc, series1, series2, lineChart);

        stage.setTitle("Facebook Financial Tracker");
        stage.setScene(scene);
        stage.show();

    }

    private void actionButtons(Scene scene, RadioButton button1, RadioButton button2, RadioButton button3, RadioButton button4, RadioButton button5, RadioButton button6, RadioButton button7, BorderPane pane2, NumberAxis yAxis, BarChart<String, Number> bc, XYChart.Series series1, XYChart.Series series2, LineChart<String, Number> lineChart) {
        button1.setOnAction(e->{
            if(button1.isSelected()){
                stage.setScene(scene);
                button2.setSelected(true);
            }});

        button2.setOnAction(e->{
            if(button2.isSelected()){
                stage.setScene(scene1);
                button1.setSelected(true);
                }});
        button3.setOnAction(e->{
            if(button3.isSelected()){
                stage.setScene(scene);
                button4.setSelected(true);
            }});
        button4.setOnAction(e->{
            if(button4.isSelected()){
                stage.setScene(scene1);
                button3.setSelected(true);
            }});

        button5.setOnAction(e->{
            if(button5.isSelected()){
                pane2.setCenter(bc);
                bc.getData().clear();
                bc.getData().addAll(series1);
                bc.setTitle("Stock Distribution of Closing Date");
                yAxis.setLabel("Data Points");
            }});
        button6.setOnAction(e->{
            if(button6.isSelected()){
                bc.getData().clear();
                pane2.setCenter(bc);
                bc.getData().addAll(series2);
                bc.setTitle("Stock Distribution of Total Trades");
                yAxis.setLabel("Trades x10^3");

            }});

        button7.setOnAction(e->{
            if(button7.isSelected()){
                pane2.setCenter(lineChart);
            }});
    }

    private void reduceLineChart(Map<LocalDate, Double> seriesClose, NumberAxis yAxis2, LineChart<String, Number> lineChart, XYChart.Series series3) {
        lineChart.setTitle("Stock Time Series");

        seriesClose.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(n ->{
                    series3.getData().add(new XYChart.Data(n.getKey().toString(), n.getValue()));
                });
        series3.setName("Close Price");
        yAxis2.setLabel("Stock Price");
        lineChart.getData().addAll(series3);
    }

    private void bChart2(XYChart.Series series2) {
        series2.getData().add(new XYChart.Data(Lowest, boxDisplay.getVolumeTrades("LOWEST")));
        series2.getData().add(new XYChart.Data(Low, boxDisplay.getVolumeTrades("LOW")));
        series2.getData().add(new XYChart.Data(Mid, boxDisplay.getVolumeTrades("MID")));
        series2.getData().add(new XYChart.Data(High, boxDisplay.getVolumeTrades("HIGH")));
    }

    private void bChart1(CategoryAxis xAxis, NumberAxis yAxis, BarChart<String, Number> bc, XYChart.Series series1) {
        xAxis.setLabel("Value");
        yAxis.setLabel("Data Points");
        series1.setName("Frequecy");
        series1.getData().add(new XYChart.Data(Lowest, boxDisplay.getCount("LOWEST")));
        series1.getData().add(new XYChart.Data(Low, boxDisplay.getCount("LOW")));
        series1.getData().add(new XYChart.Data(Mid, boxDisplay.getCount("MID")));
        series1.getData().add(new XYChart.Data(High, boxDisplay.getCount("HIGH")));
        bc.setTitle("Stock Distribution of Closing Date");
        xAxis.setLabel("Value");
    }

    private void createButtonG2(VBox paneForRadioButtons2, VBox paneForRadioButtons3, ToggleGroup group1, ToggleGroup group2, RadioButton button3, RadioButton button4, RadioButton button5, RadioButton button6, RadioButton button7) {
        button3.setToggleGroup(group1);
        button4.setToggleGroup(group1);
        button4.setSelected(true);
        button5.setToggleGroup(group2);
        button6.setToggleGroup(group2);
        button7.setToggleGroup(group2);
        button5.setSelected(true);
        paneForRadioButtons2.getChildren().addAll(button3, button4);
        paneForRadioButtons3.getChildren().addAll(button5, button6, button7);
    }

    private void createButtonG1(ToggleGroup group, RadioButton button1, RadioButton button2) {
        button1.setToggleGroup(group);
        button1.setSelected(true);
        button2.setToggleGroup(group);
    }

    private void setUpBorder(BorderPane border){
        HBox hBox = new HBox();
        setUpHBox(hBox);
        border.setTop(hBox);
    }

    private void setUpHBox(HBox hBox) {
        hBox.setSpacing(10);
        ComboBox<Stock> levels = boxDisplay.getCategories();
        ComboBox<StockDate> dates = boxDisplay.getStockClose();
        Text textBox = boxDisplay.getTextBox();
        hBox.getChildren().addAll(levels,dates,textBox);
        HBox.setMargin(levels, new Insets(20, 5,5, 10));
        HBox.setMargin(dates, new Insets(20, 5,5,5));
        HBox.setMargin(textBox, new Insets(20, 5,5,5));
    }

//    private void setUpBoxChart()
}
