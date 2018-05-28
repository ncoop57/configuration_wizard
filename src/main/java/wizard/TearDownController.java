package wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by natha on 3/29/2017.
 */
public class TearDownController implements IController
{

    private Stage stage;

    @FXML
    private TextField personalAccessToken;

    @FXML
    private TextField ownerUsername;

    @FXML
    private TextField organizationName;

    @FXML
    private TextField numOfRepositories;

    @FXML
    private TextField prefixRepo;

    @FXML
    private Button cancelButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button finishButton;

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
    private void onButtonClickedPrevious() throws Exception
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/start_scene.fxml"));
        Parent root = loader.load();

        StartController controller = (StartController) loader.getController();
        controller.setStage(this.stage);

        this.stage.getScene().setRoot(root);

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
    private void onButtonClickedFinish() throws Exception
    {

        // Test auth token: 6eeb86a1927d17ce53b1cbe649fd42b3996cc011
        GitHubController gitHubController = new GitHubController(this.ownerUsername.getText(),
                                                                 this.personalAccessToken.getText(),
                                                                 this.organizationName.getText(),
                                                                 Integer.parseInt(this.numOfRepositories.getText()),
                                                                 "",
                                                                 this.prefixRepo.getText(),
                                                                 null);

        try
        {

            gitHubController.removeRepos();
            gitHubController.removeTeams();

        }
        catch(Exception e)
        {

            e.printStackTrace();

        }

        System.exit(0);

    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

}
