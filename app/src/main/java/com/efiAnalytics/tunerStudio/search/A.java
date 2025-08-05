package com.efiAnalytics.tunerStudio.search;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/A.class */
public class A extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    private String f10152a = null;

    public boolean a(String str) {
        return !isEmpty() && ((B) get(0)).a().toLowerCase().startsWith(str.toLowerCase());
    }

    public String a() {
        return this.f10152a;
    }

    public void b(String str) {
        this.f10152a = str;
    }

    public boolean c(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((B) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
