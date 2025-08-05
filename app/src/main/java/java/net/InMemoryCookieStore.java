package java.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/net/InMemoryCookieStore.class */
class InMemoryCookieStore implements CookieStore {
    private List<HttpCookie> cookieJar;
    private Map<String, List<HttpCookie>> domainIndex;
    private Map<URI, List<HttpCookie>> uriIndex;
    private ReentrantLock lock;

    public InMemoryCookieStore() {
        this.cookieJar = null;
        this.domainIndex = null;
        this.uriIndex = null;
        this.lock = null;
        this.cookieJar = new ArrayList();
        this.domainIndex = new HashMap();
        this.uriIndex = new HashMap();
        this.lock = new ReentrantLock(false);
    }

    @Override // java.net.CookieStore
    public void add(URI uri, HttpCookie httpCookie) {
        if (httpCookie == null) {
            throw new NullPointerException("cookie is null");
        }
        this.lock.lock();
        try {
            this.cookieJar.remove(httpCookie);
            if (httpCookie.getMaxAge() != 0) {
                this.cookieJar.add(httpCookie);
                if (httpCookie.getDomain() != null) {
                    addIndex(this.domainIndex, httpCookie.getDomain(), httpCookie);
                }
                if (uri != null) {
                    addIndex(this.uriIndex, getEffectiveURI(uri), httpCookie);
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.net.CookieStore
    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }
        ArrayList arrayList = new ArrayList();
        boolean zEqualsIgnoreCase = "https".equalsIgnoreCase(uri.getScheme());
        this.lock.lock();
        try {
            getInternal1(arrayList, this.domainIndex, uri.getHost(), zEqualsIgnoreCase);
            getInternal2(arrayList, this.uriIndex, getEffectiveURI(uri), zEqualsIgnoreCase);
            this.lock.unlock();
            return arrayList;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    @Override // java.net.CookieStore
    public List<HttpCookie> getCookies() {
        List<HttpCookie> listUnmodifiableList;
        this.lock.lock();
        try {
            Iterator<HttpCookie> it = this.cookieJar.iterator();
            while (it.hasNext()) {
                if (it.next().hasExpired()) {
                    it.remove();
                }
            }
            return listUnmodifiableList;
        } finally {
            Collections.unmodifiableList(this.cookieJar);
            this.lock.unlock();
        }
    }

    @Override // java.net.CookieStore
    public List<URI> getURIs() {
        ArrayList arrayList = new ArrayList();
        this.lock.lock();
        try {
            Iterator<URI> it = this.uriIndex.keySet().iterator();
            while (it.hasNext()) {
                List<HttpCookie> list = this.uriIndex.get(it.next());
                if (list == null || list.size() == 0) {
                    it.remove();
                }
            }
            return arrayList;
        } finally {
            arrayList.addAll(this.uriIndex.keySet());
            this.lock.unlock();
        }
    }

    @Override // java.net.CookieStore
    public boolean remove(URI uri, HttpCookie httpCookie) {
        if (httpCookie == null) {
            throw new NullPointerException("cookie is null");
        }
        this.lock.lock();
        try {
            boolean zRemove = this.cookieJar.remove(httpCookie);
            this.lock.unlock();
            return zRemove;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    @Override // java.net.CookieStore
    public boolean removeAll() {
        this.lock.lock();
        try {
            if (this.cookieJar.isEmpty()) {
                return false;
            }
            this.cookieJar.clear();
            this.domainIndex.clear();
            this.uriIndex.clear();
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    private boolean netscapeDomainMatches(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        boolean zEqualsIgnoreCase = ".local".equalsIgnoreCase(str);
        int iIndexOf = str.indexOf(46);
        if (iIndexOf == 0) {
            iIndexOf = str.indexOf(46, 1);
        }
        if (!zEqualsIgnoreCase && (iIndexOf == -1 || iIndexOf == str.length() - 1)) {
            return false;
        }
        if (str2.indexOf(46) == -1 && zEqualsIgnoreCase) {
            return true;
        }
        int length = str2.length() - str.length();
        if (length == 0) {
            return str2.equalsIgnoreCase(str);
        }
        if (length <= 0) {
            return length == -1 && str.charAt(0) == '.' && str2.equalsIgnoreCase(str.substring(1));
        }
        str2.substring(0, length);
        return str2.substring(length).equalsIgnoreCase(str);
    }

    private void getInternal1(List<HttpCookie> list, Map<String, List<HttpCookie>> map, String str, boolean z2) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, List<HttpCookie>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<HttpCookie> value = entry.getValue();
            for (HttpCookie httpCookie : value) {
                if ((httpCookie.getVersion() == 0 && netscapeDomainMatches(key, str)) || (httpCookie.getVersion() == 1 && HttpCookie.domainMatches(key, str))) {
                    if (this.cookieJar.indexOf(httpCookie) != -1) {
                        if (!httpCookie.hasExpired()) {
                            if (z2 || !httpCookie.getSecure()) {
                                if (!list.contains(httpCookie)) {
                                    list.add(httpCookie);
                                }
                            }
                        } else {
                            arrayList.add(httpCookie);
                        }
                    } else {
                        arrayList.add(httpCookie);
                    }
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                HttpCookie httpCookie2 = (HttpCookie) it.next();
                value.remove(httpCookie2);
                this.cookieJar.remove(httpCookie2);
            }
            arrayList.clear();
        }
    }

    private <T> void getInternal2(List<HttpCookie> list, Map<T, List<HttpCookie>> map, Comparable<T> comparable, boolean z2) {
        List<HttpCookie> list2;
        for (T t2 : map.keySet()) {
            if (comparable.compareTo(t2) == 0 && (list2 = map.get(t2)) != null) {
                Iterator<HttpCookie> it = list2.iterator();
                while (it.hasNext()) {
                    HttpCookie next = it.next();
                    if (this.cookieJar.indexOf(next) != -1) {
                        if (!next.hasExpired()) {
                            if (z2 || !next.getSecure()) {
                                if (!list.contains(next)) {
                                    list.add(next);
                                }
                            }
                        } else {
                            it.remove();
                            this.cookieJar.remove(next);
                        }
                    } else {
                        it.remove();
                    }
                }
            }
        }
    }

    private <T> void addIndex(Map<T, List<HttpCookie>> map, T t2, HttpCookie httpCookie) {
        if (t2 != null) {
            List<HttpCookie> list = map.get(t2);
            if (list != null) {
                list.remove(httpCookie);
                list.add(httpCookie);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(httpCookie);
                map.put(t2, arrayList);
            }
        }
    }

    private URI getEffectiveURI(URI uri) {
        URI uri2;
        try {
            uri2 = new URI("http", uri.getHost(), null, null, null);
        } catch (URISyntaxException e2) {
            uri2 = uri;
        }
        return uri2;
    }
}
