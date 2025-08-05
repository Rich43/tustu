package javax.management.relation;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/relation/MBeanServerNotificationFilter.class */
public class MBeanServerNotificationFilter extends NotificationFilterSupport {
    private static final long oldSerialVersionUID = 6001782699077323605L;
    private static final long newSerialVersionUID = 2605900539589789736L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("mySelectObjNameList", Vector.class), new ObjectStreamField("myDeselectObjNameList", Vector.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("selectedNames", List.class), new ObjectStreamField("deselectedNames", List.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private List<ObjectName> selectedNames = new Vector();
    private List<ObjectName> deselectedNames = null;

    static {
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e2) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
    }

    public MBeanServerNotificationFilter() {
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "MBeanServerNotificationFilter");
        enableType(MBeanServerNotification.REGISTRATION_NOTIFICATION);
        enableType(MBeanServerNotification.UNREGISTRATION_NOTIFICATION);
        JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "MBeanServerNotificationFilter");
    }

    public synchronized void disableAllObjectNames() {
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "disableAllObjectNames");
        this.selectedNames = new Vector();
        this.deselectedNames = null;
        JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "disableAllObjectNames");
    }

    public synchronized void disableObjectName(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "disableObjectName", objectName);
        if (this.selectedNames != null && this.selectedNames.size() != 0) {
            this.selectedNames.remove(objectName);
        }
        if (this.deselectedNames != null && !this.deselectedNames.contains(objectName)) {
            this.deselectedNames.add(objectName);
        }
        JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "disableObjectName");
    }

    public synchronized void enableAllObjectNames() {
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "enableAllObjectNames");
        this.selectedNames = null;
        this.deselectedNames = new Vector();
        JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "enableAllObjectNames");
    }

    public synchronized void enableObjectName(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "enableObjectName", objectName);
        if (this.deselectedNames != null && this.deselectedNames.size() != 0) {
            this.deselectedNames.remove(objectName);
        }
        if (this.selectedNames != null && !this.selectedNames.contains(objectName)) {
            this.selectedNames.add(objectName);
        }
        JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "enableObjectName");
    }

    public synchronized Vector<ObjectName> getEnabledObjectNames() {
        if (this.selectedNames != null) {
            return new Vector<>(this.selectedNames);
        }
        return null;
    }

    public synchronized Vector<ObjectName> getDisabledObjectNames() {
        if (this.deselectedNames != null) {
            return new Vector<>(this.deselectedNames);
        }
        return null;
    }

    @Override // javax.management.NotificationFilterSupport, javax.management.NotificationFilter
    public synchronized boolean isNotificationEnabled(Notification notification) throws IllegalArgumentException {
        if (notification == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", notification);
        if (!getEnabledTypes().contains(notification.getType())) {
            JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "Type not selected, exiting");
            return false;
        }
        ObjectName mBeanName = ((MBeanServerNotification) notification).getMBeanName();
        boolean zContains = false;
        if (this.selectedNames != null) {
            if (this.selectedNames.size() == 0) {
                JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "No ObjectNames selected, exiting");
                return false;
            }
            zContains = this.selectedNames.contains(mBeanName);
            if (!zContains) {
                JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName not in selected list, exiting");
                return false;
            }
        }
        if (!zContains) {
            if (this.deselectedNames == null) {
                JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName not selected, and all names deselected, exiting");
                return false;
            }
            if (this.deselectedNames.contains(mBeanName)) {
                JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName explicitly not selected, exiting");
                return false;
            }
        }
        JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName selected, exiting");
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.selectedNames = (List) Util.cast(fields.get("mySelectObjNameList", (Object) null));
            if (fields.defaulted("mySelectObjNameList")) {
                throw new NullPointerException("mySelectObjNameList");
            }
            this.deselectedNames = (List) Util.cast(fields.get("myDeselectObjNameList", (Object) null));
            if (fields.defaulted("myDeselectObjNameList")) {
                throw new NullPointerException("myDeselectObjNameList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("mySelectObjNameList", this.selectedNames);
            putFieldPutFields.put("myDeselectObjNameList", this.deselectedNames);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
