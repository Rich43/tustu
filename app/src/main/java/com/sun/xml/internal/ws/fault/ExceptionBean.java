package com.sun.xml.internal.ws.fault;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.ws.developer.ServerSideException;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@XmlRootElement(namespace = ExceptionBean.NS, name = ExceptionBean.LOCAL_NAME)
/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/ExceptionBean.class */
final class ExceptionBean {

    @XmlAttribute(name = Constants.ATTRNAME_CLASS)
    public String className;

    @XmlElement
    public String message;

    @XmlElement(namespace = NS, name = "cause")
    public ExceptionBean cause;
    private static final JAXBContext JAXB_CONTEXT;
    static final String NS = "http://jax-ws.dev.java.net/";
    static final String LOCAL_NAME = "exception";
    private static final NamespacePrefixMapper nsp;

    @XmlElementWrapper(namespace = NS, name = "stackTrace")
    @XmlElement(namespace = NS, name = "frame")
    public List<StackFrame> stackTrace = new ArrayList();

    @XmlAttribute
    public String note = "To disable this feature, set " + SOAPFaultBuilder.CAPTURE_STACK_TRACE_PROPERTY + " system property to false";

    public static void marshal(Throwable t2, Node parent) throws JAXBException {
        Marshaller m2 = JAXB_CONTEXT.createMarshaller();
        try {
            m2.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", nsp);
        } catch (PropertyException e2) {
        }
        m2.marshal(new ExceptionBean(t2), parent);
    }

    public static ServerSideException unmarshal(Node xml) throws JAXBException {
        ExceptionBean e2 = (ExceptionBean) JAXB_CONTEXT.createUnmarshaller().unmarshal(xml);
        return e2.toException();
    }

    ExceptionBean() {
    }

    private ExceptionBean(Throwable t2) {
        this.className = t2.getClass().getName();
        this.message = t2.getMessage();
        for (StackTraceElement f2 : t2.getStackTrace()) {
            this.stackTrace.add(new StackFrame(f2));
        }
        Throwable cause = t2.getCause();
        if (t2 != cause && cause != null) {
            this.cause = new ExceptionBean(cause);
        }
    }

    private ServerSideException toException() {
        ServerSideException e2 = new ServerSideException(this.className, this.message);
        if (this.stackTrace != null) {
            StackTraceElement[] ste = new StackTraceElement[this.stackTrace.size()];
            for (int i2 = 0; i2 < this.stackTrace.size(); i2++) {
                ste[i2] = this.stackTrace.get(i2).toStackTraceElement();
            }
            e2.setStackTrace(ste);
        }
        if (this.cause != null) {
            e2.initCause(this.cause.toException());
        }
        return e2;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/fault/ExceptionBean$StackFrame.class */
    static final class StackFrame {

        @XmlAttribute(name = Constants.ATTRNAME_CLASS)
        public String declaringClass;

        @XmlAttribute(name = "method")
        public String methodName;

        @XmlAttribute(name = DeploymentDescriptorParser.ATTR_FILE)
        public String fileName;

        @XmlAttribute(name = "line")
        public String lineNumber;

        StackFrame() {
        }

        public StackFrame(StackTraceElement ste) {
            this.declaringClass = ste.getClassName();
            this.methodName = ste.getMethodName();
            this.fileName = ste.getFileName();
            this.lineNumber = box(ste.getLineNumber());
        }

        private String box(int i2) {
            return i2 >= 0 ? String.valueOf(i2) : i2 == -2 ? "native" : "unknown";
        }

        private int unbox(String v2) {
            try {
                return Integer.parseInt(v2);
            } catch (NumberFormatException e2) {
                if ("native".equals(v2)) {
                    return -2;
                }
                return -1;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public StackTraceElement toStackTraceElement() {
            return new StackTraceElement(this.declaringClass, this.methodName, this.fileName, unbox(this.lineNumber));
        }
    }

    public static boolean isStackTraceXml(Element n2) {
        return LOCAL_NAME.equals(n2.getLocalName()) && NS.equals(n2.getNamespaceURI());
    }

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(ExceptionBean.class);
            nsp = new NamespacePrefixMapper() { // from class: com.sun.xml.internal.ws.fault.ExceptionBean.1
                @Override // com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper
                public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                    if (ExceptionBean.NS.equals(namespaceUri)) {
                        return "";
                    }
                    return suggestion;
                }
            };
        } catch (JAXBException e2) {
            throw new Error(e2);
        }
    }
}
