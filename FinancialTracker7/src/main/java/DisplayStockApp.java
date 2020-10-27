import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockpkg.Stock;
import stockpkg.StockDate;
import views.StockDisplay;

public class DisplayStockApp extends Application {

    private StockDisplay boxDisplay;

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {   ///stage var is here!
        boxDisplay = new StockDisplay("monthly", "FB");

        BorderPane pane = new BorderPane();
        setUpBorder(pane);

        Scene scene = new Scene(pane, 600, 300);
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
        ComboBox<StockDate> dates = boxDisplay.getStockWk();
        Text textBox = boxDisplay.getTextBox();
        hBox.getChildren().addAll(levels,dates,textBox);
        HBox.setMargin(levels, new Insets(20, 5,5, 10));
        HBox.setMargin(dates, new Insets(20, 5,5,5));
        HBox.setMargin(textBox, new Insets(20, 5,5,5));
    }
}
