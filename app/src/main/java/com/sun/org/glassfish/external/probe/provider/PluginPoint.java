package com.sun.org.glassfish.external.probe.provider;

/* loaded from: rt.jar:com/sun/org/glassfish/external/probe/provider/PluginPoint.class */
public enum PluginPoint {
    SERVER("server", "server"),
    APPLICATIONS("applications", "server/applications");

    String name;
    String path;

    PluginPoint(String lname, String lpath) {
        this.name = lname;
        this.path = lpath;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }
}
