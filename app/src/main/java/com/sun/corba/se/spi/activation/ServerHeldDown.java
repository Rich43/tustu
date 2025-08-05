package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerHeldDown.class */
public final class ServerHeldDown extends UserException {
    public int serverId;

    public ServerHeldDown() {
        super(ServerHeldDownHelper.id());
        this.serverId = 0;
    }

    public ServerHeldDown(int i2) {
        super(ServerHeldDownHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerHeldDown(String str, int i2) {
        super(ServerHeldDownHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
