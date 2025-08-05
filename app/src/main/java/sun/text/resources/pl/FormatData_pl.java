package sun.text.resources.pl;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/pl/FormatData_pl.class */
public class FormatData_pl extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"stycznia", "lutego", "marca", "kwietnia", "maja", "czerwca", "lipca", "sierpnia", "września", "października", "listopada", "grudnia", ""}}, new Object[]{"standalone.MonthNames", new String[]{"styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień", ""}}, new Object[]{"MonthAbbreviations", new String[]{"sty", "lut", "mar", "kwi", "maj", "cze", "lip", "sie", "wrz", "paź", "lis", "gru", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"sty", "lut", "mar", "kwi", "maj", "cze", "lip", "sie", "wrz", "paź", "lis", "gru", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.s_TOKEN, PdfOps.l_TOKEN, PdfOps.m_TOKEN, PdfOps.k_TOKEN, PdfOps.m_TOKEN, PdfOps.c_TOKEN, PdfOps.l_TOKEN, PdfOps.s_TOKEN, PdfOps.w_TOKEN, "p", PdfOps.l_TOKEN, PdfOps.g_TOKEN, ""}}, new Object[]{"standalone.MonthNarrows", new String[]{PdfOps.s_TOKEN, PdfOps.l_TOKEN, PdfOps.m_TOKEN, PdfOps.k_TOKEN, PdfOps.m_TOKEN, PdfOps.c_TOKEN, PdfOps.l_TOKEN, PdfOps.s_TOKEN, PdfOps.w_TOKEN, "p", PdfOps.l_TOKEN, PdfOps.g_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"}}, new Object[]{"standalone.DayNames", new String[]{"niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"}}, new Object[]{"DayAbbreviations", new String[]{"N", "Pn", "Wt", "Śr", "Cz", "Pt", "So"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"niedz.", "pon.", "wt.", "śr.", "czw.", "pt.", "sob."}}, new Object[]{"DayNarrows", new String[]{"N", Constants._TAG_P, PdfOps.W_TOKEN, "Ś", "C", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{"N", Constants._TAG_P, PdfOps.W_TOKEN, "Ś", "C", Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"p.n.e.", "n.e."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, d MMMM yyyy", "d MMMM yyyy", "yyyy-MM-dd", "yy-MM-dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
