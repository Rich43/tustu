package sun.text.resources.es;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/es/FormatData_es_VE.class */
public class FormatData_es_VE extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤#,##0.00;¤ -#,##0.00", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"hh:mm:ss a z", "hh:mm:ss a z", "hh:mm:ss a", "hh:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE d' de 'MMMM' de 'yyyy", "d' de 'MMMM' de 'yyyy", "dd/MM/yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
