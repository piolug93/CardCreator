package Controller;

import Model.DriverConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.BatchUpdateException;
import java.sql.SQLException;

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

            try {
                sendAllQueryToBatch();
                DriverConnection.execute();
                inProcessing(false);
                completDialog();
            } catch (BatchUpdateException exception) {
                inProcessing(false);
                errorDialog(exception);
            } catch (SQLException exception) {
                inProcessing(false);
                errorDialog(exception);
            }
        }
    }

    private void completDialog() {
        Alert doneAlert = new Alert(Alert.AlertType.INFORMATION);
        doneAlert.setTitle("Update");
        doneAlert.setHeaderText("");
        doneAlert.setContentText("Update has completed");
        doneAlert.showAndWait();
    }

    private void errorDialog(SQLException exception) {
        Alert doneAlert = new Alert(Alert.AlertType.ERROR);
        doneAlert.setTitle("Error");
        doneAlert.setHeaderText(exception.getClass().toString());
        doneAlert.setContentText(exception.getMessage());
        doneAlert.showAndWait();
    }

    private void errorDialog(BatchUpdateException exception) {
        Alert doneAlert = new Alert(Alert.AlertType.ERROR);
        doneAlert.setTitle("Error");
        doneAlert.setHeaderText(exception.getMessage());
        doneAlert.setContentText(getExceptionRows(exception));
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

    private void sendAllQueryToBatch() throws SQLException{
        DriverConnection.prepareStatment(tableTemplate.getText());
        for(Node element : editContent.getChildren()) {
            FieldValueEditor node = (FieldValueEditor) element;
            node.update();
        }
    }

    private boolean isListQueryEmpty() {
        return editContent.getChildren().isEmpty();
    }

    private String getExceptionRows(BatchUpdateException exception) {
        StringBuilder errors = new StringBuilder();
        int count = 0;
        for(int error: exception.getUpdateCounts()){
            if(error==0) {
                String key = ((FieldValueEditor)(editContent.getChildren().get(count))).getKeyField().getText();
                String value = ((FieldValueEditor)(editContent.getChildren().get(count))).getValueField().getText();
                errors.append("Row in "+key+" doesn't update to: "+value).append(System.getProperty("line.separator"));
            }
            count++;
        }
        return errors.toString();
    }
}
