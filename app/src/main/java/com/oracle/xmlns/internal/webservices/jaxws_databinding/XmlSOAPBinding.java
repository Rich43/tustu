package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.annotation.Annotation;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "soap-binding")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlSOAPBinding.class */
public class XmlSOAPBinding implements SOAPBinding {

    @XmlAttribute(name = Constants.ATTRNAME_STYLE)
    protected SoapBindingStyle style;

    @XmlAttribute(name = "use")
    protected SoapBindingUse use;

    @XmlAttribute(name = "parameter-style")
    protected SoapBindingParameterStyle parameterStyle;

    public SoapBindingStyle getStyle() {
        if (this.style == null) {
            return SoapBindingStyle.DOCUMENT;
        }
        return this.style;
    }

    public void setStyle(SoapBindingStyle value) {
        this.style = value;
    }

    public SoapBindingUse getUse() {
        if (this.use == null) {
            return SoapBindingUse.LITERAL;
        }
        return this.use;
    }

    public void setUse(SoapBindingUse value) {
        this.use = value;
    }

    public SoapBindingParameterStyle getParameterStyle() {
        if (this.parameterStyle == null) {
            return SoapBindingParameterStyle.WRAPPED;
        }
        return this.parameterStyle;
    }

    public void setParameterStyle(SoapBindingParameterStyle value) {
        this.parameterStyle = value;
    }

    @Override // javax.jws.soap.SOAPBinding
    public SOAPBinding.Style style() {
        return (SOAPBinding.Style) Util.nullSafe((Enum) this.style, SOAPBinding.Style.DOCUMENT);
    }

    @Override // javax.jws.soap.SOAPBinding
    public SOAPBinding.Use use() {
        return (SOAPBinding.Use) Util.nullSafe((Enum) this.use, SOAPBinding.Use.LITERAL);
    }

    @Override // javax.jws.soap.SOAPBinding
    public SOAPBinding.ParameterStyle parameterStyle() {
        return (SOAPBinding.ParameterStyle) Util.nullSafe((Enum) this.parameterStyle, SOAPBinding.ParameterStyle.WRAPPED);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return SOAPBinding.class;
    }
}
