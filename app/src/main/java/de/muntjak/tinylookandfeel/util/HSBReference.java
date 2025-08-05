package de.muntjak.tinylookandfeel.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.plaf.ColorUIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/HSBReference.class */
public class HSBReference extends SBReference {
    protected int hue;
    protected boolean preserveGrey;

    public HSBReference(int i2, int i3, int i4, int i5) {
        this.hue = i2;
        this.sat = i3;
        this.bri = i4;
        this.ref = i5;
        this.preserveGrey = true;
    }

    public HSBReference(HSBReference hSBReference) {
        super(false);
        this.color = new ColorUIResource(hSBReference.color);
        this.hue = hSBReference.hue;
        this.sat = hSBReference.sat;
        this.bri = hSBReference.bri;
        this.ref = hSBReference.ref;
        this.preserveGrey = hSBReference.preserveGrey;
    }

    public void update(HSBReference hSBReference) {
        this.color = new ColorUIResource(hSBReference.color);
        this.hue = hSBReference.hue;
        this.sat = hSBReference.sat;
        this.bri = hSBReference.bri;
        this.ref = hSBReference.ref;
        this.preserveGrey = hSBReference.preserveGrey;
    }

    public void update(HSBReference hSBReference, Vector vector) {
        this.color = new ColorUIResource(hSBReference.color);
        this.hue = hSBReference.hue;
        this.sat = hSBReference.sat;
        this.bri = hSBReference.bri;
        this.ref = hSBReference.ref;
        this.preserveGrey = hSBReference.preserveGrey;
    }

    public int getHue() {
        return this.hue;
    }

    public void setHue(int i2) {
        this.hue = i2;
    }

    @Override // de.muntjak.tinylookandfeel.util.SBReference
    public void load(DataInputStream dataInputStream) throws IOException {
        try {
            this.hue = dataInputStream.readInt();
            this.sat = dataInputStream.readInt();
            this.bri = dataInputStream.readInt();
            this.ref = dataInputStream.readInt();
            this.preserveGrey = dataInputStream.readBoolean();
        } catch (Exception e2) {
            throw new IOException(new StringBuffer().append("HSBReference.load() : ").append(e2.getMessage()).toString());
        }
    }

    @Override // de.muntjak.tinylookandfeel.util.SBReference
    public void save(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.hue);
        dataOutputStream.writeInt(this.sat);
        dataOutputStream.writeInt(this.bri);
        dataOutputStream.writeInt(this.ref);
        dataOutputStream.writeBoolean(this.preserveGrey);
    }

    public boolean isPreserveGrey() {
        return this.preserveGrey;
    }

    public void setPreserveGrey(boolean z2) {
        this.preserveGrey = z2;
    }

    @Override // de.muntjak.tinylookandfeel.util.SBReference
    public String toString() {
        return new StringBuffer().append("HSBReference[bri=").append(this.bri).append(",sat=").append(this.sat).append(",hue=").append(this.hue).append(",ref=").append(this.ref).append(",c=(").append(this.color.getRed()).append(",").append(this.color.getGreen()).append(",").append(this.color.getBlue()).append(")]").toString();
    }
}
