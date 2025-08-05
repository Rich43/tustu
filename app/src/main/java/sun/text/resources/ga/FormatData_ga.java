package sun.text.resources.ga;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ga/FormatData_ga.class */
public class FormatData_ga extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Eanáir", "Feabhra", "Márta", "Aibreán", "Bealtaine", "Meitheamh", "Iúil", "Lúnasa", "Meán Fómhair", "Deireadh Fómhair", "Samhain", "Nollaig", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Ean", "Feabh", "Márta", "Aib", "Beal", "Meith", "Iúil", "Lún", "MFómh", "DFómh", "Samh", "Noll", ""}}, new Object[]{"MonthNarrows", new String[]{"E", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.B_TOKEN, PdfOps.M_TOKEN, "I", "L", PdfOps.M_TOKEN, PdfOps.D_TOKEN, PdfOps.S_TOKEN, "N", ""}}, new Object[]{"DayNames", new String[]{"Dé Domhnaigh", "Dé Luain", "Dé Máirt", "Dé Céadaoin", "Déardaoin", "Dé hAoine", "Dé Sathairn"}}, new Object[]{"DayAbbreviations", new String[]{"Domh", "Luan", "Máirt", "Céad", "Déar", "Aoine", "Sath"}}, new Object[]{"AmPmMarkers", new String[]{"a.m.", "p.m."}}, new Object[]{"Eras", new String[]{"RC", "AD"}}, new Object[]{"short.Eras", new String[]{"RC", "AD"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "¤ #,##0.00", "#,##0%"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "RbMLkUnsSElFtTauKcZ"}};
    }
}
