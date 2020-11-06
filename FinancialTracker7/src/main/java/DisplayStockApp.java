import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockpkg.Stock;
import stockpkg.StockDate;
import views.StockDisplay;

public class DisplayStockApp extends Application {

    private StockDisplay boxDisplay;
    Stage stage, stage2;
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

        BorderPane pane = new BorderPane();

        setUpBorder(pane);


        Scene scene = new Scene(pane, 600, 300);

        VBox paneForRadioButtons = new VBox(20);
        paneForRadioButtons.setPadding(new Insets(5, 5,5,5));
        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton("Data Split");
        button1.setToggleGroup(group);
        button1.setSelected(true);
        RadioButton button2 = new RadioButton("Bar Chart");
        button2.setToggleGroup(group);
        paneForRadioButtons.getChildren().addAll(button1,button2);
        pane.setLeft(paneForRadioButtons);

        VBox paneForRadioButtons2 = new VBox(20);
        paneForRadioButtons2.setPadding(new Insets(5, 5,5,5));
        ToggleGroup group1 = new ToggleGroup();
        RadioButton button3 = new RadioButton("Data Split");
        button3.setToggleGroup(group1);
        RadioButton button4 = new RadioButton("Bar Chart");
        button4.setToggleGroup(group1);
        button4.setSelected(true);
        paneForRadioButtons2.getChildren().addAll(button3,button4);

        VBox paneForRadioButtons3 = new VBox(20);
        paneForRadioButtons3.setPadding(new Insets(5, 5,5,5));
        ToggleGroup group2 = new ToggleGroup();
        RadioButton button5 = new RadioButton("Count of dates");
        button5.setToggleGroup(group2);
        RadioButton button6 = new RadioButton("Total Trades");
        button6.setToggleGroup(group2);
        button5.setSelected(true);
        paneForRadioButtons3.getChildren().addAll(button5,button6);



        BorderPane pane2 = new BorderPane();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis,yAxis);
        xAxis.setLabel("Value");
        XYChart.Series series1 = new XYChart.Series();
        bc.setTitle("Stock Distribution of Closing Date");
        series1.setName("Frequecy");
        series1.getData().add(new XYChart.Data(Lowest, boxDisplay.getCount("LOWEST")));
        series1.getData().add(new XYChart.Data(Low, boxDisplay.getCount("LOW")));
        series1.getData().add(new XYChart.Data(Mid, boxDisplay.getCount("MID")));
        series1.getData().add(new XYChart.Data(High, boxDisplay.getCount("HIGH")));
        bc.setTitle("Stock Distribution of Closing Date");
        xAxis.setLabel("Value");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Total Trades");
        series2.getData().add(new XYChart.Data(Lowest, boxDisplay.getVolumeTrades("LOWEST")));
        series2.getData().add(new XYChart.Data(Low, boxDisplay.getVolumeTrades("LOW")));
        series2.getData().add(new XYChart.Data(Mid, boxDisplay.getVolumeTrades("MID")));
        series2.getData().add(new XYChart.Data(High, boxDisplay.getVolumeTrades("HIGH")));


        bc.getData().addAll(series1);
        pane2.setCenter(bc);
        pane2.setLeft(paneForRadioButtons2);
        pane2.setRight(paneForRadioButtons3);
        scene1 = new Scene(pane2, 800, 600);



        button2.setOnAction(e->{
            if(button2.isSelected()){
                stage.setScene(scene1);
                }});
        button1.setOnAction(e->{
            if(button1.isSelected()){
                stage.setScene(scene);
        }});

        button3.setOnAction(e->{
            if(button3.isSelected()){
                stage.setScene(scene);
            }});
        button4.setOnAction(e->{
            if(button4.isSelected()){
                stage.setScene(scene1);
            }});
        button5.setOnAction(e->{
            if(button5.isSelected()){
                bc.getData().clear();
                bc.getData().addAll(series1);
                bc.setTitle("Stock Distribution of Closing Date");
            }});
        button6.setOnAction(e->{
            if(button6.isSelected()){
                bc.getData().clear();
                bc.getData().addAll(series2);
                bc.setTitle("Stock Distribution of Total Trades");
            }});

        stage.setTitle("Financial Tracker");
        stage.setScene(scene);
        stage.show();

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
