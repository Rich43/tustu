package java.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:java/beans/ChangeListenerMap.class */
abstract class ChangeListenerMap<L extends EventListener> {
    private Map<String, L[]> map;

    protected abstract L[] newArray(int i2);

    protected abstract L newProxy(String str, L l2);

    public abstract L extract(L l2);

    ChangeListenerMap() {
    }

    public final synchronized void add(String str, L l2) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        L[] lArr = this.map.get(str);
        int length = lArr != null ? lArr.length : 0;
        EventListener[] eventListenerArrNewArray = newArray(length + 1);
        eventListenerArrNewArray[length] = l2;
        if (lArr != null) {
            System.arraycopy(lArr, 0, eventListenerArrNewArray, 0, length);
        }
        this.map.put(str, eventListenerArrNewArray);
    }

    public final synchronized void remove(String str, L l2) {
        L[] lArr;
        if (this.map != null && (lArr = this.map.get(str)) != null) {
            for (int i2 = 0; i2 < lArr.length; i2++) {
                if (l2.equals(lArr[i2])) {
                    int length = lArr.length - 1;
                    if (length > 0) {
                        EventListener[] eventListenerArrNewArray = newArray(length);
                        System.arraycopy(lArr, 0, eventListenerArrNewArray, 0, i2);
                        System.arraycopy(lArr, i2 + 1, eventListenerArrNewArray, i2, length - i2);
                        this.map.put(str, eventListenerArrNewArray);
                        return;
                    }
                    this.map.remove(str);
                    if (this.map.isEmpty()) {
                        this.map = null;
                        return;
                    }
                    return;
                }
            }
        }
    }

    public final synchronized L[] get(String str) {
        if (this.map != null) {
            return this.map.get(str);
        }
        return null;
    }

    public final void set(String str, L[] lArr) {
        if (lArr != null) {
            if (this.map == null) {
                this.map = new HashMap();
            }
            this.map.put(str, lArr);
        } else if (this.map != null) {
            this.map.remove(str);
            if (this.map.isEmpty()) {
                this.map = null;
            }
        }
    }

    public final synchronized L[] getListeners() {
        if (this.map == null) {
            return (L[]) newArray(0);
        }
        ArrayList arrayList = new ArrayList();
        L[] lArr = this.map.get(null);
        if (lArr != null) {
            for (L l2 : lArr) {
                arrayList.add(l2);
            }
        }
        for (Map.Entry<String, L[]> entry : this.map.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                for (L l3 : entry.getValue()) {
                    arrayList.add(newProxy(key, l3));
                }
            }
        }
        return (L[]) ((EventListener[]) arrayList.toArray(newArray(arrayList.size())));
    }

    public final L[] getListeners(String str) {
        EventListener[] eventListenerArr;
        if (str != null && (eventListenerArr = get(str)) != null) {
            return (L[]) ((EventListener[]) eventListenerArr.clone());
        }
        return (L[]) newArray(0);
    }

    public final synchronized boolean hasListeners(String str) {
        if (this.map == null) {
            return false;
        }
        return (this.map.get(null) == null && (str == null || null == this.map.get(str))) ? false : true;
    }

    public final Set<Map.Entry<String, L[]>> getEntries() {
        if (this.map != null) {
            return this.map.entrySet();
        }
        return Collections.emptySet();
    }
}
