package com.efiAnalytics.ui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.ItemSelectable;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.icepdf.core.util.PdfOps;

/* renamed from: com.efiAnalytics.ui.dc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dc.class */
public class C1619dc extends C1605cp implements ItemSelectable, ActionListener {

    /* renamed from: z, reason: collision with root package name */
    ArrayList f11410z;

    /* renamed from: A, reason: collision with root package name */
    JPopupMenu f11411A;

    public C1619dc() {
        this("");
    }

    public C1619dc(String str) {
        super(str);
        this.f11410z = new ArrayList();
        this.f11411A = new JPopupMenu();
        add(this.f11411A);
        a(this);
        a(true);
        a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/down.gif")));
        a(new Dimension(9, 9));
        a(4);
    }

    public boolean b(String str) {
        a(str);
        for (int i2 = 0; i2 < this.f11411A.getComponentCount(); i2++) {
            JMenuItem jMenuItem = (JMenuItem) this.f11411A.getComponent(i2);
            if (jMenuItem.getText().equals(str) || (jMenuItem.getActionCommand() != null && jMenuItem.getActionCommand().equals(str))) {
                a(str);
                return true;
            }
        }
        return false;
    }

    public void c(String str) {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setActionCommand(str);
        jMenuItem.setText(bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, ""));
        jMenuItem.addActionListener(this);
        this.f11411A.add(jMenuItem);
    }

    private void b() {
        this.f11411A.show(this, 0, getHeight());
    }

    @Override // java.awt.ItemSelectable
    public void addItemListener(ItemListener itemListener) {
        this.f11410z.add(itemListener);
    }

    @Override // java.awt.ItemSelectable
    public void removeItemListener(ItemListener itemListener) {
        this.f11410z.remove(itemListener);
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        return new String[]{a()};
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if ((actionEvent.getSource() instanceof JMenuItem) && b(actionEvent.getActionCommand())) {
            d(actionEvent.getActionCommand());
        } else if (actionEvent.getSource() == this) {
            b();
        }
    }

    private void d(String str) {
        ItemEvent itemEvent = new ItemEvent(this, 701, str, 1);
        Iterator it = this.f11410z.iterator();
        while (it.hasNext()) {
            ((ItemListener) it.next()).itemStateChanged(itemEvent);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        this.f11411A.removeAll();
        a("");
    }

    @Override // com.efiAnalytics.ui.C1605cp, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int i2 = 0;
        for (int i3 = 0; i3 < this.f11411A.getComponentCount(); i3++) {
            int iStringWidth = fontMetrics.stringWidth(((JMenuItem) this.f11411A.getComponent(i3)).getText());
            i2 = iStringWidth > i2 ? iStringWidth : i2;
        }
        preferredSize.setSize(i2 + 18, preferredSize.getHeight());
        return preferredSize;
    }
}
