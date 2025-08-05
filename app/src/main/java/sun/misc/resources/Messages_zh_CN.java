package sun.misc.resources;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:sun/misc/resources/Messages_zh_CN.class */
public final class Messages_zh_CN extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"optpkg.versionerror", "错误: {0} JAR 文件中使用的版本格式无效。请检查文档以了解支持的版本格式。"}, new Object[]{"optpkg.attributeerror", "错误: 必要的{0} JAR 清单属性未在{1} JAR 文件中设置。"}, new Object[]{"optpkg.attributeserror", "错误: 某些必要的 JAR 清单属性未在{0} JAR 文件中设置。"}};
    }
}
