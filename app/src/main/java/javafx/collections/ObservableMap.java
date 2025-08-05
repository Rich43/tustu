package javafx.collections;

import java.util.Map;
import javafx.beans.Observable;

/* loaded from: jfxrt.jar:javafx/collections/ObservableMap.class */
public interface ObservableMap<K, V> extends Map<K, V>, Observable {
    void addListener(MapChangeListener<? super K, ? super V> mapChangeListener);

    void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener);
}
