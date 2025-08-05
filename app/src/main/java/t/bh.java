package t;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:t/bh.class */
class bh implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ be f13838a;

    bh(be beVar) {
        this.f13838a = beVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }
}
