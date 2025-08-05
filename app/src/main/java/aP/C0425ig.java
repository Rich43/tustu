package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.ig, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ig.class */
class C0425ig implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0423id f3730a;

    C0425ig(C0423id c0423id) {
        this.f3730a = c0423id;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3730a.a(actionEvent.getActionCommand());
    }
}
