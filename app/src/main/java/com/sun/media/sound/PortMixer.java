package com.sun.media.sound;

import com.sun.media.sound.PortMixerProvider;
import java.util.Vector;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

/* loaded from: rt.jar:com/sun/media/sound/PortMixer.class */
final class PortMixer extends AbstractMixer {
    private static final int SRC_UNKNOWN = 1;
    private static final int SRC_MICROPHONE = 2;
    private static final int SRC_LINE_IN = 3;
    private static final int SRC_COMPACT_DISC = 4;
    private static final int SRC_MASK = 255;
    private static final int DST_UNKNOWN = 256;
    private static final int DST_SPEAKER = 512;
    private static final int DST_HEADPHONE = 768;
    private static final int DST_LINE_OUT = 1024;
    private static final int DST_MASK = 65280;
    private Port.Info[] portInfos;
    private PortMixerPort[] ports;
    private long id;

    private static native long nOpen(int i2) throws LineUnavailableException;

    private static native void nClose(long j2);

    private static native int nGetPortCount(long j2);

    private static native int nGetPortType(long j2, int i2);

    private static native String nGetPortName(long j2, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nGetControls(long j2, int i2, Vector vector);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nControlSetIntValue(long j2, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nControlGetIntValue(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nControlSetFloatValue(long j2, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native float nControlGetFloatValue(long j2);

    PortMixer(PortMixerProvider.PortMixerInfo portMixerInfo) {
        super(portMixerInfo, null, null, null);
        this.id = 0L;
        int iNGetPortCount = 0;
        int i2 = 0;
        int i3 = 0;
        try {
            try {
                this.id = nOpen(getMixerIndex());
                if (this.id != 0) {
                    iNGetPortCount = nGetPortCount(this.id);
                    if (iNGetPortCount < 0) {
                        iNGetPortCount = 0;
                    }
                }
            } catch (Exception e2) {
            }
            this.portInfos = new Port.Info[iNGetPortCount];
            for (int i4 = 0; i4 < iNGetPortCount; i4++) {
                int iNGetPortType = nGetPortType(this.id, i4);
                i2 += (iNGetPortType & 255) != 0 ? 1 : 0;
                i3 += (iNGetPortType & 65280) != 0 ? 1 : 0;
                this.portInfos[i4] = getPortInfo(i4, iNGetPortType);
            }
            this.sourceLineInfo = new Port.Info[i2];
            this.targetLineInfo = new Port.Info[i3];
            int i5 = 0;
            int i6 = 0;
            for (int i7 = 0; i7 < iNGetPortCount; i7++) {
                if (this.portInfos[i7].isSource()) {
                    int i8 = i5;
                    i5++;
                    this.sourceLineInfo[i8] = this.portInfos[i7];
                } else {
                    int i9 = i6;
                    i6++;
                    this.targetLineInfo[i9] = this.portInfos[i7];
                }
            }
        } finally {
            if (this.id != 0) {
                nClose(this.id);
            }
            this.id = 0L;
        }
    }

    @Override // com.sun.media.sound.AbstractMixer, javax.sound.sampled.Mixer
    public Line getLine(Line.Info info) throws LineUnavailableException {
        Line.Info lineInfo = getLineInfo(info);
        if (lineInfo != null && (lineInfo instanceof Port.Info)) {
            for (int i2 = 0; i2 < this.portInfos.length; i2++) {
                if (lineInfo.equals(this.portInfos[i2])) {
                    return getPort(i2);
                }
            }
        }
        throw new IllegalArgumentException("Line unsupported: " + ((Object) info));
    }

    @Override // com.sun.media.sound.AbstractMixer, javax.sound.sampled.Mixer
    public int getMaxLines(Line.Info info) {
        Line.Info lineInfo = getLineInfo(info);
        if (lineInfo != null && (lineInfo instanceof Port.Info)) {
            return 1;
        }
        return 0;
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implOpen() throws LineUnavailableException {
        this.id = nOpen(getMixerIndex());
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implClose() {
        long j2 = this.id;
        this.id = 0L;
        nClose(j2);
        if (this.ports != null) {
            for (int i2 = 0; i2 < this.ports.length; i2++) {
                if (this.ports[i2] != null) {
                    this.ports[i2].disposeControls();
                }
            }
        }
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implStart() {
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implStop() {
    }

    private Port.Info getPortInfo(int i2, int i3) {
        switch (i3) {
            case 1:
                return new PortInfo(nGetPortName(getID(), i2), true);
            case 2:
                return Port.Info.MICROPHONE;
            case 3:
                return Port.Info.LINE_IN;
            case 4:
                return Port.Info.COMPACT_DISC;
            case 256:
                return new PortInfo(nGetPortName(getID(), i2), false);
            case 512:
                return Port.Info.SPEAKER;
            case 768:
                return Port.Info.HEADPHONE;
            case 1024:
                return Port.Info.LINE_OUT;
            default:
                return null;
        }
    }

    int getMixerIndex() {
        return ((PortMixerProvider.PortMixerInfo) getMixerInfo()).getIndex();
    }

    Port getPort(int i2) {
        if (this.ports == null) {
            this.ports = new PortMixerPort[this.portInfos.length];
        }
        if (this.ports[i2] == null) {
            this.ports[i2] = new PortMixerPort(this.portInfos[i2], this, i2);
            return this.ports[i2];
        }
        return this.ports[i2];
    }

    long getID() {
        return this.id;
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixer$PortMixerPort.class */
    private static final class PortMixerPort extends AbstractLine implements Port {
        private final int portIndex;
        private long id;

        private PortMixerPort(Port.Info info, PortMixer portMixer, int i2) {
            super(info, portMixer, null);
            this.portIndex = i2;
        }

        void implOpen() throws LineUnavailableException {
            long id = ((PortMixer) this.mixer).getID();
            if (this.id == 0 || id != this.id || this.controls.length == 0) {
                this.id = id;
                Vector vector = new Vector();
                synchronized (vector) {
                    PortMixer.nGetControls(this.id, this.portIndex, vector);
                    this.controls = new Control[vector.size()];
                    for (int i2 = 0; i2 < this.controls.length; i2++) {
                        this.controls[i2] = (Control) vector.elementAt(i2);
                    }
                }
                return;
            }
            enableControls(this.controls, true);
        }

        private void enableControls(Control[] controlArr, boolean z2) {
            for (int i2 = 0; i2 < controlArr.length; i2++) {
                if (!(controlArr[i2] instanceof BoolCtrl)) {
                    if (!(controlArr[i2] instanceof FloatCtrl)) {
                        if (controlArr[i2] instanceof CompoundControl) {
                            enableControls(((CompoundControl) controlArr[i2]).getMemberControls(), z2);
                        }
                    } else {
                        ((FloatCtrl) controlArr[i2]).closed = !z2;
                    }
                } else {
                    ((BoolCtrl) controlArr[i2]).closed = !z2;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void disposeControls() {
            enableControls(this.controls, false);
            this.controls = new Control[0];
        }

        void implClose() {
            enableControls(this.controls, false);
        }

        @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line
        public void open() throws LineUnavailableException {
            synchronized (this.mixer) {
                if (!isOpen()) {
                    this.mixer.open(this);
                    try {
                        implOpen();
                        setOpen(true);
                    } catch (LineUnavailableException e2) {
                        this.mixer.close(this);
                        throw e2;
                    }
                }
            }
        }

        @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line, java.lang.AutoCloseable
        public void close() {
            synchronized (this.mixer) {
                if (isOpen()) {
                    setOpen(false);
                    implClose();
                    this.mixer.close(this);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixer$BoolCtrl.class */
    private static final class BoolCtrl extends BooleanControl {
        private final long controlID;
        private boolean closed;

        private static BooleanControl.Type createType(String str) {
            if (str.equals("Mute")) {
                return BooleanControl.Type.MUTE;
            }
            if (str.equals("Select")) {
            }
            return new BCT(str);
        }

        private BoolCtrl(long j2, String str) {
            this(j2, createType(str));
        }

        private BoolCtrl(long j2, BooleanControl.Type type) {
            super(type, false);
            this.closed = false;
            this.controlID = j2;
        }

        @Override // javax.sound.sampled.BooleanControl
        public void setValue(boolean z2) {
            if (!this.closed) {
                PortMixer.nControlSetIntValue(this.controlID, z2 ? 1 : 0);
            }
        }

        @Override // javax.sound.sampled.BooleanControl
        public boolean getValue() {
            return (this.closed || PortMixer.nControlGetIntValue(this.controlID) == 0) ? false : true;
        }

        /* loaded from: rt.jar:com/sun/media/sound/PortMixer$BoolCtrl$BCT.class */
        private static final class BCT extends BooleanControl.Type {
            private BCT(String str) {
                super(str);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixer$CompCtrl.class */
    private static final class CompCtrl extends CompoundControl {
        private CompCtrl(String str, Control[] controlArr) {
            super(new CCT(str), controlArr);
        }

        /* loaded from: rt.jar:com/sun/media/sound/PortMixer$CompCtrl$CCT.class */
        private static final class CCT extends CompoundControl.Type {
            private CCT(String str) {
                super(str);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixer$FloatCtrl.class */
    private static final class FloatCtrl extends FloatControl {
        private final long controlID;
        private boolean closed;
        private static final FloatControl.Type[] FLOAT_CONTROL_TYPES = {null, FloatControl.Type.BALANCE, FloatControl.Type.MASTER_GAIN, FloatControl.Type.PAN, FloatControl.Type.VOLUME};

        private FloatCtrl(long j2, String str, float f2, float f3, float f4, String str2) {
            this(j2, new FCT(str), f2, f3, f4, str2);
        }

        private FloatCtrl(long j2, int i2, float f2, float f3, float f4, String str) {
            this(j2, FLOAT_CONTROL_TYPES[i2], f2, f3, f4, str);
        }

        private FloatCtrl(long j2, FloatControl.Type type, float f2, float f3, float f4, String str) {
            super(type, f2, f3, f4, 1000, f2, str);
            this.closed = false;
            this.controlID = j2;
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            if (!this.closed) {
                PortMixer.nControlSetFloatValue(this.controlID, f2);
            }
        }

        @Override // javax.sound.sampled.FloatControl
        public float getValue() {
            if (!this.closed) {
                return PortMixer.nControlGetFloatValue(this.controlID);
            }
            return getMinimum();
        }

        /* loaded from: rt.jar:com/sun/media/sound/PortMixer$FloatCtrl$FCT.class */
        private static final class FCT extends FloatControl.Type {
            private FCT(String str) {
                super(str);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixer$PortInfo.class */
    private static final class PortInfo extends Port.Info {
        private PortInfo(String str, boolean z2) {
            super(Port.class, str, z2);
        }
    }
}
