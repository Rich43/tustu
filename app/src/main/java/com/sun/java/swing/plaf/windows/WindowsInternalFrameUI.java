package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameUI.class */
public class WindowsInternalFrameUI extends BasicInternalFrameUI {
    XPStyle xp;

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    public void installDefaults() {
        super.installDefaults();
        if (this.xp != null) {
            this.frame.setBorder(new XPBorder());
        } else {
            this.frame.setBorder(UIManager.getBorder("InternalFrame.border"));
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        LookAndFeel.installProperty(jComponent, "opaque", this.xp == null ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    public void uninstallDefaults() {
        this.frame.setBorder(null);
        super.uninstallDefaults();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsInternalFrameUI((JInternalFrame) jComponent);
    }

    public WindowsInternalFrameUI(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
        this.xp = XPStyle.getXP();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected DesktopManager createDesktopManager() {
        return new WindowsDesktopManager();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected JComponent createNorthPane(JInternalFrame jInternalFrame) {
        this.titlePane = new WindowsInternalFrameTitlePane(jInternalFrame);
        return this.titlePane;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameUI$XPBorder.class */
    private class XPBorder extends AbstractBorder {
        private XPStyle.Skin leftSkin;
        private XPStyle.Skin rightSkin;
        private XPStyle.Skin bottomSkin;

        private XPBorder() {
            this.leftSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMELEFT);
            this.rightSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMERIGHT);
            this.bottomSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMEBOTTOM);
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            TMSchema.State state = ((JInternalFrame) component).isSelected() ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE;
            int i6 = WindowsInternalFrameUI.this.titlePane != null ? WindowsInternalFrameUI.this.titlePane.getSize().height : 0;
            this.bottomSkin.paintSkin(graphics, 0, i5 - this.bottomSkin.getHeight(), i4, this.bottomSkin.getHeight(), state);
            this.leftSkin.paintSkin(graphics, 0, i6 - 1, this.leftSkin.getWidth(), ((i5 - i6) - this.bottomSkin.getHeight()) + 2, state);
            this.rightSkin.paintSkin(graphics, i4 - this.rightSkin.getWidth(), i6 - 1, this.rightSkin.getWidth(), ((i5 - i6) - this.bottomSkin.getHeight()) + 2, state);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.top = 4;
            insets.left = this.leftSkin.getWidth();
            insets.right = this.rightSkin.getWidth();
            insets.bottom = this.bottomSkin.getHeight();
            return insets;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
