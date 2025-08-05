package javax.xml.ws;

/* loaded from: rt.jar:javax/xml/ws/WebServiceFeature.class */
public abstract class WebServiceFeature {
    protected boolean enabled = false;

    public abstract String getID();

    protected WebServiceFeature() {
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
