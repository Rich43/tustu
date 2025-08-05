package java.awt;

import java.io.Serializable;
import sun.awt.image.MultiResolutionToolkitImage;

/* loaded from: rt.jar:java/awt/MediaTracker.class */
public class MediaTracker implements Serializable {
    Component target;
    MediaEntry head;
    private static final long serialVersionUID = -483174189758638095L;
    public static final int LOADING = 1;
    public static final int ABORTED = 2;
    public static final int ERRORED = 4;
    public static final int COMPLETE = 8;
    static final int DONE = 14;

    public MediaTracker(Component component) {
        this.target = component;
    }

    public void addImage(Image image, int i2) {
        addImage(image, i2, -1, -1);
    }

    public synchronized void addImage(Image image, int i2, int i3, int i4) {
        addImageImpl(image, i2, i3, i4);
        Image resolutionVariant = getResolutionVariant(image);
        if (resolutionVariant != null) {
            addImageImpl(resolutionVariant, i2, i3 == -1 ? -1 : 2 * i3, i4 == -1 ? -1 : 2 * i4);
        }
    }

    private void addImageImpl(Image image, int i2, int i3, int i4) {
        this.head = MediaEntry.insert(this.head, new ImageMediaEntry(this, image, i2, i3, i4));
    }

    public boolean checkAll() {
        return checkAll(false, true);
    }

    public boolean checkAll(boolean z2) {
        return checkAll(z2, true);
    }

