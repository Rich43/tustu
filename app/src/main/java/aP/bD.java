package aP;

import W.C0178d;
import W.C0200z;
import com.efiAnalytics.ui.C1603cn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/bD.class */
public class bD extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    C1603cn f2968a;

    /* renamed from: b, reason: collision with root package name */
    C1603cn f2969b;

    /* renamed from: c, reason: collision with root package name */
    JCheckBox f2970c;

    /* renamed from: d, reason: collision with root package name */
    JPasswordField f2971d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f2972e;

    /* renamed from: f, reason: collision with root package name */
    static String f2973f = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";

    /* renamed from: g, reason: collision with root package name */
    Frame f2974g;

    public bD(Frame frame) throws IllegalArgumentException {
        super(frame, "File Encryption & Decryption Dialog");
        this.f2968a = new C1603cn();
        this.f2969b = new C1603cn();
        this.f2970c = new JCheckBox("Encrypt Output", true);
        this.f2971d = new JPasswordField(16);
        this.f2972e = new JLabel("Output Password:");
        this.f2974g = null;
        this.f2974g = frame;
        setLayout(new GridLayout(0, 1, 1, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", new JLabel("Input File:"));
        jPanel.add(BorderLayout.CENTER, this.f2968a);
        JButton jButton = new JButton("Select Input");
        jButton.addActionListener(new bE(this));
        jPanel.add("East", jButton);
        if (aE.a.A() != null) {
            this.f2968a.a(aE.a.A().v());
        }
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", new JLabel("Output File:"));
        jPanel2.add(BorderLayout.CENTER, this.f2969b);
        JButton jButton2 = new JButton("Select Output");
        jButton2.addActionListener(new bF(this));
        jPanel2.add("East", jButton2);
        add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        this.f2970c.addActionListener(new bG(this));
        jPanel3.add(this.f2970c);
        jPanel3.add(this.f2972e);
        jPanel3.add(this.f2971d);
        add(jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(2));
        JButton jButton3 = new JButton("Create Output File");
        jButton3.addActionListener(new bH(this));
        jPanel4.add(jButton3);
        JButton jButton4 = new JButton("Close");
        jButton4.addActionListener(new bI(this));
        jPanel4.add(jButton4);
        add(jPanel4);
    }

    public void a() throws IllegalArgumentException {
        String[] strArr = {""};
        String absolutePath = ".";
        if (this.f2968a.a() != null && !this.f2968a.a().isEmpty()) {
            absolutePath = new File(this.f2968a.a()).getParentFile().getAbsolutePath();
        }
        String strA = com.efiAnalytics.ui.bV.a((Component) this, "Select Input", strArr, "", C1798a.a().c("encryptDialogDirIn", absolutePath), true);
        if (strA != null && !strA.isEmpty()) {
            C1798a.a().b("encryptDialogDirIn", new File(strA).getParent());
        }
        this.f2968a.a(strA);
    }

    public void b() throws IllegalArgumentException {
        String[] strArr = {""};
        String absolutePath = ".";
        if (this.f2968a.a() != null && !this.f2968a.a().isEmpty()) {
            absolutePath = new File(this.f2968a.a()).getParentFile().getAbsolutePath();
        }
        String strA = com.efiAnalytics.ui.bV.a((Component) this, "Select Output", strArr, "", C1798a.a().c("encryptDialogDir", absolutePath), false);
        if (strA != null && !strA.isEmpty()) {
            C1798a.a().b("encryptDialogDir", new File(strA).getParent());
        }
        this.f2969b.a(strA);
    }

    private boolean c() {
        char[] password = this.f2971d.getPassword();
        if (password.length < 8) {
            return false;
        }
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        for (char c2 : password) {
            if (!z2 && Character.isUpperCase(c2)) {
                z2 = true;
            } else if (!z3 && Character.isLowerCase(c2)) {
                z3 = true;
            } else if (!z4 && Character.isDigit(c2)) {
                z4 = true;
            } else if (!z5 && f2973f.indexOf(String.valueOf(c2)) >= 0) {
                z5 = true;
            }
        }
        return z2 && z3 && z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strA;
        File file = new File(this.f2968a.a());
        if (!file.exists()) {
            com.efiAnalytics.ui.bV.d("Must Select an Input File", this);
            return;
        }
        if (this.f2970c.isSelected() && !c()) {
            com.efiAnalytics.ui.bV.d("Password must be at least 6 characters long and contain:\n- At least 1 Upper Case Character\n- At least 1 Lower Case Character\n- At least 1 Numeric Character\n- Additional Special Characters Recommended (" + f2973f + ")", this);
            return;
        }
        byte[] bArrA = null;
        if (W.ak.b(file)) {
            int i2 = 0;
            while (bArrA == null) {
                int i3 = i2;
                i2++;
                if (i3 >= 3) {
                    break;
                }
                String strA2 = a("Enter the password for the Encrypted Input File:");
                if (strA2 == null) {
                    return;
                }
                try {
                    bArrA = new W.ak().a(file, strA2);
                } catch (W.aj e2) {
                    com.efiAnalytics.ui.bV.d("Invalid Password.", this);
                } catch (FileNotFoundException e3) {
                    com.efiAnalytics.ui.bV.d("Input File not found.", this);
                    return;
                } catch (IOException e4) {
                    com.efiAnalytics.ui.bV.d("Unable to read Input File.", this);
                    e4.printStackTrace();
                    return;
                }
            }
        } else {
            try {
                bArrA = C0178d.a(file);
            } catch (FileNotFoundException e5) {
                com.efiAnalytics.ui.bV.d("Input File not found.", this);
                return;
            } catch (IOException e6) {
                com.efiAnalytics.ui.bV.d("Unable to read Input File.", this);
                e6.printStackTrace();
                return;
            }
        }
        File file2 = new File(this.f2969b.a());
        if (file2.exists()) {
            file2.delete();
        }
        try {
            file2.createNewFile();
            if (!this.f2970c.isSelected()) {
                try {
                    C0178d.a(file2, bArrA);
                    com.efiAnalytics.ui.bV.d("Unencrypted file written to:\n" + file2.getAbsolutePath(), this);
                    return;
                } catch (IOException e7) {
                    com.efiAnalytics.ui.bV.d("Unable to Write Output File.\n" + e7.getMessage(), this);
                    Logger.getLogger(bD.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                    return;
                }
            }
            String strA3 = C0200z.a(file);
            double dC = C0200z.c(file);
            W.ak akVar = new W.ak();
            String str = new String(this.f2971d.getPassword());
            if (!str.isEmpty()) {
                do {
                    strA = a("Validate Output File Password:");
                    if (strA == null) {
                        return;
                    }
                    if (!strA.equals(str)) {
                        com.efiAnalytics.ui.bV.d("Validate Password does not match!", this);
                    }
                } while (!strA.equals(str));
            }
            try {
                akVar.a(bArrA, file2, str, strA3, dC);
                com.efiAnalytics.ui.bV.d("Encrypted file written to:\n" + file2.getAbsolutePath(), this);
            } catch (IOException e8) {
                Logger.getLogger(bD.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                com.efiAnalytics.ui.bV.d("Unable to Write Output File.\n" + e8.getMessage(), this);
            }
        } catch (IOException e9) {
            e9.printStackTrace();
            com.efiAnalytics.ui.bV.d("Failed to create file:\n" + file2.getAbsolutePath(), this);
        }
    }

    private String a(String str) {
        return com.efiAnalytics.ui.bV.a((Component) this, str);
    }

    public static void a(Frame frame) {
        bD bDVar = new bD(frame);
        bDVar.pack();
        bDVar.setSize(800, bDVar.getHeight());
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) bDVar);
        bDVar.setVisible(true);
    }
}
