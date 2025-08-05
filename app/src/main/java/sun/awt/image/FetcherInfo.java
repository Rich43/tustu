package sun.awt.image;

import java.util.Vector;
import sun.awt.AppContext;

/* compiled from: ImageFetcher.java */
/* loaded from: rt.jar:sun/awt/image/FetcherInfo.class */
class FetcherInfo {
    static final int MAX_NUM_FETCHERS_PER_APPCONTEXT = 4;
    Thread[] fetchers = new Thread[4];
    int numFetchers = 0;
    int numWaiting = 0;
    Vector waitList = new Vector();
    private static final Object FETCHER_INFO_KEY = new StringBuffer("FetcherInfo");

    private FetcherInfo() {
    }

    static FetcherInfo getFetcherInfo() {
        FetcherInfo fetcherInfo;
        AppContext appContext = AppContext.getAppContext();
        synchronized (appContext) {
            FetcherInfo fetcherInfo2 = (FetcherInfo) appContext.get(FETCHER_INFO_KEY);
            if (fetcherInfo2 == null) {
                fetcherInfo2 = new FetcherInfo();
                appContext.put(FETCHER_INFO_KEY, fetcherInfo2);
            }
            fetcherInfo = fetcherInfo2;
        }
        return fetcherInfo;
    }
}
