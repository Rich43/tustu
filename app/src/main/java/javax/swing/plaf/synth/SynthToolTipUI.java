package javax.swing.plaf.synth;

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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthToolTipUI.class */
public class SynthToolTipUI extends BasicToolTipUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthToolTipUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI
    protected void installDefaults(JComponent jComponent) {
        updateStyle(jComponent);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI
    protected void uninstallDefaults(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI
    protected void installListeners(JComponent jComponent) {
        jComponent.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI
    protected void uninstallListeners(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        JComponent component = ((JToolTip) jComponent).getComponent();
        if (component != null && !component.isEnabled()) {
            return 8;
        }
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintToolTipBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintToolTipBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        JToolTip jToolTip = (JToolTip) synthContext.getComponent();
        Insets insets = jToolTip.getInsets();
        View view = (View) jToolTip.getClientProperty("html");
        if (view != null) {
            view.paint(graphics, new Rectangle(insets.left, insets.top, jToolTip.getWidth() - (insets.left + insets.right), jToolTip.getHeight() - (insets.top + insets.bottom)));
            return;
        }
        graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.TEXT_FOREGROUND));
        graphics.setFont(this.style.getFont(synthContext));
        synthContext.getStyle().getGraphicsUtils(synthContext).paintText(synthContext, graphics, jToolTip.getTipText(), insets.left, insets.top, -1);
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        Insets insets = jComponent.getInsets();
        Dimension dimension = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        String tipText = ((JToolTip) jComponent).getTipText();
        if (tipText != null) {
            View view = jComponent != null ? (View) jComponent.getClientProperty("html") : null;
            if (view != null) {
                dimension.width += (int) view.getPreferredSpan(0);
                dimension.height += (int) view.getPreferredSpan(1);
            } else {
                Font font = context.getStyle().getFont(context);
                FontMetrics fontMetrics = jComponent.getFontMetrics(font);
                dimension.width += context.getStyle().getGraphicsUtils(context).computeStringWidth(context, font, fontMetrics, tipText);
                dimension.height += fontMetrics.getHeight();
            }
        }
        context.dispose();
        return dimension;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JToolTip) propertyChangeEvent.getSource());
        }
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals("tiptext") || "font".equals(propertyName) || "foreground".equals(propertyName)) {
            JToolTip jToolTip = (JToolTip) propertyChangeEvent.getSource();
            BasicHTML.updateRenderer(jToolTip, jToolTip.getTipText());
        }
    }
}
