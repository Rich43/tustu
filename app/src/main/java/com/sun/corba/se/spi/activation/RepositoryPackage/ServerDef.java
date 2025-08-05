package com.sun.corba.se.spi.activation.RepositoryPackage;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryPackage/ServerDef.class */
public final class ServerDef implements IDLEntity {
    public String applicationName;
    public String serverName;
    public String serverClassPath;
    public String serverArgs;
    public String serverVmArgs;

    public ServerDef() {
        this.applicationName = null;
        this.serverName = null;
        this.serverClassPath = null;
        this.serverArgs = null;
        this.serverVmArgs = null;
    }

    public ServerDef(String str, String str2, String str3, String str4, String str5) {
        this.applicationName = null;
        this.serverName = null;
        this.serverClassPath = null;
        this.serverArgs = null;
        this.serverVmArgs = null;
        this.applicationName = str;
        this.serverName = str2;
        this.serverClassPath = str3;
        this.serverArgs = str4;
        this.serverVmArgs = str5;
    }
}
