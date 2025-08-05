package sun.text.resources.cldr.sq;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/sq/FormatData_sq.class */
public class FormatData_sq extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"janar", "shkurt", "mars", "prill", "maj", "qershor", "korrik", "gusht", "shtator", "tetor", "nëntor", "dhjetor", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Shk", "Mar", "Pri", "Maj", "Qer", "Kor", "Gsh", "Sht", "Tet", "Nën", "Dhj", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.S_TOKEN, PdfOps.M_TOKEN, Constants._TAG_P, PdfOps.M_TOKEN, "Q", PdfOps.K_TOKEN, "G", PdfOps.S_TOKEN, "T", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"e diel", "e hënë", "e martë", "e mërkurë", "e enjte", "e premte", "e shtunë"}}, new Object[]{"DayAbbreviations", new String[]{"Die", "Hën", "Mar", "Mër", "Enj", "Pre", "Sht"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, PdfOps.H_TOKEN, PdfOps.M_TOKEN, PdfOps.M_TOKEN, "E", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"AmPmMarkers", new String[]{"PD", "MD"}}, new Object[]{"Eras", new String[]{"p.e.r.", "n.e.r."}}, new Object[]{"TimePatterns", new String[]{"h.mm.ss.a zzzz", "h.mm.ss.a z", "h.mm.ss.a", "h.mm.a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, dd MMMM y", "dd MMMM y", "yyyy-MM-dd", "yy-MM-dd"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤#,##0.00", "#,##0%"}}};
    }
}
