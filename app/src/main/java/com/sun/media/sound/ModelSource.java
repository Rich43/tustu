package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/ModelSource.class */
public final class ModelSource {
    public static final ModelIdentifier SOURCE_NONE = null;
    public static final ModelIdentifier SOURCE_NOTEON_KEYNUMBER = new ModelIdentifier("noteon", "keynumber");
    public static final ModelIdentifier SOURCE_NOTEON_VELOCITY = new ModelIdentifier("noteon", "velocity");
    public static final ModelIdentifier SOURCE_EG1 = new ModelIdentifier("eg", null, 0);
    public static final ModelIdentifier SOURCE_EG2 = new ModelIdentifier("eg", null, 1);
    public static final ModelIdentifier SOURCE_LFO1 = new ModelIdentifier("lfo", null, 0);
    public static final ModelIdentifier SOURCE_LFO2 = new ModelIdentifier("lfo", null, 1);
    public static final ModelIdentifier SOURCE_MIDI_PITCH = new ModelIdentifier("midi", "pitch", 0);
    public static final ModelIdentifier SOURCE_MIDI_CHANNEL_PRESSURE = new ModelIdentifier("midi", "channel_pressure", 0);
    public static final ModelIdentifier SOURCE_MIDI_POLY_PRESSURE = new ModelIdentifier("midi", "poly_pressure", 0);
    public static final ModelIdentifier SOURCE_MIDI_CC_0 = new ModelIdentifier("midi_cc", "0", 0);
    public static final ModelIdentifier SOURCE_MIDI_RPN_0 = new ModelIdentifier("midi_rpn", "0", 0);
    private ModelIdentifier source;
    private ModelTransform transform;

    public ModelSource() {
        this.source = SOURCE_NONE;
        this.transform = new ModelStandardTransform();
    }

    public ModelSource(ModelIdentifier modelIdentifier) {
        this.source = SOURCE_NONE;
        this.source = modelIdentifier;
        this.transform = new ModelStandardTransform();
    }

    public ModelSource(ModelIdentifier modelIdentifier, boolean z2) {
        this.source = SOURCE_NONE;
        this.source = modelIdentifier;
        this.transform = new ModelStandardTransform(z2);
    }

    public ModelSource(ModelIdentifier modelIdentifier, boolean z2, boolean z3) {
        this.source = SOURCE_NONE;
        this.source = modelIdentifier;
        this.transform = new ModelStandardTransform(z2, z3);
    }

    public ModelSource(ModelIdentifier modelIdentifier, boolean z2, boolean z3, int i2) {
        this.source = SOURCE_NONE;
        this.source = modelIdentifier;
        this.transform = new ModelStandardTransform(z2, z3, i2);
    }

    public ModelSource(ModelIdentifier modelIdentifier, ModelTransform modelTransform) {
        this.source = SOURCE_NONE;
        this.source = modelIdentifier;
        this.transform = modelTransform;
    }

    public ModelIdentifier getIdentifier() {
        return this.source;
    }

    public void setIdentifier(ModelIdentifier modelIdentifier) {
        this.source = modelIdentifier;
    }

    public ModelTransform getTransform() {
        return this.transform;
    }

    public void setTransform(ModelTransform modelTransform) {
        this.transform = modelTransform;
    }
}
