package org.omg.stub.javax.management.remote.rmi;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
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
import javax.management.remote.rmi.RMIConnectionImpl;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import javax.security.auth.Subject;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;
import org.omg.CORBA_2_3.portable.ObjectImpl;

/* loaded from: rt.jar:org/omg/stub/javax/management/remote/rmi/_RMIConnectionImpl_Tie.class */
public class _RMIConnectionImpl_Tie extends ObjectImpl implements Tie {
    private volatile RMIConnectionImpl target = null;
    private static final String[] _type_ids = {"RMI:javax.management.remote.rmi.RMIConnection:0000000000000000"};
    static Class class$javax$management$ObjectName;
    static Class class$java$lang$String;
    static Class class$javax$security$auth$Subject;
    static Class class$javax$management$MBeanException;
    static Class class$javax$management$AttributeNotFoundException;
    static Class class$javax$management$InstanceNotFoundException;
    static Class class$javax$management$ReflectionException;
    static Class class$java$io$IOException;
    static Class array$Ljava$lang$String;
    static Class class$javax$management$AttributeList;
    static Class class$java$rmi$MarshalledObject;
    static Class class$javax$management$InvalidAttributeValueException;
    static Class class$java$lang$Integer;
    static Class class$javax$management$IntrospectionException;
    static Class class$javax$management$MBeanInfo;
    static Class array$Ljavax$management$ObjectName;
    static Class array$Ljava$rmi$MarshalledObject;
    static Class array$Ljavax$security$auth$Subject;
    static Class array$Ljava$lang$Integer;
    static Class class$javax$management$ObjectInstance;
    static Class class$javax$management$InstanceAlreadyExistsException;
    static Class class$javax$management$NotCompliantMBeanException;
    static Class class$javax$management$remote$NotificationResult;
    static Class class$javax$management$MBeanRegistrationException;
    static Class class$javax$management$ListenerNotFoundException;
    static Class class$java$util$Set;

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) throws SystemException {
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
        Class clsClass$16;
        Class clsClass$17;
        Class clsClass$18;
        Class clsClass$19;
        Class clsClass$20;
        Class clsClass$21;
        Class clsClass$22;
        Class clsClass$23;
        Class clsClass$24;
        Class clsClass$25;
        Class clsClass$26;
        Class clsClass$27;
        Class clsClass$28;
        Class clsClass$29;
        Class clsClass$30;
        Class clsClass$31;
        Class clsClass$32;
        Class clsClass$33;
        Class clsClass$34;
        Class clsClass$35;
        Class clsClass$36;
        Class clsClass$37;
        Class clsClass$38;
        Class clsClass$39;
        Class clsClass$40;
        Class clsClass$41;
        Class clsClass$42;
        Class clsClass$43;
        Class clsClass$44;
        Class clsClass$45;
        Class clsClass$46;
        Class clsClass$47;
        Class clsClass$48;
        Class clsClass$49;
        Class clsClass$50;
        Class clsClass$51;
        Class clsClass$52;
        Class clsClass$53;
        Class clsClass$54;
        Class clsClass$55;
        Class clsClass$56;
        Class clsClass$57;
        Class clsClass$58;
        Class clsClass$59;
        Class clsClass$60;
        Class clsClass$61;
        Class clsClass$62;
        Class clsClass$63;
        Class clsClass$64;
        Class clsClass$65;
        Class clsClass$66;
        Class clsClass$67;
        Class clsClass$68;
        Class clsClass$69;
        Class clsClass$70;
        Class clsClass$71;
        Class clsClass$72;
        Class clsClass$73;
        Class clsClass$74;
        Class clsClass$75;
        Class clsClass$76;
        Class clsClass$77;
        Class clsClass$78;
        Class clsClass$79;
        Class clsClass$80;
        Class clsClass$81;
        Class clsClass$82;
        Class clsClass$83;
        Class clsClass$84;
        Class clsClass$85;
        Class clsClass$86;
        Class clsClass$87;
        Class clsClass$88;
        Class clsClass$89;
        Class clsClass$90;
        Class clsClass$91;
        Class clsClass$92;
        Class clsClass$93;
        Class clsClass$94;
        Class clsClass$95;
        Class clsClass$96;
        Class clsClass$97;
        Class clsClass$98;
        Class clsClass$99;
        Class clsClass$100;
        Class clsClass$101;
        Class clsClass$102;
        Class clsClass$103;
        Class clsClass$104;
        Class clsClass$105;
        Class clsClass$106;
        Class clsClass$107;
        Class clsClass$108;
        Class clsClass$109;
        Class clsClass$110;
        Class clsClass$111;
        Class clsClass$112;
        Class clsClass$113;
        Class clsClass$114;
        Class clsClass$115;
        Class clsClass$116;
        Class clsClass$117;
        Class clsClass$118;
        Class clsClass$119;
        Class clsClass$120;
        Class clsClass$121;
        Class clsClass$122;
        Class clsClass$123;
        Class clsClass$124;
        Class clsClass$125;
        Class clsClass$126;
        Class clsClass$127;
        Class clsClass$128;
        Class clsClass$129;
        Class clsClass$130;
        Class clsClass$131;
        Class clsClass$132;
        Class clsClass$133;
        Class clsClass$134;
        Class clsClass$135;
        Class clsClass$136;
        Class clsClass$137;
        Class clsClass$138;
        Class clsClass$139;
        Class clsClass$140;
        Class clsClass$141;
        Class clsClass$142;
        Class clsClass$143;
        Class clsClass$144;
        Class clsClass$145;
        Class clsClass$146;
        Class clsClass$147;
        Class clsClass$148;
        Class clsClass$149;
        Class clsClass$150;
        Class clsClass$151;
        Class clsClass$152;
        Class clsClass$153;
        Class clsClass$154;
        Class clsClass$155;
        Class clsClass$156;
        Class clsClass$157;
        Class clsClass$158;
        Class clsClass$159;
        Class clsClass$160;
        Class clsClass$161;
        Class clsClass$162;
        Class clsClass$163;
        Class clsClass$164;
        Class clsClass$165;
        Class clsClass$166;
        try {
            RMIConnectionImpl rMIConnectionImpl = this.target;
            if (rMIConnectionImpl == null) {
                throw new IOException();
            }
            org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) inputStream;
            switch (str.charAt(3)) {
                case 'A':
                    if (str.equals("getAttribute")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$159 = class$javax$management$ObjectName;
                        } else {
                            clsClass$159 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$159;
                        }
                        ObjectName objectName = (ObjectName) inputStream2.read_value(clsClass$159);
                        if (class$java$lang$String != null) {
                            clsClass$160 = class$java$lang$String;
                        } else {
                            clsClass$160 = class$("java.lang.String");
                            class$java$lang$String = clsClass$160;
                        }
                        String str2 = (String) inputStream2.read_value(clsClass$160);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$161 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$161 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$161;
                        }
                        try {
                            Object attribute = rMIConnectionImpl.getAttribute(objectName, str2, (Subject) inputStream2.read_value(clsClass$161));
                            OutputStream outputStreamCreateReply = responseHandler.createReply();
                            Util.writeAny(outputStreamCreateReply, attribute);
                            return outputStreamCreateReply;
                        } catch (IOException e2) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$166 = class$java$io$IOException;
                            } else {
                                clsClass$166 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$166;
                            }
                            outputStream.write_value(e2, clsClass$166);
                            return outputStream;
                        } catch (AttributeNotFoundException e3) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream2 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream2.write_string("IDL:javax/management/AttributeNotFoundEx:1.0");
                            if (class$javax$management$AttributeNotFoundException != null) {
                                clsClass$165 = class$javax$management$AttributeNotFoundException;
                            } else {
                                clsClass$165 = class$("javax.management.AttributeNotFoundException");
                                class$javax$management$AttributeNotFoundException = clsClass$165;
                            }
                            outputStream2.write_value(e3, clsClass$165);
                            return outputStream2;
                        } catch (InstanceNotFoundException e4) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream3 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream3.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$164 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$164 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$164;
                            }
                            outputStream3.write_value(e4, clsClass$164);
                            return outputStream3;
                        } catch (MBeanException e5) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream4 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream4.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$163 = class$javax$management$MBeanException;
                            } else {
                                clsClass$163 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$163;
                            }
                            outputStream4.write_value(e5, clsClass$163);
                            return outputStream4;
                        } catch (ReflectionException e6) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream5 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream5.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$162 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$162 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$162;
                            }
                            outputStream5.write_value(e6, clsClass$162);
                            return outputStream5;
                        }
                    }
                    if (str.equals("getAttributes")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$152 = class$javax$management$ObjectName;
                        } else {
                            clsClass$152 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$152;
                        }
                        ObjectName objectName2 = (ObjectName) inputStream2.read_value(clsClass$152);
                        if (array$Ljava$lang$String != null) {
                            clsClass$153 = array$Ljava$lang$String;
                        } else {
                            clsClass$153 = class$("[Ljava.lang.String;");
                            array$Ljava$lang$String = clsClass$153;
                        }
                        String[] strArr = (String[]) inputStream2.read_value(clsClass$153);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$154 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$154 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$154;
                        }
                        try {
                            AttributeList attributes = rMIConnectionImpl.getAttributes(objectName2, strArr, (Subject) inputStream2.read_value(clsClass$154));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream6 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$AttributeList != null) {
                                clsClass$158 = class$javax$management$AttributeList;
                            } else {
                                clsClass$158 = class$("javax.management.AttributeList");
                                class$javax$management$AttributeList = clsClass$158;
                            }
                            outputStream6.write_value(attributes, clsClass$158);
                            return outputStream6;
                        } catch (IOException e7) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream7 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream7.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$157 = class$java$io$IOException;
                            } else {
                                clsClass$157 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$157;
                            }
                            outputStream7.write_value(e7, clsClass$157);
                            return outputStream7;
                        } catch (InstanceNotFoundException e8) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream8 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream8.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$156 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$156 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$156;
                            }
                            outputStream8.write_value(e8, clsClass$156);
                            return outputStream8;
                        } catch (ReflectionException e9) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream9 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream9.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$155 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$155 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$155;
                            }
                            outputStream9.write_value(e9, clsClass$155);
                            return outputStream9;
                        }
                    }
                    if (str.equals("setAttribute")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$143 = class$javax$management$ObjectName;
                        } else {
                            clsClass$143 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$143;
                        }
                        ObjectName objectName3 = (ObjectName) inputStream2.read_value(clsClass$143);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$144 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$144 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$144;
                        }
                        MarshalledObject marshalledObject = (MarshalledObject) inputStream2.read_value(clsClass$144);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$145 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$145 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$145;
                        }
                        try {
                            rMIConnectionImpl.setAttribute(objectName3, marshalledObject, (Subject) inputStream2.read_value(clsClass$145));
                            return responseHandler.createReply();
                        } catch (IOException e10) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream10 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream10.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$151 = class$java$io$IOException;
                            } else {
                                clsClass$151 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$151;
                            }
                            outputStream10.write_value(e10, clsClass$151);
                            return outputStream10;
                        } catch (AttributeNotFoundException e11) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream11 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream11.write_string("IDL:javax/management/AttributeNotFoundEx:1.0");
                            if (class$javax$management$AttributeNotFoundException != null) {
                                clsClass$150 = class$javax$management$AttributeNotFoundException;
                            } else {
                                clsClass$150 = class$("javax.management.AttributeNotFoundException");
                                class$javax$management$AttributeNotFoundException = clsClass$150;
                            }
                            outputStream11.write_value(e11, clsClass$150);
                            return outputStream11;
                        } catch (InstanceNotFoundException e12) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream12 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream12.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$149 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$149 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$149;
                            }
                            outputStream12.write_value(e12, clsClass$149);
                            return outputStream12;
                        } catch (InvalidAttributeValueException e13) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream13 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream13.write_string("IDL:javax/management/InvalidAttributeValueEx:1.0");
                            if (class$javax$management$InvalidAttributeValueException != null) {
                                clsClass$148 = class$javax$management$InvalidAttributeValueException;
                            } else {
                                clsClass$148 = class$("javax.management.InvalidAttributeValueException");
                                class$javax$management$InvalidAttributeValueException = clsClass$148;
                            }
                            outputStream13.write_value(e13, clsClass$148);
                            return outputStream13;
                        } catch (MBeanException e14) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream14 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream14.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$147 = class$javax$management$MBeanException;
                            } else {
                                clsClass$147 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$147;
                            }
                            outputStream14.write_value(e14, clsClass$147);
                            return outputStream14;
                        } catch (ReflectionException e15) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream15 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream15.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$146 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$146 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$146;
                            }
                            outputStream15.write_value(e15, clsClass$146);
                            return outputStream15;
                        }
                    }
                    if (str.equals("setAttributes")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$136 = class$javax$management$ObjectName;
                        } else {
                            clsClass$136 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$136;
                        }
                        ObjectName objectName4 = (ObjectName) inputStream2.read_value(clsClass$136);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$137 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$137 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$137;
                        }
                        MarshalledObject marshalledObject2 = (MarshalledObject) inputStream2.read_value(clsClass$137);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$138 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$138 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$138;
                        }
                        try {
                            AttributeList attributes2 = rMIConnectionImpl.setAttributes(objectName4, marshalledObject2, (Subject) inputStream2.read_value(clsClass$138));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream16 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$AttributeList != null) {
                                clsClass$142 = class$javax$management$AttributeList;
                            } else {
                                clsClass$142 = class$("javax.management.AttributeList");
                                class$javax$management$AttributeList = clsClass$142;
                            }
                            outputStream16.write_value(attributes2, clsClass$142);
                            return outputStream16;
                        } catch (IOException e16) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream17 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream17.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$141 = class$java$io$IOException;
                            } else {
                                clsClass$141 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$141;
                            }
                            outputStream17.write_value(e16, clsClass$141);
                            return outputStream17;
                        } catch (InstanceNotFoundException e17) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream18 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream18.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$140 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$140 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$140;
                            }
                            outputStream18.write_value(e17, clsClass$140);
                            return outputStream18;
                        } catch (ReflectionException e18) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream19 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream19.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$139 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$139 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$139;
                            }
                            outputStream19.write_value(e18, clsClass$139);
                            return outputStream19;
                        }
                    }
                case 'C':
                    if (str.equals("getConnectionId")) {
                        try {
                            String connectionId = rMIConnectionImpl.getConnectionId();
                            org.omg.CORBA_2_3.portable.OutputStream outputStream20 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$java$lang$String != null) {
                                clsClass$135 = class$java$lang$String;
                            } else {
                                clsClass$135 = class$("java.lang.String");
                                class$java$lang$String = clsClass$135;
                            }
                            outputStream20.write_value(connectionId, clsClass$135);
                            return outputStream20;
                        } catch (IOException e19) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream21 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream21.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$134 = class$java$io$IOException;
                            } else {
                                clsClass$134 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$134;
                            }
                            outputStream21.write_value(e19, clsClass$134);
                            return outputStream21;
                        }
                    }
                case 'D':
                    if (str.equals("getDefaultDomain")) {
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$131 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$131 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$131;
                        }
                        try {
                            String defaultDomain = rMIConnectionImpl.getDefaultDomain((Subject) inputStream2.read_value(clsClass$131));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream22 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$java$lang$String != null) {
                                clsClass$133 = class$java$lang$String;
                            } else {
                                clsClass$133 = class$("java.lang.String");
                                class$java$lang$String = clsClass$133;
                            }
                            outputStream22.write_value(defaultDomain, clsClass$133);
                            return outputStream22;
                        } catch (IOException e20) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream23 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream23.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$132 = class$java$io$IOException;
                            } else {
                                clsClass$132 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$132;
                            }
                            outputStream23.write_value(e20, clsClass$132);
                            return outputStream23;
                        }
                    }
                    if (str.equals("getDomains")) {
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$128 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$128 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$128;
                        }
                        try {
                            String[] domains = rMIConnectionImpl.getDomains((Subject) inputStream2.read_value(clsClass$128));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream24 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            Serializable serializableCast_array = cast_array(domains);
                            if (array$Ljava$lang$String != null) {
                                clsClass$130 = array$Ljava$lang$String;
                            } else {
                                clsClass$130 = class$("[Ljava.lang.String;");
                                array$Ljava$lang$String = clsClass$130;
                            }
                            outputStream24.write_value(serializableCast_array, clsClass$130);
                            return outputStream24;
                        } catch (IOException e21) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream25 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream25.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$129 = class$java$io$IOException;
                            } else {
                                clsClass$129 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$129;
                            }
                            outputStream25.write_value(e21, clsClass$129);
                            return outputStream25;
                        }
                    }
                case 'M':
                    if (str.equals("getMBeanCount")) {
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$125 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$125 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$125;
                        }
                        try {
                            Integer mBeanCount = rMIConnectionImpl.getMBeanCount((Subject) inputStream2.read_value(clsClass$125));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream26 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$java$lang$Integer != null) {
                                clsClass$127 = class$java$lang$Integer;
                            } else {
                                clsClass$127 = class$(Constants.INTEGER_CLASS);
                                class$java$lang$Integer = clsClass$127;
                            }
                            outputStream26.write_value(mBeanCount, clsClass$127);
                            return outputStream26;
                        } catch (IOException e22) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream27 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream27.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$126 = class$java$io$IOException;
                            } else {
                                clsClass$126 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$126;
                            }
                            outputStream27.write_value(e22, clsClass$126);
                            return outputStream27;
                        }
                    }
                    if (str.equals("getMBeanInfo")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$118 = class$javax$management$ObjectName;
                        } else {
                            clsClass$118 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$118;
                        }
                        ObjectName objectName5 = (ObjectName) inputStream2.read_value(clsClass$118);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$119 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$119 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$119;
                        }
                        try {
                            MBeanInfo mBeanInfo = rMIConnectionImpl.getMBeanInfo(objectName5, (Subject) inputStream2.read_value(clsClass$119));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream28 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$MBeanInfo != null) {
                                clsClass$124 = class$javax$management$MBeanInfo;
                            } else {
                                clsClass$124 = class$("javax.management.MBeanInfo");
                                class$javax$management$MBeanInfo = clsClass$124;
                            }
                            outputStream28.write_value(mBeanInfo, clsClass$124);
                            return outputStream28;
                        } catch (IOException e23) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream29 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream29.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$123 = class$java$io$IOException;
                            } else {
                                clsClass$123 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$123;
                            }
                            outputStream29.write_value(e23, clsClass$123);
                            return outputStream29;
                        } catch (InstanceNotFoundException e24) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream30 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream30.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$122 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$122 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$122;
                            }
                            outputStream30.write_value(e24, clsClass$122);
                            return outputStream30;
                        } catch (IntrospectionException e25) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream31 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream31.write_string("IDL:javax/management/IntrospectionEx:1.0");
                            if (class$javax$management$IntrospectionException != null) {
                                clsClass$121 = class$javax$management$IntrospectionException;
                            } else {
                                clsClass$121 = class$("javax.management.IntrospectionException");
                                class$javax$management$IntrospectionException = clsClass$121;
                            }
                            outputStream31.write_value(e25, clsClass$121);
                            return outputStream31;
                        } catch (ReflectionException e26) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream32 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream32.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$120 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$120 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$120;
                            }
                            outputStream32.write_value(e26, clsClass$120);
                            return outputStream32;
                        }
                    }
                case 'N':
                    if (str.equals("addNotificationListener")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$111 = class$javax$management$ObjectName;
                        } else {
                            clsClass$111 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$111;
                        }
                        ObjectName objectName6 = (ObjectName) inputStream2.read_value(clsClass$111);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$112 = class$javax$management$ObjectName;
                        } else {
                            clsClass$112 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$112;
                        }
                        ObjectName objectName7 = (ObjectName) inputStream2.read_value(clsClass$112);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$113 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$113 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$113;
                        }
                        MarshalledObject marshalledObject3 = (MarshalledObject) inputStream2.read_value(clsClass$113);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$114 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$114 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$114;
                        }
                        MarshalledObject marshalledObject4 = (MarshalledObject) inputStream2.read_value(clsClass$114);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$115 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$115 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$115;
                        }
                        try {
                            rMIConnectionImpl.addNotificationListener(objectName6, objectName7, marshalledObject3, marshalledObject4, (Subject) inputStream2.read_value(clsClass$115));
                            return responseHandler.createReply();
                        } catch (IOException e27) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream33 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream33.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$117 = class$java$io$IOException;
                            } else {
                                clsClass$117 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$117;
                            }
                            outputStream33.write_value(e27, clsClass$117);
                            return outputStream33;
                        } catch (InstanceNotFoundException e28) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream34 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream34.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$116 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$116 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$116;
                            }
                            outputStream34.write_value(e28, clsClass$116);
                            return outputStream34;
                        }
                    }
                    if (str.equals("addNotificationListeners")) {
                        if (array$Ljavax$management$ObjectName != null) {
                            clsClass$105 = array$Ljavax$management$ObjectName;
                        } else {
                            clsClass$105 = class$("[Ljavax.management.ObjectName;");
                            array$Ljavax$management$ObjectName = clsClass$105;
                        }
                        ObjectName[] objectNameArr = (ObjectName[]) inputStream2.read_value(clsClass$105);
                        if (array$Ljava$rmi$MarshalledObject != null) {
                            clsClass$106 = array$Ljava$rmi$MarshalledObject;
                        } else {
                            clsClass$106 = class$("[Ljava.rmi.MarshalledObject;");
                            array$Ljava$rmi$MarshalledObject = clsClass$106;
                        }
                        MarshalledObject[] marshalledObjectArr = (MarshalledObject[]) inputStream2.read_value(clsClass$106);
                        if (array$Ljavax$security$auth$Subject != null) {
                            clsClass$107 = array$Ljavax$security$auth$Subject;
                        } else {
                            clsClass$107 = class$("[Ljavax.security.auth.Subject;");
                            array$Ljavax$security$auth$Subject = clsClass$107;
                        }
                        try {
                            Integer[] numArrAddNotificationListeners = rMIConnectionImpl.addNotificationListeners(objectNameArr, marshalledObjectArr, (Subject[]) inputStream2.read_value(clsClass$107));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream35 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            Serializable serializableCast_array2 = cast_array(numArrAddNotificationListeners);
                            if (array$Ljava$lang$Integer != null) {
                                clsClass$110 = array$Ljava$lang$Integer;
                            } else {
                                clsClass$110 = class$("[Ljava.lang.Integer;");
                                array$Ljava$lang$Integer = clsClass$110;
                            }
                            outputStream35.write_value(serializableCast_array2, clsClass$110);
                            return outputStream35;
                        } catch (IOException e29) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream36 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream36.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$109 = class$java$io$IOException;
                            } else {
                                clsClass$109 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$109;
                            }
                            outputStream36.write_value(e29, clsClass$109);
                            return outputStream36;
                        } catch (InstanceNotFoundException e30) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream37 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream37.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$108 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$108 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$108;
                            }
                            outputStream37.write_value(e30, clsClass$108);
                            return outputStream37;
                        }
                    }
                case 'O':
                    if (str.equals("getObjectInstance")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$100 = class$javax$management$ObjectName;
                        } else {
                            clsClass$100 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$100;
                        }
                        ObjectName objectName8 = (ObjectName) inputStream2.read_value(clsClass$100);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$101 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$101 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$101;
                        }
                        try {
                            ObjectInstance objectInstance = rMIConnectionImpl.getObjectInstance(objectName8, (Subject) inputStream2.read_value(clsClass$101));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream38 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$ObjectInstance != null) {
                                clsClass$104 = class$javax$management$ObjectInstance;
                            } else {
                                clsClass$104 = class$("javax.management.ObjectInstance");
                                class$javax$management$ObjectInstance = clsClass$104;
                            }
                            outputStream38.write_value(objectInstance, clsClass$104);
                            return outputStream38;
                        } catch (IOException e31) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream39 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream39.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$103 = class$java$io$IOException;
                            } else {
                                clsClass$103 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$103;
                            }
                            outputStream39.write_value(e31, clsClass$103);
                            return outputStream39;
                        } catch (InstanceNotFoundException e32) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream40 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream40.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$102 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$102 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$102;
                            }
                            outputStream40.write_value(e32, clsClass$102);
                            return outputStream40;
                        }
                    }
                case 'a':
                    if (str.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_security_auth_Subject")) {
                        if (class$java$lang$String != null) {
                            clsClass$91 = class$java$lang$String;
                        } else {
                            clsClass$91 = class$("java.lang.String");
                            class$java$lang$String = clsClass$91;
                        }
                        String str3 = (String) inputStream2.read_value(clsClass$91);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$92 = class$javax$management$ObjectName;
                        } else {
                            clsClass$92 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$92;
                        }
                        ObjectName objectName9 = (ObjectName) inputStream2.read_value(clsClass$92);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$93 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$93 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$93;
                        }
                        try {
                            ObjectInstance objectInstanceCreateMBean = rMIConnectionImpl.createMBean(str3, objectName9, (Subject) inputStream2.read_value(clsClass$93));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream41 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$ObjectInstance != null) {
                                clsClass$99 = class$javax$management$ObjectInstance;
                            } else {
                                clsClass$99 = class$("javax.management.ObjectInstance");
                                class$javax$management$ObjectInstance = clsClass$99;
                            }
                            outputStream41.write_value(objectInstanceCreateMBean, clsClass$99);
                            return outputStream41;
                        } catch (IOException e33) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream42 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream42.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$98 = class$java$io$IOException;
                            } else {
                                clsClass$98 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$98;
                            }
                            outputStream42.write_value(e33, clsClass$98);
                            return outputStream42;
                        } catch (InstanceAlreadyExistsException e34) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream43 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream43.write_string("IDL:javax/management/InstanceAlreadyExistsEx:1.0");
                            if (class$javax$management$InstanceAlreadyExistsException != null) {
                                clsClass$97 = class$javax$management$InstanceAlreadyExistsException;
                            } else {
                                clsClass$97 = class$("javax.management.InstanceAlreadyExistsException");
                                class$javax$management$InstanceAlreadyExistsException = clsClass$97;
                            }
                            outputStream43.write_value(e34, clsClass$97);
                            return outputStream43;
                        } catch (MBeanException e35) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream44 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream44.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$96 = class$javax$management$MBeanException;
                            } else {
                                clsClass$96 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$96;
                            }
                            outputStream44.write_value(e35, clsClass$96);
                            return outputStream44;
                        } catch (NotCompliantMBeanException e36) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream45 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream45.write_string("IDL:javax/management/NotCompliantMBeanEx:1.0");
                            if (class$javax$management$NotCompliantMBeanException != null) {
                                clsClass$95 = class$javax$management$NotCompliantMBeanException;
                            } else {
                                clsClass$95 = class$("javax.management.NotCompliantMBeanException");
                                class$javax$management$NotCompliantMBeanException = clsClass$95;
                            }
                            outputStream45.write_value(e36, clsClass$95);
                            return outputStream45;
                        } catch (ReflectionException e37) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream46 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream46.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$94 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$94 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$94;
                            }
                            outputStream46.write_value(e37, clsClass$94);
                            return outputStream46;
                        }
                    }
                    if (str.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject")) {
                        if (class$java$lang$String != null) {
                            clsClass$80 = class$java$lang$String;
                        } else {
                            clsClass$80 = class$("java.lang.String");
                            class$java$lang$String = clsClass$80;
                        }
                        String str4 = (String) inputStream2.read_value(clsClass$80);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$81 = class$javax$management$ObjectName;
                        } else {
                            clsClass$81 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$81;
                        }
                        ObjectName objectName10 = (ObjectName) inputStream2.read_value(clsClass$81);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$82 = class$javax$management$ObjectName;
                        } else {
                            clsClass$82 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$82;
                        }
                        ObjectName objectName11 = (ObjectName) inputStream2.read_value(clsClass$82);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$83 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$83 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$83;
                        }
                        try {
                            ObjectInstance objectInstanceCreateMBean2 = rMIConnectionImpl.createMBean(str4, objectName10, objectName11, (Subject) inputStream2.read_value(clsClass$83));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream47 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$ObjectInstance != null) {
                                clsClass$90 = class$javax$management$ObjectInstance;
                            } else {
                                clsClass$90 = class$("javax.management.ObjectInstance");
                                class$javax$management$ObjectInstance = clsClass$90;
                            }
                            outputStream47.write_value(objectInstanceCreateMBean2, clsClass$90);
                            return outputStream47;
                        } catch (IOException e38) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream48 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream48.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$89 = class$java$io$IOException;
                            } else {
                                clsClass$89 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$89;
                            }
                            outputStream48.write_value(e38, clsClass$89);
                            return outputStream48;
                        } catch (InstanceAlreadyExistsException e39) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream49 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream49.write_string("IDL:javax/management/InstanceAlreadyExistsEx:1.0");
                            if (class$javax$management$InstanceAlreadyExistsException != null) {
                                clsClass$88 = class$javax$management$InstanceAlreadyExistsException;
                            } else {
                                clsClass$88 = class$("javax.management.InstanceAlreadyExistsException");
                                class$javax$management$InstanceAlreadyExistsException = clsClass$88;
                            }
                            outputStream49.write_value(e39, clsClass$88);
                            return outputStream49;
                        } catch (InstanceNotFoundException e40) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream50 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream50.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$87 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$87 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$87;
                            }
                            outputStream50.write_value(e40, clsClass$87);
                            return outputStream50;
                        } catch (MBeanException e41) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream51 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream51.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$86 = class$javax$management$MBeanException;
                            } else {
                                clsClass$86 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$86;
                            }
                            outputStream51.write_value(e41, clsClass$86);
                            return outputStream51;
                        } catch (NotCompliantMBeanException e42) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream52 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream52.write_string("IDL:javax/management/NotCompliantMBeanEx:1.0");
                            if (class$javax$management$NotCompliantMBeanException != null) {
                                clsClass$85 = class$javax$management$NotCompliantMBeanException;
                            } else {
                                clsClass$85 = class$("javax.management.NotCompliantMBeanException");
                                class$javax$management$NotCompliantMBeanException = clsClass$85;
                            }
                            outputStream52.write_value(e42, clsClass$85);
                            return outputStream52;
                        } catch (ReflectionException e43) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream53 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream53.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$84 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$84 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$84;
                            }
                            outputStream53.write_value(e43, clsClass$84);
                            return outputStream53;
                        }
                    }
                    if (str.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject")) {
                        if (class$java$lang$String != null) {
                            clsClass$69 = class$java$lang$String;
                        } else {
                            clsClass$69 = class$("java.lang.String");
                            class$java$lang$String = clsClass$69;
                        }
                        String str5 = (String) inputStream2.read_value(clsClass$69);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$70 = class$javax$management$ObjectName;
                        } else {
                            clsClass$70 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$70;
                        }
                        ObjectName objectName12 = (ObjectName) inputStream2.read_value(clsClass$70);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$71 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$71 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$71;
                        }
                        MarshalledObject marshalledObject5 = (MarshalledObject) inputStream2.read_value(clsClass$71);
                        if (array$Ljava$lang$String != null) {
                            clsClass$72 = array$Ljava$lang$String;
                        } else {
                            clsClass$72 = class$("[Ljava.lang.String;");
                            array$Ljava$lang$String = clsClass$72;
                        }
                        String[] strArr2 = (String[]) inputStream2.read_value(clsClass$72);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$73 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$73 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$73;
                        }
                        try {
                            ObjectInstance objectInstanceCreateMBean3 = rMIConnectionImpl.createMBean(str5, objectName12, marshalledObject5, strArr2, (Subject) inputStream2.read_value(clsClass$73));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream54 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$ObjectInstance != null) {
                                clsClass$79 = class$javax$management$ObjectInstance;
                            } else {
                                clsClass$79 = class$("javax.management.ObjectInstance");
                                class$javax$management$ObjectInstance = clsClass$79;
                            }
                            outputStream54.write_value(objectInstanceCreateMBean3, clsClass$79);
                            return outputStream54;
                        } catch (IOException e44) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream55 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream55.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$78 = class$java$io$IOException;
                            } else {
                                clsClass$78 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$78;
                            }
                            outputStream55.write_value(e44, clsClass$78);
                            return outputStream55;
                        } catch (InstanceAlreadyExistsException e45) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream56 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream56.write_string("IDL:javax/management/InstanceAlreadyExistsEx:1.0");
                            if (class$javax$management$InstanceAlreadyExistsException != null) {
                                clsClass$77 = class$javax$management$InstanceAlreadyExistsException;
                            } else {
                                clsClass$77 = class$("javax.management.InstanceAlreadyExistsException");
                                class$javax$management$InstanceAlreadyExistsException = clsClass$77;
                            }
                            outputStream56.write_value(e45, clsClass$77);
                            return outputStream56;
                        } catch (MBeanException e46) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream57 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream57.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$76 = class$javax$management$MBeanException;
                            } else {
                                clsClass$76 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$76;
                            }
                            outputStream57.write_value(e46, clsClass$76);
                            return outputStream57;
                        } catch (NotCompliantMBeanException e47) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream58 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream58.write_string("IDL:javax/management/NotCompliantMBeanEx:1.0");
                            if (class$javax$management$NotCompliantMBeanException != null) {
                                clsClass$75 = class$javax$management$NotCompliantMBeanException;
                            } else {
                                clsClass$75 = class$("javax.management.NotCompliantMBeanException");
                                class$javax$management$NotCompliantMBeanException = clsClass$75;
                            }
                            outputStream58.write_value(e47, clsClass$75);
                            return outputStream58;
                        } catch (ReflectionException e48) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream59 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream59.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$74 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$74 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$74;
                            }
                            outputStream59.write_value(e48, clsClass$74);
                            return outputStream59;
                        }
                    }
                    if (str.equals("createMBean__CORBA_WStringValue__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__org_omg_boxedRMI_CORBA_seq1_WStringValue__javax_security_auth_Subject")) {
                        if (class$java$lang$String != null) {
                            clsClass$56 = class$java$lang$String;
                        } else {
                            clsClass$56 = class$("java.lang.String");
                            class$java$lang$String = clsClass$56;
                        }
                        String str6 = (String) inputStream2.read_value(clsClass$56);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$57 = class$javax$management$ObjectName;
                        } else {
                            clsClass$57 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$57;
                        }
                        ObjectName objectName13 = (ObjectName) inputStream2.read_value(clsClass$57);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$58 = class$javax$management$ObjectName;
                        } else {
                            clsClass$58 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$58;
                        }
                        ObjectName objectName14 = (ObjectName) inputStream2.read_value(clsClass$58);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$59 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$59 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$59;
                        }
                        MarshalledObject marshalledObject6 = (MarshalledObject) inputStream2.read_value(clsClass$59);
                        if (array$Ljava$lang$String != null) {
                            clsClass$60 = array$Ljava$lang$String;
                        } else {
                            clsClass$60 = class$("[Ljava.lang.String;");
                            array$Ljava$lang$String = clsClass$60;
                        }
                        String[] strArr3 = (String[]) inputStream2.read_value(clsClass$60);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$61 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$61 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$61;
                        }
                        try {
                            ObjectInstance objectInstanceCreateMBean4 = rMIConnectionImpl.createMBean(str6, objectName13, objectName14, marshalledObject6, strArr3, (Subject) inputStream2.read_value(clsClass$61));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream60 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$ObjectInstance != null) {
                                clsClass$68 = class$javax$management$ObjectInstance;
                            } else {
                                clsClass$68 = class$("javax.management.ObjectInstance");
                                class$javax$management$ObjectInstance = clsClass$68;
                            }
                            outputStream60.write_value(objectInstanceCreateMBean4, clsClass$68);
                            return outputStream60;
                        } catch (IOException e49) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream61 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream61.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$67 = class$java$io$IOException;
                            } else {
                                clsClass$67 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$67;
                            }
                            outputStream61.write_value(e49, clsClass$67);
                            return outputStream61;
                        } catch (InstanceAlreadyExistsException e50) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream62 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream62.write_string("IDL:javax/management/InstanceAlreadyExistsEx:1.0");
                            if (class$javax$management$InstanceAlreadyExistsException != null) {
                                clsClass$66 = class$javax$management$InstanceAlreadyExistsException;
                            } else {
                                clsClass$66 = class$("javax.management.InstanceAlreadyExistsException");
                                class$javax$management$InstanceAlreadyExistsException = clsClass$66;
                            }
                            outputStream62.write_value(e50, clsClass$66);
                            return outputStream62;
                        } catch (InstanceNotFoundException e51) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream63 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream63.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$65 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$65 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$65;
                            }
                            outputStream63.write_value(e51, clsClass$65);
                            return outputStream63;
                        } catch (MBeanException e52) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream64 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream64.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$64 = class$javax$management$MBeanException;
                            } else {
                                clsClass$64 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$64;
                            }
                            outputStream64.write_value(e52, clsClass$64);
                            return outputStream64;
                        } catch (NotCompliantMBeanException e53) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream65 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream65.write_string("IDL:javax/management/NotCompliantMBeanEx:1.0");
                            if (class$javax$management$NotCompliantMBeanException != null) {
                                clsClass$63 = class$javax$management$NotCompliantMBeanException;
                            } else {
                                clsClass$63 = class$("javax.management.NotCompliantMBeanException");
                                class$javax$management$NotCompliantMBeanException = clsClass$63;
                            }
                            outputStream65.write_value(e53, clsClass$63);
                            return outputStream65;
                        } catch (ReflectionException e54) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream66 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream66.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$62 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$62 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$62;
                            }
                            outputStream66.write_value(e54, clsClass$62);
                            return outputStream66;
                        }
                    }
                case 'c':
                    if (str.equals("fetchNotifications")) {
                        try {
                            NotificationResult notificationResultFetchNotifications = rMIConnectionImpl.fetchNotifications(inputStream2.read_longlong(), inputStream2.read_long(), inputStream2.read_longlong());
                            org.omg.CORBA_2_3.portable.OutputStream outputStream67 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            if (class$javax$management$remote$NotificationResult != null) {
                                clsClass$55 = class$javax$management$remote$NotificationResult;
                            } else {
                                clsClass$55 = class$("javax.management.remote.NotificationResult");
                                class$javax$management$remote$NotificationResult = clsClass$55;
                            }
                            outputStream67.write_value(notificationResultFetchNotifications, clsClass$55);
                            return outputStream67;
                        } catch (IOException e55) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream68 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream68.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$54 = class$java$io$IOException;
                            } else {
                                clsClass$54 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$54;
                            }
                            outputStream68.write_value(e55, clsClass$54);
                            return outputStream68;
                        }
                    }
                case 'e':
                    if (str.equals("unregisterMBean")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$49 = class$javax$management$ObjectName;
                        } else {
                            clsClass$49 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$49;
                        }
                        ObjectName objectName15 = (ObjectName) inputStream2.read_value(clsClass$49);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$50 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$50 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$50;
                        }
                        try {
                            rMIConnectionImpl.unregisterMBean(objectName15, (Subject) inputStream2.read_value(clsClass$50));
                            return responseHandler.createReply();
                        } catch (IOException e56) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream69 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream69.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$53 = class$java$io$IOException;
                            } else {
                                clsClass$53 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$53;
                            }
                            outputStream69.write_value(e56, clsClass$53);
                            return outputStream69;
                        } catch (InstanceNotFoundException e57) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream70 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream70.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$52 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$52 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$52;
                            }
                            outputStream70.write_value(e57, clsClass$52);
                            return outputStream70;
                        } catch (MBeanRegistrationException e58) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream71 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream71.write_string("IDL:javax/management/MBeanRegistrationEx:1.0");
                            if (class$javax$management$MBeanRegistrationException != null) {
                                clsClass$51 = class$javax$management$MBeanRegistrationException;
                            } else {
                                clsClass$51 = class$("javax.management.MBeanRegistrationException");
                                class$javax$management$MBeanRegistrationException = clsClass$51;
                            }
                            outputStream71.write_value(e58, clsClass$51);
                            return outputStream71;
                        }
                    }
                    if (str.equals("isRegistered")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$46 = class$javax$management$ObjectName;
                        } else {
                            clsClass$46 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$46;
                        }
                        ObjectName objectName16 = (ObjectName) inputStream2.read_value(clsClass$46);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$47 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$47 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$47;
                        }
                        try {
                            boolean zIsRegistered = rMIConnectionImpl.isRegistered(objectName16, (Subject) inputStream2.read_value(clsClass$47));
                            OutputStream outputStreamCreateReply2 = responseHandler.createReply();
                            outputStreamCreateReply2.write_boolean(zIsRegistered);
                            return outputStreamCreateReply2;
                        } catch (IOException e59) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream72 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream72.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$48 = class$java$io$IOException;
                            } else {
                                clsClass$48 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$48;
                            }
                            outputStream72.write_value(e59, clsClass$48);
                            return outputStream72;
                        }
                    }
                case 'n':
                    if (str.equals("isInstanceOf")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$41 = class$javax$management$ObjectName;
                        } else {
                            clsClass$41 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$41;
                        }
                        ObjectName objectName17 = (ObjectName) inputStream2.read_value(clsClass$41);
                        if (class$java$lang$String != null) {
                            clsClass$42 = class$java$lang$String;
                        } else {
                            clsClass$42 = class$("java.lang.String");
                            class$java$lang$String = clsClass$42;
                        }
                        String str7 = (String) inputStream2.read_value(clsClass$42);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$43 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$43 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$43;
                        }
                        try {
                            boolean zIsInstanceOf = rMIConnectionImpl.isInstanceOf(objectName17, str7, (Subject) inputStream2.read_value(clsClass$43));
                            OutputStream outputStreamCreateReply3 = responseHandler.createReply();
                            outputStreamCreateReply3.write_boolean(zIsInstanceOf);
                            return outputStreamCreateReply3;
                        } catch (IOException e60) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream73 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream73.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$45 = class$java$io$IOException;
                            } else {
                                clsClass$45 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$45;
                            }
                            outputStream73.write_value(e60, clsClass$45);
                            return outputStream73;
                        } catch (InstanceNotFoundException e61) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream74 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream74.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$44 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$44 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$44;
                            }
                            outputStream74.write_value(e61, clsClass$44);
                            return outputStream74;
                        }
                    }
                case 'o':
                    if (str.equals("invoke")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$32 = class$javax$management$ObjectName;
                        } else {
                            clsClass$32 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$32;
                        }
                        ObjectName objectName18 = (ObjectName) inputStream2.read_value(clsClass$32);
                        if (class$java$lang$String != null) {
                            clsClass$33 = class$java$lang$String;
                        } else {
                            clsClass$33 = class$("java.lang.String");
                            class$java$lang$String = clsClass$33;
                        }
                        String str8 = (String) inputStream2.read_value(clsClass$33);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$34 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$34 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$34;
                        }
                        MarshalledObject marshalledObject7 = (MarshalledObject) inputStream2.read_value(clsClass$34);
                        if (array$Ljava$lang$String != null) {
                            clsClass$35 = array$Ljava$lang$String;
                        } else {
                            clsClass$35 = class$("[Ljava.lang.String;");
                            array$Ljava$lang$String = clsClass$35;
                        }
                        String[] strArr4 = (String[]) inputStream2.read_value(clsClass$35);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$36 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$36 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$36;
                        }
                        try {
                            Object objInvoke = rMIConnectionImpl.invoke(objectName18, str8, marshalledObject7, strArr4, (Subject) inputStream2.read_value(clsClass$36));
                            OutputStream outputStreamCreateReply4 = responseHandler.createReply();
                            Util.writeAny(outputStreamCreateReply4, objInvoke);
                            return outputStreamCreateReply4;
                        } catch (IOException e62) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream75 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream75.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$40 = class$java$io$IOException;
                            } else {
                                clsClass$40 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$40;
                            }
                            outputStream75.write_value(e62, clsClass$40);
                            return outputStream75;
                        } catch (InstanceNotFoundException e63) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream76 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream76.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$39 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$39 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$39;
                            }
                            outputStream76.write_value(e63, clsClass$39);
                            return outputStream76;
                        } catch (MBeanException e64) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream77 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream77.write_string("IDL:javax/management/MBeanEx:1.0");
                            if (class$javax$management$MBeanException != null) {
                                clsClass$38 = class$javax$management$MBeanException;
                            } else {
                                clsClass$38 = class$("javax.management.MBeanException");
                                class$javax$management$MBeanException = clsClass$38;
                            }
                            outputStream77.write_value(e64, clsClass$38);
                            return outputStream77;
                        } catch (ReflectionException e65) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream78 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream78.write_string("IDL:javax/management/ReflectionEx:1.0");
                            if (class$javax$management$ReflectionException != null) {
                                clsClass$37 = class$javax$management$ReflectionException;
                            } else {
                                clsClass$37 = class$("javax.management.ReflectionException");
                                class$javax$management$ReflectionException = clsClass$37;
                            }
                            outputStream78.write_value(e65, clsClass$37);
                            return outputStream78;
                        }
                    }
                    if (str.equals("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__javax_security_auth_Subject")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$26 = class$javax$management$ObjectName;
                        } else {
                            clsClass$26 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$26;
                        }
                        ObjectName objectName19 = (ObjectName) inputStream2.read_value(clsClass$26);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$27 = class$javax$management$ObjectName;
                        } else {
                            clsClass$27 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$27;
                        }
                        ObjectName objectName20 = (ObjectName) inputStream2.read_value(clsClass$27);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$28 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$28 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$28;
                        }
                        try {
                            rMIConnectionImpl.removeNotificationListener(objectName19, objectName20, (Subject) inputStream2.read_value(clsClass$28));
                            return responseHandler.createReply();
                        } catch (IOException e66) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream79 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream79.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$31 = class$java$io$IOException;
                            } else {
                                clsClass$31 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$31;
                            }
                            outputStream79.write_value(e66, clsClass$31);
                            return outputStream79;
                        } catch (InstanceNotFoundException e67) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream80 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream80.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$30 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$30 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$30;
                            }
                            outputStream80.write_value(e67, clsClass$30);
                            return outputStream80;
                        } catch (ListenerNotFoundException e68) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream81 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream81.write_string("IDL:javax/management/ListenerNotFoundEx:1.0");
                            if (class$javax$management$ListenerNotFoundException != null) {
                                clsClass$29 = class$javax$management$ListenerNotFoundException;
                            } else {
                                clsClass$29 = class$("javax.management.ListenerNotFoundException");
                                class$javax$management$ListenerNotFoundException = clsClass$29;
                            }
                            outputStream81.write_value(e68, clsClass$29);
                            return outputStream81;
                        }
                    }
                    if (str.equals("removeNotificationListener__javax_management_ObjectName__javax_management_ObjectName__java_rmi_MarshalledObject__java_rmi_MarshalledObject__javax_security_auth_Subject")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$18 = class$javax$management$ObjectName;
                        } else {
                            clsClass$18 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$18;
                        }
                        ObjectName objectName21 = (ObjectName) inputStream2.read_value(clsClass$18);
                        if (class$javax$management$ObjectName != null) {
                            clsClass$19 = class$javax$management$ObjectName;
                        } else {
                            clsClass$19 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$19;
                        }
                        ObjectName objectName22 = (ObjectName) inputStream2.read_value(clsClass$19);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$20 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$20 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$20;
                        }
                        MarshalledObject marshalledObject8 = (MarshalledObject) inputStream2.read_value(clsClass$20);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$21 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$21 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$21;
                        }
                        MarshalledObject marshalledObject9 = (MarshalledObject) inputStream2.read_value(clsClass$21);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$22 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$22 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$22;
                        }
                        try {
                            rMIConnectionImpl.removeNotificationListener(objectName21, objectName22, marshalledObject8, marshalledObject9, (Subject) inputStream2.read_value(clsClass$22));
                            return responseHandler.createReply();
                        } catch (IOException e69) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream82 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream82.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$25 = class$java$io$IOException;
                            } else {
                                clsClass$25 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$25;
                            }
                            outputStream82.write_value(e69, clsClass$25);
                            return outputStream82;
                        } catch (InstanceNotFoundException e70) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream83 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream83.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$24 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$24 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$24;
                            }
                            outputStream83.write_value(e70, clsClass$24);
                            return outputStream83;
                        } catch (ListenerNotFoundException e71) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream84 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream84.write_string("IDL:javax/management/ListenerNotFoundEx:1.0");
                            if (class$javax$management$ListenerNotFoundException != null) {
                                clsClass$23 = class$javax$management$ListenerNotFoundException;
                            } else {
                                clsClass$23 = class$("javax.management.ListenerNotFoundException");
                                class$javax$management$ListenerNotFoundException = clsClass$23;
                            }
                            outputStream84.write_value(e71, clsClass$23);
                            return outputStream84;
                        }
                    }
                    if (str.equals("removeNotificationListeners")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$12 = class$javax$management$ObjectName;
                        } else {
                            clsClass$12 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$12;
                        }
                        ObjectName objectName23 = (ObjectName) inputStream2.read_value(clsClass$12);
                        if (array$Ljava$lang$Integer != null) {
                            clsClass$13 = array$Ljava$lang$Integer;
                        } else {
                            clsClass$13 = class$("[Ljava.lang.Integer;");
                            array$Ljava$lang$Integer = clsClass$13;
                        }
                        Integer[] numArr = (Integer[]) inputStream2.read_value(clsClass$13);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$14 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$14 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$14;
                        }
                        try {
                            rMIConnectionImpl.removeNotificationListeners(objectName23, numArr, (Subject) inputStream2.read_value(clsClass$14));
                            return responseHandler.createReply();
                        } catch (IOException e72) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream85 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream85.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$17 = class$java$io$IOException;
                            } else {
                                clsClass$17 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$17;
                            }
                            outputStream85.write_value(e72, clsClass$17);
                            return outputStream85;
                        } catch (InstanceNotFoundException e73) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream86 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream86.write_string("IDL:javax/management/InstanceNotFoundEx:1.0");
                            if (class$javax$management$InstanceNotFoundException != null) {
                                clsClass$16 = class$javax$management$InstanceNotFoundException;
                            } else {
                                clsClass$16 = class$("javax.management.InstanceNotFoundException");
                                class$javax$management$InstanceNotFoundException = clsClass$16;
                            }
                            outputStream86.write_value(e73, clsClass$16);
                            return outputStream86;
                        } catch (ListenerNotFoundException e74) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream87 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream87.write_string("IDL:javax/management/ListenerNotFoundEx:1.0");
                            if (class$javax$management$ListenerNotFoundException != null) {
                                clsClass$15 = class$javax$management$ListenerNotFoundException;
                            } else {
                                clsClass$15 = class$("javax.management.ListenerNotFoundException");
                                class$javax$management$ListenerNotFoundException = clsClass$15;
                            }
                            outputStream87.write_value(e74, clsClass$15);
                            return outputStream87;
                        }
                    }
                case 'r':
                    if (str.equals("queryMBeans")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$7 = class$javax$management$ObjectName;
                        } else {
                            clsClass$7 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$7;
                        }
                        ObjectName objectName24 = (ObjectName) inputStream2.read_value(clsClass$7);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$8 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$8 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$8;
                        }
                        MarshalledObject marshalledObject10 = (MarshalledObject) inputStream2.read_value(clsClass$8);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$9 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$9 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$9;
                        }
                        try {
                            Set<ObjectInstance> setQueryMBeans = rMIConnectionImpl.queryMBeans(objectName24, marshalledObject10, (Subject) inputStream2.read_value(clsClass$9));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream88 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            Serializable serializable = (Serializable) setQueryMBeans;
                            if (class$java$util$Set != null) {
                                clsClass$11 = class$java$util$Set;
                            } else {
                                clsClass$11 = class$("java.util.Set");
                                class$java$util$Set = clsClass$11;
                            }
                            outputStream88.write_value(serializable, clsClass$11);
                            return outputStream88;
                        } catch (IOException e75) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream89 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream89.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$10 = class$java$io$IOException;
                            } else {
                                clsClass$10 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$10;
                            }
                            outputStream89.write_value(e75, clsClass$10);
                            return outputStream89;
                        }
                    }
                    if (str.equals("queryNames")) {
                        if (class$javax$management$ObjectName != null) {
                            clsClass$2 = class$javax$management$ObjectName;
                        } else {
                            clsClass$2 = class$("javax.management.ObjectName");
                            class$javax$management$ObjectName = clsClass$2;
                        }
                        ObjectName objectName25 = (ObjectName) inputStream2.read_value(clsClass$2);
                        if (class$java$rmi$MarshalledObject != null) {
                            clsClass$3 = class$java$rmi$MarshalledObject;
                        } else {
                            clsClass$3 = class$("java.rmi.MarshalledObject");
                            class$java$rmi$MarshalledObject = clsClass$3;
                        }
                        MarshalledObject marshalledObject11 = (MarshalledObject) inputStream2.read_value(clsClass$3);
                        if (class$javax$security$auth$Subject != null) {
                            clsClass$4 = class$javax$security$auth$Subject;
                        } else {
                            clsClass$4 = class$("javax.security.auth.Subject");
                            class$javax$security$auth$Subject = clsClass$4;
                        }
                        try {
                            Set<ObjectName> setQueryNames = rMIConnectionImpl.queryNames(objectName25, marshalledObject11, (Subject) inputStream2.read_value(clsClass$4));
                            org.omg.CORBA_2_3.portable.OutputStream outputStream90 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
                            Serializable serializable2 = (Serializable) setQueryNames;
                            if (class$java$util$Set != null) {
                                clsClass$6 = class$java$util$Set;
                            } else {
                                clsClass$6 = class$("java.util.Set");
                                class$java$util$Set = clsClass$6;
                            }
                            outputStream90.write_value(serializable2, clsClass$6);
                            return outputStream90;
                        } catch (IOException e76) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream91 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream91.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$5 = class$java$io$IOException;
                            } else {
                                clsClass$5 = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$5;
                            }
                            outputStream91.write_value(e76, clsClass$5);
                            return outputStream91;
                        }
                    }
                case 's':
                    if (str.equals("close")) {
                        try {
                            rMIConnectionImpl.close();
                            return responseHandler.createReply();
                        } catch (IOException e77) {
                            org.omg.CORBA_2_3.portable.OutputStream outputStream92 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                            outputStream92.write_string("IDL:java/io/IOEx:1.0");
                            if (class$java$io$IOException != null) {
                                clsClass$ = class$java$io$IOException;
                            } else {
                                clsClass$ = class$("java.io.IOException");
                                class$java$io$IOException = clsClass$;
                            }
                            outputStream92.write_value(e77, clsClass$);
                            return outputStream92;
                        }
                    }
                default:
                    throw new BAD_OPERATION();
            }
        } catch (SystemException e78) {
            throw e78;
        } catch (Throwable th) {
            throw new UnknownException(th);
        }
    }

    private Serializable cast_array(Object obj) {
        return (Serializable) obj;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // javax.rmi.CORBA.Tie
    public void deactivate() {
        _orb().disconnect(this);
        _set_delegate(null);
        this.target = null;
    }

    @Override // javax.rmi.CORBA.Tie
    public Remote getTarget() {
        return this.target;
    }

    @Override // javax.rmi.CORBA.Tie
    public ORB orb() {
        return _orb();
    }

    @Override // javax.rmi.CORBA.Tie
    public void orb(ORB orb) {
        orb.connect(this);
    }

    @Override // javax.rmi.CORBA.Tie
    public void setTarget(Remote remote) {
        this.target = (RMIConnectionImpl) remote;
    }

    @Override // javax.rmi.CORBA.Tie
    public Object thisObject() {
        return this;
    }
}
