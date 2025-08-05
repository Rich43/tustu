package javafx.beans.property;

import com.sun.javafx.binding.MapExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyMapPropertyBase.class */
public abstract class ReadOnlyMapPropertyBase<K, V> extends ReadOnlyMapProperty<K, V> {
    private MapExpressionHelper<K, V> helper;

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.helper = MapExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = MapExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        this.helper = MapExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        this.helper = MapExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableMap
    public void addListener(MapChangeListener<? super K, ? super V> listener) {
        this.helper = MapExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.collections.ObservableMap
    public void removeListener(MapChangeListener<? super K, ? super V> listener) {
        this.helper = MapExpressionHelper.removeListener(this.helper, listener);
    }

    protected void fireValueChangedEvent() {
        MapExpressionHelper.fireValueChangedEvent(this.helper);
    }

    protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
        MapExpressionHelper.fireValueChangedEvent(this.helper, change);
    }
}
