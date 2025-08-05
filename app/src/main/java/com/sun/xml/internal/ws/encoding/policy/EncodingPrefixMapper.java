package com.sun.xml.internal.ws.encoding.policy;

import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/policy/EncodingPrefixMapper.class */
public class EncodingPrefixMapper implements PrefixMapper {
    private static final Map<String, String> prefixMap = new HashMap();

    static {
        prefixMap.put(EncodingConstants.ENCODING_NS, "wspe");
        prefixMap.put(EncodingConstants.OPTIMIZED_MIME_NS, "wsoma");
        prefixMap.put(EncodingConstants.SUN_ENCODING_CLIENT_NS, "cenc");
        prefixMap.put(EncodingConstants.SUN_FI_SERVICE_NS, "fi");
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PrefixMapper
    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }
}
