package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.SeparatorUI;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthSeparatorUI.class */
public class SynthSeparatorUI extends SeparatorUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthSeparatorUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults((JSeparator) jComponent);
        installListeners((JSeparator) jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallListeners((JSeparator) jComponent);
        uninstallDefaults((JSeparator) jComponent);
    }

    public void installDefaults(JSeparator jSeparator) {
        updateStyle(jSeparator);
    }

    private void updateStyle(JSeparator jSeparator) {
        Dimension separatorSize;
        SynthContext context = getContext(jSeparator, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle && (jSeparator instanceof JToolBar.Separator) && ((separatorSize = ((JToolBar.Separator) jSeparator).getSeparatorSize()) == null || (separatorSize instanceof UIResource))) {
            DimensionUIResource dimensionUIResource = (DimensionUIResource) this.style.get(context, "ToolBar.separatorSize");
            if (dimensionUIResource == null) {
                dimensionUIResource = new DimensionUIResource(10, 10);
            }
            ((JToolBar.Separator) jSeparator).setSeparatorSize(dimensionUIResource);
        }
        context.dispose();
    }

    public void uninstallDefaults(JSeparator jSeparator) {
        SynthContext context = getContext(jSeparator, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    public void installListeners(JSeparator jSeparator) {
        jSeparator.addPropertyChangeListener(this);
    }

    public void uninstallListeners(JSeparator jSeparator) {
        jSeparator.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        JSeparator jSeparator = (JSeparator) context.getComponent();
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintSeparatorBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), jSeparator.getOrientation());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        JSeparator jSeparator = (JSeparator) synthContext.getComponent();
        synthContext.getPainter().paintSeparatorForeground(synthContext, graphics, 0, 0, jSeparator.getWidth(), jSeparator.getHeight(), jSeparator.getOrientation());
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintSeparatorBorder(synthContext, graphics, i2, i3, i4, i5, ((JSeparator) synthContext.getComponent()).getOrientation());
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension;
        SynthContext context = getContext(jComponent);
        int i2 = this.style.getInt(context, "Separator.thickness", 2);
        Insets insets = jComponent.getInsets();
        if (((JSeparator) jComponent).getOrientation() == 1) {
            dimension = new Dimension(insets.left + insets.right + i2, insets.top + insets.bottom);
        } else {
            dimension = new Dimension(insets.left + insets.right, insets.top + insets.bottom + i2);
        }
        context.dispose();
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return getPreferredSize(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JSeparator) propertyChangeEvent.getSource());
        }
    }
}
