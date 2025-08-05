package java.text;

import java.lang.ref.SoftReference;
import java.text.spi.CollatorProvider;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:java/text/Collator.class */
public abstract class Collator implements Comparator<Object>, Cloneable {
    public static final int PRIMARY = 0;
    public static final int SECONDARY = 1;
    public static final int TERTIARY = 2;
    public static final int IDENTICAL = 3;
    public static final int NO_DECOMPOSITION = 0;
    public static final int CANONICAL_DECOMPOSITION = 1;
    public static final int FULL_DECOMPOSITION = 2;
    private int strength;
    private int decmp;
    private static final ConcurrentMap<Locale, SoftReference<Collator>> cache = new ConcurrentHashMap();
    static final int LESS = -1;
    static final int EQUAL = 0;
    static final int GREATER = 1;

    public abstract int compare(String str, String str2);

    public abstract CollationKey getCollationKey(String str);

    public abstract int hashCode();

    public static synchronized Collator getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static Collator getInstance(Locale locale) {
        SoftReference<Collator> softReferencePutIfAbsent = cache.get(locale);
        Collator collatorProvider = softReferencePutIfAbsent != null ? softReferencePutIfAbsent.get() : null;
        if (collatorProvider == null) {
            collatorProvider = LocaleProviderAdapter.getAdapter(CollatorProvider.class, locale).getCollatorProvider().getInstance(locale);
            if (collatorProvider == null) {
                collatorProvider = LocaleProviderAdapter.forJRE().getCollatorProvider().getInstance(locale);
            }
            while (true) {
                if (softReferencePutIfAbsent != null) {
                    cache.remove(locale, softReferencePutIfAbsent);
                }
                softReferencePutIfAbsent = cache.putIfAbsent(locale, new SoftReference<>(collatorProvider));
                if (softReferencePutIfAbsent == null) {
                    break;
                }
                Collator collator = softReferencePutIfAbsent.get();
                if (collator != null) {
                    collatorProvider = collator;
                    break;
                }
            }
        }
        return (Collator) collatorProvider.clone();
    }

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        return compare((String) obj, (String) obj2);
    }

    public boolean equals(String str, String str2) {
        return compare(str, str2) == 0;
    }

    public synchronized int getStrength() {
        return this.strength;
    }

    public synchronized void setStrength(int i2) {
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 3) {
            throw new IllegalArgumentException("Incorrect comparison level.");
        }
        this.strength = i2;
    }

    public synchronized int getDecomposition() {
        return this.decmp;
    }

    public synchronized void setDecomposition(int i2) {
        if (i2 != 0 && i2 != 1 && i2 != 2) {
            throw new IllegalArgumentException("Wrong decomposition mode.");
        }
        this.decmp = i2;
    }

    public static synchronized Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(CollatorProvider.class).getAvailableLocales();
    }

    public Object clone() {
        try {
            return (Collator) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Collator collator = (Collator) obj;
        return this.strength == collator.strength && this.decmp == collator.decmp;
    }

    protected Collator() {
        this.strength = 0;
        this.decmp = 0;
        this.strength = 2;
        this.decmp = 1;
    }
}
