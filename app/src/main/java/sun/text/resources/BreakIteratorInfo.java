package sun.text.resources;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:sun/text/resources/BreakIteratorInfo.class */
public class BreakIteratorInfo extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"BreakIteratorClasses", new String[]{"RuleBasedBreakIterator", "RuleBasedBreakIterator", "RuleBasedBreakIterator", "RuleBasedBreakIterator"}}, new Object[]{"CharacterData", "CharacterBreakIteratorData"}, new Object[]{"WordData", "WordBreakIteratorData"}, new Object[]{"LineData", "LineBreakIteratorData"}, new Object[]{"SentenceData", "SentenceBreakIteratorData"}};
    }
}
