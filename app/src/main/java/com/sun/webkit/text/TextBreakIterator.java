package com.sun.webkit.text;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/webkit/text/TextBreakIterator.class */
final class TextBreakIterator {
    static final int CHARACTER_ITERATOR = 0;
    static final int WORD_ITERATOR = 1;
    static final int LINE_ITERATOR = 2;
    static final int SENTENCE_ITERATOR = 3;
    static final int TEXT_BREAK_FIRST = 0;
    static final int TEXT_BREAK_LAST = 1;
    static final int TEXT_BREAK_NEXT = 2;
    static final int TEXT_BREAK_PREVIOUS = 3;
    static final int TEXT_BREAK_CURRENT = 4;
    static final int TEXT_BREAK_PRECEDING = 5;
    static final int TEXT_BREAK_FOLLOWING = 6;
    static final int IS_TEXT_BREAK = 7;
    static final int IS_WORD_TEXT_BREAK = 8;
    private static final Map<CacheKey, BreakIterator> iteratorCache = new HashMap();

    TextBreakIterator() {
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/text/TextBreakIterator$CacheKey.class */
    private static final class CacheKey {
        private final int type;
        private final Locale locale;
        private final int hashCode;

        CacheKey(int type, Locale locale) {
            this.type = type;
            this.locale = locale;
            this.hashCode = locale.hashCode() + type;
        }

        public boolean equals(Object o2) {
            if (!(o2 instanceof CacheKey)) {
                return false;
            }
            CacheKey that = (CacheKey) o2;
            return that.type == this.type && that.locale.equals(this.locale);
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    static BreakIterator getIterator(int type, String localeName, String text, boolean create) {
        String country;
        BreakIterator iterator;
        String[] parts = localeName.split(LanguageTag.SEP);
        switch (parts.length) {
            case 1:
                country = null;
                break;
            case 2:
                country = parts[1];
                break;
            default:
                country = parts[2];
                break;
        }
        String lang = parts[0].toLowerCase();
        Locale locale = country == null ? new Locale(lang) : new Locale(lang, country.toUpperCase());
        if (create) {
            iterator = createIterator(type, locale);
        } else {
            CacheKey key = new CacheKey(type, locale);
            iterator = iteratorCache.get(key);
            if (iterator == null) {
                iterator = createIterator(type, locale);
                iteratorCache.put(key, iterator);
            }
        }
        iterator.setText(text);
        return iterator;
    }

    private static BreakIterator createIterator(int type, Locale locale) {
        switch (type) {
            case 0:
                return BreakIterator.getCharacterInstance(locale);
            case 1:
                return BreakIterator.getWordInstance(locale);
            case 2:
                return BreakIterator.getLineInstance(locale);
            case 3:
                return BreakIterator.getSentenceInstance(locale);
            default:
                throw new IllegalArgumentException("invalid type: " + type);
        }
    }

    static int invokeMethod(BreakIterator iterator, int method, int pos) {
        CharacterIterator text = iterator.getText();
        int length = text.getEndIndex() - text.getBeginIndex();
        if (method == 5 && pos > length) {
            return length;
        }
        if (pos < 0 || pos > length) {
            pos = pos < 0 ? 0 : length;
        }
        switch (method) {
            case 0:
                return iterator.first();
            case 1:
                return iterator.last();
            case 2:
                return iterator.next();
            case 3:
                return iterator.previous();
            case 4:
                return iterator.current();
            case 5:
                return iterator.preceding(pos);
            case 6:
                return iterator.following(pos);
            case 7:
                return iterator.isBoundary(pos) ? 1 : 0;
            case 8:
                return 1;
            default:
                throw new IllegalArgumentException("invalid method: " + method);
        }
    }
}
