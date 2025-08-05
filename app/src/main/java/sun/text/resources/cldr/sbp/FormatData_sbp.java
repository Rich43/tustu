package sun.text.resources.cldr.sbp;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/sbp/FormatData_sbp.class */
public class FormatData_sbp extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Mupalangulwa", "Mwitope", "Mushende", "Munyi", "Mushende Magali", "Mujimbi", "Mushipepo", "Mupuguto", "Munyense", "Mokhu", "Musongandembwe", "Muhaano", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Mup", "Mwi", "Msh", "Mun", "Mag", "Muj", "Msp", "Mpg", "Mye", "Mok", "Mus", "Muh", ""}}, new Object[]{"DayNames", new String[]{"Mulungu", "Jumatatu", "Jumanne", "Jumatano", "Alahamisi", "Ijumaa", "Jumamosi"}}, new Object[]{"DayAbbreviations", new String[]{"Mul", "Jtt", "Jnn", "Jtn", "Alh", "Iju", "Jmo"}}, new Object[]{"DayNarrows", new String[]{PdfOps.M_TOKEN, "J", "J", "J", "A", "I", "J"}}, new Object[]{"QuarterNames", new String[]{"Lobo 1", "Lobo 2", "Lobo 3", "Lobo 4"}}, new Object[]{"QuarterAbbreviations", new String[]{"L1", "L2", "L3", "L4"}}, new Object[]{"AmPmMarkers", new String[]{"Lwamilawu", "Pashamihe"}}, new Object[]{"long.Eras", new String[]{"Ashanali uKilisito", "Pamwandi ya Kilisto"}}, new Object[]{"Eras", new String[]{"AK", "PK"}}, new Object[]{"field.era", "Uluhaavi lwa"}, new Object[]{"field.year", "Mwakha"}, new Object[]{"field.month", "Mwesi"}, new Object[]{"field.week", "Ilijuma"}, new Object[]{"field.weekday", "Ulusiku lwa Lijuma"}, new Object[]{"field.dayperiod", "Uluhaavi lwa lusiku"}, new Object[]{"field.hour", "Ilisala"}, new Object[]{"field.minute", "Idakika"}, new Object[]{"field.second", "Isekunde"}, new Object[]{"field.zone", "Uluhaavi lwa lisaa"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
