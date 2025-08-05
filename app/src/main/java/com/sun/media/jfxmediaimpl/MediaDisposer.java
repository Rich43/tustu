package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.logging.Logger;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaDisposer.class */
public class MediaDisposer {
    private final ReferenceQueue<Object> purgatory = new ReferenceQueue<>();
    private final Map<Reference, Disposable> disposers = new HashMap();
    private static MediaDisposer theDisposinator;

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaDisposer$Disposable.class */
    public interface Disposable {
        void dispose();
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaDisposer$ResourceDisposer.class */
    public interface ResourceDisposer {
        void disposeResource(Object obj);
    }

    public static void addResourceDisposer(Object referent, Object resource, ResourceDisposer disposer) {
        disposinator().implAddResourceDisposer(referent, resource, disposer);
    }

    public static void removeResourceDisposer(Object resource) {
        disposinator().implRemoveResourceDisposer(resource);
    }

    public static void addDisposable(Object referent, Disposable disposable) {
        disposinator().implAddDisposable(referent, disposable);
    }

    private static synchronized MediaDisposer disposinator() {
        if (null == theDisposinator) {
            theDisposinator = new MediaDisposer();
            Thread disposerThread = new Thread(() -> {
                theDisposinator.disposerLoop();
            }, "Media Resource Disposer");
            disposerThread.setDaemon(true);
            disposerThread.start();
        }
        return theDisposinator;
    }

    private MediaDisposer() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disposerLoop() {
        Disposable disposer;
        while (true) {
            try {
                Reference denizen = this.purgatory.remove();
                synchronized (this.disposers) {
                    disposer = this.disposers.remove(denizen);
                }
                denizen.clear();
                if (null != disposer) {
                    disposer.dispose();
                }
            } catch (InterruptedException e2) {
                if (Logger.canLog(1)) {
                    Logger.logMsg(1, MediaDisposer.class.getName(), "disposerLoop", "Disposer loop interrupted, terminating");
                }
            }
        }
    }

    private void implAddResourceDisposer(Object referent, Object resource, ResourceDisposer disposer) {
        Reference denizen = new PhantomReference(referent, this.purgatory);
        synchronized (this.disposers) {
            this.disposers.put(denizen, new ResourceDisposerRecord(resource, disposer));
        }
    }

    private void implRemoveResourceDisposer(Object resource) {
        Reference resourceKey = null;
        synchronized (this.disposers) {
            Iterator<Map.Entry<Reference, Disposable>> it = this.disposers.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Reference, Disposable> entry = it.next();
                Disposable disposer = entry.getValue();
                if (disposer instanceof ResourceDisposerRecord) {
                    ResourceDisposerRecord rd = (ResourceDisposerRecord) disposer;
                    if (rd.resource.equals(resource)) {
                        resourceKey = entry.getKey();
                        break;
                    }
                }
            }
            if (null != resourceKey) {
                this.disposers.remove(resourceKey);
            }
        }
    }

    private void implAddDisposable(Object referent, Disposable disposer) {
        Reference denizen = new PhantomReference(referent, this.purgatory);
        synchronized (this.disposers) {
            this.disposers.put(denizen, disposer);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaDisposer$ResourceDisposerRecord.class */
    private static class ResourceDisposerRecord implements Disposable {
        Object resource;
        ResourceDisposer disposer;

        public ResourceDisposerRecord(Object resource, ResourceDisposer disposer) {
            this.resource = resource;
            this.disposer = disposer;
        }

        @Override // com.sun.media.jfxmediaimpl.MediaDisposer.Disposable
        public void dispose() {
            this.disposer.disposeResource(this.resource);
        }
    }
}
