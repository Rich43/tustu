package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthToolBarUI.class */
public class SynthToolBarUI extends BasicToolBarUI implements PropertyChangeListener, SynthUI {
    private Icon handleIcon = null;
    private Rectangle contentRect = new Rectangle();
    private SynthStyle style;
    private SynthStyle contentStyle;
    private SynthStyle dragWindowStyle;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthToolBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void installDefaults() {
        this.toolBar.setLayout(createLayout());
        updateStyle(this.toolBar);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void installListeners() {
        super.installListeners();
        this.toolBar.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.toolBar.removePropertyChangeListener(this);
    }

    private void updateStyle(JToolBar jToolBar) {
        SynthContext context = getContext(jToolBar, Region.TOOL_BAR_CONTENT, null, 1);
        this.contentStyle = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
        SynthContext context2 = getContext(jToolBar, Region.TOOL_BAR_DRAG_WINDOW, null, 1);
        this.dragWindowStyle = SynthLookAndFeel.updateStyle(context2, this);
        context2.dispose();
        SynthContext context3 = getContext(jToolBar, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context3, this);
        if (synthStyle != this.style) {
            this.handleIcon = this.style.getIcon(context3, "ToolBar.handleIcon");
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context3.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.toolBar, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        this.handleIcon = null;
        SynthContext context2 = getContext(this.toolBar, Region.TOOL_BAR_CONTENT, this.contentStyle, 1);
        this.contentStyle.uninstallDefaults(context2);
        context2.dispose();
        this.contentStyle = null;
        SynthContext context3 = getContext(this.toolBar, Region.TOOL_BAR_DRAG_WINDOW, this.dragWindowStyle, 1);
        this.dragWindowStyle.uninstallDefaults(context3);
        context3.dispose();
        this.dragWindowStyle = null;
        this.toolBar.setLayout(null);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void installComponents() {
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void uninstallComponents() {
    }

    protected LayoutManager createLayout() {
        return new SynthToolBarLayoutManager();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private SynthContext getContext(JComponent jComponent, Region region, SynthStyle synthStyle) {
        return SynthContext.getContext(jComponent, region, synthStyle, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, SynthStyle synthStyle, int i2) {
        return SynthContext.getContext(jComponent, region, synthStyle, i2);
    }

    private int getComponentState(JComponent jComponent, Region region) {
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintToolBarBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), this.toolBar.getOrientation());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintToolBarBorder(synthContext, graphics, i2, i3, i4, i5, this.toolBar.getOrientation());
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToNonRollover(Component component) {
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToRollover(Component component) {
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToNormal(Component component) {
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        if (this.handleIcon != null && this.toolBar.isFloatable()) {
            SynthIcon.paintIcon(this.handleIcon, synthContext, graphics, this.toolBar.getComponentOrientation().isLeftToRight() ? 0 : this.toolBar.getWidth() - SynthIcon.getIconWidth(this.handleIcon, synthContext), 0, SynthIcon.getIconWidth(this.handleIcon, synthContext), SynthIcon.getIconHeight(this.handleIcon, synthContext));
        }
        SynthContext context = getContext(this.toolBar, Region.TOOL_BAR_CONTENT, this.contentStyle);
        paintContent(context, graphics, this.contentRect);
        context.dispose();
    }

    protected void paintContent(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        synthContext.getPainter().paintToolBarContentBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, this.toolBar.getOrientation());
        synthContext.getPainter().paintToolBarContentBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, this.toolBar.getOrientation());
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void paintDragWindow(Graphics graphics) {
        int width = this.dragWindow.getWidth();
        int height = this.dragWindow.getHeight();
        SynthContext context = getContext(this.toolBar, Region.TOOL_BAR_DRAG_WINDOW, this.dragWindowStyle);
        SynthLookAndFeel.updateSubregion(context, graphics, new Rectangle(0, 0, width, height));
        context.getPainter().paintToolBarDragWindowBackground(context, graphics, 0, 0, width, height, this.dragWindow.getOrientation());
        context.getPainter().paintToolBarDragWindowBorder(context, graphics, 0, 0, width, height, this.dragWindow.getOrientation());
        context.dispose();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JToolBar) propertyChangeEvent.getSource());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthToolBarUI$SynthToolBarLayoutManager.class */
    class SynthToolBarLayoutManager implements LayoutManager {
        SynthToolBarLayoutManager() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            JToolBar jToolBar = (JToolBar) container;
            Insets insets = jToolBar.getInsets();
            Dimension dimension = new Dimension();
            SynthContext context = SynthToolBarUI.this.getContext(jToolBar);
            if (jToolBar.getOrientation() == 0) {
                dimension.width = jToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, context) : 0;
                for (int i2 = 0; i2 < jToolBar.getComponentCount(); i2++) {
                    Component component = jToolBar.getComponent(i2);
                    if (component.isVisible()) {
                        Dimension minimumSize = component.getMinimumSize();
                        dimension.width += minimumSize.width;
                        dimension.height = Math.max(dimension.height, minimumSize.height);
                    }
                }
            } else {
                dimension.height = jToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, context) : 0;
                for (int i3 = 0; i3 < jToolBar.getComponentCount(); i3++) {
                    Component component2 = jToolBar.getComponent(i3);
                    if (component2.isVisible()) {
                        Dimension minimumSize2 = component2.getMinimumSize();
                        dimension.width = Math.max(dimension.width, minimumSize2.width);
                        dimension.height += minimumSize2.height;
                    }
                }
            }
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
            context.dispose();
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            JToolBar jToolBar = (JToolBar) container;
            Insets insets = jToolBar.getInsets();
            Dimension dimension = new Dimension();
            SynthContext context = SynthToolBarUI.this.getContext(jToolBar);
            if (jToolBar.getOrientation() == 0) {
                dimension.width = jToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, context) : 0;
                for (int i2 = 0; i2 < jToolBar.getComponentCount(); i2++) {
                    Component component = jToolBar.getComponent(i2);
                    if (component.isVisible()) {
                        Dimension preferredSize = component.getPreferredSize();
                        dimension.width += preferredSize.width;
                        dimension.height = Math.max(dimension.height, preferredSize.height);
                    }
                }
            } else {
                dimension.height = jToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, context) : 0;
                for (int i3 = 0; i3 < jToolBar.getComponentCount(); i3++) {
                    Component component2 = jToolBar.getComponent(i3);
                    if (component2.isVisible()) {
                        Dimension preferredSize2 = component2.getPreferredSize();
                        dimension.width = Math.max(dimension.width, preferredSize2.width);
                        dimension.height += preferredSize2.height;
                    }
                }
            }
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
            context.dispose();
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int i3;
            int i4;
            int i5;
            JToolBar jToolBar = (JToolBar) container;
            Insets insets = jToolBar.getInsets();
            boolean zIsLeftToRight = jToolBar.getComponentOrientation().isLeftToRight();
            SynthContext context = SynthToolBarUI.this.getContext(jToolBar);
            int i6 = 0;
            for (int i7 = 0; i7 < jToolBar.getComponentCount(); i7++) {
                if (isGlue(jToolBar.getComponent(i7))) {
                    i6++;
                }
            }
            if (jToolBar.getOrientation() == 0) {
                int iconWidth = jToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, context) : 0;
                SynthToolBarUI.this.contentRect.f12372x = zIsLeftToRight ? iconWidth : 0;
                SynthToolBarUI.this.contentRect.f12373y = 0;
                SynthToolBarUI.this.contentRect.width = jToolBar.getWidth() - iconWidth;
                SynthToolBarUI.this.contentRect.height = jToolBar.getHeight();
                int width = zIsLeftToRight ? iconWidth + insets.left : (jToolBar.getWidth() - iconWidth) - insets.right;
                int i8 = insets.top;
                int height = (jToolBar.getHeight() - insets.top) - insets.bottom;
                int width2 = 0;
                if (i6 > 0) {
                    width2 = (jToolBar.getWidth() - minimumLayoutSize(container).width) / i6;
                    if (width2 < 0) {
                        width2 = 0;
                    }
                }
                for (int i9 = 0; i9 < jToolBar.getComponentCount(); i9++) {
                    Component component = jToolBar.getComponent(i9);
                    if (component.isVisible()) {
                        Dimension preferredSize = component.getPreferredSize();
                        if (preferredSize.height >= height || (component instanceof JSeparator)) {
                            i4 = i8;
                            i5 = height;
                        } else {
                            i4 = (i8 + (height / 2)) - (preferredSize.height / 2);
                            i5 = preferredSize.height;
                        }
                        if (isGlue(component)) {
                            preferredSize.width += width2;
                        }
                        component.setBounds(zIsLeftToRight ? width : width - preferredSize.width, i4, preferredSize.width, i5);
                        width = zIsLeftToRight ? width + preferredSize.width : width - preferredSize.width;
                    }
                }
            } else {
                int iconHeight = jToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, context) : 0;
                SynthToolBarUI.this.contentRect.f12372x = 0;
                SynthToolBarUI.this.contentRect.f12373y = iconHeight;
                SynthToolBarUI.this.contentRect.width = jToolBar.getWidth();
                SynthToolBarUI.this.contentRect.height = jToolBar.getHeight() - iconHeight;
                int i10 = insets.left;
                int width3 = (jToolBar.getWidth() - insets.left) - insets.right;
                int i11 = iconHeight + insets.top;
                int height2 = 0;
                if (i6 > 0) {
                    height2 = (jToolBar.getHeight() - minimumLayoutSize(container).height) / i6;
                    if (height2 < 0) {
                        height2 = 0;
                    }
                }
                for (int i12 = 0; i12 < jToolBar.getComponentCount(); i12++) {
                    Component component2 = jToolBar.getComponent(i12);
                    if (component2.isVisible()) {
                        Dimension preferredSize2 = component2.getPreferredSize();
                        if (preferredSize2.width >= width3 || (component2 instanceof JSeparator)) {
                            i2 = i10;
                            i3 = width3;
                        } else {
                            i2 = (i10 + (width3 / 2)) - (preferredSize2.width / 2);
                            i3 = preferredSize2.width;
                        }
                        if (isGlue(component2)) {
                            preferredSize2.height += height2;
                        }
                        component2.setBounds(i2, i11, i3, preferredSize2.height);
                        i11 += preferredSize2.height;
                    }
                }
            }
            context.dispose();
        }

        private boolean isGlue(Component component) {
            if (component.isVisible() && (component instanceof Box.Filler)) {
                Box.Filler filler = (Box.Filler) component;
                Dimension minimumSize = filler.getMinimumSize();
                Dimension preferredSize = filler.getPreferredSize();
                return minimumSize.width == 0 && minimumSize.height == 0 && preferredSize.width == 0 && preferredSize.height == 0;
            }
            return false;
        }
    }
}
