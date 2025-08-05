package sun.nio.cs;

import java.lang.ref.SoftReference;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import sun.misc.ASCIICaseInsensitiveComparator;

/* loaded from: rt.jar:sun/nio/cs/AbstractCharsetProvider.class */
public class AbstractCharsetProvider extends CharsetProvider {
    private Map<String, String> classMap;
    private Map<String, String> aliasMap;
    private Map<String, String[]> aliasNameMap;
    private Map<String, SoftReference<Charset>> cache;
    private String packagePrefix;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractCharsetProvider.class.desiredAssertionStatus();
    }

    protected AbstractCharsetProvider() {
        this.classMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.aliasMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.aliasNameMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.cache = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.packagePrefix = "sun.nio.cs";
    }

    protected AbstractCharsetProvider(String str) {
        this.classMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.aliasMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.aliasNameMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.cache = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
        this.packagePrefix = str;
    }

    private static <K, V> void put(Map<K, V> map, K k2, V v2) {
        if (!map.containsKey(k2)) {
            map.put(k2, v2);
        }
    }

    private static <K, V> void remove(Map<K, V> map, K k2) {
        V vRemove = map.remove(k2);
        if (!$assertionsDisabled && vRemove == null) {
            throw new AssertionError();
        }
    }

    protected void charset(String str, String str2, String[] strArr) {
        synchronized (this) {
            put(this.classMap, str, str2);
            for (String str3 : strArr) {
                put(this.aliasMap, str3, str);
            }
            put(this.aliasNameMap, str, strArr);
            this.cache.clear();
        }
    }

    protected void deleteCharset(String str, String[] strArr) {
        synchronized (this) {
            remove(this.classMap, str);
            for (String str2 : strArr) {
                remove(this.aliasMap, str2);
            }
            remove(this.aliasNameMap, str);
            this.cache.clear();
        }
    }

    protected void init() {
    }

    private String canonicalize(String str) {
        String str2 = this.aliasMap.get(str);
        return str2 != null ? str2 : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Charset lookup(String str) {
        Charset charset;
        SoftReference<Charset> softReference = this.cache.get(str);
        if (softReference != null && (charset = softReference.get()) != null) {
            return charset;
        }
        String str2 = this.classMap.get(str);
        if (str2 == null) {
            return null;
        }
        try {
            Charset charset2 = (Charset) Class.forName(this.packagePrefix + "." + str2, true, getClass().getClassLoader()).newInstance();
            this.cache.put(str, new SoftReference<>(charset2));
            return charset2;
        } catch (ClassNotFoundException e2) {
            return null;
        } catch (IllegalAccessException e3) {
            return null;
        } catch (InstantiationException e4) {
            return null;
        }
    }

    @Override // java.nio.charset.spi.CharsetProvider
    public final Charset charsetForName(String str) {
        Charset charsetLookup;
        synchronized (this) {
            init();
            charsetLookup = lookup(canonicalize(str));
        }
        return charsetLookup;
    }

    @Override // java.nio.charset.spi.CharsetProvider
    public final Iterator<Charset> charsets() {
        final ArrayList arrayList;
        synchronized (this) {
            init();
            arrayList = new ArrayList(this.classMap.keySet());
        }
        return new Iterator<Charset>() { // from class: sun.nio.cs.AbstractCharsetProvider.1

            /* renamed from: i, reason: collision with root package name */
            Iterator<String> f13587i;

            {
                this.f13587i = arrayList.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f13587i.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Charset next() {
                Charset charsetLookup;
                String next = this.f13587i.next();
                synchronized (AbstractCharsetProvider.this) {
                    charsetLookup = AbstractCharsetProvider.this.lookup(next);
                }
                return charsetLookup;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public final String[] aliases(String str) {
        String[] strArr;
        synchronized (this) {
            init();
            strArr = this.aliasNameMap.get(str);
        }
        return strArr;
    }
}
