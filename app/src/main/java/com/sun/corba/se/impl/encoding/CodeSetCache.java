package com.sun.corba.se.impl.encoding;

import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CodeSetCache.class */
class CodeSetCache {
    private ThreadLocal converterCaches = new ThreadLocal() { // from class: com.sun.corba.se.impl.encoding.CodeSetCache.1
        @Override // java.lang.ThreadLocal
        public Object initialValue() {
            return new Map[]{new WeakHashMap(), new WeakHashMap()};
        }
    };
    private static final int BTC_CACHE_MAP = 0;
    private static final int CTB_CACHE_MAP = 1;

    CodeSetCache() {
    }

    CharsetDecoder getByteToCharConverter(Object obj) {
        return (CharsetDecoder) ((Map[]) this.converterCaches.get())[0].get(obj);
    }

    CharsetEncoder getCharToByteConverter(Object obj) {
        return (CharsetEncoder) ((Map[]) this.converterCaches.get())[1].get(obj);
    }

    CharsetDecoder setConverter(Object obj, CharsetDecoder charsetDecoder) {
        ((Map[]) this.converterCaches.get())[0].put(obj, charsetDecoder);
        return charsetDecoder;
    }

    CharsetEncoder setConverter(Object obj, CharsetEncoder charsetEncoder) {
        ((Map[]) this.converterCaches.get())[1].put(obj, charsetEncoder);
        return charsetEncoder;
    }
}
