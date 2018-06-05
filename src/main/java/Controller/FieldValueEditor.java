package Controller;

import Model.DriverConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;

public class FieldValueEditor extends FlowPane {

    @FXML private TextField keyField;
    @FXML private TextField valueField;
    @FXML private VBox editContent;

    private FieldValueEditor() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/FieldValueEditor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    FieldValueEditor(String key, String value, VBox mainContent) {
        this();
        keyField.setText(key);
        valueField.setText(value);
        this.editContent = mainContent;
    }

    @FXML
    private void removeButtonAction(ActionEvent e) {
        editContent.getChildren().remove(this);
    }

    public void update() throws SQLException{
        DriverConnection.update(keyField.getText(), valueField.getText());
    }

    public TextField getKeyField() {
        return keyField;
    }

    public TextField getValueField() {
        return valueField;
    }
}