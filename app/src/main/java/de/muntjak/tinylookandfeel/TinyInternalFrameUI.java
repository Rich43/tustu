package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyInternalFrameBorder;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyInternalFrameUI.class */
public class TinyInternalFrameUI extends BasicInternalFrameUI {
    private TinyInternalFrameBorder frameBorder;
    private TinyInternalFrameTitlePane titlePane;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyInternalFrameUI$TinyInternalFramePropertyChangeListener.class */
    public class TinyInternalFramePropertyChangeListener extends BasicInternalFrameUI.InternalFramePropertyChangeListener {
        private final TinyInternalFrameUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TinyInternalFramePropertyChangeListener(TinyInternalFrameUI tinyInternalFrameUI) {
            super();
            this.this$0 = tinyInternalFrameUI;
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameUI.InternalFramePropertyChangeListener, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            TinyInternalFrameUI tinyInternalFrameUI = (TinyInternalFrameUI) ((JInternalFrame) propertyChangeEvent.getSource()).getUI();
            if (propertyName.equals("JInternalFrame.isPalette")) {
                if (propertyChangeEvent.getNewValue() != null) {
                    tinyInternalFrameUI.setPalette(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                } else {
                    tinyInternalFrameUI.setPalette(false);
                }
            }
            super.propertyChange(propertyChangeEvent);
        }
    }

    public TinyInternalFrameUI(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyInternalFrameUI((JInternalFrame) jComponent);
    }

    JDesktopPane getDesktopPane(JComponent jComponent) {
        JDesktopPane jDesktopPane = null;
        Container parent = jComponent.getParent();
        while (jDesktopPane == null) {
            if (parent instanceof JDesktopPane) {
                jDesktopPane = (JDesktopPane) parent;
            } else {
                if (parent == null) {
                    break;
                }
                parent = parent.getParent();
            }
        }
        return jDesktopPane;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.frameBorder = new TinyInternalFrameBorder();
        this.frame.setBorder(this.frameBorder);
        this.frame.setOpaque(false);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected PropertyChangeListener createPropertyChangeListener() {
        return new TinyInternalFramePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected JComponent createNorthPane(JInternalFrame jInternalFrame) {
        super.createNorthPane(jInternalFrame);
        this.titlePane = new TinyInternalFrameTitlePane(jInternalFrame);
        return this.titlePane;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void activateFrame(JInternalFrame jInternalFrame) {
        super.activateFrame(jInternalFrame);
        this.frameBorder.setActive(true);
        this.titlePane.activate();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void deactivateFrame(JInternalFrame jInternalFrame) {
        super.deactivateFrame(jInternalFrame);
        this.frameBorder.setActive(false);
        this.titlePane.deactivate();
    }

    public void setPalette(boolean z2) {
        this.titlePane.setPalette(z2);
        this.frame.setBorder(this.frameBorder);
        this.frame.putClientProperty("isPalette", z2 ? Boolean.TRUE : Boolean.FALSE);
    }
}
