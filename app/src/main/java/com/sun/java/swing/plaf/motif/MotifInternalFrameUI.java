package com.sun.java.swing.plaf.motif;

import com.sun.java.swing.plaf.motif.MotifBorders;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameUI.class */
public class MotifInternalFrameUI extends BasicInternalFrameUI {
    Color color;
    Color highlight;
    Color shadow;
    MotifInternalFrameTitlePane titlePane;

    @Deprecated
    protected KeyStroke closeMenuKey;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifInternalFrameUI((JInternalFrame) jComponent);
    }

    public MotifInternalFrameUI(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        setColors((JInternalFrame) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void installDefaults() {
        Border border = this.frame.getBorder();
        JInternalFrame jInternalFrame = this.frame;
        LayoutManager layoutManagerCreateLayoutManager = createLayoutManager();
        this.internalFrameLayout = layoutManagerCreateLayoutManager;
        jInternalFrame.setLayout(layoutManagerCreateLayoutManager);
        if (border == null || (border instanceof UIResource)) {
            this.frame.setBorder(new MotifBorders.InternalFrameBorder(this.frame));
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        this.closeMenuKey = KeyStroke.getKeyStroke(27, 0);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.frame);
        this.frame.setLayout(null);
        this.internalFrameLayout = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JInternalFrame getFrame() {
        return this.frame;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    public JComponent createNorthPane(JInternalFrame jInternalFrame) {
        this.titlePane = new MotifInternalFrameTitlePane(jInternalFrame);
        return this.titlePane;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallKeyboardActions() {
        super.uninstallKeyboardActions();
        if (isKeyBindingRegistered()) {
            JInternalFrame.JDesktopIcon desktopIcon = this.frame.getDesktopIcon();
            SwingUtilities.replaceUIActionMap(desktopIcon, null);
            SwingUtilities.replaceUIInputMap(desktopIcon, 2, null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void setupMenuOpenKey() {
        super.setupMenuOpenKey();
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(this.frame);
        if (uIActionMap != null) {
            uIActionMap.put("showSystemMenu", new AbstractAction() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameUI.1
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    MotifInternalFrameUI.this.titlePane.showSystemMenu();
                }

                @Override // javax.swing.AbstractAction, javax.swing.Action
                public boolean isEnabled() {
                    return MotifInternalFrameUI.this.isKeyBindingActive();
                }
            });
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void setupMenuCloseKey() {
        Object[] objArr;
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(this.frame);
        if (uIActionMap != null) {
            uIActionMap.put("hideSystemMenu", new AbstractAction() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameUI.2
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
                    MotifInternalFrameUI.this.titlePane.hideSystemMenu();
                }

                @Override // javax.swing.AbstractAction, javax.swing.Action
                public boolean isEnabled() {
                    return MotifInternalFrameUI.this.isKeyBindingActive();
                }
            });
        }
        JInternalFrame.JDesktopIcon desktopIcon = this.frame.getDesktopIcon();
        if (SwingUtilities.getUIInputMap(desktopIcon, 2) == null && (objArr = (Object[]) UIManager.get("DesktopIcon.windowBindings")) != null) {
            SwingUtilities.replaceUIInputMap(desktopIcon, 2, LookAndFeel.makeComponentInputMap(desktopIcon, objArr));
        }
        if (SwingUtilities.getUIActionMap(desktopIcon) == null) {
            ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
            actionMapUIResource.put("hideSystemMenu", new AbstractAction() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameUI.3
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
                    ((MotifDesktopIconUI) MotifInternalFrameUI.this.getFrame().getDesktopIcon().getUI()).hideSystemMenu();
                }

                @Override // javax.swing.AbstractAction, javax.swing.Action
                public boolean isEnabled() {
                    return MotifInternalFrameUI.this.isKeyBindingActive();
                }
            });
            SwingUtilities.replaceUIActionMap(desktopIcon, actionMapUIResource);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void activateFrame(JInternalFrame jInternalFrame) {
        super.activateFrame(jInternalFrame);
        setColors(jInternalFrame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void deactivateFrame(JInternalFrame jInternalFrame) {
        setColors(jInternalFrame);
        super.deactivateFrame(jInternalFrame);
    }

    void setColors(JInternalFrame jInternalFrame) {
        if (jInternalFrame.isSelected()) {
            this.color = UIManager.getColor("InternalFrame.activeTitleBackground");
        } else {
            this.color = UIManager.getColor("InternalFrame.inactiveTitleBackground");
        }
        this.highlight = this.color.brighter();
        this.shadow = this.color.darker().darker();
        this.titlePane.setColors(this.color, this.highlight, this.shadow);
    }
}
