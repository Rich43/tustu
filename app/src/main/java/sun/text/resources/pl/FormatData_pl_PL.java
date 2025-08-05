package sun.text.resources.pl;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/pl/FormatData_pl_PL.class */
public class FormatData_pl_PL extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "#,##0.## ¤;-#,##0.## ¤", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM yyyy", "d MMMM yyyy", "yyyy-MM-dd", "dd.MM.yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
