package sun.text.resources.zh;

import sun.util.resources.ParallelListResourceBundle;

/* loaded from: localedata.jar:sun/text/resources/zh/FormatData_zh_TW.class */
public class FormatData_zh_TW extends ParallelListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // sun.util.resources.ParallelListResourceBundle
    protected final Object[][] getContents() {
        String[] strArr = {"民國前", "民國"};
        return new Object[]{new Object[]{"Eras", new String[]{"西元前", "西元"}}, new Object[]{"standalone.MonthAbbreviations", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""}}, new Object[]{"MonthNarrows", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""}}, new Object[]{"NumberPatterns", new String[]{"#,##0.###;-#,##0.###", "¤#,##0.00;-¤#,##0.00", "#,##0%"}}, new Object[]{"TimePatterns", new String[]{"ahh'時'mm'分'ss'秒' z", "ahh'時'mm'分'ss'秒'", "a hh:mm:ss", "a h:mm"}}, new Object[]{"DatePatterns", new String[]{"yyyy'年'M'月'd'日' EEEE", "yyyy'年'M'月'd'日'", "yyyy/M/d", "yyyy/M/d"}}, new Object[]{"DateTimePatterns", new String[]{"{1} {0}"}}, new Object[]{"buddhist.DatePatterns", new String[]{"GGGGy年M月d日EEEE", "GGGGy年M月d日", "GGGGy/M/d", "GGGGy/M/d"}}, new Object[]{"japanese.DatePatterns", new String[]{"GGGGy年M月d日EEEE", "GGGGy年M月d日", "GGGGy/M/d", "GGGGy/M/d"}}, new Object[]{"DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ"}};
    }
}
