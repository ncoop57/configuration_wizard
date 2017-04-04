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
    private TextField webhookURL;

    @FXML
    private TextField numOfRepositories;

    @FXML
    private TextField usernamesFile;

    @FXML
    private Button browseButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button finishButton;

    @FXML
    private void initialize()
    {



    }

    @FXML
    private void onButtonClickedBrowse()
    {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File fileSelected = fileChooser.showOpenDialog(this.stage);

        if (fileSelected != null)
            this.usernamesFile.setText(fileSelected.toString());

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
    private void onButtonClickedFinish() throws Exception
    {

        // Test auth token: 16e242ed7cbe49bd667b245eb7a1490f45a44054
        GitHubController gitHubController = new GitHubController(this.ownerUsername.getText(),
                                                                 this.personalAccessToken.getText(),
                                                                 this.organizationName.getText(),
                                                                 Integer.parseInt(this.numOfRepositories.getText()),
                                                                 this.webhookURL.getText());

        gitHubController.removeRepos();
        gitHubController.removeTeams();

        System.exit(0);

    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

}
