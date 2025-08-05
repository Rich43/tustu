package java.nio.charset;

import com.sun.jmx.defaults.ServiceName;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.spi.CharsetProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import sun.misc.ASCIICaseInsensitiveComparator;
import sun.misc.VM;
import sun.nio.cs.ThreadLocalCoders;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/nio/charset/Charset.class */
public abstract class Charset implements Comparable<Charset> {
    private static volatile String bugLevel = null;
    private static CharsetProvider standardProvider = new sun.nio.cs.StandardCharsets();
    private static volatile Object[] cache1 = null;
    private static volatile Object[] cache2 = null;
    private static ThreadLocal<ThreadLocal<?>> gate = new ThreadLocal<>();
    private static volatile Charset defaultCharset;
    private final String name;
    private final String[] aliases;
    private Set<String> aliasSet = null;

    public abstract boolean contains(Charset charset);

    public abstract CharsetDecoder newDecoder();

    public abstract CharsetEncoder newEncoder();

    static boolean atBugLevel(String str) {
        String str2 = bugLevel;
        if (str2 == null) {
            if (!VM.isBooted()) {
                return false;
            }
            String str3 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.cs.bugLevel", ""));
            str2 = str3;
            bugLevel = str3;
        }
        return str2.equals(str);
    }

    private static void checkName(String str) {
        int length = str.length();
        if (!atBugLevel(ServiceName.JMX_SPEC_VERSION) && length == 0) {
            throw new IllegalCharsetNameException(str);
        }
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt < 'A' || cCharAt > 'Z') && ((cCharAt < 'a' || cCharAt > 'z') && ((cCharAt < '0' || cCharAt > '9') && ((cCharAt != '-' || i2 == 0) && ((cCharAt != '+' || i2 == 0) && ((cCharAt != ':' || i2 == 0) && ((cCharAt != '_' || i2 == 0) && (cCharAt != '.' || i2 == 0)))))))) {
                throw new IllegalCharsetNameException(str);
            }
        }
    }

    private static void cache(String str, Charset charset) {
        cache2 = cache1;
        cache1 = new Object[]{str, charset};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterator<CharsetProvider> providers() {
        return new Iterator<CharsetProvider>() { // from class: java.nio.charset.Charset.1
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            ServiceLoader<CharsetProvider> sl = ServiceLoader.load(CharsetProvider.class, this.cl);

            /* renamed from: i, reason: collision with root package name */
            Iterator<CharsetProvider> f12468i = this.sl.iterator();
            CharsetProvider next = null;

            private boolean getNext() {
                while (this.next == null) {
                    try {
                    } catch (ServiceConfigurationError e2) {
                        if (!(e2.getCause() instanceof SecurityException)) {
                            throw e2;
                        }
                    }
                    if (!this.f12468i.hasNext()) {
                        return false;
                    }
                    this.next = this.f12468i.next();
                }
                return true;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return getNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public CharsetProvider next() {
                if (!getNext()) {
                    throw new NoSuchElementException();
                }
                CharsetProvider charsetProvider = this.next;
                this.next = null;
                return charsetProvider;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static Charset lookupViaProviders(final String str) {
        if (!VM.isBooted() || gate.get() != null) {
            return null;
        }
        try {
            gate.set(gate);
            Charset charset = (Charset) AccessController.doPrivileged(new PrivilegedAction<Charset>() { // from class: java.nio.charset.Charset.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Charset run() {
                    Iterator itProviders = Charset.providers();
                    while (itProviders.hasNext()) {
                        Charset charsetCharsetForName = ((CharsetProvider) itProviders.next()).charsetForName(str);
                        if (charsetCharsetForName != null) {
                            return charsetCharsetForName;
                        }
                    }
                    return null;
                }
            });
            gate.set(null);
            return charset;
        } catch (Throwable th) {
            gate.set(null);
            throw th;
        }
    }

    /* loaded from: rt.jar:java/nio/charset/Charset$ExtendedProviderHolder.class */
    private static class ExtendedProviderHolder {
        static final CharsetProvider extendedProvider = extendedProvider();

        private ExtendedProviderHolder() {
        }

        private static CharsetProvider extendedProvider() {
            return (CharsetProvider) AccessController.doPrivileged(new PrivilegedAction<CharsetProvider>() { // from class: java.nio.charset.Charset.ExtendedProviderHolder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public CharsetProvider run() {
                    try {
                        return (CharsetProvider) Class.forName("sun.nio.cs.ext.ExtendedCharsets").newInstance();
                    } catch (ClassNotFoundException e2) {
                        return null;
                    } catch (IllegalAccessException | InstantiationException e3) {
                        throw new Error(e3);
                    }
                }
            });
        }
    }

    private static Charset lookupExtendedCharset(String str) {
        CharsetProvider charsetProvider = ExtendedProviderHolder.extendedProvider;
        if (charsetProvider != null) {
            return charsetProvider.charsetForName(str);
        }
        return null;
    }

    private static Charset lookup(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Null charset name");
        }
        Object[] objArr = cache1;
        if (objArr != null && str.equals(objArr[0])) {
            return (Charset) objArr[1];
        }
        return lookup2(str);
    }

    private static Charset lookup2(String str) {
        Object[] objArr = cache2;
        if (objArr != null && str.equals(objArr[0])) {
            cache2 = cache1;
            cache1 = objArr;
            return (Charset) objArr[1];
        }
        Charset charsetCharsetForName = standardProvider.charsetForName(str);
        Charset charset = charsetCharsetForName;
        if (charsetCharsetForName == null) {
            Charset charsetLookupExtendedCharset = lookupExtendedCharset(str);
            charset = charsetLookupExtendedCharset;
            if (charsetLookupExtendedCharset == null) {
                Charset charsetLookupViaProviders = lookupViaProviders(str);
                charset = charsetLookupViaProviders;
                if (charsetLookupViaProviders == null) {
                    checkName(str);
                    return null;
                }
            }
        }
        cache(str, charset);
        return charset;
    }

    public static boolean isSupported(String str) {
        return lookup(str) != null;
    }

    public static Charset forName(String str) {
        Charset charsetLookup = lookup(str);
        if (charsetLookup != null) {
            return charsetLookup;
        }
        throw new UnsupportedCharsetException(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void put(Iterator<Charset> it, Map<String, Charset> map) {
        while (it.hasNext()) {
            Charset next = it.next();
            if (!map.containsKey(next.name())) {
                map.put(next.name(), next);
            }
        }
    }

    public static SortedMap<String, Charset> availableCharsets() {
        return (SortedMap) AccessController.doPrivileged(new PrivilegedAction<SortedMap<String, Charset>>() { // from class: java.nio.charset.Charset.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public SortedMap<String, Charset> run() {
                TreeMap treeMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
                Charset.put(Charset.standardProvider.charsets(), treeMap);
                CharsetProvider charsetProvider = ExtendedProviderHolder.extendedProvider;
                if (charsetProvider != null) {
                    Charset.put(charsetProvider.charsets(), treeMap);
                }
                Iterator itProviders = Charset.providers();
                while (itProviders.hasNext()) {
                    Charset.put(((CharsetProvider) itProviders.next()).charsets(), treeMap);
                }
                return Collections.unmodifiableSortedMap(treeMap);
            }
        });
    }

    public static Charset defaultCharset() {
        if (defaultCharset == null) {
            synchronized (Charset.class) {
                Charset charsetLookup = lookup((String) AccessController.doPrivileged(new GetPropertyAction("file.encoding")));
                if (charsetLookup != null) {
                    defaultCharset = charsetLookup;
                } else {
                    defaultCharset = forName("UTF-8");
                }
            }
        }
        return defaultCharset;
    }

    protected Charset(String str, String[] strArr) {
        checkName(str);
        String[] strArr2 = strArr == null ? new String[0] : strArr;
        for (String str2 : strArr2) {
            checkName(str2);
        }
        this.name = str;
        this.aliases = strArr2;
    }

    public final String name() {
        return this.name;
    }

    public final Set<String> aliases() {
        if (this.aliasSet != null) {
            return this.aliasSet;
        }
        int length = this.aliases.length;
        HashSet hashSet = new HashSet(length);
        for (int i2 = 0; i2 < length; i2++) {
            hashSet.add(this.aliases[i2]);
        }
        this.aliasSet = Collections.unmodifiableSet(hashSet);
        return this.aliasSet;
    }

    public String displayName() {
        return this.name;
    }

    public final boolean isRegistered() {
        return (this.name.startsWith("X-") || this.name.startsWith("x-")) ? false : true;
    }

    public String displayName(Locale locale) {
        return this.name;
    }

    public boolean canEncode() {
        return true;
    }

    public final CharBuffer decode(ByteBuffer byteBuffer) {
        try {
            return ThreadLocalCoders.decoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(byteBuffer);
        } catch (CharacterCodingException e2) {
            throw new Error(e2);
        }
    }

    public final ByteBuffer encode(CharBuffer charBuffer) {
        try {
            return ThreadLocalCoders.encoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(charBuffer);
        } catch (CharacterCodingException e2) {
            throw new Error(e2);
        }
    }

    public final ByteBuffer encode(String str) {
        return encode(CharBuffer.wrap(str));
    }

    @Override // java.lang.Comparable
    public final int compareTo(Charset charset) {
        return name().compareToIgnoreCase(charset.name());
    }

    public final int hashCode() {
        return name().hashCode();
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Charset)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return this.name.equals(((Charset) obj).name());
    }

    public final String toString() {
        return name();
    }
}
