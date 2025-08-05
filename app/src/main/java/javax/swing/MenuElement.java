package javax.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/* loaded from: rt.jar:javax/swing/MenuElement.class */
public interface MenuElement {
    void processMouseEvent(MouseEvent mouseEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager);

    void processKeyEvent(KeyEvent keyEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager);

    void menuSelectionChanged(boolean z2);

    MenuElement[] getSubElements();

    Component getComponent();
}
