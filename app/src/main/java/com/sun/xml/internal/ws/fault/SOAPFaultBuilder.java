package com.sun.xml.internal.ws.fault;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.model.ExceptionType;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import com.sun.xml.internal.ws.message.FaultMessage;
import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.util.DOMUtil;
import com.sun.xml.internal.ws.util.StringUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/SOAPFaultBuilder.class */
public abstract class SOAPFaultBuilder {
    private static final JAXBContext JAXB_CONTEXT;
    public static final boolean captureStackTrace;
    private static final Logger logger = Logger.getLogger(SOAPFaultBuilder.class.getName());
    static final String CAPTURE_STACK_TRACE_PROPERTY = SOAPFaultBuilder.class.getName() + ".captureStackTrace";

    abstract DetailType getDetail();

    abstract void setDetail(DetailType detailType);

    abstract String getFaultString();

    protected abstract Throwable getProtocolException();

    @XmlTransient
    @Nullable
    public QName getFirstDetailEntryName() {
        Node entry;
        DetailType dt = getDetail();
        if (dt != null && (entry = dt.getDetail(0)) != null) {
            return new QName(entry.getNamespaceURI(), entry.getLocalName());
        }
        return null;
    }

    public Throwable createException(Map<QName, CheckedExceptionImpl> exceptions) throws JAXBException {
        DetailType dt = getDetail();
        Node detail = null;
        if (dt != null) {
            detail = dt.getDetail(0);
        }
        if (detail == null || exceptions == null) {
            return attachServerException(getProtocolException());
        }
        QName detailName = new QName(detail.getNamespaceURI(), detail.getLocalName());
        CheckedExceptionImpl ce = exceptions.get(detailName);
        if (ce == null) {
            return attachServerException(getProtocolException());
        }
        if (ce.getExceptionType().equals(ExceptionType.UserDefined)) {
            return attachServerException(createUserDefinedException(ce));
        }
        Class exceptionClass = ce.getExceptionClass();
        try {
            Constructor constructor = exceptionClass.getConstructor(String.class, (Class) ce.getDetailType().type);
            Exception exception = (Exception) constructor.newInstance(getFaultString(), getJAXBObject(detail, ce));
            return attachServerException(exception);
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    @NotNull
    public static Message createSOAPFaultMessage(@NotNull SOAPVersion soapVersion, @NotNull ProtocolException ex, @Nullable QName faultcode) {
        Object detail = getFaultDetail(null, ex);
        if (soapVersion == SOAPVersion.SOAP_12) {
            return createSOAP12Fault(soapVersion, ex, detail, null, faultcode);
        }
        return createSOAP11Fault(soapVersion, ex, detail, null, faultcode);
    }

    public static Message createSOAPFaultMessage(SOAPVersion soapVersion, CheckedExceptionImpl ceModel, Throwable ex) {
        Throwable t2 = ex instanceof InvocationTargetException ? ((InvocationTargetException) ex).getTargetException() : ex;
        return createSOAPFaultMessage(soapVersion, ceModel, t2, (QName) null);
    }

    public static Message createSOAPFaultMessage(SOAPVersion soapVersion, CheckedExceptionImpl ceModel, Throwable ex, QName faultCode) {
        Object detail = getFaultDetail(ceModel, ex);
        if (soapVersion == SOAPVersion.SOAP_12) {
            return createSOAP12Fault(soapVersion, ex, detail, ceModel, faultCode);
        }
        return createSOAP11Fault(soapVersion, ex, detail, ceModel, faultCode);
    }

    public static Message createSOAPFaultMessage(SOAPVersion soapVersion, String faultString, QName faultCode) {
        if (faultCode == null) {
            faultCode = getDefaultFaultCode(soapVersion);
        }
        return createSOAPFaultMessage(soapVersion, faultString, faultCode, (Element) null);
    }

    public static Message createSOAPFaultMessage(SOAPVersion soapVersion, SOAPFault fault) {
        switch (soapVersion) {
            case SOAP_11:
                return JAXBMessage.create(JAXB_CONTEXT, new SOAP11Fault(fault), soapVersion);
            case SOAP_12:
                return JAXBMessage.create(JAXB_CONTEXT, new SOAP12Fault(fault), soapVersion);
            default:
                throw new AssertionError();
        }
    }

    private static Message createSOAPFaultMessage(SOAPVersion soapVersion, String faultString, QName faultCode, Element detail) {
        switch (soapVersion) {
            case SOAP_11:
                return JAXBMessage.create(JAXB_CONTEXT, new SOAP11Fault(faultCode, faultString, null, detail), soapVersion);
            case SOAP_12:
                return JAXBMessage.create(JAXB_CONTEXT, new SOAP12Fault(faultCode, faultString, detail), soapVersion);
            default:
                throw new AssertionError();
        }
    }

    final void captureStackTrace(@Nullable Throwable t2) {
        if (t2 != null && captureStackTrace) {
            try {
                Document d2 = DOMUtil.createDom();
                ExceptionBean.marshal(t2, d2);
                DetailType detail = getDetail();
                if (detail == null) {
                    DetailType detailType = new DetailType();
                    detail = detailType;
                    setDetail(detailType);
                }
                detail.getDetails().add(d2.getDocumentElement());
            } catch (JAXBException e2) {
                logger.log(Level.WARNING, "Unable to capture the stack trace into XML", (Throwable) e2);
            }
        }
    }

    private <T extends Throwable> T attachServerException(T t2) {
        DetailType detail = getDetail();
        if (detail == null) {
            return t2;
        }
        for (Element n2 : detail.getDetails()) {
            if (ExceptionBean.isStackTraceXml(n2)) {
                try {
                    t2.initCause(ExceptionBean.unmarshal(n2));
                } catch (JAXBException e2) {
                    logger.log(Level.WARNING, "Unable to read the capture stack trace in the fault", (Throwable) e2);
                }
                return t2;
            }
        }
        return t2;
    }

    private Object getJAXBObject(Node jaxbBean, CheckedExceptionImpl ce) throws JAXBException {
        XMLBridge bridge = ce.getBond();
        return bridge.unmarshal(jaxbBean, (AttachmentUnmarshaller) null);
    }

    private Exception createUserDefinedException(CheckedExceptionImpl ce) {
        Class exceptionClass = ce.getExceptionClass();
        Class detailBean = ce.getDetailBean();
        try {
            Node detailNode = getDetail().getDetails().get(0);
            Object jaxbDetail = getJAXBObject(detailNode, ce);
            try {
                Constructor exConstructor = exceptionClass.getConstructor(String.class, detailBean);
                return (Exception) exConstructor.newInstance(getFaultString(), jaxbDetail);
            } catch (NoSuchMethodException e2) {
                Constructor exConstructor2 = exceptionClass.getConstructor(String.class);
                return (Exception) exConstructor2.newInstance(getFaultString());
            }
        } catch (Exception e3) {
            throw new WebServiceException(e3);
        }
    }

    private static String getWriteMethod(Field f2) {
        return "set" + StringUtils.capitalize(f2.getName());
    }

    private static Object getFaultDetail(CheckedExceptionImpl ce, Throwable exception) {
        if (ce == null) {
            return null;
        }
        if (ce.getExceptionType().equals(ExceptionType.UserDefined)) {
            return createDetailFromUserDefinedException(ce, exception);
        }
        try {
            Method m2 = exception.getClass().getMethod("getFaultInfo", new Class[0]);
            return m2.invoke(exception, new Object[0]);
        } catch (Exception e2) {
            throw new SerializationException(e2);
        }
    }

    private static Object createDetailFromUserDefinedException(CheckedExceptionImpl ce, Object exception) throws SecurityException {
        Class detailBean = ce.getDetailBean();
        Field[] fields = detailBean.getDeclaredFields();
        try {
            Object detail = detailBean.newInstance();
            for (Field f2 : fields) {
                Method em = exception.getClass().getMethod(getReadMethod(f2), new Class[0]);
                try {
                    Method sm = detailBean.getMethod(getWriteMethod(f2), em.getReturnType());
                    sm.invoke(detail, em.invoke(exception, new Object[0]));
                } catch (NoSuchMethodException e2) {
                    Field sf = detailBean.getField(f2.getName());
                    sf.set(detail, em.invoke(exception, new Object[0]));
                }
            }
            return detail;
        } catch (Exception e3) {
            throw new SerializationException(e3);
        }
    }

    private static String getReadMethod(Field f2) {
        if (f2.getType().isAssignableFrom(Boolean.TYPE)) {
            return BeanAdapter.IS_PREFIX + StringUtils.capitalize(f2.getName());
        }
        return "get" + StringUtils.capitalize(f2.getName());
    }

    private static Message createSOAP11Fault(SOAPVersion soapVersion, Throwable e2, Object detail, CheckedExceptionImpl ce, QName faultCode) {
        SOAPFaultException soapFaultException = null;
        String faultString = null;
        String faultActor = null;
        Throwable cause = e2.getCause();
        if (e2 instanceof SOAPFaultException) {
            soapFaultException = (SOAPFaultException) e2;
        } else if (cause != null && (cause instanceof SOAPFaultException)) {
            soapFaultException = (SOAPFaultException) e2.getCause();
        }
        if (soapFaultException != null) {
            QName soapFaultCode = soapFaultException.getFault().getFaultCodeAsQName();
            if (soapFaultCode != null) {
                faultCode = soapFaultCode;
            }
            faultString = soapFaultException.getFault().getFaultString();
            faultActor = soapFaultException.getFault().getFaultActor();
        }
        if (faultCode == null) {
            faultCode = getDefaultFaultCode(soapVersion);
        }
        if (faultString == null) {
            faultString = e2.getMessage();
            if (faultString == null) {
                faultString = e2.toString();
            }
        }
        Element detailNode = null;
        QName firstEntry = null;
        if (detail == null && soapFaultException != null) {
            detailNode = soapFaultException.getFault().getDetail();
            firstEntry = getFirstDetailEntryName((Detail) detailNode);
        } else if (ce != null) {
            try {
                DOMResult dr = new DOMResult();
                ce.getBond().marshal((XMLBridge) detail, (Result) dr);
                detailNode = (Element) dr.getNode().getFirstChild();
                firstEntry = getFirstDetailEntryName(detailNode);
            } catch (JAXBException e3) {
                faultString = e2.getMessage();
                faultCode = getDefaultFaultCode(soapVersion);
            }
        }
        SOAP11Fault soap11Fault = new SOAP11Fault(faultCode, faultString, faultActor, detailNode);
        if (ce == null) {
            soap11Fault.captureStackTrace(e2);
        }
        Message msg = JAXBMessage.create(JAXB_CONTEXT, soap11Fault, soapVersion);
        return new FaultMessage(msg, firstEntry);
    }

    @Nullable
    private static QName getFirstDetailEntryName(@Nullable Detail detail) {
        if (detail != null) {
            Iterator<DetailEntry> it = detail.getDetailEntries();
            if (it.hasNext()) {
                DetailEntry entry = it.next();
                return getFirstDetailEntryName(entry);
            }
            return null;
        }
        return null;
    }

    @NotNull
    private static QName getFirstDetailEntryName(@NotNull Element entry) {
        return new QName(entry.getNamespaceURI(), entry.getLocalName());
    }

    private static Message createSOAP12Fault(SOAPVersion soapVersion, Throwable e2, Object detail, CheckedExceptionImpl ce, QName faultCode) {
        SOAPFaultException soapFaultException = null;
        CodeType code = null;
        String faultString = null;
        String faultRole = null;
        String faultNode = null;
        Throwable cause = e2.getCause();
        if (e2 instanceof SOAPFaultException) {
            soapFaultException = (SOAPFaultException) e2;
        } else if (cause != null && (cause instanceof SOAPFaultException)) {
            soapFaultException = (SOAPFaultException) e2.getCause();
        }
        if (soapFaultException != null) {
            SOAPFault fault = soapFaultException.getFault();
            QName soapFaultCode = fault.getFaultCodeAsQName();
            if (soapFaultCode != null) {
                faultCode = soapFaultCode;
                code = new CodeType(faultCode);
                Iterator iter = fault.getFaultSubcodes();
                boolean first = true;
                SubcodeType subcode = null;
                while (iter.hasNext()) {
                    QName value = (QName) iter.next();
                    if (first) {
                        SubcodeType sct = new SubcodeType(value);
                        code.setSubcode(sct);
                        subcode = sct;
                        first = false;
                    } else {
                        subcode = fillSubcodes(subcode, value);
                    }
                }
            }
            faultString = soapFaultException.getFault().getFaultString();
            faultRole = soapFaultException.getFault().getFaultActor();
            faultNode = soapFaultException.getFault().getFaultNode();
        }
        if (faultCode == null) {
            code = new CodeType(getDefaultFaultCode(soapVersion));
        } else if (code == null) {
            code = new CodeType(faultCode);
        }
        if (faultString == null) {
            faultString = e2.getMessage();
            if (faultString == null) {
                faultString = e2.toString();
            }
        }
        ReasonType reason = new ReasonType(faultString);
        Element detailNode = null;
        QName firstEntry = null;
        if (detail == null && soapFaultException != null) {
            detailNode = soapFaultException.getFault().getDetail();
            firstEntry = getFirstDetailEntryName((Detail) detailNode);
        } else if (detail != null) {
            try {
                DOMResult dr = new DOMResult();
                ce.getBond().marshal((XMLBridge) detail, (Result) dr);
                detailNode = (Element) dr.getNode().getFirstChild();
                firstEntry = getFirstDetailEntryName(detailNode);
            } catch (JAXBException e3) {
                e2.getMessage();
            }
        }
        SOAP12Fault soap12Fault = new SOAP12Fault(code, reason, faultNode, faultRole, detailNode);
        if (ce == null) {
            soap12Fault.captureStackTrace(e2);
        }
        Message msg = JAXBMessage.create(JAXB_CONTEXT, soap12Fault, soapVersion);
        return new FaultMessage(msg, firstEntry);
    }

    private static SubcodeType fillSubcodes(SubcodeType parent, QName value) {
        SubcodeType newCode = new SubcodeType(value);
        parent.setSubcode(newCode);
        return newCode;
    }

    private static QName getDefaultFaultCode(SOAPVersion soapVersion) {
        return soapVersion.faultCodeServer;
    }

    public static SOAPFaultBuilder create(Message msg) throws JAXBException {
        return (SOAPFaultBuilder) msg.readPayloadAsJAXB(JAXB_CONTEXT.createUnmarshaller());
    }

    static {
        boolean tmpVal = false;
        try {
            tmpVal = Boolean.getBoolean(CAPTURE_STACK_TRACE_PROPERTY);
        } catch (SecurityException e2) {
        }
        captureStackTrace = tmpVal;
        JAXB_CONTEXT = createJAXBContext();
    }

    private static JAXBContext createJAXBContext() {
        if (isJDKRuntime()) {
            Permissions permissions = new Permissions();
            permissions.add(new RuntimePermission("accessClassInPackage.com.sun.xml.internal.ws.fault"));
            permissions.add(new ReflectPermission("suppressAccessChecks"));
            return (JAXBContext) AccessController.doPrivileged(new PrivilegedAction<JAXBContext>() { // from class: com.sun.xml.internal.ws.fault.SOAPFaultBuilder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public JAXBContext run2() {
                    try {
                        return JAXBContext.newInstance(SOAP11Fault.class, SOAP12Fault.class);
                    } catch (JAXBException e2) {
                        throw new Error(e2);
                    }
                }
            }, new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)}));
        }
        try {
            return JAXBContext.newInstance(SOAP11Fault.class, SOAP12Fault.class);
        } catch (JAXBException e2) {
            throw new Error(e2);
        }
    }

    private static boolean isJDKRuntime() {
        return SOAPFaultBuilder.class.getName().contains("internal");
    }
}
