package sun.text.resources.sk;

import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/sk/FormatData_sk.class */
public class FormatData_sk extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"januára", "februára", "marca", "apríla", "mája", "júna", "júla", "augusta", "septembra", "októbra", "novembra", "decembra", ""}}, new Object[]{"standalone.MonthNames", new String[]{"január", "február", "marec", "apríl", "máj", "jún", "júl", "august", "september", "október", "november", "december", ""}}, new Object[]{"MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "máj", "jún", "júl", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"jan", "feb", "mar", "apr", "máj", "jún", "júl", "aug", "sep", "okt", "nov", "dec", ""}}, new Object[]{"MonthNarrows", new String[]{PdfOps.j_TOKEN, PdfOps.f_TOKEN, PdfOps.m_TOKEN, "a", PdfOps.m_TOKEN, PdfOps.j_TOKEN, PdfOps.j_TOKEN, "a", PdfOps.s_TOKEN, "o", PdfOps.n_TOKEN, PdfOps.d_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"Nedeľa", "Pondelok", "Utorok", "Streda", "Štvrtok", "Piatok", "Sobota"}}, new Object[]{"standalone.DayNames", new String[]{"nedeľa", "pondelok", "utorok", "streda", "štvrtok", "piatok", "sobota"}}, new Object[]{"DayAbbreviations", new String[]{"Ne", "Po", "Ut", "St", "Št", "Pi", "So"}}, new Object[]{"standalone.DayAbbreviations", new String[]{"ne", "po", "ut", "st", "št", Constants.ELEMNAME_PI_OLD_STRING, "so"}}, new Object[]{"DayNarrows", new String[]{"N", com.sun.org.apache.xml.internal.security.utils.Constants._TAG_P, "U", PdfOps.S_TOKEN, "Š", com.sun.org.apache.xml.internal.security.utils.Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"standalone.DayNarrows", new String[]{"N", com.sun.org.apache.xml.internal.security.utils.Constants._TAG_P, "U", PdfOps.S_TOKEN, "Š", com.sun.org.apache.xml.internal.security.utils.Constants._TAG_P, PdfOps.S_TOKEN}}, new Object[]{"Eras", new String[]{"pred n.l.", "n.l."}}, new Object[]{"NumberElements", new String[]{",", " ", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"EEEE, yyyy, MMMM d", "EEEE, yyyy, MMMM d", "d.M.yyyy", "d.M.yyyy"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GanjkHmsSEDFwWxhKzZ"}};
    }
}
