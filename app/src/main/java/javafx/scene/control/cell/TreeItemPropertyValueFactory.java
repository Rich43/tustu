package javafx.scene.control.cell;

import com.sun.javafx.property.PropertyReference;
import com.sun.javafx.scene.control.Logging;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/TreeItemPropertyValueFactory.class */
public class TreeItemPropertyValueFactory<S, T> implements Callback<TreeTableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
    private final String property;
    private Class<?> columnClass;
    private String previousProperty;
    private PropertyReference<T> propertyRef;

    public TreeItemPropertyValueFactory(@NamedArg("property") String property) {
        this.property = property;
    }

    @Override // javafx.util.Callback
    public ObservableValue<T> call(TreeTableColumn.CellDataFeatures<S, T> param) {
        TreeItem<S> treeItem = param.getValue();
        return getCellDataReflectively(treeItem.getValue());
    }

    public final String getProperty() {
        return this.property;
    }

    private ObservableValue<T> getCellDataReflectively(S rowData) {
        if (getProperty() == null || getProperty().isEmpty() || rowData == null) {
            return null;
        }
        try {
            if (this.columnClass == null || this.previousProperty == null || !this.columnClass.equals(rowData.getClass()) || !this.previousProperty.equals(getProperty())) {
                this.columnClass = rowData.getClass();
                this.previousProperty = getProperty();
                this.propertyRef = new PropertyReference<>(rowData.getClass(), getProperty());
            }
            return this.propertyRef.getProperty(rowData);
        } catch (IllegalStateException e2) {
            try {
                T value = this.propertyRef.get(rowData);
                return new ReadOnlyObjectWrapper(value);
            } catch (IllegalStateException e3) {
                PlatformLogger logger = Logging.getControlsLogger();
                if (logger.isLoggable(PlatformLogger.Level.WARNING)) {
                    logger.finest("Can not retrieve property '" + getProperty() + "' in TreeItemPropertyValueFactory: " + ((Object) this) + " with provided class type: " + ((Object) rowData.getClass()), e2);
                    return null;
                }
                return null;
            }
        }
    }
}
