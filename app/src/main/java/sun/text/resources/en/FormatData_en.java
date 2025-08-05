package sun.text.resources.en;

import org.icepdf.core.util.PdfOps;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: rt.jar:sun/text/resources/en/FormatData_en.class */
public class FormatData_en extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤#,##0.00;-¤#,##0.00", "#,##0%"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
