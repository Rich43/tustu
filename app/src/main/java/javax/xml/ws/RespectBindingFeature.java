package javax.xml.ws;

/* loaded from: rt.jar:javax/xml/ws/RespectBindingFeature.class */
public final class RespectBindingFeature extends WebServiceFeature {
    public static final String ID = "javax.xml.ws.RespectBindingFeature";

    public RespectBindingFeature() {
        this.enabled = true;
    }

    public RespectBindingFeature(boolean enabled) {
        this.enabled = enabled;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ID;
    }
}
