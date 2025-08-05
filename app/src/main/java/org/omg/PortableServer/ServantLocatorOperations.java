package org.omg.PortableServer;

import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

/* loaded from: rt.jar:org/omg/PortableServer/ServantLocatorOperations.class */
public interface ServantLocatorOperations extends ServantManagerOperations {
    Servant preinvoke(byte[] bArr, POA poa, String str, CookieHolder cookieHolder) throws ForwardRequest;

    void postinvoke(byte[] bArr, POA poa, String str, Object obj, Servant servant);
}
