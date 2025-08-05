package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/l.class */
class l extends JList {

    /* renamed from: a, reason: collision with root package name */
    DefaultListModel f10572a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f10573b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10574c;

    public l(OutputPortEditor outputPortEditor) {
        this.f10574c = outputPortEditor;
        this.f10572a = null;
        this.f10572a = new DefaultListModel();
        super.setModel(this.f10572a);
        super.setCellRenderer(new n(outputPortEditor));
        super.addListSelectionListener(new m(this, outputPortEditor));
        setSelectionMode(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        super.getSelectedValue();
        Iterator it = this.f10573b.iterator();
        while (it.hasNext()) {
            ((ActionListener) it.next()).actionPerformed(new ActionEvent(this, 1001, ""));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(o oVar) {
        this.f10572a.addElement(oVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(ActionListener actionListener) {
        this.f10573b.add(actionListener);
    }

    public Object a() {
        return super.getSelectedValue();
    }

    public int b() {
        return this.f10572a.getSize();
    }

    public Object a(int i2) {
        return this.f10572a.elementAt(i2);
    }
}
