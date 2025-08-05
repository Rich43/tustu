package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.SetExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/binding/SetBinding.class */
public abstract class SetBinding<E> extends SetExpression<E> implements Binding<ObservableSet<E>> {
    private ObservableSet<E> value;
    private BindingHelperObserver observer;
    private SetBinding<E>.SizeProperty size0;
    private SetBinding<E>.EmptyProperty empty0;
    private final SetChangeListener<E> setChangeListener = new SetChangeListener<E>() { // from class: javafx.beans.binding.SetBinding.1
        @Override // javafx.collections.SetChangeListener
        public void onChanged(SetChangeListener.Change<? extends E> change) {
            SetBinding.this.invalidateProperties();
            SetBinding.this.onInvalidating();
            SetExpressionHelper.fireValueChangedEvent(SetBinding.this.helper, change);
        }
    };
    private boolean valid = false;
    private SetExpressionHelper<E> helper = null;

    protected abstract ObservableSet<E> computeValue();

    @Override // javafx.beans.binding.SetExpression, javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public /* bridge */ /* synthetic */ Object getValue2() {
        return super.getValue2();
    }

    @Override // javafx.beans.binding.SetExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/binding/SetBinding$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return SetBinding.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return SetBinding.this;
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

    @Override // javafx.beans.binding.SetExpression
    public ReadOnlyBooleanProperty emptyProperty() {
        if (this.empty0 == null) {
            this.empty0 = new EmptyProperty();
        }
        return this.empty0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/binding/SetBinding$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return SetBinding.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return SetBinding.this;
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
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super ObservableSet<E>> listener) {
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super ObservableSet<E>> listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableSet
    public void addListener(SetChangeListener<? super E> listener) {
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.collections.ObservableSet
    public void removeListener(SetChangeListener<? super E> listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
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
    public final ObservableSet<E> get() {
        if (!this.valid) {
            this.value = computeValue();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.setChangeListener);
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
                this.value.removeListener(this.setChangeListener);
            }
            this.valid = false;
            invalidateProperties();
            onInvalidating();
            SetExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    @Override // javafx.beans.binding.Binding
    public final boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return this.valid ? "SetBinding [value: " + ((Object) get()) + "]" : "SetBinding [invalid]";
    }
}
