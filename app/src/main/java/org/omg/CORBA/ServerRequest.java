package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/ServerRequest.class */
public abstract class ServerRequest {
    public abstract Context ctx();

    @Deprecated
    public String op_name() {
        return operation();
    }

    public String operation() {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public void params(NVList nVList) {
        arguments(nVList);
    }

    public void arguments(NVList nVList) {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public void result(Any any) {
        set_result(any);
    }

    public void set_result(Any any) {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public void except(Any any) {
        set_exception(any);
    }

    public void set_exception(Any any) {
        throw new NO_IMPLEMENT();
    }
}
