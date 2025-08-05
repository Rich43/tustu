package org.omg.stub.java.rmi;

import java.rmi.Remote;
import javax.rmi.CORBA.Stub;

/* loaded from: rt.jar:org/omg/stub/java/rmi/_Remote_Stub.class */
public final class _Remote_Stub extends Stub implements Remote {
    private static final String[] _type_ids = {""};

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }
}
