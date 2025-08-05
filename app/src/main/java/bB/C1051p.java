package bb;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import s.C1818g;

/* renamed from: bb.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/p.class */
public class C1051p extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    final C1051p f7803a = this;

    /* renamed from: b, reason: collision with root package name */
    JPanel f7804b = new C1053r(this);

    /* renamed from: c, reason: collision with root package name */
    JPanel f7805c = null;

    /* renamed from: d, reason: collision with root package name */
    JLabel f7806d = new JLabel("", 0);

    /* renamed from: e, reason: collision with root package name */
    JLabel f7807e = new JLabel(C1818g.b("Please click Checkbox to continue."), 0);

    public C1051p(String str) throws IllegalArgumentException {
        this.f7806d.setText(str);
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f7806d);
        jPanel.add("South", this.f7807e);
        this.f7807e.setVisible(false);
        add("North", jPanel);
        this.f7806d.setFont(new Font(Font.DIALOG, 1, eJ.a() * 2));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.add(this.f7804b);
        add(BorderLayout.CENTER, jPanel2);
        this.f7804b.setLayout(new BoxLayout(this.f7804b, 1));
    }

    public synchronized boolean a(String str) {
        SwingUtilities.invokeLater(new RunnableC1052q(this, str));
        try {
            wait();
            return true;
        } catch (InterruptedException e2) {
            Logger.getLogger(C1051p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) throws IllegalArgumentException {
        s sVar = new s(this);
        sVar.a(str);
        this.f7804b.add(sVar);
        this.f7806d.setText(C1818g.b("Attention!!"));
        this.f7807e.setVisible(true);
        this.f7807e.repaint();
        validate();
    }
}
