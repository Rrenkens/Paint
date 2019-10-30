import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Paint extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("paint.fxml"))));
        stage.setResizable(false);
        stage.setTitle("My paint");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
