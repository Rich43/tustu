package com.sun.jndi.ldap;

import java.io.IOException;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.ControlFactory;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.naming.ldap.SortResponseControl;

/* loaded from: rt.jar:com/sun/jndi/ldap/DefaultResponseControlFactory.class */
public class DefaultResponseControlFactory extends ControlFactory {
    @Override // javax.naming.ldap.ControlFactory
    public Control getControlInstance(Control control) throws NamingException {
        String id = control.getID();
        try {
            if (id.equals(SortResponseControl.OID)) {
                return new SortResponseControl(id, control.isCritical(), control.getEncodedValue());
            }
            if (id.equals("1.2.840.113556.1.4.319")) {
                return new PagedResultsResponseControl(id, control.isCritical(), control.getEncodedValue());
            }
            if (id.equals(EntryChangeResponseControl.OID)) {
                return new EntryChangeResponseControl(id, control.isCritical(), control.getEncodedValue());
            }
            return null;
        } catch (IOException e2) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e2);
            throw namingException;
        }
    }
}
