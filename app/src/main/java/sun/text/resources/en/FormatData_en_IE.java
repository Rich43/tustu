package sun.text.resources.en;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: rt.jar:sun/text/resources/en/FormatData_en_IE.class */
public class FormatData_en_IE extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"TimePatterns", new String[]{"HH:mm:ss 'o''clock' z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"dd MMMM yyyy", "dd MMMM yyyy", "dd-MMM-yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
