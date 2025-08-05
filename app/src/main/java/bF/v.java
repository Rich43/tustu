package bF;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/* loaded from: TunerStudioMS.jar:bF/v.class */
class v extends DefaultTableCellRenderer {

    /* renamed from: c, reason: collision with root package name */
    JTable f6890c;

    /* renamed from: b, reason: collision with root package name */
    Color f6888b = Color.lightGray;

    /* renamed from: a, reason: collision with root package name */
    private Color f6889a = UIManager.getColor("Label.background").brighter().brighter();

    /* renamed from: d, reason: collision with root package name */
    private boolean f6891d = true;

    public v(JTable jTable) {
        this.f6890c = jTable;
        setHorizontalAlignment(0);
        jTable.getColumnModel().getSelectionModel().addListSelectionListener(new w(this));
        setBackground(Color.lightGray);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f6891d) {
            graphics.setColor(this.f6888b);
            graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, true);
        }
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
        Color background = getBackground();
        if (a(this.f6890c.getSelectedColumns(), i3)) {
            background = a();
        }
        tableCellRendererComponent.setBackground(background);
        return tableCellRendererComponent;
    }

    private boolean a(int[] iArr, int i2) {
        for (int i3 : iArr) {
            if (i3 == i2) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.JLabel
    public String getText() {
        String text = super.getText();
        if (text != null) {
        }
        return text;
    }

    public void a(boolean z2) {
        this.f6891d = z2;
    }

    public Color a() {
        return this.f6889a;
    }
}
