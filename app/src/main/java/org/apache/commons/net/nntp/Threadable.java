package org.apache.commons.net.nntp;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/Threadable.class */
public interface Threadable {
    boolean isDummy();

    String messageThreadId();

    String[] messageThreadReferences();

    String simplifiedSubject();

    boolean subjectIsReply();

    void setChild(Threadable threadable);

    void setNext(Threadable threadable);

    Threadable makeDummy();
}
