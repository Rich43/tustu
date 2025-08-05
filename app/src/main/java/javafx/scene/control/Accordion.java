package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.AccordionSkin;
import java.util.Iterator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;

/* loaded from: jfxrt.jar:javafx/scene/control/Accordion.class */
public class Accordion extends Control {
    private final ObservableList<TitledPane> panes;
    private ObjectProperty<TitledPane> expandedPane;
    private static final String DEFAULT_STYLE_CLASS = "accordion";

    public Accordion() {
        this((TitledPane[]) null);
    }

    public Accordion(TitledPane... titledPanes) {
        this.panes = new TrackableObservableList<TitledPane>() { // from class: javafx.scene.control.Accordion.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<TitledPane> c2) {
                while (c2.next()) {
                    if (c2.wasRemoved() && !Accordion.this.expandedPane.isBound()) {
                        Iterator<TitledPane> it = c2.getRemoved().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                TitledPane pane = it.next();
                                if (!c2.getAddedSubList().contains(pane) && Accordion.this.getExpandedPane() == pane) {
                                    Accordion.this.setExpandedPane(null);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
        this.expandedPane = new ObjectPropertyBase<TitledPane>() { // from class: javafx.scene.control.Accordion.2
            private TitledPane oldValue;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TitledPane value = get();
                if (value != null) {
                    value.setExpanded(true);
                } else if (this.oldValue != null) {
                    this.oldValue.setExpanded(false);
                }
                this.oldValue = value;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "expandedPane";
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Accordion.this;
            }
        };
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        if (titledPanes != null) {
            getPanes().addAll(titledPanes);
        }
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    public final void setExpandedPane(TitledPane value) {
        expandedPaneProperty().set(value);
    }

    public final TitledPane getExpandedPane() {
        return this.expandedPane.get();
    }

    public final ObjectProperty<TitledPane> expandedPaneProperty() {
        return this.expandedPane;
    }

    public final ObservableList<TitledPane> getPanes() {
        return this.panes;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new AccordionSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
