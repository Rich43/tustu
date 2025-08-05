package javax.management;

/* loaded from: rt.jar:javax/management/MBeanServerNotification.class */
public class MBeanServerNotification extends Notification {
    private static final long serialVersionUID = 2876477500475969677L;
    public static final String REGISTRATION_NOTIFICATION = "JMX.mbean.registered";
    public static final String UNREGISTRATION_NOTIFICATION = "JMX.mbean.unregistered";
    private final ObjectName objectName;

    public MBeanServerNotification(String str, Object obj, long j2, ObjectName objectName) {
        super(str, obj, j2);
        this.objectName = objectName;
    }

    public ObjectName getMBeanName() {
        return this.objectName;
    }

    @Override // javax.management.Notification, java.util.EventObject
    public String toString() {
        return super.toString() + "[mbeanName=" + ((Object) this.objectName) + "]";
    }
}
