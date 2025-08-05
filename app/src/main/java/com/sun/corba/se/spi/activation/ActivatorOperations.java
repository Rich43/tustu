package com.sun.corba.se.spi.activation;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ActivatorOperations.class */
public interface ActivatorOperations {
    void active(int i2, Server server) throws ServerNotRegistered;

    void registerEndpoints(int i2, String str, EndPointInfo[] endPointInfoArr) throws NoSuchEndPoint, ORBAlreadyRegistered, ServerNotRegistered;

    int[] getActiveServers();

    void activate(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyActive;

    void shutdown(int i2) throws ServerNotRegistered, ServerNotActive;

    void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled, ServerHeldDown;

    String[] getORBNames(int i2) throws ServerNotRegistered;

    void uninstall(int i2) throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled;
}
