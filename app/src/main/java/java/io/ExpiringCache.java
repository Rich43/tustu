package java.io;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:java/io/ExpiringCache.class */
class ExpiringCache {
    private long millisUntilExpiration;
    private Map<String, Entry> map;
    private int queryCount;
    private int queryOverflow;
    private int MAX_ENTRIES;

    /* loaded from: rt.jar:java/io/ExpiringCache$Entry.class */
    static class Entry {
        private long timestamp;
        private String val;

        Entry(long j2, String str) {
            this.timestamp = j2;
            this.val = str;
        }

        long timestamp() {
            return this.timestamp;
        }

        void setTimestamp(long j2) {
            this.timestamp = j2;
        }

        String val() {
            return this.val;
        }

        void setVal(String str) {
            this.val = str;
        }
    }

    ExpiringCache() {
        this(30000L);
    }

    ExpiringCache(long j2) {
        this.queryOverflow = 300;
        this.MAX_ENTRIES = 200;
        this.millisUntilExpiration = j2;
        this.map = new LinkedHashMap<String, Entry>() { // from class: java.io.ExpiringCache.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<String, Entry> entry) {
                return size() > ExpiringCache.this.MAX_ENTRIES;
            }
        };
    }

    synchronized String get(String str) {
        int i2 = this.queryCount + 1;
        this.queryCount = i2;
        if (i2 >= this.queryOverflow) {
            cleanup();
        }
        Entry entryEntryFor = entryFor(str);
        if (entryEntryFor != null) {
            return entryEntryFor.val();
        }
        return null;
    }

    synchronized void put(String str, String str2) {
        int i2 = this.queryCount + 1;
        this.queryCount = i2;
        if (i2 >= this.queryOverflow) {
            cleanup();
        }
        Entry entryEntryFor = entryFor(str);
        if (entryEntryFor != null) {
            entryEntryFor.setTimestamp(System.currentTimeMillis());
            entryEntryFor.setVal(str2);
        } else {
            this.map.put(str, new Entry(System.currentTimeMillis(), str2));
        }
    }

    synchronized void clear() {
        this.map.clear();
    }

    private Entry entryFor(String str) {
        Entry entry = this.map.get(str);
        if (entry != null) {
            long jCurrentTimeMillis = System.currentTimeMillis() - entry.timestamp();
            if (jCurrentTimeMillis < 0 || jCurrentTimeMillis >= this.millisUntilExpiration) {
                this.map.remove(str);
                entry = null;
            }
        }
        return entry;
    }

    private void cleanup() {
        Set<String> setKeySet = this.map.keySet();
        String[] strArr = new String[setKeySet.size()];
        int i2 = 0;
        Iterator<String> it = setKeySet.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            strArr[i3] = it.next();
        }
        for (String str : strArr) {
            entryFor(str);
        }
        this.queryCount = 0;
    }
}
