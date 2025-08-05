package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;
import java.util.Map;

/* loaded from: rt.jar:sun/nio/cs/FastCharsetProvider.class */
public class FastCharsetProvider extends CharsetProvider {
    private Map<String, String> classMap;
    private Map<String, String> aliasMap;
    private Map<String, Charset> cache;
    private String packagePrefix;

    protected FastCharsetProvider(String str, Map<String, String> map, Map<String, String> map2, Map<String, Charset> map3) {
        this.packagePrefix = str;
        this.aliasMap = map;
        this.classMap = map2;
        this.cache = map3;
    }

    private String canonicalize(String str) {
        String str2 = this.aliasMap.get(str);
        return str2 != null ? str2 : str;
    }

    private static String toLower(String str) {
        int length = str.length();
        boolean z2 = true;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char cCharAt = str.charAt(i2);
            if (((cCharAt - 'A') | ('Z' - cCharAt)) < 0) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        if (z2) {
            return str;
        }
        char[] cArr = new char[length];
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt2 = str.charAt(i3);
            if (((cCharAt2 - 'A') | ('Z' - cCharAt2)) >= 0) {
                cArr[i3] = (char) (cCharAt2 + ' ');
            } else {
                cArr[i3] = cCharAt2;
            }
        }
        return new String(cArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Charset lookup(String str) {
        String strCanonicalize = canonicalize(toLower(str));
        Charset charset = this.cache.get(strCanonicalize);
        if (charset != null) {
            return charset;
        }
        String str2 = this.classMap.get(strCanonicalize);
        if (str2 == null) {
            return null;
        }
        if (str2.equals("US_ASCII")) {
            US_ASCII us_ascii = new US_ASCII();
            this.cache.put(strCanonicalize, us_ascii);
            return us_ascii;
        }
        try {
            Charset charset2 = (Charset) Class.forName(this.packagePrefix + "." + str2, true, getClass().getClassLoader()).newInstance();
            this.cache.put(strCanonicalize, charset2);
            return charset2;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
            return null;
        }
    }

    @Override // java.nio.charset.spi.CharsetProvider
    public final Charset charsetForName(String str) {
        Charset charsetLookup;
        synchronized (this) {
            charsetLookup = lookup(canonicalize(str));
        }
        return charsetLookup;
    }

    @Override // java.nio.charset.spi.CharsetProvider
    public final Iterator<Charset> charsets() {
        return new Iterator<Charset>() { // from class: sun.nio.cs.FastCharsetProvider.1

            /* renamed from: i, reason: collision with root package name */
            Iterator<String> f13590i;

            {
                this.f13590i = FastCharsetProvider.this.classMap.keySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f13590i.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Charset next() {
                return FastCharsetProvider.this.lookup(this.f13590i.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
