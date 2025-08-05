package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/f.class */
public class f {

    /* renamed from: b, reason: collision with root package name */
    private static f f9539b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f9540c = 5;

    /* renamed from: a, reason: collision with root package name */
    HashMap f9541a = new HashMap();

    private f() {
    }

    public static f a() {
        if (f9539b == null) {
            f9539b = new f();
        }
        return f9539b;
    }

    public Image a(String str, int i2, Component component) {
        HashMap map = (HashMap) this.f9541a.get(str);
        if (map == null) {
            map = new HashMap();
            this.f9541a.put(str, map);
        }
        Image image = (Image) map.get(Integer.valueOf(i2));
        if (image == null) {
            image = Toolkit.getDefaultToolkit().getImage(str);
            MediaTracker mediaTracker = new MediaTracker(component);
            mediaTracker.addImage(image, 0);
            try {
                mediaTracker.waitForAll(250L);
            } catch (InterruptedException e2) {
                Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (image.getWidth(null) <= 0 && image.getHeight(null) <= 0) {
                return image;
            }
            int width = (image.getWidth(null) * i2) / image.getHeight(null);
            if (width > 0 && i2 > 0) {
                image = image.getScaledInstance(width, i2, 4);
                map.put(Integer.valueOf(i2), image);
            }
        }
        if (map.size() > this.f9540c) {
            ArrayList<Integer> arrayList = new ArrayList();
            arrayList.addAll(map.keySet());
            for (Integer num : arrayList) {
                if (num.intValue() != i2) {
                    map.remove(num);
                }
            }
        }
        return image;
    }
}
