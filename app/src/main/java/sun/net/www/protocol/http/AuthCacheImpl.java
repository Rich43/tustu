package sun.net.www.protocol.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

/* loaded from: rt.jar:sun/net/www/protocol/http/AuthCacheImpl.class */
public class AuthCacheImpl implements AuthCache {
    HashMap<String, LinkedList<AuthCacheValue>> hashtable = new HashMap<>();

    public void setMap(HashMap<String, LinkedList<AuthCacheValue>> map) {
        this.hashtable = map;
    }

    @Override // sun.net.www.protocol.http.AuthCache
    public synchronized void put(String str, AuthCacheValue authCacheValue) {
        LinkedList<AuthCacheValue> linkedList = this.hashtable.get(str);
        String path = authCacheValue.getPath();
        if (linkedList == null) {
            linkedList = new LinkedList<>();
            this.hashtable.put(str, linkedList);
        }
        ListIterator<AuthCacheValue> listIterator = linkedList.listIterator();
        while (listIterator.hasNext()) {
            AuthenticationInfo authenticationInfo = (AuthenticationInfo) listIterator.next();
            if (authenticationInfo.path == null || authenticationInfo.path.startsWith(path)) {
                listIterator.remove();
            }
        }
        listIterator.add(authCacheValue);
    }

    @Override // sun.net.www.protocol.http.AuthCache
    public synchronized AuthCacheValue get(String str, String str2) {
        LinkedList<AuthCacheValue> linkedList = this.hashtable.get(str);
        if (linkedList == null || linkedList.size() == 0) {
            return null;
        }
        if (str2 == null) {
            return (AuthenticationInfo) linkedList.get(0);
        }
        ListIterator<AuthCacheValue> listIterator = linkedList.listIterator();
        while (listIterator.hasNext()) {
            AuthenticationInfo authenticationInfo = (AuthenticationInfo) listIterator.next();
            if (str2.startsWith(authenticationInfo.path)) {
                return authenticationInfo;
            }
        }
        return null;
    }

    @Override // sun.net.www.protocol.http.AuthCache
    public synchronized void remove(String str, AuthCacheValue authCacheValue) {
        LinkedList<AuthCacheValue> linkedList = this.hashtable.get(str);
        if (linkedList == null) {
            return;
        }
        if (authCacheValue == null) {
            linkedList.clear();
            return;
        }
        ListIterator<AuthCacheValue> listIterator = linkedList.listIterator();
        while (listIterator.hasNext()) {
            if (authCacheValue.equals((AuthenticationInfo) listIterator.next())) {
                listIterator.remove();
            }
        }
    }
}
