package sun.awt.image;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;

/* loaded from: rt.jar:sun/awt/image/ImageWatched.class */
public abstract class ImageWatched {
    public static Link endlink = new Link();
    public Link watcherList = endlink;

    protected abstract void notifyWatcherListEmpty();

    /* loaded from: rt.jar:sun/awt/image/ImageWatched$Link.class */
    public static class Link {
        public boolean isWatcher(ImageObserver imageObserver) {
            return false;
        }

        public Link removeWatcher(ImageObserver imageObserver) {
            return this;
        }

        public boolean newInfo(Image image, int i2, int i3, int i4, int i5, int i6) {
            return false;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/ImageWatched$AccWeakReference.class */
    static class AccWeakReference<T> extends WeakReference<T> {
        private final AccessControlContext acc;

        AccWeakReference(T t2) {
            super(t2);
            this.acc = AccessController.getContext();
        }
    }

    /* loaded from: rt.jar:sun/awt/image/ImageWatched$WeakLink.class */
    public static class WeakLink extends Link {
        private final AccWeakReference<ImageObserver> myref;
        private Link next;

        public WeakLink(ImageObserver imageObserver, Link link) {
            this.myref = new AccWeakReference<>(imageObserver);
            this.next = link;
        }

        @Override // sun.awt.image.ImageWatched.Link
        public boolean isWatcher(ImageObserver imageObserver) {
            return this.myref.get() == imageObserver || this.next.isWatcher(imageObserver);
        }

        @Override // sun.awt.image.ImageWatched.Link
        public Link removeWatcher(ImageObserver imageObserver) {
            ImageObserver imageObserver2 = this.myref.get();
            if (imageObserver2 == null) {
                return this.next.removeWatcher(imageObserver);
            }
            if (imageObserver2 == imageObserver) {
                return this.next;
            }
            this.next = this.next.removeWatcher(imageObserver);
            return this;
        }

        private static boolean update(ImageObserver imageObserver, AccessControlContext accessControlContext, Image image, int i2, int i3, int i4, int i5, int i6) {
            if (accessControlContext != null || System.getSecurityManager() != null) {
                return ((Boolean) AccessController.doPrivileged(() -> {
                    return Boolean.valueOf(imageObserver.imageUpdate(image, i2, i3, i4, i5, i6));
                }, accessControlContext)).booleanValue();
            }
            return false;
        }

        @Override // sun.awt.image.ImageWatched.Link
        public boolean newInfo(Image image, int i2, int i3, int i4, int i5, int i6) {
            boolean zNewInfo = this.next.newInfo(image, i2, i3, i4, i5, i6);
            ImageObserver imageObserver = this.myref.get();
            if (imageObserver == null) {
                zNewInfo = true;
            } else if (!update(imageObserver, ((AccWeakReference) this.myref).acc, image, i2, i3, i4, i5, i6)) {
                this.myref.clear();
                zNewInfo = true;
            }
            return zNewInfo;
        }
    }

    public synchronized void addWatcher(ImageObserver imageObserver) {
        if (imageObserver != null && !isWatcher(imageObserver)) {
            this.watcherList = new WeakLink(imageObserver, this.watcherList);
        }
        this.watcherList = this.watcherList.removeWatcher(null);
    }

    public synchronized boolean isWatcher(ImageObserver imageObserver) {
        return this.watcherList.isWatcher(imageObserver);
    }

    public void removeWatcher(ImageObserver imageObserver) {
        synchronized (this) {
            this.watcherList = this.watcherList.removeWatcher(imageObserver);
        }
        if (this.watcherList == endlink) {
            notifyWatcherListEmpty();
        }
    }

    public boolean isWatcherListEmpty() {
        synchronized (this) {
            this.watcherList = this.watcherList.removeWatcher(null);
        }
        return this.watcherList == endlink;
    }

    public void newInfo(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (this.watcherList.newInfo(image, i2, i3, i4, i5, i6)) {
            removeWatcher(null);
        }
    }
}
