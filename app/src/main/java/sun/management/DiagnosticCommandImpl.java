package sun.management;

import com.sun.management.DiagnosticCommandMBean;
import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.InvalidAttributeValueException;
import javax.management.JMX;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/* loaded from: rt.jar:sun/management/DiagnosticCommandImpl.class */
class DiagnosticCommandImpl extends NotificationEmitterSupport implements DiagnosticCommandMBean {
    private final VMManagement jvm;
    private final boolean isSupported;
    private static final String notifName = "javax.management.Notification";
    private static final String strClassName = "".getClass().getName();
    private static final String strArrayClassName = String[].class.getName();
    private static final String[] diagFramNotifTypes = {"jmx.mbean.info.changed"};
    private static long seqNumber = 0;
    private volatile Map<String, Wrapper> wrappers = null;
    private MBeanNotificationInfo[] notifInfo = null;

    private native void setNotificationEnabled(boolean z2);

    private native String[] getDiagnosticCommands();

    private native DiagnosticCommandInfo[] getDiagnosticCommandInfo(String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public native String executeDiagnosticCommand(String str);

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws MBeanException, AttributeNotFoundException, ReflectionException {
        throw new AttributeNotFoundException(str);
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException {
        throw new AttributeNotFoundException(attribute.getName());
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        return new AttributeList();
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        return new AttributeList();
    }

    /* loaded from: rt.jar:sun/management/DiagnosticCommandImpl$Wrapper.class */
    private class Wrapper {
        String name;
        String cmd;
        DiagnosticCommandInfo info;
        Permission permission;

        Wrapper(String str, String str2, DiagnosticCommandInfo diagnosticCommandInfo) throws InstantiationException {
            this.name = str;
            this.cmd = str2;
            this.info = diagnosticCommandInfo;
            this.permission = null;
            Throwable th = null;
            if (diagnosticCommandInfo.getPermissionClass() != null) {
                try {
                    Class<?> cls = Class.forName(diagnosticCommandInfo.getPermissionClass());
                    if (diagnosticCommandInfo.getPermissionAction() == null) {
                        try {
                            this.permission = (Permission) cls.getConstructor(String.class).newInstance(diagnosticCommandInfo.getPermissionName());
                        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e2) {
                            th = e2;
                        }
                    }
                    if (this.permission == null) {
                        try {
                            this.permission = (Permission) cls.getConstructor(String.class, String.class).newInstance(diagnosticCommandInfo.getPermissionName(), diagnosticCommandInfo.getPermissionAction());
                        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e3) {
                            th = e3;
                        }
                    }
                } catch (ClassNotFoundException e4) {
                }
                if (this.permission == null) {
                    new InstantiationException("Unable to instantiate required permission").initCause(th);
                }
            }
        }

        public String execute(String[] strArr) {
            SecurityManager securityManager;
            if (this.permission != null && (securityManager = System.getSecurityManager()) != null) {
                securityManager.checkPermission(this.permission);
            }
            if (strArr == null) {
                return DiagnosticCommandImpl.this.executeDiagnosticCommand(this.cmd);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.cmd);
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (strArr[i2] == null) {
                    throw new IllegalArgumentException("Invalid null argument");
                }
                sb.append(" ");
                sb.append(strArr[i2]);
            }
            return DiagnosticCommandImpl.this.executeDiagnosticCommand(sb.toString());
        }
    }

    DiagnosticCommandImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
        this.isSupported = vMManagement.isRemoteDiagnosticCommandsSupported();
    }

    /* loaded from: rt.jar:sun/management/DiagnosticCommandImpl$OperationInfoComparator.class */
    private static class OperationInfoComparator implements Comparator<MBeanOperationInfo> {
        private OperationInfoComparator() {
        }

        @Override // java.util.Comparator
        public int compare(MBeanOperationInfo mBeanOperationInfo, MBeanOperationInfo mBeanOperationInfo2) {
            return mBeanOperationInfo.getName().compareTo(mBeanOperationInfo2.getName());
        }
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        Map map;
        TreeSet treeSet = new TreeSet(new OperationInfoComparator());
        if (!this.isSupported) {
            map = Collections.EMPTY_MAP;
        } else {
            try {
                String[] diagnosticCommands = getDiagnosticCommands();
                DiagnosticCommandInfo[] diagnosticCommandInfo = getDiagnosticCommandInfo(diagnosticCommands);
                MBeanParameterInfo[] mBeanParameterInfoArr = {new MBeanParameterInfo("arguments", strArrayClassName, "Array of Diagnostic Commands Arguments and Options")};
                map = new HashMap();
                for (int i2 = 0; i2 < diagnosticCommands.length; i2++) {
                    String strTransform = transform(diagnosticCommands[i2]);
                    try {
                        Wrapper wrapper = new Wrapper(strTransform, diagnosticCommands[i2], diagnosticCommandInfo[i2]);
                        map.put(strTransform, wrapper);
                        treeSet.add(new MBeanOperationInfo(wrapper.name, wrapper.info.getDescription(), (wrapper.info.getArgumentsInfo() == null || wrapper.info.getArgumentsInfo().isEmpty()) ? null : mBeanParameterInfoArr, strClassName, 2, commandDescriptor(wrapper)));
                    } catch (InstantiationException e2) {
                    }
                }
            } catch (IllegalArgumentException | UnsupportedOperationException e3) {
                map = Collections.EMPTY_MAP;
            }
        }
        this.wrappers = Collections.unmodifiableMap(map);
        HashMap map2 = new HashMap();
        map2.put("immutableInfo", "false");
        map2.put(JMX.INTERFACE_CLASS_NAME_FIELD, "com.sun.management.DiagnosticCommandMBean");
        map2.put(JMX.MXBEAN_FIELD, "false");
        return new MBeanInfo(getClass().getName(), "Diagnostic Commands", null, null, (MBeanOperationInfo[]) treeSet.toArray(new MBeanOperationInfo[treeSet.size()]), getNotificationInfo(), new ImmutableDescriptor(map2));
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        if (!this.isSupported) {
            throw new UnsupportedOperationException();
        }
        if (this.wrappers == null) {
            getMBeanInfo();
        }
        Wrapper wrapper = this.wrappers.get(str);
        if (wrapper != null) {
            if (wrapper.info.getArgumentsInfo().isEmpty() && ((objArr == null || objArr.length == 0) && (strArr == null || strArr.length == 0))) {
                return wrapper.execute(null);
            }
            if (objArr != null && objArr.length == 1 && strArr != null && strArr.length == 1 && strArr[0] != null && strArr[0].compareTo(strArrayClassName) == 0) {
                return wrapper.execute((String[]) objArr[0]);
            }
        }
        throw new ReflectionException(new NoSuchMethodException(str));
    }

