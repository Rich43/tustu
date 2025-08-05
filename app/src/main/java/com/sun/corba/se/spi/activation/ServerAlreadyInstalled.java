package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyInstalled.class */
public final class ServerAlreadyInstalled extends UserException {
    public int serverId;

    public ServerAlreadyInstalled() {
        super(ServerAlreadyInstalledHelper.id());
        this.serverId = 0;
    }

    public ServerAlreadyInstalled(int i2) {
        super(ServerAlreadyInstalledHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerAlreadyInstalled(String str, int i2) {
        super(ServerAlreadyInstalledHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
