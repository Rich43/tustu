package java.text;

import java.lang.ref.SoftReference;
import java.text.spi.BreakIteratorProvider;
import java.util.Locale;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:java/text/BreakIterator.class */
public abstract class BreakIterator implements Cloneable {
    public static final int DONE = -1;
    private static final int CHARACTER_INDEX = 0;
    private static final int WORD_INDEX = 1;
    private static final int LINE_INDEX = 2;
    private static final int SENTENCE_INDEX = 3;
    private static final SoftReference<BreakIteratorCache>[] iterCache = new SoftReference[4];

    public abstract int first();

    public abstract int last();

    public abstract int next(int i2);

    public abstract int next();

    public abstract int previous();

    public abstract int following(int i2);

    public abstract int current();

    public abstract CharacterIterator getText();

    public abstract void setText(CharacterIterator characterIterator);

    protected BreakIterator() {
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public int preceding(int i2) {
        int i3;
        int iFollowing = following(i2);
        while (true) {
            i3 = iFollowing;
            if (i3 < i2 || i3 == -1) {
                break;
            }
            iFollowing = previous();
        }
        return i3;
    }

    public boolean isBoundary(int i2) {
        if (i2 == 0) {
            return true;
        }
        int iFollowing = following(i2 - 1);
        if (iFollowing == -1) {
            throw new IllegalArgumentException();
        }
        return iFollowing == i2;
    }

    public void setText(String str) {
        setText(new StringCharacterIterator(str));
    }

    public static BreakIterator getWordInstance() {
        return getWordInstance(Locale.getDefault());
    }

    public static BreakIterator getWordInstance(Locale locale) {
        return getBreakInstance(locale, 1);
    }

    public static BreakIterator getLineInstance() {
        return getLineInstance(Locale.getDefault());
    }

    public static BreakIterator getLineInstance(Locale locale) {
        return getBreakInstance(locale, 2);
    }

    public static BreakIterator getCharacterInstance() {
        return getCharacterInstance(Locale.getDefault());
    }

    public static BreakIterator getCharacterInstance(Locale locale) {
        return getBreakInstance(locale, 0);
    }

    public static BreakIterator getSentenceInstance() {
        return getSentenceInstance(Locale.getDefault());
    }

    public static BreakIterator getSentenceInstance(Locale locale) {
        return getBreakInstance(locale, 3);
    }

    private static BreakIterator getBreakInstance(Locale locale, int i2) {
        BreakIteratorCache breakIteratorCache;
        if (iterCache[i2] != null && (breakIteratorCache = iterCache[i2].get()) != null && breakIteratorCache.getLocale().equals(locale)) {
            return breakIteratorCache.createBreakInstance();
        }
        BreakIterator breakIteratorCreateBreakInstance = createBreakInstance(locale, i2);
        iterCache[i2] = new SoftReference<>(new BreakIteratorCache(locale, breakIteratorCreateBreakInstance));
        return breakIteratorCreateBreakInstance;
    }

    private static BreakIterator createBreakInstance(Locale locale, int i2) {
        BreakIterator breakIteratorCreateBreakInstance = createBreakInstance(LocaleProviderAdapter.getAdapter(BreakIteratorProvider.class, locale), locale, i2);
        if (breakIteratorCreateBreakInstance == null) {
            breakIteratorCreateBreakInstance = createBreakInstance(LocaleProviderAdapter.forJRE(), locale, i2);
        }
        return breakIteratorCreateBreakInstance;
    }

    private static BreakIterator createBreakInstance(LocaleProviderAdapter localeProviderAdapter, Locale locale, int i2) {
        BreakIteratorProvider breakIteratorProvider = localeProviderAdapter.getBreakIteratorProvider();
        BreakIterator sentenceInstance = null;
        switch (i2) {
            case 0:
                sentenceInstance = breakIteratorProvider.getCharacterInstance(locale);
                break;
            case 1:
                sentenceInstance = breakIteratorProvider.getWordInstance(locale);
                break;
            case 2:
                sentenceInstance = breakIteratorProvider.getLineInstance(locale);
                break;
            case 3:
                sentenceInstance = breakIteratorProvider.getSentenceInstance(locale);
                break;
        }
        return sentenceInstance;
    }

    public static synchronized Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(BreakIteratorProvider.class).getAvailableLocales();
    }

    /* loaded from: rt.jar:java/text/BreakIterator$BreakIteratorCache.class */
    private static final class BreakIteratorCache {
        private BreakIterator iter;
        private Locale locale;

        BreakIteratorCache(Locale locale, BreakIterator breakIterator) {
            this.locale = locale;
            this.iter = (BreakIterator) breakIterator.clone();
        }

        Locale getLocale() {
            return this.locale;
        }

        BreakIterator createBreakInstance() {
            return (BreakIterator) this.iter.clone();
        }
    }
}
