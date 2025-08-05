package sun.text.resources.hu;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/hu/FormatData_hu.class */
public class FormatData_hu extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december", ""}}, new Object[]{"standalone.MonthNames", new String[]{"január", "február", "március", "április", "május", "június", "július", "augusztus", "szeptember", "október", "november", "december", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan.", "febr.", "márc.", "ápr.", "máj.", "jún.", "júl.", "aug.", "szept.", "okt.", "nov.", "dec.", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan.", "febr.", "márc.", "ápr.", "máj.", "jún.", "júl.", "aug.", "szept.", "okt.", "nov.", "dec.", ""}}, new Object[]{"MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "Á", PdfOps.M_TOKEN, "J", "J", "A", "Sz", "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{"J", PdfOps.F_TOKEN, PdfOps.M_TOKEN, "Á", PdfOps.M_TOKEN, "J", "J", "A", "Sz", "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"vasárnap", "hétfő", "kedd", "szerda", "csütörtök", "péntek", "szombat"}}, new Object[]{"standalone.DayNames", new String[]{"vasárnap", "hétfő", "kedd", "szerda", "csütörtök", "péntek", "szombat"}}, new Object[]{"DayAbbreviations", new String[]{"V", PdfOps.H_TOKEN, PdfOps.K_TOKEN, "Sze", "Cs", Constants._TAG_P, "Szo"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"V", PdfOps.H_TOKEN, PdfOps.K_TOKEN, "Sze", "Cs", Constants._TAG_P, "Szo"}}, new Object[]{"DayNarrows", new String[]{"V", PdfOps.H_TOKEN, PdfOps.K_TOKEN, "Sz", "Cs", Constants._TAG_P, "Sz"}}, new Object[]{"standalone.DayNarrows", new String[]{"V", PdfOps.H_TOKEN, PdfOps.K_TOKEN, "Sz", "Cs", Constants._TAG_P, "Sz"}}, new Object[]{"AmPmMarkers", new String[]{"DE", "DU"}}, new Object[]{"Eras", new String[]{"i.e.", "i.u."}}, new Object[]{"short.Eras", new String[]{"i. e.", "i. sz."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy. MMMM d.", "yyyy. MMMM d.", "yyyy.MM.dd.", "yyyy.MM.dd."}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}, new Object[]{"buddhist.Eras", new String[]{"BC", "BK"}}, new Object[]{"buddhist.short.Eras", new String[]{"BC", "BK"}}};
    }
}
