package bl;

import bH.W;
import com.efiAnalytics.plugin.ApplicationPlugin;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.aN;
import com.efiAnalytics.ui.bV;
import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import r.C1798a;
import s.C1818g;

/* renamed from: bl.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/h.class */
public class C1186h extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    ApplicationPlugin f8251a;

    public C1186h(ApplicationPlugin applicationPlugin) {
        this.f8251a = null;
        this.f8251a = applicationPlugin;
        setLayout(new BorderLayout());
        try {
            String str = C1798a.f13268b + " Plugin: " + applicationPlugin.getDisplayName();
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            JMenuBar jMenuBar = new JMenuBar();
            JMenu jMenu = new JMenu(C1818g.b("Help"));
            jMenuBar.add(jMenu);
            if (applicationPlugin.getHelpUrl() != null && applicationPlugin.getHelpUrl().length() > 0) {
                JMenuItem jMenuItem = new JMenuItem(C1818g.b("Online Help for " + applicationPlugin.getDisplayName()));
                jMenuItem.addActionListener(new C1187i(this));
                jMenu.add(jMenuItem);
            }
            JMenuItem jMenuItem2 = new JMenuItem(C1818g.b("About Plugin"));
            jMenuItem2.addActionListener(new C1188j(this));
            jMenu.add(jMenuItem2);
            jPanel.add("North", jMenuBar);
            add("North", jPanel);
            add(BorderLayout.CENTER, applicationPlugin.getPluginPanel());
        } catch (Exception e2) {
            bV.d("Error Showing Plugin:\n" + e2.getMessage(), this);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8251a.close();
    }

    public void a() {
        aN.a(this.f8251a.getHelpUrl());
    }

    public void b() {
        String str = "<html><body><center><h2>" + this.f8251a.getDisplayName() + " version " + this.f8251a.getVersion() + "</h2><h3>implemented as a " + C1798a.f13268b + " Plugin</h3></center>Written by " + this.f8251a.getAuthor() + "<br><br>" + W.a(this.f8251a.getDescription(), 60, "<br>") + "<br>";
        if (this.f8251a.getHelpUrl() != null) {
            str = str + "<br>For More Information on this plugin see: <a href=\"" + this.f8251a.getHelpUrl() + "\">Online Help</a>";
        }
        JEditorPane jEditorPane = new JEditorPane(Clipboard.HTML_TYPE, str + "</body></html>");
        jEditorPane.addHyperlinkListener(new C1189k(this));
        jEditorPane.setEditable(false);
        jEditorPane.setOpaque(false);
        JOptionPane.showMessageDialog(this, jEditorPane, C1798a.f13268b + " Plugin Info", 1);
    }
}
