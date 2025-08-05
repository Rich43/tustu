package com.sun.webkit.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/network/CookieStore.class */
final class CookieStore {
    private static final Logger logger = Logger.getLogger(CookieStore.class.getName());
    private static final int MAX_BUCKET_SIZE = 50;
    private static final int TOTAL_COUNT_LOWER_THRESHOLD = 3000;
    private static final int TOTAL_COUNT_UPPER_THRESHOLD = 4000;
    private final Map<String, Map<Cookie, Cookie>> buckets = new HashMap();
    private int totalCount = 0;

    CookieStore() {
    }

    Cookie get(Cookie cookie) {
        Cookie storedCookie;
        Map<Cookie, Cookie> bucket = this.buckets.get(cookie.getDomain());
        if (bucket == null || (storedCookie = bucket.get(cookie)) == null) {
            return null;
        }
        if (storedCookie.hasExpired()) {
            bucket.remove(storedCookie);
            this.totalCount--;
            log("Expired cookie removed by get", storedCookie, bucket);
            return null;
        }
        return storedCookie;
    }

    List<Cookie> get(String hostname, String path, boolean secureProtocol, boolean httpApi) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "hostname: [{0}], path: [{1}], secureProtocol: [{2}], httpApi: [{3}]", new Object[]{hostname, path, Boolean.valueOf(secureProtocol), Boolean.valueOf(httpApi)});
        }
        ArrayList<Cookie> result = new ArrayList<>();
        String strSubstring = hostname;
        while (true) {
            String domain = strSubstring;
            if (domain.length() > 0) {
                Map<Cookie, Cookie> bucket = this.buckets.get(domain);
                if (bucket != null) {
                    find(result, bucket, hostname, path, secureProtocol, httpApi);
                }
                int nextPoint = domain.indexOf(46);
                if (nextPoint == -1) {
                    break;
                }
                strSubstring = domain.substring(nextPoint + 1);
            } else {
                break;
            }
        }
        Collections.sort(result, new GetComparator());
        long currentTime = System.currentTimeMillis();
        Iterator<Cookie> it = result.iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            cookie.setLastAccessTime(currentTime);
        }
        logger.log(Level.FINEST, "result: {0}", result);
        return result;
    }

    private void find(List<Cookie> list, Map<Cookie, Cookie> bucket, String hostname, String path, boolean secureProtocol, boolean httpApi) {
        Iterator<Cookie> it = bucket.values().iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            if (cookie.hasExpired()) {
                it.remove();
                this.totalCount--;
                log("Expired cookie removed by find", cookie, bucket);
            } else if (cookie.getHostOnly()) {
                if (hostname.equalsIgnoreCase(cookie.getDomain())) {
                    if (Cookie.pathMatches(path, cookie.getPath()) && (!cookie.getSecureOnly() || secureProtocol)) {
                        if (cookie.getHttpOnly() || httpApi) {
                            list.add(cookie);
                        }
                    }
                }
            } else if (Cookie.domainMatches(hostname, cookie.getDomain())) {
                if (Cookie.pathMatches(path, cookie.getPath())) {
                    if (cookie.getHttpOnly()) {
                    }
                    list.add(cookie);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/CookieStore$GetComparator.class */
    private static final class GetComparator implements Comparator<Cookie> {
        private GetComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Cookie c1, Cookie c2) {
            int d2 = c2.getPath().length() - c1.getPath().length();
            if (d2 != 0) {
                return d2;
            }
            return c1.getCreationTime().compareTo(c2.getCreationTime());
        }
    }

    void put(Cookie cookie) {
        Map<Cookie, Cookie> bucket = this.buckets.get(cookie.getDomain());
        if (bucket == null) {
            bucket = new LinkedHashMap(20);
            this.buckets.put(cookie.getDomain(), bucket);
        }
        if (cookie.hasExpired()) {
            log("Cookie expired", cookie, bucket);
            if (bucket.remove(cookie) != null) {
                this.totalCount--;
                log("Expired cookie removed by put", cookie, bucket);
                return;
            }
            return;
        }
        if (bucket.put(cookie, cookie) == null) {
            this.totalCount++;
            log("Cookie added", cookie, bucket);
            if (bucket.size() > 50) {
                purge(bucket);
            }
            if (this.totalCount > TOTAL_COUNT_UPPER_THRESHOLD) {
                purge();
                return;
            }
            return;
        }
        log("Cookie updated", cookie, bucket);
    }

    private void purge(Map<Cookie, Cookie> bucket) {
        logger.log(Level.FINEST, "Purging bucket: {0}", bucket.values());
        Cookie earliestCookie = null;
        Iterator<Cookie> it = bucket.values().iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            if (cookie.hasExpired()) {
                it.remove();
                this.totalCount--;
                log("Expired cookie removed", cookie, bucket);
            } else if (earliestCookie == null || cookie.getLastAccessTime() < earliestCookie.getLastAccessTime()) {
                earliestCookie = cookie;
            }
        }
        if (bucket.size() > 50) {
            bucket.remove(earliestCookie);
            this.totalCount--;
            log("Excess cookie removed", earliestCookie, bucket);
        }
    }

    private void purge() {
        logger.log(Level.FINEST, "Purging store");
        Queue<Cookie> removalQueue = new PriorityQueue<>(this.totalCount / 2, new RemovalComparator());
        for (Map.Entry<String, Map<Cookie, Cookie>> entry : this.buckets.entrySet()) {
            Map<Cookie, Cookie> bucket = entry.getValue();
            Iterator<Cookie> it = bucket.values().iterator();
            while (it.hasNext()) {
                Cookie cookie = it.next();
                if (cookie.hasExpired()) {
                    it.remove();
                    this.totalCount--;
                    log("Expired cookie removed", cookie, bucket);
                } else {
                    removalQueue.add(cookie);
                }
            }
        }
        while (this.totalCount > 3000) {
            Cookie cookie2 = removalQueue.remove();
            Map<Cookie, Cookie> bucket2 = this.buckets.get(cookie2.getDomain());
            if (bucket2 != null) {
                bucket2.remove(cookie2);
                this.totalCount--;
                log("Excess cookie removed", cookie2, bucket2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/CookieStore$RemovalComparator.class */
    private static final class RemovalComparator implements Comparator<Cookie> {
        private RemovalComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Cookie c1, Cookie c2) {
            return (int) (c1.getLastAccessTime() - c2.getLastAccessTime());
        }
    }

    private void log(String message, Cookie cookie, Map<Cookie, Cookie> bucket) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0}: {1}, bucket size: {2}, total count: {3}", new Object[]{message, cookie, Integer.valueOf(bucket.size()), Integer.valueOf(this.totalCount)});
        }
    }
}
