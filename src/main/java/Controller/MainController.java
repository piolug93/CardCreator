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

public class MainController {
    @FXML private VBox editContent;
    @FXML private TextField keyTemplate;
    @FXML private TextField valueTemplate;
    @FXML private MenuItem menuOpenDatabase;
    @FXML private Button buttonUpdate;
    @FXML private ScrollPane manualPanel;
    @FXML private ProgressIndicator progress;
    @FXML private TextField tableTemplate;
    private File database;

    @FXML
    public void buttonAddAction(ActionEvent e) {
        editContent.getChildren().add(new FieldValueEditor(keyTemplate.getText(), valueTemplate.getText(), editContent));
    }

    @FXML
    private void openDatabase(ActionEvent e) {
        openDialogSelectDatabase();
    }

    @FXML
    private void buttonUpdateAction(ActionEvent e) {

        if(!isListQueryEmpty()) {
            inProcessing(true);
            sendAllQueryToBatch();
            DriverConnection.execute();
            inProcessing(false);
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

    private void openDialogSelectDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db", "*.sqlite", "*.sqlite3", "*.db3"));
        database = fileChooser.showOpenDialog(menuOpenDatabase.getParentPopup().getScene().getWindow());
        if(database != null) {
            connectionToDatabase();
        }
    }

    private void connectionToDatabase() {
        DriverConnection.connect(database.getPath());
        buttonUpdate.setDisable(false);
    }

    private void inProcessing(boolean inProcessing) {
        manualPanel.setDisable(inProcessing);
        progress.setVisible(inProcessing);
    }

    private void sendAllQueryToBatch() {
        for(Node element : editContent.getChildren()) {
            FieldValueEditor node = (FieldValueEditor) element;
            node.update(tableTemplate.getText());
        }
    }

    private boolean isListQueryEmpty() {
        return editContent.getChildren().isEmpty();
    }
}
