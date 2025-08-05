package bx;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/* loaded from: TunerStudioMS.jar:bx/q.class */
public class q extends JTextField {

    /* renamed from: a, reason: collision with root package name */
    int f9218a;

    public q() {
        this("");
    }

    public q(String str) {
        this(str, 10);
    }

    public q(String str, int i2) {
        super(str, i2);
        this.f9218a = 20;
        a();
    }

    private void a() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new r(this));
    }
}
