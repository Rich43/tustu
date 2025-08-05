package javax.swing.plaf;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/* loaded from: rt.jar:javax/swing/plaf/PopupMenuUI.class */
public abstract class PopupMenuUI extends ComponentUI {
    public boolean isPopupTrigger(MouseEvent mouseEvent) {
        return mouseEvent.isPopupTrigger();
    }

    public Popup getPopup(JPopupMenu jPopupMenu, int i2, int i3) {
        return PopupFactory.getSharedInstance().getPopup(jPopupMenu.getInvoker(), jPopupMenu, i2, i3);
    }
}
