package org.omg.CosNaming;

import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextOperations.class */
public interface NamingContextOperations {
    void bind(NameComponent[] nameComponentArr, Object object) throws NotFound, AlreadyBound, InvalidName, CannotProceed;

    void bind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, AlreadyBound, InvalidName, CannotProceed;

    void rebind(NameComponent[] nameComponentArr, Object object) throws NotFound, InvalidName, CannotProceed;

    void rebind_context(NameComponent[] nameComponentArr, NamingContext namingContext) throws NotFound, InvalidName, CannotProceed;

    Object resolve(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed;

    void unbind(NameComponent[] nameComponentArr) throws NotFound, InvalidName, CannotProceed;

    void list(int i2, BindingListHolder bindingListHolder, BindingIteratorHolder bindingIteratorHolder);

    NamingContext new_context();

    NamingContext bind_new_context(NameComponent[] nameComponentArr) throws NotFound, AlreadyBound, InvalidName, CannotProceed;

    void destroy() throws NotEmpty;
}
