package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;

/* renamed from: aP.fo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/fo.class */
class C0353fo implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3385a;

    C0353fo(C0308dx c0308dx) {
        this.f3385a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13323ae, Boolean.toString(((bA.c) actionEvent.getSource()).isSelected()));
    }
}
