package javafx.collections;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/collections/MapChangeListener.class */
public interface MapChangeListener<K, V> {
    void onChanged(Change<? extends K, ? extends V> change);

    /* loaded from: jfxrt.jar:javafx/collections/MapChangeListener$Change.class */
    public static abstract class Change<K, V> {
        private final ObservableMap<K, V> map;

        public abstract boolean wasAdded();

        public abstract boolean wasRemoved();

        public abstract K getKey();

        public abstract V getValueAdded();

        public abstract V getValueRemoved();

        public Change(ObservableMap<K, V> map) {
            this.map = map;
        }

        public ObservableMap<K, V> getMap() {
            return this.map;
        }
    }
}
