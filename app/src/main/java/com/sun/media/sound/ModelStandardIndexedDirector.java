package com.sun.media.sound;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/media/sound/ModelStandardIndexedDirector.class */
public final class ModelStandardIndexedDirector implements ModelDirector {
    private final ModelPerformer[] performers;
    private final ModelDirectedPlayer player;
    private boolean noteOnUsed;
    private boolean noteOffUsed;
    private byte[][] trantables;
    private int[] counters;
    private int[][] mat;

    public ModelStandardIndexedDirector(ModelPerformer[] modelPerformerArr, ModelDirectedPlayer modelDirectedPlayer) {
        this.noteOnUsed = false;
        this.noteOffUsed = false;
        this.performers = (ModelPerformer[]) Arrays.copyOf(modelPerformerArr, modelPerformerArr.length);
        this.player = modelDirectedPlayer;
        for (ModelPerformer modelPerformer : this.performers) {
            if (modelPerformer.isReleaseTriggered()) {
                this.noteOffUsed = true;
            } else {
                this.noteOnUsed = true;
            }
        }
        buildindex();
    }

    private int[] lookupIndex(int i2, int i3) {
        if (i2 >= 0 && i2 < 128 && i3 >= 0 && i3 < 128) {
            byte b2 = this.trantables[0][i2];
            byte b3 = this.trantables[1][i3];
            if (b2 != -1 && b3 != -1) {
                return this.mat[b2 + (b3 * this.counters[0])];
            }
            return null;
        }
        return null;
    }

    private int restrict(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 > 127) {
            return 127;
        }
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v14, types: [int[], int[][]] */
    private void buildindex() {
        this.trantables = new byte[2][129];
        this.counters = new int[this.trantables.length];
        for (ModelPerformer modelPerformer : this.performers) {
            int keyFrom = modelPerformer.getKeyFrom();
            int keyTo = modelPerformer.getKeyTo();
            int velFrom = modelPerformer.getVelFrom();
            int velTo = modelPerformer.getVelTo();
            if (keyFrom <= keyTo && velFrom <= velTo) {
                int iRestrict = restrict(keyFrom);
                int iRestrict2 = restrict(keyTo);
                int iRestrict3 = restrict(velFrom);
                int iRestrict4 = restrict(velTo);
                this.trantables[0][iRestrict] = 1;
                this.trantables[0][iRestrict2 + 1] = 1;
                this.trantables[1][iRestrict3] = 1;
                this.trantables[1][iRestrict4 + 1] = 1;
            }
        }
        for (int i2 = 0; i2 < this.trantables.length; i2++) {
            byte[] bArr = this.trantables[i2];
            int length = bArr.length;
            int i3 = length - 1;
            while (true) {
                if (i3 < 0) {
                    break;
                }
                if (bArr[i3] == 1) {
                    bArr[i3] = -1;
                    break;
                } else {
                    bArr[i3] = -1;
                    i3--;
                }
            }
            int i4 = -1;
            for (int i5 = 0; i5 < length; i5++) {
                if (bArr[i5] != 0) {
                    i4++;
                    if (bArr[i5] == -1) {
                        break;
                    }
                }
                bArr[i5] = (byte) i4;
            }
            this.counters[i2] = i4;
        }
        this.mat = new int[this.counters[0] * this.counters[1]];
        int i6 = 0;
        for (ModelPerformer modelPerformer2 : this.performers) {
            int keyFrom2 = modelPerformer2.getKeyFrom();
            int keyTo2 = modelPerformer2.getKeyTo();
            int velFrom2 = modelPerformer2.getVelFrom();
            int velTo2 = modelPerformer2.getVelTo();
            if (keyFrom2 <= keyTo2 && velFrom2 <= velTo2) {
                int iRestrict5 = restrict(keyFrom2);
                int iRestrict6 = restrict(keyTo2);
                int iRestrict7 = restrict(velFrom2);
                int iRestrict8 = restrict(velTo2);
                byte b2 = this.trantables[0][iRestrict5];
                int i7 = this.trantables[0][iRestrict6 + 1];
                byte b3 = this.trantables[1][iRestrict7];
                int i8 = this.trantables[1][iRestrict8 + 1];
                if (i7 == -1) {
                    i7 = this.counters[0];
                }
                if (i8 == -1) {
                    i8 = this.counters[1];
                }
                for (int i9 = b3; i9 < i8; i9++) {
                    int i10 = b2 + (i9 * this.counters[0]);
                    for (int i11 = b2; i11 < i7; i11++) {
                        int[] iArr = this.mat[i10];
                        if (iArr == null) {
                            this.mat[i10] = new int[]{i6};
                        } else {
                            int[] iArr2 = new int[iArr.length + 1];
                            iArr2[iArr2.length - 1] = i6;
                            for (int i12 = 0; i12 < iArr.length; i12++) {
                                iArr2[i12] = iArr[i12];
                            }
                            this.mat[i10] = iArr2;
                        }
                        i10++;
                    }
                }
                i6++;
            }
        }
    }

    @Override // com.sun.media.sound.ModelDirector
    public void close() {
    }

    @Override // com.sun.media.sound.ModelDirector
    public void noteOff(int i2, int i3) {
        int[] iArrLookupIndex;
        if (this.noteOffUsed && (iArrLookupIndex = lookupIndex(i2, i3)) != null) {
            for (int i4 : iArrLookupIndex) {
                if (this.performers[i4].isReleaseTriggered()) {
                    this.player.play(i4, null);
                }
            }
        }
    }

    @Override // com.sun.media.sound.ModelDirector
    public void noteOn(int i2, int i3) {
        int[] iArrLookupIndex;
        if (this.noteOnUsed && (iArrLookupIndex = lookupIndex(i2, i3)) != null) {
            for (int i4 : iArrLookupIndex) {
                if (!this.performers[i4].isReleaseTriggered()) {
                    this.player.play(i4, null);
                }
            }
        }
    }
}
