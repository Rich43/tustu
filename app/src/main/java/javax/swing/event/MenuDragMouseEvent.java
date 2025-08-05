package javax.swing.event;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

/* loaded from: rt.jar:javax/swing/event/MenuDragMouseEvent.class */
public class MenuDragMouseEvent extends MouseEvent {
    private MenuElement[] path;
    private MenuSelectionManager manager;

    public MenuDragMouseEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, boolean z2, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        super(component, i2, j2, i3, i4, i5, i6, z2);
        this.path = menuElementArr;
        this.manager = menuSelectionManager;
    }

    public MenuDragMouseEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        super(component, i2, j2, i3, i4, i5, i6, i7, i8, z2, 0);
        this.path = menuElementArr;
        this.manager = menuSelectionManager;
    }

    public MenuElement[] getPath() {
        return this.path;
    }

    public MenuSelectionManager getMenuSelectionManager() {
        return this.manager;
    }
}
