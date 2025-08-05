package org.jpedal.jbig2.segment;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/Flags.class */
public abstract class Flags {
    protected int flagsAsInt;
    protected Map<String, Integer> flags = new LinkedHashMap();

    public abstract void setFlags(int i2);

    public int getFlagValue(String key) {
        return this.flags.get(key).intValue();
    }
}
