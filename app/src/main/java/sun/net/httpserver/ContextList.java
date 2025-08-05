package sun.net.httpserver;

import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: rt.jar:sun/net/httpserver/ContextList.class */
class ContextList {
    static final int MAX_CONTEXTS = 50;
    LinkedList<HttpContextImpl> list = new LinkedList<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    ContextList() {
    }

    static {
        $assertionsDisabled = !ContextList.class.desiredAssertionStatus();
    }

    public synchronized void add(HttpContextImpl httpContextImpl) {
        if (!$assertionsDisabled && httpContextImpl.getPath() == null) {
            throw new AssertionError();
        }
        this.list.add(httpContextImpl);
    }

    public synchronized int size() {
        return this.list.size();
    }

    synchronized HttpContextImpl findContext(String str, String str2) {
        return findContext(str, str2, false);
    }

    synchronized HttpContextImpl findContext(String str, String str2, boolean z2) {
        String lowerCase = str.toLowerCase();
        String str3 = "";
        HttpContextImpl httpContextImpl = null;
        Iterator<HttpContextImpl> it = this.list.iterator();
        while (it.hasNext()) {
            HttpContextImpl next = it.next();
            if (next.getProtocol().equals(lowerCase)) {
                String path = next.getPath();
                if (!z2 || path.equals(str2)) {
                    if (z2 || str2.startsWith(path)) {
                        if (path.length() > str3.length()) {
                            str3 = path;
                            httpContextImpl = next;
                        }
                    }
                }
            }
        }
        return httpContextImpl;
    }

    public synchronized void remove(String str, String str2) throws IllegalArgumentException {
        HttpContextImpl httpContextImplFindContext = findContext(str, str2, true);
        if (httpContextImplFindContext == null) {
            throw new IllegalArgumentException("cannot remove element from list");
        }
        this.list.remove(httpContextImplFindContext);
    }

    public synchronized void remove(HttpContextImpl httpContextImpl) throws IllegalArgumentException {
        Iterator<HttpContextImpl> it = this.list.iterator();
        while (it.hasNext()) {
            HttpContextImpl next = it.next();
            if (next.equals(httpContextImpl)) {
                this.list.remove(next);
                return;
            }
        }
        throw new IllegalArgumentException("no such context in list");
    }
}
