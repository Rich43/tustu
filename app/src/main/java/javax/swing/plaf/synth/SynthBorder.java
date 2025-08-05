package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthBorder.class */
class SynthBorder extends AbstractBorder implements UIResource {
    private SynthUI ui;
    private Insets insets;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SynthBorder.class.desiredAssertionStatus();
    }

    SynthBorder(SynthUI synthUI, Insets insets) {
        this.ui = synthUI;
        this.insets = insets;
    }

    SynthBorder(SynthUI synthUI) {
        this(synthUI, null);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        SynthContext context = this.ui.getContext((JComponent) component);
        if (context.getStyle() == null) {
            if (!$assertionsDisabled) {
                throw new AssertionError((Object) "SynthBorder is being used outside after the UI has been uninstalled");
            }
        } else {
            this.ui.paintBorder(context, graphics, i2, i3, i4, i5);
            context.dispose();
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        if (this.insets != null) {
            if (insets == null) {
                insets = new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
            } else {
                insets.top = this.insets.top;
                insets.bottom = this.insets.bottom;
                insets.left = this.insets.left;
                insets.right = this.insets.right;
            }
        } else if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        } else {
            insets.right = 0;
            insets.left = 0;
            insets.bottom = 0;
            insets.top = 0;
        }
        if (component instanceof JComponent) {
            Region region = Region.getRegion((JComponent) component);
            Insets margin = null;
            if ((region == Region.ARROW_BUTTON || region == Region.BUTTON || region == Region.CHECK_BOX || region == Region.CHECK_BOX_MENU_ITEM || region == Region.MENU || region == Region.MENU_ITEM || region == Region.RADIO_BUTTON || region == Region.RADIO_BUTTON_MENU_ITEM || region == Region.TOGGLE_BUTTON) && (component instanceof AbstractButton)) {
                margin = ((AbstractButton) component).getMargin();
            } else if ((region == Region.EDITOR_PANE || region == Region.FORMATTED_TEXT_FIELD || region == Region.PASSWORD_FIELD || region == Region.TEXT_AREA || region == Region.TEXT_FIELD || region == Region.TEXT_PANE) && (component instanceof JTextComponent)) {
                margin = ((JTextComponent) component).getMargin();
            } else if (region == Region.TOOL_BAR && (component instanceof JToolBar)) {
                margin = ((JToolBar) component).getMargin();
            } else if (region == Region.MENU_BAR && (component instanceof JMenuBar)) {
                margin = ((JMenuBar) component).getMargin();
            }
            if (margin != null) {
                insets.top += margin.top;
                insets.bottom += margin.bottom;
                insets.left += margin.left;
                insets.right += margin.right;
            }
        }
        return insets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }
}
