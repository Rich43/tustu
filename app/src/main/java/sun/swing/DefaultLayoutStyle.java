package sun.swing;

import java.awt.Container;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:sun/swing/DefaultLayoutStyle.class */
public class DefaultLayoutStyle extends LayoutStyle {
    private static final DefaultLayoutStyle INSTANCE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DefaultLayoutStyle.class.desiredAssertionStatus();
        INSTANCE = new DefaultLayoutStyle();
    }

    public static LayoutStyle getInstance() {
        return INSTANCE;
    }

    @Override // javax.swing.LayoutStyle
    public int getPreferredGap(JComponent jComponent, JComponent jComponent2, LayoutStyle.ComponentPlacement componentPlacement, int i2, Container container) {
        int indent;
        if (jComponent == null || jComponent2 == null || componentPlacement == null) {
            throw new NullPointerException();
        }
        checkPosition(i2);
        if (componentPlacement != LayoutStyle.ComponentPlacement.INDENT || (!(i2 == 3 || i2 == 7) || (indent = getIndent(jComponent, i2)) <= 0)) {
            return componentPlacement == LayoutStyle.ComponentPlacement.UNRELATED ? 12 : 6;
        }
        return indent;
    }

    @Override // javax.swing.LayoutStyle
    public int getContainerGap(JComponent jComponent, int i2, Container container) {
        if (jComponent == null) {
            throw new NullPointerException();
        }
        checkPosition(i2);
        return 6;
    }

    protected boolean isLabelAndNonlabel(JComponent jComponent, JComponent jComponent2, int i2) {
        if (i2 == 3 || i2 == 7) {
            boolean z2 = jComponent instanceof JLabel;
            boolean z3 = jComponent2 instanceof JLabel;
            return (z2 || z3) && z2 != z3;
        }
        return false;
    }

    protected int getButtonGap(JComponent jComponent, JComponent jComponent2, int i2, int i3) {
        int buttonGap = i3 - getButtonGap(jComponent, i2);
        if (buttonGap > 0) {
            buttonGap -= getButtonGap(jComponent2, flipDirection(i2));
        }
        if (buttonGap < 0) {
            return 0;
        }
        return buttonGap;
    }

    protected int getButtonGap(JComponent jComponent, int i2, int i3) {
        return Math.max(i3 - getButtonGap(jComponent, i2), 0);
    }

    public int getButtonGap(JComponent jComponent, int i2) {
        String uIClassID = jComponent.getUIClassID();
        if ((uIClassID == "CheckBoxUI" || uIClassID == "RadioButtonUI") && !((AbstractButton) jComponent).isBorderPainted() && (jComponent.getBorder() instanceof UIResource)) {
            return getInset(jComponent, i2);
        }
        return 0;
    }

    private void checkPosition(int i2) {
        if (i2 != 1 && i2 != 5 && i2 != 7 && i2 != 3) {
            throw new IllegalArgumentException();
        }
    }

    protected int flipDirection(int i2) {
        switch (i2) {
            case 1:
                return 5;
            case 2:
            case 4:
            case 6:
            default:
                if ($assertionsDisabled) {
                    return 0;
                }
                throw new AssertionError();
            case 3:
                return 7;
            case 5:
                return 1;
            case 7:
                return 3;
        }
    }

    protected int getIndent(JComponent jComponent, int i2) {
        String uIClassID = jComponent.getUIClassID();
        if (uIClassID == "CheckBoxUI" || uIClassID == "RadioButtonUI") {
            AbstractButton abstractButton = (AbstractButton) jComponent;
            Insets insets = jComponent.getInsets();
            Icon icon = getIcon(abstractButton);
            int iconTextGap = abstractButton.getIconTextGap();
            if (isLeftAligned(abstractButton, i2)) {
                return insets.left + icon.getIconWidth() + iconTextGap;
            }
            if (isRightAligned(abstractButton, i2)) {
                return insets.right + icon.getIconWidth() + iconTextGap;
            }
            return 0;
        }
        return 0;
    }

    private Icon getIcon(AbstractButton abstractButton) {
        Icon icon = abstractButton.getIcon();
        if (icon != null) {
            return icon;
        }
        Object obj = null;
        if (abstractButton instanceof JCheckBox) {
            obj = "CheckBox.icon";
        } else if (abstractButton instanceof JRadioButton) {
            obj = "RadioButton.icon";
        }
        if (obj != null) {
            Object obj2 = UIManager.get(obj);
            if (obj2 instanceof Icon) {
                return (Icon) obj2;
            }
            return null;
        }
        return null;
    }

    private boolean isLeftAligned(AbstractButton abstractButton, int i2) {
        if (i2 == 7) {
            boolean zIsLeftToRight = abstractButton.getComponentOrientation().isLeftToRight();
            int horizontalAlignment = abstractButton.getHorizontalAlignment();
            return (zIsLeftToRight && (horizontalAlignment == 2 || horizontalAlignment == 10)) || (!zIsLeftToRight && horizontalAlignment == 11);
        }
        return false;
    }

    private boolean isRightAligned(AbstractButton abstractButton, int i2) {
        if (i2 == 3) {
            boolean zIsLeftToRight = abstractButton.getComponentOrientation().isLeftToRight();
            int horizontalAlignment = abstractButton.getHorizontalAlignment();
            return (zIsLeftToRight && (horizontalAlignment == 4 || horizontalAlignment == 11)) || (!zIsLeftToRight && horizontalAlignment == 10);
        }
        return false;
    }

    private int getInset(JComponent jComponent, int i2) {
        return getInset(jComponent.getInsets(), i2);
    }

    private int getInset(Insets insets, int i2) {
        if (insets == null) {
            return 0;
        }
        switch (i2) {
            case 1:
                return insets.top;
            case 2:
            case 4:
            case 6:
            default:
                if ($assertionsDisabled) {
                    return 0;
                }
                throw new AssertionError();
            case 3:
                return insets.right;
            case 5:
                return insets.bottom;
            case 7:
                return insets.left;
        }
    }
}
