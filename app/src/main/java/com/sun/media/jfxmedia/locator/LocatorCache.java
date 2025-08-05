package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/LocatorCache.class */
public class LocatorCache {
    private final Map<URI, WeakReference<CacheReference>> uriCache;
    private final CacheDisposer cacheDisposer;

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/LocatorCache$CacheInitializer.class */
    private static class CacheInitializer {
        private static final LocatorCache globalInstance = new LocatorCache();

        private CacheInitializer() {
        }
    }

    public static LocatorCache locatorCache() {
        return CacheInitializer.globalInstance;
    }

    private LocatorCache() {
        this.uriCache = new HashMap();
        this.cacheDisposer = new CacheDisposer();
    }

    public CacheReference registerURICache(URI sourceURI, ByteBuffer data, String mimeType) {
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "New cache entry: URI " + ((Object) sourceURI) + ", buffer " + ((Object) data) + ", MIME type " + mimeType);
        }
        if (!data.isDirect()) {
            data.rewind();
            ByteBuffer newData = ByteBuffer.allocateDirect(data.capacity());
            newData.put(data);
            data = newData;
        }
        CacheReference ref = new CacheReference(data, mimeType);
        synchronized (this.uriCache) {
            this.uriCache.put(sourceURI, new WeakReference<>(ref));
        }
        MediaDisposer.addResourceDisposer(ref, sourceURI, this.cacheDisposer);
        return ref;
    }

    public CacheReference fetchURICache(URI sourceURI) {
        synchronized (this.uriCache) {
            WeakReference<CacheReference> ref = this.uriCache.get(sourceURI);
            if (null == ref) {
                return null;
            }
            CacheReference cacheData = ref.get();
            if (null != cacheData) {
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, "Fetched cache entry: URI " + ((Object) sourceURI) + ", buffer " + ((Object) cacheData.getBuffer()) + ", MIME type " + cacheData.getMIMEType());
                }
                return cacheData;
            }
            return null;
        }
    }

    public boolean isCached(URI sourceURI) {
        boolean zContainsKey;
        synchronized (this.uriCache) {
            zContainsKey = this.uriCache.containsKey(sourceURI);
        }
        return zContainsKey;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/LocatorCache$CacheReference.class */
    public static class CacheReference {
        private final ByteBuffer buffer;
        private String mimeType;

        public CacheReference(ByteBuffer buf, String mimeType) {
            this.buffer = buf;
            this.mimeType = mimeType;
        }

        public ByteBuffer getBuffer() {
            return this.buffer;
        }

        public String getMIMEType() {
            return this.mimeType;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/LocatorCache$CacheDisposer.class */
    private class CacheDisposer implements MediaDisposer.ResourceDisposer {
        private CacheDisposer() {
        }

        @Override // com.sun.media.jfxmediaimpl.MediaDisposer.ResourceDisposer
        public void disposeResource(Object resource) {
            if (resource instanceof URI) {
                synchronized (LocatorCache.this.uriCache) {
                    LocatorCache.this.uriCache.remove((URI) resource);
                }
            }
        }
    }
}
