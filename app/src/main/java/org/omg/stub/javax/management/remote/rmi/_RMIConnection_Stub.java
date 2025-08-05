package org.omg.stub.javax.management.remote.rmi;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.SerializablePermission;
import java.rmi.MarshalledObject;
import java.rmi.UnexpectedException;
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
import javax.management.remote.rmi.RMIConnection;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Util;
import javax.security.auth.Subject;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:org/omg/stub/javax/management/remote/rmi/_RMIConnection_Stub.class */
public class _RMIConnection_Stub extends Stub implements RMIConnection {
    private static final String[] _type_ids = {"RMI:javax.management.remote.rmi.RMIConnection:0000000000000000"};
    private transient boolean _instantiated;
    static Class class$java$io$IOException;
    static Class class$javax$management$remote$rmi$RMIConnection;
    static Class class$java$lang$String;
    static Class class$javax$management$ObjectName;
    static Class class$javax$security$auth$Subject;
    static Class class$javax$management$ObjectInstance;
    static Class class$javax$management$ReflectionException;
    static Class class$javax$management$InstanceAlreadyExistsException;
    static Class class$javax$management$MBeanRegistrationException;
    static Class class$javax$management$MBeanException;
    static Class class$javax$management$NotCompliantMBeanException;
    static Class class$javax$management$InstanceNotFoundException;
    static Class class$java$rmi$MarshalledObject;
    static Class array$Ljava$lang$String;
    static Class class$java$util$Set;
    static Class class$java$lang$Integer;
    static Class class$javax$management$AttributeNotFoundException;
    static Class class$javax$management$AttributeList;
    static Class class$javax$management$InvalidAttributeValueException;
    static Class class$javax$management$MBeanInfo;
    static Class class$javax$management$IntrospectionException;
    static Class class$javax$management$ListenerNotFoundException;
    static Class array$Ljavax$management$ObjectName;
    static Class array$Ljava$rmi$MarshalledObject;
    static Class array$Ljavax$security$auth$Subject;
    static Class array$Ljava$lang$Integer;
    static Class class$javax$management$remote$NotificationResult;

    public _RMIConnection_Stub() {
        this(checkPermission());
        this._instantiated = true;
    }

