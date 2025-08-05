package com.sun.org.apache.xml.internal.dtm.ref;

import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/CustomStringPool.class */
public class CustomStringPool extends DTMStringPool {
    final Map<String, Integer> m_stringToInt = new HashMap();
    public static final int NULL = -1;

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public void removeAllElements() {
        this.m_intToString.removeAllElements();
        if (this.m_stringToInt != null) {
            this.m_stringToInt.clear();
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public String indexToString(int i2) throws ArrayIndexOutOfBoundsException {
        return (String) this.m_intToString.elementAt(i2);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public int stringToIndex(String s2) {
        if (s2 == null) {
            return -1;
        }
        Integer iobj = this.m_stringToInt.get(s2);
        if (iobj == null) {
            this.m_intToString.addElement(s2);
            iobj = Integer.valueOf(this.m_intToString.size());
            this.m_stringToInt.put(s2, iobj);
        }
        return iobj.intValue();
    }
}
