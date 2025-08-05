package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuItemUI;
import org.slf4j.Marker;
import sun.swing.MenuItemLayoutHelper;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthMenuItemUI.class */
public class SynthMenuItemUI extends BasicMenuItemUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private SynthStyle accStyle;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        JComponent menuItemParent = MenuItemLayoutHelper.getMenuItemParent((JMenuItem) jComponent);
        if (menuItemParent != null) {
            menuItemParent.putClientProperty(SynthMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installDefaults() {
        updateStyle(this.menuItem);
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installListeners() {
        super.installListeners();
        this.menuItem.addPropertyChangeListener(this);
    }

    private void updateStyle(JMenuItem jMenuItem) {
        SynthContext context = getContext(jMenuItem, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (synthStyle != this.style) {
            String propertyPrefix = getPropertyPrefix();
            Object obj = this.style.get(context, propertyPrefix + ".textIconGap");
            if (obj != null) {
                LookAndFeel.installProperty(jMenuItem, "iconTextGap", obj);
            }
            this.defaultTextIconGap = jMenuItem.getIconTextGap();
            if (this.menuItem.getMargin() == null || (this.menuItem.getMargin() instanceof UIResource)) {
                Insets insets = (Insets) this.style.get(context, propertyPrefix + ".margin");
                if (insets == null) {
                    insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                }
                this.menuItem.setMargin(insets);
            }
            this.acceleratorDelimiter = this.style.getString(context, propertyPrefix + ".acceleratorDelimiter", Marker.ANY_NON_NULL_MARKER);
            this.arrowIcon = this.style.getIcon(context, propertyPrefix + ".arrowIcon");
            this.checkIcon = this.style.getIcon(context, propertyPrefix + ".checkIcon");
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
        SynthContext context2 = getContext(jMenuItem, Region.MENU_ITEM_ACCELERATOR, 1);
        this.accStyle = SynthLookAndFeel.updateStyle(context2, this);
        context2.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.menuItem, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        SynthContext context2 = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR, 1);
        this.accStyle.uninstallDefaults(context2);
        context2.dispose();
        this.accStyle = null;
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.menuItem.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    SynthContext getContext(JComponent jComponent, Region region) {
        return getContext(jComponent, region, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        return SynthContext.getContext(jComponent, region, this.accStyle, i2);
    }

    private int getComponentState(JComponent jComponent) {
        int componentState;
        if (!jComponent.isEnabled()) {
            componentState = 8;
        } else if (this.menuItem.isArmed()) {
            componentState = 2;
        } else {
            componentState = SynthLookAndFeel.getComponentState(jComponent);
        }
        if (this.menuItem.isSelected()) {
            componentState |= 512;
        }
        return componentState;
    }

    private int getComponentState(JComponent jComponent, Region region) {
        return getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected Dimension getPreferredMenuItemSize(JComponent jComponent, Icon icon, Icon icon2, int i2) {
        SynthContext context = getContext(jComponent);
        SynthContext context2 = getContext(jComponent, Region.MENU_ITEM_ACCELERATOR);
        Dimension preferredMenuItemSize = SynthGraphicsUtils.getPreferredMenuItemSize(context, context2, jComponent, icon, icon2, i2, this.acceleratorDelimiter, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
        context.dispose();
        context2.dispose();
        return preferredMenuItemSize;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI, javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        paintBackground(context, graphics, jComponent);
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        SynthContext context = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR);
        String propertyPrefix = getPropertyPrefix();
        SynthGraphicsUtils.paint(synthContext, context, graphics, this.style.getIcon(synthContext, propertyPrefix + ".checkIcon"), this.style.getIcon(synthContext, propertyPrefix + ".arrowIcon"), this.acceleratorDelimiter, this.defaultTextIconGap, getPropertyPrefix());
        context.dispose();
    }

    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        SynthGraphicsUtils.paintBackground(synthContext, graphics, jComponent);
    }

    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JMenuItem) propertyChangeEvent.getSource());
        }
    }
}
