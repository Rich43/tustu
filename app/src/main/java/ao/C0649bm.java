package ao;

import g.C1733k;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

/* renamed from: ao.bm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bm.class */
public class C0649bm extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f5419a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JDialog f5420b = null;

    /* renamed from: c, reason: collision with root package name */
    String f5421c = "body { font-size: 100% }";

    public C0649bm() {
        setLayout(new BorderLayout());
        this.f5419a.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f5419a.setEditable(false);
        this.f5419a.setContentType("text/html; charset=UTF-8");
        this.f5419a.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        this.f5419a.setText("<html><body></body></html>");
        ((HTMLDocument) this.f5419a.getDocument()).getStyleSheet().addRule(this.f5421c);
        this.f5419a.addHyperlinkListener(new C0650bn(this));
        add(BorderLayout.CENTER, new JScrollPane(this.f5419a));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new C0651bo(this));
        jPanel.add(jButton);
        add("South", jPanel);
    }

    public void a(String str) {
        try {
            this.f5419a.setPage(str);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void a() {
        if (this.f5420b != null) {
            this.f5420b.setVisible(false);
        }
    }

    public void a(Component component, String str) throws HeadlessException {
        if (this.f5420b == null) {
            Frame frameA = C1733k.a(component);
            this.f5420b = new JDialog(frameA, str);
            this.f5420b.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            this.f5420b.add(this);
            this.f5420b.setSize(960, 600);
            C1733k.a(frameA, this.f5420b);
        }
        this.f5420b.setVisible(true);
    }
}
