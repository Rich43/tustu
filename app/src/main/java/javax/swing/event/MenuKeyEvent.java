package javax.swing.event;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

/* loaded from: rt.jar:javax/swing/event/MenuKeyEvent.class */
public class MenuKeyEvent extends KeyEvent {
    private MenuElement[] path;
    private MenuSelectionManager manager;

    public MenuKeyEvent(Component component, int i2, long j2, int i3, int i4, char c2, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        super(component, i2, j2, i3, i4, c2);
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
