package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.Window;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/R.class */
public class R implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    J f9741a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ J f9742b;

    R(J j2, J j3) {
        this.f9742b = j2;
        this.f9741a = null;
        this.f9741a = j3;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0180  */
    @Override // java.awt.KeyEventDispatcher
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchKeyEvent(java.awt.event.KeyEvent r4) {
        /*
            Method dump skipped, instructions count: 395
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.apps.ts.tuningViews.R.dispatchKeyEvent(java.awt.event.KeyEvent):boolean");
    }

    private boolean a(Object obj) {
        if (!(obj instanceof Component)) {
            return false;
        }
        Component parent = (Component) obj;
        if (this.f9741a.equals(obj)) {
            return true;
        }
        while (parent != null) {
            if (parent.equals(this.f9741a)) {
                return true;
            }
            if (this.f9742b.f9688D != null && parent.equals(this.f9742b.f9688D) && this.f9742b.f9697K.contains(obj)) {
                return true;
            }
            parent = parent.getParent();
            if (parent instanceof Window) {
                return false;
            }
        }
        for (Component component : this.f9742b.getComponents()) {
            if (obj.equals(component)) {
                return true;
            }
        }
        return false;
    }
}
