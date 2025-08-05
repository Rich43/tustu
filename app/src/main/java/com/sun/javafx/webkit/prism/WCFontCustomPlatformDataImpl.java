package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PGFont;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCFontCustomPlatformDataImpl.class */
final class WCFontCustomPlatformDataImpl extends WCFontCustomPlatformData {
    private final PGFont font;

    WCFontCustomPlatformDataImpl(InputStream inputStream) throws IOException {
        FontFactory factory = GraphicsPipeline.getPipeline().getFontFactory();
        this.font = factory.loadEmbeddedFont((String) null, inputStream, 10.0f, false);
        if (this.font == null) {
            throw new IOException("Error loading font");
        }
    }

    @Override // com.sun.webkit.graphics.WCFontCustomPlatformData
    protected WCFont createFont(int size, boolean bold, boolean italic) {
        FontFactory factory = GraphicsPipeline.getPipeline().getFontFactory();
        return new WCFontImpl(factory.deriveFont(this.font, bold, italic, size));
    }
}
