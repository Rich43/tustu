package sun.text.resources.ja;

import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;
import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/ja/FormatData_ja.class */
public class FormatData_ja extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        String[] strArr = {"民国前", "民国"};
        return new Object[]{new Object[]{"MonthNames", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""}}, new Object[]{"MonthAbbreviations", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""}}, new Object[]{"DayNames", new String[]{"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"}}, new Object[]{"DayAbbreviations", new String[]{"日", "月", "火", "水", "木", "金", "土"}}, new Object[]{"DayNarrows", new String[]{"日", "月", "火", "水", "木", "金", "土"}}, new Object[]{"AmPmMarkers", new String[]{"午前", "午後"}}, new Object[]{"Eras", new String[]{"紀元前", "西暦"}}, new Object[]{"buddhist.Eras", new String[]{"紀元前", "仏暦"}}, new Object[]{"japanese.Eras", new String[]{"西暦", "明治", "大正", "昭和", "平成", "令和"}}, new Object[]{"japanese.FirstYear", new String[]{"元"}}, new Object[]{"NumberElements", new String[]{".", ",", ";", FXMLLoader.RESOURCE_KEY_PREFIX, "0", FXMLLoader.CONTROLLER_METHOD_PREFIX, LanguageTag.SEP, "E", "‰", "∞", "�"}}, new Object[]{"TimePatterns", new String[]{"H'時'mm'分'ss'秒' z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy'年'M'月'd'日'", "yyyy/MM/dd", "yyyy/MM/dd", "yy/MM/dd"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"japanese.DatePatterns", new String[]{"GGGGyyyy'年'M'月'd'日'", "Gy.MM.dd", "Gy.MM.dd", "Gy.MM.dd"}}, new Object[]{"japanese.TimePatterns", new String[]{"H'時'mm'分'ss'秒' z", "H:mm:ss z", "H:mm:ss", "H:mm"}}, new Object[]{"japanese.DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
