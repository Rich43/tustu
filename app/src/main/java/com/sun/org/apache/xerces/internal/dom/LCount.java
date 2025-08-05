package com.sun.org.apache.xerces.internal.dom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/LCount.class */
class LCount {
    static final Map<String, LCount> lCounts = new ConcurrentHashMap();
    public int defaults;
    public int captures = 0;
    public int bubbles = 0;
    public int total = 0;

    LCount() {
    }

    static LCount lookup(String evtName) {
        LCount lc = lCounts.get(evtName);
        if (lc == null) {
            Map<String, LCount> map = lCounts;
            LCount lCount = new LCount();
            lc = lCount;
            map.put(evtName, lCount);
        }
        return lc;
    }
}
