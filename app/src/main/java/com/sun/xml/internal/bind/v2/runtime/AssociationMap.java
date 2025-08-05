package com.sun.xml.internal.bind.v2.runtime;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/AssociationMap.class */
public final class AssociationMap<XmlNode> {
    private final Map<XmlNode, Entry<XmlNode>> byElement = new IdentityHashMap();
    private final Map<Object, Entry<XmlNode>> byPeer = new IdentityHashMap();
    private final Set<XmlNode> usedNodes = new HashSet();

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/AssociationMap$Entry.class */
    static final class Entry<XmlNode> {
        private XmlNode element;
        private Object inner;
        private Object outer;

        Entry() {
        }

        public XmlNode element() {
            return this.element;
        }

        public Object inner() {
            return this.inner;
        }

        public Object outer() {
            return this.outer;
        }
    }

    public void addInner(XmlNode element, Object inner) {
        Entry<XmlNode> e2 = this.byElement.get(element);
        if (e2 != null) {
            if (((Entry) e2).inner != null) {
                this.byPeer.remove(((Entry) e2).inner);
            }
            ((Entry) e2).inner = inner;
        } else {
            e2 = new Entry<>();
            ((Entry) e2).element = element;
            ((Entry) e2).inner = inner;
        }
        this.byElement.put(element, e2);
        Entry<XmlNode> old = this.byPeer.put(inner, e2);
        if (old != null) {
            if (((Entry) old).outer != null) {
                this.byPeer.remove(((Entry) old).outer);
            }
            if (((Entry) old).element != null) {
                this.byElement.remove(((Entry) old).element);
            }
        }
    }

    public void addOuter(XmlNode element, Object outer) {
        Entry<XmlNode> e2 = this.byElement.get(element);
        if (e2 != null) {
            if (((Entry) e2).outer != null) {
                this.byPeer.remove(((Entry) e2).outer);
            }
            ((Entry) e2).outer = outer;
        } else {
            e2 = new Entry<>();
            ((Entry) e2).element = element;
            ((Entry) e2).outer = outer;
        }
        this.byElement.put(element, e2);
        Entry<XmlNode> old = this.byPeer.put(outer, e2);
        if (old != null) {
            ((Entry) old).outer = null;
            if (((Entry) old).inner == null) {
                this.byElement.remove(((Entry) old).element);
            }
        }
    }

    public void addUsed(XmlNode n2) {
        this.usedNodes.add(n2);
    }

    public Entry<XmlNode> byElement(Object e2) {
        return this.byElement.get(e2);
    }

    public Entry<XmlNode> byPeer(Object o2) {
        return this.byPeer.get(o2);
    }

    public Object getInnerPeer(XmlNode element) {
        Entry e2 = byElement(element);
        if (e2 == null) {
            return null;
        }
        return e2.inner;
    }

    public Object getOuterPeer(XmlNode element) {
        Entry e2 = byElement(element);
        if (e2 == null) {
            return null;
        }
        return e2.outer;
    }
}
