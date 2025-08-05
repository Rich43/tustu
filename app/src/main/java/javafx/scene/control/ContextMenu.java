package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import com.sun.javafx.util.Utils;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/control/ContextMenu.class */
public class ContextMenu extends PopupControl {
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    private final ObservableList<MenuItem> items;

    @Deprecated
    private final BooleanProperty impl_showRelativeToWindow;
    private static final String DEFAULT_STYLE_CLASS = "context-menu";

    public ContextMenu() {
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ContextMenu.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ContextMenu.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ContextMenu.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
        this.items = new TrackableObservableList<MenuItem>() { // from class: javafx.scene.control.ContextMenu.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<MenuItem> c2) {
                while (c2.next()) {
                    Iterator<MenuItem> it = c2.getRemoved().iterator();
                    while (it.hasNext()) {
                        it.next().setParentPopup(null);
                    }
                    for (MenuItem item : c2.getAddedSubList()) {
                        if (item.getParentPopup() != null) {
                            item.getParentPopup().getItems().remove(item);
                        }
                        item.setParentPopup(ContextMenu.this);
                    }
                }
            }
        };
        this.impl_showRelativeToWindow = new SimpleBooleanProperty(false);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAutoHide(true);
        setConsumeAutoHidingEvents(false);
    }

    public ContextMenu(MenuItem... items) {
        this();
        this.items.addAll(items);
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final ObservableList<MenuItem> getItems() {
        return this.items;
    }

    public final boolean isImpl_showRelativeToWindow() {
        return this.impl_showRelativeToWindow.get();
    }

    public final void setImpl_showRelativeToWindow(boolean value) {
        this.impl_showRelativeToWindow.set(value);
    }

    public final BooleanProperty impl_showRelativeToWindowProperty() {
        return this.impl_showRelativeToWindow;
    }

    public void show(Node anchor, Side side, double dx, double dy) {
        if (anchor == null || getItems().size() == 0) {
            return;
        }
        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        HPos hpos = side == Side.LEFT ? HPos.LEFT : side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER;
        VPos vpos = side == Side.TOP ? VPos.TOP : side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER;
        Point2D point = Utils.pointRelativeTo(anchor, prefWidth(-1.0d), prefHeight(-1.0d), hpos, vpos, dx, dy, true);
        doShow(anchor, point.getX(), point.getY());
    }

    @Override // javafx.stage.PopupWindow
    public void show(Node anchor, double screenX, double screenY) {
        if (anchor == null || getItems().size() == 0) {
            return;
        }
        getScene().setNodeOrientation(anchor.getEffectiveNodeOrientation());
        doShow(anchor, screenX, screenY);
    }

    private void doShow(Node anchor, double screenX, double screenY) {
        Event.fireEvent(this, new Event(Menu.ON_SHOWING));
        if (isImpl_showRelativeToWindow()) {
            Scene scene = anchor == null ? null : anchor.getScene();
            Window win = scene == null ? null : scene.getWindow();
            if (win == null) {
                return;
            } else {
                super.show(win, screenX, screenY);
            }
        } else {
            super.show(anchor, screenX, screenY);
        }
        Event.fireEvent(this, new Event(Menu.ON_SHOWN));
    }

    @Override // javafx.stage.PopupWindow, javafx.stage.Window
    public void hide() {
        if (isShowing()) {
            Event.fireEvent(this, new Event(Menu.ON_HIDING));
            super.hide();
            Event.fireEvent(this, new Event(Menu.ON_HIDDEN));
        }
    }

    @Override // javafx.scene.control.PopupControl
    protected Skin<?> createDefaultSkin() {
        return new ContextMenuSkin(this);
    }
}
