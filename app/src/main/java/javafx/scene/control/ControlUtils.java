package javafx.scene.control;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:javafx/scene/control/ControlUtils.class */
class ControlUtils {
    private ControlUtils() {
    }

    public static void scrollToIndex(Control control, int index) {
        Utils.executeOnceWhenPropertyIsNonNull(control.skinProperty(), skin -> {
            Event.fireEvent(control, new ScrollToEvent(control, control, ScrollToEvent.scrollToTopIndex(), Integer.valueOf(index)));
        });
    }

    public static void scrollToColumn(Control control, TableColumnBase<?, ?> column) {
        Utils.executeOnceWhenPropertyIsNonNull(control.skinProperty(), skin -> {
            control.fireEvent(new ScrollToEvent(control, control, ScrollToEvent.scrollToColumn(), column));
        });
    }

    static void requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(Control c2) {
        Scene scene = c2.getScene();
        Node focusOwner = scene == null ? null : scene.getFocusOwner();
        if (focusOwner == null) {
            c2.requestFocus();
            return;
        }
        if (!c2.equals(focusOwner)) {
            Parent parent = focusOwner.getParent();
            while (true) {
                Parent p2 = parent;
                if (p2 != null) {
                    if (c2.equals(p2)) {
                        c2.requestFocus();
                        return;
                    }
                    parent = p2.getParent();
                } else {
                    return;
                }
            }
        }
    }

    static <T> ListChangeListener.Change<T> buildClearAndSelectChange(ObservableList<T> list, final List<T> removed, final int retainedRow) {
        return new ListChangeListener.Change<T>(list) { // from class: javafx.scene.control.ControlUtils.1
            private final int removedSize;
            private final List<T> firstRemovedRange;
            private final List<T> secondRemovedRange;
            private final int[] EMPTY_PERM = new int[0];
            private boolean invalid = true;
            private boolean atFirstRange = true;
            private int from = -1;

            {
                this.removedSize = removed.size();
                int midIndex = retainedRow >= this.removedSize ? this.removedSize : retainedRow < 0 ? 0 : retainedRow;
                this.firstRemovedRange = removed.subList(0, midIndex);
                this.secondRemovedRange = removed.subList(midIndex, this.removedSize);
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getFrom() {
                checkState();
                return this.from;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getTo() {
                return getFrom();
            }

            @Override // javafx.collections.ListChangeListener.Change
            public List<T> getRemoved() {
                checkState();
                return this.atFirstRange ? this.firstRemovedRange : this.secondRemovedRange;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getRemovedSize() {
                return this.atFirstRange ? this.firstRemovedRange.size() : this.secondRemovedRange.size();
            }

            @Override // javafx.collections.ListChangeListener.Change
            protected int[] getPermutation() {
                checkState();
                return this.EMPTY_PERM;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public boolean next() {
                if (this.invalid && this.atFirstRange) {
                    this.invalid = false;
                    this.from = 0;
                    return true;
                }
                if (this.atFirstRange && !this.secondRemovedRange.isEmpty()) {
                    this.atFirstRange = false;
                    this.from = 1;
                    return true;
                }
                return false;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public void reset() {
                this.invalid = true;
                this.atFirstRange = true;
            }

            private void checkState() {
                if (this.invalid) {
                    throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
                }
            }
        };
    }
}
