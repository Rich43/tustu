package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsButtonUI.class */
public class WindowsButtonUI extends BasicButtonUI {
    protected int dashedRectGapX;
    protected int dashedRectGapY;
    protected int dashedRectGapWidth;
    protected int dashedRectGapHeight;
    protected Color focusColor;
    private static final Object WINDOWS_BUTTON_UI_KEY = new Object();
    private boolean defaults_initialized = false;
    private Rectangle viewRect = new Rectangle();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        WindowsButtonUI windowsButtonUI = (WindowsButtonUI) appContext.get(WINDOWS_BUTTON_UI_KEY);
        if (windowsButtonUI == null) {
            windowsButtonUI = new WindowsButtonUI();
            appContext.put(WINDOWS_BUTTON_UI_KEY, windowsButtonUI);
        }
        return windowsButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            String propertyPrefix = getPropertyPrefix();
            this.dashedRectGapX = UIManager.getInt(propertyPrefix + "dashedRectGapX");
            this.dashedRectGapY = UIManager.getInt(propertyPrefix + "dashedRectGapY");
            this.dashedRectGapWidth = UIManager.getInt(propertyPrefix + "dashedRectGapWidth");
            this.dashedRectGapHeight = UIManager.getInt(propertyPrefix + "dashedRectGapHeight");
            this.focusColor = UIManager.getColor(propertyPrefix + "focus");
            this.defaults_initialized = true;
        }
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            abstractButton.setBorder(xp.getBorder(abstractButton, getXPButtonType(abstractButton)));
            LookAndFeel.installProperty(abstractButton, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY, Boolean.TRUE);
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintText(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, String str) {
        WindowsGraphicsUtils.paintText(graphics, abstractButton, rectangle, str, getTextShiftOffset());
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        int width = abstractButton.getWidth();
        int height = abstractButton.getHeight();
        graphics.setColor(getFocusColor());
        BasicGraphicsUtils.drawDashedRect(graphics, this.dashedRectGapX, this.dashedRectGapY, width - this.dashedRectGapWidth, height - this.dashedRectGapHeight);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        setTextShiftOffset();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = super.getPreferredSize(jComponent);
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if (preferredSize != null && abstractButton.isFocusPainted()) {
            if (preferredSize.width % 2 == 0) {
                preferredSize.width++;
            }
            if (preferredSize.height % 2 == 0) {
                preferredSize.height++;
            }
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (XPStyle.getXP() != null) {
            paintXPButtonBackground(graphics, jComponent);
        }
        super.paint(graphics, jComponent);
    }

    static TMSchema.Part getXPButtonType(AbstractButton abstractButton) {
        if (abstractButton instanceof JCheckBox) {
            return TMSchema.Part.BP_CHECKBOX;
        }
        if (abstractButton instanceof JRadioButton) {
            return TMSchema.Part.BP_RADIOBUTTON;
        }
        return abstractButton.getParent() instanceof JToolBar ? TMSchema.Part.TP_BUTTON : TMSchema.Part.BP_PUSHBUTTON;
    }

    static TMSchema.State getXPButtonState(AbstractButton abstractButton) {
        TMSchema.Part xPButtonType = getXPButtonType(abstractButton);
        ButtonModel model = abstractButton.getModel();
        TMSchema.State state = TMSchema.State.NORMAL;
        switch (xPButtonType) {
            case BP_RADIOBUTTON:
            case BP_CHECKBOX:
                if (!model.isEnabled()) {
                    state = model.isSelected() ? TMSchema.State.CHECKEDDISABLED : TMSchema.State.UNCHECKEDDISABLED;
                    break;
                } else if (model.isPressed() && model.isArmed()) {
                    state = model.isSelected() ? TMSchema.State.CHECKEDPRESSED : TMSchema.State.UNCHECKEDPRESSED;
                    break;
                } else if (model.isRollover()) {
                    state = model.isSelected() ? TMSchema.State.CHECKEDHOT : TMSchema.State.UNCHECKEDHOT;
                    break;
                } else {
                    state = model.isSelected() ? TMSchema.State.CHECKEDNORMAL : TMSchema.State.UNCHECKEDNORMAL;
                    break;
                }
                break;
            case BP_PUSHBUTTON:
            case TP_BUTTON:
                if (abstractButton.getParent() instanceof JToolBar) {
                    if (model.isArmed() && model.isPressed()) {
                        state = TMSchema.State.PRESSED;
                        break;
                    } else if (!model.isEnabled()) {
                        state = TMSchema.State.DISABLED;
                        break;
                    } else if (model.isSelected() && model.isRollover()) {
                        state = TMSchema.State.HOTCHECKED;
                        break;
                    } else if (model.isSelected()) {
                        state = TMSchema.State.CHECKED;
                        break;
                    } else if (model.isRollover() || abstractButton.hasFocus()) {
                        state = TMSchema.State.HOT;
                        break;
                    }
                } else if ((model.isArmed() && model.isPressed()) || model.isSelected()) {
                    state = TMSchema.State.PRESSED;
                    break;
                } else if (!model.isEnabled()) {
                    state = TMSchema.State.DISABLED;
                    break;
                } else if (model.isRollover() || model.isPressed()) {
                    state = TMSchema.State.HOT;
                    break;
                } else if ((abstractButton instanceof JButton) && ((JButton) abstractButton).isDefaultButton()) {
                    state = TMSchema.State.DEFAULTED;
                    break;
                } else if (abstractButton.hasFocus()) {
                    state = TMSchema.State.HOT;
                    break;
                }
                break;
            default:
                state = TMSchema.State.NORMAL;
                break;
        }
        return state;
    }

    static void paintXPButtonBackground(Graphics graphics, JComponent jComponent) {
        Insets insets;
        AbstractButton abstractButton = (AbstractButton) jComponent;
        XPStyle xp = XPStyle.getXP();
        TMSchema.Part xPButtonType = getXPButtonType(abstractButton);
        if (abstractButton.isContentAreaFilled() && xp != null) {
            XPStyle.Skin skin = xp.getSkin(abstractButton, xPButtonType);
            TMSchema.State xPButtonState = getXPButtonState(abstractButton);
            Dimension size = jComponent.getSize();
            int i2 = 0;
            int i3 = 0;
            int i4 = size.width;
            int i5 = size.height;
            Border border = jComponent.getBorder();
            if (border != null) {
                insets = getOpaqueInsets(border, jComponent);
            } else {
                insets = jComponent.getInsets();
            }
            if (insets != null) {
                i2 = 0 + insets.left;
                i3 = 0 + insets.top;
                i4 -= insets.left + insets.right;
                i5 -= insets.top + insets.bottom;
            }
            skin.paintSkin(graphics, i2, i3, i4, i5, xPButtonState);
        }
    }

    private static Insets getOpaqueInsets(Border border, Component component) {
        if (border == null) {
            return null;
        }
        if (border.isBorderOpaque()) {
            return border.getBorderInsets(component);
        }
        if (border instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) border;
            Insets opaqueInsets = getOpaqueInsets(compoundBorder.getOutsideBorder(), component);
            if (opaqueInsets != null && opaqueInsets.equals(compoundBorder.getOutsideBorder().getBorderInsets(component))) {
                Insets opaqueInsets2 = getOpaqueInsets(compoundBorder.getInsideBorder(), component);
                if (opaqueInsets2 == null) {
                    return opaqueInsets;
                }
                return new Insets(opaqueInsets.top + opaqueInsets2.top, opaqueInsets.left + opaqueInsets2.left, opaqueInsets.bottom + opaqueInsets2.bottom, opaqueInsets.right + opaqueInsets2.right);
            }
            return opaqueInsets;
        }
        return null;
    }
}
