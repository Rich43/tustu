package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryOperations.class */
public interface RepositoryOperations {
    int registerServer(ServerDef serverDef) throws ServerAlreadyRegistered, BadServerDefinition;

    void unregisterServer(int i2) throws ServerNotRegistered;

    ServerDef getServer(int i2) throws ServerNotRegistered;

    boolean isInstalled(int i2) throws ServerNotRegistered;

    void install(int i2) throws ServerNotRegistered, ServerAlreadyInstalled;

    void uninstall(int i2) throws ServerNotRegistered, ServerAlreadyUninstalled;

    int[] listRegisteredServers();

    String[] getApplicationNames();

    int getServerID(String str) throws ServerNotRegistered;
}
