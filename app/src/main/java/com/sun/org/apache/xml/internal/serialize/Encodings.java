package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.util.EncodingMap;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/Encodings.class */
class Encodings {
    static final int DEFAULT_LAST_PRINTABLE = 127;
    static final int LAST_PRINTABLE_UNICODE = 65535;
    static final String DEFAULT_ENCODING = "UTF8";
    static final String JIS_DANGER_CHARS = "\\~\u007f¢£¥¬—―‖…‾‾∥∯〜＼～￠￡￢￣";
    static final String[] UNICODE_ENCODINGS = {"Unicode", "UnicodeBig", "UnicodeLittle", "GB2312", "UTF8", "UTF-16"};
    private static final Map<String, EncodingInfo> _encodings = new ConcurrentHashMap();

    Encodings() {
    }

    static EncodingInfo getEncodingInfo(String encoding, boolean allowJavaNames) throws UnsupportedEncodingException {
        if (encoding == null) {
            EncodingInfo eInfo = _encodings.get("UTF8");
            if (eInfo != null) {
                return eInfo;
            }
            EncodingInfo eInfo2 = new EncodingInfo(EncodingMap.getJava2IANAMapping("UTF8"), "UTF8", 65535);
            _encodings.put("UTF8", eInfo2);
            return eInfo2;
        }
        String encoding2 = encoding.toUpperCase(Locale.ENGLISH);
        String jName = EncodingMap.getIANA2JavaMapping(encoding2);
        if (jName == null) {
            if (allowJavaNames) {
                EncodingInfo.testJavaEncodingName(encoding2);
                EncodingInfo encodingInfo = _encodings.get(encoding2);
                EncodingInfo eInfo3 = encodingInfo;
                if (encodingInfo != null) {
                    return eInfo3;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= UNICODE_ENCODINGS.length) {
                        break;
                    }
                    if (!UNICODE_ENCODINGS[i2].equalsIgnoreCase(encoding2)) {
                        i2++;
                    } else {
                        eInfo3 = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding2), encoding2, 65535);
                        break;
                    }
                }
                if (i2 == UNICODE_ENCODINGS.length) {
                    eInfo3 = new EncodingInfo(EncodingMap.getJava2IANAMapping(encoding2), encoding2, 127);
                }
                _encodings.put(encoding2, eInfo3);
                return eInfo3;
            }
            throw new UnsupportedEncodingException(encoding2);
        }
        EncodingInfo encodingInfo2 = _encodings.get(jName);
        EncodingInfo eInfo4 = encodingInfo2;
        if (encodingInfo2 != null) {
            return eInfo4;
        }
        int i3 = 0;
        while (true) {
            if (i3 >= UNICODE_ENCODINGS.length) {
                break;
            }
            if (!UNICODE_ENCODINGS[i3].equalsIgnoreCase(jName)) {
                i3++;
            } else {
                eInfo4 = new EncodingInfo(encoding2, jName, 65535);
                break;
            }
        }
        if (i3 == UNICODE_ENCODINGS.length) {
            eInfo4 = new EncodingInfo(encoding2, jName, 127);
        }
        _encodings.put(jName, eInfo4);
        return eInfo4;
    }
}
