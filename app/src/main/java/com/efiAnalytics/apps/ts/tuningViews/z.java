package com.efiAnalytics.apps.ts.tuningViews;

import bH.Z;
import com.efiAnalytics.ui.C1609ct;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1798a;
import r.C1807j;
import s.C1818g;
import v.C1887g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/z.class */
public class z implements aE.e, InterfaceC1428a {

    /* renamed from: a, reason: collision with root package name */
    C1441n f9882a;

    /* renamed from: b, reason: collision with root package name */
    aE.a f9883b = null;

    public z(C1441n c1441n) {
        this.f9882a = c1441n;
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        this.f9883b = aVar;
        String[] strArrD = G.T.a().d();
        G.c();
        ArrayList arrayList = new ArrayList();
        for (String str : strArrD) {
            G.R rC = G.T.a().c(str);
            if (rC != null) {
                arrayList.add(rC);
            }
        }
        try {
            Z z2 = new Z();
            z2.a();
            this.f9882a.a(G.b(arrayList));
            bH.C.c("Tuning View Files loaded in " + z2.d() + "ms.");
        } catch (V.a e2) {
            bV.d("Unable to load Tuning View Tabs for this Project.\nError:\n" + e2.getMessage(), this.f9882a);
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // aE.e
    public void e_() {
    }

    @Override // aE.e
    public void a(aE.a aVar) {
        int i2 = 0;
        Iterator itE = this.f9882a.e();
        while (itE.hasNext()) {
            J j2 = (J) itE.next();
            if (j2 == null) {
                bH.C.b("null TuningViewPanel!!");
            }
            if (j2 != null && !j2.O()) {
                j2.b(true);
                j2.repaint();
                F fG = j2.g();
                if (bV.a(C1818g.b("A Tuning View Tab has been changed.") + "\n" + C1818g.b("Do you want to save the changes?") + "\n" + fG.b(), (Component) j2, true)) {
                    try {
                        new C1887g().a(fG, new File(C1807j.a(aVar), G.a(i2)));
                    } catch (V.a e2) {
                        bV.d(e2.getMessage(), j2);
                    }
                }
            }
            i2++;
        }
        this.f9882a.c();
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.InterfaceC1428a
    public void a() {
        c();
    }

    private void c() {
        C1429b c1429b = new C1429b();
        String[] strArrD = G.T.a().d();
        String[] strArr = new String[strArrD.length];
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = G.T.a().c(strArrD[i2]);
            arrayList.add(rC);
            strArr[i2] = rC.i();
        }
        C c2 = new C(this, c1429b, strArr);
        new A(this, arrayList, c1429b).start();
        Window windowB = bV.b(this.f9882a);
        Window window = windowB;
        if (!(window instanceof Dialog) && !(window instanceof Frame)) {
            window = null;
        }
        C1609ct c1609ct = new C1609ct(window, c1429b, "Select Tuning View", c2, 7);
        if (c2 != null) {
            c1609ct.a(c2);
        }
        c1609ct.setSize(eJ.a(640), eJ.a(NNTPReply.AUTHENTICATION_REQUIRED));
        bV.a((Component) windowB, (Component) c1609ct);
        c1609ct.setVisible(true);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.InterfaceC1428a
    public boolean a(String str, C1441n c1441n) {
        int iF = this.f9882a.f(str);
        if (iF < 0) {
            return true;
        }
        new File(C1807j.a(this.f9883b), G.a(iF)).delete();
        for (int i2 = iF + 1; i2 < this.f9882a.getTabCount() - 1; i2++) {
            File file = new File(C1807j.a(this.f9883b), G.a(i2));
            File file2 = new File(C1807j.a(this.f9883b), G.a(i2 - 1));
            file.renameTo(file2);
            this.f9882a.b(i2).c(file2.getAbsolutePath());
        }
        return true;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.InterfaceC1428a
    public void a(J j2) {
        j2.a(new B(this, j2, j2.getName()));
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.InterfaceC1428a
    public void a(J j2, int i2) throws HeadlessException {
        int iF = this.f9882a.f(j2.getName());
        boolean zIsEnabledAt = this.f9882a.isEnabledAt(iF);
        J jB = this.f9882a.b(i2);
        this.f9882a.a(i2);
        this.f9882a.a(iF);
        File file = new File(j2.w());
        File fileB = G.b(i2);
        File fileB2 = G.b(iF);
        do {
            fileB2 = new File(fileB2.getParentFile(), bH.W.b(fileB2.getName(), "." + C1798a.cp, "a." + C1798a.cp));
        } while (fileB2.exists());
        bH.C.c("renaming: " + fileB.getName() + " to " + fileB2.getName());
        if (fileB.exists() && !fileB.renameTo(fileB2)) {
            bH.C.a("Failed to rename TuningViewFile in movingTuningView.");
            bV.d("Unable to rename tmp TuningView File.", this.f9882a);
            return;
        }
        jB.c(fileB2.getAbsolutePath());
        File fileB3 = G.b(i2);
        bH.C.c("renaming: " + file.getName() + " to " + fileB3.getName());
        if (!file.renameTo(fileB3)) {
            bH.C.a("Failed to rename TuningViewFile in movingTuningView.");
            bV.d("Unable to rename TuningView File.\nFrom:" + file.getAbsolutePath() + "\n  To:" + fileB3.getAbsolutePath(), this.f9882a);
            return;
        }
        j2.c(fileB3.getAbsolutePath());
        File fileB4 = G.b(iF);
        if (!fileB4.equals(fileB2)) {
            bH.C.c("renaming: " + fileB2.getName() + " to " + fileB4.getName());
            if (fileB2.renameTo(fileB4)) {
                jB.c(fileB4.getAbsolutePath());
            } else {
                bH.C.a("Failed to rename TuningViewFile in movingTuningView. " + fileB2.getName() + " --> " + fileB4.getName());
                bV.d("Unable to rename TuningView File.\nFrom:" + fileB2.getAbsolutePath() + "\n  To:" + fileB4.getAbsolutePath(), this.f9882a);
            }
        }
        bH.C.c("Finished moving TV files.");
        this.f9882a.remove(iF);
        this.f9882a.a(j2, j2.getName(), i2);
        if (zIsEnabledAt) {
            this.f9882a.setSelectedIndex(i2);
        }
    }
}
