package ao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:ao/bJ.class */
public class bJ extends C0618ai implements ItemSelectable, ActionListener {

    /* renamed from: x, reason: collision with root package name */
    ArrayList f5315x;

    /* renamed from: y, reason: collision with root package name */
    PopupMenu f5316y;

    public bJ() {
        this("");
    }

    public bJ(String str) {
        super(str);
        this.f5315x = new ArrayList();
        this.f5316y = new PopupMenu();
        add(this.f5316y);
        this.f5316y.addActionListener(this);
        a(this);
    }

    @Override // ao.C0618ai, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f5185j || this.f5184i) {
            return;
        }
        graphics.setColor(Color.GRAY);
        graphics.draw3DRect(0, 0, getWidth(), getHeight(), false);
    }

    public boolean b(String str) {
        for (int i2 = 0; i2 < this.f5316y.getItemCount(); i2++) {
            if (this.f5316y.getItem(i2).getLabel().equals(str)) {
                a(str);
                return true;
            }
        }
        return false;
    }

    public void a(int i2) {
        b(this.f5316y.getItem(i2).getLabel());
    }

    private void e() {
        this.f5316y.show(this, 0, getHeight());
    }

    @Override // java.awt.ItemSelectable
    public void addItemListener(ItemListener itemListener) {
        this.f5315x.add(itemListener);
    }

    @Override // java.awt.ItemSelectable
    public void removeItemListener(ItemListener itemListener) {
        this.f5315x.remove(itemListener);
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        return new String[]{a()};
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f5316y && b(actionEvent.getActionCommand())) {
            d(actionEvent.getActionCommand());
        } else if (actionEvent.getSource() == this) {
            e();
        }
    }

    private void d(String str) {
        ItemEvent itemEvent = new ItemEvent(this, 701, str, 1);
        Iterator it = this.f5315x.iterator();
        while (it.hasNext()) {
            ((ItemListener) it.next()).itemStateChanged(itemEvent);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        this.f5316y.removeAll();
        a("");
    }

    public void c(String str) {
        this.f5316y.add(str);
    }

    public String b() {
        return a();
    }

    public String b(int i2) {
        return this.f5316y.getItem(i2).getLabel();
    }

    public int c() {
        return this.f5316y.getItemCount();
    }

    public int d() {
        for (int i2 = 0; i2 < this.f5316y.getItemCount(); i2++) {
            if (this.f5316y.getItem(i2).getLabel().equals(a())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // ao.C0618ai, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int i2 = 0;
        for (int i3 = 0; i3 < this.f5316y.getItemCount(); i3++) {
            int iStringWidth = fontMetrics.stringWidth(this.f5316y.getItem(i3).getLabel());
            i2 = iStringWidth > i2 ? iStringWidth : i2;
        }
        preferredSize.setSize(i2 + 18, preferredSize.getHeight());
        return preferredSize;
    }
}
