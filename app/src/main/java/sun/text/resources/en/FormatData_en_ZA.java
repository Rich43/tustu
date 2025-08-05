package sun.text.resources.en;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: rt.jar:sun/text/resources/en/FormatData_en_ZA.class */
public class FormatData_en_ZA extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤ #,##0.00;¤-#,##0.00", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a", "h:mm:ss a", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE dd MMMM yyyy", "dd MMMM yyyy", "dd MMM yyyy", "yyyy/MM/dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
