package com.sun.xml.internal.ws.config.management.policy;

import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/config/management/policy/ManagementPrefixMapper.class */
public class ManagementPrefixMapper implements PrefixMapper {
    private static final Map<String, String> prefixMap = new HashMap();

    static {
        prefixMap.put(PolicyConstants.SUN_MANAGEMENT_NAMESPACE, "sunman");
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PrefixMapper
    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }
}
