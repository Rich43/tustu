package com.sun.xml.internal.bind.v2.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.WeakHashMap;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/EditDistance.class */
public class EditDistance {
    private static final WeakHashMap<AbstractMap.SimpleEntry<String, String>, Integer> CACHE = new WeakHashMap<>();
    private int[] cost;
    private int[] back;

    /* renamed from: a, reason: collision with root package name */
    private final String f12075a;

    /* renamed from: b, reason: collision with root package name */
    private final String f12076b;

    public static int editDistance(String a2, String b2) {
        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>(a2, b2);
        Integer result = null;
        if (CACHE.containsKey(entry)) {
            result = CACHE.get(entry);
        }
        if (result == null) {
            result = Integer.valueOf(new EditDistance(a2, b2).calc());
            CACHE.put(entry, result);
        }
        return result.intValue();
    }

    public static String findNearest(String key, String[] group) {
        return findNearest(key, Arrays.asList(group));
    }

    public static String findNearest(String key, Collection<String> group) {
        int c2 = Integer.MAX_VALUE;
        String r2 = null;
        for (String s2 : group) {
            int ed = editDistance(key, s2);
            if (c2 > ed) {
                c2 = ed;
                r2 = s2;
            }
        }
        return r2;
    }

    private EditDistance(String a2, String b2) {
        this.f12075a = a2;
        this.f12076b = b2;
        this.cost = new int[a2.length() + 1];
        this.back = new int[a2.length() + 1];
        for (int i2 = 0; i2 <= a2.length(); i2++) {
            this.cost[i2] = i2;
        }
    }

    private void flip() {
        int[] t2 = this.cost;
        this.cost = this.back;
        this.back = t2;
    }

    private int min(int a2, int b2, int c2) {
        return Math.min(a2, Math.min(b2, c2));
    }

    private int calc() {
        for (int j2 = 0; j2 < this.f12076b.length(); j2++) {
            flip();
            this.cost[0] = j2 + 1;
            for (int i2 = 0; i2 < this.f12075a.length(); i2++) {
                int match = this.f12075a.charAt(i2) == this.f12076b.charAt(j2) ? 0 : 1;
                this.cost[i2 + 1] = min(this.back[i2] + match, this.cost[i2] + 1, this.back[i2 + 1] + 1);
            }
        }
        return this.cost[this.f12075a.length()];
    }
}
