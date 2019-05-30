import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Coffea main class
 *
 * @author: Rahul Deshpande
 * @author: Siddhant Sharma
 */
public class Coffea extends Application {
    @Override
    /**
     * Handles starting the JavaFX stage
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Coffea");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/img/coffee-cup.png")));
        stage.show();

    }
    /**
     * Launches JavaFX Music Player
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
