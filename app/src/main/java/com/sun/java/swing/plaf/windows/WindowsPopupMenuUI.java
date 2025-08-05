package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.MenuSelectionManager;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import sun.swing.StringUIClientPropertyKey;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsPopupMenuUI.class */
public class WindowsPopupMenuUI extends BasicPopupMenuUI {
    static MnemonicListener mnemonicListener = null;
    static final Object GUTTER_OFFSET_KEY = new StringUIClientPropertyKey("GUTTER_OFFSET_KEY");

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsPopupMenuUI();
    }

    @Override // javax.swing.plaf.basic.BasicPopupMenuUI
    public void installListeners() {
        super.installListeners();
        if (!UIManager.getBoolean("Button.showMnemonics") && mnemonicListener == null) {
            mnemonicListener = new MnemonicListener();
            MenuSelectionManager.defaultManager().addChangeListener(mnemonicListener);
        }
    }

    @Override // javax.swing.plaf.PopupMenuUI
    public Popup getPopup(JPopupMenu jPopupMenu, int i2, int i3) {
        return PopupFactory.getSharedInstance().getPopup(jPopupMenu.getInvoker(), jPopupMenu, i2, i3);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsPopupMenuUI$MnemonicListener.class */
    static class MnemonicListener implements ChangeListener {
        JRootPane repaintRoot = null;

        MnemonicListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            Object[] selectedPath = ((MenuSelectionManager) changeEvent.getSource()).getSelectedPath();
            if (selectedPath.length == 0) {
                if (!WindowsLookAndFeel.isMnemonicHidden()) {
                    WindowsLookAndFeel.setMnemonicHidden(true);
                    if (this.repaintRoot != null) {
                        WindowsGraphicsUtils.repaintMnemonicsInWindow(SwingUtilities.getWindowAncestor(this.repaintRoot));
                        return;
                    }
                    return;
                }
                return;
            }
            Component invoker = (Component) selectedPath[0];
            if (invoker instanceof JPopupMenu) {
                invoker = ((JPopupMenu) invoker).getInvoker();
            }
            this.repaintRoot = SwingUtilities.getRootPane(invoker);
        }
    }

    static int getTextOffset(JComponent jComponent) {
        int i2 = -1;
        Object clientProperty = jComponent.getClientProperty(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET);
        if (clientProperty instanceof Integer) {
            int iIntValue = ((Integer) clientProperty).intValue();
            int x2 = 0;
            Component component = jComponent.getComponent(0);
            if (component != null) {
                x2 = component.getX();
            }
            i2 = iIntValue + x2;
        }
        return i2;
    }

    static int getSpanBeforeGutter() {
        return 3;
    }

    static int getSpanAfterGutter() {
        return 3;
    }

    static int getGutterWidth() {
        int width = 2;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            width = xp.getSkin(null, TMSchema.Part.MP_POPUPGUTTER).getWidth();
        }
        return width;
    }

    private static boolean isLeftToRight(JComponent jComponent) {
        boolean zIsLeftToRight = true;
        for (int componentCount = jComponent.getComponentCount() - 1; componentCount >= 0 && zIsLeftToRight; componentCount--) {
            zIsLeftToRight = jComponent.getComponent(componentCount).getComponentOrientation().isLeftToRight();
        }
        return zIsLeftToRight;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        XPStyle xp = XPStyle.getXP();
        if (WindowsMenuItemUI.isVistaPainting(xp)) {
            xp.getSkin(jComponent, TMSchema.Part.MP_POPUPBACKGROUND).paintSkin(graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), TMSchema.State.NORMAL);
            int textOffset = getTextOffset(jComponent);
            if (textOffset >= 0 && isLeftToRight(jComponent)) {
                XPStyle.Skin skin = xp.getSkin(jComponent, TMSchema.Part.MP_POPUPGUTTER);
                int gutterWidth = getGutterWidth();
                int spanAfterGutter = (textOffset - getSpanAfterGutter()) - gutterWidth;
                jComponent.putClientProperty(GUTTER_OFFSET_KEY, Integer.valueOf(spanAfterGutter));
                Insets insets = jComponent.getInsets();
                skin.paintSkin(graphics, spanAfterGutter, insets.top, gutterWidth, (jComponent.getHeight() - insets.bottom) - insets.top, TMSchema.State.NORMAL);
                return;
            }
            if (jComponent.getClientProperty(GUTTER_OFFSET_KEY) != null) {
                jComponent.putClientProperty(GUTTER_OFFSET_KEY, null);
                return;
            }
            return;
        }
        super.paint(graphics, jComponent);
    }
}
