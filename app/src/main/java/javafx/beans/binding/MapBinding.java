package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.MapExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/binding/MapBinding.class */
public abstract class MapBinding<K, V> extends MapExpression<K, V> implements Binding<ObservableMap<K, V>> {
    private ObservableMap<K, V> value;
    private BindingHelperObserver observer;
    private MapBinding<K, V>.SizeProperty size0;
    private MapBinding<K, V>.EmptyProperty empty0;
    private final MapChangeListener<K, V> mapChangeListener = new MapChangeListener<K, V>() { // from class: javafx.beans.binding.MapBinding.1
        @Override // javafx.collections.MapChangeListener
        public void onChanged(MapChangeListener.Change<? extends K, ? extends V> change) {
            MapBinding.this.invalidateProperties();
            MapBinding.this.onInvalidating();
            MapExpressionHelper.fireValueChangedEvent(MapBinding.this.helper, change);
        }
    };
    private boolean valid = false;
    private MapExpressionHelper<K, V> helper = null;

    protected abstract ObservableMap<K, V> computeValue();

    @Override // javafx.beans.binding.MapExpression, javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public /* bridge */ /* synthetic */ Object getValue2() {
        return super.getValue2();
    }

    @Override // javafx.beans.binding.MapExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/binding/MapBinding$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return MapBinding.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return MapBinding.this;
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

    /* loaded from: jfxrt.jar:javafx/beans/binding/MapBinding$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return MapBinding.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return MapBinding.this;
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

    protected final void bind(Observable... dependencies) {
        if (dependencies != null && dependencies.length > 0) {
            if (this.observer == null) {
                this.observer = new BindingHelperObserver(this);
            }
            for (Observable dep : dependencies) {
                if (dep != null) {
                    dep.addListener(this.observer);
                }
            }
        }
    }

    protected final void unbind(Observable... dependencies) {
        if (this.observer != null) {
            for (Observable dep : dependencies) {
                if (dep != null) {
                    dep.removeListener(this.observer);
                }
            }
            this.observer = null;
        }
    }

    @Override // javafx.beans.binding.Binding
    public void dispose() {
    }

    @Override // javafx.beans.binding.Binding
    public ObservableList<?> getDependencies() {
        return FXCollections.emptyObservableList();
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public final ObservableMap<K, V> get() {
        if (!this.valid) {
            this.value = computeValue();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.mapChangeListener);
            }
        }
        return this.value;
    }

    protected void onInvalidating() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateProperties() {
        if (this.size0 != null) {
            this.size0.fireValueChangedEvent();
        }
        if (this.empty0 != null) {
            this.empty0.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.binding.Binding
    public final void invalidate() {
        if (this.valid) {
            if (this.value != null) {
                this.value.removeListener(this.mapChangeListener);
            }
            this.valid = false;
            invalidateProperties();
            onInvalidating();
            MapExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    @Override // javafx.beans.binding.Binding
    public final boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return this.valid ? "MapBinding [value: " + ((Object) get()) + "]" : "MapBinding [invalid]";
    }
}
