package javafx.beans.value;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/beans/value/ChangeListener.class */
public interface ChangeListener<T> {
    void changed(ObservableValue<? extends T> observableValue, T t2, T t3);
}
