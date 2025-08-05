package javafx.beans.value;

import javafx.beans.Observable;

/* loaded from: jfxrt.jar:javafx/beans/value/ObservableValue.class */
public interface ObservableValue<T> extends Observable {
    void addListener(ChangeListener<? super T> changeListener);

    void removeListener(ChangeListener<? super T> changeListener);

    T getValue();
}
