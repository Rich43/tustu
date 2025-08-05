package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.text.JTextComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthScrollPaneUI.class */
public class SynthScrollPaneUI extends BasicScrollPaneUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private boolean viewportViewHasFocus = false;
    private ViewportViewFocusHandler viewportViewFocusHandler;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthScrollPaneUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintScrollPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        Border viewportBorder = this.scrollpane.getViewportBorder();
        if (viewportBorder != null) {
            Rectangle viewportBorderBounds = this.scrollpane.getViewportBorderBounds();
            viewportBorder.paintBorder(this.scrollpane, graphics, viewportBorderBounds.f12372x, viewportBorderBounds.f12373y, viewportBorderBounds.width, viewportBorderBounds.height);
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintScrollPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void installDefaults(JScrollPane jScrollPane) {
        updateStyle(jScrollPane);
    }

    private void updateStyle(JScrollPane jScrollPane) {
        SynthContext context = getContext(jScrollPane, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            Border viewportBorder = this.scrollpane.getViewportBorder();
            if (viewportBorder == null || (viewportBorder instanceof UIResource)) {
                this.scrollpane.setViewportBorder(new ViewportBorder(context));
            }
            if (synthStyle != null) {
                uninstallKeyboardActions(jScrollPane);
                installKeyboardActions(jScrollPane);
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void installListeners(JScrollPane jScrollPane) {
        super.installListeners(jScrollPane);
        jScrollPane.addPropertyChangeListener(this);
        if (UIManager.getBoolean("ScrollPane.useChildTextComponentFocus")) {
            this.viewportViewFocusHandler = new ViewportViewFocusHandler();
            jScrollPane.getViewport().addContainerListener(this.viewportViewFocusHandler);
            Component view = jScrollPane.getViewport().getView();
            if (view instanceof JTextComponent) {
                view.addFocusListener(this.viewportViewFocusHandler);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void uninstallDefaults(JScrollPane jScrollPane) {
        SynthContext context = getContext(jScrollPane, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        if (this.scrollpane.getViewportBorder() instanceof UIResource) {
            this.scrollpane.setViewportBorder(null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void uninstallListeners(JComponent jComponent) {
        super.uninstallListeners(jComponent);
        jComponent.removePropertyChangeListener(this);
        if (this.viewportViewFocusHandler != null) {
            JViewport viewport = ((JScrollPane) jComponent).getViewport();
            viewport.removeContainerListener(this.viewportViewFocusHandler);
            if (viewport.getView() != null) {
                viewport.getView().removeFocusListener(this.viewportViewFocusHandler);
            }
            this.viewportViewFocusHandler = null;
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        int componentState = SynthLookAndFeel.getComponentState(jComponent);
        if (this.viewportViewFocusHandler != null && this.viewportViewHasFocus) {
            componentState |= 256;
        }
        return componentState;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle(this.scrollpane);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthScrollPaneUI$ViewportBorder.class */
    private class ViewportBorder extends AbstractBorder implements UIResource {
        private Insets insets;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SynthScrollPaneUI.class.desiredAssertionStatus();
        }

        ViewportBorder(SynthContext synthContext) {
            this.insets = (Insets) synthContext.getStyle().get(synthContext, "ScrollPane.viewportBorderInsets");
            if (this.insets == null) {
                this.insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            SynthContext context = SynthScrollPaneUI.this.getContext((JComponent) component);
            if (context.getStyle() == null) {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) "SynthBorder is being used outside after the  UI has been uninstalled");
                }
            } else {
                context.getPainter().paintViewportBorder(context, graphics, i2, i3, i4, i5);
                context.dispose();
            }
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            if (insets == null) {
                return new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
            }
            insets.top = this.insets.top;
            insets.bottom = this.insets.bottom;
            insets.left = this.insets.left;
            insets.right = this.insets.left;
            return insets;
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthScrollPaneUI$ViewportViewFocusHandler.class */
    private class ViewportViewFocusHandler implements ContainerListener, FocusListener {
        private ViewportViewFocusHandler() {
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            if (containerEvent.getChild() instanceof JTextComponent) {
                containerEvent.getChild().addFocusListener(this);
                SynthScrollPaneUI.this.viewportViewHasFocus = containerEvent.getChild().isFocusOwner();
                SynthScrollPaneUI.this.scrollpane.repaint();
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            if (containerEvent.getChild() instanceof JTextComponent) {
                containerEvent.getChild().removeFocusListener(this);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            SynthScrollPaneUI.this.viewportViewHasFocus = true;
            SynthScrollPaneUI.this.scrollpane.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            SynthScrollPaneUI.this.viewportViewHasFocus = false;
            SynthScrollPaneUI.this.scrollpane.repaint();
        }
    }
}
