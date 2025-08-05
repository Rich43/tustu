package aA;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.TextField;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aA/e.class */
class e extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    TextField f2255a;

    /* renamed from: b, reason: collision with root package name */
    JLabel f2256b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f2257c;

    e(a aVar, String str, String str2) {
        this(aVar, str, str2, false);
    }

    e(a aVar, String str, String str2, boolean z2) {
        this.f2257c = aVar;
        this.f2255a = null;
        this.f2256b = null;
        setLayout(new BorderLayout(eJ.a(10), eJ.a(10)));
        setBorder(BorderFactory.createEmptyBorder(eJ.a(1), eJ.a(10), eJ.a(1), eJ.a(10)));
        JLabel jLabel = new JLabel(aVar.a(str));
        jLabel.setHorizontalAlignment(4);
        add(BorderLayout.CENTER, jLabel);
        if (z2) {
            this.f2256b = new JLabel(str2);
            add("East", this.f2256b);
        } else {
            this.f2255a = new TextField(str2);
            this.f2255a.addFocusListener(new f(this));
            this.f2255a.setColumns(30);
            add("East", this.f2255a);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void requestFocus() {
        if (this.f2255a != null) {
            this.f2255a.requestFocus();
        } else {
            super.requestFocus();
        }
    }

    public String a() {
        return this.f2256b == null ? this.f2255a.getText() : this.f2256b.getText();
    }

    public void a(String str) throws IllegalArgumentException {
        if (this.f2256b == null) {
            this.f2255a.setText(str);
        } else {
            this.f2256b.setText(str);
        }
    }
}
