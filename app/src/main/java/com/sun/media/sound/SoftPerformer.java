package com.sun.media.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.swing.plaf.basic.BasicRootPaneUI;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/media/sound/SoftPerformer.class */
public final class SoftPerformer {
    static ModelConnectionBlock[] defaultconnections = new ModelConnectionBlock[42];
    public int keyFrom;
    public int keyTo;
    public int velFrom;
    public int velTo;
    public int exclusiveClass;
    public boolean selfNonExclusive;
    public boolean forcedVelocity;
    public boolean forcedKeynumber;
    public ModelPerformer performer;
    public ModelConnectionBlock[] connections;
    public ModelOscillator[] oscillators;
    public int[][] midi_ctrl_connections;
    public int[][] midi_connections;
    public int[] ctrl_connections;
    private static KeySortComparator keySortComparator;
    public Map<Integer, int[]> midi_rpn_connections = new HashMap();
    public Map<Integer, int[]> midi_nrpn_connections = new HashMap();
    private List<Integer> ctrl_connections_list = new ArrayList();

    static {
        int i2 = 0 + 1;
        defaultconnections[0] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", FXMLLoader.EVENT_HANDLER_PREFIX, 0), false, false, 0), 1.0d, new ModelDestination(new ModelIdentifier("eg", FXMLLoader.EVENT_HANDLER_PREFIX, 0)));
        int i3 = i2 + 1;
        defaultconnections[i2] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", FXMLLoader.EVENT_HANDLER_PREFIX, 0), false, false, 0), 1.0d, new ModelDestination(new ModelIdentifier("eg", FXMLLoader.EVENT_HANDLER_PREFIX, 1)));
        int i4 = i3 + 1;
        defaultconnections[i3] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("eg", "active", 0), false, false, 0), 1.0d, new ModelDestination(new ModelIdentifier("mixer", "active", 0)));
        int i5 = i4 + 1;
        defaultconnections[i4] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("eg", 0), true, false, 0), -960.0d, new ModelDestination(new ModelIdentifier("mixer", "gain")));
        int i6 = i5 + 1;
        defaultconnections[i5] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "velocity"), true, false, 1), -960.0d, new ModelDestination(new ModelIdentifier("mixer", "gain")));
        int i7 = i6 + 1;
        defaultconnections[i6] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi", "pitch"), false, true, 0), new ModelSource(new ModelIdentifier("midi_rpn", "0"), new ModelTransform() { // from class: com.sun.media.sound.SoftPerformer.1
            @Override // com.sun.media.sound.ModelTransform
            public double transform(double d2) {
                int i8 = ((int) (d2 * 16384.0d)) >> 7;
                return (i8 * 100) + (r0 & 127);
            }
        }), new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i8 = i7 + 1;
        defaultconnections[i7] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "keynumber"), false, false, 0), 12800.0d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i9 = i8 + 1;
        defaultconnections[i8] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "7"), true, false, 1), -960.0d, new ModelDestination(new ModelIdentifier("mixer", "gain")));
        int i10 = i9 + 1;
        defaultconnections[i9] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "8"), false, false, 0), 1000.0d, new ModelDestination(new ModelIdentifier("mixer", "balance")));
        int i11 = i10 + 1;
        defaultconnections[i10] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "10"), false, false, 0), 1000.0d, new ModelDestination(new ModelIdentifier("mixer", "pan")));
        int i12 = i11 + 1;
        defaultconnections[i11] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "11"), true, false, 1), -960.0d, new ModelDestination(new ModelIdentifier("mixer", "gain")));
        int i13 = i12 + 1;
        defaultconnections[i12] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "91"), false, false, 0), 1000.0d, new ModelDestination(new ModelIdentifier("mixer", "reverb")));
        int i14 = i13 + 1;
        defaultconnections[i13] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "93"), false, false, 0), 1000.0d, new ModelDestination(new ModelIdentifier("mixer", "chorus")));
        int i15 = i14 + 1;
        defaultconnections[i14] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "71"), false, true, 0), 200.0d, new ModelDestination(new ModelIdentifier("filter", PdfOps.q_TOKEN)));
        int i16 = i15 + 1;
        defaultconnections[i15] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "74"), false, true, 0), 9600.0d, new ModelDestination(new ModelIdentifier("filter", "freq")));
        int i17 = i16 + 1;
        defaultconnections[i16] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "72"), false, true, 0), 6000.0d, new ModelDestination(new ModelIdentifier("eg", "release2")));
        int i18 = i17 + 1;
        defaultconnections[i17] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "73"), false, true, 0), 2000.0d, new ModelDestination(new ModelIdentifier("eg", "attack2")));
        int i19 = i18 + 1;
        defaultconnections[i18] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "75"), false, true, 0), 6000.0d, new ModelDestination(new ModelIdentifier("eg", "decay2")));
        int i20 = i19 + 1;
        defaultconnections[i19] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "67"), false, false, 3), -50.0d, new ModelDestination(ModelDestination.DESTINATION_GAIN));
        int i21 = i20 + 1;
        defaultconnections[i20] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "67"), false, false, 3), -2400.0d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
        int i22 = i21 + 1;
        defaultconnections[i21] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_rpn", "1"), false, true, 0), 100.0d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i23 = i22 + 1;
        defaultconnections[i22] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_rpn", "2"), false, true, 0), 12800.0d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i24 = i23 + 1;
        defaultconnections[i23] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("master", "fine_tuning"), false, true, 0), 100.0d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i25 = i24 + 1;
        defaultconnections[i24] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("master", "coarse_tuning"), false, true, 0), 12800.0d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
        int i26 = i25 + 1;
        defaultconnections[i25] = new ModelConnectionBlock(13500.0d, new ModelDestination(new ModelIdentifier("filter", "freq", 0)));
        int i27 = i26 + 1;
        defaultconnections[i26] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "delay", 0)));
        int i28 = i27 + 1;
        defaultconnections[i27] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "attack", 0)));
        int i29 = i28 + 1;
        defaultconnections[i28] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "hold", 0)));
        int i30 = i29 + 1;
        defaultconnections[i29] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "decay", 0)));
        int i31 = i30 + 1;
        defaultconnections[i30] = new ModelConnectionBlock(1000.0d, new ModelDestination(new ModelIdentifier("eg", "sustain", 0)));
        int i32 = i31 + 1;
        defaultconnections[i31] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", BasicRootPaneUI.Actions.RELEASE, 0)));
        int i33 = i32 + 1;
        defaultconnections[i32] = new ModelConnectionBlock((1200.0d * Math.log(0.015d)) / Math.log(2.0d), new ModelDestination(new ModelIdentifier("eg", "shutdown", 0)));
        int i34 = i33 + 1;
        defaultconnections[i33] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "delay", 1)));
        int i35 = i34 + 1;
        defaultconnections[i34] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "attack", 1)));
        int i36 = i35 + 1;
        defaultconnections[i35] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "hold", 1)));
        int i37 = i36 + 1;
        defaultconnections[i36] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "decay", 1)));
        int i38 = i37 + 1;
        defaultconnections[i37] = new ModelConnectionBlock(1000.0d, new ModelDestination(new ModelIdentifier("eg", "sustain", 1)));
        int i39 = i38 + 1;
        defaultconnections[i38] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", BasicRootPaneUI.Actions.RELEASE, 1)));
        int i40 = i39 + 1;
        defaultconnections[i39] = new ModelConnectionBlock(-8.51318d, new ModelDestination(new ModelIdentifier("lfo", "freq", 0)));
        int i41 = i40 + 1;
        defaultconnections[i40] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("lfo", "delay", 0)));
        int i42 = i41 + 1;
        defaultconnections[i41] = new ModelConnectionBlock(-8.51318d, new ModelDestination(new ModelIdentifier("lfo", "freq", 1)));
        int i43 = i42 + 1;
        defaultconnections[i42] = new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("lfo", "delay", 1)));
        keySortComparator = new KeySortComparator();
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftPerformer$KeySortComparator.class */
    private static class KeySortComparator implements Comparator<ModelSource> {
        private KeySortComparator() {
        }

        @Override // java.util.Comparator
        public int compare(ModelSource modelSource, ModelSource modelSource2) {
            return modelSource.getIdentifier().toString().compareTo(modelSource2.getIdentifier().toString());
        }
    }

    private String extractKeys(ModelConnectionBlock modelConnectionBlock) {
        StringBuffer stringBuffer = new StringBuffer();
        if (modelConnectionBlock.getSources() != null) {
            stringBuffer.append("[");
            ModelSource[] sources = modelConnectionBlock.getSources();
            ModelSource[] modelSourceArr = new ModelSource[sources.length];
            for (int i2 = 0; i2 < sources.length; i2++) {
                modelSourceArr[i2] = sources[i2];
            }
            Arrays.sort(modelSourceArr, keySortComparator);
            for (ModelSource modelSource : sources) {
                stringBuffer.append((Object) modelSource.getIdentifier());
                stringBuffer.append(";");
            }
            stringBuffer.append("]");
        }
        stringBuffer.append(";");
        if (modelConnectionBlock.getDestination() != null) {
            stringBuffer.append((Object) modelConnectionBlock.getDestination().getIdentifier());
        }
        stringBuffer.append(";");
        return stringBuffer.toString();
    }

    private void processSource(ModelSource modelSource, int i2) throws NumberFormatException {
        String object = modelSource.getIdentifier().getObject();
        if (object.equals("midi_cc")) {
            processMidiControlSource(modelSource, i2);
            return;
        }
        if (object.equals("midi_rpn")) {
            processMidiRpnSource(modelSource, i2);
            return;
        }
        if (object.equals("midi_nrpn")) {
            processMidiNrpnSource(modelSource, i2);
            return;
        }
        if (object.equals("midi")) {
            processMidiSource(modelSource, i2);
            return;
        }
        if (object.equals("noteon")) {
            processNoteOnSource(modelSource, i2);
        } else {
            if (object.equals("osc") || object.equals("mixer")) {
                return;
            }
            this.ctrl_connections_list.add(Integer.valueOf(i2));
        }
    }

    private void processMidiControlSource(ModelSource modelSource, int i2) throws NumberFormatException {
        String variable = modelSource.getIdentifier().getVariable();
        if (variable == null) {
            return;
        }
        int i3 = Integer.parseInt(variable);
        if (this.midi_ctrl_connections[i3] == null) {
            this.midi_ctrl_connections[i3] = new int[]{i2};
            return;
        }
        int[] iArr = this.midi_ctrl_connections[i3];
        int[] iArr2 = new int[iArr.length + 1];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr2[i4] = iArr[i4];
        }
        iArr2[iArr2.length - 1] = i2;
        this.midi_ctrl_connections[i3] = iArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void processNoteOnSource(ModelSource modelSource, int i2) {
        String variable = modelSource.getIdentifier().getVariable();
        boolean z2 = -1;
        if (variable.equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
            z2 = 3;
        }
        boolean z3 = z2;
        if (variable.equals("keynumber")) {
            z3 = 4;
        }
        if (z3 == -1) {
            return;
        }
        if (this.midi_connections[z3 ? 1 : 0] == null) {
            this.midi_connections[z3 ? 1 : 0] = new int[]{i2};
            return;
        }
        int[] iArr = this.midi_connections[z3 ? 1 : 0];
        int[] iArr2 = new int[iArr.length + 1];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            iArr2[i3] = iArr[i3];
        }
        iArr2[iArr2.length - 1] = i2;
        this.midi_connections[z3 ? 1 : 0] = iArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void processMidiSource(ModelSource modelSource, int i2) {
        String variable = modelSource.getIdentifier().getVariable();
        boolean z2 = -1;
        if (variable.equals("pitch")) {
            z2 = false;
        }
        boolean z3 = z2;
        if (variable.equals("channel_pressure")) {
            z3 = true;
        }
        boolean z4 = z3;
        if (variable.equals("poly_pressure")) {
            z4 = 2;
        }
        if (z4 == -1) {
            return;
        }
        if (this.midi_connections[z4 ? 1 : 0] == null) {
            this.midi_connections[z4 ? 1 : 0] = new int[]{i2};
            return;
        }
        int[] iArr = this.midi_connections[z4 ? 1 : 0];
        int[] iArr2 = new int[iArr.length + 1];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            iArr2[i3] = iArr[i3];
        }
        iArr2[iArr2.length - 1] = i2;
        this.midi_connections[z4 ? 1 : 0] = iArr2;
    }

    private void processMidiRpnSource(ModelSource modelSource, int i2) throws NumberFormatException {
        String variable = modelSource.getIdentifier().getVariable();
        if (variable == null) {
            return;
        }
        int i3 = Integer.parseInt(variable);
        if (this.midi_rpn_connections.get(Integer.valueOf(i3)) == null) {
            this.midi_rpn_connections.put(Integer.valueOf(i3), new int[]{i2});
            return;
        }
        int[] iArr = this.midi_rpn_connections.get(Integer.valueOf(i3));
        int[] iArr2 = new int[iArr.length + 1];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr2[i4] = iArr[i4];
        }
        iArr2[iArr2.length - 1] = i2;
        this.midi_rpn_connections.put(Integer.valueOf(i3), iArr2);
    }

    private void processMidiNrpnSource(ModelSource modelSource, int i2) throws NumberFormatException {
        String variable = modelSource.getIdentifier().getVariable();
        if (variable == null) {
            return;
        }
        int i3 = Integer.parseInt(variable);
        if (this.midi_nrpn_connections.get(Integer.valueOf(i3)) == null) {
            this.midi_nrpn_connections.put(Integer.valueOf(i3), new int[]{i2});
            return;
        }
        int[] iArr = this.midi_nrpn_connections.get(Integer.valueOf(i3));
        int[] iArr2 = new int[iArr.length + 1];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr2[i4] = iArr[i4];
        }
        iArr2[iArr2.length - 1] = i2;
        this.midi_nrpn_connections.put(Integer.valueOf(i3), iArr2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v30, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v35, types: [int[], int[][]] */
    public SoftPerformer(ModelPerformer modelPerformer) throws NumberFormatException {
        this.keyFrom = 0;
        this.keyTo = 127;
        this.velFrom = 0;
        this.velTo = 127;
        this.exclusiveClass = 0;
        this.selfNonExclusive = false;
        this.forcedVelocity = false;
        this.forcedKeynumber = false;
        this.performer = modelPerformer;
        this.keyFrom = modelPerformer.getKeyFrom();
        this.keyTo = modelPerformer.getKeyTo();
        this.velFrom = modelPerformer.getVelFrom();
        this.velTo = modelPerformer.getVelTo();
        this.exclusiveClass = modelPerformer.getExclusiveClass();
        this.selfNonExclusive = modelPerformer.isSelfNonExclusive();
        HashMap map = new HashMap();
        ArrayList<ModelConnectionBlock> arrayList = new ArrayList();
        arrayList.addAll(modelPerformer.getConnectionBlocks());
        if (modelPerformer.isDefaultConnectionsEnabled()) {
            boolean z2 = false;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                ModelConnectionBlock modelConnectionBlock = (ModelConnectionBlock) arrayList.get(i2);
                ModelSource[] sources = modelConnectionBlock.getSources();
                boolean z3 = false;
                if (modelConnectionBlock.getDestination() != null && sources != null && sources.length > 1) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= sources.length) {
                            break;
                        }
                        if (!sources[i3].getIdentifier().getObject().equals("midi_cc") || !sources[i3].getIdentifier().getVariable().equals("1")) {
                            i3++;
                        } else {
                            z3 = true;
                            z2 = true;
                            break;
                        }
                    }
                }
                if (z3) {
                    ModelConnectionBlock modelConnectionBlock2 = new ModelConnectionBlock();
                    modelConnectionBlock2.setSources(modelConnectionBlock.getSources());
                    modelConnectionBlock2.setDestination(modelConnectionBlock.getDestination());
                    modelConnectionBlock2.addSource(new ModelSource(new ModelIdentifier("midi_rpn", "5")));
                    modelConnectionBlock2.setScale(modelConnectionBlock.getScale() * 256.0d);
                    arrayList.set(i2, modelConnectionBlock2);
                }
            }
            if (!z2) {
                ModelConnectionBlock modelConnectionBlock3 = new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(new ModelIdentifier("midi_cc", "1", 0), false, false, 0), 50.0d, new ModelDestination(ModelDestination.DESTINATION_PITCH));
                modelConnectionBlock3.addSource(new ModelSource(new ModelIdentifier("midi_rpn", "5")));
                modelConnectionBlock3.setScale(modelConnectionBlock3.getScale() * 256.0d);
                arrayList.add(modelConnectionBlock3);
            }
            boolean z4 = false;
            boolean z5 = false;
            ModelConnectionBlock modelConnectionBlock4 = null;
            int i4 = 0;
            for (ModelConnectionBlock modelConnectionBlock5 : arrayList) {
                ModelSource[] sources2 = modelConnectionBlock5.getSources();
                if (modelConnectionBlock5.getDestination() != null && sources2 != null) {
                    for (int i5 = 0; i5 < sources2.length; i5++) {
                        ModelIdentifier identifier = sources2[i5].getIdentifier();
                        if (identifier.getObject().equals("midi_cc") && identifier.getVariable().equals("1")) {
                            modelConnectionBlock4 = modelConnectionBlock5;
                            i4 = i5;
                        }
                        if (identifier.getObject().equals("midi")) {
                            z4 = identifier.getVariable().equals("channel_pressure") ? true : z4;
                            if (identifier.getVariable().equals("poly_pressure")) {
                                z5 = true;
                            }
                        }
                    }
                }
            }
            if (modelConnectionBlock4 != null) {
                if (!z4) {
                    ModelConnectionBlock modelConnectionBlock6 = new ModelConnectionBlock();
                    modelConnectionBlock6.setDestination(modelConnectionBlock4.getDestination());
                    modelConnectionBlock6.setScale(modelConnectionBlock4.getScale());
                    ModelSource[] sources3 = modelConnectionBlock4.getSources();
                    ModelSource[] modelSourceArr = new ModelSource[sources3.length];
                    for (int i6 = 0; i6 < modelSourceArr.length; i6++) {
                        modelSourceArr[i6] = sources3[i6];
                    }
                    modelSourceArr[i4] = new ModelSource(new ModelIdentifier("midi", "channel_pressure"));
                    modelConnectionBlock6.setSources(modelSourceArr);
                    map.put(extractKeys(modelConnectionBlock6), modelConnectionBlock6);
                }
                if (!z5) {
                    ModelConnectionBlock modelConnectionBlock7 = new ModelConnectionBlock();
                    modelConnectionBlock7.setDestination(modelConnectionBlock4.getDestination());
                    modelConnectionBlock7.setScale(modelConnectionBlock4.getScale());
                    ModelSource[] sources4 = modelConnectionBlock4.getSources();
                    ModelSource[] modelSourceArr2 = new ModelSource[sources4.length];
                    for (int i7 = 0; i7 < modelSourceArr2.length; i7++) {
                        modelSourceArr2[i7] = sources4[i7];
                    }
                    modelSourceArr2[i4] = new ModelSource(new ModelIdentifier("midi", "poly_pressure"));
                    modelConnectionBlock7.setSources(modelSourceArr2);
                    map.put(extractKeys(modelConnectionBlock7), modelConnectionBlock7);
                }
            }
            ModelConnectionBlock modelConnectionBlock8 = null;
            for (ModelConnectionBlock modelConnectionBlock9 : arrayList) {
                ModelSource[] sources5 = modelConnectionBlock9.getSources();
                if (sources5.length != 0 && sources5[0].getIdentifier().getObject().equals("lfo") && modelConnectionBlock9.getDestination().getIdentifier().equals(ModelDestination.DESTINATION_PITCH)) {
                    if (modelConnectionBlock8 == null) {
                        modelConnectionBlock8 = modelConnectionBlock9;
                    } else if (modelConnectionBlock8.getSources().length > sources5.length) {
                        modelConnectionBlock8 = modelConnectionBlock9;
                    } else if (modelConnectionBlock8.getSources()[0].getIdentifier().getInstance() < 1 && modelConnectionBlock8.getSources()[0].getIdentifier().getInstance() > sources5[0].getIdentifier().getInstance()) {
                        modelConnectionBlock8 = modelConnectionBlock9;
                    }
                }
            }
            int modelIdentifier = modelConnectionBlock8 != null ? modelConnectionBlock8.getSources()[0].getIdentifier().getInstance() : 1;
            ModelConnectionBlock modelConnectionBlock10 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "78"), false, true, 0), 2000.0d, new ModelDestination(new ModelIdentifier("lfo", "delay2", modelIdentifier)));
            map.put(extractKeys(modelConnectionBlock10), modelConnectionBlock10);
            final double scale = modelConnectionBlock8 == null ? 0.0d : modelConnectionBlock8.getScale();
            ModelConnectionBlock modelConnectionBlock11 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("lfo", modelIdentifier)), new ModelSource(new ModelIdentifier("midi_cc", "77"), new ModelTransform() { // from class: com.sun.media.sound.SoftPerformer.2

                /* renamed from: s, reason: collision with root package name */
                double f11984s;

                {
                    this.f11984s = scale;
                }

                @Override // com.sun.media.sound.ModelTransform
                public double transform(double d2) {
                    double d3 = ((d2 * 2.0d) - 1.0d) * 600.0d;
                    if (this.f11984s == 0.0d) {
                        return d3;
                    }
                    if (this.f11984s > 0.0d) {
                        if (d3 < (-this.f11984s)) {
                            d3 = -this.f11984s;
                        }
                        return d3;
                    }
                    if (d3 < this.f11984s) {
                        d3 = -this.f11984s;
                    }
                    return -d3;
                }
            }), new ModelDestination(ModelDestination.DESTINATION_PITCH));
            map.put(extractKeys(modelConnectionBlock11), modelConnectionBlock11);
            ModelConnectionBlock modelConnectionBlock12 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "76"), false, true, 0), 2400.0d, new ModelDestination(new ModelIdentifier("lfo", "freq", modelIdentifier)));
            map.put(extractKeys(modelConnectionBlock12), modelConnectionBlock12);
        }
        if (modelPerformer.isDefaultConnectionsEnabled()) {
            for (ModelConnectionBlock modelConnectionBlock13 : defaultconnections) {
                map.put(extractKeys(modelConnectionBlock13), modelConnectionBlock13);
            }
        }
        for (ModelConnectionBlock modelConnectionBlock14 : arrayList) {
            map.put(extractKeys(modelConnectionBlock14), modelConnectionBlock14);
        }
        ArrayList<ModelConnectionBlock> arrayList2 = new ArrayList();
        this.midi_ctrl_connections = new int[128];
        for (int i8 = 0; i8 < this.midi_ctrl_connections.length; i8++) {
            this.midi_ctrl_connections[i8] = null;
        }
        this.midi_connections = new int[5];
        for (int i9 = 0; i9 < this.midi_connections.length; i9++) {
            this.midi_connections[i9] = null;
        }
        int i10 = 0;
        boolean z6 = false;
        for (V v2 : map.values()) {
            if (v2.getDestination() != null) {
                ModelIdentifier identifier2 = v2.getDestination().getIdentifier();
                if (identifier2.getObject().equals("noteon")) {
                    z6 = true;
                    if (identifier2.getVariable().equals("keynumber")) {
                        this.forcedKeynumber = true;
                    }
                    if (identifier2.getVariable().equals("velocity")) {
                        this.forcedVelocity = true;
                    }
                }
            }
            if (z6) {
                arrayList2.add(0, v2);
                z6 = false;
            } else {
                arrayList2.add(v2);
            }
        }
        for (ModelConnectionBlock modelConnectionBlock15 : arrayList2) {
            if (modelConnectionBlock15.getSources() != null) {
                for (ModelSource modelSource : modelConnectionBlock15.getSources()) {
                    processSource(modelSource, i10);
                }
            }
            i10++;
        }
        this.connections = new ModelConnectionBlock[arrayList2.size()];
        arrayList2.toArray(this.connections);
        this.ctrl_connections = new int[this.ctrl_connections_list.size()];
        for (int i11 = 0; i11 < this.ctrl_connections.length; i11++) {
            this.ctrl_connections[i11] = this.ctrl_connections_list.get(i11).intValue();
        }
        this.oscillators = new ModelOscillator[modelPerformer.getOscillators().size()];
        modelPerformer.getOscillators().toArray(this.oscillators);
        for (ModelConnectionBlock modelConnectionBlock16 : arrayList2) {
            if (modelConnectionBlock16.getDestination() != null && isUnnecessaryTransform(modelConnectionBlock16.getDestination().getTransform())) {
                modelConnectionBlock16.getDestination().setTransform(null);
            }
            if (modelConnectionBlock16.getSources() != null) {
                for (ModelSource modelSource2 : modelConnectionBlock16.getSources()) {
                    if (isUnnecessaryTransform(modelSource2.getTransform())) {
                        modelSource2.setTransform(null);
                    }
                }
            }
        }
    }

    private static boolean isUnnecessaryTransform(ModelTransform modelTransform) {
        if (modelTransform == null || !(modelTransform instanceof ModelStandardTransform)) {
            return false;
        }
        ModelStandardTransform modelStandardTransform = (ModelStandardTransform) modelTransform;
        if (!modelStandardTransform.getDirection() && !modelStandardTransform.getPolarity() && modelStandardTransform.getTransform() != 0) {
            return false;
        }
        return false;
    }
}
