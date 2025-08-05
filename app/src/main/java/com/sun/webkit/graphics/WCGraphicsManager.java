package com.sun.webkit.graphics;

import com.sun.webkit.SharedBuffer;
import com.sun.webkit.SimpleSharedBufferInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCGraphicsManager.class */
public abstract class WCGraphicsManager {
    private final AtomicInteger idCount = new AtomicInteger(0);
    private final HashMap<Integer, Ref> refMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger(WCGraphicsManager.class.getName());
    private static ResourceBundle imageProperties = null;
    private static WCGraphicsManager manager = null;

    public abstract float getDevicePixelScale();

    protected abstract WCImageDecoder getImageDecoder();

    public abstract WCGraphicsContext createGraphicsContext(Object obj);

    public abstract WCRenderQueue createRenderQueue(WCRectangle wCRectangle, boolean z2);

    protected abstract WCRenderQueue createBufferedContextRQ(WCImage wCImage);

    public abstract WCPageBackBuffer createPageBackBuffer();

    protected abstract WCFont getWCFont(String str, boolean z2, boolean z3, float f2);

    protected abstract WCFontCustomPlatformData createFontCustomPlatformData(InputStream inputStream) throws IOException;

    protected abstract WCPath createWCPath();

    protected abstract WCPath createWCPath(WCPath wCPath);

    protected abstract WCImage createWCImage(int i2, int i3);

    protected abstract WCImage createRTImage(int i2, int i3);

    public abstract WCImage getIconImage(String str);

    public abstract Object toPlatformImage(WCImage wCImage);

    protected abstract WCImageFrame createFrame(int i2, int i3, ByteBuffer byteBuffer);

    protected abstract WCTransform createTransform(double d2, double d3, double d4, double d5, double d6, double d7);

    protected abstract WCMediaPlayer createMediaPlayer();

    private static native void append(long j2, byte[] bArr, int i2);

    public static void setGraphicsManager(WCGraphicsManager manager2) {
        manager = manager2;
    }

    public static WCGraphicsManager getGraphicsManager() {
        return manager;
    }

    private WCFontCustomPlatformData fwkCreateFontCustomPlatformData(SharedBuffer sharedBuffer) {
        try {
            return createFontCustomPlatformData(new SimpleSharedBufferInputStream(sharedBuffer));
        } catch (IOException ex) {
            logger.log(Level.FINEST, "Error creating font custom platform data", (Throwable) ex);
            return null;
        }
    }

    public static String getResourceName(String key) {
        if (imageProperties == null) {
            imageProperties = ResourceBundle.getBundle("com.sun.webkit.graphics.Images");
        }
        try {
            return imageProperties.getString(key);
        } catch (MissingResourceException e2) {
            return key;
        }
    }

    private void fwkLoadFromResource(String key, long bufPtr) {
        InputStream in = getClass().getResourceAsStream(getResourceName(key));
        if (in == null) {
            return;
        }
        byte[] buf = new byte[1024];
        while (true) {
            try {
                int count = in.read(buf);
                if (count > -1) {
                    append(bufPtr, buf, count);
                } else {
                    in.close();
                    return;
                }
            } catch (IOException e2) {
                return;
            }
        }
    }

    protected String[] getSupportedMediaTypes() {
        return new String[0];
    }

    private WCMediaPlayer fwkCreateMediaPlayer(long nativePointer) {
        WCMediaPlayer mediaPlayer = createMediaPlayer();
        mediaPlayer.setNativePointer(nativePointer);
        return mediaPlayer;
    }

    int createID() {
        return this.idCount.incrementAndGet();
    }

    synchronized void ref(Ref ref) {
        this.refMap.put(Integer.valueOf(ref.getID()), ref);
    }

    synchronized Ref deref(Ref ref) {
        return this.refMap.remove(Integer.valueOf(ref.getID()));
    }

    synchronized Ref getRef(int id) {
        return this.refMap.get(Integer.valueOf(id));
    }
}
