package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javax.management.JMX;

/* loaded from: jfxrt.jar:javafx/scene/control/TextInputDialog.class */
public class TextInputDialog extends Dialog<String> {
    private final GridPane grid;
    private final Label label;
    private final TextField textField;
    private final String defaultValue;

    public TextInputDialog() {
        this("");
    }

    public TextInputDialog(@NamedArg(JMX.DEFAULT_VALUE_FIELD) String defaultValue) {
        DialogPane dialogPane = getDialogPane();
        this.textField = new TextField(defaultValue);
        this.textField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.textField, Priority.ALWAYS);
        GridPane.setFillWidth(this.textField, true);
        this.label = DialogPane.createContentLabel(dialogPane.getContentText());
        this.label.setPrefWidth(-1.0d);
        this.label.textProperty().bind(dialogPane.contentTextProperty());
        this.defaultValue = defaultValue;
        this.grid = new GridPane();
        this.grid.setHgap(10.0d);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        dialogPane.contentTextProperty().addListener(o2 -> {
            updateGrid();
        });
        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        updateGrid();
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                return this.textField.getText();
            }
            return null;
        });
    }

    public final TextField getEditor() {
        return this.textField;
    }

    public final String getDefaultValue() {
        return this.defaultValue;
    }

    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.label, 0, 0);
        this.grid.add(this.textField, 1, 0);
        getDialogPane().setContent(this.grid);
        Platform.runLater(() -> {
            this.textField.requestFocus();
        });
    }
}
