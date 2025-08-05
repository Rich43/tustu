package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.soap.MTOM;
import jdk.jfr.Enabled;
import jdk.jfr.Threshold;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mtom")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlMTOM.class */
public class XmlMTOM implements MTOM {

    @XmlAttribute(name = Enabled.NAME)
    protected Boolean enabled;

    @XmlAttribute(name = Threshold.NAME)
    protected Integer threshold;

    public boolean isEnabled() {
        if (this.enabled == null) {
            return true;
        }
        return this.enabled.booleanValue();
    }

    public void setEnabled(Boolean value) {
        this.enabled = value;
    }

    public int getThreshold() {
        if (this.threshold == null) {
            return 0;
        }
        return this.threshold.intValue();
    }

    public void setThreshold(Integer value) {
        this.threshold = value;
    }

    @Override // javax.xml.ws.soap.MTOM
    public boolean enabled() {
        return ((Boolean) Util.nullSafe(this.enabled, Boolean.TRUE)).booleanValue();
    }

    @Override // javax.xml.ws.soap.MTOM
    public int threshold() {
        return ((Integer) Util.nullSafe((int) this.threshold, 0)).intValue();
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return MTOM.class;
    }
}
