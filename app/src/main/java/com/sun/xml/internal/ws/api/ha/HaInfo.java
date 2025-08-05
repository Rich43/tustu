package com.sun.xml.internal.ws.api.ha;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/ha/HaInfo.class */
public class HaInfo {
    private final String replicaInstance;
    private final String key;
    private final boolean failOver;

    public HaInfo(String key, String replicaInstance, boolean failOver) {
        this.key = key;
        this.replicaInstance = replicaInstance;
        this.failOver = failOver;
    }

    public String getReplicaInstance() {
        return this.replicaInstance;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isFailOver() {
        return this.failOver;
    }
}
