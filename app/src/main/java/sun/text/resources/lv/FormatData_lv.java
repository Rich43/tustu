package sun.text.resources.lv;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/lv/FormatData_lv.class */
public class FormatData_lv extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"janvāris", "februāris", "marts", "aprīlis", "maijs", "jūnijs", "jūlijs", "augusts", "septembris", "oktobris", "novembris", "decembris", ""}}, new Object[]{"MonthAbbreviations", new String[]{"janv.", "febr.", "marts", "apr.", "maijs", "jūn.", "jūl.", "aug.", "sept.", "okt.", "nov.", "dec.", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"Jan", "Feb", "Mar", "Apr", "Maijs", "Jūn", "Jūl", "Aug", "Sep", "Okt", "Nov", "Dec", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "J", "J", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"svētdiena", "pirmdiena", "otrdiena", "trešdiena", "ceturtdiena", "piektdiena", "sestdiena"}}, new Object[]{"DayAbbreviations", new String[]{"Sv", Constants._TAG_P, "O", "T", "C", "Pk", PdfOps.S_TOKEN}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, Constants._TAG_P, "O", "T", "C", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"pmē", "mē"}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, yyyy, d MMMM", "EEEE, yyyy, d MMMM", "yyyy.d.M", "yy.d.M"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
