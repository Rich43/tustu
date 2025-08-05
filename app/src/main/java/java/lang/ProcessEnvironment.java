package java.lang;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/lang/ProcessEnvironment.class */
final class ProcessEnvironment extends HashMap<String, String> {
    private static final long serialVersionUID = -8017839552603542824L;
    static final int MIN_NAME_LENGTH = 1;
    private static final NameComparator nameComparator = new NameComparator();
    private static final EntryComparator entryComparator = new EntryComparator();
    private static final ProcessEnvironment theEnvironment = new ProcessEnvironment();
    private static final Map<String, String> theUnmodifiableEnvironment = Collections.unmodifiableMap(theEnvironment);
    private static final Map<String, String> theCaseInsensitiveEnvironment;

    private static native String environmentBlock();

    private static String validateName(String str) {
        if (str.indexOf(61, 1) != -1 || str.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable name: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String validateValue(String str) {
        if (str.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable value: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String nonNullString(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return (String) obj;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public String put(String str, String str2) {
        return (String) super.put((ProcessEnvironment) validateName(str), validateValue(str2));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public String get(Object obj) {
        return (String) super.get((Object) nonNullString(obj));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return super.containsKey(nonNullString(obj));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        return super.containsValue(nonNullString(obj));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public String remove(Object obj) {
        return (String) super.remove((Object) nonNullString(obj));
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$CheckedEntry.class */
    private static class CheckedEntry implements Map.Entry<String, String> {

        /* renamed from: e, reason: collision with root package name */
        private final Map.Entry<String, String> f12439e;

        public CheckedEntry(Map.Entry<String, String> entry) {
            this.f12439e = entry;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public String getKey() {
            return this.f12439e.getKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public String getValue() {
            return this.f12439e.getValue();
        }

        @Override // java.util.Map.Entry
        public String setValue(String str) {
            return this.f12439e.setValue(ProcessEnvironment.validateValue(str));
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            return this.f12439e.equals(obj);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.f12439e.hashCode();
        }
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$CheckedEntrySet.class */
    private static class CheckedEntrySet extends AbstractSet<Map.Entry<String, String>> {

        /* renamed from: s, reason: collision with root package name */
        private final Set<Map.Entry<String, String>> f12440s;

        public CheckedEntrySet(Set<Map.Entry<String, String>> set) {
            this.f12440s = set;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12440s.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12440s.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12440s.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<String, String>> iterator() {
            return new Iterator<Map.Entry<String, String>>() { // from class: java.lang.ProcessEnvironment.CheckedEntrySet.1

                /* renamed from: i, reason: collision with root package name */
                Iterator<Map.Entry<String, String>> f12441i;

                {
                    this.f12441i = CheckedEntrySet.this.f12440s.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.f12441i.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<String, String> next() {
                    return new CheckedEntry(this.f12441i.next());
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.f12441i.remove();
                }
            };
        }

        private static Map.Entry<String, String> checkedEntry(Object obj) {
            Map.Entry<String, String> entry = (Map.Entry) obj;
            ProcessEnvironment.nonNullString(entry.getKey());
            ProcessEnvironment.nonNullString(entry.getValue());
            return entry;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12440s.contains(checkedEntry(obj));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12440s.remove(checkedEntry(obj));
        }
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$CheckedValues.class */
    private static class CheckedValues extends AbstractCollection<String> {

        /* renamed from: c, reason: collision with root package name */
        private final Collection<String> f12443c;

        public CheckedValues(Collection<String> collection) {
            this.f12443c = collection;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12443c.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12443c.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12443c.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<String> iterator() {
            return this.f12443c.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12443c.contains(ProcessEnvironment.nonNullString(obj));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12443c.remove(ProcessEnvironment.nonNullString(obj));
        }
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$CheckedKeySet.class */
    private static class CheckedKeySet extends AbstractSet<String> {

        /* renamed from: s, reason: collision with root package name */
        private final Set<String> f12442s;

        public CheckedKeySet(Set<String> set) {
            this.f12442s = set;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12442s.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12442s.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12442s.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<String> iterator() {
            return this.f12442s.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12442s.contains(ProcessEnvironment.nonNullString(obj));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12442s.remove(ProcessEnvironment.nonNullString(obj));
        }
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Set<String> keySet() {
        return new CheckedKeySet(super.keySet());
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Collection<String> values() {
        return new CheckedValues(super.values());
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, String>> entrySet() {
        return new CheckedEntrySet(super.entrySet());
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$NameComparator.class */
    private static final class NameComparator implements Comparator<String> {
        private NameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            char upperCase;
            char upperCase2;
            int length = str.length();
            int length2 = str2.length();
            int iMin = Math.min(length, length2);
            for (int i2 = 0; i2 < iMin; i2++) {
                char cCharAt = str.charAt(i2);
                char cCharAt2 = str2.charAt(i2);
                if (cCharAt != cCharAt2 && (upperCase = Character.toUpperCase(cCharAt)) != (upperCase2 = Character.toUpperCase(cCharAt2))) {
                    return upperCase - upperCase2;
                }
            }
            return length - length2;
        }
    }

    /* loaded from: rt.jar:java/lang/ProcessEnvironment$EntryComparator.class */
    private static final class EntryComparator implements Comparator<Map.Entry<String, String>> {
        private EntryComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Map.Entry<String, String> entry, Map.Entry<String, String> entry2) {
            return ProcessEnvironment.nameComparator.compare(entry.getKey(), entry2.getKey());
        }
    }

    static {
        int iIndexOf;
        String strEnvironmentBlock = environmentBlock();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            int iIndexOf2 = strEnvironmentBlock.indexOf(0, i3);
            if (iIndexOf2 == -1 || (iIndexOf = strEnvironmentBlock.indexOf(61, i3 + 1)) == -1) {
                break;
            }
            if (iIndexOf < iIndexOf2) {
                theEnvironment.put(strEnvironmentBlock.substring(i3, iIndexOf), strEnvironmentBlock.substring(iIndexOf + 1, iIndexOf2));
            }
            i2 = iIndexOf2 + 1;
        }
        theCaseInsensitiveEnvironment = new TreeMap(nameComparator);
        theCaseInsensitiveEnvironment.putAll(theEnvironment);
    }

    private ProcessEnvironment() {
    }

    private ProcessEnvironment(int i2) {
        super(i2);
    }

    static String getenv(String str) {
        return theCaseInsensitiveEnvironment.get(str);
    }

    static Map<String, String> getenv() {
        return theUnmodifiableEnvironment;
    }

    static Map<String, String> environment() {
        return (Map) theEnvironment.clone();
    }

    static Map<String, String> emptyEnvironment(int i2) {
        return new ProcessEnvironment(i2);
    }

    String toEnvironmentBlock() {
        ArrayList<Map.Entry> arrayList = new ArrayList(entrySet());
        Collections.sort(arrayList, entryComparator);
        StringBuilder sb = new StringBuilder(size() * 30);
        int i2 = -1;
        for (Map.Entry entry : arrayList) {
            String str = (String) entry.getKey();
            String str2 = (String) entry.getValue();
            if (i2 < 0) {
                int iCompare = nameComparator.compare(str, "SystemRoot");
                i2 = iCompare;
                if (iCompare > 0) {
                    addToEnvIfSet(sb, "SystemRoot");
                }
            }
            addToEnv(sb, str, str2);
        }
        if (i2 < 0) {
            addToEnvIfSet(sb, "SystemRoot");
        }
        if (sb.length() == 0) {
            sb.append((char) 0);
        }
        sb.append((char) 0);
        return sb.toString();
    }

    private static void addToEnvIfSet(StringBuilder sb, String str) {
        String str2 = getenv(str);
        if (str2 != null) {
            addToEnv(sb, str, str2);
        }
    }

    private static void addToEnv(StringBuilder sb, String str, String str2) {
        sb.append(str).append('=').append(str2).append((char) 0);
    }

    static String toEnvironmentBlock(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        return ((ProcessEnvironment) map).toEnvironmentBlock();
    }
}
