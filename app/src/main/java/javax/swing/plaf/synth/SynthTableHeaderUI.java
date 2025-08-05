package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import sun.swing.table.DefaultTableCellHeaderRenderer;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTableHeaderUI.class */
public class SynthTableHeaderUI extends BasicTableHeaderUI implements PropertyChangeListener, SynthUI {
    private TableCellRenderer prevRenderer = null;
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTableHeaderUI();
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void installDefaults() {
        this.prevRenderer = this.header.getDefaultRenderer();
        if (this.prevRenderer instanceof UIResource) {
            this.header.setDefaultRenderer(new HeaderRenderer());
        }
        updateStyle(this.header);
    }

    private void updateStyle(JTableHeader jTableHeader) {
        SynthContext context = getContext(jTableHeader, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle && synthStyle != null) {
            uninstallKeyboardActions();
            installKeyboardActions();
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void installListeners() {
        super.installListeners();
        this.header.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void uninstallDefaults() {
        if (this.header.getDefaultRenderer() instanceof HeaderRenderer) {
            this.header.setDefaultRenderer(this.prevRenderer);
        }
        SynthContext context = getContext(this.header, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void uninstallListeners() {
        this.header.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintTableHeaderBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        super.paint(graphics, synthContext.getComponent());
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTableHeaderBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void rolloverColumnUpdated(int i2, int i3) {
        this.header.repaint(this.header.getHeaderRect(i2));
        this.header.repaint(this.header.getHeaderRect(i3));
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTableHeader) propertyChangeEvent.getSource());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTableHeaderUI$HeaderRenderer.class */
    private class HeaderRenderer extends DefaultTableCellHeaderRenderer {
        HeaderRenderer() {
            setHorizontalAlignment(10);
            setName("TableHeader.renderer");
        }

        @Override // sun.swing.table.DefaultTableCellHeaderRenderer, javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            boolean z4 = i3 == SynthTableHeaderUI.this.getRolloverColumn();
            if (z2 || z4 || z3) {
                SynthLookAndFeel.setSelectedUI((SynthLabelUI) SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), z2, z3, jTable.isEnabled(), z4);
            } else {
                SynthLookAndFeel.resetSelectedUI();
            }
            RowSorter<? extends TableModel> rowSorter = jTable == null ? null : jTable.getRowSorter();
            List<? extends RowSorter.SortKey> sortKeys = rowSorter == null ? null : rowSorter.getSortKeys();
            if (sortKeys != null && sortKeys.size() > 0 && sortKeys.get(0).getColumn() == jTable.convertColumnIndexToModel(i3)) {
                switch (sortKeys.get(0).getSortOrder()) {
                    case ASCENDING:
                        putClientProperty("Table.sortOrder", "ASCENDING");
                        break;
                    case DESCENDING:
                        putClientProperty("Table.sortOrder", "DESCENDING");
                        break;
                    case UNSORTED:
                        putClientProperty("Table.sortOrder", "UNSORTED");
                        break;
                    default:
                        throw new AssertionError((Object) "Cannot happen");
                }
            } else {
                putClientProperty("Table.sortOrder", "UNSORTED");
            }
            super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
            return this;
        }

        @Override // javax.swing.JComponent
        public void setBorder(Border border) {
            if (border instanceof SynthBorder) {
                super.setBorder(border);
            }
        }
    }
}
