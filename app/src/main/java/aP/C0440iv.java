package aP;

import bt.C1351j;
import c.C1382a;
import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: aP.iv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/iv.class */
public class C0440iv extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    static String f3755a = "$controllerDescription";

    /* renamed from: b, reason: collision with root package name */
    aE.a f3756b;

    /* renamed from: c, reason: collision with root package name */
    G.bS f3757c;

    /* renamed from: d, reason: collision with root package name */
    String f3758d;

    /* renamed from: e, reason: collision with root package name */
    String f3759e;

    /* renamed from: f, reason: collision with root package name */
    boolean f3760f;

    public C0440iv(Frame frame, aE.a aVar, String str, G.bS bSVar, String str2) {
        super(frame, C1818g.b("Signature Mismatch"), true);
        this.f3756b = null;
        this.f3757c = null;
        this.f3758d = "";
        this.f3760f = false;
        this.f3756b = aVar;
        this.f3759e = str;
        this.f3758d = str2;
        this.f3757c = bSVar;
        setLayout(new BorderLayout());
        JTextPane jTextPane = new JTextPane();
        jTextPane.setForeground(Color.BLACK);
        jTextPane.setEditable(false);
        jTextPane.setContentType(Clipboard.HTML_TYPE);
        jTextPane.setText(bH.W.b(bH.W.b(bH.W.b(a(str2), "$configSig", str2), "$controllerSig", bSVar.b()), f3755a, bSVar.c()));
        jTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, jTextPane);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout());
        JButton jButton = new JButton(C1818g.b("Open Different Project"));
        jButton.addActionListener(new C0441iw(this));
        jPanel2.add(jButton);
        JButton jButton2 = new JButton(C1818g.b("Update ECU Definition"));
        jButton2.addActionListener(new C0442ix(this));
        jPanel2.add(jButton2);
        JButton jButton3 = new JButton(C1818g.b("Connect Anyway"));
        jButton3.addActionListener(new C0444iz(this));
        jPanel2.add(jButton3);
        JButton jButton4 = new JButton(C1818g.b("Work offline"));
        jButton4.addActionListener(new iA(this));
        jPanel2.add(jButton4);
        jPanel2.add(new C1351j((((((((((((((C1818g.b("This message indicates that your current project is configured to work with a different firmware than found on the ECU.") + "\n") + C1818g.b("You must select one of the following choices to proceed:")) + "\n") + "\n") + C1818g.b("Open a Different Project - If you have a different project already created that is normally used with the attached ECU, use this option.")) + "\n") + "\n") + C1818g.b("Update ECU Definition - By Selecting this option, " + C1798a.f13268b + " will attempt to update the ECU Definition (AKA ini file) for this project to one that will work with the firmware on the attached ECU. This is commonly the best option after updating firmware versions where it is the same car and ECU, just a newer version of firmware. It is not advised to used this option if it is a different ECU family.")) + "\n") + "\n") + C1818g.b("Connect Anyway - Only to be used if you know what you are doing! This could lead to corruption of your tune.")) + "\n") + "\n") + C1818g.b("Work offline - This will close this dialog and go offline. This is a safe option if you do not know what to do or want to create a new project to work with this ECU.")));
        add("South", jPanel2);
        pack();
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        if (SwingUtilities.isEventDispatchThread()) {
            pack();
            super.setVisible(z2);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(new iB(this, z2));
        } catch (InterruptedException e2) {
            Logger.getLogger(C0440iv.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C0440iv.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    private String a(String str) {
        boolean zA = C1806i.a().a(" ,SD;LKODGPOIGD9");
        String str2 = "<br><center><b>" + C1818g.b("Firmware signature Mismatch!!!!") + "</b></center><br>";
        if (!zA) {
            str2 = str2 + C1818g.b("Your " + C1382a.a(str, C1798a.f13272f) + " is using firmware:") + "<br>\n<center>'" + f3755a + "'</center><br>\n<p>";
        }
        return str2 + C1818g.b("For " + C1798a.f13268b + " to communicate correctly") + ", <br>" + C1818g.b("the Serial Signature for your project must match what is reported by the " + C1382a.a(str, C1798a.f13273g) + ".") + " <br></p><br>\n&nbsp;&nbsp;&nbsp;&nbsp;<b>" + C1818g.b("Project Serial signature") + ":</b> '$configSig'<br>\n&nbsp;&nbsp;&nbsp;&nbsp;<b>" + C1382a.a(str, C1798a.f13272f) + " " + C1818g.b("Serial signature") + ": </b> '$controllerSig'<br><br>\n<p>" + C1818g.b("It is not recommended that you connect with the incorrect ecu definition!") + "<br>" + C1818g.b("To correct this, up date the ECU Definition in your project to match that on the " + C1382a.a(str, C1798a.f13272f) + ".") + "<br></p><br>\n<center><b>" + C1818g.b("How would you like " + C1798a.f13268b + " to handle this?") + "</b></center><br> ";
    }

    public void a() {
        C0338f.a().c((Window) cZ.a().c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (new iC().a(this.f3756b, this.f3759e, this.f3758d, this.f3757c)) {
            dispose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.f3760f = true;
        dispose();
    }

    public boolean b() {
        return this.f3760f;
    }
}
