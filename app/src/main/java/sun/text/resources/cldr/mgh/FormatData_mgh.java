package sun.text.resources.cldr.mgh;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/mgh/FormatData_mgh.class */
public class FormatData_mgh extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Mweri wo kwanza", "Mweri wo unayeli", "Mweri wo uneraru", "Mweri wo unecheshe", "Mweri wo unethanu", "Mweri wo thanu na mocha", "Mweri wo saba", "Mweri wo nane", "Mweri wo tisa", "Mweri wo kumi", "Mweri wo kumi na moja", "Mweri wo kumi na yel'li", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Kwa", "Una", "Rar", "Che", "Tha", "Moc", "Sab", "Nan", "Tis", "Kum", "Moj", "Yel", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.K_TOKEN, "U", "R", "C", "T", PdfOps.M_TOKEN, PdfOps.S_TOKEN, "N", "T", PdfOps.K_TOKEN, PdfOps.M_TOKEN, Constants._TAG_Y, ""}}, new Object[]{"DayNames", new String[]{"Sabato", "Jumatatu", "Jumanne", "Jumatano", "Arahamisi", "Ijumaa", "Jumamosi"}}, new Object[]{"DayAbbreviations", new String[]{"Sab", "Jtt", "Jnn", "Jtn", "Ara", "Iju", "Jmo"}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, "J", "J", "J", "A", "I", "J"}}, new Object[]{"long.Eras", new String[]{"Hinapiya yesu", "Yopia yesu"}}, new Object[]{"Eras", new String[]{"HY", "YY"}}, new Object[]{"field.era", "kal'lai"}, new Object[]{"field.year", "yaka"}, new Object[]{"field.month", "mweri"}, new Object[]{"field.week", "iwiki mocha"}, new Object[]{"field.weekday", "nihuku no mwisho wa wiki"}, new Object[]{"field.dayperiod", "nihuku"}, new Object[]{"field.hour", "isaa"}, new Object[]{"field.minute", "idakika"}, new Object[]{"field.second", "isekunde"}, new Object[]{"TimePatterns", new String[]{"h:mm:ss a zzzz", "h:mm:ss a z", "h:mm:ss a", "h:mm a"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM y", "d MMMM y", "d MMM y", "dd/MM/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
