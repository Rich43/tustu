package sun.awt.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.awt.AppContext;
import sun.java2d.marlin.MarlinConst;

/* loaded from: rt.jar:sun/awt/image/ImageFetcher.class */
class ImageFetcher extends Thread {
    static final int HIGH_PRIORITY = 8;
    static final int LOW_PRIORITY = 3;
    static final int ANIM_PRIORITY = 2;
    static final int TIMEOUT = 5000;

    private ImageFetcher(ThreadGroup threadGroup, int i2) {
        super(threadGroup, "Image Fetcher " + i2);
        setDaemon(true);
    }

    public static boolean add(ImageFetchable imageFetchable) {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        synchronized (fetcherInfo.waitList) {
            if (!fetcherInfo.waitList.contains(imageFetchable)) {
                fetcherInfo.waitList.addElement(imageFetchable);
                if (fetcherInfo.numWaiting == 0 && fetcherInfo.numFetchers < fetcherInfo.fetchers.length) {
                    createFetchers(fetcherInfo);
                }
                if (fetcherInfo.numFetchers > 0) {
                    fetcherInfo.waitList.notify();
                } else {
                    fetcherInfo.waitList.removeElement(imageFetchable);
                    return false;
                }
            }
            return true;
        }
    }

    public static void remove(ImageFetchable imageFetchable) {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        synchronized (fetcherInfo.waitList) {
            if (fetcherInfo.waitList.contains(imageFetchable)) {
                fetcherInfo.waitList.removeElement(imageFetchable);
            }
        }
    }

    public static boolean isFetcher(Thread thread) {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        synchronized (fetcherInfo.waitList) {
            for (int i2 = 0; i2 < fetcherInfo.fetchers.length; i2++) {
                if (fetcherInfo.fetchers[i2] == thread) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean amFetcher() {
        return isFetcher(Thread.currentThread());
    }

    private static ImageFetchable nextImage() {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        synchronized (fetcherInfo.waitList) {
            ImageFetchable imageFetchable = null;
            long jCurrentTimeMillis = System.currentTimeMillis() + MarlinConst.statDump;
            while (imageFetchable == null) {
                while (fetcherInfo.waitList.size() == 0) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    if (jCurrentTimeMillis2 >= jCurrentTimeMillis) {
                        return null;
                    }
                    try {
                        fetcherInfo.numWaiting++;
                        fetcherInfo.waitList.wait(jCurrentTimeMillis - jCurrentTimeMillis2);
                        fetcherInfo.numWaiting--;
                    } catch (InterruptedException e2) {
                        fetcherInfo.numWaiting--;
                        return null;
                    } catch (Throwable th) {
                        fetcherInfo.numWaiting--;
                        throw th;
                    }
                }
                imageFetchable = (ImageFetchable) fetcherInfo.waitList.elementAt(0);
                fetcherInfo.waitList.removeElement(imageFetchable);
            }
            return imageFetchable;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        try {
            try {
                fetchloop();
                synchronized (fetcherInfo.waitList) {
                    Thread threadCurrentThread = Thread.currentThread();
                    for (int i2 = 0; i2 < fetcherInfo.fetchers.length; i2++) {
                        if (fetcherInfo.fetchers[i2] == threadCurrentThread) {
                            fetcherInfo.fetchers[i2] = null;
                            fetcherInfo.numFetchers--;
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                synchronized (fetcherInfo.waitList) {
                    Thread threadCurrentThread2 = Thread.currentThread();
                    for (int i3 = 0; i3 < fetcherInfo.fetchers.length; i3++) {
                        if (fetcherInfo.fetchers[i3] == threadCurrentThread2) {
                            fetcherInfo.fetchers[i3] = null;
                            fetcherInfo.numFetchers--;
                        }
                    }
                }
            }
        } catch (Throwable th) {
            synchronized (fetcherInfo.waitList) {
                Thread threadCurrentThread3 = Thread.currentThread();
                for (int i4 = 0; i4 < fetcherInfo.fetchers.length; i4++) {
                    if (fetcherInfo.fetchers[i4] == threadCurrentThread3) {
                        fetcherInfo.fetchers[i4] = null;
                        fetcherInfo.numFetchers--;
                    }
                }
                throw th;
            }
        }
    }

    private void fetchloop() {
        Thread threadCurrentThread = Thread.currentThread();
        while (isFetcher(threadCurrentThread)) {
            Thread.interrupted();
            threadCurrentThread.setPriority(8);
            ImageFetchable imageFetchableNextImage = nextImage();
            if (imageFetchableNextImage == null) {
                return;
            }
            try {
                imageFetchableNextImage.doFetch();
            } catch (Exception e2) {
                System.err.println("Uncaught error fetching image:");
                e2.printStackTrace();
            }
            stoppingAnimation(threadCurrentThread);
        }
    }

    static void startingAnimation() {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        Thread threadCurrentThread = Thread.currentThread();
        synchronized (fetcherInfo.waitList) {
            for (int i2 = 0; i2 < fetcherInfo.fetchers.length; i2++) {
                if (fetcherInfo.fetchers[i2] == threadCurrentThread) {
                    fetcherInfo.fetchers[i2] = null;
                    fetcherInfo.numFetchers--;
                    threadCurrentThread.setName("Image Animator " + i2);
                    if (fetcherInfo.waitList.size() > fetcherInfo.numWaiting) {
                        createFetchers(fetcherInfo);
                    }
                    return;
                }
            }
            threadCurrentThread.setPriority(2);
            threadCurrentThread.setName("Image Animator");
        }
    }

    private static void stoppingAnimation(Thread thread) {
        FetcherInfo fetcherInfo = FetcherInfo.getFetcherInfo();
        synchronized (fetcherInfo.waitList) {
            int i2 = -1;
            for (int i3 = 0; i3 < fetcherInfo.fetchers.length; i3++) {
                if (fetcherInfo.fetchers[i3] == thread) {
                    return;
                }
                if (fetcherInfo.fetchers[i3] == null) {
                    i2 = i3;
                }
            }
            if (i2 >= 0) {
                fetcherInfo.fetchers[i2] = thread;
                fetcherInfo.numFetchers++;
                thread.setName("Image Fetcher " + i2);
            }
        }
    }

    private static void createFetchers(final FetcherInfo fetcherInfo) {
        ThreadGroup threadGroup;
        AppContext appContext = AppContext.getAppContext();
        ThreadGroup threadGroup2 = appContext.getThreadGroup();
        try {
            if (threadGroup2.getParent() != null) {
                threadGroup = threadGroup2;
            } else {
                ThreadGroup threadGroup3 = Thread.currentThread().getThreadGroup();
                ThreadGroup parent = threadGroup3.getParent();
                while (parent != null) {
                    if (parent.getParent() == null) {
                        break;
                    }
                    threadGroup3 = parent;
                    parent = threadGroup3.getParent();
                }
                threadGroup = threadGroup3;
            }
        } catch (SecurityException e2) {
            threadGroup = appContext.getThreadGroup();
        }
        final ThreadGroup threadGroup4 = threadGroup;
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.image.ImageFetcher.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                for (int i2 = 0; i2 < fetcherInfo.fetchers.length; i2++) {
                    if (fetcherInfo.fetchers[i2] == null) {
                        ImageFetcher imageFetcher = new ImageFetcher(threadGroup4, i2);
                        try {
                            imageFetcher.start();
                            fetcherInfo.fetchers[i2] = imageFetcher;
                            fetcherInfo.numFetchers++;
                            return null;
                        } catch (Error e3) {
                        }
                    }
                }
                return null;
            }
        });
    }
}
