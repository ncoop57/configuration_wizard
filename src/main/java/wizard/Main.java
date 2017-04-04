package wizard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        this.stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/start_scene.fxml"));
        Parent root = loader.load();

        StartController controller = (StartController) loader.getController();
        controller.setStage(primaryStage); // or what you want to do

        primaryStage.setTitle("CDEP Configuration Wizard");
        primaryStage.getIcons().add(new Image("cdep_logo_with_mario.png"));
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
