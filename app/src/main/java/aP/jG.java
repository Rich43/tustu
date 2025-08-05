package aP;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/jG.class */
public class jG extends JPanel implements jF {

    /* renamed from: a, reason: collision with root package name */
    G.R f3775a = null;

    /* renamed from: b, reason: collision with root package name */
    JTextPane f3776b = new JTextPane();

    public jG() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Tuners Log")));
        add("North", new JLabel("<html>" + C1818g.b("Notes and Comments for the current tune") + "</html>"));
        add(BorderLayout.CENTER, new JScrollPane(this.f3776b));
        this.f3776b.addFocusListener(new jH(this));
    }

    public void e(G.R r2) {
        this.f3775a = r2;
        if (r2 == null) {
            this.f3776b.setEnabled(false);
            this.f3776b.setText("");
            return;
        }
        this.f3776b.setEnabled(true);
        if (this.f3775a.Q() != null && this.f3775a.Q().length() > 32768) {
            this.f3775a.v(bH.W.b(bH.W.b(bH.W.b(this.f3775a.Q(), "<br><br><br><br><br><br><br><br><br><br><br><br><br><br>", ""), "<br><br> <br><br> <br><br> <br><br> <br><br> <br><br> ", ""), "    <br>\n    <br>\n    <br>\n    <br>\n    <br>\n    <br>\n    <br>\n    <br>\n", ""));
            if (this.f3775a.Q() != null && this.f3775a.Q().length() > 65536) {
                this.f3775a.v(this.f3775a.Q().substring(0, 65536));
            }
        }
        String strQ = this.f3775a.Q();
        if (strQ.contains("<br>")) {
            strQ = a(this.f3775a.Q());
            this.f3775a.v(strQ);
        }
        this.f3776b.setText(strQ);
        this.f3776b.setCaretPosition(0);
    }

    private String a(String str) {
        return bH.W.b(bH.W.b(str, "<br>", "\n").replaceAll("(?<=<p)(.*)(?=>)", ""), LineSeparator.Macintosh, "").replaceAll("<p>", "\n").replaceAll("</p>", "");
    }

    public G.R a() {
        return this.f3775a;
    }

    public void b() {
        if (this.f3775a != null) {
            this.f3775a.v(this.f3776b.getText());
            bH.C.c("TuneLog Editor set text");
        }
    }

    public void c() {
        if (this.f3775a == null || this.f3776b.getText().equals(this.f3775a.Q())) {
            return;
        }
        this.f3776b.setText(this.f3775a.Q());
        this.f3776b.setCaretPosition(0);
        bH.C.c("TuneLog Editor loaded text");
    }

    @Override // aP.jF
    public void a(G.R r2) {
    }

    @Override // aP.jF
    public void b(G.R r2) {
        if (this.f3775a == null || !this.f3775a.equals(r2)) {
            return;
        }
        c();
    }

    @Override // aP.jF
    public void c(G.R r2) {
        if (this.f3775a == null || !this.f3775a.equals(r2)) {
            return;
        }
        b();
    }

    @Override // aP.jF
    public void d(G.R r2) {
    }
}
