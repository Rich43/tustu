package com.sun.xml.internal.ws.api.message;

import com.sun.xml.internal.ws.api.WSBinding;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/MessageHeaders.class */
public interface MessageHeaders {
    void understood(Header header);

    void understood(QName qName);

    void understood(String str, String str2);

    Header get(String str, String str2, boolean z2);

    Header get(QName qName, boolean z2);

    Iterator<Header> getHeaders(String str, String str2, boolean z2);

    Iterator<Header> getHeaders(String str, boolean z2);

    Iterator<Header> getHeaders(QName qName, boolean z2);

    Iterator<Header> getHeaders();

    boolean hasHeaders();

    boolean add(Header header);

    Header remove(QName qName);

    Header remove(String str, String str2);

    void replace(Header header, Header header2);

    boolean addOrReplace(Header header);

    Set<QName> getUnderstoodHeaders();

    Set<QName> getNotUnderstoodHeaders(Set<String> set, Set<QName> set2, WSBinding wSBinding);

    boolean isUnderstood(Header header);

    boolean isUnderstood(QName qName);

    boolean isUnderstood(String str, String str2);

    List<Header> asList();
}
