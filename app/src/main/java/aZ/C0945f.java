package az;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import f.C1719a;
import f.C1720b;
import f.C1722d;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import org.icepdf.ri.common.FileExtensionUtils;

/* renamed from: az.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:az/f.class */
public class C0945f extends JDialog implements ClipboardOwner {

    /* renamed from: a, reason: collision with root package name */
    C1720b f6480a;

    /* renamed from: b, reason: collision with root package name */
    JTextPane f6481b;

    /* renamed from: c, reason: collision with root package name */
    JTextPane f6482c;

    /* renamed from: d, reason: collision with root package name */
    C1719a f6483d;

    /* renamed from: e, reason: collision with root package name */
    aa f6484e;

    /* renamed from: f, reason: collision with root package name */
    InterfaceC0943d f6485f;

    /* renamed from: g, reason: collision with root package name */
    JButton f6486g;

    /* renamed from: h, reason: collision with root package name */
    JButton f6487h;

    public C0945f(Window window, InterfaceC0943d interfaceC0943d, aa aaVar, C1720b c1720b) throws IllegalArgumentException {
        super(window, aaVar.a("Offline Activate") + " " + W.b(interfaceC0943d.a(), "Lite!", ""), Dialog.ModalityType.APPLICATION_MODAL);
        this.f6481b = new JTextPane();
        this.f6482c = new JTextPane();
        this.f6483d = null;
        this.f6484e = null;
        this.f6485f = null;
        this.f6486g = null;
        this.f6487h = null;
        this.f6484e = aaVar;
        this.f6480a = c1720b;
        this.f6485f = interfaceC0943d;
        c();
    }

