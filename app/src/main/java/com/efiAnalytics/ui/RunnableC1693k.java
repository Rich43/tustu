package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* renamed from: com.efiAnalytics.ui.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/k.class */
class RunnableC1693k implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f11725a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1642e f11726b;

    RunnableC1693k(C1642e c1642e, String str) {
        this.f11726b = c1642e;
        this.f11725a = str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public void run() {
        if (this.f11725a.isEmpty()) {
            Object selectedItem = this.f11726b.getSelectedItem();
            this.f11726b.f11466e = true;
            this.f11726b.c();
            Iterator it = this.f11726b.f11465d.iterator();
            while (it.hasNext()) {
                this.f11726b.a((String) it.next());
            }
            this.f11726b.setSelectedItem(selectedItem);
            this.f11726b.f11466e = false;
            return;
        }
        ArrayList arrayList = new ArrayList(this.f11726b.f11464a.a(this.f11725a));
        Collections.sort(arrayList);
        int i2 = 0;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            String str = (String) arrayList.get(i3);
            if (str.toLowerCase().startsWith(this.f11725a.toLowerCase())) {
                if (i3 > 0) {
                    arrayList.remove(i3);
                    arrayList.add(i2, str);
                }
                i2++;
            }
        }
        this.f11726b.setEditable(false);
        this.f11726b.c();
        this.f11726b.f11466e = true;
        Iterator<E> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.f11726b.a((String) it2.next());
        }
        this.f11726b.f11466e = false;
        this.f11726b.setPopupVisible(true);
        this.f11726b.requestFocus();
    }
}
