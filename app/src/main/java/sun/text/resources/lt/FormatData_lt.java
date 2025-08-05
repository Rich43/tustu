package sun.text.resources.lt;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/lt/FormatData_lt.class */
public class FormatData_lt extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"sausio", "vasaris", "kovas", "balandis", "gegužė", "birželis", "liepa", "rugpjūtis", "rugsėjis", "spalis", "lapkritis", "gruodis", ""}}, new Object[]{"standalone.MonthNames", new String[]{"Sausio", "Vasario", "Kovo", "Balandžio", "Gegužės", "Birželio", "Liepos", "Rugpjūčio", "Rugsėjo", "Spalio", "Lapkričio", "Gruodžio", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Sau", "Vas", "Kov", "Bal", "Geg", "Bir", "Lie", "Rgp", "Rgs", "Spa", "Lap", "Grd", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"Saus.", "Vas.", "Kov.", "Bal.", "Geg.", "Bir.", "Liep.", "Rugp.", "Rugs.", "Spal.", "Lapkr.", "Gruod.", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.S_TOKEN, "V", PdfOps.K_TOKEN, PdfOps.B_TOKEN, "G", PdfOps.B_TOKEN, "L", "R", "R", PdfOps.S_TOKEN, "L", "G", ""}}, new Object[]{"standalone.MonthNarrows", new String[]{PdfOps.S_TOKEN, "V", PdfOps.K_TOKEN, PdfOps.B_TOKEN, "G", PdfOps.B_TOKEN, "L", "R", "R", PdfOps.S_TOKEN, "L", "G", ""}}, new Object[]{"DayNames", new String[]{"Sekmadienis", "Pirmadienis", "Antradienis", "Trečiadienis", "Ketvirtadienis", "Penktadienis", "Šeštadienis"}}, new Object[]{"standalone.DayNames", new String[]{"sekmadienis", "pirmadienis", "antradienis", "trečiadienis", "ketvirtadienis", "penktadienis", "šeštadienis"}}, new Object[]{"DayAbbreviations", new String[]{"Sk", "Pr", "An", PdfOps.Tr_TOKEN, "Kt", "Pn", "Št"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"Sk", "Pr", "An", PdfOps.Tr_TOKEN, "Kt", "Pn", "Št"}}, new Object[]{"DayNarrows", new String[]{PdfOps.S_TOKEN, Constants._TAG_P, "A", "T", PdfOps.K_TOKEN, Constants._TAG_P, "Š"}}, new Object[]{"standalone.DayNarrows", new String[]{PdfOps.S_TOKEN, Constants._TAG_P, "A", "T", PdfOps.K_TOKEN, Constants._TAG_P, "Š"}}, new Object[]{"Eras", new String[]{"pr.Kr.", "po.Kr."}}, new Object[]{"short.Eras", new String[]{"pr. Kr.", "po Kr."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH.mm.ss z", "HH.mm.ss z", "HH.mm.ss", "HH.mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, yyyy, MMMM d", "EEEE, yyyy, MMMM d", "yyyy-MM-dd", "yy.M.d"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
