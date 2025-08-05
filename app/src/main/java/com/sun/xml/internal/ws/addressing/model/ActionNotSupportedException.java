package com.sun.xml.internal.ws.addressing.model;

import com.sun.xml.internal.ws.resources.AddressingMessages;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/model/ActionNotSupportedException.class */
public class ActionNotSupportedException extends WebServiceException {
    private String action;

    public ActionNotSupportedException(String action) {
        super(AddressingMessages.ACTION_NOT_SUPPORTED_EXCEPTION(action));
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
