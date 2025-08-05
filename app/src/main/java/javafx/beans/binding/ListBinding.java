package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.ListExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/ListBinding.class */
public abstract class ListBinding<E> extends ListExpression<E> implements Binding<ObservableList<E>> {
    private ObservableList<E> value;
    private BindingHelperObserver observer;
    private ListBinding<E>.SizeProperty size0;
    private ListBinding<E>.EmptyProperty empty0;
    private final ListChangeListener<E> listChangeListener = new ListChangeListener<E>() { // from class: javafx.beans.binding.ListBinding.1
        @Override // javafx.collections.ListChangeListener
        public void onChanged(ListChangeListener.Change<? extends E> change) {
            ListBinding.this.invalidateProperties();
            ListBinding.this.onInvalidating();
            ListExpressionHelper.fireValueChangedEvent(ListBinding.this.helper, change);
        }
    };
    private boolean valid = false;
    private ListExpressionHelper<E> helper = null;

    protected abstract ObservableList<E> computeValue();

    @Override // javafx.beans.binding.ListExpression, javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public /* bridge */ /* synthetic */ Object getValue2() {
        return super.getValue2();
    }

    @Override // javafx.beans.binding.ListExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/binding/ListBinding$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return ListBinding.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ListBinding.this;
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

    @Override // javafx.beans.binding.ListExpression
    public ReadOnlyBooleanProperty emptyProperty() {
        if (this.empty0 == null) {
            this.empty0 = new EmptyProperty();
        }
        return this.empty0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/binding/ListBinding$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return ListBinding.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ListBinding.this;
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
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super ObservableList<E>> listener) {
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super ObservableList<E>> listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableList
    public void addListener(ListChangeListener<? super E> listener) {
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.collections.ObservableList
    public void removeListener(ListChangeListener<? super E> listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
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
    public final ObservableList<E> get() {
        if (!this.valid) {
            this.value = computeValue();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.listChangeListener);
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
                this.value.removeListener(this.listChangeListener);
            }
            this.valid = false;
            invalidateProperties();
            onInvalidating();
            ListExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    @Override // javafx.beans.binding.Binding
    public final boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return this.valid ? "ListBinding [value: " + ((Object) get()) + "]" : "ListBinding [invalid]";
    }
}
