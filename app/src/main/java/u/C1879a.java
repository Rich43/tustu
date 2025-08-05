package u;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.ToolTipManager;

/* renamed from: u.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:u/a.class */
class C1879a extends JButton {
    public C1879a(g gVar, c cVar) {
        super(gVar.a());
        if (gVar.b() != null) {
            super.setToolTipText(gVar.b());
        }
        addActionListener(new C1880b(this, gVar, cVar));
    }

    public void a() throws HeadlessException {
        ToolTipManager.sharedInstance().mouseMoved(new MouseEvent(this, 504, System.currentTimeMillis(), 0, 0, 0, 0, false));
    }

    public void b() throws HeadlessException {
        ToolTipManager toolTipManagerSharedInstance = ToolTipManager.sharedInstance();
        MouseEvent mouseEvent = new MouseEvent(this, 505, System.currentTimeMillis(), 0, 0, 0, 0, false);
        toolTipManagerSharedInstance.mouseMoved(mouseEvent);
        toolTipManagerSharedInstance.mouseExited(mouseEvent);
    }
}
