package java.lang;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/lang/ClassValue.class */
public abstract class ClassValue<T> {
    private static final int HASH_INCREMENT = 1640531527;
    static final int HASH_MASK = 1073741823;
    private static final Entry<?>[] EMPTY_CACHE = {null};
    private static final AtomicInteger nextHashCode = new AtomicInteger();
    private static final Object CRITICAL_SECTION = new Object();
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    final int hashCodeForCache = nextHashCode.getAndAdd(HASH_INCREMENT) & HASH_MASK;
    final Identity identity = new Identity();
    private volatile Version<T> version = new Version<>(this);

    protected abstract T computeValue(Class<?> cls);

    protected ClassValue() {
    }

    public T get(Class<?> cls) {
        Entry<?>[] cacheCarefully = getCacheCarefully(cls);
        Entry<?> entryProbeHomeLocation = ClassValueMap.probeHomeLocation(cacheCarefully, this);
        if (match(entryProbeHomeLocation)) {
            return (T) entryProbeHomeLocation.value();
        }
        return getFromBackup(cacheCarefully, cls);
    }

    public void remove(Class<?> cls) {
        getMap(cls).removeEntry(this);
    }

    void put(Class<?> cls, T t2) {
        getMap(cls).changeEntry(this, t2);
    }

    private static Entry<?>[] getCacheCarefully(Class<?> cls) {
        ClassValueMap classValueMap = cls.classValueMap;
        return classValueMap == null ? EMPTY_CACHE : classValueMap.getCache();
    }

    private T getFromBackup(Entry<?>[] entryArr, Class<?> cls) {
        Entry entryProbeBackupLocations = ClassValueMap.probeBackupLocations(entryArr, this);
        if (entryProbeBackupLocations != null) {
            return (T) entryProbeBackupLocations.value();
        }
        return getFromHashMap(cls);
    }

    /* JADX WARN: Multi-variable type inference failed */
    Entry<T> castEntry(Entry<?> entry) {
        return entry;
    }

    private T getFromHashMap(Class<?> cls) {
        Entry<T> entryFinishEntry;
        ClassValueMap map = getMap(cls);
        do {
            Entry<T> entryStartEntry = map.startEntry(this);
            if (!entryStartEntry.isPromise()) {
                return entryStartEntry.value();
            }
            try {
                entryStartEntry = makeEntry(entryStartEntry.version(), computeValue(cls));
                entryFinishEntry = map.finishEntry(this, entryStartEntry);
            } catch (Throwable th) {
                map.finishEntry(this, entryStartEntry);
                throw th;
            }
        } while (entryFinishEntry == null);
        return entryFinishEntry.value();
    }

    boolean match(Entry<?> entry) {
        return entry != null && entry.get() == this.version;
    }

    /* loaded from: rt.jar:java/lang/ClassValue$Identity.class */
    static class Identity {
        Identity() {
        }
    }

    Version<T> version() {
        return this.version;
    }

    void bumpVersion() {
        this.version = new Version<>(this);
    }

    /* loaded from: rt.jar:java/lang/ClassValue$Version.class */
    static class Version<T> {
        private final ClassValue<T> classValue;
        private final Entry<T> promise = new Entry<>(this);

        Version(ClassValue<T> classValue) {
            this.classValue = classValue;
        }

        ClassValue<T> classValue() {
            return this.classValue;
        }

        Entry<T> promise() {
            return this.promise;
        }

        boolean isLive() {
            return this.classValue.version() == this;
        }
    }

    /* loaded from: rt.jar:java/lang/ClassValue$Entry.class */
    static class Entry<T> extends WeakReference<Version<T>> {
        final Object value;
        static final Entry<?> DEAD_ENTRY;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ClassValue.class.desiredAssertionStatus();
            DEAD_ENTRY = new Entry<>(null, null);
        }

        Entry(Version<T> version, T t2) {
            super(version);
            this.value = t2;
        }

        private void assertNotPromise() {
            if (!$assertionsDisabled && isPromise()) {
                throw new AssertionError();
            }
        }

        Entry(Version<T> version) {
            super(version);
            this.value = this;
        }

        T value() {
            assertNotPromise();
            return (T) this.value;
        }

        boolean isPromise() {
            return this.value == this;
        }

        Version<T> version() {
            return (Version) get();
        }

        ClassValue<T> classValueOrNull() {
            Version<T> version = version();
            if (version == null) {
                return null;
            }
            return version.classValue();
        }

        boolean isLive() {
            Version<T> version = version();
            if (version == null) {
                return false;
            }
            if (version.isLive()) {
                return true;
            }
            clear();
            return false;
        }

