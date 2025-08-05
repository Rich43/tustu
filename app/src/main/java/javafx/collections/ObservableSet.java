package javafx.collections;

import java.util.Set;
import javafx.beans.Observable;

/* loaded from: jfxrt.jar:javafx/collections/ObservableSet.class */
public interface ObservableSet<E> extends Set<E>, Observable {
    void addListener(SetChangeListener<? super E> setChangeListener);

    void removeListener(SetChangeListener<? super E> setChangeListener);
}
