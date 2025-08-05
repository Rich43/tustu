package javafx.beans.property;

import com.sun.javafx.binding.ListExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyListPropertyBase.class */
public abstract class ReadOnlyListPropertyBase<E> extends ReadOnlyListProperty<E> {
    private ListExpressionHelper<E> helper;

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

    protected void fireValueChangedEvent() {
        ListExpressionHelper.fireValueChangedEvent(this.helper);
    }

    protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        ListExpressionHelper.fireValueChangedEvent(this.helper, change);
    }
}
