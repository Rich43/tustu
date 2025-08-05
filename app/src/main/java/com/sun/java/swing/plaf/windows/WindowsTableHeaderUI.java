package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SortOrder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import sun.swing.SwingUtilities2;
import sun.swing.table.DefaultTableCellHeaderRenderer;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTableHeaderUI.class */
public class WindowsTableHeaderUI extends BasicTableHeaderUI {
    private TableCellRenderer originalHeaderRenderer;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsTableHeaderUI();
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        if (XPStyle.getXP() != null) {
            this.originalHeaderRenderer = this.header.getDefaultRenderer();
            if (this.originalHeaderRenderer instanceof UIResource) {
                this.header.setDefaultRenderer(new XPDefaultRenderer());
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        if (this.header.getDefaultRenderer() instanceof XPDefaultRenderer) {
            this.header.setDefaultRenderer(this.originalHeaderRenderer);
        }
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void rolloverColumnUpdated(int i2, int i3) {
        if (XPStyle.getXP() != null) {
            this.header.repaint(this.header.getHeaderRect(i2));
            this.header.repaint(this.header.getHeaderRect(i3));
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTableHeaderUI$XPDefaultRenderer.class */
    private class XPDefaultRenderer extends DefaultTableCellHeaderRenderer {
        XPStyle.Skin skin;
        boolean isSelected;
        boolean hasFocus;
        boolean hasRollover;
        int column;

        XPDefaultRenderer() {
            setHorizontalAlignment(10);
        }

        /* JADX WARN: Removed duplicated region for block: B:44:0x016a  */
        @Override // sun.swing.table.DefaultTableCellHeaderRenderer, javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable r9, java.lang.Object r10, boolean r11, boolean r12, int r13, int r14) {
            /*
                Method dump skipped, instructions count: 390
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.java.swing.plaf.windows.WindowsTableHeaderUI.XPDefaultRenderer.getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int):java.awt.Component");
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            SortOrder columnSortOrder;
            Dimension size = getSize();
            TMSchema.State state = TMSchema.State.NORMAL;
            TableColumn draggedColumn = WindowsTableHeaderUI.this.header.getDraggedColumn();
            if (draggedColumn != null && this.column == SwingUtilities2.convertColumnIndexToView(WindowsTableHeaderUI.this.header.getColumnModel(), draggedColumn.getModelIndex())) {
                state = TMSchema.State.PRESSED;
            } else if (this.isSelected || this.hasFocus || this.hasRollover) {
                state = TMSchema.State.HOT;
            }
            if (WindowsLookAndFeel.isOnVista() && (columnSortOrder = getColumnSortOrder(WindowsTableHeaderUI.this.header.getTable(), this.column)) != null) {
                switch (columnSortOrder) {
                    case ASCENDING:
                    case DESCENDING:
                        switch (state) {
                            case NORMAL:
                                state = TMSchema.State.SORTEDNORMAL;
                                break;
                            case PRESSED:
                                state = TMSchema.State.SORTEDPRESSED;
                                break;
                            case HOT:
                                state = TMSchema.State.SORTEDHOT;
                                break;
                        }
                }
            }
            this.skin.paintSkin(graphics, 0, 0, size.width - 1, size.height - 1, state);
            super.paint(graphics);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTableHeaderUI$IconBorder.class */
    private static class IconBorder implements Border, UIResource {
        private final Icon icon;
        private final int top;
        private final int left;
        private final int bottom;
        private final int right;

        public IconBorder(Icon icon, int i2, int i3, int i4, int i5) {
            this.icon = icon;
            this.top = i2;
            this.left = i3;
            this.bottom = i4;
            this.right = i5;
        }

        @Override // javax.swing.border.Border
        public Insets getBorderInsets(Component component) {
            return new Insets(this.icon.getIconHeight() + this.top, this.left, this.bottom, this.right);
        }

        @Override // javax.swing.border.Border
        public boolean isBorderOpaque() {
            return false;
        }

        @Override // javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            this.icon.paintIcon(component, graphics, i2 + this.left + ((((i4 - this.left) - this.right) - this.icon.getIconWidth()) / 2), i3 + this.top);
        }
    }
}
