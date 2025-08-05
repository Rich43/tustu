package com.sun.xml.internal.ws.streaming;

import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/PrefixFactoryImpl.class */
public class PrefixFactoryImpl implements PrefixFactory {
    private String _base;
    private int _next = 1;
    private Map _cachedUriToPrefixMap;

    public PrefixFactoryImpl(String base) {
        this._base = base;
    }

    @Override // com.sun.xml.internal.ws.streaming.PrefixFactory
    public String getPrefix(String uri) {
        String prefix = null;
        if (this._cachedUriToPrefixMap == null) {
            this._cachedUriToPrefixMap = new HashMap();
        } else {
            prefix = (String) this._cachedUriToPrefixMap.get(uri);
        }
        if (prefix == null) {
            StringBuilder sbAppend = new StringBuilder().append(this._base);
            int i2 = this._next;
            this._next = i2 + 1;
            prefix = sbAppend.append(Integer.toString(i2)).toString();
            this._cachedUriToPrefixMap.put(uri, prefix);
        }
        return prefix;
    }
}
