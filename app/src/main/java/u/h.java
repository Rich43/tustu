package u;

import G.R;
import bH.C;
import bt.bO;
import c.C1382a;
import com.efiAnalytics.ui.C1641dz;
import com.efiAnalytics.ui.cI;
import com.efiAnalytics.ui.eJ;
import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:u/h.class */
public class h extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    File f13990a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f13991b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f13992c;

    /* renamed from: d, reason: collision with root package name */
    C1641dz f13993d;

    /* renamed from: e, reason: collision with root package name */
    d f13994e;

    public h(Window window, R r2, R r3, ArrayList arrayList, ArrayList arrayList2, File file) throws HeadlessException {
        String strB;
        super(window, C1818g.b("Difference Report"), Dialog.DEFAULT_MODALITY_TYPE);
        this.f13990a = null;
        this.f13991b = new ArrayList();
        this.f13992c = null;
        this.f13993d = null;
        this.f13990a = file;
        this.f13992c = arrayList2;
        System.currentTimeMillis();
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JTextPane jTextPane = new JTextPane();
        jTextPane.setContentType(Clipboard.HTML_TYPE);
        if (file == null || !file.exists()) {
            strB = C1818g.b(C1382a.a(r2.i(), C1798a.f13272f));
            setDefaultCloseOperation(0);
        } else {
            strB = file.getName();
        }
        jTextPane.setText(("<html><center><b>" + C1818g.b("Difference Report") + ":</b><br>" + C1818g.b("There are differences between the settings currently in " + C1798a.f13268b + " and the settings") + "<br>" + C1818g.b("that were found in the") + " " + strB + ".") + C1818g.b("You must select which settings you wish to use.") + "</center></html>");
        jTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        jPanel.add(BorderLayout.CENTER, jTextPane);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 2));
        jPanel2.add(new JLabel(C1818g.b("Current " + C1798a.f13268b + " Settings"), 0));
        jPanel2.add(new JLabel(C1818g.b("Settings in") + " " + strB, 0));
        jPanel.add("South", jPanel2);
        add("North", jPanel);
        C.d("Difference Report disabling shared table Models");
        bO.a().a(false);
        this.f13994e = new d(r2, r3, arrayList);
        this.f13993d = new C1641dz(this.f13994e, C1818g.d());
        add(BorderLayout.CENTER, this.f13993d);
        k kVar = new k(this, getContentPane());
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0));
        String str = "<html><body>" + C1818g.b("Button Actions") + ":<br><br>";
        i iVar = new i(this);
        int i2 = 0;
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            g gVar = (g) it.next();
            JPanel jPanel4 = new JPanel();
            jPanel4.setLayout(new FlowLayout(1));
            C1879a c1879a = new C1879a(gVar, iVar);
            jPanel4.add(c1879a);
            jPanel3.add(jPanel4);
            kVar.a(c1879a);
            int i3 = i2;
            i2++;
            if (i3 < 1) {
                c1879a.requestFocus();
            }
            str = str + "<b>" + C1818g.b(gVar.a()) + " - </b>" + C1818g.b(gVar.b()) + "<br><br>";
        }
        String str2 = str + "</body></html>";
        JPanel jPanel5 = new JPanel();
        jPanel5.setToolTipText(str2);
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(BorderLayout.CENTER, jPanel3);
        add("South", jPanel5);
        cI cIVar = new cI(str2);
        kVar.a(cIVar);
        jPanel5.add("East", cIVar);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= eJ.a(100);
        setSize(getWidth() + this.f13993d.a().getVerticalScrollBar().getPreferredSize().width, getHeight());
        if (getWidth() < screenSize.width) {
            screenSize.width = getWidth();
        }
        if (getHeight() > screenSize.height) {
            setSize(screenSize);
        }
        j jVar = new j(this);
        jVar.addMouseListener(kVar);
        jVar.addMouseMotionListener(kVar);
        kVar.a(this.f13993d.a().getHorizontalScrollBar());
        kVar.a(this.f13993d.a().getVerticalScrollBar());
        for (int i4 = 0; i4 < this.f13993d.a().getHorizontalScrollBar().getComponentCount(); i4++) {
            kVar.a(this.f13993d.a().getHorizontalScrollBar().getComponent(i4));
        }
        for (int i5 = 0; i5 < this.f13993d.a().getVerticalScrollBar().getComponentCount(); i5++) {
            kVar.a(this.f13993d.a().getVerticalScrollBar().getComponent(i5));
        }
        kVar.a(this.f13993d.b());
        kVar.a(this.f13993d.c());
        getRootPane().setGlassPane(jVar);
        jVar.a();
    }

    public boolean a() {
        return this.f13994e.f13984b.isEmpty();
    }

    @Override // java.awt.Window
    public void dispose() {
        this.f13994e.close();
        C.d("Difference Report enabling shared table Models");
        bO.a().a(true);
        super.dispose();
    }

    public boolean b() {
        Iterator it = this.f13992c.iterator();
        while (it.hasNext()) {
            if (((g) it.next()).c()) {
                return true;
            }
        }
        return false;
    }

    public void a(f fVar) {
        this.f13991b.add(fVar);
    }

    public void b(f fVar) {
        this.f13991b.remove(fVar);
    }
}
