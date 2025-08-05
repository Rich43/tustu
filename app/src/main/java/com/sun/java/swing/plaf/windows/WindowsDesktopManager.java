package com.sun.java.swing.plaf.windows;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import javax.swing.DefaultDesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsDesktopManager.class */
public class WindowsDesktopManager extends DefaultDesktopManager implements Serializable, UIResource {
    private WeakReference<JInternalFrame> currentFrameRef;

    @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
    public void activateFrame(JInternalFrame jInternalFrame) {
        JInternalFrame jInternalFrame2 = this.currentFrameRef != null ? this.currentFrameRef.get() : null;
        try {
            super.activateFrame(jInternalFrame);
            if (jInternalFrame2 != null && jInternalFrame != jInternalFrame2) {
                if (jInternalFrame2.isMaximum() && jInternalFrame.getClientProperty("JInternalFrame.frameType") != "optionDialog" && !jInternalFrame2.isIcon()) {
                    jInternalFrame2.setMaximum(false);
                    if (jInternalFrame.isMaximizable()) {
                        if (!jInternalFrame.isMaximum()) {
                            jInternalFrame.setMaximum(true);
                        } else if (jInternalFrame.isMaximum() && jInternalFrame.isIcon()) {
                            jInternalFrame.setIcon(false);
                        } else {
                            jInternalFrame.setMaximum(false);
                        }
                    }
                }
                if (jInternalFrame2.isSelected()) {
                    jInternalFrame2.setSelected(false);
                }
            }
            if (!jInternalFrame.isSelected()) {
                jInternalFrame.setSelected(true);
            }
        } catch (PropertyVetoException e2) {
        }
        if (jInternalFrame != jInternalFrame2) {
            this.currentFrameRef = new WeakReference<>(jInternalFrame);
        }
    }
}
