package java.awt;

import java.util.HashMap;
import java.util.Map;

/* compiled from: AWTKeyStroke.java */
/* loaded from: rt.jar:java/awt/VKCollection.class */
class VKCollection {
    Map<Integer, String> code2name = new HashMap();
    Map<String, Integer> name2code = new HashMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VKCollection.class.desiredAssertionStatus();
    }

    public synchronized void put(String str, Integer num) {
        if (!$assertionsDisabled && (str == null || num == null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && findName(num) != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && findCode(str) != null) {
            throw new AssertionError();
        }
        this.code2name.put(num, str);
        this.name2code.put(str, num);
    }

    public synchronized Integer findCode(String str) {
        if ($assertionsDisabled || str != null) {
            return this.name2code.get(str);
        }
        throw new AssertionError();
    }

    public synchronized String findName(Integer num) {
        if ($assertionsDisabled || num != null) {
            return this.code2name.get(num);
        }
        throw new AssertionError();
    }
}
