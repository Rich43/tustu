package sun.management;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/HotspotInternal.class */
public class HotspotInternal implements HotspotInternalMBean, MBeanRegistration {
    private static final String HOTSPOT_INTERNAL_MBEAN_NAME = "sun.management:type=HotspotInternal";
    private static ObjectName objName = Util.newObjectName(HOTSPOT_INTERNAL_MBEAN_NAME);
    private MBeanServer server = null;

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        ManagementFactoryHelper.registerInternalMBeans(mBeanServer);
        this.server = mBeanServer;
        return objName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
        ManagementFactoryHelper.unregisterInternalMBeans(this.server);
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }
}
