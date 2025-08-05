package javafx.beans.property;

import com.sun.javafx.binding.MapExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/MapPropertyBase.class */
public abstract class MapPropertyBase<K, V> extends MapProperty<K, V> {
    private ObservableMap<K, V> value;
    private MapPropertyBase<K, V>.SizeProperty size0;
    private MapPropertyBase<K, V>.EmptyProperty empty0;
    private final MapChangeListener<K, V> mapChangeListener = change -> {
        invalidateProperties();
        invalidated();
        fireValueChangedEvent(change);
    };
    private ObservableValue<? extends ObservableMap<K, V>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private MapExpressionHelper<K, V> helper = null;

    public MapPropertyBase() {
    }

    public MapPropertyBase(ObservableMap<K, V> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(this.mapChangeListener);
        }
    }

    @Override // javafx.beans.binding.MapExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/MapPropertyBase$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return MapPropertyBase.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return MapPropertyBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "size";
        }

        @Override // javafx.beans.property.ReadOnlyIntegerPropertyBase
        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.binding.MapExpression
    public ReadOnlyBooleanProperty emptyProperty() {
        if (this.empty0 == null) {
            this.empty0 = new EmptyProperty();
        }
        return this.empty0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/MapPropertyBase$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return MapPropertyBase.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return MapPropertyBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return Constants.ELEMNAME_EMPTY_STRING;
        }

        @Override // javafx.beans.property.ReadOnlyBooleanPropertyBase
        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

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

    private void invalidateProperties() {
        if (this.size0 != null) {
            this.size0.fireValueChangedEvent();
        }
        if (this.empty0 != null) {
            this.empty0.fireValueChangedEvent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markInvalid(ObservableMap<K, V> oldValue) {
        if (this.valid) {
            if (oldValue != null) {
                oldValue.removeListener(this.mapChangeListener);
            }
            this.valid = false;
            invalidateProperties();
            invalidated();
            fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public ObservableMap<K, V> get() {
        if (!this.valid) {
            this.value = this.observable == null ? this.value : this.observable.getValue2();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.mapChangeListener);
            }
        }
        return this.value;
    }

    @Override // javafx.beans.value.WritableObjectValue
    public void set(ObservableMap<K, V> newValue) {
        if (isBound()) {
            throw new RuntimeException(((getBean() == null || getName() == null) ? "" : getBean().getClass().getSimpleName() + "." + getName() + " : ") + "A bound value cannot be set.");
        }
        if (this.value != newValue) {
            ObservableMap<K, V> oldValue = this.value;
            this.value = newValue;
            markInvalid(oldValue);
        }
    }

    @Override // javafx.beans.property.Property
    public boolean isBound() {
        return this.observable != null;
    }

    @Override // javafx.beans.property.Property
    public void bind(ObservableValue<? extends ObservableMap<K, V>> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!newObservable.equals(this.observable)) {
            unbind();
            this.observable = newObservable;
            if (this.listener == null) {
                this.listener = new Listener(this);
            }
            this.observable.addListener(this.listener);
            markInvalid(this.value);
        }
    }

    @Override // javafx.beans.property.Property
    public void unbind() {
        if (this.observable != null) {
            this.value = this.observable.getValue2();
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override // javafx.beans.property.MapProperty, javafx.beans.property.ReadOnlyMapProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("MapProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (this.valid) {
                result.append("value: ").append((Object) get());
            } else {
                result.append("invalid");
            }
        } else {
            result.append("value: ").append((Object) get());
        }
        result.append("]");
        return result.toString();
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/MapPropertyBase$Listener.class */
    private static class Listener<K, V> implements InvalidationListener {
        private final WeakReference<MapPropertyBase<K, V>> wref;

        public Listener(MapPropertyBase<K, V> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            MapPropertyBase<K, V> ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid(((MapPropertyBase) ref).value);
            } else {
                observable.removeListener(this);
            }
        }
    }
}
