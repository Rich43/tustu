package bb;

import aP.iK;
import com.efiAnalytics.apps.ts.dashboard.HtmlDisplay;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fS;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bb.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/k.class */
public class C1046k extends JPanel implements fS {

    /* renamed from: a, reason: collision with root package name */
    HtmlDisplay f7774a;

    /* renamed from: c, reason: collision with root package name */
    boolean f7776c;

    /* renamed from: b, reason: collision with root package name */
    JCheckBox f7775b = new JCheckBox(C1818g.b(XIncludeHandler.HTTP_ACCEPT));

    /* renamed from: d, reason: collision with root package name */
    private ae.q f7777d = null;

    /* renamed from: e, reason: collision with root package name */
    private boolean f7778e = false;

    public C1046k(String str, boolean z2) {
        this.f7776c = false;
        setLayout(new BorderLayout());
        Component jLabel = new JLabel(C1818g.b(str), 0);
        jLabel.setFont(new Font("SansSerif", 1, eJ.a() * 2));
        add("North", jLabel);
        this.f7774a = new HtmlDisplay();
        add(BorderLayout.CENTER, this.f7774a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(this.f7775b);
        if (!z2) {
            this.f7776c = false;
        } else {
            add("South", jPanel);
            this.f7776c = true;
        }
    }

    public void a(File file) throws V.a {
        if (!file.exists()) {
            throw new V.a("File not found: " + file.getAbsolutePath());
        }
        a(b(file).getAbsolutePath());
    }

    public void a(String str) throws V.a {
        this.f7774a.setDocumentUrl(str);
    }

    public boolean a() {
        return this.f7776c && !this.f7775b.isSelected();
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }

    private File b(File file) {
        File fileCreateTempFile;
        if (!this.f7778e) {
            return file;
        }
        try {
            if (file.getParentFile().canWrite()) {
                fileCreateTempFile = new File(file.getParentFile(), "prepped_" + file.getName());
                fileCreateTempFile.deleteOnExit();
            } else {
                try {
                    fileCreateTempFile = File.createTempFile("prepped_" + file.getName(), file.getName().contains(".") ? file.getName().substring(file.getName().lastIndexOf(".") + 1) : "");
                } catch (IOException e2) {
                    Logger.getLogger(C1046k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    return file;
                }
            }
            fileCreateTempFile.deleteOnExit();
            return iK.a().a(file, fileCreateTempFile);
        } catch (V.a e3) {
            Logger.getLogger(C1046k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return file;
        }
    }

    public void a(ae.q qVar) {
        this.f7777d = qVar;
    }

    public void a(boolean z2) {
        this.f7778e = z2;
    }
}
