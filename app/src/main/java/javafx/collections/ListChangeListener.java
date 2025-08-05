package javafx.collections;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/collections/ListChangeListener.class */
public interface ListChangeListener<E> {
    void onChanged(Change<? extends E> change);

    /* loaded from: jfxrt.jar:javafx/collections/ListChangeListener$Change.class */
    public static abstract class Change<E> {
        private final ObservableList<E> list;

        public abstract boolean next();

        public abstract void reset();

        public abstract int getFrom();

        public abstract int getTo();

        public abstract List<E> getRemoved();

        protected abstract int[] getPermutation();

        public Change(ObservableList<E> list) {
            this.list = list;
        }

        public ObservableList<E> getList() {
            return this.list;
        }

        public boolean wasPermutated() {
            return getPermutation().length != 0;
        }

        public boolean wasAdded() {
            return (wasPermutated() || wasUpdated() || getFrom() >= getTo()) ? false : true;
        }

        public boolean wasRemoved() {
            return !getRemoved().isEmpty();
        }

        public boolean wasReplaced() {
            return wasAdded() && wasRemoved();
        }

        public boolean wasUpdated() {
            return false;
        }

        public List<E> getAddedSubList() {
            return wasAdded() ? getList().subList(getFrom(), getTo()) : Collections.emptyList();
        }

        public int getRemovedSize() {
            return getRemoved().size();
        }

        public int getAddedSize() {
            if (wasAdded()) {
                return getTo() - getFrom();
            }
            return 0;
        }

        public int getPermutation(int i2) {
            if (!wasPermutated()) {
                throw new IllegalStateException("Not a permutation change");
            }
            return getPermutation()[i2 - getFrom()];
        }
    }
}
