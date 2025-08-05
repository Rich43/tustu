package aP;

import bt.InterfaceC1281I;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: aP.hg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hg.class */
class C0399hg extends JTextPane implements InterfaceC1281I {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0394hb f3575a;

    C0399hg(C0394hb c0394hb) {
        this.f3575a = c0394hb;
        setEditable(false);
        setBackground(UIManager.getColor("Label.background"));
        setForeground(UIManager.getColor("Label.foreground"));
    }

    @Override // bt.InterfaceC1281I
    public void a(String str) {
        String strW = this.f3575a.f3556a.w(str);
        if (strW == null) {
            setText("");
        } else {
            setText(C1818g.b(strW));
            setCaretPosition(0);
        }
    }

    @Override // bt.InterfaceC1281I
    public void b(String str) {
        setText("");
    }
}
