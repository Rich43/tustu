package com.sun.org.glassfish.gmbal;

import com.sun.org.glassfish.external.amx.AMX;
import java.util.Map;

@ManagedObject
@Description("Base interface for any MBean that works in the AMX framework")
/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/AMXMBeanInterface.class */
public interface AMXMBeanInterface {
    Map<String, ?> getMeta();

    @ManagedAttribute(id = "Name")
    @Description("Return the name of this MBean.")
    String getName();

    @ManagedAttribute(id = AMX.ATTR_PARENT)
    @Description("The container that contains this MBean")
    AMXMBeanInterface getParent();

    @ManagedAttribute(id = AMX.ATTR_CHILDREN)
    @Description("All children of this AMX MBean")
    AMXMBeanInterface[] getChildren();
}
