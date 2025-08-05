package bt;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComboBox;

/* renamed from: bt.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/y.class */
public class C1366y extends JComboBox implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f9137a;

    /* renamed from: b, reason: collision with root package name */
    private int f9138b;

    public C1366y() {
        this("");
    }

    public C1366y(String str) {
        this.f9137a = new ArrayList();
        this.f9138b = 1;
        addItemListener(this);
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            if (getItemAt(i2).equals(str)) {
                super.setSelectedItem(getItemAt(i2));
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.JComboBox
    public void setSelectedItem(Object obj) {
        if (!(obj instanceof String) && !(obj instanceof C1367z)) {
            super.setSelectedItem(obj);
            return;
        }
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            if (getItemAt(i2).equals(obj)) {
                super.setSelectedItem(getItemAt(i2));
                return;
            }
        }
    }

    public void a(String str, String str2) {
        super.addItem(new C1367z(this, str, str2));
    }

    @Override // javax.swing.JComboBox
    public void addItem(Object obj) {
        if (obj instanceof String) {
            super.addItem(new C1367z(this, (String) obj));
        }
    }

    public String a(int i2) {
        Object itemAt = super.getItemAt(i2);
        if (!(itemAt instanceof C1367z)) {
            if (itemAt instanceof String) {
                return itemAt.toString();
            }
            return null;
        }
        C1367z c1367z = (C1367z) itemAt;
        if (c1367z != null) {
            return c1367z.a();
        }
        return null;
    }

    public String a() {
        Object selectedItem = getSelectedItem();
        if (!(selectedItem instanceof C1367z)) {
            if (selectedItem instanceof String) {
                return selectedItem.toString();
            }
            return null;
        }
        C1367z c1367z = (C1367z) selectedItem;
        if (c1367z != null) {
            return c1367z.a();
        }
        return null;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        int size = (int) ((getFont().getSize() * 1.9d) + ((this.f9138b - 1) * getFont().getSize()));
        preferredSize.height = size;
        if (preferredSize.width < 140) {
            preferredSize.setSize(140, size);
        }
        return preferredSize;
    }

    public void a(bX bXVar) {
        this.f9137a.add(bXVar);
    }

    public void b(bX bXVar) {
        this.f9137a.remove(bXVar);
    }

    public void b() {
        this.f9137a.clear();
    }

    private void c() {
        Iterator it = this.f9137a.iterator();
        while (it.hasNext()) {
            ((bX) it.next()).b(a());
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            c();
        }
    }

    @Override // javax.swing.JComboBox, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        repaint();
    }

    public boolean b(String str) {
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            Object itemAt = getItemAt(i2);
            if (itemAt instanceof C1367z) {
                if (((C1367z) itemAt).a().equals(str)) {
                    return true;
                }
            } else if ((itemAt instanceof String) && itemAt.toString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void b(int i2) {
        this.f9138b = i2;
    }
}
