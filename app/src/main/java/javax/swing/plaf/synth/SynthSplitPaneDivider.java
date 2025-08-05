package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthSplitPaneDivider.class */
class SynthSplitPaneDivider extends BasicSplitPaneDivider {
    public SynthSplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        super(basicSplitPaneUI);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected void setMouseOver(boolean z2) {
        if (isMouseOver() != z2) {
            repaint();
        }
        super.setMouseOver(z2);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        if (propertyChangeEvent.getSource() == this.splitPane && propertyChangeEvent.getPropertyName() == "orientation") {
            if (this.leftButton instanceof SynthArrowButton) {
                ((SynthArrowButton) this.leftButton).setDirection(mapDirection(true));
            }
            if (this.rightButton instanceof SynthArrowButton) {
                ((SynthArrowButton) this.rightButton).setDirection(mapDirection(false));
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Graphics graphicsCreate = graphics.create();
        SynthContext context = ((SynthSplitPaneUI) this.splitPaneUI).getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER);
        Rectangle bounds = getBounds();
        bounds.f12373y = 0;
        bounds.f12372x = 0;
        SynthLookAndFeel.updateSubregion(context, graphics, bounds);
        context.getPainter().paintSplitPaneDividerBackground(context, graphics, 0, 0, bounds.width, bounds.height, this.splitPane.getOrientation());
        context.getPainter().paintSplitPaneDividerForeground(context, graphics, 0, 0, getWidth(), getHeight(), this.splitPane.getOrientation());
        context.dispose();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            Rectangle bounds2 = component.getBounds();
            Graphics graphicsCreate2 = graphics.create(bounds2.f12372x, bounds2.f12373y, bounds2.width, bounds2.height);
            component.paint(graphicsCreate2);
            graphicsCreate2.dispose();
        }
        graphicsCreate.dispose();
    }

    private int mapDirection(boolean z2) {
        if (z2) {
            if (this.splitPane.getOrientation() == 1) {
                return 7;
            }
            return 1;
        }
        if (this.splitPane.getOrientation() == 1) {
            return 3;
        }
        return 5;
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createLeftOneTouchButton() {
        SynthArrowButton synthArrowButton = new SynthArrowButton(1);
        int iLookupOneTouchSize = lookupOneTouchSize();
        synthArrowButton.setName("SplitPaneDivider.leftOneTouchButton");
        synthArrowButton.setMinimumSize(new Dimension(iLookupOneTouchSize, iLookupOneTouchSize));
        synthArrowButton.setCursor(Cursor.getPredefinedCursor(0));
        synthArrowButton.setFocusPainted(false);
        synthArrowButton.setBorderPainted(false);
        synthArrowButton.setRequestFocusEnabled(false);
        synthArrowButton.setDirection(mapDirection(true));
        return synthArrowButton;
    }

    private int lookupOneTouchSize() {
        return DefaultLookup.getInt(this.splitPaneUI.getSplitPane(), this.splitPaneUI, "SplitPaneDivider.oneTouchButtonSize", 6);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider
    protected JButton createRightOneTouchButton() {
        SynthArrowButton synthArrowButton = new SynthArrowButton(1);
        int iLookupOneTouchSize = lookupOneTouchSize();
        synthArrowButton.setName("SplitPaneDivider.rightOneTouchButton");
        synthArrowButton.setMinimumSize(new Dimension(iLookupOneTouchSize, iLookupOneTouchSize));
        synthArrowButton.setCursor(Cursor.getPredefinedCursor(0));
        synthArrowButton.setFocusPainted(false);
        synthArrowButton.setBorderPainted(false);
        synthArrowButton.setRequestFocusEnabled(false);
        synthArrowButton.setDirection(mapDirection(false));
        return synthArrowButton;
    }
}
