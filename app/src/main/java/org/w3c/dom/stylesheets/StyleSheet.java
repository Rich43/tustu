package org.w3c.dom.stylesheets;

import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/stylesheets/StyleSheet.class */
public interface StyleSheet {
    String getType();

    boolean getDisabled();

    void setDisabled(boolean z2);

    Node getOwnerNode();

    StyleSheet getParentStyleSheet();

    String getHref();

    String getTitle();

    MediaList getMedia();
}
