package bf;

import G.C0043ac;
import G.C0048ah;
import G.Q;
import G.R;
import G.aH;
import W.InterfaceC0192r;
import be.C1102r;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import s.C1818g;
import sun.security.tools.policytool.ToolWindow;

/* renamed from: bf.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/a.class */
public class C1108a extends JPanel implements TreeSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    public static String f8032a = C1102r.f7991a;

    /* renamed from: b, reason: collision with root package name */
    public static String f8033b = C1102r.f7992b;

    /* renamed from: c, reason: collision with root package name */
    public static String f8034c = C1102r.f7993c;

    /* renamed from: j, reason: collision with root package name */
    JTree f8041j;

    /* renamed from: d, reason: collision with root package name */
    C1117j f8035d = new C1117j(FXMLLoader.CONTROLLER_SUFFIX);

    /* renamed from: e, reason: collision with root package name */
    C1117j f8036e = null;

    /* renamed from: f, reason: collision with root package name */
    Q f8037f = null;

    /* renamed from: g, reason: collision with root package name */
    ArrayList f8038g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    ArrayList f8039h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    HashMap f8040i = new HashMap();

    /* renamed from: k, reason: collision with root package name */
    R f8042k = null;

    /* renamed from: m, reason: collision with root package name */
    private InterfaceC0192r f8043m = new C1109b(this);

    /* renamed from: l, reason: collision with root package name */
    List f8044l = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    private C1120m f8045n = new C1120m(this);

    public C1108a() {
        setLayout(new BorderLayout());
        this.f8041j = new JTree(this.f8035d);
        this.f8041j.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JScrollPane jScrollPane = new JScrollPane(this.f8041j);
        jScrollPane.setPreferredSize(new Dimension(eJ.a(240), eJ.a(500)));
        this.f8041j.addTreeSelectionListener(this);
        add(jScrollPane, BorderLayout.CENTER);
        this.f8041j.addMouseListener(new C1110c(this));
        Font font = (Font) UIManager.get("Tree.font");
        if (font != null) {
            this.f8041j.setRowHeight(font.getSize() + eJ.a(4));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(MouseEvent mouseEvent) {
        if (this.f8036e != null) {
            String string = this.f8036e.a() == null ? this.f8036e.getUserObject().toString() : ((C1117j) this.f8036e.getParent()).getUserObject().toString();
            JPopupMenu jPopupMenu = new JPopupMenu();
            jPopupMenu.add(C1818g.b("Add") + " " + string).addActionListener(new C1111d(this, string));
            Q qA = this.f8036e.a();
            if (qA != null) {
                jPopupMenu.add(C1818g.b(ToolWindow.EDIT_KEYSTORE) + " " + string).addActionListener(new C1112e(this, qA));
                jPopupMenu.add(C1818g.b("Delete") + " " + string).addActionListener(new C1113f(this, qA));
            }
            jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
        }
    }

    public void a(Q q2) {
        C1117j c1117jB = b(q2);
        if (c1117jB == null) {
            bV.d("Unknown Component Type!", this);
            return;
        }
        C1117j c1117j = new C1117j(q2);
        c1117jB.add(c1117j);
        ((DefaultTreeModel) this.f8041j.getModel()).reload(c1117jB);
        this.f8041j.setSelectionPath(new TreePath((Object[]) c1117j.getPath()));
        this.f8041j.setSelectionPath(new TreePath((Object[]) c1117j.getPath()));
    }

    private C1117j b(Q q2) {
        if (q2 instanceof aH) {
            return (C1117j) this.f8040i.get(f8032a);
        }
        if (q2 instanceof C0048ah) {
            return (C1117j) this.f8040i.get(f8033b);
        }
        if (q2 instanceof C0043ac) {
            return (C1117j) this.f8040i.get(f8034c);
        }
        return null;
    }

    public void a(R r2) {
        this.f8042k = r2;
        this.f8035d.removeAllChildren();
        if (a(f8032a)) {
            C1117j c1117j = new C1117j(f8032a);
            this.f8040i.put(f8032a, c1117j);
            this.f8035d.add(c1117j);
            ArrayList<aH> arrayList = new ArrayList();
            Iterator itQ = r2.q();
            while (itQ.hasNext()) {
                arrayList.add(itQ.next());
            }
            Collections.sort(arrayList, new C1114g(this));
            for (aH aHVar : arrayList) {
                if (this.f8043m == null || this.f8043m.a(aHVar)) {
                    c1117j.add(new C1117j(aHVar));
                }
            }
        }
        if (a(f8033b)) {
            C1117j c1117j2 = new C1117j(f8033b);
            this.f8035d.add(c1117j2);
            this.f8040i.put(f8033b, c1117j2);
            ArrayList<C0048ah> arrayList2 = new ArrayList();
            Iterator itB = r2.B();
            while (itB.hasNext()) {
                arrayList2.add(itB.next());
            }
            Collections.sort(arrayList2, new C1115h(this));
            for (C0048ah c0048ah : arrayList2) {
                if (this.f8043m == null || this.f8043m.a(c0048ah)) {
                    try {
                        c1117j2.add(new C1117j(c0048ah));
                    } catch (Exception e2) {
                        Logger.getLogger(C1108a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            }
        }
        if (a(f8034c)) {
            C1117j c1117j3 = new C1117j(f8034c);
            this.f8035d.add(c1117j3);
            this.f8040i.put(f8034c, c1117j3);
            ArrayList<C0043ac> arrayList3 = new ArrayList();
            arrayList3.addAll(r2.g());
            Collections.sort(arrayList3, new C1116i(this));
            for (C0043ac c0043ac : arrayList3) {
                if (this.f8043m == null || this.f8043m.a(c0043ac)) {
                    c1117j3.add(new C1117j(c0043ac));
                }
            }
        }
        ((DefaultTreeModel) this.f8041j.getModel()).reload(this.f8035d);
        this.f8041j.expandPath(new TreePath((Object[]) this.f8035d.getPath()));
    }

    public boolean a(String str) {
        return this.f8044l.contains(str);
    }

    public void b(String str) {
        this.f8044l.add(str);
    }

    public void a(InterfaceC1118k interfaceC1118k) {
        this.f8038g.add(interfaceC1118k);
    }

    private void a(C1117j c1117j) {
        Iterator it = this.f8038g.iterator();
        while (it.hasNext()) {
            ((InterfaceC1118k) it.next()).a(this.f8036e);
        }
    }

    private void c(Q q2) {
        Iterator it = this.f8038g.iterator();
        while (it.hasNext()) {
            ((InterfaceC1118k) it.next()).a(q2);
        }
    }

    public void a(InterfaceC1119l interfaceC1119l) {
        this.f8039h.add(interfaceC1119l);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Q q2) {
        Iterator it = this.f8039h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1119l) it.next()).a(q2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Q q2) {
        Iterator it = this.f8039h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1119l) it.next()).b(q2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f8039h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1119l) it.next()).a(str);
        }
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        treeSelectionEvent.getNewLeadSelectionPath();
        treeSelectionEvent.getPath();
        treeSelectionEvent.getOldLeadSelectionPath();
        C1117j c1117j = (C1117j) this.f8041j.getLastSelectedPathComponent();
        if (c1117j == null) {
            c1117j = this.f8036e;
        }
        if (c1117j.getParent() != null) {
            this.f8036e = c1117j;
        } else {
            this.f8036e = null;
        }
        if (c1117j.a() != null) {
            this.f8037f = c1117j.a();
        } else {
            this.f8037f = null;
        }
        c(this.f8037f);
        a(c1117j);
    }

    public C1120m a() {
        return this.f8045n;
    }
}
