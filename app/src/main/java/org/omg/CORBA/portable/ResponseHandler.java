package org.omg.CORBA.portable;

/* loaded from: rt.jar:org/omg/CORBA/portable/ResponseHandler.class */
public interface ResponseHandler {
    OutputStream createReply();

    OutputStream createExceptionReply();
}
