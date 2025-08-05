package bt;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/* loaded from: TunerStudioMS.jar:bt/bI.class */
public class bI extends JTextField {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8911a;

    /* renamed from: b, reason: collision with root package name */
    private int f8912b;

    public bI() {
        this("", 20);
    }

    public bI(String str, int i2) {
        super(str, i2);
        this.f8911a = new ArrayList();
        this.f8912b = 1000000;
        addFocusListener(new bK(this));
        addKeyListener(new bL(this));
        setBorder(BorderFactory.createLoweredBevelBorder());
        a();
    }

    @Override // javax.swing.JTextField, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < 60) {
            preferredSize.setSize(60, preferredSize.height);
        }
        return preferredSize;
    }

    public void a(bX bXVar) {
        this.f8911a.add(bXVar);
    }

    public void b(bX bXVar) {
        this.f8911a.remove(bXVar);
    }

    private void a() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new bJ(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        Iterator it = this.f8911a.iterator();
        while (it.hasNext()) {
            ((bX) it.next()).b(getText());
        }
    }

    public void a(int i2) {
        this.f8912b = i2;
    }
}
