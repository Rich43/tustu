package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthLabelUI.class */
public class SynthLabelUI extends BasicLabelUI implements SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthLabelUI();
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI
    protected void installDefaults(JLabel jLabel) {
        updateStyle(jLabel);
    }

    void updateStyle(JLabel jLabel) {
        SynthContext context = getContext(jLabel, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI
    protected void uninstallDefaults(JLabel jLabel) {
        SynthContext context = getContext(jLabel, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
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
        if (SynthLookAndFeel.getSelectedUI() == this && componentState == 1) {
            componentState = SynthLookAndFeel.getSelectedUIState() | 1;
        }
        return componentState;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int ascent;
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        JLabel jLabel = (JLabel) jComponent;
        String text = jLabel.getText();
        if (text == null || "".equals(text)) {
            return -1;
        }
        Insets insets = jLabel.getInsets();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        rectangle.f12372x = insets.left;
        rectangle.f12373y = insets.top;
        rectangle.width = i2 - (insets.right + rectangle.f12372x);
        rectangle.height = i3 - (insets.bottom + rectangle.f12373y);
        SynthContext context = getContext(jLabel);
        FontMetrics fontMetrics = context.getComponent().getFontMetrics(context.getStyle().getFont(context));
        context.getStyle().getGraphicsUtils(context).layoutText(context, fontMetrics, jLabel.getText(), jLabel.getIcon(), jLabel.getHorizontalAlignment(), jLabel.getVerticalAlignment(), jLabel.getHorizontalTextPosition(), jLabel.getVerticalTextPosition(), rectangle, rectangle3, rectangle2, jLabel.getIconTextGap());
        View view = (View) jLabel.getClientProperty("html");
        if (view != null) {
            ascent = BasicHTML.getHTMLBaseline(view, rectangle2.width, rectangle2.height);
            if (ascent >= 0) {
                ascent += rectangle2.f12373y;
            }
        } else {
            ascent = rectangle2.f12373y + fontMetrics.getAscent();
        }
        context.dispose();
        return ascent;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintLabelBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        JLabel jLabel = (JLabel) synthContext.getComponent();
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.TEXT_FOREGROUND));
        graphics.setFont(this.style.getFont(synthContext));
        synthContext.getStyle().getGraphicsUtils(synthContext).paintText(synthContext, graphics, jLabel.getText(), icon, jLabel.getHorizontalAlignment(), jLabel.getVerticalAlignment(), jLabel.getHorizontalTextPosition(), jLabel.getVerticalTextPosition(), jLabel.getIconTextGap(), jLabel.getDisplayedMnemonicIndex(), 0);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintLabelBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        JLabel jLabel = (JLabel) jComponent;
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        SynthContext context = getContext(jComponent);
        Dimension preferredSize = context.getStyle().getGraphicsUtils(context).getPreferredSize(context, context.getStyle().getFont(context), jLabel.getText(), icon, jLabel.getHorizontalAlignment(), jLabel.getVerticalAlignment(), jLabel.getHorizontalTextPosition(), jLabel.getVerticalTextPosition(), jLabel.getIconTextGap(), jLabel.getDisplayedMnemonicIndex());
        context.dispose();
        return preferredSize;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        JLabel jLabel = (JLabel) jComponent;
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        SynthContext context = getContext(jComponent);
        Dimension minimumSize = context.getStyle().getGraphicsUtils(context).getMinimumSize(context, context.getStyle().getFont(context), jLabel.getText(), icon, jLabel.getHorizontalAlignment(), jLabel.getVerticalAlignment(), jLabel.getHorizontalTextPosition(), jLabel.getVerticalTextPosition(), jLabel.getIconTextGap(), jLabel.getDisplayedMnemonicIndex());
        context.dispose();
        return minimumSize;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        JLabel jLabel = (JLabel) jComponent;
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        SynthContext context = getContext(jComponent);
        Dimension maximumSize = context.getStyle().getGraphicsUtils(context).getMaximumSize(context, context.getStyle().getFont(context), jLabel.getText(), icon, jLabel.getHorizontalAlignment(), jLabel.getVerticalAlignment(), jLabel.getHorizontalTextPosition(), jLabel.getVerticalTextPosition(), jLabel.getIconTextGap(), jLabel.getDisplayedMnemonicIndex());
        context.dispose();
        return maximumSize;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JLabel) propertyChangeEvent.getSource());
        }
    }
}
