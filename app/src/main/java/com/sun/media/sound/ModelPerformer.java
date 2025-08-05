package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/media/sound/ModelPerformer.class */
public final class ModelPerformer {
    private final List<ModelOscillator> oscillators = new ArrayList();
    private List<ModelConnectionBlock> connectionBlocks = new ArrayList();
    private int keyFrom = 0;
    private int keyTo = 127;
    private int velFrom = 0;
    private int velTo = 127;
    private int exclusiveClass = 0;
    private boolean releaseTrigger = false;
    private boolean selfNonExclusive = false;
    private Object userObject = null;
    private boolean addDefaultConnections = true;
    private String name = null;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<ModelConnectionBlock> getConnectionBlocks() {
        return this.connectionBlocks;
    }

    public void setConnectionBlocks(List<ModelConnectionBlock> list) {
        this.connectionBlocks = list;
    }

    public List<ModelOscillator> getOscillators() {
        return this.oscillators;
    }

    public int getExclusiveClass() {
        return this.exclusiveClass;
    }

    public void setExclusiveClass(int i2) {
        this.exclusiveClass = i2;
    }

    public boolean isSelfNonExclusive() {
        return this.selfNonExclusive;
    }

    public void setSelfNonExclusive(boolean z2) {
        this.selfNonExclusive = z2;
    }

    public int getKeyFrom() {
        return this.keyFrom;
    }

    public void setKeyFrom(int i2) {
        this.keyFrom = i2;
    }

    public int getKeyTo() {
        return this.keyTo;
    }

    public void setKeyTo(int i2) {
        this.keyTo = i2;
    }

    public int getVelFrom() {
        return this.velFrom;
    }

    public void setVelFrom(int i2) {
        this.velFrom = i2;
    }

    public int getVelTo() {
        return this.velTo;
    }

    public void setVelTo(int i2) {
        this.velTo = i2;
    }

    public boolean isReleaseTriggered() {
        return this.releaseTrigger;
    }

    public void setReleaseTriggered(boolean z2) {
        this.releaseTrigger = z2;
    }

    public Object getUserObject() {
        return this.userObject;
    }

    public void setUserObject(Object obj) {
        this.userObject = obj;
    }

    public boolean isDefaultConnectionsEnabled() {
        return this.addDefaultConnections;
    }

    public void setDefaultConnectionsEnabled(boolean z2) {
        this.addDefaultConnections = z2;
    }
}