        Entry<T> refreshVersion(Version<T> version) {
            assertNotPromise();
            Entry<T> entry = new Entry<>(version, this.value);
            clear();
            return entry;
        }
    }

    private static ClassValueMap getMap(Class<?> cls) {
        ClassValueMap classValueMap = cls.classValueMap;
        return classValueMap != null ? classValueMap : initializeMap(cls);
    }

    private static ClassValueMap initializeMap(Class<?> cls) {
        ClassValueMap classValueMap;
        synchronized (CRITICAL_SECTION) {
            ClassValueMap classValueMap2 = cls.classValueMap;
            classValueMap = classValueMap2;
            if (classValueMap2 == null) {
                classValueMap = new ClassValueMap(cls);
                UNSAFE.storeFence();
                cls.classValueMap = classValueMap;
            }
        }
        return classValueMap;
    }

    static <T> Entry<T> makeEntry(Version<T> version, T t2) {
        return new Entry<>(version, t2);
    }

    /* loaded from: rt.jar:java/lang/ClassValue$ClassValueMap.class */
    static class ClassValueMap extends WeakHashMap<Identity, Entry<?>> {
        private final Class<?> type;
        private Entry<?>[] cacheArray;
        private int cacheLoad;
        private int cacheLoadLimit;
        private static final int INITIAL_ENTRIES = 32;
        private static final int CACHE_LOAD_LIMIT = 67;
        private static final int PROBE_LIMIT = 6;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ClassValue.class.desiredAssertionStatus();
        }

        ClassValueMap(Class<?> cls) {
            this.type = cls;
            sizeCache(32);
        }

        Entry<?>[] getCache() {
            return this.cacheArray;
        }

        synchronized <T> Entry<T> startEntry(ClassValue<T> classValue) {
            Entry<T> entryRefreshVersion = (Entry) get(classValue.identity);
            Version<T> version = classValue.version();
            if (entryRefreshVersion == null) {
                Entry<T> entryPromise = version.promise();
                put(classValue.identity, entryPromise);
                return entryPromise;
            }
            if (entryRefreshVersion.isPromise()) {
                if (entryRefreshVersion.version() != version) {
                    entryRefreshVersion = version.promise();
                    put(classValue.identity, entryRefreshVersion);
                }
                return entryRefreshVersion;
            }
            if (entryRefreshVersion.version() != version) {
                entryRefreshVersion = entryRefreshVersion.refreshVersion(version);
                put(classValue.identity, entryRefreshVersion);
            }
            checkCacheLoad();
            addToCache(classValue, entryRefreshVersion);
            return entryRefreshVersion;
        }

        synchronized <T> Entry<T> finishEntry(ClassValue<T> classValue, Entry<T> entry) {
            Entry<T> entry2 = (Entry) get(classValue.identity);
            if (entry == entry2) {
                if (!$assertionsDisabled && !entry.isPromise()) {
                    throw new AssertionError();
                }
                remove(classValue.identity);
                return null;
            }
            if (entry2 != null && entry2.isPromise() && entry2.version() == entry.version()) {
                Version<T> version = classValue.version();
                if (entry.version() != version) {
                    entry = entry.refreshVersion(version);
                }
                put(classValue.identity, entry);
                checkCacheLoad();
                addToCache(classValue, entry);
                return entry;
            }
            return null;
        }

        synchronized void removeEntry(ClassValue<?> classValue) {
            Entry<?> entryRemove = remove(classValue.identity);
            if (entryRemove != null) {
                if (entryRemove.isPromise()) {
                    put(classValue.identity, entryRemove);
                } else {
                    classValue.bumpVersion();
                    removeStaleEntries(classValue);
                }
            }
        }

        synchronized <T> void changeEntry(ClassValue<T> classValue, T t2) {
            Entry<?> entry = get(classValue.identity);
            Version<T> version = classValue.version();
            if (entry != null) {
                if (entry.version() == version && entry.value() == t2) {
                    return;
                }
                classValue.bumpVersion();
                removeStaleEntries(classValue);
            }
            Entry<T> entryMakeEntry = ClassValue.makeEntry(version, t2);
            put(classValue.identity, entryMakeEntry);
            checkCacheLoad();
            addToCache(classValue, entryMakeEntry);
        }

        static Entry<?> loadFromCache(Entry<?>[] entryArr, int i2) {
            return entryArr[i2 & (entryArr.length - 1)];
        }

        static <T> Entry<T> probeHomeLocation(Entry<?>[] entryArr, ClassValue<T> classValue) {
            return classValue.castEntry(loadFromCache(entryArr, classValue.hashCodeForCache));
        }

        static <T> Entry<T> probeBackupLocations(Entry<?>[] entryArr, ClassValue<T> classValue) {
            Entry<?> entry;
            int length = entryArr.length - 1;
            int i2 = classValue.hashCodeForCache & length;
            Entry<?> entry2 = entryArr[i2];
            if (entry2 == null) {
                return null;
            }
            int i3 = -1;
            for (int i4 = i2 + 1; i4 < i2 + 6 && (entry = entryArr[i4 & length]) != null; i4++) {
                if (classValue.match(entry)) {
                    entryArr[i2] = entry;
                    if (i3 >= 0) {
                        entryArr[i4 & length] = Entry.DEAD_ENTRY;
                    } else {
                        i3 = i4;
                    }
                    entryArr[i3 & length] = entryDislocation(entryArr, i3, entry2) < 6 ? entry2 : Entry.DEAD_ENTRY;
                    return classValue.castEntry(entry);
                }
                if (!entry.isLive() && i3 < 0) {
                    i3 = i4;
                }
            }
            return null;
        }

        private static int entryDislocation(Entry<?>[] entryArr, int i2, Entry<?> entry) {
            ClassValue<?> classValueClassValueOrNull = entry.classValueOrNull();
            if (classValueClassValueOrNull == null) {
                return 0;
            }
            return (i2 - classValueClassValueOrNull.hashCodeForCache) & (entryArr.length - 1);
        }

        private void sizeCache(int i2) {
            if (!$assertionsDisabled && (i2 & (i2 - 1)) != 0) {
                throw new AssertionError();
            }
            this.cacheLoad = 0;
            this.cacheLoadLimit = (int) ((i2 * 67.0d) / 100.0d);
            this.cacheArray = new Entry[i2];
        }

        private void checkCacheLoad() {
            if (this.cacheLoad >= this.cacheLoadLimit) {
                reduceCacheLoad();
            }
        }

        private void reduceCacheLoad() {
            removeStaleEntries();
            if (this.cacheLoad < this.cacheLoadLimit) {
                return;
            }
            Entry<?>[] cache = getCache();
            if (cache.length > ClassValue.HASH_MASK) {
                return;
            }
            sizeCache(cache.length * 2);
            for (Entry<?> entry : cache) {
                if (entry != null && entry.isLive()) {
                    addToCache(entry);
                }
            }
        }

        private void removeStaleEntries(Entry<?>[] entryArr, int i2, int i3) {
            int length = entryArr.length - 1;
            int i4 = 0;
            for (int i5 = i2; i5 < i2 + i3; i5++) {
                Entry<?> entry = entryArr[i5 & length];
                if (entry != null && !entry.isLive()) {
                    Entry<?> entryFindReplacement = findReplacement(entryArr, i5);
                    entryArr[i5 & length] = entryFindReplacement;
                    if (entryFindReplacement == null) {
                        i4++;
                    }
                }
            }
            this.cacheLoad = Math.max(0, this.cacheLoad - i4);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v28 */
        /* JADX WARN: Type inference failed for: r0v9 */
        private Entry<?> findReplacement(Entry<?>[] entryArr, int i2) {
            Entry<?> entry;
            int iEntryDislocation;
            int i3;
            Entry<?> entry2 = null;
            boolean z2 = -1;
            int i4 = 0;
            int length = entryArr.length - 1;
            for (int i5 = i2 + 1; i5 < i2 + 6 && (entry = entryArr[i5 & length]) != null; i5++) {
                if (entry.isLive() && (iEntryDislocation = entryDislocation(entryArr, i5, entry)) != 0 && (i3 = i5 - iEntryDislocation) <= i2) {
                    if (i3 == i2) {
                        z2 = true;
                        i4 = i5;
                        entry2 = entry;
                    } else if (z2 <= 0) {
                        z2 = false;
                        i4 = i5;
                        entry2 = entry;
                    }
                }
            }
            if (z2 >= 0) {
                if (entryArr[(i4 + 1) & length] != null) {
                    entryArr[i4 & length] = Entry.DEAD_ENTRY;
                } else {
                    entryArr[i4 & length] = null;
                    this.cacheLoad--;
                }
            }
            return entry2;
        }

        private void removeStaleEntries(ClassValue<?> classValue) {
            removeStaleEntries(getCache(), classValue.hashCodeForCache, 6);
        }

        private void removeStaleEntries() {
            Entry<?>[] cache = getCache();
            removeStaleEntries(cache, 0, (cache.length + 6) - 1);
        }

        private <T> void addToCache(Entry<T> entry) {
            ClassValue<T> classValueClassValueOrNull = entry.classValueOrNull();
            if (classValueClassValueOrNull != null) {
                addToCache(classValueClassValueOrNull, entry);
            }
        }

        private <T> void addToCache(ClassValue<T> classValue, Entry<T> entry) {
            Entry<?>[] cache = getCache();
            int length = cache.length - 1;
            int i2 = classValue.hashCodeForCache & length;
            Entry<?> entryPlaceInCache = placeInCache(cache, i2, entry, false);
            if (entryPlaceInCache == null) {
                return;
            }
            int iEntryDislocation = i2 - entryDislocation(cache, i2, entryPlaceInCache);
            for (int i3 = iEntryDislocation; i3 < iEntryDislocation + 6 && placeInCache(cache, i3 & length, entryPlaceInCache, true) != null; i3++) {
            }
        }

        private Entry<?> placeInCache(Entry<?>[] entryArr, int i2, Entry<?> entry, boolean z2) {
            Entry<?> entryOverwrittenEntry = overwrittenEntry(entryArr[i2]);
            if (z2 && entryOverwrittenEntry != null) {
                return entry;
            }
            entryArr[i2] = entry;
            return entryOverwrittenEntry;
        }

        private <T> Entry<T> overwrittenEntry(Entry<T> entry) {
            if (entry != null) {
                if (entry.isLive()) {
                    return entry;
                }
                return null;
            }
            this.cacheLoad++;
            return null;
        }
    }
}
