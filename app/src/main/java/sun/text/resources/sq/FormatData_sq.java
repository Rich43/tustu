package sun.text.resources.sq;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sq/FormatData_sq.class */
public class FormatData_sq extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"janar", "shkurt", "mars", "prill", "maj", "qershor", "korrik", "gusht", "shtator", "tetor", "nëntor", "dhjetor", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Shk", "Mar", "Pri", "Maj", "Qer", "Kor", "Gsh", "Sht", "Tet", "Nën", "Dhj", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.S_TOKEN, PdfOps.M_TOKEN, Constants._TAG_P, PdfOps.M_TOKEN, "Q", PdfOps.K_TOKEN, "G", PdfOps.S_TOKEN, "T", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"e diel", "e hënë", "e martë", "e mërkurë", "e enjte", "e premte", "e shtunë"}}, new Object[]{"DayAbbreviations", new String[]{"Die", "Hën", "Mar", "Mër", "Enj", "Pre", "Sht"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, PdfOps.H_TOKEN, PdfOps.M_TOKEN, PdfOps.M_TOKEN, "E", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"AmPmMarkers", new String[]{"PD", "MD"}}, new Object[]{"Eras", new String[]{"p.e.r.", "n.e.r."}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"h.mm.ss.a z", "h.mm.ss.a z", "h:mm:ss.a", "h.mm.a"}}, new Object[]{"DatePatterns", new String[]{"yyyy-MM-dd", "yyyy-MM-dd", "yyyy-MM-dd", "yy-MM-dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
