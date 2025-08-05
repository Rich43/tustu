package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ProgressBarTreeTableCell.class */
public class ProgressBarTreeTableCell<S> extends TreeTableCell<S, Double> {
    private final ProgressBar progressBar;
    private ObservableValue<Double> observable;

    public static <S> Callback<TreeTableColumn<S, Double>, TreeTableCell<S, Double>> forTreeTableColumn() {
        return param -> {
            return new ProgressBarTreeTableCell();
        };
    }

    public ProgressBarTreeTableCell() {
        getStyleClass().add("progress-bar-tree-table-cell");
        this.progressBar = new ProgressBar();
        this.progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(Double item, boolean empty) {
        super.updateItem((ProgressBarTreeTableCell<S>) item, empty);
        if (empty) {
            setGraphic(null);
            return;
        }
        this.progressBar.progressProperty().unbind();
        TreeTableColumn<S, Double> column = getTableColumn();
        this.observable = column == null ? null : column.getCellObservableValue(getIndex());
        if (this.observable != null) {
            this.progressBar.progressProperty().bind(this.observable);
        } else if (item != null) {
            this.progressBar.setProgress(item.doubleValue());
        }
        setGraphic(this.progressBar);
    }
}
