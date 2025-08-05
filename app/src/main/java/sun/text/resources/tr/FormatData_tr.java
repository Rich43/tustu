package sun.text.resources.tr;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/tr/FormatData_tr.class */
public class FormatData_tr extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık", ""}}, new Object[]{"standalone.MonthNames", new String[]{"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara", ""}}, new Object[]{"MonthNarrows", new String[]{"O", "Ş", PdfOps.M_TOKEN, "N", PdfOps.M_TOKEN, PdfOps.H_TOKEN, "T", "A", "E", "E", PdfOps.K_TOKEN, "A", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"O", "Ş", PdfOps.M_TOKEN, "N", PdfOps.M_TOKEN, PdfOps.H_TOKEN, "T", "A", "E", "E", PdfOps.K_TOKEN, "A", ""}}, new Object[]{"DayNames", new String[]{"Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi"}}, new Object[]{"standalone.DayNames", new String[]{"Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi"}}, new Object[]{"DayAbbreviations", new String[]{"Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt"}}, new Object[]{"DayNarrows", new String[]{Constants._TAG_P, Constants._TAG_P, PdfOps.S_TOKEN, "Ç", Constants._TAG_P, "C", "C"}}, new Object[]{"standalone.DayNarrows", new String[]{Constants._TAG_P, Constants._TAG_P, PdfOps.S_TOKEN, "Ç", Constants._TAG_P, "C", "C"}}, new Object[]{"long.Eras", new String[]{"Milattan Önce", "Milattan Sonra"}}, new Object[]{"Eras", new String[]{"MÖ", "MS"}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "#,##0.00 ¤;-#,##0.00 ¤", "% #,##0"}}, new Object[]{"NumberElements", new String[]{",", ".", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"dd MMMM yyyy EEEE", "dd MMMM yyyy EEEE", "dd.MMM.yyyy", "dd.MM.yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
