package sun.text.resources.cldr.dje;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/dje/FormatData_dje.class */
public class FormatData_dje extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Žanwiye", "Feewiriye", "Marsi", "Awiril", "Me", "Žuweŋ", "Žuyye", "Ut", "Sektanbur", "Oktoobur", "Noowanbur", "Deesanbur", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Žan", "Fee", "Mar", "Awi", "Me", "Žuw", "Žuy", "Ut", "Sek", "Okt", "Noo", "Dee", ""}}, new Object[]{"MonthNarrows", new String[]{"Ž", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "Ž", "Ž", "U", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Alhadi", "Atinni", "Atalaata", "Alarba", "Alhamisi", "Alzuma", "Asibti"}}, new Object[]{"DayAbbreviations", new String[]{"Alh", "Ati", "Ata", "Ala", "Alm", "Alz", "Asi"}}, new Object[]{"DayNarrows", new String[]{PdfOps.H_TOKEN, "T", "T", "L", PdfOps.M_TOKEN, Constants.HASIDCALL_INDEX_SIG, PdfOps.S_TOKEN}}, new Object[]{"QuarterNames", new String[]{"Arrubu 1", "Arrubu 2", "Arrubu 3", "Arrubu 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"A1", "A2", "A3", "A4"}}, new Object[]{"AmPmMarkers", new String[]{"Subbaahi", "Zaarikay b"}}, new Object[]{"long.Eras", new String[]{"Isaa jine", "Isaa zamanoo"}}, new Object[]{"Eras", new String[]{"IJ", "IZ"}}, new Object[]{"field.era", "Zaman"}, new Object[]{"field.year", "Jiiri"}, new Object[]{"field.month", "Handu"}, new Object[]{"field.week", "Hebu"}, new Object[]{"field.weekday", "Zaari"}, new Object[]{"field.dayperiod", "Subbaahi/Zaarikay banda"}, new Object[]{"field.hour", "Guuru"}, new Object[]{"field.minute", "Miniti"}, new Object[]{"field.second", "Miti"}, new Object[]{"field.zone", "Leerazuu"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM, y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
