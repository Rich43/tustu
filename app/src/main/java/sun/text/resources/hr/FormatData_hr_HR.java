package sun.text.resources.hr;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/hr/FormatData_hr_HR.class */
public class FormatData_hr_HR extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤ #,##0.##;-¤ #,##0.##", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy. MMMM dd", "yyyy. MMMM dd", "dd.MM.yyyy.", "dd.MM.yy."}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}};
    }
}
