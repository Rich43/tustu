package com.efiAnalytics.tunerStudio.search;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1421t;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.TableHeaderUI;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1807j;
import r.C1811n;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/ContinuousIpSearchPanel.class */
public class ContinuousIpSearchPanel extends AbstractC1420s implements InterfaceC1421t, InterfaceC1565bc, Serializable {

    /* renamed from: c, reason: collision with root package name */
    q f10163c;

    /* renamed from: g, reason: collision with root package name */
    C1482e f10166g;

    /* renamed from: a, reason: collision with root package name */
    JProgressBar f10161a = new JProgressBar();

    /* renamed from: b, reason: collision with root package name */
    JPanel f10162b = new JPanel();

    /* renamed from: d, reason: collision with root package name */
    C1480c f10164d = new C1480c(this);

    /* renamed from: f, reason: collision with root package name */
    int f10165f = q.f10208a;

    /* renamed from: h, reason: collision with root package name */
    C1481d f10167h = null;

    /* renamed from: i, reason: collision with root package name */
    JLabel f10168i = new JLabel();

    /* renamed from: j, reason: collision with root package name */
    Map f10169j = new HashMap();

    /* renamed from: k, reason: collision with root package name */
    List f10170k = new ArrayList();

    public ContinuousIpSearchPanel() {
        this.f10166g = null;
        setOpaque(true);
        setBackground(UIManager.getColor("Label.background"));
        setLayout(new BorderLayout(eJ.a(15), eJ.a(15)));
        setBorder(BorderFactory.createEmptyBorder(eJ.a(40), eJ.a(40), eJ.a(40), eJ.a(40)));
        ImageIcon imageIcon = new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().createImage(C1798a.a().c(C1798a.f13368aX)), this, 360));
        this.f10168i.setMinimumSize(new Dimension(100, eJ.a(360)));
        this.f10168i.setVerticalTextPosition(3);
        this.f10168i.setHorizontalTextPosition(0);
        this.f10168i.setHorizontalAlignment(0);
        int iA = eJ.a(5);
        this.f10168i.setBorder(BorderFactory.createEmptyBorder(iA, iA, iA, iA));
        this.f10168i.setIcon(imageIcon);
        add("North", this.f10168i);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f10163c = new q();
        this.f10163c.getTableHeader().setUI((TableHeaderUI) null);
        JScrollPane jScrollPane = new JScrollPane(this.f10163c);
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray, eJ.a(2), true));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        this.f10161a.setIndeterminate(true);
        jPanel.add("South", this.f10161a);
        add(BorderLayout.CENTER, jPanel);
        this.f10166g = new C1482e(this);
        this.f10166g.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadRecentProjects() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList arrayListA = new C1811n().a();
        ArrayList arrayList = new ArrayList();
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            File file = new File(str);
            B.i iVarD = C1807j.d(file);
            if (file.exists() && iVarD != null && !iVarD.e().isEmpty()) {
                D.c cVarD = aA.h.a().d(iVarD.i(), iVarD.e());
                f fVar = new f();
                iVarD.b("Offline");
                fVar.a(iVarD);
                fVar.a(cVarD.b());
                fVar.a(new j(file));
                this.f10170k.add(fVar);
                SwingUtilities.invokeLater(new RunnableC1478a(this, fVar));
                arrayList.add(str);
            }
        }
        for (File file2 : new File(C1807j.u()).listFiles()) {
            B.i iVarD2 = C1807j.d(file2);
            if (!arrayList.contains(file2.getAbsolutePath()) && file2.exists() && iVarD2 != null && !iVarD2.e().isEmpty()) {
                D.c cVarD2 = aA.h.a().d(iVarD2.i(), iVarD2.e());
                f fVar2 = new f();
                iVarD2.b("Offline");
                fVar2.a(iVarD2);
                fVar2.a(cVarD2.b());
                fVar2.a(new j(file2));
                this.f10170k.add(fVar2);
                SwingUtilities.invokeLater(new RunnableC1479b(this, fVar2));
                arrayList.add(file2.getAbsolutePath());
            }
        }
        bH.C.c("Time to load Projects list: " + (System.currentTimeMillis() - jCurrentTimeMillis));
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, getWidth(), getHeight()));
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        B.g.a().b(this.f10164d);
        this.f10166g.a();
        if (this.f10167h != null) {
            this.f10167h.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getKey(B.i iVar) {
        return (iVar.e() == null || iVar.e().isEmpty()) ? iVar.c() + CallSiteDescriptor.TOKEN_DELIMITER + iVar.g() : iVar.e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExpired() {
        for (String str : (String[]) this.f10169j.keySet().toArray(new String[this.f10169j.keySet().size()])) {
            f fVar = (f) this.f10169j.get(str);
            if (fVar != null && fVar.f() != null && fVar.e() < System.currentTimeMillis() - this.f10165f) {
                fVar.f().b("Offline");
                this.f10163c.b(fVar);
                this.f10163c.a();
            }
        }
        for (f fVar2 : this.f10163c.f10209b) {
            if (fVar2.h() != null && fVar2.h().b() != null && !fVar2.h().b().exists()) {
                this.f10169j.remove(getKey(fVar2.f()));
                this.f10163c.c(fVar2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceStatus() {
        for (f fVar : this.f10169j.values()) {
            if (fVar.g() == null || fVar.g().c().equals("U")) {
                D.c cVarC = aA.h.a().c(fVar.b(), fVar.a());
                if (cVarC.a() == 0 || cVarC.a() == 32768) {
                    if (cVarC.b() != null) {
                        fVar.a(cVarC.b());
                    }
                    this.f10163c.b(fVar);
                    this.f10163c.a();
                    this.f10163c.b();
                }
            }
        }
        for (f fVar2 : this.f10170k) {
            D.c cVarC2 = aA.h.a().c(fVar2.b(), fVar2.a());
            if (cVarC2.a() == 0 || cVarC2.a() == 32768) {
                fVar2.a(cVarC2.b());
                this.f10163c.b(fVar2);
                this.f10163c.a();
                this.f10163c.b();
            }
        }
        this.f10170k.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void devicesUpdated() {
        if (this.f10167h == null || !this.f10167h.isAlive()) {
            this.f10167h = new C1481d(this);
            this.f10167h.start();
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void unsubscribeToOutput() {
    }
}
