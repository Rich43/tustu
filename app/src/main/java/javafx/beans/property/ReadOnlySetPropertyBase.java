package javafx.beans.property;

import com.sun.javafx.binding.SetExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlySetPropertyBase.class */
public abstract class ReadOnlySetPropertyBase<E> extends ReadOnlySetProperty<E> {
    private SetExpressionHelper<E> helper;

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

    protected void fireValueChangedEvent() {
        SetExpressionHelper.fireValueChangedEvent(this.helper);
    }

    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        SetExpressionHelper.fireValueChangedEvent(this.helper, change);
    }
}
