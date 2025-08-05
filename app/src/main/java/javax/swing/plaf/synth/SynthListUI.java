package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicListUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthListUI.class */
public class SynthListUI extends BasicListUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private boolean useListColors;
    private boolean useUIBorder;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthListUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintListBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        context.dispose();
        paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintListBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicListUI
    protected void installListeners() {
        super.installListeners();
        this.list.addPropertyChangeListener(this);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JList) propertyChangeEvent.getSource());
        }
    }

    @Override // javax.swing.plaf.basic.BasicListUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.list.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicListUI
    protected void installDefaults() {
        if (this.list.getCellRenderer() == null || (this.list.getCellRenderer() instanceof UIResource)) {
            this.list.setCellRenderer(new SynthListCellRenderer());
        }
        updateStyle(this.list);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(this.list, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            context.setComponentState(512);
            Color selectionBackground = this.list.getSelectionBackground();
            if (selectionBackground == null || (selectionBackground instanceof UIResource)) {
                this.list.setSelectionBackground(this.style.getColor(context, ColorType.TEXT_BACKGROUND));
            }
            Color selectionForeground = this.list.getSelectionForeground();
            if (selectionForeground == null || (selectionForeground instanceof UIResource)) {
                this.list.setSelectionForeground(this.style.getColor(context, ColorType.TEXT_FOREGROUND));
            }
            this.useListColors = this.style.getBoolean(context, "List.rendererUseListColors", true);
            this.useUIBorder = this.style.getBoolean(context, "List.rendererUseUIBorder", true);
            int i2 = this.style.getInt(context, "List.cellHeight", -1);
            if (i2 != -1) {
                this.list.setFixedCellHeight(i2);
            }
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicListUI
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        SynthContext context = getContext(this.list, 1);
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
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthListUI$SynthListCellRenderer.class */
    private class SynthListCellRenderer extends DefaultListCellRenderer.UIResource {
        private SynthListCellRenderer() {
        }

        @Override // java.awt.Component
        public String getName() {
            return "List.cellRenderer";
        }

        @Override // javax.swing.JComponent
        public void setBorder(Border border) {
            if (SynthListUI.this.useUIBorder || (border instanceof SynthBorder)) {
                super.setBorder(border);
            }
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            if (!SynthListUI.this.useListColors && (z2 || z3)) {
                SynthLookAndFeel.setSelectedUI((SynthLabelUI) SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), z2, z3, jList.isEnabled(), false);
            } else {
                SynthLookAndFeel.resetSelectedUI();
            }
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            return this;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            super.paint(graphics);
            SynthLookAndFeel.resetSelectedUI();
        }
    }
}
