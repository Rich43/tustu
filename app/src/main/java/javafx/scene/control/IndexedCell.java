package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.css.PseudoClass;

/* loaded from: jfxrt.jar:javafx/scene/control/IndexedCell.class */
public class IndexedCell<T> extends Cell<T> {
    private ReadOnlyIntegerWrapper index = new ReadOnlyIntegerWrapper(this, "index", -1) { // from class: javafx.scene.control.IndexedCell.1
        @Override // javafx.beans.property.IntegerPropertyBase
        protected void invalidated() {
            boolean active = get() % 2 == 0;
            IndexedCell.this.pseudoClassStateChanged(IndexedCell.PSEUDO_CLASS_EVEN, active);
            IndexedCell.this.pseudoClassStateChanged(IndexedCell.PSEUDO_CLASS_ODD, !active);
        }
    };
    private static final String DEFAULT_STYLE_CLASS = "indexed-cell";
    private static final PseudoClass PSEUDO_CLASS_ODD = PseudoClass.getPseudoClass("odd");
    private static final PseudoClass PSEUDO_CLASS_EVEN = PseudoClass.getPseudoClass("even");

    public IndexedCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
    }

    public final int getIndex() {
        return this.index.get();
    }

    public final ReadOnlyIntegerProperty indexProperty() {
        return this.index.getReadOnlyProperty();
    }

    public void updateIndex(int i2) {
        int oldIndex = this.index.get();
        this.index.set(i2);
        indexChanged(oldIndex, i2);
    }

    void indexChanged(int oldIndex, int newIndex) {
    }
}