    private static String transform(String str) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        boolean z3 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '.' || cCharAt == '_') {
                z2 = false;
                z3 = true;
            } else if (z3) {
                z3 = false;
                sb.append(Character.toUpperCase(cCharAt));
            } else if (z2) {
                sb.append(Character.toLowerCase(cCharAt));
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    private Descriptor commandDescriptor(Wrapper wrapper) throws IllegalArgumentException {
        HashMap map = new HashMap();
        map.put("dcmd.name", wrapper.info.getName());
        map.put("dcmd.description", wrapper.info.getDescription());
        map.put("dcmd.vmImpact", wrapper.info.getImpact());
        map.put("dcmd.permissionClass", wrapper.info.getPermissionClass());
        map.put("dcmd.permissionName", wrapper.info.getPermissionName());
        map.put("dcmd.permissionAction", wrapper.info.getPermissionAction());
        map.put("dcmd.enabled", Boolean.valueOf(wrapper.info.isEnabled()));
        map.put("dcmd.help", executeDiagnosticCommand("help " + wrapper.info.getName()));
        if (wrapper.info.getArgumentsInfo() != null && !wrapper.info.getArgumentsInfo().isEmpty()) {
            HashMap map2 = new HashMap();
            for (DiagnosticCommandArgumentInfo diagnosticCommandArgumentInfo : wrapper.info.getArgumentsInfo()) {
                HashMap map3 = new HashMap();
                map3.put("dcmd.arg.name", diagnosticCommandArgumentInfo.getName());
                map3.put("dcmd.arg.type", diagnosticCommandArgumentInfo.getType());
                map3.put("dcmd.arg.description", diagnosticCommandArgumentInfo.getDescription());
                map3.put("dcmd.arg.isMandatory", Boolean.valueOf(diagnosticCommandArgumentInfo.isMandatory()));
                map3.put("dcmd.arg.isMultiple", Boolean.valueOf(diagnosticCommandArgumentInfo.isMultiple()));
                boolean zIsOption = diagnosticCommandArgumentInfo.isOption();
                map3.put("dcmd.arg.isOption", Boolean.valueOf(zIsOption));
                if (!zIsOption) {
                    map3.put("dcmd.arg.position", Integer.valueOf(diagnosticCommandArgumentInfo.getPosition()));
                } else {
                    map3.put("dcmd.arg.position", -1);
                }
                map2.put(diagnosticCommandArgumentInfo.getName(), new ImmutableDescriptor(map3));
            }
            map.put("dcmd.arguments", new ImmutableDescriptor(map2));
        }
        return new ImmutableDescriptor(map);
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        synchronized (this) {
            if (this.notifInfo == null) {
                this.notifInfo = new MBeanNotificationInfo[1];
                this.notifInfo[0] = new MBeanNotificationInfo(diagFramNotifTypes, notifName, "Diagnostic Framework Notification");
            }
        }
        return (MBeanNotificationInfo[]) this.notifInfo.clone();
    }

    private static long getNextSeqNumber() {
        long j2 = seqNumber + 1;
        seqNumber = j2;
        return j2;
    }

    private void createDiagnosticFrameworkNotification() throws NullPointerException {
        if (!hasListeners()) {
            return;
        }
        ObjectName objectName = null;
        try {
            objectName = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
        } catch (MalformedObjectNameException e2) {
        }
        Notification notification = new Notification("jmx.mbean.info.changed", objectName, getNextSeqNumber());
        notification.setUserData(getMBeanInfo());
        sendNotification(notification);
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public synchronized void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        boolean zHasListeners = hasListeners();
        super.addNotificationListener(notificationListener, notificationFilter, obj);
        boolean zHasListeners2 = hasListeners();
        if (!zHasListeners && zHasListeners2) {
            setNotificationEnabled(true);
        }
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public synchronized void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        boolean zHasListeners = hasListeners();
        super.removeNotificationListener(notificationListener);
        boolean zHasListeners2 = hasListeners();
        if (zHasListeners && !zHasListeners2) {
            setNotificationEnabled(false);
        }
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationEmitter
    public synchronized void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        boolean zHasListeners = hasListeners();
        super.removeNotificationListener(notificationListener, notificationFilter, obj);
        boolean zHasListeners2 = hasListeners();
        if (zHasListeners && !zHasListeners2) {
            setNotificationEnabled(false);
        }
    }
}
