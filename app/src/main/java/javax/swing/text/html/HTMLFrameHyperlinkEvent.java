package javax.swing.text.html;

import java.awt.event.InputEvent;
import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Element;

/* loaded from: rt.jar:javax/swing/text/html/HTMLFrameHyperlinkEvent.class */
public class HTMLFrameHyperlinkEvent extends HyperlinkEvent {
    private String targetFrame;

    public HTMLFrameHyperlinkEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, String str) {
        super(obj, eventType, url);
        this.targetFrame = str;
    }

    public HTMLFrameHyperlinkEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, String str, String str2) {
        super(obj, eventType, url, str);
        this.targetFrame = str2;
    }

    public HTMLFrameHyperlinkEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, Element element, String str) {
        super(obj, eventType, url, null, element);
        this.targetFrame = str;
    }

    public HTMLFrameHyperlinkEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, String str, Element element, String str2) {
        super(obj, eventType, url, str, element);
        this.targetFrame = str2;
    }

    public HTMLFrameHyperlinkEvent(Object obj, HyperlinkEvent.EventType eventType, URL url, String str, Element element, InputEvent inputEvent, String str2) {
        super(obj, eventType, url, str, element, inputEvent);
        this.targetFrame = str2;
    }

    public String getTarget() {
        return this.targetFrame;
    }
}
