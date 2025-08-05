package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/bW.class */
class bW implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bT f3018a;

    bW(bT bTVar) {
        this.f3018a = bTVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3018a.b(this.f3018a.getTitleAt(this.f3018a.getSelectedIndex()));
    }
}
