package sun.text.resources.pt;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/pt/FormatData_pt_BR.class */
public class FormatData_pt_BR extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤ #,##0.00;-¤ #,##0.00", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"HH'h'mm'min'ss's' z", "H'h'm'min's's' z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d' de 'MMMM' de 'yyyy", "d' de 'MMMM' de 'yyyy", "dd/MM/yyyy", "dd/MM/yy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
