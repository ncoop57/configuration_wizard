package wizard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by natha on 3/29/2017.
 */
public class SetUpController implements IController
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
    private TextField prefixRepo;

    @FXML
    private TextField usernamesFile;

    @FXML
    private Button browseButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button nextButton;

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

    private ArrayList<String> getUserNames() {
        ArrayList<String> userNames = new ArrayList<>();
        try
        {
            Scanner sc = new Scanner(new File(this.usernamesFile.getText()));
            while(sc.hasNextLine()) {
                userNames.add(sc.nextLine());
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        return userNames;
    }

    @FXML
    private void onButtonClickedNext() throws Exception
    {

        ArrayList<String> usernames = this.getUserNames();

        GitHubController gitHubController = new GitHubController(this.ownerUsername.getText(),
                                                                 this.personalAccessToken.getText(),
                                                                 this.organizationName.getText(),
                                                                 Integer.parseInt(this.numOfRepositories.getText()),
                                                                 this.webhookURL.getText(),
                                                                 this.prefixRepo.getText(),
                                                                 usernames);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/config_scene.fxml"));
        Parent root = loader.load();

        Controller controller = (Controller) loader.getController();
        controller.setStage(this.stage);
        controller.setGitHubController(gitHubController);

        this.stage.getScene().setRoot(root);
    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

}
