package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/NameBuilder.class */
public final class NameBuilder {
    private Map<String, Integer> uriIndexMap = new HashMap();
    private Set<String> nonDefaultableNsUris = new HashSet();
    private Map<String, Integer> localNameIndexMap = new HashMap();
    private QNameMap<Integer> elementQNameIndexMap = new QNameMap<>();
    private QNameMap<Integer> attributeQNameIndexMap = new QNameMap<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NameBuilder.class.desiredAssertionStatus();
    }

    public Name createElementName(QName name) {
        return createElementName(name.getNamespaceURI(), name.getLocalPart());
    }

    public Name createElementName(String nsUri, String localName) {
        return createName(nsUri, localName, false, this.elementQNameIndexMap);
    }

    public Name createAttributeName(QName name) {
        return createAttributeName(name.getNamespaceURI(), name.getLocalPart());
    }

    public Name createAttributeName(String nsUri, String localName) {
        if (!$assertionsDisabled && nsUri.intern() != nsUri) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && localName.intern() != localName) {
            throw new AssertionError();
        }
        if (nsUri.length() == 0) {
            return new Name(allocIndex(this.attributeQNameIndexMap, "", localName), -1, nsUri, allocIndex(this.localNameIndexMap, localName), localName, true);
        }
        this.nonDefaultableNsUris.add(nsUri);
        return createName(nsUri, localName, true, this.attributeQNameIndexMap);
    }

    private Name createName(String nsUri, String localName, boolean isAttribute, QNameMap<Integer> map) {
        if (!$assertionsDisabled && nsUri.intern() != nsUri) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || localName.intern() == localName) {
            return new Name(allocIndex(map, nsUri, localName), allocIndex(this.uriIndexMap, nsUri), nsUri, allocIndex(this.localNameIndexMap, localName), localName, isAttribute);
        }
        throw new AssertionError();
    }

    private int allocIndex(Map<String, Integer> map, String str) {
        Integer i2 = map.get(str);
        if (i2 == null) {
            i2 = Integer.valueOf(map.size());
            map.put(str, i2);
        }
        return i2.intValue();
    }

    private int allocIndex(QNameMap<Integer> map, String nsUri, String localName) {
        Integer i2 = map.get(nsUri, localName);
        if (i2 == null) {
            i2 = Integer.valueOf(map.size());
            map.put(nsUri, localName, i2);
        }
        return i2.intValue();
    }

    public NameList conclude() {
        boolean[] nsUriCannotBeDefaulted = new boolean[this.uriIndexMap.size()];
        for (Map.Entry<String, Integer> e2 : this.uriIndexMap.entrySet()) {
            nsUriCannotBeDefaulted[e2.getValue().intValue()] = this.nonDefaultableNsUris.contains(e2.getKey());
        }
        NameList r2 = new NameList(list(this.uriIndexMap), nsUriCannotBeDefaulted, list(this.localNameIndexMap), this.elementQNameIndexMap.size(), this.attributeQNameIndexMap.size());
        this.uriIndexMap = null;
        this.localNameIndexMap = null;
        return r2;
    }

    private String[] list(Map<String, Integer> map) {
        String[] r2 = new String[map.size()];
        for (Map.Entry<String, Integer> e2 : map.entrySet()) {
            r2[e2.getValue().intValue()] = e2.getKey();
        }
        return r2;
    }
}
