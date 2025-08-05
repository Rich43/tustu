package com.sun.org.glassfish.external.amx;

import com.sun.jmx.defaults.ServiceName;
import com.sun.org.glassfish.external.arc.Stability;
import com.sun.org.glassfish.external.arc.Taxonomy;
import javax.management.ObjectName;
import jdk.internal.dynalink.CallSiteDescriptor;

@Taxonomy(stability = Stability.UNCOMMITTED)
/* loaded from: rt.jar:com/sun/org/glassfish/external/amx/AMXUtil.class */
public final class AMXUtil {
    private AMXUtil() {
    }

    public static ObjectName newObjectName(String s2) {
        try {
            return new ObjectName(s2);
        } catch (Exception e2) {
            throw new RuntimeException("bad ObjectName", e2);
        }
    }

    public static ObjectName newObjectName(String domain, String props) {
        return newObjectName(domain + CallSiteDescriptor.TOKEN_DELIMITER + props);
    }

    public static ObjectName getMBeanServerDelegateObjectName() {
        return newObjectName(ServiceName.DELEGATE);
    }

    public static String prop(String key, String value) {
        return key + "=" + value;
    }
}
