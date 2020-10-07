package stockPkg;


import api.finAPI;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

public class DisplayDataApp extends Application {

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {   ///stage var is here!
    final ComboBox<String> categories = new ComboBox<>();
    categories.setPromptText("--Select a value--");

    final ComboBox<DateClass> closeWk = new ComboBox<>();
    closeWk.setVisible(false);

    ////I have to add boxes to select API
    finAPI obj1=new finAPI("monthly", "FB", System.getenv("API-KEY"));
    Map<String, List<DateClass>> hMap = obj1.getAPI(obj1);

    ObservableList<String> close = FXCollections.observableArrayList(hMap.keySet());
    categories.getItems().addAll(close);

    categories.valueProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            closeWk.getItems().addAll(hMap.get(newValue));
            closeWk.setVisible(true);
        }
    });

    BorderPane pane = new BorderPane();
    pane.setTop(categories);
    pane.setCenter(closeWk);
    Scene scene = new Scene(pane, 300, 300);
    stage.setScene(scene);
    stage.setTitle("Financial Tracker");
    stage.show();

    }
}
