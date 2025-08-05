package sun.text.resources.cldr.lu;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/lu/FormatData_lu.class */
public class FormatData_lu extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Ciongo", "Lùishi", "Lusòlo", "Mùuyà", "Lumùngùlù", "Lufuimi", "Kabàlàshìpù", "Lùshìkà", "Lutongolo", "Lungùdi", "Kaswèkèsè", "Ciswà", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Cio", "Lui", "Lus", "Muu", "Lum", "Luf", "Kab", "Lush", "Lut", "Lun", "Kas", "Cis", ""}}, new Object[]{"MonthNarrows", new String[]{"C", "L", "L", PdfOps.M_TOKEN, "L", "L", PdfOps.K_TOKEN, "L", "L", "L", PdfOps.K_TOKEN, "C", ""}}, new Object[]{"DayNames", new String[]{"Lumingu", "Nkodya", "Ndàayà", "Ndangù", "Njòwa", "Ngòvya", "Lubingu"}}, new Object[]{"DayAbbreviations", new String[]{"Lum", "Nko", "Ndy", "Ndg", "Njw", "Ngv", "Lub"}}, new Object[]{"DayNarrows", new String[]{"L", "N", "N", "N", "N", "N", "L"}}, new Object[]{"QuarterNames", new String[]{"Mueji 1", "Mueji 2", "Mueji 3", "Mueji 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"M1", "M2", "M3", "M4"}}, new Object[]{"AmPmMarkers", new String[]{"Dinda", "Dilolo"}}, new Object[]{"long.Eras", new String[]{"Kumpala kwa Yezu Kli", "Kunyima kwa Yezu Kli"}}, new Object[]{"Eras", new String[]{"kmp. Y.K.", "kny. Y. K."}}, new Object[]{"field.era", "Tshipungu"}, new Object[]{"field.year", "Tshidimu"}, new Object[]{"field.month", "Ngondo"}, new Object[]{"field.week", "Lubingu"}, new Object[]{"field.weekday", "Dituku dia lubingu"}, new Object[]{"field.dayperiod", "Mutantshi wa diba"}, new Object[]{"field.hour", "Diba"}, new Object[]{"field.minute", "Kasunsu"}, new Object[]{"field.second", "Kasunsukusu"}, new Object[]{"field.zone", "Nzeepu"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
