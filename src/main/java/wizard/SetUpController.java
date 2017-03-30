package wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by natha on 3/29/2017.
 */
public class SetUpController
{

    private Stage stage;

    @FXML
    private Button cancelButton;

    @FXML
    private Button nextButton;

    @FXML
    private void initialize()
    {



    }

    @FXML
    private void onButtonClickedCancel()
    {

        System.exit(0);

    }

    @FXML
    private void onButtonClickedNext() throws Exception
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/config_scene.fxml"));
        Parent root = loader.load();
        this.stage.getScene().setRoot(root);

    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

}
