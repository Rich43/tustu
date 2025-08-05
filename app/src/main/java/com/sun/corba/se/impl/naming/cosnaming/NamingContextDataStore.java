package com.sun.corba.se.impl.naming.cosnaming;

import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.BindingTypeHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.PortableServer.POA;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/NamingContextDataStore.class */
public interface NamingContextDataStore {
    void Bind(NameComponent nameComponent, Object object, BindingType bindingType) throws SystemException;

    Object Resolve(NameComponent nameComponent, BindingTypeHolder bindingTypeHolder) throws SystemException;

    Object Unbind(NameComponent nameComponent) throws SystemException;

    void List(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder) throws SystemException;

    NamingContext NewContext() throws SystemException;

    void Destroy() throws SystemException;

    boolean IsEmpty();

    POA getNSPOA();
}
