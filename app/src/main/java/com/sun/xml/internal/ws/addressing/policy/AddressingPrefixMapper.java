package com.sun.xml.internal.ws.addressing.policy;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/policy/AddressingPrefixMapper.class */
public class AddressingPrefixMapper implements PrefixMapper {
    private static final Map<String, String> prefixMap = new HashMap();

    static {
        prefixMap.put(AddressingVersion.MEMBER.policyNsUri, "wsap");
        prefixMap.put(AddressingVersion.MEMBER.nsUri, "wsa");
        prefixMap.put(W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME, W3CAddressingMetadataConstants.WSAM_PREFIX_NAME);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PrefixMapper
    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }
}
