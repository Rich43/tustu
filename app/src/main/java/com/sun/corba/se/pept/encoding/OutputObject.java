package com.sun.corba.se.pept.encoding;

import com.sun.corba.se.pept.protocol.MessageMediator;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/corba/se/pept/encoding/OutputObject.class */
public interface OutputObject {
    void setMessageMediator(MessageMediator messageMediator);

    MessageMediator getMessageMediator();

    void close() throws IOException;
}
