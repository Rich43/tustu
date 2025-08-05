package javafx.scene.control.cell;

import java.util.Map;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/MapValueFactory.class */
public class MapValueFactory<T> implements Callback<TableColumn.CellDataFeatures<Map, T>, ObservableValue<T>> {
    private final Object key;

    public MapValueFactory(@NamedArg("key") Object key) {
        this.key = key;
    }

    @Override // javafx.util.Callback
    public ObservableValue<T> call(TableColumn.CellDataFeatures<Map, T> cdf) {
        Map map = cdf.getValue();
        Object value = map.get(this.key);
        if (value instanceof ObservableValue) {
            return (ObservableValue) value;
        }
        if (value instanceof Boolean) {
            return new ReadOnlyBooleanWrapper(((Boolean) value).booleanValue());
        }
        if (value instanceof Integer) {
            return new ReadOnlyIntegerWrapper(((Integer) value).intValue());
        }
        if (value instanceof Float) {
            return new ReadOnlyFloatWrapper(((Float) value).floatValue());
        }
        if (value instanceof Long) {
            return new ReadOnlyLongWrapper(((Long) value).longValue());
        }
        if (value instanceof Double) {
            return new ReadOnlyDoubleWrapper(((Double) value).doubleValue());
        }
        if (value instanceof String) {
            return new ReadOnlyStringWrapper((String) value);
        }
        return new ReadOnlyObjectWrapper(value);
    }
}
