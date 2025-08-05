package javax.management.remote.rmi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import java.util.Set;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.NotificationResult;
import javax.security.auth.Subject;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectionImpl_Stub.class */
public final class RMIConnectionImpl_Stub extends RemoteStub implements RMIConnection {
    private static final long serialVersionUID = 2;
    private static Method $method_addNotificationListener_0;
    private static Method $method_addNotificationListeners_1;
    private static Method $method_close_2;
    private static Method $method_createMBean_3;
    private static Method $method_createMBean_4;
    private static Method $method_createMBean_5;
    private static Method $method_createMBean_6;
    private static Method $method_fetchNotifications_7;
    private static Method $method_getAttribute_8;
    private static Method $method_getAttributes_9;
    private static Method $method_getConnectionId_10;
    private static Method $method_getDefaultDomain_11;
    private static Method $method_getDomains_12;
    private static Method $method_getMBeanCount_13;
    private static Method $method_getMBeanInfo_14;
    private static Method $method_getObjectInstance_15;
    private static Method $method_invoke_16;
    private static Method $method_isInstanceOf_17;
    private static Method $method_isRegistered_18;
    private static Method $method_queryMBeans_19;
    private static Method $method_queryNames_20;
    private static Method $method_removeNotificationListener_21;
    private static Method $method_removeNotificationListener_22;
    private static Method $method_removeNotificationListeners_23;
    private static Method $method_setAttribute_24;
    private static Method $method_setAttributes_25;
    private static Method $method_unregisterMBean_26;
    static Class class$javax$management$remote$rmi$RMIConnection;
    static Class class$javax$management$ObjectName;
    static Class class$java$rmi$MarshalledObject;
    static Class class$javax$security$auth$Subject;
    static Class array$Ljavax$management$ObjectName;
    static Class array$Ljava$rmi$MarshalledObject;
    static Class array$Ljavax$security$auth$Subject;
    static Class class$java$lang$AutoCloseable;
    static Class class$java$lang$String;
    static Class array$Ljava$lang$String;
    static Class array$Ljava$lang$Integer;

