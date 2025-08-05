package ao;

import java.awt.Container;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

/* renamed from: ao.fc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fc.class */
public class C0746fc extends JTextField {
    public C0746fc() {
        this("");
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public C0746fc(String str) {
        this(str, 0);
    }

    public C0746fc(String str, int i2) {
        super(str, i2);
        enableEvents(8L);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processKeyEvent(KeyEvent keyEvent) {
        if ((keyEvent.getID() == 401 && (keyEvent.getKeyChar() == '\n' || keyEvent.getKeyChar() == 127)) || keyEvent.getKeyChar() == '\b') {
            super.processKeyEvent(keyEvent);
            return;
        }
        if ("0987654321-".indexOf(keyEvent.getKeyChar()) == -1 || keyEvent.getKeyChar() == '\n') {
            Container parent = getParent();
            if (parent != null) {
                parent.dispatchEvent(keyEvent);
            }
            keyEvent.consume();
        }
    }
}
