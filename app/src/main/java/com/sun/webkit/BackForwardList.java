package com.sun.webkit;

import com.sun.webkit.event.WCChangeEvent;
import com.sun.webkit.event.WCChangeListener;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.network.URLs;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/webkit/BackForwardList.class */
public final class BackForwardList {
    private final WebPage page;
    private final List<WCChangeListener> listenerList = new LinkedList();

    /* JADX INFO: Access modifiers changed from: private */
    public static native String bflItemGetURL(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String bflItemGetTitle(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native WCImage bflItemGetIcon(long j2);

    private static native long bflItemGetLastVisitedDate(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean bflItemIsTargetItem(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native Entry[] bflItemGetChildren(long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String bflItemGetTarget(long j2);

    private static native void bflClearBackForwardListForDRT(long j2);

    private static native int bflSize(long j2);

    private static native int bflGetMaximumSize(long j2);

    private static native void bflSetMaximumSize(long j2, int i2);

    private static native int bflGetCurrentIndex(long j2);

    private static native int bflIndexOf(long j2, long j3, boolean z2);

    private static native void bflSetEnabled(long j2, boolean z2);

    private static native boolean bflIsEnabled(long j2);

    private static native Object bflGet(long j2, int i2);

    private static native int bflSetCurrentIndex(long j2, int i2);

    private static native void bflSetHostObject(long j2, Object obj);

    /* loaded from: jfxrt.jar:com/sun/webkit/BackForwardList$Entry.class */
    public static final class Entry {
        private long pitem;
        private long ppage;
        private Entry[] children;
        private URL url;
        private String title;
        private Date lastVisitedDate;
        private WCImage icon;
        private String target;
        private boolean isTargetItem;
        private final List<WCChangeListener> listenerList = new LinkedList();

        private Entry(long pitem, long ppage) {
            this.pitem = 0L;
            this.ppage = 0L;
            this.pitem = pitem;
            this.ppage = ppage;
            getURL();
            getTitle();
            getLastVisitedDate();
            getIcon();
            getTarget();
            isTargetItem();
            getChildren();
        }

        private void notifyItemDestroyed() {
            this.pitem = 0L;
        }

        private void notifyItemChanged() {
            for (WCChangeListener l2 : this.listenerList) {
                l2.stateChanged(new WCChangeEvent(this));
            }
        }

        public URL getURL() {
            try {
                if (this.pitem == 0) {
                    return this.url;
                }
                URL urlNewURL = URLs.newURL(BackForwardList.bflItemGetURL(this.pitem));
                this.url = urlNewURL;
                return urlNewURL;
            } catch (MalformedURLException e2) {
                this.url = null;
                return null;
            }
        }

        public String getTitle() {
            if (this.pitem == 0) {
                return this.title;
            }
            String strBflItemGetTitle = BackForwardList.bflItemGetTitle(this.pitem);
            this.title = strBflItemGetTitle;
            return strBflItemGetTitle;
        }

        public WCImage getIcon() {
            if (this.pitem == 0) {
                return this.icon;
            }
            WCImage wCImageBflItemGetIcon = BackForwardList.bflItemGetIcon(this.pitem);
            this.icon = wCImageBflItemGetIcon;
            return wCImageBflItemGetIcon;
        }

        public String getTarget() {
            if (this.pitem == 0) {
                return this.target;
            }
            String strBflItemGetTarget = BackForwardList.bflItemGetTarget(this.pitem);
            this.target = strBflItemGetTarget;
            return strBflItemGetTarget;
        }

        public Date getLastVisitedDate() {
            if (this.lastVisitedDate == null) {
                return null;
            }
            return (Date) this.lastVisitedDate.clone();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateLastVisitedDate() {
            this.lastVisitedDate = new Date(System.currentTimeMillis());
            notifyItemChanged();
        }

        public boolean isTargetItem() {
            if (this.pitem == 0) {
                return this.isTargetItem;
            }
            boolean zBflItemIsTargetItem = BackForwardList.bflItemIsTargetItem(this.pitem);
            this.isTargetItem = zBflItemIsTargetItem;
            return zBflItemIsTargetItem;
        }

        public Entry[] getChildren() {
            if (this.pitem == 0) {
                return this.children;
            }
            Entry[] entryArrBflItemGetChildren = BackForwardList.bflItemGetChildren(this.pitem, this.ppage);
            this.children = entryArrBflItemGetChildren;
            return entryArrBflItemGetChildren;
        }

        public String toString() {
            return "url=" + ((Object) getURL()) + ",title=" + getTitle() + ",date=" + ((Object) getLastVisitedDate());
        }

        public void addChangeListener(WCChangeListener l2) {
            if (l2 == null) {
                return;
            }
            this.listenerList.add(l2);
        }

        public void removeChangeListener(WCChangeListener l2) {
            if (l2 == null) {
                return;
            }
            this.listenerList.remove(l2);
        }
    }

    BackForwardList(WebPage page) {
        this.page = page;
        page.addLoadListenerClient(new LoadListenerClient() { // from class: com.sun.webkit.BackForwardList.1
            @Override // com.sun.webkit.LoadListenerClient
            public void dispatchLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
                Entry entry;
                if (state == 14 && (entry = BackForwardList.this.getCurrentEntry()) != null) {
                    entry.updateLastVisitedDate();
                }
            }

            @Override // com.sun.webkit.LoadListenerClient
            public void dispatchResourceLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
            }
        });
    }

    public int size() {
        return bflSize(this.page.getPage());
    }

    public int getMaximumSize() {
        return bflGetMaximumSize(this.page.getPage());
    }

    public void setMaximumSize(int size) {
        bflSetMaximumSize(this.page.getPage(), size);
    }

    public int getCurrentIndex() {
        return bflGetCurrentIndex(this.page.getPage());
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void setEnabled(boolean flag) {
        bflSetEnabled(this.page.getPage(), flag);
    }

    public boolean isEnabled() {
        return bflIsEnabled(this.page.getPage());
    }

    public Entry get(int index) {
        Entry host = (Entry) bflGet(this.page.getPage(), index);
        return host;
    }

    public Entry getCurrentEntry() {
        return get(getCurrentIndex());
    }

    public void clearBackForwardListForDRT() {
        bflClearBackForwardListForDRT(this.page.getPage());
    }

    public int indexOf(Entry e2) {
        return bflIndexOf(this.page.getPage(), e2.pitem, false);
    }

    public boolean contains(Entry e2) {
        return indexOf(e2) >= 0;
    }

    public Entry[] toArray() {
        int size = size();
        Entry[] entries = new Entry[size];
        for (int i2 = 0; i2 < size; i2++) {
            entries[i2] = get(i2);
        }
        return entries;
    }

    public void setCurrentIndex(int index) {
        if (bflSetCurrentIndex(this.page.getPage(), index) < 0) {
            throw new IllegalArgumentException("invalid index: " + index);
        }
    }

    private boolean canGoBack(int index) {
        return index > 0;
    }

    public boolean canGoBack() {
        return canGoBack(getCurrentIndex());
    }

    public boolean goBack() {
        int index = getCurrentIndex();
        if (canGoBack(index)) {
            setCurrentIndex(index - 1);
            return true;
        }
        return false;
    }

    private boolean canGoForward(int index) {
        return index < size() - 1;
    }

    public boolean canGoForward() {
        return canGoForward(getCurrentIndex());
    }

    public boolean goForward() {
        int index = getCurrentIndex();
        if (canGoForward(index)) {
            setCurrentIndex(index + 1);
            return true;
        }
        return false;
    }

    public void addChangeListener(WCChangeListener l2) {
        if (l2 == null) {
            return;
        }
        if (this.listenerList.isEmpty()) {
            bflSetHostObject(this.page.getPage(), this);
        }
        this.listenerList.add(l2);
    }

    public void removeChangeListener(WCChangeListener l2) {
        if (l2 == null) {
            return;
        }
        this.listenerList.remove(l2);
        if (this.listenerList.isEmpty()) {
            bflSetHostObject(this.page.getPage(), null);
        }
    }

    public WCChangeListener[] getChangeListeners() {
        return (WCChangeListener[]) this.listenerList.toArray(new WCChangeListener[0]);
    }

    private void notifyChanged() {
        for (WCChangeListener l2 : this.listenerList) {
            l2.stateChanged(new WCChangeEvent(this));
        }
    }
}
