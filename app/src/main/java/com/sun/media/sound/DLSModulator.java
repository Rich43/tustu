package com.sun.media.sound;

import javax.swing.JSplitPane;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:com/sun/media/sound/DLSModulator.class */
public final class DLSModulator {
    public static final int CONN_DST_NONE = 0;
    public static final int CONN_DST_GAIN = 1;
    public static final int CONN_DST_PITCH = 3;
    public static final int CONN_DST_PAN = 4;
    public static final int CONN_DST_LFO_FREQUENCY = 260;
    public static final int CONN_DST_LFO_STARTDELAY = 261;
    public static final int CONN_DST_EG1_ATTACKTIME = 518;
    public static final int CONN_DST_EG1_DECAYTIME = 519;
    public static final int CONN_DST_EG1_RELEASETIME = 521;
    public static final int CONN_DST_EG1_SUSTAINLEVEL = 522;
    public static final int CONN_DST_EG2_ATTACKTIME = 778;
    public static final int CONN_DST_EG2_DECAYTIME = 779;
    public static final int CONN_DST_EG2_RELEASETIME = 781;
    public static final int CONN_DST_EG2_SUSTAINLEVEL = 782;
    public static final int CONN_DST_KEYNUMBER = 5;
    public static final int CONN_DST_LEFT = 16;
    public static final int CONN_DST_RIGHT = 17;
    public static final int CONN_DST_CENTER = 18;
    public static final int CONN_DST_LEFTREAR = 19;
    public static final int CONN_DST_RIGHTREAR = 20;
    public static final int CONN_DST_LFE_CHANNEL = 21;
    public static final int CONN_DST_CHORUS = 128;
    public static final int CONN_DST_REVERB = 129;
    public static final int CONN_DST_VIB_FREQUENCY = 276;
    public static final int CONN_DST_VIB_STARTDELAY = 277;
    public static final int CONN_DST_EG1_DELAYTIME = 523;
    public static final int CONN_DST_EG1_HOLDTIME = 524;
    public static final int CONN_DST_EG1_SHUTDOWNTIME = 525;
    public static final int CONN_DST_EG2_DELAYTIME = 783;
    public static final int CONN_DST_EG2_HOLDTIME = 784;
    public static final int CONN_DST_FILTER_CUTOFF = 1280;
    public static final int CONN_DST_FILTER_Q = 1281;
    public static final int CONN_SRC_NONE = 0;
    public static final int CONN_SRC_LFO = 1;
    public static final int CONN_SRC_KEYONVELOCITY = 2;
    public static final int CONN_SRC_KEYNUMBER = 3;
    public static final int CONN_SRC_EG1 = 4;
    public static final int CONN_SRC_EG2 = 5;
    public static final int CONN_SRC_PITCHWHEEL = 6;
    public static final int CONN_SRC_CC1 = 129;
    public static final int CONN_SRC_CC7 = 135;
    public static final int CONN_SRC_CC10 = 138;
    public static final int CONN_SRC_CC11 = 139;
    public static final int CONN_SRC_RPN0 = 256;
    public static final int CONN_SRC_RPN1 = 257;
    public static final int CONN_SRC_RPN2 = 258;
    public static final int CONN_SRC_POLYPRESSURE = 7;
    public static final int CONN_SRC_CHANNELPRESSURE = 8;
    public static final int CONN_SRC_VIBRATO = 9;
    public static final int CONN_SRC_MONOPRESSURE = 10;
    public static final int CONN_SRC_CC91 = 219;
    public static final int CONN_SRC_CC93 = 221;
    public static final int CONN_TRN_NONE = 0;
    public static final int CONN_TRN_CONCAVE = 1;
    public static final int CONN_TRN_CONVEX = 2;
    public static final int CONN_TRN_SWITCH = 3;
    public static final int DST_FORMAT_CB = 1;
    public static final int DST_FORMAT_CENT = 1;
    public static final int DST_FORMAT_TIMECENT = 2;
    public static final int DST_FORMAT_PERCENT = 3;
    int source;
    int control;
    int destination;
    int transform;
    int scale;
    int version = 1;

    public int getControl() {
        return this.control;
    }

    public void setControl(int i2) {
        this.control = i2;
    }

