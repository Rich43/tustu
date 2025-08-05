package sun.text.resources.cldr.mfe;

import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/mfe/FormatData_mfe.class */
public class FormatData_mfe extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"zanvie", "fevriye", "mars", "avril", "me", "zin", "zilye", "out", "septam", "oktob", "novam", "desam", ""}}, new Object[]{"MonthAbbreviations", new String[]{"zan", "fev", "mar", "avr", "me", "zin", "zil", "out", "sep", "okt", "nov", "des", ""}}, new Object[]{"MonthNarrows", new String[]{"z", PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, "z", "z", "o", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"dimans", "lindi", "mardi", "merkredi", "zedi", "vandredi", "samdi"}}, new Object[]{"DayAbbreviations", new String[]{"dim", "lin", "mar", "mer", "ze", "van", "sam"}}, new Object[]{"DayNarrows", new String[]{PdfOps.d_TOKEN, PdfOps.l_TOKEN, PdfOps.m_TOKEN, PdfOps.m_TOKEN, "z", PdfOps.v_TOKEN, PdfOps.s_TOKEN}}, new Object[]{"QuarterNames", new String[]{"1e trimes", "2em trimes", "3em trimes", "4em trimes"}}, new Object[]{"QuarterAbbreviations", new String[]{"T1", "T2", "T3", "T4"}}, new Object[]{"long.Eras", new String[]{"avan Zezi-Krist", "apre Zezi-Krist"}}, new Object[]{"Eras", new String[]{"av. Z-K", "ap. Z-K"}}, new Object[]{"field.era", "Lepok"}, new Object[]{"field.year", "Lane"}, new Object[]{"field.month", "Mwa"}, new Object[]{"field.week", "Semenn"}, new Object[]{"field.weekday", "Zour lasemenn"}, new Object[]{"field.dayperiod", "Peryod dan lazourne"}, new Object[]{"field.hour", "Ler"}, new Object[]{"field.minute", "Minit"}, new Object[]{"field.second", "Segonn"}, new Object[]{"field.zone", "Peryod letan"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE d MMMM y", "d MMMM y", "d MMM, y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
