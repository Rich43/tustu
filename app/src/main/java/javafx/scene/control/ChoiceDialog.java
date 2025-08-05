package javafx.scene.control;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/* loaded from: jfxrt.jar:javafx/scene/control/ChoiceDialog.class */
public class ChoiceDialog<T> extends Dialog<T> {
    private final GridPane grid;
    private final Label label;
    private final ComboBox<T> comboBox;
    private final T defaultChoice;

    public ChoiceDialog() {
        this((Object) null, (Object[]) null);
    }

    public ChoiceDialog(T defaultChoice, T... choices) {
        this(defaultChoice, choices == null ? Collections.emptyList() : Arrays.asList(choices));
    }

    public ChoiceDialog(T defaultChoice, Collection<T> choices) {
        DialogPane dialogPane = getDialogPane();
        this.grid = new GridPane();
        this.grid.setHgap(10.0d);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        this.label = DialogPane.createContentLabel(dialogPane.getContentText());
        this.label.setPrefWidth(-1.0d);
        this.label.textProperty().bind(dialogPane.contentTextProperty());
        dialogPane.contentTextProperty().addListener(o2 -> {
            updateGrid();
        });
        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("choice-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.comboBox = new ComboBox<>();
        this.comboBox.setMinWidth(150.0d);
        if (choices != null) {
            this.comboBox.getItems().addAll((Collection<? extends T>) choices);
        }
        this.comboBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.comboBox, Priority.ALWAYS);
        GridPane.setFillWidth(this.comboBox, true);
        this.defaultChoice = this.comboBox.getItems().contains(defaultChoice) ? defaultChoice : null;
        if (defaultChoice == null) {
            this.comboBox.getSelectionModel().selectFirst();
        } else {
            this.comboBox.getSelectionModel().select((SingleSelectionModel<T>) defaultChoice);
        }
        updateGrid();
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                return getSelectedItem();
            }
            return null;
        });
    }

    public final T getSelectedItem() {
        return this.comboBox.getSelectionModel().getSelectedItem();
    }

    public final ReadOnlyObjectProperty<T> selectedItemProperty() {
        return this.comboBox.getSelectionModel().selectedItemProperty();
    }

    public final void setSelectedItem(T item) {
        this.comboBox.getSelectionModel().select((SingleSelectionModel<T>) item);
    }

    public final ObservableList<T> getItems() {
        return this.comboBox.getItems();
    }

    public final T getDefaultChoice() {
        return this.defaultChoice;
    }

    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.label, 0, 0);
        this.grid.add(this.comboBox, 1, 0);
        getDialogPane().setContent(this.grid);
        Platform.runLater(() -> {
            this.comboBox.requestFocus();
        });
    }
}
