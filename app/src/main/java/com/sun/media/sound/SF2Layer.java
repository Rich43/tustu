package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.SoundbankResource;

/* loaded from: rt.jar:com/sun/media/sound/SF2Layer.class */
public final class SF2Layer extends SoundbankResource {
    String name;
    SF2GlobalRegion globalregion;
    List<SF2LayerRegion> regions;

    public SF2Layer(SF2Soundbank sF2Soundbank) {
        super(sF2Soundbank, null, null);
        this.name = "";
        this.globalregion = null;
        this.regions = new ArrayList();
    }

    public SF2Layer() {
        super(null, null, null);
        this.name = "";
        this.globalregion = null;
        this.regions = new ArrayList();
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return null;
    }

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<SF2LayerRegion> getRegions() {
        return this.regions;
    }

    public SF2GlobalRegion getGlobalRegion() {
        return this.globalregion;
    }

    public void setGlobalZone(SF2GlobalRegion sF2GlobalRegion) {
        this.globalregion = sF2GlobalRegion;
    }

    public String toString() {
        return "Layer: " + this.name;
    }
}
