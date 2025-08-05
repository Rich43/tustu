package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorOperations.class */
public interface LocatorOperations {
    ServerLocation locateServer(int i2, String str) throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown;

    ServerLocationPerORB locateServerForORB(int i2, String str) throws InvalidORBid, ServerNotRegistered, ServerHeldDown;

    int getEndpoint(String str) throws NoSuchEndPoint;

    int getServerPortForType(ServerLocationPerORB serverLocationPerORB, String str) throws NoSuchEndPoint;
}
