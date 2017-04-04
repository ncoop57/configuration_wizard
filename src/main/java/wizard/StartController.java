package wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Created by natha on 4/4/2017.
 */
public class StartController implements IController
{

    private Stage stage;

    @FXML
    private RadioButton setUpRadio;

    @FXML
    private RadioButton tearDownRadio;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private Button cancelButton;

    @FXML
    private Button helpButton;

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
    private void onButtonClickedHelp()
    {

        HelpBox.display("Hello and welcome to the CDEP configuration wizard.\n\n"
                + "Some helpful tips to get you started are to make sure\n"
                + "you fill out all of the fields displayed on the screen\n"
                + "except for the template field which is optional.\n\n"
                + "For the project folder location field make sure you\n"
                + "select the root directory of the repository you wish\n"
                + "to be tested");

    }

    @FXML
    private void onButtonClickedNext() throws Exception
    {

        FXMLLoader loader = null;

        if (this.setUpRadio.isSelected())
            loader = new FXMLLoader(getClass().getResource("/setup_scene.fxml"));
        else
            loader = new FXMLLoader(getClass().getResource("/teardown_scene.fxml"));

        Parent root = loader.load();

        IController controller = loader.getController();
        controller.setStage(this.stage);

        this.stage.getScene().setRoot(root);

    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

}
