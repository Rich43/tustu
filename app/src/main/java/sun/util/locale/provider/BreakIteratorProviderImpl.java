package sun.util.locale.provider;

import java.io.IOException;
import java.text.BreakIterator;
import java.text.spi.BreakIteratorProvider;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/BreakIteratorProviderImpl.class */
public class BreakIteratorProviderImpl extends BreakIteratorProvider implements AvailableLanguageTags {
    private static final int CHARACTER_INDEX = 0;
    private static final int WORD_INDEX = 1;
    private static final int LINE_INDEX = 2;
    private static final int SENTENCE_INDEX = 3;
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public BreakIteratorProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // java.text.spi.BreakIteratorProvider
    public BreakIterator getWordInstance(Locale locale) {
        return getBreakInstance(locale, 1, "WordData", "WordDictionary");
    }

    @Override // java.text.spi.BreakIteratorProvider
    public BreakIterator getLineInstance(Locale locale) {
        return getBreakInstance(locale, 2, "LineData", "LineDictionary");
    }

    @Override // java.text.spi.BreakIteratorProvider
    public BreakIterator getCharacterInstance(Locale locale) {
        return getBreakInstance(locale, 0, "CharacterData", "CharacterDictionary");
    }

    @Override // java.text.spi.BreakIteratorProvider
    public BreakIterator getSentenceInstance(Locale locale) {
        return getBreakInstance(locale, 3, "SentenceData", "SentenceDictionary");
    }

    private BreakIterator getBreakInstance(Locale locale, int i2, String str, String str2) {
        if (locale == null) {
            throw new NullPointerException();
        }
        LocaleResources localeResources = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
        String[] strArr = (String[]) localeResources.getBreakIteratorInfo("BreakIteratorClasses");
        String str3 = (String) localeResources.getBreakIteratorInfo(str);
        try {
            switch (strArr[i2]) {
                case "RuleBasedBreakIterator":
                    return new RuleBasedBreakIterator(str3);
                case "DictionaryBasedBreakIterator":
                    return new DictionaryBasedBreakIterator(str3, (String) localeResources.getBreakIteratorInfo(str2));
                default:
                    throw new IllegalArgumentException("Invalid break iterator class \"" + strArr[i2] + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        } catch (IOException | IllegalArgumentException | MissingResourceException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public boolean isSupportedLocale(Locale locale) {
        return LocaleProviderAdapter.isSupportedLocale(locale, this.type, this.langtags);
    }
}
