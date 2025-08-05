package java.lang.ref;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/* loaded from: rt.jar:java/lang/ref/FinalizerHistogram.class */
final class FinalizerHistogram {
    FinalizerHistogram() {
    }

    /* loaded from: rt.jar:java/lang/ref/FinalizerHistogram$Entry.class */
    private static final class Entry {
        private int instanceCount;
        private final String className;

        int getInstanceCount() {
            return this.instanceCount;
        }

        void increment() {
            this.instanceCount++;
        }

        Entry(String str) {
            this.className = str;
        }
    }

    static Entry[] getFinalizerHistogram() {
        HashMap map = new HashMap();
        Finalizer.getQueue().forEach(reference -> {
            Object obj = reference.get();
            if (obj != null) {
                ((Entry) map.computeIfAbsent(obj.getClass().getName(), Entry::new)).increment();
            }
        });
        Entry[] entryArr = (Entry[]) map.values().toArray(new Entry[map.size()]);
        Arrays.sort(entryArr, Comparator.comparingInt((v0) -> {
            return v0.getInstanceCount();
        }).reversed());
        return entryArr;
    }
}
