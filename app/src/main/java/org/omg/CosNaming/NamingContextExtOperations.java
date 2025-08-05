package org.omg.CosNaming;

import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtOperations.class */
public interface NamingContextExtOperations extends NamingContextOperations {
    String to_string(NameComponent[] nameComponentArr) throws InvalidName;

    NameComponent[] to_name(String str) throws InvalidName;

    String to_url(String str, String str2) throws InvalidAddress, InvalidName;

    Object resolve_str(String str) throws NotFound, InvalidName, CannotProceed;
}
