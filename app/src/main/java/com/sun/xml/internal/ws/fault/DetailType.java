package com.sun.xml.internal.ws.fault;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/DetailType.class */
class DetailType {

    @XmlAnyElement
    private final List<Element> detailEntry = new ArrayList();

    @NotNull
    List<Element> getDetails() {
        return this.detailEntry;
    }

    @Nullable
    Node getDetail(int n2) {
        if (n2 < this.detailEntry.size()) {
            return this.detailEntry.get(n2);
        }
        return null;
    }

    DetailType(Element detailObject) {
        if (detailObject != null) {
            this.detailEntry.add(detailObject);
        }
    }

    DetailType() {
    }
}
