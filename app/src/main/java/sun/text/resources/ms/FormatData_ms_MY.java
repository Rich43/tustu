package sun.text.resources.ms;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ms/FormatData_ms_MY.class */
public class FormatData_ms_MY extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a z", "h:mm:ss a z", "h:mm:ss a", "h:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE dd MMM yyyy", "dd MMMM yyyy", "dd MMMM yyyy", "dd/MM/yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
