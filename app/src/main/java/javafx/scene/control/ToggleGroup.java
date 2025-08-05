package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/scene/control/ToggleGroup.class */
public class ToggleGroup {
    private final ObservableList<Toggle> toggles = new VetoableListDecorator<Toggle>(new TrackableObservableList<Toggle>() { // from class: javafx.scene.control.ToggleGroup.1
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<Toggle> c2) {
            while (c2.next()) {
                List<Toggle> addedToggles = c2.getAddedSubList();
                for (Toggle t2 : c2.getRemoved()) {
                    if (t2.isSelected()) {
                        ToggleGroup.this.selectToggle(null);
                    }
                    if (!addedToggles.contains(t2)) {
                        t2.setToggleGroup(null);
                    }
                }
                for (Toggle t3 : addedToggles) {
                    if (!ToggleGroup.this.equals(t3.getToggleGroup())) {
                        if (t3.getToggleGroup() != null) {
                            t3.getToggleGroup().getToggles().remove(t3);
                        }
                        t3.setToggleGroup(ToggleGroup.this);
                    }
                }
                Iterator<Toggle> it = addedToggles.iterator();
                while (true) {
                    if (it.hasNext()) {
                        Toggle t4 = it.next();
                        if (t4.isSelected()) {
                            ToggleGroup.this.selectToggle(t4);
                            break;
                        }
                    }
                }
            }
        }
    }) { // from class: javafx.scene.control.ToggleGroup.2
        @Override // com.sun.javafx.collections.VetoableListDecorator
        protected void onProposedChange(List<Toggle> toBeAdded, int... indexes) {
            for (Toggle t2 : toBeAdded) {
                if (indexes[0] != 0 || indexes[1] != size()) {
                    if (ToggleGroup.this.toggles.contains(t2)) {
                        throw new IllegalArgumentException("Duplicate toggles are not allow in a ToggleGroup.");
                    }
                } else {
                    return;
                }
            }
        }
    };
    private final ReadOnlyObjectWrapper<Toggle> selectedToggle = new ReadOnlyObjectWrapper<Toggle>() { // from class: javafx.scene.control.ToggleGroup.3
        @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
        public void set(Toggle newSelectedToggle) {
            if (isBound()) {
                throw new RuntimeException("A bound value cannot be set.");
            }
            Toggle old = get();
            if (old != newSelectedToggle) {
                if (ToggleGroup.this.setSelected(newSelectedToggle, true) || ((newSelectedToggle != null && newSelectedToggle.getToggleGroup() == ToggleGroup.this) || newSelectedToggle == null)) {
                    if (old == null || old.getToggleGroup() == ToggleGroup.this || !old.isSelected()) {
                        ToggleGroup.this.setSelected(old, false);
                    }
                    super.set((AnonymousClass3) newSelectedToggle);
                }
            }
        }
    };
    private static final Object USER_DATA_KEY = new Object();
    private ObservableMap<Object, Object> properties;

    public final ObservableList<Toggle> getToggles() {
        return this.toggles;
    }

    public final void selectToggle(Toggle value) {
        this.selectedToggle.set(value);
    }

    public final Toggle getSelectedToggle() {
        return this.selectedToggle.get();
    }

    public final ReadOnlyObjectProperty<Toggle> selectedToggleProperty() {
        return this.selectedToggle.getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setSelected(Toggle toggle, boolean selected) {
        if (toggle != null && toggle.getToggleGroup() == this && !toggle.selectedProperty().isBound()) {
            toggle.setSelected(selected);
            return true;
        }
        return false;
    }

    final void clearSelectedToggle() {
        if (!this.selectedToggle.getValue2().isSelected()) {
            for (Toggle toggle : getToggles()) {
                if (toggle.isSelected()) {
                    return;
                }
            }
        }
        this.selectedToggle.set(null);
    }

    public final ObservableMap<Object, Object> getProperties() {
        if (this.properties == null) {
            this.properties = FXCollections.observableMap(new HashMap());
        }
        return this.properties;
    }

    public boolean hasProperties() {
        return (this.properties == null || this.properties.isEmpty()) ? false : true;
    }

    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
    }
}
