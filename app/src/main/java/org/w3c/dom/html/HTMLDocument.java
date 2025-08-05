package org.w3c.dom.html;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLDocument.class */
public interface HTMLDocument extends Document {
    String getTitle();

    void setTitle(String str);

    String getReferrer();

    String getDomain();

    String getURL();

    HTMLElement getBody();

    void setBody(HTMLElement hTMLElement);

    HTMLCollection getImages();

    HTMLCollection getApplets();

    HTMLCollection getLinks();

    HTMLCollection getForms();

    HTMLCollection getAnchors();

    String getCookie();

    void setCookie(String str);

    void open();

    void close();

    void write(String str);

    void writeln(String str);

    NodeList getElementsByName(String str);
}
