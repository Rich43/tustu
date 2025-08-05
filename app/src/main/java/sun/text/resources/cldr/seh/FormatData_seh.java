package sun.text.resources.cldr.seh;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/seh/FormatData_seh.class */
public class FormatData_seh extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Janeiro", "Fevreiro", "Marco", "Abril", "Maio", "Junho", "Julho", "Augusto", "Setembro", "Otubro", "Novembro", "Decembro", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Aug", "Set", "Otu", "Nov", "Dec", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Dimingu", "Chiposi", "Chipiri", "Chitatu", "Chinai", "Chishanu", "Sabudu"}}, new Object[]{"DayAbbreviations", new String[]{"Dim", "Pos", "Pir", "Tat", "Nai", "Sha", "Sab"}}, new Object[]{"DayNarrows", new String[]{PdfOps.D_TOKEN, Constants._TAG_P, "C", "T", "N", PdfOps.S_TOKEN, PdfOps.S_TOKEN}}, new Object[]{"long.Eras", new String[]{"Antes de Cristo", "Anno Domini"}}, new Object[]{"Eras", new String[]{"AC", "AD"}}, new Object[]{"field.year", "Chaka"}, new Object[]{"field.month", "Mwezi"}, new Object[]{"field.weekday", "Ntsiku"}, new Object[]{"field.hour", "Hora"}, new Object[]{"field.minute", "Minuto"}, new Object[]{"field.second", "Segundo"}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss zzzz", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d 'de' MMMM 'de' y", "d 'de' MMMM 'de' y", "d 'de' MMM 'de' y", "d/M/yyyy"}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###", "#,##0.00¤", "#,##0%"}}};
    }
}
