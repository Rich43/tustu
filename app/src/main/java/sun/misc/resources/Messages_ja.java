package sun.misc.resources;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:sun/misc/resources/Messages_ja.class */
public final class Messages_ja extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"optpkg.versionerror", "エラー: JARファイル{0}で無効なバージョン形式が使用されています。サポートされるバージョン形式についてのドキュメントを参照してください。"}, new Object[]{"optpkg.attributeerror", "エラー: 必要なJARマニフェスト属性{0}がJARファイル{1}に設定されていません。"}, new Object[]{"optpkg.attributeserror", "エラー: 複数の必要なJARマニフェスト属性がJARファイル{0}に設定されていません。"}};
    }
}
