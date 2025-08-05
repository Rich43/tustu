package sun.text.resources.fr;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/fr/FormatData_fr_CA.class */
public class FormatData_fr_CA extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "#,##0.00 ¤;(#,##0.00¤)", "#,##0 %"}}, new Object[]{"TimePatterns", new String[]{"H' h 'mm z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM yyyy", "d MMMM yyyy", "yyyy-MM-dd", "yy-MM-dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GaMjkHmsSEDFwWxhKzZ"}};
    }
}
