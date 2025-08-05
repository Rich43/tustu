package com.efiAnalytics.ui;

import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import sun.security.tools.policytool.ToolWindow;

/* renamed from: com.efiAnalytics.ui.bi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bi.class */
public class C1571bi extends JWindow {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f10996a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f10997b;

    /* renamed from: c, reason: collision with root package name */
    C1576bn f10998c;

    /* renamed from: d, reason: collision with root package name */
    Image f10999d;

    /* renamed from: e, reason: collision with root package name */
    Insets f11000e;

    /* renamed from: f, reason: collision with root package name */
    Image f11001f;

    /* renamed from: g, reason: collision with root package name */
    boolean f11002g;

    /* renamed from: h, reason: collision with root package name */
    boolean f11003h;

    public C1571bi(Window window, String str) {
        this(window, str, false);
        a();
    }

    public C1571bi(Window window, String str, boolean z2) {
        super(window);
        this.f10996a = new JTextPane();
        this.f10997b = new ArrayList();
        this.f10998c = null;
        this.f10999d = null;
        this.f11000e = eJ.a(new Insets(44, 12, 12, 12));
        this.f11001f = null;
        this.f11002g = false;
        this.f11003h = false;
        a();
        setBackground(new Color(0, 0, 0, 0));
        this.f11003h = z2;
        this.f11002g = C1685fp.c();
        if (!this.f11002g) {
            bH.C.b("Translucent Windows not supported on this PC.");
        }
        d();
        this.f10996a.setBackground(new Color(255, 255, 153, 255));
        this.f10996a.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        if (z2 && !str.startsWith("<html")) {
            str = "<html>" + str;
        }
        if (z2) {
            this.f10996a.setContentType(Clipboard.HTML_TYPE);
            str = a(bH.W.b(str, "\n", "<br>"));
        }
        this.f10996a.addHyperlinkListener(new C1572bj(this));
        this.f10996a.setText(str);
        this.f10996a.setForeground(Color.BLACK);
        this.f10996a.setEditable(!z2);
        this.f10996a.setCaretPosition(0);
        this.f10998c = new C1576bn(this, this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f10998c);
        C1573bk c1573bk = new C1573bk(this);
        addFocusListener(c1573bk);
        this.f10996a.addFocusListener(c1573bk);
        if (z2) {
            this.f10999d = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/commentBoxReadOnly.png"));
        } else {
            this.f10999d = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/commentBox.png"));
        }
        this.f10999d = eJ.a(this.f10999d, this);
        addMouseListener(new C1577bo(this));
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(new Color(245, 245, 143));
        if (this.f11003h) {
            graphics.fill3DRect(eJ.a(278), eJ.a(13), eJ.a(35), eJ.a(48), true);
        } else {
            graphics.fill3DRect(eJ.a(230), eJ.a(13), eJ.a(83), eJ.a(50), true);
        }
        graphics.drawImage(this.f10999d, 0, 0, null);
        super.paintComponents(graphics);
    }

    public void a() {
        if (!this.f11002g) {
        }
    }

    @Override // java.awt.Container
    public Insets getInsets() {
        return this.f11000e;
    }

    private void d() {
        setLayout(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(this.f10996a);
        jScrollPane.setBorder(null);
        add(BorderLayout.CENTER, jScrollPane);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (!this.f11003h) {
            JButton jButton = new JButton("Delete");
            jButton.addActionListener(new C1574bl(this));
            jPanel.add(jButton);
        }
        JButton jButton2 = new JButton(ToolWindow.SAVE_POLICY_FILE);
        jButton2.addActionListener(new C1575bm(this));
        jPanel.add(jButton2);
    }

    protected void b() {
        e();
        dispose();
    }

    protected void c() {
        f();
        dispose();
    }

    @Override // java.awt.Window
    public void dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f10998c);
        super.dispose();
    }

    private void e() {
        Iterator it = this.f10997b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1678fi) it.next()).a(this.f10996a.getText());
        }
    }

    private void f() {
        Iterator it = this.f10997b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1678fi) it.next()).a();
        }
    }

    public void a(InterfaceC1678fi interfaceC1678fi) {
        this.f10997b.add(interfaceC1678fi);
    }

    private String a(String str) {
        String[] strArrSplit = str.split("<br>");
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArrSplit.length; i2++) {
            String str2 = strArrSplit[i2];
            if (str2.contains("<a h") || !(str2.contains("http://") || str2.contains("https://"))) {
                sb.append(str2);
            } else {
                int iIndexOf = str2.contains("http:") ? str2.indexOf("http://") : str2.indexOf("https://");
                int iIndexOf2 = str2.indexOf(" ", iIndexOf);
                if (iIndexOf2 == -1) {
                    iIndexOf2 = str2.length();
                }
                String strSubstring = str2.substring(0, iIndexOf);
                String strSubstring2 = str2.substring(iIndexOf, iIndexOf2);
                String strSubstring3 = iIndexOf2 < str2.length() ? str2.substring(iIndexOf2) : "";
                sb.append(strSubstring);
                sb.append("<a href=\"").append(strSubstring2).append("\">").append(strSubstring2).append("</a>");
                sb.append(strSubstring3);
            }
            if (i2 < strArrSplit.length - 1) {
                sb.append("<br>");
            }
        }
        return sb.toString();
    }
}
