package Controller;

import Model.DriverConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private VBox editContent;
    @FXML private TextField keyTemplate;
    @FXML private TextField valueTemplate;
    @FXML private MenuItem menuOpenDatabase;
    @FXML private Button buttonUpdate;
    @FXML private ScrollPane manualPanel;
    @FXML private ProgressIndicator progress;
    private File database;


    public MainController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void buttonAddAction(ActionEvent e) {
        editContent.getChildren().add(new FieldValueEditor(keyTemplate.getText(), valueTemplate.getText(), editContent));
    }

    @FXML
    private void openDatabase(ActionEvent e) {
        database = new FileChooser().showOpenDialog(menuOpenDatabase.getParentPopup().getScene().getWindow());
        if(database != null) {
            DriverConnection.connect(database.getPath());
            buttonUpdate.setDisable(false);
        }
    }

    @FXML
    private void buttonUpdateAction(ActionEvent e) {
        if(!editContent.getChildren().isEmpty()) {
            manualPanel.setDisable(true);
            progress.setVisible(true);
            for(Node element : editContent.getChildren()) {
                FieldValueEditor node = (FieldValueEditor) element;
                node.update();
            }
            DriverConnection.execute();
            manualPanel.setDisable(false);
            progress.setVisible(false);
            completDialog();
        }
    }

    private void completDialog() {
        Alert doneAlert = new Alert(Alert.AlertType.INFORMATION);
        doneAlert.setTitle("Update");
        doneAlert.setHeaderText("");
        doneAlert.setContentText("Update has completed");
        doneAlert.showAndWait();
    }
}