    private void c() throws IllegalArgumentException {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(this.f6485f.f() + " " + this.f6484e.a("Offline Registration Activation")));
        this.f6486g = new JButton(this.f6484e.a(XIncludeHandler.HTTP_ACCEPT));
        this.f6487h = new JButton(this.f6484e.a("Cancel"));
        JLabel jLabel = new JLabel();
        jLabel.setText("<html><body><H1>" + this.f6484e.a("5 Step Offline Activation") + "</H1><strong>" + this.f6484e.a("Step 1") + "</strong> - " + this.f6484e.a("Save Activation Request to File ActivationRequest.txt on a USB drive or other medium.") + "<br><strong>" + this.f6484e.a("Step 2") + "</strong> - " + this.f6484e.a("On a Computer that is connected to the Internet, open a web browser and go to") + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"blue\"><u>http://www.efianalytics.com/activate</u></font><br><strong>" + this.f6484e.a("Step 3") + "</strong> - " + this.f6484e.a("Upload your saved ActivationRequest.txt, the site will provide you with ActivationCode.txt") + "<br><strong>" + this.f6484e.a("Step 4") + "</strong> - " + this.f6484e.a("Return to TunerStudio and click Load Activation From File to load ActivationCode.txt into TunerStudio") + "<br><strong>" + this.f6484e.a("Step 5") + "</strong> - " + this.f6484e.a("Click Accept") + "<br>" + this.f6484e.a("Done!") + "</body></html>");
        jLabel.addMouseListener(new C0946g(this));
        jPanel.add("North", jLabel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBorder(BorderFactory.createTitledBorder(this.f6484e.a("Activation Request")));
        int i2 = Toolkit.getDefaultToolkit().getScreenSize().height < 640 ? 60 : 120;
        this.f6481b.setMinimumSize(new Dimension(520, i2));
        this.f6481b.setPreferredSize(new Dimension(520, i2));
        this.f6481b.setMaximumSize(new Dimension(520, i2));
        this.f6481b.setBorder(BorderFactory.createBevelBorder(1));
        this.f6481b.setEditable(false);
        this.f6481b.setBackground(Color.LIGHT_GRAY);
        try {
            this.f6481b.setText(this.f6480a.a());
            this.f6481b.selectAll();
        } catch (IOException e2) {
            bV.d(e2.getMessage(), this);
            Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        jPanel2.add(BorderLayout.CENTER, this.f6481b);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        jPanel3.add(new JLabel("Step 1 -->"));
        JButton jButton = new JButton(this.f6484e.a("Save Request to File"));
        jButton.addActionListener(new C0947h(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(this.f6484e.a("Copy Request to Clipboard"));
        jButton2.addActionListener(new C0948i(this));
        jPanel3.add(jButton2);
        jPanel2.add("South", jPanel3);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.setBorder(BorderFactory.createTitledBorder(this.f6484e.a("Server Activation Code")));
        this.f6482c.setMinimumSize(new Dimension(520, 120));
        this.f6482c.setPreferredSize(new Dimension(520, 120));
        this.f6482c.setMaximumSize(new Dimension(520, 120));
        this.f6482c.setBorder(BorderFactory.createBevelBorder(1));
        this.f6482c.getDocument().addDocumentListener(new C0953n(this));
        jPanel4.add(BorderLayout.CENTER, this.f6482c);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(2));
        jPanel5.add(new JLabel("Step 4 -->"));
        JButton jButton3 = new JButton(this.f6484e.a("Load Activation From File"));
        jButton3.addActionListener(new C0949j(this));
        jPanel5.add(jButton3);
        JButton jButton4 = new JButton(this.f6484e.a("Paste Activation Code"));
        jButton4.addActionListener(new C0950k(this));
        jPanel5.add(jButton4);
        jPanel4.add("South", jPanel5);
        jPanel.add("South", jPanel4);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new FlowLayout(2));
        this.f6486g.addActionListener(new C0951l(this));
        this.f6487h.addActionListener(new C0952m(this));
        jPanel6.add(new JLabel("Step 5 -->"));
        jPanel6.add(this.f6486g);
        jPanel6.add(this.f6487h);
        this.f6486g.setEnabled(false);
        add("South", jPanel6);
        add(BorderLayout.CENTER, jPanel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        this.f6483d = null;
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f6481b.selectAll();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.f6481b.getText()), this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        String strB = bV.b(this, this.f6484e.a("Load Activation From File"), new String[]{FileExtensionUtils.txt}, "*.txt", this.f6485f.b());
        if (strB == null || strB.equals("")) {
            return;
        }
        File file = new File(strB);
        FileReader fileReader = null;
        try {
            try {
                fileReader = new FileReader(file);
                StringBuilder sb = new StringBuilder();
                for (int i2 = fileReader.read(); i2 != -1; i2 = fileReader.read()) {
                    sb.append((char) i2);
                }
                this.f6482c.setText(sb.toString());
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            } catch (IOException e3) {
                bV.d("unable to read file:\n" + file.getAbsolutePath(), this);
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e4) {
                        Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
            }
        } catch (Throwable th) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e5) {
                    Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        this.f6481b.selectAll();
        String text = this.f6481b.getText();
        String strA = bV.a(this, "Save Activation Request to File", new String[]{FileExtensionUtils.txt}, this.f6485f.f() + "ActivationRequest.txt", this.f6485f.b());
        if (strA == null || strA.equals("")) {
            return;
        }
        File file = new File(strA);
        FileWriter fileWriter = null;
        try {
            try {
                if (file.exists()) {
                    file.delete();
                }
                if (file.createNewFile()) {
                    fileWriter = new FileWriter(file);
                    fileWriter.write(text);
                } else {
                    bV.d("Can not write file to:\n" + file.getAbsolutePath(), this);
                }
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (IOException e3) {
                bV.d("Can not write to file:\n" + file.getAbsolutePath(), this);
                if (0 != 0) {
                    try {
                        fileWriter.close();
                    } catch (Exception e4) {
                    }
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    fileWriter.close();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    public String a() {
        return this.f6482c.getText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() throws HeadlessException {
        try {
            this.f6482c.setText(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
        } catch (UnsupportedFlavorException e2) {
            Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0945f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        String strA = a();
        if (strA != null && !strA.trim().equals("")) {
            try {
                this.f6483d = new C1719a(strA);
                this.f6483d.i(strA);
                if (this.f6483d.h().before(new Date())) {
                    bV.d("This Activation code has expired. Please request a new one from:\nhttps://www.efianalytics.com/activate", this);
                    return;
                }
                C1722d c1722dA = o.d().a(this.f6483d);
                if (c1722dA.a() != 0) {
                    bV.d(c1722dA.b(), this);
                    this.f6483d = null;
                    this.f6486g.setEnabled(false);
                    return;
                }
            } catch (f.h e2) {
                bV.d(this.f6484e.a(C0942c.f6471d), this);
                this.f6483d = null;
                this.f6486g.setEnabled(false);
                return;
            }
        }
        this.f6486g.setEnabled(true);
    }

    @Override // java.awt.datatransfer.ClipboardOwner
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public C1719a b() {
        return this.f6483d;
    }
}
