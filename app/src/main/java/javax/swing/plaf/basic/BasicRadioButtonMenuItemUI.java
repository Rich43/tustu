package javax.swing.plaf.basic;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonMenuItemUI.class */
public class BasicRadioButtonMenuItemUI extends BasicMenuItemUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicRadioButtonMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected String getPropertyPrefix() {
        return "RadioButtonMenuItem";
    }

    public void processMouseEvent(JMenuItem jMenuItem, MouseEvent mouseEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        Point point = mouseEvent.getPoint();
        if (point.f12370x >= 0 && point.f12370x < jMenuItem.getWidth() && point.f12371y >= 0 && point.f12371y < jMenuItem.getHeight()) {
            if (mouseEvent.getID() == 502) {
                menuSelectionManager.clearSelectedPath();
                jMenuItem.doClick(0);
                jMenuItem.setArmed(false);
                return;
            }
            menuSelectionManager.setSelectedPath(menuElementArr);
            return;
        }
        if (jMenuItem.getModel().isArmed()) {
            MenuElement[] menuElementArr2 = new MenuElement[menuElementArr.length - 1];
            int length = menuElementArr.length - 1;
            for (int i2 = 0; i2 < length; i2++) {
                menuElementArr2[i2] = menuElementArr[i2];
            }
            menuSelectionManager.setSelectedPath(menuElementArr2);
        }
    }
}