    public static int getDestinationFormat(int i2) {
        if (i2 == 1 || i2 == 3) {
            return 1;
        }
        if (i2 == 4) {
            return 3;
        }
        if (i2 == 260) {
            return 1;
        }
        if (i2 == 261 || i2 == 518 || i2 == 519 || i2 == 521) {
            return 2;
        }
        if (i2 == 522) {
            return 3;
        }
        if (i2 == 778 || i2 == 779 || i2 == 781) {
            return 2;
        }
        if (i2 == 782) {
            return 3;
        }
        if (i2 == 5 || i2 == 16 || i2 == 17 || i2 == 18 || i2 == 19 || i2 == 20 || i2 == 21) {
            return 1;
        }
        if (i2 == 128 || i2 == 129) {
            return 3;
        }
        if (i2 == 276) {
            return 1;
        }
        if (i2 == 277 || i2 == 523 || i2 == 524 || i2 == 525 || i2 == 783 || i2 == 784) {
            return 2;
        }
        if (i2 == 1280 || i2 == 1281) {
            return 1;
        }
        return -1;
    }

    public static String getDestinationName(int i2) {
        if (i2 == 1) {
            return "gain";
        }
        if (i2 == 3) {
            return "pitch";
        }
        if (i2 == 4) {
            return "pan";
        }
        if (i2 == 260) {
            return "lfo1.freq";
        }
        if (i2 == 261) {
            return "lfo1.delay";
        }
        if (i2 == 518) {
            return "eg1.attack";
        }
        if (i2 == 519) {
            return "eg1.decay";
        }
        if (i2 == 521) {
            return "eg1.release";
        }
        if (i2 == 522) {
            return "eg1.sustain";
        }
        if (i2 == 778) {
            return "eg2.attack";
        }
        if (i2 == 779) {
            return "eg2.decay";
        }
        if (i2 == 781) {
            return "eg2.release";
        }
        if (i2 == 782) {
            return "eg2.sustain";
        }
        if (i2 == 5) {
            return "keynumber";
        }
        if (i2 == 16) {
            return JSplitPane.LEFT;
        }
        if (i2 == 17) {
            return JSplitPane.RIGHT;
        }
        if (i2 == 18) {
            return "center";
        }
        if (i2 == 19) {
            return "leftrear";
        }
        if (i2 == 20) {
            return "rightrear";
        }
        if (i2 == 21) {
            return "lfe_channel";
        }
        if (i2 == 128) {
            return "chorus";
        }
        if (i2 == 129) {
            return "reverb";
        }
        if (i2 == 276) {
            return "vib.freq";
        }
        if (i2 == 277) {
            return "vib.delay";
        }
        if (i2 == 523) {
            return "eg1.delay";
        }
        if (i2 == 524) {
            return "eg1.hold";
        }
        if (i2 == 525) {
            return "eg1.shutdown";
        }
        if (i2 == 783) {
            return "eg2.delay";
        }
        if (i2 == 784) {
            return "eg.2hold";
        }
        if (i2 == 1280) {
            return "filter.cutoff";
        }
        if (i2 == 1281) {
            return "filter.q";
        }
        return null;
    }

    public static String getSourceName(int i2) {
        if (i2 == 0) {
            return Separation.COLORANT_NONE;
        }
        if (i2 == 1) {
            return "lfo";
        }
        if (i2 == 2) {
            return "keyonvelocity";
        }
        if (i2 == 3) {
            return "keynumber";
        }
        if (i2 == 4) {
            return "eg1";
        }
        if (i2 == 5) {
            return "eg2";
        }
        if (i2 == 6) {
            return "pitchweel";
        }
        if (i2 == 129) {
            return "cc1";
        }
        if (i2 == 135) {
            return "cc7";
        }
        if (i2 == 138) {
            return "c10";
        }
        if (i2 == 139) {
            return "cc11";
        }
        if (i2 == 7) {
            return "polypressure";
        }
        if (i2 == 8) {
            return "channelpressure";
        }
        if (i2 == 9) {
            return "vibrato";
        }
        if (i2 == 10) {
            return "monopressure";
        }
        if (i2 == 219) {
            return "cc91";
        }
        if (i2 == 221) {
            return "cc93";
        }
        return null;
    }

    public int getDestination() {
        return this.destination;
    }

    public void setDestination(int i2) {
        this.destination = i2;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int i2) {
        this.scale = i2;
    }

    public int getSource() {
        return this.source;
    }

    public void setSource(int i2) {
        this.source = i2;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i2) {
        this.version = i2;
    }

    public int getTransform() {
        return this.transform;
    }

    public void setTransform(int i2) {
        this.transform = i2;
    }
}
