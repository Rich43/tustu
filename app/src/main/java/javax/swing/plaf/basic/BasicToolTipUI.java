package javax.swing.plaf.basic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ToolTipUI;
import javax.swing.text.View;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolTipUI.class */
public class BasicToolTipUI extends ToolTipUI {
    static BasicToolTipUI sharedInstance = new BasicToolTipUI();
    private static PropertyChangeListener sharedPropertyChangedListener;
    private PropertyChangeListener propertyChangeListener;

    public static ComponentUI createUI(JComponent jComponent) {
        return sharedInstance;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults(jComponent);
        installComponents(jComponent);
        installListeners(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults(jComponent);
        uninstallComponents(jComponent);
        uninstallListeners(jComponent);
    }

    protected void installDefaults(JComponent jComponent) {
        LookAndFeel.installColorsAndFont(jComponent, "ToolTip.background", "ToolTip.foreground", "ToolTip.font");
        LookAndFeel.installProperty(jComponent, "opaque", Boolean.TRUE);
        componentChanged(jComponent);
    }

    protected void uninstallDefaults(JComponent jComponent) {
        LookAndFeel.uninstallBorder(jComponent);
    }

    private void installComponents(JComponent jComponent) {
        BasicHTML.updateRenderer(jComponent, ((JToolTip) jComponent).getTipText());
    }

    private void uninstallComponents(JComponent jComponent) {
        BasicHTML.updateRenderer(jComponent, "");
    }

    protected void installListeners(JComponent jComponent) {
        this.propertyChangeListener = createPropertyChangeListener(jComponent);
        jComponent.addPropertyChangeListener(this.propertyChangeListener);
    }

    protected void uninstallListeners(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }

    private PropertyChangeListener createPropertyChangeListener(JComponent jComponent) {
        if (sharedPropertyChangedListener == null) {
            sharedPropertyChangedListener = new PropertyChangeHandler();
        }
        return sharedPropertyChangedListener;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Font font = jComponent.getFont();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics, font);
        Dimension size = jComponent.getSize();
        graphics.setColor(jComponent.getForeground());
        String tipText = ((JToolTip) jComponent).getTipText();
        if (tipText == null) {
            tipText = "";
        }
        Insets insets = jComponent.getInsets();
        Rectangle rectangle = new Rectangle(insets.left + 3, insets.top, (size.width - (insets.left + insets.right)) - 6, size.height - (insets.top + insets.bottom));
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            view.paint(graphics, rectangle);
        } else {
            graphics.setFont(font);
            SwingUtilities2.drawString(jComponent, graphics, tipText, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        FontMetrics fontMetrics = jComponent.getFontMetrics(jComponent.getFont());
        Insets insets = jComponent.getInsets();
        Dimension dimension = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        String tipText = ((JToolTip) jComponent).getTipText();
        if (tipText != null && !tipText.equals("")) {
            View view = jComponent != null ? (View) jComponent.getClientProperty("html") : null;
            if (view != null) {
                dimension.width += ((int) view.getPreferredSpan(0)) + 6;
                dimension.height += (int) view.getPreferredSpan(1);
            } else {
                dimension.width += SwingUtilities2.stringWidth(jComponent, fontMetrics, tipText) + 6;
                dimension.height += fontMetrics.getHeight();
            }
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width - (view.getPreferredSpan(0) - view.getMinimumSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width + (view.getMaximumSpan(0) - view.getPreferredSpan(0)));
        }
        return preferredSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void componentChanged(JComponent jComponent) {
        JComponent component = ((JToolTip) jComponent).getComponent();
        if (component != null && !component.isEnabled()) {
            if (UIManager.getBorder("ToolTip.borderInactive") != null) {
                LookAndFeel.installBorder(jComponent, "ToolTip.borderInactive");
            } else {
                LookAndFeel.installBorder(jComponent, "ToolTip.border");
            }
            if (UIManager.getColor("ToolTip.backgroundInactive") != null) {
                LookAndFeel.installColors(jComponent, "ToolTip.backgroundInactive", "ToolTip.foregroundInactive");
                return;
            } else {
                LookAndFeel.installColors(jComponent, "ToolTip.background", "ToolTip.foreground");
                return;
            }
        }
        LookAndFeel.installBorder(jComponent, "ToolTip.border");
        LookAndFeel.installColors(jComponent, "ToolTip.background", "ToolTip.foreground");
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolTipUI$PropertyChangeHandler.class */
    private static class PropertyChangeHandler implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals("tiptext") || "font".equals(propertyName) || "foreground".equals(propertyName)) {
                JToolTip jToolTip = (JToolTip) propertyChangeEvent.getSource();
                BasicHTML.updateRenderer(jToolTip, jToolTip.getTipText());
            } else if ("component".equals(propertyName)) {
                JToolTip jToolTip2 = (JToolTip) propertyChangeEvent.getSource();
                if (jToolTip2.getUI() instanceof BasicToolTipUI) {
                    ((BasicToolTipUI) jToolTip2.getUI()).componentChanged(jToolTip2);
                }
            }
        }
    }
}
