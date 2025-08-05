package javax.xml.ws;

import java.io.StringWriter;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

@XmlTransient
/* loaded from: rt.jar:javax/xml/ws/EndpointReference.class */
public abstract class EndpointReference {
    public abstract void writeTo(Result result);

    protected EndpointReference() {
    }

    public static EndpointReference readFrom(Source eprInfoset) {
        return javax.xml.ws.spi.Provider.provider().readEndpointReference(eprInfoset);
    }

    public <T> T getPort(Class<T> cls, WebServiceFeature... webServiceFeatureArr) {
        return (T) javax.xml.ws.spi.Provider.provider().getPort(this, cls, webServiceFeatureArr);
    }

    public String toString() {
        StringWriter w2 = new StringWriter();
        writeTo(new StreamResult(w2));
        return w2.toString();
    }
}
