package com.sun.media.sound;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/media/sound/ModelConnectionBlock.class */
public final class ModelConnectionBlock {
    private static final ModelSource[] no_sources = new ModelSource[0];
    private ModelSource[] sources;
    private double scale;
    private ModelDestination destination;

    public ModelConnectionBlock() {
        this.sources = no_sources;
        this.scale = 1.0d;
    }

    public ModelConnectionBlock(double d2, ModelDestination modelDestination) {
        this.sources = no_sources;
        this.scale = 1.0d;
        this.scale = d2;
        this.destination = modelDestination;
    }

    public ModelConnectionBlock(ModelSource modelSource, ModelDestination modelDestination) {
        this.sources = no_sources;
        this.scale = 1.0d;
        if (modelSource != null) {
            this.sources = new ModelSource[1];
            this.sources[0] = modelSource;
        }
        this.destination = modelDestination;
    }

    public ModelConnectionBlock(ModelSource modelSource, double d2, ModelDestination modelDestination) {
        this.sources = no_sources;
        this.scale = 1.0d;
        if (modelSource != null) {
            this.sources = new ModelSource[1];
            this.sources[0] = modelSource;
        }
        this.scale = d2;
        this.destination = modelDestination;
    }

    public ModelConnectionBlock(ModelSource modelSource, ModelSource modelSource2, ModelDestination modelDestination) {
        this.sources = no_sources;
        this.scale = 1.0d;
        if (modelSource != null) {
            if (modelSource2 == null) {
                this.sources = new ModelSource[1];
                this.sources[0] = modelSource;
            } else {
                this.sources = new ModelSource[2];
                this.sources[0] = modelSource;
                this.sources[1] = modelSource2;
            }
        }
        this.destination = modelDestination;
    }

    public ModelConnectionBlock(ModelSource modelSource, ModelSource modelSource2, double d2, ModelDestination modelDestination) {
        this.sources = no_sources;
        this.scale = 1.0d;
        if (modelSource != null) {
            if (modelSource2 == null) {
                this.sources = new ModelSource[1];
                this.sources[0] = modelSource;
            } else {
                this.sources = new ModelSource[2];
                this.sources[0] = modelSource;
                this.sources[1] = modelSource2;
            }
        }
        this.scale = d2;
        this.destination = modelDestination;
    }

    public ModelDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ModelDestination modelDestination) {
        this.destination = modelDestination;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double d2) {
        this.scale = d2;
    }

    public ModelSource[] getSources() {
        return (ModelSource[]) Arrays.copyOf(this.sources, this.sources.length);
    }

    public void setSources(ModelSource[] modelSourceArr) {
        this.sources = modelSourceArr == null ? no_sources : (ModelSource[]) Arrays.copyOf(modelSourceArr, modelSourceArr.length);
    }

    public void addSource(ModelSource modelSource) {
        ModelSource[] modelSourceArr = this.sources;
        this.sources = new ModelSource[modelSourceArr.length + 1];
        System.arraycopy(modelSourceArr, 0, this.sources, 0, modelSourceArr.length);
        this.sources[this.sources.length - 1] = modelSource;
    }
}
