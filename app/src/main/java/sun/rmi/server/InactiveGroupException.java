package sun.rmi.server;

import java.rmi.activation.ActivationException;

/* loaded from: rt.jar:sun/rmi/server/InactiveGroupException.class */
public class InactiveGroupException extends ActivationException {
    private static final long serialVersionUID = -7491041778450214975L;

    public InactiveGroupException(String str) {
        super(str);
    }
}
