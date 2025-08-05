package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ProgressBarTableCell.class */
public class ProgressBarTableCell<S> extends TableCell<S, Double> {
    private final ProgressBar progressBar;
    private ObservableValue<Double> observable;

    public static <S> Callback<TableColumn<S, Double>, TableCell<S, Double>> forTableColumn() {
        return param -> {
            return new ProgressBarTableCell();
        };
    }

    public ProgressBarTableCell() {
        getStyleClass().add("progress-bar-table-cell");
        this.progressBar = new ProgressBar();
        this.progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(Double item, boolean empty) {
        super.updateItem((ProgressBarTableCell<S>) item, empty);
        if (empty) {
            setGraphic(null);
            return;
        }
        this.progressBar.progressProperty().unbind();
        TableColumn<S, Double> column = getTableColumn();
        this.observable = column == null ? null : column.getCellObservableValue(getIndex());
        if (this.observable != null) {
            this.progressBar.progressProperty().bind(this.observable);
        } else if (item != null) {
            this.progressBar.setProgress(item.doubleValue());
        }
        setGraphic(this.progressBar);
    }
}
