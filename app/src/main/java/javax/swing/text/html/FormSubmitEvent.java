package javax.swing.text.html;

import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Element;

/* loaded from: rt.jar:javax/swing/text/html/FormSubmitEvent.class */
public class FormSubmitEvent extends HTMLFrameHyperlinkEvent {
    private MethodType method;
    private String data;

    /* loaded from: rt.jar:javax/swing/text/html/FormSubmitEvent$MethodType.class */
    public enum MethodType {
        GET,
        POST
    }

    FormSubmitEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, Element element, String str, MethodType methodType, String str2) {
        super(obj, eventType, url, element, str);
        this.method = methodType;
        this.data = str2;
    }

    public MethodType getMethod() {
        return this.method;
    }

    public String getData() {
        return this.data;
    }
}
