package Q;

import R.k;
import R.m;
import bH.C;
import com.efiAnalytics.ui.InterfaceC1632dq;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.nntp.NNTPReply;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:Q/a.class */
public class a extends JPanel implements InterfaceC1632dq {

    /* renamed from: d, reason: collision with root package name */
    String f1768d;

    /* renamed from: e, reason: collision with root package name */
    String f1769e;

    /* renamed from: f, reason: collision with root package name */
    String f1770f;

    /* renamed from: g, reason: collision with root package name */
    String f1771g;

    /* renamed from: h, reason: collision with root package name */
    String f1772h;

    /* renamed from: i, reason: collision with root package name */
    String f1773i;

    /* renamed from: k, reason: collision with root package name */
    String f1775k;

    /* renamed from: a, reason: collision with root package name */
    JTextPane f1765a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JTextPane f1766b = new JTextPane();

    /* renamed from: c, reason: collision with root package name */
    JTextPane f1767c = new JTextPane();

    /* renamed from: j, reason: collision with root package name */
    int f1774j = 5;

    public a(String str, String str2, String str3, String str4, String str5, String str6) {
        this.f1768d = null;
        this.f1769e = null;
        this.f1770f = null;
        this.f1771g = null;
        this.f1772h = null;
        this.f1773i = null;
        this.f1775k = null;
        this.f1768d = str;
        this.f1769e = str2;
        this.f1770f = str3;
        this.f1771g = str4;
        this.f1772h = str5;
        this.f1773i = str6;
        setBorder(BorderFactory.createLoweredBevelBorder());
        this.f1775k = "<html><font size=\"" + eJ.a(12) + "\"><center><strong>" + C1818g.b(str4 + " " + str5 + " Review") + "</strong></font></center><br>" + C1818g.b("Briefly tell us about your experience using " + str4 + " " + str5) + ".<br> <br></html>";
        this.f1765a.setContentType(Clipboard.HTML_TYPE);
        this.f1765a.setText(this.f1775k);
        this.f1765a.setEditable(false);
        this.f1765a.setOpaque(true);
        setLayout(new BorderLayout());
        add("North", this.f1765a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(""));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel2.add(new JLabel(C1818g.b("Overall Rating (5-Good, 1-Problematic)") + CallSiteDescriptor.TOKEN_DELIMITER));
        b bVar = new b(this);
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton jRadioButton = new JRadioButton("5", true);
        jRadioButton.addActionListener(bVar);
        buttonGroup.add(jRadioButton);
        jPanel2.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton("4", false);
        jRadioButton2.addActionListener(bVar);
        buttonGroup.add(jRadioButton2);
        jPanel2.add(jRadioButton2);
        JRadioButton jRadioButton3 = new JRadioButton("3", false);
        jRadioButton3.addActionListener(bVar);
        buttonGroup.add(jRadioButton3);
        jPanel2.add(jRadioButton3);
        JRadioButton jRadioButton4 = new JRadioButton("2", false);
        jRadioButton4.addActionListener(bVar);
        buttonGroup.add(jRadioButton4);
        jPanel2.add(jRadioButton4);
        JRadioButton jRadioButton5 = new JRadioButton("1", false);
        jRadioButton5.addActionListener(bVar);
        buttonGroup.add(jRadioButton5);
        jPanel2.add(jRadioButton5);
        jPanel.add("North", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(2, 1));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("North", new JLabel(C1818g.b("Good Experiences") + CallSiteDescriptor.TOKEN_DELIMITER));
        this.f1766b.setEditable(true);
        jPanel4.add(BorderLayout.CENTER, new JScrollPane(this.f1766b));
        jPanel3.add(jPanel4);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add("North", new JLabel(C1818g.b("Improvements you would like to see") + CallSiteDescriptor.TOKEN_DELIMITER));
        this.f1767c.setEditable(true);
        jPanel5.add(BorderLayout.CENTER, new JScrollPane(this.f1767c));
        jPanel3.add(jPanel5);
        jPanel.add(BorderLayout.CENTER, jPanel3);
        add(BorderLayout.CENTER, jPanel);
    }

    public void a(Component component) {
        JDialog jDialog = new JDialog(bV.a(component), C1818g.b("User Feedback"));
        jDialog.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Complete Later"));
        jButton.addActionListener(new c(this, this, jDialog));
        JButton jButton2 = new JButton(C1818g.b("Submit Review"));
        jButton2.setDefaultCapable(true);
        jButton2.addActionListener(new d(this, this, jDialog));
        jDialog.enableInputMethods(true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        jDialog.add("South", jPanel);
        jDialog.pack();
        jDialog.setSize(eJ.a(640), eJ.a(NNTPReply.AUTHENTICATION_REQUIRED));
        bV.a((Window) bV.a(component), (Component) jDialog);
        jDialog.setVisible(true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1632dq
    public boolean a() {
        try {
            C.c("UserReview Queued:" + (!new m().a(this.f1768d, this.f1769e, this.f1770f, this.f1771g, this.f1772h, this.f1773i, this.f1774j, new StringBuilder().append(C1818g.c().getLanguage()).append("\nPros:\n").append(this.f1766b.getText()).append("\nWant:\n").append(this.f1767c.getText()).toString())));
            return true;
        } catch (k e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1632dq
    public boolean b() {
        bV.d(C1818g.b("You can return to the User Review Dialog at any time.") + "\n" + C1818g.b("Check the Main Menubar under Help"), this);
        return true;
    }
}
