package javax.naming.ldap;

import com.sun.naming.internal.FactoryEnumeration;
import com.sun.naming.internal.ResourceManager;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/ldap/ControlFactory.class */
public abstract class ControlFactory {
    public abstract Control getControlInstance(Control control) throws NamingException;

    protected ControlFactory() {
    }

    public static Control getControlInstance(Control control, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        Control control2;
        FactoryEnumeration factories = ResourceManager.getFactories(LdapContext.CONTROL_FACTORIES, hashtable, context);
        if (factories == null) {
            return control;
        }
        Control controlInstance = null;
        while (true) {
            control2 = controlInstance;
            if (control2 != null || !factories.hasMore()) {
                break;
            }
            controlInstance = ((ControlFactory) factories.next()).getControlInstance(control);
        }
        return control2 != null ? control2 : control;
    }
}
