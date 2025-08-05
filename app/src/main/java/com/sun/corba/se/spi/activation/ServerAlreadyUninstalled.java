package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyUninstalled.class */
public final class ServerAlreadyUninstalled extends UserException {
    public int serverId;

    public ServerAlreadyUninstalled() {
        super(ServerAlreadyUninstalledHelper.id());
        this.serverId = 0;
    }

    public ServerAlreadyUninstalled(int i2) {
        super(ServerAlreadyUninstalledHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerAlreadyUninstalled(String str, int i2) {
        super(ServerAlreadyUninstalledHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
