package com.sun.javafx.webkit.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import com.sun.webkit.graphics.WCMediaPlayer;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCTransform;
import com.sun.webkit.perf.WCFontPerfLogger;
import com.sun.webkit.perf.WCGraphicsPerfLogger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/PrismGraphicsManager.class */
public final class PrismGraphicsManager extends WCGraphicsManager {
    private static final float highestPixelScale;
    private static final BaseTransform pixelScaleTransform;

    static {
        float ps = 1.0f;
        for (Screen s2 : Screen.getScreens()) {
            ps = Math.max(s2.getRenderScale(), ps);
        }
        highestPixelScale = ps;
        pixelScaleTransform = BaseTransform.getScaleInstance(ps, ps);
    }

    static BaseTransform getPixelScaleTransform() {
        return pixelScaleTransform;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public float getDevicePixelScale() {
        return highestPixelScale;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCImageDecoder getImageDecoder() {
        return new WCImageDecoderImpl();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public WCRenderQueue createRenderQueue(WCRectangle clip, boolean opaque) {
        return new WCRenderQueueImpl(clip, opaque);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCRenderQueue createBufferedContextRQ(WCImage image) {
        WCGraphicsContext g2 = new WCBufferedContext((PrismImage) image);
        WCRenderQueue rq = new WCRenderQueueImpl(WCGraphicsPerfLogger.isEnabled() ? new WCGraphicsPerfLogger(g2) : g2);
        image.setRQ(rq);
        return rq;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCFont getWCFont(String name, boolean bold, boolean italic, float size) {
        WCFont f2 = WCFontImpl.getFont(name, bold, italic, size);
        return (!WCFontPerfLogger.isEnabled() || f2 == null) ? f2 : new WCFontPerfLogger(f2);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCFontCustomPlatformData createFontCustomPlatformData(InputStream inputStream) throws IOException {
        return new WCFontCustomPlatformDataImpl(inputStream);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public WCGraphicsContext createGraphicsContext(Object platG) {
        WCGraphicsContext g2 = new WCGraphicsPrismContext((Graphics) platG);
        return WCGraphicsPerfLogger.isEnabled() ? new WCGraphicsPerfLogger(g2) : g2;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public WCPageBackBuffer createPageBackBuffer() {
        return new WCPageBackBufferImpl(highestPixelScale);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCPath createWCPath() {
        return new WCPathImpl();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCPath createWCPath(WCPath path) {
        return new WCPathImpl((WCPathImpl) path);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCImage createWCImage(int w2, int h2) {
        return new WCImageImpl(w2, h2);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCImage createRTImage(int w2, int h2) {
        return new RTImage(w2, h2, highestPixelScale);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public WCImage getIconImage(String iconURL) {
        return null;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    public Object toPlatformImage(WCImage image) {
        return ((WCImageImpl) image).getImage();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCImageFrame createFrame(final int w2, final int h2, ByteBuffer bytes) {
        int[] data = new int[bytes.capacity() / 4];
        bytes.order(ByteOrder.nativeOrder());
        bytes.asIntBuffer().get(data);
        final WCImageImpl wimg = new WCImageImpl(data, w2, h2);
        return new WCImageFrame() { // from class: com.sun.javafx.webkit.prism.PrismGraphicsManager.1
            @Override // com.sun.webkit.graphics.WCImageFrame
            public WCImage getFrame() {
                return wimg;
            }

            @Override // com.sun.webkit.graphics.WCImageFrame
            public int[] getSize() {
                return new int[]{w2, h2};
            }
        };
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCTransform createTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        return new WCTransform(m00, m10, m01, m11, m02, m12);
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected String[] getSupportedMediaTypes() {
        String[] types = MediaManager.getSupportedContentTypes();
        int len = types.length;
        for (int i2 = 0; i2 < len; i2++) {
            if (MediaUtils.CONTENT_TYPE_FLV.compareToIgnoreCase(types[i2]) == 0) {
                System.arraycopy(types, i2 + 1, types, i2, len - (i2 + 1));
                len--;
            }
        }
        if (len < types.length) {
            String[] trimmedArray = new String[len];
            System.arraycopy(types, 0, trimmedArray, 0, len);
            types = trimmedArray;
        }
        return types;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsManager
    protected WCMediaPlayer createMediaPlayer() {
        return new WCMediaPlayerImpl();
    }
}
