package wizard;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by natha on 3/11/2017.
 */
public class HelpBox
{

    public static void display(String message)
    {

        Stage stage = new Stage();
        stage.setTitle("Wizard Helper");
        stage.getIcons().add(new Image("help_icon.png"));

        Label label = new Label(message);

        Button button = new Button("Done");
        button.setOnAction(e -> stage.close());

        VBox scene = new VBox();
        scene.setAlignment(Pos.CENTER);
        scene.getChildren().addAll(label, button);


        stage.setScene(new Scene(scene, 400, 250));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();

    }

}
