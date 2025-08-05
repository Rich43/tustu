package ai;

import G.C0050aj;
import bH.C;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dF;
import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

/* renamed from: ai.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/f.class */
public class C0516f extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f4515a;

    /* renamed from: b, reason: collision with root package name */
    JTextPane f4516b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f4517c;

    /* renamed from: d, reason: collision with root package name */
    String f4518d;

    /* renamed from: e, reason: collision with root package name */
    private InterfaceC1662et f4519e;

    /* renamed from: f, reason: collision with root package name */
    private InterfaceC1565bc f4520f;

    public C0516f() {
        this.f4515a = new JTextPane();
        this.f4516b = null;
        this.f4517c = null;
        this.f4518d = "body { font-size: 100% }";
        this.f4519e = null;
        this.f4520f = null;
        setLayout(new BorderLayout());
        this.f4515a.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f4515a.setEditable(false);
        this.f4515a.setContentType("text/html; charset=UTF-8");
        this.f4515a.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        this.f4515a.setText("<html><body></body></html>");
        ((HTMLDocument) this.f4515a.getDocument()).getStyleSheet().addRule(this.f4518d);
        this.f4515a.addHyperlinkListener(new C0517g(this));
        add(BorderLayout.CENTER, new JScrollPane(this.f4515a));
        this.f4517c = new JPanel();
        this.f4517c.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new C0518h(this));
        this.f4517c.add(jButton);
        add("South", this.f4517c);
    }

    public C0516f(C0050aj c0050aj) {
        this();
        a(c0050aj);
    }

    public void a(C0050aj c0050aj) {
        if (c0050aj.b() == null || c0050aj.b().equals("")) {
            this.f4515a.setText("<html>" + c0050aj.c() + "</html>");
        } else {
            this.f4515a.setText("<html>" + c0050aj.c() + "<br><br>Web Help: <a href=\"" + c0050aj.b() + "\">" + c0050aj.b() + "</a></html>");
        }
        this.f4515a.setCaretPosition(0);
    }

    public void a(String str) {
        this.f4516b = new JTextPane();
        this.f4516b.setEditable(false);
        this.f4516b.setContentType(Clipboard.HTML_TYPE);
        this.f4516b.addHyperlinkListener(new C0519i(this));
        try {
            this.f4516b.setPage(str);
        } catch (IOException e2) {
            C.d("Can not find index file:" + str + ", not showing left pane.");
        }
        JScrollPane jScrollPane = new JScrollPane(this.f4516b);
        jScrollPane.setMinimumSize(new Dimension(200, 100));
        jScrollPane.setPreferredSize(new Dimension(200, 100));
        add("West", jScrollPane);
    }

    public void a() {
        this.f4515a.setText("");
        if (this.f4520f != null) {
            this.f4520f.close();
        }
        System.gc();
    }

    public void a(boolean z2) {
        this.f4517c.setVisible(z2);
    }

    public void a(C0512b c0512b) {
        try {
            b(c0512b.b());
        } catch (Exception e2) {
            System.out.println("Tried to load web help, but something went wrong.");
            e2.printStackTrace();
            bV.d("Error Opening:\n" + ((Object) c0512b), this);
        }
    }

    public void b(String str) {
        try {
            String strA = str;
            if (!strA.startsWith("http") && !strA.startsWith(DeploymentDescriptorParser.ATTR_FILE)) {
                strA = C0514d.a(strA);
            }
            this.f4515a.setPage(strA);
        } catch (FileNotFoundException e2) {
            bV.d("File Not Found:\n" + e2.getMessage(), this);
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Unable to read file:\n" + str);
        }
    }

    public void a(InterfaceC1565bc interfaceC1565bc) {
        this.f4520f = interfaceC1565bc;
    }

    public void a(Window window, String str) {
        dF dFVar = new dF(window, str);
        dFVar.a(this.f4519e);
        dFVar.add(this);
        a(dFVar);
        dFVar.pack();
        bV.a((Component) window, (Component) dFVar);
        dFVar.setVisible(true);
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f4519e = interfaceC1662et;
    }
}
