package javafx.collections;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/collections/SetChangeListener.class */
public interface SetChangeListener<E> {
    void onChanged(Change<? extends E> change);

    /* loaded from: jfxrt.jar:javafx/collections/SetChangeListener$Change.class */
    public static abstract class Change<E> {
        private ObservableSet<E> set;

        public abstract boolean wasAdded();

        public abstract boolean wasRemoved();

        public abstract E getElementAdded();

        public abstract E getElementRemoved();

        public Change(ObservableSet<E> set) {
            this.set = set;
        }

        public ObservableSet<E> getSet() {
            return this.set;
        }
    }
}
