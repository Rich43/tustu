package sun.text.resources.cldr.uz;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: cldrdata.jar:sun/text/resources/cldr/uz/FormatData_uz_Latn.class */
public class FormatData_uz_Latn extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected final Object[][] getContents() {
        return new Object[]{new Object[]{"MonthNames", new String[]{"Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun", "Iyul", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr", ""}}, new Object[]{"MonthAbbreviations", new String[]{"Yanv", "Fev", "Mar", "Apr", "May", "Iyun", "Iyul", "Avg", "Sen", "Okt", "Noya", "Dek", ""}}, new Object[]{"MonthNarrows", new String[]{Constants._TAG_Y, PdfOps.F_TOKEN, PdfOps.M_TOKEN, "A", PdfOps.M_TOKEN, "I", "I", "A", PdfOps.S_TOKEN, "O", "N", PdfOps.D_TOKEN, ""}}, new Object[]{"DayNames", new String[]{"yakshanba", "dushanba", "seshanba", "chorshanba", "payshanba", "cuma", "shanba"}}, new Object[]{"DayAbbreviations", new String[]{"Yaksh", "Dush", "Sesh", "Chor", "Pay", "Cum", "Shan"}}, new Object[]{"DayNarrows", new String[]{Constants._TAG_Y, PdfOps.D_TOKEN, PdfOps.S_TOKEN, "C", Constants._TAG_P, "C", PdfOps.S_TOKEN}}, new Object[]{"DefaultNumberingSystem", "latn"}, new Object[]{"latn.NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "NaN"}}};
    }
}
