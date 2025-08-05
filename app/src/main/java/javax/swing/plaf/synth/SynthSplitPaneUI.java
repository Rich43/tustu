package javax.swing.plaf.synth;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthSplitPaneUI.class */
public class SynthSplitPaneUI extends BasicSplitPaneUI implements PropertyChangeListener, SynthUI {
    private static Set<KeyStroke> managingFocusForwardTraversalKeys;
    private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
    private SynthStyle style;
    private SynthStyle dividerStyle;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthSplitPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    protected void installDefaults() {
        updateStyle(this.splitPane);
        setOrientation(this.splitPane.getOrientation());
        setContinuousLayout(this.splitPane.isContinuousLayout());
        resetLayoutManager();
        if (this.nonContinuousLayoutDivider == null) {
            setNonContinuousLayoutDivider(createDefaultNonContinuousLayoutDivider(), true);
        } else {
            setNonContinuousLayoutDivider(this.nonContinuousLayoutDivider, true);
        }
        if (managingFocusForwardTraversalKeys == null) {
            managingFocusForwardTraversalKeys = new HashSet();
            managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
        }
        this.splitPane.setFocusTraversalKeys(0, managingFocusForwardTraversalKeys);
        if (managingFocusBackwardTraversalKeys == null) {
            managingFocusBackwardTraversalKeys = new HashSet();
            managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
        }
        this.splitPane.setFocusTraversalKeys(1, managingFocusBackwardTraversalKeys);
    }

    private void updateStyle(JSplitPane jSplitPane) {
        SynthContext context = getContext(jSplitPane, Region.SPLIT_PANE_DIVIDER, 1);
        SynthStyle synthStyle = this.dividerStyle;
        this.dividerStyle = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
        SynthContext context2 = getContext(jSplitPane, 1);
        SynthStyle synthStyle2 = this.style;
        this.style = SynthLookAndFeel.updateStyle(context2, this);
        if (this.style != synthStyle2) {
            Object obj = this.style.get(context2, "SplitPane.size");
            if (obj == null) {
                obj = 6;
            }
            LookAndFeel.installProperty(jSplitPane, JSplitPane.DIVIDER_SIZE_PROPERTY, obj);
            Object obj2 = this.style.get(context2, "SplitPane.oneTouchExpandable");
            if (obj2 != null) {
                LookAndFeel.installProperty(jSplitPane, JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY, obj2);
            }
            if (this.divider != null) {
                jSplitPane.remove(this.divider);
                this.divider.setDividerSize(jSplitPane.getDividerSize());
            }
            if (synthStyle2 != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        if (this.style != synthStyle2 || this.dividerStyle != synthStyle) {
            if (this.divider != null) {
                jSplitPane.remove(this.divider);
            }
            this.divider = createDefaultDivider();
            this.divider.setBasicSplitPaneUI(this);
            jSplitPane.add(this.divider, JSplitPane.DIVIDER);
        }
        context2.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    protected void installListeners() {
        super.installListeners();
        this.splitPane.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.splitPane, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        SynthContext context2 = getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER, 1);
        this.dividerStyle.uninstallDefaults(context2);
        context2.dispose();
        this.dividerStyle = null;
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.splitPane.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    SynthContext getContext(JComponent jComponent, Region region) {
        return getContext(jComponent, region, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        if (region == Region.SPLIT_PANE_DIVIDER) {
            return SynthContext.getContext(jComponent, region, this.dividerStyle, i2);
        }
        return SynthContext.getContext(jComponent, region, this.style, i2);
    }

    private int getComponentState(JComponent jComponent, Region region) {
        int componentState = SynthLookAndFeel.getComponentState(jComponent);
        if (this.divider.isMouseOver()) {
            componentState |= 2;
        }
        return componentState;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JSplitPane) propertyChangeEvent.getSource());
        }
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    public BasicSplitPaneDivider createDefaultDivider() {
        SynthSplitPaneDivider synthSplitPaneDivider = new SynthSplitPaneDivider(this);
        synthSplitPaneDivider.setDividerSize(this.splitPane.getDividerSize());
        return synthSplitPaneDivider;
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    protected Component createDefaultNonContinuousLayoutDivider() {
        return new Canvas() { // from class: javax.swing.plaf.synth.SynthSplitPaneUI.1
            @Override // java.awt.Canvas, java.awt.Component
            public void paint(Graphics graphics) {
                SynthSplitPaneUI.this.paintDragDivider(graphics, 0, 0, getWidth(), getHeight());
            }
        };
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintSplitPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        super.paint(graphics, this.splitPane);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintSplitPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void paintDragDivider(Graphics graphics, int i2, int i3, int i4, int i5) {
        SynthContext context = getContext(this.splitPane, Region.SPLIT_PANE_DIVIDER);
        context.setComponentState(((context.getComponentState() | 2) ^ 2) | 4);
        Shape clip = graphics.getClip();
        graphics.clipRect(i2, i3, i4, i5);
        context.getPainter().paintSplitPaneDragDivider(context, graphics, i2, i3, i4, i5, this.splitPane.getOrientation());
        graphics.setClip(clip);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI, javax.swing.plaf.SplitPaneUI
    public void finishedPaintingChildren(JSplitPane jSplitPane, Graphics graphics) {
        if (jSplitPane == this.splitPane && getLastDragLocation() != -1 && !isContinuousLayout() && !this.draggingHW) {
            if (jSplitPane.getOrientation() == 1) {
                paintDragDivider(graphics, getLastDragLocation(), 0, this.dividerSize - 1, this.splitPane.getHeight() - 1);
            } else {
                paintDragDivider(graphics, 0, getLastDragLocation(), this.splitPane.getWidth() - 1, this.dividerSize - 1);
            }
        }
    }
}