    private synchronized boolean checkAll(boolean z2, boolean z3) {
        boolean z4 = true;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            if ((mediaEntry.getStatus(z2, z3) & 14) == 0) {
                z4 = false;
            }
        }
        return z4;
    }

    public synchronized boolean isErrorAny() {
        MediaEntry mediaEntry = this.head;
        while (true) {
            MediaEntry mediaEntry2 = mediaEntry;
            if (mediaEntry2 != null) {
                if ((mediaEntry2.getStatus(false, true) & 4) != 0) {
                    return true;
                }
                mediaEntry = mediaEntry2.next;
            } else {
                return false;
            }
        }
    }

    public synchronized Object[] getErrorsAny() {
        int i2 = 0;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            if ((mediaEntry.getStatus(false, true) & 4) != 0) {
                i2++;
            }
        }
        if (i2 == 0) {
            return null;
        }
        Object[] objArr = new Object[i2];
        int i3 = 0;
        for (MediaEntry mediaEntry2 = this.head; mediaEntry2 != null; mediaEntry2 = mediaEntry2.next) {
            if ((mediaEntry2.getStatus(false, false) & 4) != 0) {
                int i4 = i3;
                i3++;
                objArr[i4] = mediaEntry2.getMedia();
            }
        }
        return objArr;
    }

    public void waitForAll() throws InterruptedException {
        waitForAll(0L);
    }

    public synchronized boolean waitForAll(long j2) throws InterruptedException {
        long jCurrentTimeMillis;
        long jCurrentTimeMillis2 = System.currentTimeMillis() + j2;
        boolean z2 = true;
        while (true) {
            int iStatusAll = statusAll(z2, z2);
            if ((iStatusAll & 1) == 0) {
                return iStatusAll == 8;
            }
            z2 = false;
            if (j2 == 0) {
                jCurrentTimeMillis = 0;
            } else {
                jCurrentTimeMillis = jCurrentTimeMillis2 - System.currentTimeMillis();
                if (jCurrentTimeMillis <= 0) {
                    return false;
                }
            }
            wait(jCurrentTimeMillis);
        }
    }

    public int statusAll(boolean z2) {
        return statusAll(z2, true);
    }

    private synchronized int statusAll(boolean z2, boolean z3) {
        int status = 0;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            status |= mediaEntry.getStatus(z2, z3);
        }
        return status;
    }

    public boolean checkID(int i2) {
        return checkID(i2, false, true);
    }

    public boolean checkID(int i2, boolean z2) {
        return checkID(i2, z2, true);
    }

    private synchronized boolean checkID(int i2, boolean z2, boolean z3) {
        boolean z4 = true;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            if (mediaEntry.getID() == i2 && (mediaEntry.getStatus(z2, z3) & 14) == 0) {
                z4 = false;
            }
        }
        return z4;
    }

    public synchronized boolean isErrorID(int i2) {
        MediaEntry mediaEntry = this.head;
        while (true) {
            MediaEntry mediaEntry2 = mediaEntry;
            if (mediaEntry2 != null) {
                if (mediaEntry2.getID() == i2 && (mediaEntry2.getStatus(false, true) & 4) != 0) {
                    return true;
                }
                mediaEntry = mediaEntry2.next;
            } else {
                return false;
            }
        }
    }

    public synchronized Object[] getErrorsID(int i2) {
        int i3 = 0;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            if (mediaEntry.getID() == i2 && (mediaEntry.getStatus(false, true) & 4) != 0) {
                i3++;
            }
        }
        if (i3 == 0) {
            return null;
        }
        Object[] objArr = new Object[i3];
        int i4 = 0;
        for (MediaEntry mediaEntry2 = this.head; mediaEntry2 != null; mediaEntry2 = mediaEntry2.next) {
            if (mediaEntry2.getID() == i2 && (mediaEntry2.getStatus(false, false) & 4) != 0) {
                int i5 = i4;
                i4++;
                objArr[i5] = mediaEntry2.getMedia();
            }
        }
        return objArr;
    }

    public void waitForID(int i2) throws InterruptedException {
        waitForID(i2, 0L);
    }

    public synchronized boolean waitForID(int i2, long j2) throws InterruptedException {
        long jCurrentTimeMillis;
        long jCurrentTimeMillis2 = System.currentTimeMillis() + j2;
        boolean z2 = true;
        while (true) {
            int iStatusID = statusID(i2, z2, z2);
            if ((iStatusID & 1) == 0) {
                return iStatusID == 8;
            }
            z2 = false;
            if (j2 == 0) {
                jCurrentTimeMillis = 0;
            } else {
                jCurrentTimeMillis = jCurrentTimeMillis2 - System.currentTimeMillis();
                if (jCurrentTimeMillis <= 0) {
                    return false;
                }
            }
            wait(jCurrentTimeMillis);
        }
    }

    public int statusID(int i2, boolean z2) {
        return statusID(i2, z2, true);
    }

    private synchronized int statusID(int i2, boolean z2, boolean z3) {
        int status = 0;
        for (MediaEntry mediaEntry = this.head; mediaEntry != null; mediaEntry = mediaEntry.next) {
            if (mediaEntry.getID() == i2) {
                status |= mediaEntry.getStatus(z2, z3);
            }
        }
        return status;
    }

    public synchronized void removeImage(Image image) {
        removeImageImpl(image);
        Image resolutionVariant = getResolutionVariant(image);
        if (resolutionVariant != null) {
            removeImageImpl(resolutionVariant);
        }
        notifyAll();
    }

    private void removeImageImpl(Image image) {
        MediaEntry mediaEntry = this.head;
        MediaEntry mediaEntry2 = null;
        while (mediaEntry != null) {
            MediaEntry mediaEntry3 = mediaEntry.next;
            if (mediaEntry.getMedia() == image) {
                if (mediaEntry2 == null) {
                    this.head = mediaEntry3;
                } else {
                    mediaEntry2.next = mediaEntry3;
                }
                mediaEntry.cancel();
            } else {
                mediaEntry2 = mediaEntry;
            }
            mediaEntry = mediaEntry3;
        }
    }

    public synchronized void removeImage(Image image, int i2) {
        removeImageImpl(image, i2);
        Image resolutionVariant = getResolutionVariant(image);
        if (resolutionVariant != null) {
            removeImageImpl(resolutionVariant, i2);
        }
        notifyAll();
    }

    private void removeImageImpl(Image image, int i2) {
        MediaEntry mediaEntry = this.head;
        MediaEntry mediaEntry2 = null;
        while (mediaEntry != null) {
            MediaEntry mediaEntry3 = mediaEntry.next;
            if (mediaEntry.getID() == i2 && mediaEntry.getMedia() == image) {
                if (mediaEntry2 == null) {
                    this.head = mediaEntry3;
                } else {
                    mediaEntry2.next = mediaEntry3;
                }
                mediaEntry.cancel();
            } else {
                mediaEntry2 = mediaEntry;
            }
            mediaEntry = mediaEntry3;
        }
    }

    public synchronized void removeImage(Image image, int i2, int i3, int i4) {
        removeImageImpl(image, i2, i3, i4);
        Image resolutionVariant = getResolutionVariant(image);
        if (resolutionVariant != null) {
            removeImageImpl(resolutionVariant, i2, i3 == -1 ? -1 : 2 * i3, i4 == -1 ? -1 : 2 * i4);
        }
        notifyAll();
    }

    private void removeImageImpl(Image image, int i2, int i3, int i4) {
        MediaEntry mediaEntry = this.head;
        MediaEntry mediaEntry2 = null;
        while (mediaEntry != null) {
            MediaEntry mediaEntry3 = mediaEntry.next;
            if (mediaEntry.getID() == i2 && (mediaEntry instanceof ImageMediaEntry) && ((ImageMediaEntry) mediaEntry).matches(image, i3, i4)) {
                if (mediaEntry2 == null) {
                    this.head = mediaEntry3;
                } else {
                    mediaEntry2.next = mediaEntry3;
                }
                mediaEntry.cancel();
            } else {
                mediaEntry2 = mediaEntry;
            }
            mediaEntry = mediaEntry3;
        }
    }

    synchronized void setDone() {
        notifyAll();
    }

    private static Image getResolutionVariant(Image image) {
        if (image instanceof MultiResolutionToolkitImage) {
            return ((MultiResolutionToolkitImage) image).getResolutionVariant();
        }
        return null;
    }
}