    private _RMIConnection_Stub(Void r4) {
        this._instantiated = false;
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void addNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("addNotificationListener", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                addNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, objectName2, marshalledObject, marshalledObject2, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).addNotificationListener((ObjectName) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (MarshalledObject) objArrCopyObjects[2], (MarshalledObject) objArrCopyObjects[3], (Subject) objArrCopyObjects[4]);
                    return;
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (!(th2 instanceof IOException)) {
                        throw Util.wrapException(th2);
                    }
                    throw ((IOException) th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("addNotificationListener", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$4 = class$javax$management$ObjectName;
                    } else {
                        clsClass$4 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$4;
                    }
                    outputStream.write_value(objectName, clsClass$4);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName2, clsClass$5);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$6 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$6 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$6;
                    }
                    outputStream.write_value(marshalledObject, clsClass$6);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$7 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$7 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$7;
                    }
                    outputStream.write_value(marshalledObject2, clsClass$7);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$8 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$8 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$8;
                    }
                    outputStream.write_value(subject, clsClass$8);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    addNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Integer[] addNotificationListeners(ObjectName[] objectNameArr, MarshalledObject[] marshalledObjectArr, Subject[] subjectArr) throws IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("addNotificationListeners", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
                }
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectNameArr, marshalledObjectArr, subjectArr}, _orb());
                    return (Integer[]) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).addNotificationListeners((ObjectName[]) objArrCopyObjects[0], (MarshalledObject[]) objArrCopyObjects[1], (Subject[]) objArrCopyObjects[2]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("addNotificationListeners", true);
                    Serializable serializableCast_array = cast_array(objectNameArr);
                    if (array$Ljavax$management$ObjectName != null) {
                        clsClass$4 = array$Ljavax$management$ObjectName;
                    } else {
                        clsClass$4 = class$("[Ljavax.management.ObjectName;");
                        array$Ljavax$management$ObjectName = clsClass$4;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$4);
                    Serializable serializableCast_array2 = cast_array(marshalledObjectArr);
                    if (array$Ljava$rmi$MarshalledObject != null) {
                        clsClass$5 = array$Ljava$rmi$MarshalledObject;
                    } else {
                        clsClass$5 = class$("[Ljava.rmi.MarshalledObject;");
                        array$Ljava$rmi$MarshalledObject = clsClass$5;
                    }
                    outputStream.write_value(serializableCast_array2, clsClass$5);
                    Serializable serializableCast_array3 = cast_array(subjectArr);
                    if (array$Ljavax$security$auth$Subject != null) {
                        clsClass$6 = array$Ljavax$security$auth$Subject;
                    } else {
                        clsClass$6 = class$("[Ljavax.security.auth.Subject;");
                        array$Ljavax$security$auth$Subject = clsClass$6;
                    }
                    outputStream.write_value(serializableCast_array3, clsClass$6);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (array$Ljava$lang$Integer != null) {
                        clsClass$7 = array$Ljava$lang$Integer;
                    } else {
                        clsClass$7 = class$("[Ljava.lang.Integer;");
                        array$Ljava$lang$Integer = clsClass$7;
                    }
                    return (Integer[]) inputStream.read_value(clsClass$7);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return addNotificationListeners(objectNameArr, marshalledObjectArr, subjectArr);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    private Serializable cast_array(Object obj) {
        return (Serializable) obj;
    }

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
        return null;
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
        Class clsClass$;
        Class clsClass$2;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("close", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                close();
                return;
            }
            try {
                try {
                    ((RMIConnection) servantObject_servant_preinvoke.servant).close();
                    return;
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (!(th2 instanceof IOException)) {
                    throw Util.wrapException(th2);
                }
                throw ((IOException) th2);
            }
        }
        try {
            try {
                try {
                    _invoke(_request("close", true));
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    close();
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return createMBean(str, objectName, marshalledObject, strArr, subject);
                }
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{str, objectName, marshalledObject, strArr, subject}, _orb());
                    return (ObjectInstance) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).createMBean((String) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (MarshalledObject) objArrCopyObjects[2], (String[]) objArrCopyObjects[3], (Subject) objArrCopyObjects[4]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof InstanceAlreadyExistsException) {
                        throw ((InstanceAlreadyExistsException) th2);
                    }
                    if (th2 instanceof MBeanRegistrationException) {
                        throw ((MBeanRegistrationException) th2);
                    }
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof NotCompliantMBeanException) {
                        throw ((NotCompliantMBeanException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", true);
                    if (class$java$lang$String != null) {
                        clsClass$8 = class$java$lang$String;
                    } else {
                        clsClass$8 = class$("java.lang.String");
                        class$java$lang$String = clsClass$8;
                    }
                    outputStream.write_value(str, clsClass$8);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$9 = class$javax$management$ObjectName;
                    } else {
                        clsClass$9 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$9;
                    }
                    outputStream.write_value(objectName, clsClass$9);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$10 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$10 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$10;
                    }
                    outputStream.write_value(marshalledObject, clsClass$10);
                    Serializable serializableCast_array = cast_array(strArr);
                    if (array$Ljava$lang$String != null) {
                        clsClass$11 = array$Ljava$lang$String;
                    } else {
                        clsClass$11 = class$("[Ljava.lang.String;");
                        array$Ljava$lang$String = clsClass$11;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$11);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$12 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$12 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$12;
                    }
                    outputStream.write_value(subject, clsClass$12);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$ObjectInstance != null) {
                        clsClass$13 = class$javax$management$ObjectInstance;
                    } else {
                        clsClass$13 = class$("javax.management.ObjectInstance");
                        class$javax$management$ObjectInstance = clsClass$13;
                    }
                    return (ObjectInstance) inputStream.read_value(clsClass$13);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$7 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$7 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$7;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$7));
                    }
                    if (str2.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
                        if (class$javax$management$InstanceAlreadyExistsException != null) {
                            clsClass$6 = class$javax$management$InstanceAlreadyExistsException;
                        } else {
                            clsClass$6 = class$("javax.management.InstanceAlreadyExistsException");
                            class$javax$management$InstanceAlreadyExistsException = clsClass$6;
                        }
                        throw ((InstanceAlreadyExistsException) inputStream2.read_value(clsClass$6));
                    }
                    if (str2.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
                        if (class$javax$management$MBeanRegistrationException != null) {
                            clsClass$5 = class$javax$management$MBeanRegistrationException;
                        } else {
                            clsClass$5 = class$("javax.management.MBeanRegistrationException");
                            class$javax$management$MBeanRegistrationException = clsClass$5;
                        }
                        throw ((MBeanRegistrationException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$4 = class$javax$management$MBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$4;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
                        if (class$javax$management$NotCompliantMBeanException != null) {
                            clsClass$3 = class$javax$management$NotCompliantMBeanException;
                        } else {
                            clsClass$3 = class$("javax.management.NotCompliantMBeanException");
                            class$javax$management$NotCompliantMBeanException = clsClass$3;
                        }
                        throw ((NotCompliantMBeanException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return createMBean(str, objectName, marshalledObject, strArr, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        Class clsClass$14;
        Class clsClass$15;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return createMBean(str, objectName, objectName2, marshalledObject, strArr, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{str, objectName, objectName2, marshalledObject, strArr, subject}, _orb());
                    return (ObjectInstance) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).createMBean((String) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (ObjectName) objArrCopyObjects[2], (MarshalledObject) objArrCopyObjects[3], (String[]) objArrCopyObjects[4], (Subject) objArrCopyObjects[5]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof InstanceAlreadyExistsException) {
                        throw ((InstanceAlreadyExistsException) th2);
                    }
                    if (th2 instanceof MBeanRegistrationException) {
                        throw ((MBeanRegistrationException) th2);
                    }
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof NotCompliantMBeanException) {
                        throw ((NotCompliantMBeanException) th2);
                    }
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject", true);
                    if (class$java$lang$String != null) {
                        clsClass$9 = class$java$lang$String;
                    } else {
                        clsClass$9 = class$("java.lang.String");
                        class$java$lang$String = clsClass$9;
                    }
                    outputStream.write_value(str, clsClass$9);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$10 = class$javax$management$ObjectName;
                    } else {
                        clsClass$10 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$10;
                    }
                    outputStream.write_value(objectName, clsClass$10);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$11 = class$javax$management$ObjectName;
                    } else {
                        clsClass$11 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$11;
                    }
                    outputStream.write_value(objectName2, clsClass$11);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$12 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$12 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$12;
                    }
                    outputStream.write_value(marshalledObject, clsClass$12);
                    Serializable serializableCast_array = cast_array(strArr);
                    if (array$Ljava$lang$String != null) {
                        clsClass$13 = array$Ljava$lang$String;
                    } else {
                        clsClass$13 = class$("[Ljava.lang.String;");
                        array$Ljava$lang$String = clsClass$13;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$13);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$14 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$14 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$14;
                    }
                    outputStream.write_value(subject, clsClass$14);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$ObjectInstance != null) {
                        clsClass$15 = class$javax$management$ObjectInstance;
                    } else {
                        clsClass$15 = class$("javax.management.ObjectInstance");
                        class$javax$management$ObjectInstance = clsClass$15;
                    }
                    return (ObjectInstance) inputStream.read_value(clsClass$15);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$8 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$8 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$8;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$8));
                    }
                    if (str2.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
                        if (class$javax$management$InstanceAlreadyExistsException != null) {
                            clsClass$7 = class$javax$management$InstanceAlreadyExistsException;
                        } else {
                            clsClass$7 = class$("javax.management.InstanceAlreadyExistsException");
                            class$javax$management$InstanceAlreadyExistsException = clsClass$7;
                        }
                        throw ((InstanceAlreadyExistsException) inputStream2.read_value(clsClass$7));
                    }
                    if (str2.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
                        if (class$javax$management$MBeanRegistrationException != null) {
                            clsClass$6 = class$javax$management$MBeanRegistrationException;
                        } else {
                            clsClass$6 = class$("javax.management.MBeanRegistrationException");
                            class$javax$management$MBeanRegistrationException = clsClass$6;
                        }
                        throw ((MBeanRegistrationException) inputStream2.read_value(clsClass$6));
                    }
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$5 = class$javax$management$MBeanException;
                        } else {
                            clsClass$5 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$5;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
                        if (class$javax$management$NotCompliantMBeanException != null) {
                            clsClass$4 = class$javax$management$NotCompliantMBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.NotCompliantMBeanException");
                            class$javax$management$NotCompliantMBeanException = clsClass$4;
                        }
                        throw ((NotCompliantMBeanException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return createMBean(str, objectName, objectName2, marshalledObject, strArr, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, ObjectName objectName2, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return createMBean(str, objectName, objectName2, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{str, objectName, objectName2, subject}, _orb());
                    return (ObjectInstance) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).createMBean((String) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (ObjectName) objArrCopyObjects[2], (Subject) objArrCopyObjects[3]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof InstanceAlreadyExistsException) {
                        throw ((InstanceAlreadyExistsException) th2);
                    }
                    if (th2 instanceof MBeanRegistrationException) {
                        throw ((MBeanRegistrationException) th2);
                    }
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof NotCompliantMBeanException) {
                        throw ((NotCompliantMBeanException) th2);
                    }
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", true);
                    if (class$java$lang$String != null) {
                        clsClass$9 = class$java$lang$String;
                    } else {
                        clsClass$9 = class$("java.lang.String");
                        class$java$lang$String = clsClass$9;
                    }
                    outputStream.write_value(str, clsClass$9);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$10 = class$javax$management$ObjectName;
                    } else {
                        clsClass$10 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$10;
                    }
                    outputStream.write_value(objectName, clsClass$10);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$11 = class$javax$management$ObjectName;
                    } else {
                        clsClass$11 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$11;
                    }
                    outputStream.write_value(objectName2, clsClass$11);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$12 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$12 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$12;
                    }
                    outputStream.write_value(subject, clsClass$12);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$ObjectInstance != null) {
                        clsClass$13 = class$javax$management$ObjectInstance;
                    } else {
                        clsClass$13 = class$("javax.management.ObjectInstance");
                        class$javax$management$ObjectInstance = clsClass$13;
                    }
                    return (ObjectInstance) inputStream.read_value(clsClass$13);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$8 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$8 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$8;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$8));
                    }
                    if (str2.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
                        if (class$javax$management$InstanceAlreadyExistsException != null) {
                            clsClass$7 = class$javax$management$InstanceAlreadyExistsException;
                        } else {
                            clsClass$7 = class$("javax.management.InstanceAlreadyExistsException");
                            class$javax$management$InstanceAlreadyExistsException = clsClass$7;
                        }
                        throw ((InstanceAlreadyExistsException) inputStream2.read_value(clsClass$7));
                    }
                    if (str2.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
                        if (class$javax$management$MBeanRegistrationException != null) {
                            clsClass$6 = class$javax$management$MBeanRegistrationException;
                        } else {
                            clsClass$6 = class$("javax.management.MBeanRegistrationException");
                            class$javax$management$MBeanRegistrationException = clsClass$6;
                        }
                        throw ((MBeanRegistrationException) inputStream2.read_value(clsClass$6));
                    }
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$5 = class$javax$management$MBeanException;
                        } else {
                            clsClass$5 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$5;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
                        if (class$javax$management$NotCompliantMBeanException != null) {
                            clsClass$4 = class$javax$management$NotCompliantMBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.NotCompliantMBeanException");
                            class$javax$management$NotCompliantMBeanException = clsClass$4;
                        }
                        throw ((NotCompliantMBeanException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return createMBean(str, objectName, objectName2, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance createMBean(String str, ObjectName objectName, Subject subject) throws MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, IOException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return createMBean(str, objectName, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{str, objectName, subject}, _orb());
                    return (ObjectInstance) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).createMBean((String) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof InstanceAlreadyExistsException) {
                        throw ((InstanceAlreadyExistsException) th2);
                    }
                    if (th2 instanceof MBeanRegistrationException) {
                        throw ((MBeanRegistrationException) th2);
                    }
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof NotCompliantMBeanException) {
                        throw ((NotCompliantMBeanException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject", true);
                    if (class$java$lang$String != null) {
                        clsClass$8 = class$java$lang$String;
                    } else {
                        clsClass$8 = class$("java.lang.String");
                        class$java$lang$String = clsClass$8;
                    }
                    outputStream.write_value(str, clsClass$8);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$9 = class$javax$management$ObjectName;
                    } else {
                        clsClass$9 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$9;
                    }
                    outputStream.write_value(objectName, clsClass$9);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$10 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$10 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$10;
                    }
                    outputStream.write_value(subject, clsClass$10);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$ObjectInstance != null) {
                        clsClass$11 = class$javax$management$ObjectInstance;
                    } else {
                        clsClass$11 = class$("javax.management.ObjectInstance");
                        class$javax$management$ObjectInstance = clsClass$11;
                    }
                    return (ObjectInstance) inputStream.read_value(clsClass$11);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$7 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$7 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$7;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$7));
                    }
                    if (str2.equals("IDL:javax/management/InstanceAlreadyExistsEx:1.0")) {
                        if (class$javax$management$InstanceAlreadyExistsException != null) {
                            clsClass$6 = class$javax$management$InstanceAlreadyExistsException;
                        } else {
                            clsClass$6 = class$("javax.management.InstanceAlreadyExistsException");
                            class$javax$management$InstanceAlreadyExistsException = clsClass$6;
                        }
                        throw ((InstanceAlreadyExistsException) inputStream2.read_value(clsClass$6));
                    }
                    if (str2.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
                        if (class$javax$management$MBeanRegistrationException != null) {
                            clsClass$5 = class$javax$management$MBeanRegistrationException;
                        } else {
                            clsClass$5 = class$("javax.management.MBeanRegistrationException");
                            class$javax$management$MBeanRegistrationException = clsClass$5;
                        }
                        throw ((MBeanRegistrationException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$4 = class$javax$management$MBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$4;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/NotCompliantMBeanEx:1.0")) {
                        if (class$javax$management$NotCompliantMBeanException != null) {
                            clsClass$3 = class$javax$management$NotCompliantMBeanException;
                        } else {
                            clsClass$3 = class$("javax.management.NotCompliantMBeanException");
                            class$javax$management$NotCompliantMBeanException = clsClass$3;
                        }
                        throw ((NotCompliantMBeanException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return createMBean(str, objectName, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public NotificationResult fetchNotifications(long j2, int i2, long j3) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("fetchNotifications", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return fetchNotifications(j2, i2, j3);
                }
                try {
                    return (NotificationResult) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).fetchNotifications(j2, i2, j3), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    org.omg.CORBA.portable.OutputStream outputStream_request = _request("fetchNotifications", true);
                    outputStream_request.write_longlong(j2);
                    outputStream_request.write_long(i2);
                    outputStream_request.write_longlong(j3);
                    inputStream = (InputStream) _invoke(outputStream_request);
                    if (class$javax$management$remote$NotificationResult != null) {
                        clsClass$3 = class$javax$management$remote$NotificationResult;
                    } else {
                        clsClass$3 = class$("javax.management.remote.NotificationResult");
                        class$javax$management$remote$NotificationResult = clsClass$3;
                    }
                    return (NotificationResult) inputStream.read_value(clsClass$3);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return fetchNotifications(j2, i2, j3);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object getAttribute(ObjectName objectName, String str, Subject subject) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getAttribute", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return getAttribute(objectName, str, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, str, subject}, _orb());
                    return Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getAttribute((ObjectName) objArrCopyObjects[0], (String) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof AttributeNotFoundException) {
                        throw ((AttributeNotFoundException) th2);
                    }
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getAttribute", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$7 = class$javax$management$ObjectName;
                    } else {
                        clsClass$7 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$7;
                    }
                    outputStream.write_value(objectName, clsClass$7);
                    if (class$java$lang$String != null) {
                        clsClass$8 = class$java$lang$String;
                    } else {
                        clsClass$8 = class$("java.lang.String");
                        class$java$lang$String = clsClass$8;
                    }
                    outputStream.write_value(str, clsClass$8);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$9 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$9 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$9;
                    }
                    outputStream.write_value(subject, clsClass$9);
                    inputStream = (InputStream) _invoke(outputStream);
                    return Util.readAny(inputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$6 = class$javax$management$MBeanException;
                        } else {
                            clsClass$6 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$6;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$6));
                    }
                    if (str2.equals("IDL:javax/management/AttributeNotFoundEx:1.0")) {
                        if (class$javax$management$AttributeNotFoundException != null) {
                            clsClass$5 = class$javax$management$AttributeNotFoundException;
                        } else {
                            clsClass$5 = class$("javax.management.AttributeNotFoundException");
                            class$javax$management$AttributeNotFoundException = clsClass$5;
                        }
                        throw ((AttributeNotFoundException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getAttribute(objectName, str, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList getAttributes(ObjectName objectName, String[] strArr, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getAttributes", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return getAttributes(objectName, strArr, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, strArr, subject}, _orb());
                    return (AttributeList) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getAttributes((ObjectName) objArrCopyObjects[0], (String[]) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) th2);
                }
                if (th2 instanceof ReflectionException) {
                    throw ((ReflectionException) th2);
                }
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getAttributes", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    Serializable serializableCast_array = cast_array(strArr);
                    if (array$Ljava$lang$String != null) {
                        clsClass$6 = array$Ljava$lang$String;
                    } else {
                        clsClass$6 = class$("[Ljava.lang.String;");
                        array$Ljava$lang$String = clsClass$6;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$6);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$7 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$7 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$7;
                    }
                    outputStream.write_value(subject, clsClass$7);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$AttributeList != null) {
                        clsClass$8 = class$javax$management$AttributeList;
                    } else {
                        clsClass$8 = class$("javax.management.AttributeList");
                        class$javax$management$AttributeList = clsClass$8;
                    }
                    return (AttributeList) inputStream.read_value(clsClass$8);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getAttributes(objectName, strArr, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getConnectionId() throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getConnectionId", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getConnectionId();
                }
                try {
                    return ((RMIConnection) servantObject_servant_preinvoke.servant).getConnectionId();
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    inputStream = (InputStream) _invoke(_request("getConnectionId", true));
                    if (class$java$lang$String != null) {
                        clsClass$3 = class$java$lang$String;
                    } else {
                        clsClass$3 = class$("java.lang.String");
                        class$java$lang$String = clsClass$3;
                    }
                    return (String) inputStream.read_value(clsClass$3);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getConnectionId();
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String getDefaultDomain(Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getDefaultDomain", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getDefaultDomain(subject);
                }
                try {
                    return ((RMIConnection) servantObject_servant_preinvoke.servant).getDefaultDomain((Subject) Util.copyObject(subject, _orb()));
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getDefaultDomain", true);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$3 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$3 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$3;
                    }
                    outputStream.write_value(subject, clsClass$3);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$java$lang$String != null) {
                        clsClass$4 = class$java$lang$String;
                    } else {
                        clsClass$4 = class$("java.lang.String");
                        class$java$lang$String = clsClass$4;
                    }
                    return (String) inputStream.read_value(clsClass$4);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getDefaultDomain(subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public String[] getDomains(Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getDomains", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getDomains(subject);
                }
                try {
                    return (String[]) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getDomains((Subject) Util.copyObject(subject, _orb())), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getDomains", true);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$3 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$3 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$3;
                    }
                    outputStream.write_value(subject, clsClass$3);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (array$Ljava$lang$String != null) {
                        clsClass$4 = array$Ljava$lang$String;
                    } else {
                        clsClass$4 = class$("[Ljava.lang.String;");
                        array$Ljava$lang$String = clsClass$4;
                    }
                    return (String[]) inputStream.read_value(clsClass$4);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getDomains(subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Integer getMBeanCount(Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getMBeanCount", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getMBeanCount(subject);
                }
                try {
                    return (Integer) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getMBeanCount((Subject) Util.copyObject(subject, _orb())), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getMBeanCount", true);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$3 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$3 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$3;
                    }
                    outputStream.write_value(subject, clsClass$3);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$java$lang$Integer != null) {
                        clsClass$4 = class$java$lang$Integer;
                    } else {
                        clsClass$4 = class$(Constants.INTEGER_CLASS);
                        class$java$lang$Integer = clsClass$4;
                    }
                    return (Integer) inputStream.read_value(clsClass$4);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getMBeanCount(subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public MBeanInfo getMBeanInfo(ObjectName objectName, Subject subject) throws IntrospectionException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getMBeanInfo", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return getMBeanInfo(objectName, subject);
                }
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, subject}, _orb());
                    return (MBeanInfo) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getMBeanInfo((ObjectName) objArrCopyObjects[0], (Subject) objArrCopyObjects[1]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof IntrospectionException) {
                        throw ((IntrospectionException) th2);
                    }
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getMBeanInfo", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$6 = class$javax$management$ObjectName;
                    } else {
                        clsClass$6 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$6;
                    }
                    outputStream.write_value(objectName, clsClass$6);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$7 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$7 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$7;
                    }
                    outputStream.write_value(subject, clsClass$7);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$MBeanInfo != null) {
                        clsClass$8 = class$javax$management$MBeanInfo;
                    } else {
                        clsClass$8 = class$("javax.management.MBeanInfo");
                        class$javax$management$MBeanInfo = clsClass$8;
                    }
                    return (MBeanInfo) inputStream.read_value(clsClass$8);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$5 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$5 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$5;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$5));
                    }
                    if (str.equals("IDL:javax/management/IntrospectionEx:1.0")) {
                        if (class$javax$management$IntrospectionException != null) {
                            clsClass$4 = class$javax$management$IntrospectionException;
                        } else {
                            clsClass$4 = class$("javax.management.IntrospectionException");
                            class$javax$management$IntrospectionException = clsClass$4;
                        }
                        throw ((IntrospectionException) inputStream2.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getMBeanInfo(objectName, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public ObjectInstance getObjectInstance(ObjectName objectName, Subject subject) throws IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("getObjectInstance", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return getObjectInstance(objectName, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, subject}, _orb());
                    return (ObjectInstance) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).getObjectInstance((ObjectName) objArrCopyObjects[0], (Subject) objArrCopyObjects[1]), _orb());
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) th2);
                }
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("getObjectInstance", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$4 = class$javax$management$ObjectName;
                    } else {
                        clsClass$4 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$4;
                    }
                    outputStream.write_value(objectName, clsClass$4);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$5 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$5 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$5;
                    }
                    outputStream.write_value(subject, clsClass$5);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$ObjectInstance != null) {
                        clsClass$6 = class$javax$management$ObjectInstance;
                    } else {
                        clsClass$6 = class$("javax.management.ObjectInstance");
                        class$javax$management$ObjectInstance = clsClass$6;
                    }
                    return (ObjectInstance) inputStream.read_value(clsClass$6);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return getObjectInstance(objectName, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Object invoke(ObjectName objectName, String str, MarshalledObject marshalledObject, String[] strArr, Subject subject) throws MBeanException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("invoke", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return invoke(objectName, str, marshalledObject, strArr, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, str, marshalledObject, strArr, subject}, _orb());
                    return Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).invoke((ObjectName) objArrCopyObjects[0], (String) objArrCopyObjects[1], (MarshalledObject) objArrCopyObjects[2], (String[]) objArrCopyObjects[3], (Subject) objArrCopyObjects[4]), _orb());
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof MBeanException) {
                        throw ((MBeanException) th2);
                    }
                    if (th2 instanceof ReflectionException) {
                        throw ((ReflectionException) th2);
                    }
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("invoke", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$6 = class$javax$management$ObjectName;
                    } else {
                        clsClass$6 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$6;
                    }
                    outputStream.write_value(objectName, clsClass$6);
                    if (class$java$lang$String != null) {
                        clsClass$7 = class$java$lang$String;
                    } else {
                        clsClass$7 = class$("java.lang.String");
                        class$java$lang$String = clsClass$7;
                    }
                    outputStream.write_value(str, clsClass$7);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$8 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$8 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$8;
                    }
                    outputStream.write_value(marshalledObject, clsClass$8);
                    Serializable serializableCast_array = cast_array(strArr);
                    if (array$Ljava$lang$String != null) {
                        clsClass$9 = array$Ljava$lang$String;
                    } else {
                        clsClass$9 = class$("[Ljava.lang.String;");
                        array$Ljava$lang$String = clsClass$9;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$9);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$10 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$10 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$10;
                    }
                    outputStream.write_value(subject, clsClass$10);
                    inputStream = (InputStream) _invoke(outputStream);
                    return Util.readAny(inputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$5 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$5 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$5;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$5));
                    }
                    if (str2.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$4 = class$javax$management$MBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$4;
                        }
                        throw ((MBeanException) inputStream2.read_value(clsClass$4));
                    }
                    if (str2.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return invoke(objectName, str, marshalledObject, strArr, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isInstanceOf(ObjectName objectName, String str, Subject subject) throws IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("isInstanceOf", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return isInstanceOf(objectName, str, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, str, subject}, _orb());
                    return ((RMIConnection) servantObject_servant_preinvoke.servant).isInstanceOf((ObjectName) objArrCopyObjects[0], (String) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]);
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) th2);
                }
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("isInstanceOf", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$4 = class$javax$management$ObjectName;
                    } else {
                        clsClass$4 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$4;
                    }
                    outputStream.write_value(objectName, clsClass$4);
                    if (class$java$lang$String != null) {
                        clsClass$5 = class$java$lang$String;
                    } else {
                        clsClass$5 = class$("java.lang.String");
                        class$java$lang$String = clsClass$5;
                    }
                    outputStream.write_value(str, clsClass$5);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$6 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$6 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$6;
                    }
                    outputStream.write_value(subject, clsClass$6);
                    inputStream = (InputStream) _invoke(outputStream);
                    return inputStream.read_boolean();
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str2 = inputStream2.read_string();
                    if (str2.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$3 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$3;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str2.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str2);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return isInstanceOf(objectName, str, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public boolean isRegistered(ObjectName objectName, Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("isRegistered", clsClass$);
            try {
                if (servantObject_servant_preinvoke == null) {
                    return isRegistered(objectName, subject);
                }
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, subject}, _orb());
                    return ((RMIConnection) servantObject_servant_preinvoke.servant).isRegistered((ObjectName) objArrCopyObjects[0], (Subject) objArrCopyObjects[1]);
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof IOException) {
                        throw ((IOException) th2);
                    }
                    throw Util.wrapException(th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("isRegistered", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$3 = class$javax$management$ObjectName;
                    } else {
                        clsClass$3 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$3;
                    }
                    outputStream.write_value(objectName, clsClass$3);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$4 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$4 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$4;
                    }
                    outputStream.write_value(subject, clsClass$4);
                    inputStream = (InputStream) _invoke(outputStream);
                    return inputStream.read_boolean();
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return isRegistered(objectName, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set queryMBeans(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("queryMBeans", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return queryMBeans(objectName, marshalledObject, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, marshalledObject, subject}, _orb());
                    return (Set) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).queryMBeans((ObjectName) objArrCopyObjects[0], (MarshalledObject) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("queryMBeans", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$3 = class$javax$management$ObjectName;
                    } else {
                        clsClass$3 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$3;
                    }
                    outputStream.write_value(objectName, clsClass$3);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$4 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$4 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$4;
                    }
                    outputStream.write_value(marshalledObject, clsClass$4);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$5 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$5 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$5;
                    }
                    outputStream.write_value(subject, clsClass$5);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$java$util$Set != null) {
                        clsClass$6 = class$java$util$Set;
                    } else {
                        clsClass$6 = class$("java.util.Set");
                        class$java$util$Set = clsClass$6;
                    }
                    return (Set) inputStream.read_value(clsClass$6);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return queryMBeans(objectName, marshalledObject, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public Set queryNames(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("queryNames", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return queryNames(objectName, marshalledObject, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, marshalledObject, subject}, _orb());
                    return (Set) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).queryNames((ObjectName) objArrCopyObjects[0], (MarshalledObject) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("queryNames", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$3 = class$javax$management$ObjectName;
                    } else {
                        clsClass$3 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$3;
                    }
                    outputStream.write_value(objectName, clsClass$3);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$4 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$4 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$4;
                    }
                    outputStream.write_value(marshalledObject, clsClass$4);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$5 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$5 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$5;
                    }
                    outputStream.write_value(subject, clsClass$5);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$java$util$Set != null) {
                        clsClass$6 = class$java$util$Set;
                    } else {
                        clsClass$6 = class$("java.util.Set");
                        class$java$util$Set = clsClass$6;
                    }
                    return (Set) inputStream.read_value(clsClass$6);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return queryNames(objectName, marshalledObject, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        checkPermission();
        objectInputStream.defaultReadObject();
        this._instantiated = true;
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, MarshalledObject marshalledObject, MarshalledObject marshalledObject2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                removeNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, objectName2, marshalledObject, marshalledObject2, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).removeNotificationListener((ObjectName) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (MarshalledObject) objArrCopyObjects[2], (MarshalledObject) objArrCopyObjects[3], (Subject) objArrCopyObjects[4]);
                    return;
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof ListenerNotFoundException) {
                        throw ((ListenerNotFoundException) th2);
                    }
                    if (!(th2 instanceof IOException)) {
                        throw Util.wrapException(th2);
                    }
                    throw ((IOException) th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$6 = class$javax$management$ObjectName;
                    } else {
                        clsClass$6 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$6;
                    }
                    outputStream.write_value(objectName2, clsClass$6);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$7 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$7 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$7;
                    }
                    outputStream.write_value(marshalledObject, clsClass$7);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$8 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$8 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$8;
                    }
                    outputStream.write_value(marshalledObject2, clsClass$8);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$9 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$9 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$9;
                    }
                    outputStream.write_value(subject, clsClass$9);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
                        if (class$javax$management$ListenerNotFoundException != null) {
                            clsClass$3 = class$javax$management$ListenerNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.ListenerNotFoundException");
                            class$javax$management$ListenerNotFoundException = clsClass$3;
                        }
                        throw ((ListenerNotFoundException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    removeNotificationListener(objectName, objectName2, marshalledObject, marshalledObject2, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListener(ObjectName objectName, ObjectName objectName2, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                removeNotificationListener(objectName, objectName2, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, objectName2, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).removeNotificationListener((ObjectName) objArrCopyObjects[0], (ObjectName) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]);
                    return;
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof ListenerNotFoundException) {
                        throw ((ListenerNotFoundException) th2);
                    }
                    if (!(th2 instanceof IOException)) {
                        throw Util.wrapException(th2);
                    }
                    throw ((IOException) th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$6 = class$javax$management$ObjectName;
                    } else {
                        clsClass$6 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$6;
                    }
                    outputStream.write_value(objectName2, clsClass$6);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$7 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$7 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$7;
                    }
                    outputStream.write_value(subject, clsClass$7);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
                        if (class$javax$management$ListenerNotFoundException != null) {
                            clsClass$3 = class$javax$management$ListenerNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.ListenerNotFoundException");
                            class$javax$management$ListenerNotFoundException = clsClass$3;
                        }
                        throw ((ListenerNotFoundException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    removeNotificationListener(objectName, objectName2, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void removeNotificationListeners(ObjectName objectName, Integer[] numArr, Subject subject) throws ListenerNotFoundException, IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("removeNotificationListeners", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                removeNotificationListeners(objectName, numArr, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, numArr, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).removeNotificationListeners((ObjectName) objArrCopyObjects[0], (Integer[]) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]);
                    return;
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof ListenerNotFoundException) {
                        throw ((ListenerNotFoundException) th2);
                    }
                    if (!(th2 instanceof IOException)) {
                        throw Util.wrapException(th2);
                    }
                    throw ((IOException) th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("removeNotificationListeners", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    Serializable serializableCast_array = cast_array(numArr);
                    if (array$Ljava$lang$Integer != null) {
                        clsClass$6 = array$Ljava$lang$Integer;
                    } else {
                        clsClass$6 = class$("[Ljava.lang.Integer;");
                        array$Ljava$lang$Integer = clsClass$6;
                    }
                    outputStream.write_value(serializableCast_array, clsClass$6);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$7 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$7 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$7;
                    }
                    outputStream.write_value(subject, clsClass$7);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ListenerNotFoundEx:1.0")) {
                        if (class$javax$management$ListenerNotFoundException != null) {
                            clsClass$3 = class$javax$management$ListenerNotFoundException;
                        } else {
                            clsClass$3 = class$("javax.management.ListenerNotFoundException");
                            class$javax$management$ListenerNotFoundException = clsClass$3;
                        }
                        throw ((ListenerNotFoundException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    removeNotificationListeners(objectName, numArr, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void setAttribute(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("setAttribute", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                setAttribute(objectName, marshalledObject, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, marshalledObject, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).setAttribute((ObjectName) objArrCopyObjects[0], (MarshalledObject) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]);
                    return;
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) th2);
                }
                if (th2 instanceof AttributeNotFoundException) {
                    throw ((AttributeNotFoundException) th2);
                }
                if (th2 instanceof InvalidAttributeValueException) {
                    throw ((InvalidAttributeValueException) th2);
                }
                if (th2 instanceof MBeanException) {
                    throw ((MBeanException) th2);
                }
                if (th2 instanceof ReflectionException) {
                    throw ((ReflectionException) th2);
                }
                if (!(th2 instanceof IOException)) {
                    throw Util.wrapException(th2);
                }
                throw ((IOException) th2);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("setAttribute", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$8 = class$javax$management$ObjectName;
                    } else {
                        clsClass$8 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$8;
                    }
                    outputStream.write_value(objectName, clsClass$8);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$9 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$9 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$9;
                    }
                    outputStream.write_value(marshalledObject, clsClass$9);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$10 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$10 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$10;
                    }
                    outputStream.write_value(subject, clsClass$10);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$7 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$7 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$7;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$7));
                    }
                    if (str.equals("IDL:javax/management/AttributeNotFoundEx:1.0")) {
                        if (class$javax$management$AttributeNotFoundException != null) {
                            clsClass$6 = class$javax$management$AttributeNotFoundException;
                        } else {
                            clsClass$6 = class$("javax.management.AttributeNotFoundException");
                            class$javax$management$AttributeNotFoundException = clsClass$6;
                        }
                        throw ((AttributeNotFoundException) inputStream.read_value(clsClass$6));
                    }
                    if (str.equals("IDL:javax/management/InvalidAttributeValueEx:1.0")) {
                        if (class$javax$management$InvalidAttributeValueException != null) {
                            clsClass$5 = class$javax$management$InvalidAttributeValueException;
                        } else {
                            clsClass$5 = class$("javax.management.InvalidAttributeValueException");
                            class$javax$management$InvalidAttributeValueException = clsClass$5;
                        }
                        throw ((InvalidAttributeValueException) inputStream.read_value(clsClass$5));
                    }
                    if (str.equals("IDL:javax/management/MBeanEx:1.0")) {
                        if (class$javax$management$MBeanException != null) {
                            clsClass$4 = class$javax$management$MBeanException;
                        } else {
                            clsClass$4 = class$("javax.management.MBeanException");
                            class$javax$management$MBeanException = clsClass$4;
                        }
                        throw ((MBeanException) inputStream.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    setAttribute(objectName, marshalledObject, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public AttributeList setAttributes(ObjectName objectName, MarshalledObject marshalledObject, Subject subject) throws IOException, InstanceNotFoundException, ReflectionException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("setAttributes", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                return setAttributes(objectName, marshalledObject, subject);
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, marshalledObject, subject}, _orb());
                    return (AttributeList) Util.copyObject(((RMIConnection) servantObject_servant_preinvoke.servant).setAttributes((ObjectName) objArrCopyObjects[0], (MarshalledObject) objArrCopyObjects[1], (Subject) objArrCopyObjects[2]), _orb());
                } finally {
                    _servant_postinvoke(servantObject_servant_preinvoke);
                }
            } catch (Throwable th) {
                Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                if (th2 instanceof InstanceNotFoundException) {
                    throw ((InstanceNotFoundException) th2);
                }
                if (th2 instanceof ReflectionException) {
                    throw ((ReflectionException) th2);
                }
                if (th2 instanceof IOException) {
                    throw ((IOException) th2);
                }
                throw Util.wrapException(th2);
            }
        }
        org.omg.CORBA.portable.InputStream inputStream = null;
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("setAttributes", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    if (class$java$rmi$MarshalledObject != null) {
                        clsClass$6 = class$java$rmi$MarshalledObject;
                    } else {
                        clsClass$6 = class$("java.rmi.MarshalledObject");
                        class$java$rmi$MarshalledObject = clsClass$6;
                    }
                    outputStream.write_value(marshalledObject, clsClass$6);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$7 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$7 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$7;
                    }
                    outputStream.write_value(subject, clsClass$7);
                    inputStream = (InputStream) _invoke(outputStream);
                    if (class$javax$management$AttributeList != null) {
                        clsClass$8 = class$javax$management$AttributeList;
                    } else {
                        clsClass$8 = class$("javax.management.AttributeList");
                        class$javax$management$AttributeList = clsClass$8;
                    }
                    return (AttributeList) inputStream.read_value(clsClass$8);
                } catch (ApplicationException e2) {
                    InputStream inputStream2 = (InputStream) e2.getInputStream();
                    String str = inputStream2.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream2.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/ReflectionEx:1.0")) {
                        if (class$javax$management$ReflectionException != null) {
                            clsClass$3 = class$javax$management$ReflectionException;
                        } else {
                            clsClass$3 = class$("javax.management.ReflectionException");
                            class$javax$management$ReflectionException = clsClass$3;
                        }
                        throw ((ReflectionException) inputStream2.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream2.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    inputStream = inputStream;
                    return setAttributes(objectName, marshalledObject, subject);
                }
            } finally {
                _releaseReply(null);
            }
        } catch (SystemException e3) {
            throw Util.mapSystemException(e3);
        }
    }

    @Override // javax.management.remote.rmi.RMIConnection
    public void unregisterMBean(ObjectName objectName, Subject subject) throws MBeanRegistrationException, IOException, InstanceNotFoundException {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (System.getSecurityManager() != null && !this._instantiated) {
            throw new IOError(new IOException("InvalidObject "));
        }
        if (Util.isLocal(this)) {
            if (class$javax$management$remote$rmi$RMIConnection != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIConnection;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIConnection");
                class$javax$management$remote$rmi$RMIConnection = clsClass$;
            }
            ServantObject servantObject_servant_preinvoke = _servant_preinvoke("unregisterMBean", clsClass$);
            if (servantObject_servant_preinvoke == null) {
                unregisterMBean(objectName, subject);
                return;
            }
            try {
                try {
                    Object[] objArrCopyObjects = Util.copyObjects(new Object[]{objectName, subject}, _orb());
                    ((RMIConnection) servantObject_servant_preinvoke.servant).unregisterMBean((ObjectName) objArrCopyObjects[0], (Subject) objArrCopyObjects[1]);
                    return;
                } catch (Throwable th) {
                    Throwable th2 = (Throwable) Util.copyObject(th, _orb());
                    if (th2 instanceof InstanceNotFoundException) {
                        throw ((InstanceNotFoundException) th2);
                    }
                    if (th2 instanceof MBeanRegistrationException) {
                        throw ((MBeanRegistrationException) th2);
                    }
                    if (!(th2 instanceof IOException)) {
                        throw Util.wrapException(th2);
                    }
                    throw ((IOException) th2);
                }
            } finally {
                _servant_postinvoke(servantObject_servant_preinvoke);
            }
        }
        try {
            try {
                try {
                    OutputStream outputStream = (OutputStream) _request("unregisterMBean", true);
                    if (class$javax$management$ObjectName != null) {
                        clsClass$5 = class$javax$management$ObjectName;
                    } else {
                        clsClass$5 = class$("javax.management.ObjectName");
                        class$javax$management$ObjectName = clsClass$5;
                    }
                    outputStream.write_value(objectName, clsClass$5);
                    if (class$javax$security$auth$Subject != null) {
                        clsClass$6 = class$javax$security$auth$Subject;
                    } else {
                        clsClass$6 = class$("javax.security.auth.Subject");
                        class$javax$security$auth$Subject = clsClass$6;
                    }
                    outputStream.write_value(subject, clsClass$6);
                    _invoke(outputStream);
                } catch (ApplicationException e2) {
                    InputStream inputStream = (InputStream) e2.getInputStream();
                    String str = inputStream.read_string();
                    if (str.equals("IDL:javax/management/InstanceNotFoundEx:1.0")) {
                        if (class$javax$management$InstanceNotFoundException != null) {
                            clsClass$4 = class$javax$management$InstanceNotFoundException;
                        } else {
                            clsClass$4 = class$("javax.management.InstanceNotFoundException");
                            class$javax$management$InstanceNotFoundException = clsClass$4;
                        }
                        throw ((InstanceNotFoundException) inputStream.read_value(clsClass$4));
                    }
                    if (str.equals("IDL:javax/management/MBeanRegistrationEx:1.0")) {
                        if (class$javax$management$MBeanRegistrationException != null) {
                            clsClass$3 = class$javax$management$MBeanRegistrationException;
                        } else {
                            clsClass$3 = class$("javax.management.MBeanRegistrationException");
                            class$javax$management$MBeanRegistrationException = clsClass$3;
                        }
                        throw ((MBeanRegistrationException) inputStream.read_value(clsClass$3));
                    }
                    if (!str.equals("IDL:java/io/IOEx:1.0")) {
                        throw new UnexpectedException(str);
                    }
                    if (class$java$io$IOException != null) {
                        clsClass$2 = class$java$io$IOException;
                    } else {
                        clsClass$2 = class$("java.io.IOException");
                        class$java$io$IOException = clsClass$2;
                    }
                    throw ((IOException) inputStream.read_value(clsClass$2));
                } catch (RemarshalException unused) {
                    unregisterMBean(objectName, subject);
                }
            } catch (SystemException e3) {
                throw Util.mapSystemException(e3);
            }
        } finally {
            _releaseReply(null);
        }
    }
}
