package org.omg.CORBA.portable;

import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:org/omg/CORBA/portable/InvokeHandler.class */
public interface InvokeHandler {
    OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) throws SystemException;
}