    static {
        Class clsClass$;
        Class<?> clsClass$2;
        Class<?> clsClass$3;
        Class<?> clsClass$4;
        Class<?> clsClass$5;
        Class<?> clsClass$6;
        Class clsClass$7;
        Class<?> clsClass$8;
        Class<?> clsClass$9;
        Class<?> clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class<?> clsClass$13;
        Class<?> clsClass$14;
        Class<?> clsClass$15;
        Class<?> clsClass$16;
        Class<?> clsClass$17;
        Class clsClass$18;
        Class<?> clsClass$19;
        Class<?> clsClass$20;
        Class<?> clsClass$21;
        Class<?> clsClass$22;
        Class<?> clsClass$23;
        Class<?> clsClass$24;
        Class clsClass$25;
        Class<?> clsClass$26;
        Class<?> clsClass$27;
        Class<?> clsClass$28;
        Class<?> clsClass$29;
        Class clsClass$30;
        Class<?> clsClass$31;
        Class<?> clsClass$32;
        Class<?> clsClass$33;
        Class clsClass$34;
        Class clsClass$35;
        Class<?> clsClass$36;
        Class<?> clsClass$37;
        Class<?> clsClass$38;
        Class clsClass$39;
        Class<?> clsClass$40;
        Class<?> clsClass$41;
        Class<?> clsClass$42;
        Class clsClass$43;
        Class clsClass$44;
        Class<?> clsClass$45;
        Class clsClass$46;
        Class<?> clsClass$47;
        Class clsClass$48;
        Class<?> clsClass$49;
        Class clsClass$50;
        Class<?> clsClass$51;
        Class<?> clsClass$52;
        Class clsClass$53;
        Class<?> clsClass$54;
        Class<?> clsClass$55;
        Class clsClass$56;
        Class<?> clsClass$57;
        Class<?> clsClass$58;
        Class<?> clsClass$59;
        Class<?> clsClass$60;
        Class<?> clsClass$61;
        Class clsClass$62;
        Class<?> clsClass$63;
        Class<?> clsClass$64;
        Class<?> clsClass$65;
        Class clsClass$66;
        Class<?> clsClass$67;
        Class<?> clsClass$68;
        Class clsClass$69;
        Class<?> clsClass$70;
        Class<?> clsClass$71;
        Class<?> clsClass$72;
        Class clsClass$73;
        Class<?> clsClass$74;
        Class<?> clsClass$75;
        Class<?> clsClass$76;
        Class clsClass$77;
        Class<?> clsClass$78;
        Class<?> clsClass$79;
        Class<?> clsClass$80;
        Class<?> clsClass$81;
        Class<?> clsClass$82;
        Class clsClass$83;
        Class<?> clsClass$84;
        Class<?> clsClass$85;
        Class<?> clsClass$86;
        Class clsClass$87;
        Class<?> clsClass$88;
        Class<?> clsClass$89;
        Class<?> clsClass$90;
        Class clsClass$91;
        Class<?> clsClass$92;
        Class<?> clsClass$93;
        Class<?> clsClass$94;
        Class clsClass$95;
        Class<?> clsClass$96;
        Class<?> clsClass$97;
        Class<?> clsClass$98;
        Class clsClass$99;
        Class<?> clsClass$100;
        Class<?> clsClass$101;
        try {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            Class<?>[] clsArr = new Class[5];
            if (class$javax$management$ObjectName != null) {
                clsClass$2 = class$javax$management$ObjectName;
            } else {
                clsClass$2 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$2;
            }
            clsArr[0] = clsClass$2;
            if (class$javax$management$ObjectName != null) {
                clsClass$3 = class$javax$management$ObjectName;
            } else {
                clsClass$3 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$3;
            }
            clsArr[1] = clsClass$3;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$4 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$4 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$4;
            }
            clsArr[2] = clsClass$4;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$5 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$5 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$5;
            }
            clsArr[3] = clsClass$5;
            if (class$javax$security$auth$Subject != null) {
                clsClass$6 = class$javax$security$auth$Subject;
            } else {
                clsClass$6 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$6;
            }
            clsArr[4] = clsClass$6;
            $method_addNotificationListener_0 = clsClass$.getMethod("addNotificationListener", clsArr);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$7 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$7 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$7;
            }
            Class<?>[] clsArr2 = new Class[3];
            if (array$Ljavax$management$ObjectName != null) {
                clsClass$8 = array$Ljavax$management$ObjectName;
            } else {
                clsClass$8 = class$("[Ljavax.management.ObjectName;");
                array$Ljavax$management$ObjectName = clsClass$8;
            }
            clsArr2[0] = clsClass$8;
            if (array$Ljava$rmi$MarshalledObject != null) {
                clsClass$9 = array$Ljava$rmi$MarshalledObject;
            } else {
                clsClass$9 = class$("[Ljava.rmi.MarshalledObject;");
                array$Ljava$rmi$MarshalledObject = clsClass$9;
            }
            clsArr2[1] = clsClass$9;
            if (array$Ljavax$security$auth$Subject != null) {
                clsClass$10 = array$Ljavax$security$auth$Subject;
            } else {
                clsClass$10 = class$("[Ljavax.security.auth.Subject;");
                array$Ljavax$security$auth$Subject = clsClass$10;
            }
            clsArr2[2] = clsClass$10;
            $method_addNotificationListeners_1 = clsClass$7.getMethod("addNotificationListeners", clsArr2);
            if (class$java$lang$AutoCloseable != null) {
                clsClass$11 = class$java$lang$AutoCloseable;
            } else {
                clsClass$11 = class$("java.lang.AutoCloseable");
                class$java$lang$AutoCloseable = clsClass$11;
            }
            $method_close_2 = clsClass$11.getMethod("close", new Class[0]);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$12 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$12 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$12;
            }
            Class<?>[] clsArr3 = new Class[5];
            if (class$java$lang$String != null) {
                clsClass$13 = class$java$lang$String;
            } else {
                clsClass$13 = class$("java.lang.String");
                class$java$lang$String = clsClass$13;
            }
            clsArr3[0] = clsClass$13;
            if (class$javax$management$ObjectName != null) {
                clsClass$14 = class$javax$management$ObjectName;
            } else {
                clsClass$14 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$14;
            }
            clsArr3[1] = clsClass$14;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$15 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$15 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$15;
            }
            clsArr3[2] = clsClass$15;
            if (array$Ljava$lang$String != null) {
                clsClass$16 = array$Ljava$lang$String;
            } else {
                clsClass$16 = class$("[Ljava.lang.String;");
                array$Ljava$lang$String = clsClass$16;
            }
            clsArr3[3] = clsClass$16;
            if (class$javax$security$auth$Subject != null) {
                clsClass$17 = class$javax$security$auth$Subject;
            } else {
                clsClass$17 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$17;
            }
            clsArr3[4] = clsClass$17;
            $method_createMBean_3 = clsClass$12.getMethod("createMBean", clsArr3);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$18 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$18 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$18;
            }
            Class<?>[] clsArr4 = new Class[6];
            if (class$java$lang$String != null) {
                clsClass$19 = class$java$lang$String;
            } else {
                clsClass$19 = class$("java.lang.String");
                class$java$lang$String = clsClass$19;
            }
            clsArr4[0] = clsClass$19;
            if (class$javax$management$ObjectName != null) {
                clsClass$20 = class$javax$management$ObjectName;
            } else {
                clsClass$20 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$20;
            }
            clsArr4[1] = clsClass$20;
            if (class$javax$management$ObjectName != null) {
                clsClass$21 = class$javax$management$ObjectName;
            } else {
                clsClass$21 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$21;
            }
            clsArr4[2] = clsClass$21;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$22 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$22 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$22;
            }
            clsArr4[3] = clsClass$22;
            if (array$Ljava$lang$String != null) {
                clsClass$23 = array$Ljava$lang$String;
            } else {
                clsClass$23 = class$("[Ljava.lang.String;");
                array$Ljava$lang$String = clsClass$23;
            }
            clsArr4[4] = clsClass$23;
            if (class$javax$security$auth$Subject != null) {
                clsClass$24 = class$javax$security$auth$Subject;
            } else {
                clsClass$24 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$24;
            }
            clsArr4[5] = clsClass$24;
            $method_createMBean_4 = clsClass$18.getMethod("createMBean", clsArr4);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$25 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$25 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$25;
            }
            Class<?>[] clsArr5 = new Class[4];
            if (class$java$lang$String != null) {
                clsClass$26 = class$java$lang$String;
            } else {
                clsClass$26 = class$("java.lang.String");
                class$java$lang$String = clsClass$26;
            }
            clsArr5[0] = clsClass$26;
            if (class$javax$management$ObjectName != null) {
                clsClass$27 = class$javax$management$ObjectName;
            } else {
                clsClass$27 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$27;
            }
            clsArr5[1] = clsClass$27;
            if (class$javax$management$ObjectName != null) {
                clsClass$28 = class$javax$management$ObjectName;
            } else {
                clsClass$28 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$28;
            }
            clsArr5[2] = clsClass$28;
            if (class$javax$security$auth$Subject != null) {
                clsClass$29 = class$javax$security$auth$Subject;
            } else {
                clsClass$29 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$29;
            }
            clsArr5[3] = clsClass$29;
            $method_createMBean_5 = clsClass$25.getMethod("createMBean", clsArr5);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$30 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$30 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$30;
            }
            Class<?>[] clsArr6 = new Class[3];
            if (class$java$lang$String != null) {
                clsClass$31 = class$java$lang$String;
            } else {
                clsClass$31 = class$("java.lang.String");
                class$java$lang$String = clsClass$31;
            }
            clsArr6[0] = clsClass$31;
            if (class$javax$management$ObjectName != null) {
                clsClass$32 = class$javax$management$ObjectName;
            } else {
                clsClass$32 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$32;
            }
            clsArr6[1] = clsClass$32;
            if (class$javax$security$auth$Subject != null) {
                clsClass$33 = class$javax$security$auth$Subject;
            } else {
                clsClass$33 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$33;
            }
            clsArr6[2] = clsClass$33;
            $method_createMBean_6 = clsClass$30.getMethod("createMBean", clsArr6);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$34 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$34 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$34;
            }
            $method_fetchNotifications_7 = clsClass$34.getMethod("fetchNotifications", Long.TYPE, Integer.TYPE, Long.TYPE);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$35 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$35 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$35;
            }
            Class<?>[] clsArr7 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$36 = class$javax$management$ObjectName;
            } else {
                clsClass$36 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$36;
            }
            clsArr7[0] = clsClass$36;
            if (class$java$lang$String != null) {
                clsClass$37 = class$java$lang$String;
            } else {
                clsClass$37 = class$("java.lang.String");
                class$java$lang$String = clsClass$37;
            }
            clsArr7[1] = clsClass$37;
            if (class$javax$security$auth$Subject != null) {
                clsClass$38 = class$javax$security$auth$Subject;
            } else {
                clsClass$38 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$38;
            }
            clsArr7[2] = clsClass$38;
            $method_getAttribute_8 = clsClass$35.getMethod("getAttribute", clsArr7);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$39 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$39 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$39;
            }
            Class<?>[] clsArr8 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$40 = class$javax$management$ObjectName;
            } else {
                clsClass$40 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$40;
            }
            clsArr8[0] = clsClass$40;
            if (array$Ljava$lang$String != null) {
                clsClass$41 = array$Ljava$lang$String;
            } else {
                clsClass$41 = class$("[Ljava.lang.String;");
                array$Ljava$lang$String = clsClass$41;
            }
            clsArr8[1] = clsClass$41;
            if (class$javax$security$auth$Subject != null) {
                clsClass$42 = class$javax$security$auth$Subject;
            } else {
                clsClass$42 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$42;
            }
            clsArr8[2] = clsClass$42;
            $method_getAttributes_9 = clsClass$39.getMethod("getAttributes", clsArr8);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$43 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$43 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$43;
            }
            $method_getConnectionId_10 = clsClass$43.getMethod("getConnectionId", new Class[0]);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$44 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$44 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$44;
            }
            Class<?>[] clsArr9 = new Class[1];
            if (class$javax$security$auth$Subject != null) {
                clsClass$45 = class$javax$security$auth$Subject;
            } else {
                clsClass$45 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$45;
            }
            clsArr9[0] = clsClass$45;
            $method_getDefaultDomain_11 = clsClass$44.getMethod("getDefaultDomain", clsArr9);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$46 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$46 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$46;
            }
            Class<?>[] clsArr10 = new Class[1];
            if (class$javax$security$auth$Subject != null) {
                clsClass$47 = class$javax$security$auth$Subject;
            } else {
                clsClass$47 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$47;
            }
            clsArr10[0] = clsClass$47;
            $method_getDomains_12 = clsClass$46.getMethod("getDomains", clsArr10);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$48 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$48 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$48;
            }
            Class<?>[] clsArr11 = new Class[1];
            if (class$javax$security$auth$Subject != null) {
                clsClass$49 = class$javax$security$auth$Subject;
            } else {
                clsClass$49 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$49;
            }
            clsArr11[0] = clsClass$49;
            $method_getMBeanCount_13 = clsClass$48.getMethod("getMBeanCount", clsArr11);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$50 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$50 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$50;
            }
            Class<?>[] clsArr12 = new Class[2];
            if (class$javax$management$ObjectName != null) {
                clsClass$51 = class$javax$management$ObjectName;
            } else {
                clsClass$51 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$51;
            }
            clsArr12[0] = clsClass$51;
            if (class$javax$security$auth$Subject != null) {
                clsClass$52 = class$javax$security$auth$Subject;
            } else {
                clsClass$52 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$52;
            }
            clsArr12[1] = clsClass$52;
            $method_getMBeanInfo_14 = clsClass$50.getMethod("getMBeanInfo", clsArr12);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$53 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$53 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$53;
            }
            Class<?>[] clsArr13 = new Class[2];
            if (class$javax$management$ObjectName != null) {
                clsClass$54 = class$javax$management$ObjectName;
            } else {
                clsClass$54 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$54;
            }
            clsArr13[0] = clsClass$54;
            if (class$javax$security$auth$Subject != null) {
                clsClass$55 = class$javax$security$auth$Subject;
            } else {
                clsClass$55 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$55;
            }
            clsArr13[1] = clsClass$55;
            $method_getObjectInstance_15 = clsClass$53.getMethod("getObjectInstance", clsArr13);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$56 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$56 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$56;
            }
            Class<?>[] clsArr14 = new Class[5];
            if (class$javax$management$ObjectName != null) {
                clsClass$57 = class$javax$management$ObjectName;
            } else {
                clsClass$57 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$57;
            }
            clsArr14[0] = clsClass$57;
            if (class$java$lang$String != null) {
                clsClass$58 = class$java$lang$String;
            } else {
                clsClass$58 = class$("java.lang.String");
                class$java$lang$String = clsClass$58;
            }
            clsArr14[1] = clsClass$58;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$59 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$59 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$59;
            }
            clsArr14[2] = clsClass$59;
            if (array$Ljava$lang$String != null) {
                clsClass$60 = array$Ljava$lang$String;
            } else {
                clsClass$60 = class$("[Ljava.lang.String;");
                array$Ljava$lang$String = clsClass$60;
            }
            clsArr14[3] = clsClass$60;
            if (class$javax$security$auth$Subject != null) {
                clsClass$61 = class$javax$security$auth$Subject;
            } else {
                clsClass$61 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$61;
            }
            clsArr14[4] = clsClass$61;
            $method_invoke_16 = clsClass$56.getMethod("invoke", clsArr14);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$62 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$62 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$62;
            }
            Class<?>[] clsArr15 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$63 = class$javax$management$ObjectName;
            } else {
                clsClass$63 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$63;
            }
            clsArr15[0] = clsClass$63;
            if (class$java$lang$String != null) {
                clsClass$64 = class$java$lang$String;
            } else {
                clsClass$64 = class$("java.lang.String");
                class$java$lang$String = clsClass$64;
            }
            clsArr15[1] = clsClass$64;
            if (class$javax$security$auth$Subject != null) {
                clsClass$65 = class$javax$security$auth$Subject;
            } else {
                clsClass$65 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$65;
            }
            clsArr15[2] = clsClass$65;
            $method_isInstanceOf_17 = clsClass$62.getMethod("isInstanceOf", clsArr15);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$66 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$66 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$66;
            }
            Class<?>[] clsArr16 = new Class[2];
            if (class$javax$management$ObjectName != null) {
                clsClass$67 = class$javax$management$ObjectName;
            } else {
                clsClass$67 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$67;
            }
            clsArr16[0] = clsClass$67;
            if (class$javax$security$auth$Subject != null) {
                clsClass$68 = class$javax$security$auth$Subject;
            } else {
                clsClass$68 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$68;
            }
            clsArr16[1] = clsClass$68;
            $method_isRegistered_18 = clsClass$66.getMethod("isRegistered", clsArr16);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$69 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$69 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$69;
            }
            Class<?>[] clsArr17 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$70 = class$javax$management$ObjectName;
            } else {
                clsClass$70 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$70;
            }
            clsArr17[0] = clsClass$70;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$71 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$71 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$71;
            }
            clsArr17[1] = clsClass$71;
            if (class$javax$security$auth$Subject != null) {
                clsClass$72 = class$javax$security$auth$Subject;
            } else {
                clsClass$72 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$72;
            }
            clsArr17[2] = clsClass$72;
            $method_queryMBeans_19 = clsClass$69.getMethod("queryMBeans", clsArr17);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$73 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$73 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$73;
            }
            Class<?>[] clsArr18 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$74 = class$javax$management$ObjectName;
            } else {
                clsClass$74 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$74;
            }
            clsArr18[0] = clsClass$74;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$75 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$75 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$75;
            }
            clsArr18[1] = clsClass$75;
            if (class$javax$security$auth$Subject != null) {
                clsClass$76 = class$javax$security$auth$Subject;
            } else {
                clsClass$76 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$76;
            }
            clsArr18[2] = clsClass$76;
            $method_queryNames_20 = clsClass$73.getMethod("queryNames", clsArr18);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$77 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$77 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$77;
            }
            Class<?>[] clsArr19 = new Class[5];
            if (class$javax$management$ObjectName != null) {
                clsClass$78 = class$javax$management$ObjectName;
            } else {
                clsClass$78 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$78;
            }
            clsArr19[0] = clsClass$78;
            if (class$javax$management$ObjectName != null) {
                clsClass$79 = class$javax$management$ObjectName;
            } else {
                clsClass$79 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$79;
            }
            clsArr19[1] = clsClass$79;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$80 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$80 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$80;
            }
            clsArr19[2] = clsClass$80;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$81 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$81 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$81;
            }
            clsArr19[3] = clsClass$81;
            if (class$javax$security$auth$Subject != null) {
                clsClass$82 = class$javax$security$auth$Subject;
            } else {
                clsClass$82 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$82;
            }
            clsArr19[4] = clsClass$82;
            $method_removeNotificationListener_21 = clsClass$77.getMethod("removeNotificationListener", clsArr19);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$83 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$83 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$83;
            }
            Class<?>[] clsArr20 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$84 = class$javax$management$ObjectName;
            } else {
                clsClass$84 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$84;
            }
            clsArr20[0] = clsClass$84;
            if (class$javax$management$ObjectName != null) {
                clsClass$85 = class$javax$management$ObjectName;
            } else {
                clsClass$85 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$85;
            }
            clsArr20[1] = clsClass$85;
            if (class$javax$security$auth$Subject != null) {
                clsClass$86 = class$javax$security$auth$Subject;
            } else {
                clsClass$86 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$86;
            }
            clsArr20[2] = clsClass$86;
            $method_removeNotificationListener_22 = clsClass$83.getMethod("removeNotificationListener", clsArr20);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$87 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$87 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$87;
            }
            Class<?>[] clsArr21 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$88 = class$javax$management$ObjectName;
            } else {
                clsClass$88 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$88;
            }
            clsArr21[0] = clsClass$88;
            if (array$Ljava$lang$Integer != null) {
                clsClass$89 = array$Ljava$lang$Integer;
            } else {
                clsClass$89 = class$("[Ljava.lang.Integer;");
                array$Ljava$lang$Integer = clsClass$89;
            }
            clsArr21[1] = clsClass$89;
            if (class$javax$security$auth$Subject != null) {
                clsClass$90 = class$javax$security$auth$Subject;
            } else {
                clsClass$90 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$90;
            }
            clsArr21[2] = clsClass$90;
            $method_removeNotificationListeners_23 = clsClass$87.getMethod("removeNotificationListeners", clsArr21);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$91 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$91 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$91;
            }
            Class<?>[] clsArr22 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$92 = class$javax$management$ObjectName;
            } else {
                clsClass$92 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$92;
            }
            clsArr22[0] = clsClass$92;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$93 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$93 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$93;
            }
            clsArr22[1] = clsClass$93;
            if (class$javax$security$auth$Subject != null) {
                clsClass$94 = class$javax$security$auth$Subject;
            } else {
                clsClass$94 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$94;
            }
            clsArr22[2] = clsClass$94;
            $method_setAttribute_24 = clsClass$91.getMethod("setAttribute", clsArr22);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$95 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$95 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$95;
            }
            Class<?>[] clsArr23 = new Class[3];
            if (class$javax$management$ObjectName != null) {
                clsClass$96 = class$javax$management$ObjectName;
            } else {
                clsClass$96 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$96;
            }
            clsArr23[0] = clsClass$96;
            if (class$java$rmi$MarshalledObject != null) {
                clsClass$97 = class$java$rmi$MarshalledObject;
            } else {
                clsClass$97 = class$("java.rmi.MarshalledObject");
                class$java$rmi$MarshalledObject = clsClass$97;
            }
            clsArr23[1] = clsClass$97;
            if (class$javax$security$auth$Subject != null) {
                clsClass$98 = class$javax$security$auth$Subject;
            } else {
                clsClass$98 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$98;
            }
            clsArr23[2] = clsClass$98;
            $method_setAttributes_25 = clsClass$95.getMethod("setAttributes", clsArr23);
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$99 = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$99 = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$99;
            }
            Class<?>[] clsArr24 = new Class[2];
            if (class$javax$management$ObjectName != null) {
                clsClass$100 = class$javax$management$ObjectName;
            } else {
                clsClass$100 = class$("javax.management.ObjectName");
                class$javax$management$ObjectName = clsClass$100;
            }
            clsArr24[0] = clsClass$100;
            if (class$javax$security$auth$Subject != null) {
                clsClass$101 = class$javax$security$auth$Subject;
            } else {
                clsClass$101 = class$("javax.security.auth.Subject");
                class$javax$security$auth$Subject = clsClass$101;
            }
            clsArr24[1] = clsClass$101;
            $method_unregisterMBean_26 = clsClass$99.getMethod("unregisterMBean", clsArr24);
        } catch (NoSuchMethodException unused) {
            throw new NoSuchMethodError("stub class initialization failed");
        }
    }

    public RMIConnectionImpl_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws IOException, InstanceNotFoundException {
        try {
            this.ref.invoke(this, $method_addNotificationListener_0, new Object[]{objectName, objectName2, marshalledObject, marshalledObject2, subject}, -8578317696269497109L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Integer[] addNotificationListeners(ObjectName[] objectNameArr, MarshalledObject[] marshalledObjectArr, Subject[] subjectArr) throws IOException, InstanceNotFoundException {
        try {
            return (Integer[]) this.ref.invoke(this, $method_addNotificationListeners_1, new Object[]{objectNameArr, marshalledObjectArr, subjectArr}, -5321691879380783377L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.ref.invoke(this, $method_close_2, null, -4742752445160157748L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        try {
            return (ObjectInstance) this.ref.invoke(this, $method_createMBean_3, new Object[]{str, objectName, marshalledObject, strArr, subject}, 4867822117947806114L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceAlreadyExistsException e4) {
            throw e4;
        } catch (MBeanException e5) {
            throw e5;
        } catch (NotCompliantMBeanException e6) {
            throw e6;
        } catch (ReflectionException e7) {
            throw e7;
        } catch (Exception e8) {
            throw new UnexpectedException("undeclared checked exception", e8);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            return (ObjectInstance) this.ref.invoke(this, $method_createMBean_4, new Object[]{str, objectName, objectName2, marshalledObject, strArr, subject}, -6604955182088909937L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceAlreadyExistsException e4) {
            throw e4;
        } catch (InstanceNotFoundException e5) {
            throw e5;
        } catch (MBeanException e6) {
            throw e6;
        } catch (NotCompliantMBeanException e7) {
            throw e7;
        } catch (ReflectionException e8) {
            throw e8;
        } catch (Exception e9) {
            throw new UnexpectedException("undeclared checked exception", e9);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            return (ObjectInstance) this.ref.invoke(this, $method_createMBean_5, new Object[]{str, objectName, objectName2, subject}, -8679469989872508324L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceAlreadyExistsException e4) {
            throw e4;
        } catch (InstanceNotFoundException e5) {
            throw e5;
        } catch (MBeanException e6) {
            throw e6;
        } catch (NotCompliantMBeanException e7) {
            throw e7;
        } catch (ReflectionException e8) {
            throw e8;
        } catch (Exception e9) {
            throw new UnexpectedException("undeclared checked exception", e9);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        try {
            return (ObjectInstance) this.ref.invoke(this, $method_createMBean_6, new Object[]{str, objectName, subject}, 2510753813974665446L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceAlreadyExistsException e4) {
            throw e4;
        } catch (MBeanException e5) {
            throw e5;
        } catch (NotCompliantMBeanException e6) {
            throw e6;
        } catch (ReflectionException e7) {
            throw e7;
        } catch (Exception e8) {
            throw new UnexpectedException("undeclared checked exception", e8);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public NotificationResult fetchNotifications(long j2, int i2, long j3) throws IOException {
        try {
            return (NotificationResult) this.ref.invoke(this, $method_fetchNotifications_7, new Object[]{new Long(j2), new Integer(i2), new Long(j3)}, -5037523307973544478L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object getAttribute(ObjectName objectName, String str, Subject subject) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            return this.ref.invoke(this, $method_getAttribute_8, new Object[]{objectName, str, subject}, -1089783104982388203L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (AttributeNotFoundException e4) {
            throw e4;
        } catch (InstanceNotFoundException e5) {
            throw e5;
        } catch (MBeanException e6) {
            throw e6;
        } catch (ReflectionException e7) {
            throw e7;
        } catch (Exception e8) {
            throw new UnexpectedException("undeclared checked exception", e8);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        try {
            return (AttributeList) this.ref.invoke(this, $method_getAttributes_9, new Object[]{objectName, strArr, subject}, 6285293806596348999L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (ReflectionException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getConnectionId() throws IOException {
        try {
            return (String) this.ref.invoke(this, $method_getConnectionId_10, null, -67907180346059933L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getDefaultDomain(Subject subject) throws IOException {
        try {
            return (String) this.ref.invoke(this, $method_getDefaultDomain_11, new Object[]{subject}, 6047668923998658472L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String[] getDomains(Subject subject) throws IOException {
        try {
            return (String[]) this.ref.invoke(this, $method_getDomains_12, new Object[]{subject}, -6662314179953625551L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Integer getMBeanCount(Subject subject) throws IOException {
        try {
            return (Integer) this.ref.invoke(this, $method_getMBeanCount_13, new Object[]{subject}, -2042362057335820635L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName, Subject subject) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            return (MBeanInfo) this.ref.invoke(this, $method_getMBeanInfo_14, new Object[]{objectName, subject}, -7404813916326233354L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (IntrospectionException e5) {
            throw e5;
        } catch (ReflectionException e6) {
            throw e6;
        } catch (Exception e7) {
            throw new UnexpectedException("undeclared checked exception", e7);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance getObjectInstance(ObjectName objectName, Subject subject) throws IOException, InstanceNotFoundException {
        try {
            return (ObjectInstance) this.ref.invoke(this, $method_getObjectInstance_15, new Object[]{objectName, subject}, 6950095694996159938L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object invoke(ObjectName objectName, String str, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            return this.ref.invoke(this, $method_invoke_16, new Object[]{objectName, str, marshalledObject, strArr, subject}, 1434350937885235744L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (MBeanException e5) {
            throw e5;
        } catch (ReflectionException e6) {
            throw e6;
        } catch (Exception e7) {
            throw new UnexpectedException("undeclared checked exception", e7);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isInstanceOf(ObjectName objectName, String str, Subject subject) throws IOException, InstanceNotFoundException {
        try {
            return ((Boolean) this.ref.invoke(this, $method_isInstanceOf_17, new Object[]{objectName, str, subject}, -2147516868461740814L)).booleanValue();
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isRegistered(ObjectName objectName, Subject subject) throws IOException {
        try {
            return ((Boolean) this.ref.invoke(this, $method_isRegistered_18, new Object[]{objectName, subject}, 8325683335228268564L)).booleanValue();
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set queryMBeans(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        try {
            return (Set) this.ref.invoke(this, $method_queryMBeans_19, new Object[]{objectName, marshalledObject, subject}, 2915881009400597976L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set queryNames(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        try {
            return (Set) this.ref.invoke(this, $method_queryNames_20, new Object[]{objectName, marshalledObject, subject}, 9152567528369059802L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        try {
            this.ref.invoke(this, $method_removeNotificationListener_21, new Object[]{objectName, objectName2, marshalledObject, marshalledObject2, subject}, 2578029900065214857L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (ListenerNotFoundException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        try {
            this.ref.invoke(this, $method_removeNotificationListener_22, new Object[]{objectName, objectName2, subject}, 6604721169198089513L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (ListenerNotFoundException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListeners(ObjectName objectName, Integer[] numArr, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        try {
            this.ref.invoke(this, $method_removeNotificationListeners_23, new Object[]{objectName, numArr, subject}, 2549120024456183446L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (ListenerNotFoundException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void setAttribute(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        try {
            this.ref.invoke(this, $method_setAttribute_24, new Object[]{objectName, marshalledObject, subject}, 6738606893952597516L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (AttributeNotFoundException e4) {
            throw e4;
        } catch (InstanceNotFoundException e5) {
            throw e5;
        } catch (InvalidAttributeValueException e6) {
            throw e6;
        } catch (MBeanException e7) {
            throw e7;
        } catch (ReflectionException e8) {
            throw e8;
        } catch (Exception e9) {
            throw new UnexpectedException("undeclared checked exception", e9);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList setAttributes(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        try {
            return (AttributeList) this.ref.invoke(this, $method_setAttributes_25, new Object[]{objectName, marshalledObject, subject}, -230470228399681820L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (ReflectionException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void unregisterMBean(ObjectName objectName, Subject subject) throws MBeanRegistrationException, IOException, InstanceNotFoundException {
        try {
            this.ref.invoke(this, $method_unregisterMBean_26, new Object[]{objectName, subject}, -159498580868721452L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (InstanceNotFoundException e4) {
            throw e4;
        } catch (MBeanRegistrationException e5) {
            throw e5;
        } catch (Exception e6) {
            throw new UnexpectedException("undeclared checked exception", e6);
        }
    }
}
