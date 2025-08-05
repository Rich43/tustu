package aP;

import com.efiAnalytics.ui.C1603cn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/bJ.class */
public class bJ extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    C1603cn f2980a;

    /* renamed from: b, reason: collision with root package name */
    C1603cn f2981b;

    /* renamed from: c, reason: collision with root package name */
    Frame f2982c;

    public bJ(Frame frame) {
        super(frame, "File Encryption Dialog");
        this.f2980a = new C1603cn();
        this.f2981b = new C1603cn();
        this.f2982c = null;
        this.f2982c = frame;
        setLayout(new GridLayout(0, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", new JLabel("Input File:"));
        jPanel.add(BorderLayout.CENTER, this.f2980a);
        JButton jButton = new JButton("Select Input");
        jButton.addActionListener(new bK(this));
        jPanel.add("East", jButton);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", new JLabel("Output File:"));
        jPanel2.add(BorderLayout.CENTER, this.f2981b);
        JButton jButton2 = new JButton("Select Output");
        jButton2.addActionListener(new bL(this));
        jPanel2.add("East", jButton2);
        add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0));
        JButton jButton3 = new JButton("Close");
        jButton3.addActionListener(new bM(this));
        jPanel3.add(jButton3);
        JButton jButton4 = new JButton("Encrypt / Decrypt");
        jButton4.addActionListener(new bN(this));
        jPanel3.add(jButton4);
        add(jPanel3);
    }

    public void a() {
        String strA = com.efiAnalytics.ui.bV.a((Component) this, "Select Input", new String[]{""}, "", C1798a.a().c("encrypDialogDirIn", "."), true);
        if (strA != null && !strA.isEmpty()) {
            C1798a.a().b("encrypDialogDirIn", new File(strA).getParent());
        }
        this.f2980a.a(strA);
    }

    public void b() {
        String strA = com.efiAnalytics.ui.bV.a((Component) this, "Select Output", new String[]{""}, "", C1798a.a().c("encrypDialogDir", "."), false);
        if (strA != null && !strA.isEmpty()) {
            C1798a.a().b("encrypDialogDir", new File(strA).getParent());
        }
        this.f2981b.a(strA);
    }

    public void c() {
        File file = new File(this.f2980a.a());
        if (!file.exists()) {
            com.efiAnalytics.ui.bV.d("Must Select an Input File", this);
            return;
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            File file2 = new File(this.f2981b.a());
            if (file2.exists()) {
                file2.delete();
            }
            try {
                file2.createNewFile();
                try {
                    W.ay ayVar = new W.ay(file2);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(ayVar);
                    try {
                        try {
                            for (int i2 = bufferedInputStream.read(); i2 != -1; i2 = bufferedInputStream.read()) {
                                bufferedOutputStream.write(i2);
                            }
                        } catch (IOException e2) {
                            Logger.getLogger(bJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            try {
                                bufferedOutputStream.flush();
                                bufferedOutputStream.close();
                                ayVar.flush();
                                ayVar.close();
                            } catch (IOException e3) {
                                Logger.getLogger(bJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                            }
                        }
                    } finally {
                        try {
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                            ayVar.flush();
                            ayVar.close();
                        } catch (IOException e4) {
                            Logger.getLogger(bJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                } catch (FileNotFoundException e5) {
                    com.efiAnalytics.ui.bV.d("Error reading file:\n" + file2.getAbsolutePath(), this);
                    e5.printStackTrace();
                }
            } catch (IOException e6) {
                e6.printStackTrace();
                com.efiAnalytics.ui.bV.d("Failed to create file:\n" + file2.getAbsolutePath(), this);
            }
        } catch (FileNotFoundException e7) {
            com.efiAnalytics.ui.bV.d("Error reading file:\n" + file.getAbsolutePath(), this);
        }
    }

    public static void a(Frame frame) {
        bJ bJVar = new bJ(frame);
        bJVar.pack();
        bJVar.setSize(600, bJVar.getHeight());
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) bJVar);
        bJVar.setVisible(true);
    }
}
