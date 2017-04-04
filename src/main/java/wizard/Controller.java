package wizard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Controller implements IController
{

    private Pipeline pipeline;
    private Stage stage;
    private GitHubController controller;
    private ObservableList<String> languagesList = FXCollections.observableArrayList("Select a Language", "Java", "PHP");
    private ObservableList<String> stageTemplatesList = FXCollections.observableArrayList("Select a Template", "Testing", "Release");
    private ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();

    private boolean languageSelected = false;
    private boolean stagesSelected = false;

    @FXML
    private ChoiceBox languages;

    @FXML
    private TextField projectLocation;

    @FXML
    private Button browseButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button finishButton;

    @FXML
    private ChoiceBox stageTemplates;

    @FXML
    private CheckBox buildCheckBox;

    @FXML
    private CheckBox staticCheckBox;

    @FXML
    private CheckBox unitCheckBox;

    @FXML
    private CheckBox integrationCheckBox;

    @FXML
    private CheckBox stageCheckBox;

    @FXML
    private void initialize()
    {

        languages.setItems(languagesList);
        languages.setValue("Select a Language");
        languages.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {

            public void changed(ObservableValue<? extends Number> observableValue, Number oldnumber, Number newnumber)
            {

                if (newnumber.intValue() != 0)
                    languageSelected = true;

                if (newnumber.intValue() == 2)
                    buildCheckBox.setDisable(true);
                else
                    buildCheckBox.setDisable(false);

                for (CheckBox box : selectedCheckBoxes)
                    box.setSelected(false);

                selectedCheckBoxes.clear();

            }

        });

        stageTemplates.setItems(stageTemplatesList);
        stageTemplates.setValue("Select a Template");
        stageTemplates.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {

            public void changed(ObservableValue<? extends Number> observableValue, Number oldnumber, Number newnumber)
            {


                for (CheckBox box : selectedCheckBoxes)
                    box.setSelected(false);

                selectedCheckBoxes.clear();

                if (newnumber.intValue() == 1)
                {

                    staticCheckBox.setSelected(true);
                    unitCheckBox.setSelected(true);
                    integrationCheckBox.setSelected(true);

                    selectedCheckBoxes.add(staticCheckBox);
                    selectedCheckBoxes.add(unitCheckBox);
                    selectedCheckBoxes.add(integrationCheckBox);

                }
                else if (newnumber.intValue() == 2)
                {

                    if (!buildCheckBox.isDisable())
                    {

                        buildCheckBox.setSelected(true);
                        selectedCheckBoxes.add(buildCheckBox);

                    }

                    staticCheckBox.setSelected(true);
                    unitCheckBox.setSelected(true);
                    integrationCheckBox.setSelected(true);
                    stageCheckBox.setSelected(true);

                    selectedCheckBoxes.add(staticCheckBox);
                    selectedCheckBoxes.add(unitCheckBox);
                    selectedCheckBoxes.add(integrationCheckBox);
                    selectedCheckBoxes.add(stageCheckBox);

                }

                stagesSelected = true;

            }

        });

    }

    @FXML
    private void onChoiceBoxSelectedLanguages()
    {

        if (this.languages.getSelectionModel().getSelectedIndex() == 2)
            this.buildCheckBox.setDisable(true);
        else
            this.buildCheckBox.setDisable(false);

    }

    @FXML
    private void onButtonClickedBrowse()
    {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Select Project Directory");
        File directorySelected = directoryChooser.showDialog(this.stage);

        if (directorySelected != null)
            this.projectLocation.setText(directorySelected.toString());

    }

    @FXML
    private void onCheckBoxClicked(ActionEvent event)
    {

        CheckBox checkbox = (CheckBox) event.getSource();

        if (checkbox.isSelected())
            selectedCheckBoxes.add(checkbox);
        else
            selectedCheckBoxes.remove(checkbox);

        if (!selectedCheckBoxes.isEmpty())
            this.stagesSelected = true;
        else
            this.stagesSelected = false;

        System.out.println(selectedCheckBoxes.toString());

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
    private void onButtonClickedPrevious() throws Exception
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/setup_scene.fxml"));
        Parent root = loader.load();

        SetUpController controller = (SetUpController) loader.getController();
        controller.setStage(this.stage);

        this.stage.getScene().setRoot(root);

    }

    @SuppressWarnings("Since15")
    @FXML
    private void onButtonClickedFinish()
    {

        if (this.stagesSelected && this.languageSelected && !(this.projectLocation.getText().trim().isEmpty()))
        {

            List<String> stages;
            String[] tmp = new String[5];
            for (CheckBox box : this.selectedCheckBoxes)
                tmp[Integer.parseInt((String) box.getUserData())] = box.getText().split(" ")[0].toLowerCase();

            stages = new ArrayList<String>(Arrays.asList(tmp));
            stages.removeAll(Collections.singleton(null));

            this.pipeline = new Pipeline(this.languages.getSelectionModel().getSelectedItem().toString().toLowerCase(), stages);
            this.createJson(this.pipeline);

            System.exit(0);

        }

    }

    public void setStage(Stage stage)
    {

        this.stage = stage;

    }

    public void setGitHubController(GitHubController controller)
    {

        this.controller = controller;

    }

    private void createJson(Pipeline pipeline)
    {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.serializeNulls().create();

        try
        {

            PrintWriter writer = new PrintWriter(this.projectLocation.getText() + "/config.json", "UTF-8");
            writer.println(gson.toJson(pipeline));
            writer.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
