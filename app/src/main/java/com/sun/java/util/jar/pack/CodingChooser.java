package com.sun.java.util.jar.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/CodingChooser.class */
class CodingChooser {
    int verbose;
    int effort;
    boolean optUseHistogram;
    boolean optUsePopulationCoding;
    boolean optUseAdaptiveCoding;
    boolean disablePopCoding;
    boolean disableRunCoding;
    double fuzz;
    Coding[] allCodingChoices;
    Choice[] choices;
    ByteArrayOutputStream context;
    CodingChooser popHelper;
    CodingChooser runHelper;
    Random stress;
    private int[] values;
    private int start;
    private int end;
    private int[] deltas;
    private int min;
    private int max;
    private Histogram vHist;
    private Histogram dHist;
    private int searchOrder;
    private Choice regularChoice;
    private Choice bestChoice;
    private CodingMethod bestMethod;
    private int bestByteSize;
    private int bestZipSize;
    private int targetSize;
    public static final int MIN_EFFORT = 1;
    public static final int MID_EFFORT = 5;
    public static final int MAX_EFFORT = 9;
    public static final int POP_EFFORT = 4;
    public static final int RUN_EFFORT = 3;
    public static final int BYTE_SIZE = 0;
    public static final int ZIP_SIZE = 1;
    static final /* synthetic */ boolean $assertionsDisabled;
    boolean topLevel = true;
    private Sizer zipSizer = new Sizer();
    private Deflater zipDef = new Deflater();
    private DeflaterOutputStream zipOut = new DeflaterOutputStream(this.zipSizer, this.zipDef);
    private Sizer byteSizer = new Sizer(this.zipOut);
    private Sizer byteOnlySizer = new Sizer();

    static {
        $assertionsDisabled = !CodingChooser.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/CodingChooser$Choice.class */
    static class Choice {
        final Coding coding;
        final int index;
        final int[] distance;
        int searchOrder;
        int minDistance;
        int zipSize;
        int byteSize;
        int histSize;

        Choice(Coding coding, int i2, int[] iArr) {
            this.coding = coding;
            this.index = i2;
            this.distance = iArr;
        }

        void reset() {
            this.searchOrder = Integer.MAX_VALUE;
            this.minDistance = Integer.MAX_VALUE;
            this.histSize = -1;
            this.byteSize = -1;
            this.zipSize = -1;
        }

        boolean isExtra() {
            return this.index < 0;
        }

        public String toString() {
            return stringForDebug();
        }

        private String stringForDebug() {
            String str = "";
            if (this.searchOrder < Integer.MAX_VALUE) {
                str = str + " so: " + this.searchOrder;
            }
            if (this.minDistance < Integer.MAX_VALUE) {
                str = str + " md: " + this.minDistance;
            }
            if (this.zipSize > 0) {
                str = str + " zs: " + this.zipSize;
            }
            if (this.byteSize > 0) {
                str = str + " bs: " + this.byteSize;
            }
            if (this.histSize > 0) {
                str = str + " hs: " + this.histSize;
            }
            return "Choice[" + this.index + "] " + str + " " + ((Object) this.coding);
        }
    }

    CodingChooser(int i2, Coding[] codingArr) {
        this.optUseHistogram = true;
        this.optUsePopulationCoding = true;
        this.optUseAdaptiveCoding = true;
        PropMap propMapCurrentPropMap = Utils.currentPropMap();
        if (propMapCurrentPropMap != null) {
            this.verbose = Math.max(propMapCurrentPropMap.getInteger("com.sun.java.util.jar.pack.verbose"), propMapCurrentPropMap.getInteger("com.sun.java.util.jar.pack.verbose.coding"));
            this.optUseHistogram = !propMapCurrentPropMap.getBoolean("com.sun.java.util.jar.pack.no.histogram");
            this.optUsePopulationCoding = !propMapCurrentPropMap.getBoolean("com.sun.java.util.jar.pack.no.population.coding");
            this.optUseAdaptiveCoding = !propMapCurrentPropMap.getBoolean("com.sun.java.util.jar.pack.no.adaptive.coding");
            int integer = propMapCurrentPropMap.getInteger("com.sun.java.util.jar.pack.stress.coding");
            if (integer != 0) {
                this.stress = new Random(integer);
            }
        }
        this.effort = i2;
        this.allCodingChoices = codingArr;
        this.fuzz = 1.0d + (0.0025d * (i2 - 5));
        int i3 = 0;
        for (Coding coding : codingArr) {
            if (coding != null) {
                i3++;
            }
        }
        this.choices = new Choice[i3];
        int i4 = 0;
        for (int i5 = 0; i5 < codingArr.length; i5++) {
            if (codingArr[i5] != null) {
                int i6 = i4;
                i4++;
                this.choices[i6] = new Choice(codingArr[i5], i5, new int[this.choices.length]);
            }
        }
        for (int i7 = 0; i7 < this.choices.length; i7++) {
            Coding coding2 = this.choices[i7].coding;
            if (!$assertionsDisabled && coding2.distanceFrom(coding2) != 0) {
                throw new AssertionError();
            }
            for (int i8 = 0; i8 < i7; i8++) {
                Coding coding3 = this.choices[i8].coding;
                int iDistanceFrom = coding2.distanceFrom(coding3);
                if (!$assertionsDisabled && iDistanceFrom <= 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && iDistanceFrom != coding3.distanceFrom(coding2)) {
                    throw new AssertionError();
                }
                this.choices[i7].distance[i8] = iDistanceFrom;
                this.choices[i8].distance[i7] = iDistanceFrom;
            }
        }
    }

    Choice makeExtraChoice(Coding coding) {
        int[] iArr = new int[this.choices.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            Coding coding2 = this.choices[i2].coding;
            int iDistanceFrom = coding.distanceFrom(coding2);
            if (!$assertionsDisabled && iDistanceFrom <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && iDistanceFrom != coding2.distanceFrom(coding)) {
                throw new AssertionError();
            }
            iArr[i2] = iDistanceFrom;
        }
        Choice choice = new Choice(coding, -1, iArr);
        choice.reset();
        return choice;
    }

    ByteArrayOutputStream getContext() {
        if (this.context == null) {
            this.context = new ByteArrayOutputStream(65536);
        }
        return this.context;
    }

    private void reset(int[] iArr, int i2, int i3) {
        this.values = iArr;
        this.start = i2;
        this.end = i3;
        this.deltas = null;
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        this.vHist = null;
        this.dHist = null;
        this.searchOrder = 0;
        this.regularChoice = null;
        this.bestChoice = null;
        this.bestMethod = null;
        this.bestZipSize = Integer.MAX_VALUE;
        this.bestByteSize = Integer.MAX_VALUE;
        this.targetSize = Integer.MAX_VALUE;
    }

    CodingMethod choose(int[] iArr, int i2, int i3, Coding coding, int[] iArr2) {
        reset(iArr, i2, i3);
        if (this.effort <= 1 || i2 >= i3) {
            if (iArr2 != null) {
                int[] iArrComputeSizePrivate = computeSizePrivate(coding);
                iArr2[0] = iArrComputeSizePrivate[0];
                iArr2[1] = iArrComputeSizePrivate[1];
            }
            return coding;
        }
        if (this.optUseHistogram) {
            getValueHistogram();
            getDeltaHistogram();
        }
        for (int i4 = i2; i4 < i3; i4++) {
            int i5 = iArr[i4];
            if (this.min > i5) {
                this.min = i5;
            }
            if (this.max < i5) {
                this.max = i5;
            }
        }
        int iMarkUsableChoices = markUsableChoices(coding);
        if (this.stress != null) {
            int iNextInt = this.stress.nextInt((iMarkUsableChoices * 2) + 4);
            Coding codingStressAdaptiveCoding = null;
            int i6 = 0;
            while (true) {
                if (i6 >= this.choices.length) {
                    break;
                }
                Choice choice = this.choices[i6];
                if (choice.searchOrder >= 0) {
                    int i7 = iNextInt;
                    iNextInt--;
                    if (i7 == 0) {
                        codingStressAdaptiveCoding = choice.coding;
                        break;
                    }
                }
                i6++;
            }
            if (codingStressAdaptiveCoding == null) {
                if ((iNextInt & 7) != 0) {
                    codingStressAdaptiveCoding = coding;
                } else {
                    codingStressAdaptiveCoding = stressCoding(this.min, this.max);
                }
            }
            if (!this.disablePopCoding && this.optUsePopulationCoding && this.effort >= 4) {
                codingStressAdaptiveCoding = stressPopCoding(codingStressAdaptiveCoding);
            }
            if (!this.disableRunCoding && this.optUseAdaptiveCoding && this.effort >= 3) {
                codingStressAdaptiveCoding = stressAdaptiveCoding(codingStressAdaptiveCoding);
            }
            return codingStressAdaptiveCoding;
        }
        double d2 = 1.0d;
        for (int i8 = this.effort; i8 < 9; i8++) {
            d2 /= 1.414d;
        }
        int iCeil = (int) Math.ceil(iMarkUsableChoices * d2);
        this.bestChoice = this.regularChoice;
        evaluate(this.regularChoice);
        int iUpdateDistances = updateDistances(this.regularChoice);
        int i9 = this.bestZipSize;
        int i10 = this.bestByteSize;
        if (this.regularChoice.coding == coding && this.topLevel) {
            int iEncodeEscapeValue = BandStructure.encodeEscapeValue(115, coding);
            if (coding.canRepresentSigned(iEncodeEscapeValue)) {
                this.regularChoice.zipSize -= coding.getLength(iEncodeEscapeValue);
                this.bestByteSize = this.regularChoice.byteSize;
                this.bestZipSize = this.regularChoice.zipSize;
            }
        }
        int i11 = 1;
        while (this.searchOrder < iCeil) {
            if (i11 > iUpdateDistances) {
                i11 = 1;
            }
            int i12 = iUpdateDistances / i11;
            int i13 = i11 * 2;
            i11 = i13;
            Choice choiceFindChoiceNear = findChoiceNear(this.bestChoice, i12, (iUpdateDistances / i13) + 1);
            if (choiceFindChoiceNear != null) {
                if (!$assertionsDisabled && !choiceFindChoiceNear.coding.canRepresent(this.min, this.max)) {
                    throw new AssertionError();
                }
                evaluate(choiceFindChoiceNear);
                int iUpdateDistances2 = updateDistances(choiceFindChoiceNear);
                if (choiceFindChoiceNear == this.bestChoice) {
                    iUpdateDistances = iUpdateDistances2;
                    if (this.verbose > 5) {
                        Utils.log.info("maxd = " + iUpdateDistances);
                    }
                }
            }
        }
        Coding coding2 = this.bestChoice.coding;
        if (!$assertionsDisabled && coding2 != this.bestMethod) {
            throw new AssertionError();
        }
        if (this.verbose > 2) {
            Utils.log.info("chooser: plain result=" + ((Object) this.bestChoice) + " after " + this.bestChoice.searchOrder + " rounds, " + (this.regularChoice.zipSize - this.bestZipSize) + " fewer bytes than regular " + ((Object) coding));
        }
        this.bestChoice = null;
        if (!this.disablePopCoding && this.optUsePopulationCoding && this.effort >= 4 && (this.bestMethod instanceof Coding)) {
            tryPopulationCoding(coding2);
        }
        if (!this.disableRunCoding && this.optUseAdaptiveCoding && this.effort >= 3 && (this.bestMethod instanceof Coding)) {
            tryAdaptiveCoding(coding2);
        }
        if (iArr2 != null) {
            iArr2[0] = this.bestByteSize;
            iArr2[1] = this.bestZipSize;
        }
        if (this.verbose > 1) {
            Utils.log.info("chooser: result=" + ((Object) this.bestMethod) + " " + (i9 - this.bestZipSize) + " fewer bytes than regular " + ((Object) coding) + "; win=" + pct(i9 - this.bestZipSize, i9));
        }
        CodingMethod codingMethod = this.bestMethod;
        reset(null, 0, 0);
        return codingMethod;
    }

    CodingMethod choose(int[] iArr, int i2, int i3, Coding coding) {
        return choose(iArr, i2, i3, coding, null);
    }

    CodingMethod choose(int[] iArr, Coding coding, int[] iArr2) {
        return choose(iArr, 0, iArr.length, coding, iArr2);
    }

    CodingMethod choose(int[] iArr, Coding coding) {
        return choose(iArr, 0, iArr.length, coding, null);
    }

    private int markUsableChoices(Coding coding) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.choices.length; i3++) {
            Choice choice = this.choices[i3];
            choice.reset();
            if (!choice.coding.canRepresent(this.min, this.max)) {
                choice.searchOrder = -1;
                if (this.verbose > 1 && choice.coding == coding) {
                    Utils.log.info("regular coding cannot represent [" + this.min + com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_PARENT + this.max + "]: " + ((Object) coding));
                }
            } else {
                if (choice.coding == coding) {
                    this.regularChoice = choice;
                }
                i2++;
            }
        }
        if (this.regularChoice == null && coding.canRepresent(this.min, this.max)) {
            this.regularChoice = makeExtraChoice(coding);
            if (this.verbose > 1) {
                Utils.log.info("*** regular choice is extra: " + ((Object) this.regularChoice.coding));
            }
        }
        if (this.regularChoice == null) {
            int i4 = 0;
            while (true) {
                if (i4 >= this.choices.length) {
                    break;
                }
                Choice choice2 = this.choices[i4];
                if (choice2.searchOrder == -1) {
                    i4++;
                } else {
                    this.regularChoice = choice2;
                    break;
                }
            }
            if (this.verbose > 1) {
                Utils.log.info("*** regular choice does not apply " + ((Object) coding));
                Utils.log.info("    using instead " + ((Object) this.regularChoice.coding));
            }
        }
        if (this.verbose > 2) {
            Utils.log.info("chooser: #choices=" + i2 + " [" + this.min + com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_PARENT + this.max + "]");
            if (this.verbose > 4) {
                for (int i5 = 0; i5 < this.choices.length; i5++) {
                    Choice choice3 = this.choices[i5];
                    if (choice3.searchOrder >= 0) {
                        Utils.log.info(sun.security.pkcs11.wrapper.Constants.INDENT + ((Object) choice3));
                    }
                }
            }
        }
        return i2;
    }

    private Choice findChoiceNear(Choice choice, int i2, int i3) {
        if (this.verbose > 5) {
            Utils.log.info("findChoice " + i2 + com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_PARENT + i3 + " near: " + ((Object) choice));
        }
        int[] iArr = choice.distance;
        Choice choice2 = null;
        for (int i4 = 0; i4 < this.choices.length; i4++) {
            Choice choice3 = this.choices[i4];
            if (choice3.searchOrder >= this.searchOrder && iArr[i4] >= i3 && iArr[i4] <= i2) {
                if (choice3.minDistance >= i3 && choice3.minDistance <= i2) {
                    if (this.verbose > 5) {
                        Utils.log.info("findChoice => good " + ((Object) choice3));
                    }
                    return choice3;
                }
                choice2 = choice3;
            }
        }
        if (this.verbose > 5) {
            Utils.log.info("findChoice => found " + ((Object) choice2));
        }
        return choice2;
    }

    private void evaluate(Choice choice) {
        boolean z2;
        if (!$assertionsDisabled && choice.searchOrder != Integer.MAX_VALUE) {
            throw new AssertionError();
        }
        int i2 = this.searchOrder;
        this.searchOrder = i2 + 1;
        choice.searchOrder = i2;
        if (choice != this.bestChoice && !choice.isExtra() && this.optUseHistogram) {
            choice.histSize = (int) Math.ceil(getHistogram(choice.coding.isDelta()).getBitLength(choice.coding) / 8.0d);
            choice.byteSize = choice.histSize;
            z2 = choice.byteSize <= this.targetSize;
        } else {
            z2 = true;
        }
        if (z2) {
            int[] iArrComputeSizePrivate = computeSizePrivate(choice.coding);
            choice.byteSize = iArrComputeSizePrivate[0];
            choice.zipSize = iArrComputeSizePrivate[1];
            if (noteSizes(choice.coding, choice.byteSize, choice.zipSize)) {
                this.bestChoice = choice;
            }
        }
        if (choice.histSize >= 0 && !$assertionsDisabled && choice.byteSize != choice.histSize) {
            throw new AssertionError();
        }
        if (this.verbose > 4) {
            Utils.log.info("evaluated " + ((Object) choice));
        }
    }

    private boolean noteSizes(CodingMethod codingMethod, int i2, int i3) {
        if (!$assertionsDisabled && (i3 <= 0 || i2 <= 0)) {
            throw new AssertionError();
        }
        boolean z2 = i3 < this.bestZipSize;
        if (this.verbose > 3) {
            Utils.log.info("computed size " + ((Object) codingMethod) + " " + i2 + "/zs=" + i3 + ((!z2 || this.bestMethod == null) ? "" : " better by " + pct(this.bestZipSize - i3, i3)));
        }
        if (z2) {
            this.bestMethod = codingMethod;
            this.bestZipSize = i3;
            this.bestByteSize = i2;
            this.targetSize = (int) (i2 * this.fuzz);
            return true;
        }
        return false;
    }

    private int updateDistances(Choice choice) {
        int[] iArr = choice.distance;
        int i2 = 0;
        for (int i3 = 0; i3 < this.choices.length; i3++) {
            Choice choice2 = this.choices[i3];
            if (choice2.searchOrder >= this.searchOrder) {
                int i4 = iArr[i3];
                if (this.verbose > 5) {
                    Utils.log.info("evaluate dist " + i4 + " to " + ((Object) choice2));
                }
                if (choice2.minDistance > i4) {
                    choice2.minDistance = i4;
                }
                if (i2 < i4) {
                    i2 = i4;
                }
            }
        }
        if (this.verbose > 5) {
            Utils.log.info("evaluate maxd => " + i2);
        }
        return i2;
    }

    public void computeSize(CodingMethod codingMethod, int[] iArr, int i2, int i3, int[] iArr2) {
        if (i3 <= i2) {
            iArr2[1] = 0;
            iArr2[0] = 0;
            return;
        }
        try {
            resetData();
            codingMethod.writeArrayTo(this.byteSizer, iArr, i2, i3);
            iArr2[0] = getByteSize();
            iArr2[1] = getZipSize();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void computeSize(CodingMethod codingMethod, int[] iArr, int[] iArr2) {
        computeSize(codingMethod, iArr, 0, iArr.length, iArr2);
    }

    public int[] computeSize(CodingMethod codingMethod, int[] iArr, int i2, int i3) {
        int[] iArr2 = {0, 0};
        computeSize(codingMethod, iArr, i2, i3, iArr2);
        return iArr2;
    }

    public int[] computeSize(CodingMethod codingMethod, int[] iArr) {
        return computeSize(codingMethod, iArr, 0, iArr.length);
    }

    private int[] computeSizePrivate(CodingMethod codingMethod) {
        int[] iArr = {0, 0};
        computeSize(codingMethod, this.values, this.start, this.end, iArr);
        return iArr;
    }

    public int computeByteSize(CodingMethod codingMethod, int[] iArr, int i2, int i3) {
        int iCountBytesToSizer;
        if (i3 - i2 < 0) {
            return 0;
        }
        if (codingMethod instanceof Coding) {
            int length = ((Coding) codingMethod).getLength(iArr, i2, i3);
            if ($assertionsDisabled || length == (iCountBytesToSizer = countBytesToSizer(codingMethod, iArr, i2, i3))) {
                return length;
            }
            throw new AssertionError((Object) (((Object) codingMethod) + " : " + length + " != " + iCountBytesToSizer));
        }
        return countBytesToSizer(codingMethod, iArr, i2, i3);
    }

    private int countBytesToSizer(CodingMethod codingMethod, int[] iArr, int i2, int i3) {
        try {
            this.byteOnlySizer.reset();
            codingMethod.writeArrayTo(this.byteOnlySizer, iArr, i2, i3);
            return this.byteOnlySizer.getSize();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    int[] getDeltas(int i2, int i3) {
        if ((i2 | i3) != 0) {
            return Coding.makeDeltas(this.values, this.start, this.end, i2, i3);
        }
        if (this.deltas == null) {
            this.deltas = Coding.makeDeltas(this.values, this.start, this.end, 0, 0);
        }
        return this.deltas;
    }

    Histogram getValueHistogram() {
        if (this.vHist == null) {
            this.vHist = new Histogram(this.values, this.start, this.end);
            if (this.verbose > 3) {
                this.vHist.print("vHist", System.out);
            } else if (this.verbose > 1) {
                this.vHist.print("vHist", null, System.out);
            }
        }
        return this.vHist;
    }

    Histogram getDeltaHistogram() {
        if (this.dHist == null) {
            this.dHist = new Histogram(getDeltas(0, 0));
            if (this.verbose > 3) {
                this.dHist.print("dHist", System.out);
            } else if (this.verbose > 1) {
                this.dHist.print("dHist", null, System.out);
            }
        }
        return this.dHist;
    }

    Histogram getHistogram(boolean z2) {
        return z2 ? getDeltaHistogram() : getValueHistogram();
    }

    private void tryPopulationCoding(Coding coding) {
        int iMin;
        Histogram valueHistogram = getValueHistogram();
        Coding valueCoding = coding.getValueCoding();
        Coding l2 = BandStructure.UNSIGNED5.setL(64);
        Coding valueCoding2 = coding.getValueCoding();
        int iMax = 4 + Math.max(valueCoding.getLength(this.min), valueCoding.getLength(this.max));
        int length = l2.getLength(0);
        int length2 = length * (this.end - this.start);
        int iCeil = (int) Math.ceil(valueHistogram.getBitLength(valueCoding2) / 8.0d);
        int i2 = iMax + length2 + iCeil;
        int i3 = 0;
        int[] iArr = new int[1 + valueHistogram.getTotalLength()];
        int i4 = -1;
        int i5 = -1;
        int[][] matrix = valueHistogram.getMatrix();
        int i6 = -1;
        int length3 = 1;
        int i7 = 0;
        for (int i8 = 1; i8 <= valueHistogram.getTotalLength(); i8++) {
            if (length3 == 1) {
                i6++;
                i7 = matrix[i6][0];
                length3 = matrix[i6].length;
            }
            length3--;
            int i9 = matrix[i6][length3];
            iArr[i8] = i9;
            int length4 = valueCoding.getLength(i9);
            iMax += length4;
            int i10 = i7;
            length2 += (l2.getLength(i8) - length) * i10;
            iCeil -= length4 * i10;
            int i11 = iMax + length2 + iCeil;
            if (i2 > i11) {
                if (i11 <= this.targetSize) {
                    i5 = i8;
                    if (i4 < 0) {
                        i4 = i8;
                    }
                    if (this.verbose > 4) {
                        Utils.log.info("better pop-size at fvc=" + i8 + " by " + pct(i2 - i11, i2));
                    }
                }
                i2 = i11;
                i3 = i8;
            }
        }
        if (i4 < 0) {
            if (this.verbose > 1 && this.verbose > 1) {
                Utils.log.info("no good pop-size; best was " + i2 + " at " + i3 + " worse by " + pct(i2 - this.bestByteSize, this.bestByteSize));
                return;
            }
            return;
        }
        if (this.verbose > 1) {
            Utils.log.info("initial best pop-size at fvc=" + i3 + " in [" + i4 + com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_PARENT + i5 + "] by " + pct(this.bestByteSize - i2, this.bestByteSize));
        }
        int i12 = this.bestZipSize;
        int[] iArr2 = PopulationCoding.LValuesCoded;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        if (i3 <= 255) {
            arrayList.add(BandStructure.BYTE1);
        } else {
            int iB = 5;
            boolean z2 = this.effort > 4;
            if (z2) {
                arrayList2.add(BandStructure.BYTE1.setS(1));
            }
            for (int length5 = iArr2.length - 1; length5 >= 1; length5--) {
                int i13 = iArr2[length5];
                Coding codingFitTokenCoding = PopulationCoding.fitTokenCoding(i4, i13);
                Coding codingFitTokenCoding2 = PopulationCoding.fitTokenCoding(i3, i13);
                Coding codingFitTokenCoding3 = PopulationCoding.fitTokenCoding(i5, i13);
                if (codingFitTokenCoding2 != null) {
                    if (!arrayList.contains(codingFitTokenCoding2)) {
                        arrayList.add(codingFitTokenCoding2);
                    }
                    if (iB > codingFitTokenCoding2.B()) {
                        iB = codingFitTokenCoding2.B();
                    }
                }
                if (z2) {
                    if (codingFitTokenCoding3 == null) {
                        codingFitTokenCoding3 = codingFitTokenCoding2;
                    }
                    for (int iB2 = codingFitTokenCoding.B(); iB2 <= codingFitTokenCoding3.B(); iB2++) {
                        if (iB2 != codingFitTokenCoding2.B() && iB2 != 1) {
                            Coding s2 = codingFitTokenCoding3.setB(iB2).setS(1);
                            if (!arrayList2.contains(s2)) {
                                arrayList2.add(s2);
                            }
                        }
                    }
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Coding coding2 = (Coding) it.next();
                if (coding2.B() > iB) {
                    it.remove();
                    arrayList3.add(0, coding2);
                }
            }
        }
        ArrayList<Coding> arrayList4 = new ArrayList();
        Iterator it2 = arrayList.iterator();
        Iterator<E> it3 = arrayList2.iterator();
        Iterator<E> it4 = arrayList3.iterator();
        while (true) {
            if (!it2.hasNext() && !it3.hasNext() && !it4.hasNext()) {
                break;
            }
            if (it2.hasNext()) {
                arrayList4.add(it2.next());
            }
            if (it3.hasNext()) {
                arrayList4.add(it3.next());
            }
            if (it4.hasNext()) {
                arrayList4.add(it4.next());
            }
        }
        arrayList.clear();
        arrayList2.clear();
        arrayList3.clear();
        int size = arrayList4.size();
        if (this.effort == 4) {
            size = 2;
        } else if (size > 4) {
            size = (((size - 4) * (this.effort - 4)) / 5) + 4;
        }
        if (arrayList4.size() > size) {
            if (this.verbose > 4) {
                Utils.log.info("allFits before clip: " + ((Object) arrayList4));
            }
            arrayList4.subList(size, arrayList4.size()).clear();
        }
        if (this.verbose > 3) {
            Utils.log.info("allFits: " + ((Object) arrayList4));
        }
        for (Coding s3 : arrayList4) {
            boolean z3 = false;
            if (s3.S() == 1) {
                z3 = true;
                s3 = s3.setS(0);
            }
            if (!z3) {
                iMin = i3;
                if (!$assertionsDisabled && s3.umax() < iMin) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && s3.B() != 1 && s3.setB(s3.B() - 1).umax() >= iMin) {
                    throw new AssertionError();
                }
            } else {
                iMin = Math.min(s3.umax(), i5);
                if (iMin >= i4 && iMin != i3) {
                }
            }
            PopulationCoding populationCoding = new PopulationCoding();
            populationCoding.setHistogram(valueHistogram);
            populationCoding.setL(s3.L());
            populationCoding.setFavoredValues(iArr, iMin);
            if (!$assertionsDisabled && populationCoding.tokenCoding != s3) {
                throw new AssertionError();
            }
            populationCoding.resortFavoredValues();
            int[] iArrComputePopSizePrivate = computePopSizePrivate(populationCoding, valueCoding, valueCoding2);
            noteSizes(populationCoding, iArrComputePopSizePrivate[0], 4 + iArrComputePopSizePrivate[1]);
        }
        if (this.verbose > 3) {
            Utils.log.info("measured best pop, size=" + this.bestByteSize + "/zs=" + this.bestZipSize + " better by " + pct(i12 - this.bestZipSize, i12));
            if (this.bestZipSize < i12) {
                Utils.log.info(">>> POP WINS BY " + (i12 - this.bestZipSize));
            }
        }
    }

    private int[] computePopSizePrivate(PopulationCoding populationCoding, Coding coding, Coding coding2) {
        if (this.popHelper == null) {
            this.popHelper = new CodingChooser(this.effort, this.allCodingChoices);
            if (this.stress != null) {
                this.popHelper.addStressSeed(this.stress.nextInt());
            }
            this.popHelper.topLevel = false;
            this.popHelper.verbose--;
            this.popHelper.disablePopCoding = true;
            this.popHelper.disableRunCoding = this.disableRunCoding;
            if (this.effort < 5) {
                this.popHelper.disableRunCoding = true;
            }
        }
        int i2 = populationCoding.fVlen;
        if (this.verbose > 2) {
            Utils.log.info("computePopSizePrivate fvlen=" + i2 + " tc=" + ((Object) populationCoding.tokenCoding));
            Utils.log.info("{ //BEGIN");
        }
        int[] iArr = populationCoding.fValues;
        int[][] iArrEncodeValues = populationCoding.encodeValues(this.values, this.start, this.end);
        int[] iArr2 = iArrEncodeValues[0];
        int[] iArr3 = iArrEncodeValues[1];
        if (this.verbose > 2) {
            Utils.log.info("-- refine on fv[" + i2 + "] fc=" + ((Object) coding));
        }
        populationCoding.setFavoredCoding(this.popHelper.choose(iArr, 1, 1 + i2, coding));
        if ((populationCoding.tokenCoding instanceof Coding) && (this.stress == null || this.stress.nextBoolean())) {
            if (this.verbose > 2) {
                Utils.log.info("-- refine on tv[" + iArr2.length + "] tc=" + ((Object) populationCoding.tokenCoding));
            }
            CodingMethod codingMethodChoose = this.popHelper.choose(iArr2, (Coding) populationCoding.tokenCoding);
            if (codingMethodChoose != populationCoding.tokenCoding) {
                if (this.verbose > 2) {
                    Utils.log.info(">>> refined tc=" + ((Object) codingMethodChoose));
                }
                populationCoding.setTokenCoding(codingMethodChoose);
            }
        }
        if (iArr3.length == 0) {
            populationCoding.setUnfavoredCoding(null);
        } else {
            if (this.verbose > 2) {
                Utils.log.info("-- refine on uv[" + iArr3.length + "] uc=" + ((Object) populationCoding.unfavoredCoding));
            }
            populationCoding.setUnfavoredCoding(this.popHelper.choose(iArr3, coding2));
        }
        if (this.verbose > 3) {
            Utils.log.info("finish computePopSizePrivate fvlen=" + i2 + " fc=" + ((Object) populationCoding.favoredCoding) + " tc=" + ((Object) populationCoding.tokenCoding) + " uc=" + ((Object) populationCoding.unfavoredCoding));
            StringBuilder sb = new StringBuilder();
            sb.append("fv = {");
            for (int i3 = 1; i3 <= i2; i3++) {
                if (i3 % 10 == 0) {
                    sb.append('\n');
                }
                sb.append(" ").append(iArr[i3]);
            }
            sb.append('\n');
            sb.append("}");
            Utils.log.info(sb.toString());
        }
        if (this.verbose > 2) {
            Utils.log.info("} //END");
        }
        if (this.stress != null) {
            return null;
        }
        try {
            resetData();
            populationCoding.writeSequencesTo(this.byteSizer, iArr2, iArr3);
            int[] iArr4 = {getByteSize(), getZipSize()};
            int[] iArr5 = null;
            if (!$assertionsDisabled) {
                int[] iArrComputeSizePrivate = computeSizePrivate(populationCoding);
                iArr5 = iArrComputeSizePrivate;
                if (iArrComputeSizePrivate == null) {
                    throw new AssertionError();
                }
            }
            if ($assertionsDisabled || iArr5[0] == iArr4[0]) {
                return iArr4;
            }
            throw new AssertionError((Object) (iArr5[0] + " != " + iArr4[0]));
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void tryAdaptiveCoding(Coding coding) {
        double d2;
        int i2;
        CodingMethod codingMethodChoose;
        CodingMethod codingMethodChoose2;
        CodingMethod adaptiveCoding;
        double d3;
        int i3 = this.bestZipSize;
        int i4 = this.start;
        int length = this.end;
        int[] deltas = this.values;
        int i5 = length - i4;
        if (coding.isDelta()) {
            deltas = getDeltas(0, 0);
            i4 = 0;
            length = deltas.length;
        }
        int[] iArr = new int[i5 + 1];
        int i6 = 0;
        int i7 = 0;
        for (int i8 = i4; i8 < length; i8++) {
            int i9 = deltas[i8];
            int i10 = i6;
            i6++;
            iArr[i10] = i7;
            int length2 = coding.getLength(i9);
            if (!$assertionsDisabled && length2 >= Integer.MAX_VALUE) {
                throw new AssertionError();
            }
            i7 += length2;
        }
        int i11 = i6;
        int i12 = i6 + 1;
        iArr[i11] = i7;
        if (!$assertionsDisabled && i12 != iArr.length) {
            throw new AssertionError();
        }
        double d4 = i7 / i5;
        if (this.effort >= 5) {
            if (this.effort > 6) {
                d2 = 1.001d;
            } else {
                d2 = 1.003d;
            }
        } else if (this.effort > 3) {
            d2 = 1.01d;
        } else {
            d2 = 1.03d;
        }
        double d5 = d2 * d2;
        double d6 = d5 * d5;
        double d7 = d5 * d5 * d5;
        double[] dArr = new double[1 + (this.effort - 3)];
        double dLog = Math.log(i5);
        for (int i13 = 0; i13 < dArr.length; i13++) {
            dArr[i13] = Math.exp((dLog * (i13 + 1)) / (dArr.length + 1));
        }
        int[] iArr2 = new int[dArr.length];
        int i14 = 0;
        for (double d8 : dArr) {
            int nextK = AdaptiveCoding.getNextK(((int) Math.round(d8)) - 1);
            if (nextK > 0 && nextK < i5 && (i14 <= 0 || nextK != iArr2[i14 - 1])) {
                int i15 = i14;
                i14++;
                iArr2[i15] = nextK;
            }
        }
        int[] iArrRealloc = BandStructure.realloc(iArr2, i14);
        int[] iArr3 = new int[iArrRealloc.length];
        double[] dArr2 = new double[iArrRealloc.length];
        for (int i16 = 0; i16 < iArrRealloc.length; i16++) {
            int i17 = iArrRealloc[i16];
            if (i17 < 10) {
                d3 = d7;
            } else if (i17 < 100) {
                d3 = d6;
            } else {
                d3 = d5;
            }
            double d9 = d3;
            dArr2[i16] = d9;
            iArr3[i16] = 4 + ((int) Math.ceil(i17 * d4 * d9));
        }
        if (this.verbose > 1) {
            System.out.print("tryAdaptiveCoding [" + i5 + "] avgS=" + d4 + " fuzz=" + d5 + " meshes: {");
            for (int i18 = 0; i18 < iArrRealloc.length; i18++) {
                System.out.print(" " + iArrRealloc[i18] + "(" + iArr3[i18] + ")");
            }
            Utils.log.info(" }");
        }
        if (this.runHelper == null) {
            this.runHelper = new CodingChooser(this.effort, this.allCodingChoices);
            if (this.stress != null) {
                this.runHelper.addStressSeed(this.stress.nextInt());
            }
            this.runHelper.topLevel = false;
            this.runHelper.verbose--;
            this.runHelper.disableRunCoding = true;
            this.runHelper.disablePopCoding = this.disablePopCoding;
            if (this.effort < 5) {
                this.runHelper.disablePopCoding = true;
            }
        }
        int i19 = 0;
        while (i19 < i5) {
            int nextK2 = AdaptiveCoding.getNextK(i19 - 1);
            if (nextK2 > i5) {
                nextK2 = i5;
            }
            int length3 = iArrRealloc.length - 1;
            while (true) {
                if (length3 >= 0) {
                    int i20 = iArrRealloc[length3];
                    int i21 = iArr3[length3];
                    if (nextK2 + i20 > i5 || (i2 = iArr[nextK2 + i20] - iArr[nextK2]) < i21) {
                        length3--;
                    } else {
                        int nextK3 = nextK2 + i20;
                        int i22 = i2;
                        double d10 = d4 * dArr2[length3];
                        while (true) {
                            if (nextK3 >= i5 || nextK3 - nextK2 > i5 / 2) {
                                break;
                            }
                            int i23 = nextK3;
                            int i24 = i22;
                            nextK3 = nextK2 + AdaptiveCoding.getNextK(((nextK3 + i20) - nextK2) - 1);
                            if (nextK3 < 0 || nextK3 > i5) {
                                nextK3 = i5;
                            }
                            i22 = iArr[nextK3] - iArr[nextK2];
                            if (i22 < 4.0d + ((nextK3 - nextK2) * d10)) {
                                i22 = i24;
                                nextK3 = i23;
                                break;
                            }
                        }
                        int i25 = nextK3;
                        if (this.verbose > 2) {
                            Utils.log.info("bulge at " + nextK2 + "[" + (nextK3 - nextK2) + "] of " + pct(i22 - (d4 * (nextK3 - nextK2)), d4 * (nextK3 - nextK2)));
                            Utils.log.info("{ //BEGIN");
                        }
                        CodingMethod codingMethodChoose3 = this.runHelper.choose(this.values, this.start + nextK2, this.start + nextK3, coding);
                        if (codingMethodChoose3 == coding) {
                            codingMethodChoose = coding;
                            codingMethodChoose2 = coding;
                        } else {
                            codingMethodChoose = this.runHelper.choose(this.values, this.start, this.start + nextK2, coding);
                            codingMethodChoose2 = this.runHelper.choose(this.values, this.start + nextK3, this.start + i5, coding);
                        }
                        if (this.verbose > 2) {
                            Utils.log.info("} //END");
                        }
                        if (codingMethodChoose == codingMethodChoose3 && nextK2 > 0 && AdaptiveCoding.isCodableLength(nextK3)) {
                            nextK2 = 0;
                        }
                        if (codingMethodChoose3 == codingMethodChoose2 && nextK3 < i5) {
                            nextK3 = i5;
                        }
                        if (codingMethodChoose != coding || codingMethodChoose3 != coding || codingMethodChoose2 != coding) {
                            int i26 = 0;
                            if (nextK3 == i5) {
                                adaptiveCoding = codingMethodChoose3;
                            } else {
                                adaptiveCoding = new AdaptiveCoding(nextK3 - nextK2, codingMethodChoose3, codingMethodChoose2);
                                i26 = 0 + 4;
                            }
                            if (nextK2 > 0) {
                                adaptiveCoding = new AdaptiveCoding(nextK2, codingMethodChoose, adaptiveCoding);
                                i26 += 4;
                            }
                            int[] iArrComputeSizePrivate = computeSizePrivate(adaptiveCoding);
                            noteSizes(adaptiveCoding, iArrComputeSizePrivate[0], iArrComputeSizePrivate[1] + i26);
                        }
                        nextK2 = i25;
                    }
                }
            }
            i19 = nextK2 + 1;
        }
        if (this.verbose > 3 && this.bestZipSize < i3) {
            Utils.log.info(">>> RUN WINS BY " + (i3 - this.bestZipSize));
        }
    }

    private static String pct(double d2, double d3) {
        return (Math.round((d2 / d3) * 10000.0d) / 100.0d) + FXMLLoader.RESOURCE_KEY_PREFIX;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/CodingChooser$Sizer.class */
    static class Sizer extends OutputStream {
        final OutputStream out;
        private int count;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CodingChooser.class.desiredAssertionStatus();
        }

        Sizer(OutputStream outputStream) {
            this.out = outputStream;
        }

        Sizer() {
            this(null);
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.count++;
            if (this.out != null) {
                this.out.write(i2);
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.count += i3;
            if (this.out != null) {
                this.out.write(bArr, i2, i3);
            }
        }

        public void reset() {
            this.count = 0;
        }

        public int getSize() {
            return this.count;
        }

        public String toString() {
            String string = super.toString();
            if (!$assertionsDisabled) {
                String strStringForDebug = stringForDebug();
                string = strStringForDebug;
                if (strStringForDebug == null) {
                    throw new AssertionError();
                }
            }
            return string;
        }

        String stringForDebug() {
            return "<Sizer " + getSize() + ">";
        }
    }

    private void resetData() {
        flushData();
        this.zipDef.reset();
        if (this.context != null) {
            try {
                this.context.writeTo(this.byteSizer);
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        this.zipSizer.reset();
        this.byteSizer.reset();
    }

    private void flushData() {
        try {
            this.zipOut.finish();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private int getByteSize() {
        return this.byteSizer.getSize();
    }

    private int getZipSize() {
        flushData();
        return this.zipSizer.getSize();
    }

    void addStressSeed(int i2) {
        if (this.stress == null) {
            return;
        }
        this.stress.setSeed(i2 + (this.stress.nextInt() << 32));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CodingMethod stressPopCoding(CodingMethod codingMethod) {
        if (!$assertionsDisabled && this.stress == null) {
            throw new AssertionError();
        }
        if (!(codingMethod instanceof Coding)) {
            return codingMethod;
        }
        Coding valueCoding = ((Coding) codingMethod).getValueCoding();
        Histogram valueHistogram = getValueHistogram();
        int iStressLen = stressLen(valueHistogram.getTotalLength());
        if (iStressLen == 0) {
            return codingMethod;
        }
        ArrayList arrayList = new ArrayList();
        if (this.stress.nextBoolean()) {
            HashSet hashSet = new HashSet();
            for (int i2 = this.start; i2 < this.end; i2++) {
                if (hashSet.add(Integer.valueOf(this.values[i2]))) {
                    arrayList.add(Integer.valueOf(this.values[i2]));
                }
            }
        } else {
            for (int[] iArr : valueHistogram.getMatrix()) {
                for (int i3 = 1; i3 < iArr.length; i3++) {
                    arrayList.add(Integer.valueOf(iArr[i3]));
                }
            }
        }
        int iNextInt = this.stress.nextInt();
        if ((iNextInt & 7) <= 2) {
            Collections.shuffle(arrayList, this.stress);
        } else {
            int i4 = iNextInt >>> 3;
            if ((i4 & 7) <= 2) {
                Collections.sort(arrayList);
            }
            int i5 = i4 >>> 3;
            if ((i5 & 7) <= 2) {
                Collections.reverse(arrayList);
            }
            int i6 = i5 >>> 3;
            iNextInt = i6;
            if ((i6 & 7) <= 2) {
                Collections.rotate(arrayList, stressLen(arrayList.size()));
            }
        }
        if (arrayList.size() > iStressLen) {
            if (((iNextInt >>> 3) & 7) <= 2) {
                arrayList.subList(iStressLen, arrayList.size()).clear();
            } else {
                arrayList.subList(0, arrayList.size() - iStressLen).clear();
            }
        }
        int size = arrayList.size();
        int[] iArr2 = new int[1 + size];
        for (int i7 = 0; i7 < size; i7++) {
            iArr2[1 + i7] = ((Integer) arrayList.get(i7)).intValue();
        }
        PopulationCoding populationCoding = new PopulationCoding();
        populationCoding.setFavoredValues(iArr2, size);
        int[] iArr3 = PopulationCoding.LValuesCoded;
        int i8 = 0;
        while (true) {
            if (i8 >= iArr3.length / 2) {
                break;
            }
            int i9 = iArr3[this.stress.nextInt(iArr3.length)];
            if (i9 < 0 || PopulationCoding.fitTokenCoding(size, i9) == null) {
                i8++;
            } else {
                populationCoding.setL(i9);
                break;
            }
        }
        if (populationCoding.tokenCoding == null) {
            int i10 = iArr2[1];
            int i11 = i10;
            for (int i12 = 2; i12 <= size; i12++) {
                int i13 = iArr2[i12];
                if (i10 > i13) {
                    i10 = i13;
                }
                if (i11 < i13) {
                    i11 = i13;
                }
            }
            populationCoding.tokenCoding = stressCoding(i10, i11);
        }
        computePopSizePrivate(populationCoding, valueCoding, valueCoding);
        return populationCoding;
    }

    private CodingMethod stressAdaptiveCoding(CodingMethod codingMethod) {
        int iDecodeK;
        if (!$assertionsDisabled && this.stress == null) {
            throw new AssertionError();
        }
        if (!(codingMethod instanceof Coding)) {
            return codingMethod;
        }
        Coding coding = (Coding) codingMethod;
        int i2 = this.end - this.start;
        if (i2 < 2) {
            return codingMethod;
        }
        int iStressLen = stressLen(i2 - 1) + 1;
        if (iStressLen == i2) {
            return codingMethod;
        }
        try {
            if (!$assertionsDisabled && this.disableRunCoding) {
                throw new AssertionError();
            }
            this.disableRunCoding = true;
            int[] iArr = (int[]) this.values.clone();
            CodingMethod adaptiveCoding = null;
            int i3 = this.end;
            int i4 = this.start;
            while (i3 > i4) {
                int iNextInt = i3 - i4 < 100 ? -1 : this.stress.nextInt();
                if ((iNextInt & 7) != 0) {
                    iDecodeK = iStressLen == 1 ? iStressLen : stressLen(iStressLen - 1) + 1;
                } else {
                    int i5 = iNextInt >>> 3;
                    int i6 = i5 & 3;
                    int i7 = (i5 >>> 3) & 255;
                    while (true) {
                        iDecodeK = AdaptiveCoding.decodeK(i6, i7);
                        if (iDecodeK <= i3 - i4) {
                            break;
                        }
                        if (i7 != 3) {
                            i7 = 3;
                        } else {
                            i6--;
                        }
                    }
                    if (!$assertionsDisabled && !AdaptiveCoding.isCodableLength(iDecodeK)) {
                        throw new AssertionError();
                    }
                }
                if (iDecodeK > i3 - i4) {
                    iDecodeK = i3 - i4;
                }
                while (!AdaptiveCoding.isCodableLength(iDecodeK)) {
                    iDecodeK--;
                }
                int i8 = i3 - iDecodeK;
                if (!$assertionsDisabled && i8 >= i3) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && i8 < i4) {
                    throw new AssertionError();
                }
                CodingMethod codingMethodChoose = choose(iArr, i8, i3, coding);
                if (adaptiveCoding == null) {
                    adaptiveCoding = codingMethodChoose;
                } else {
                    adaptiveCoding = new AdaptiveCoding(i3 - i8, codingMethodChoose, adaptiveCoding);
                }
                i3 = i8;
            }
            return adaptiveCoding;
        } finally {
            this.disableRunCoding = false;
        }
    }

    private Coding stressCoding(int i2, int i3) {
        if (!$assertionsDisabled && this.stress == null) {
            throw new AssertionError();
        }
        for (int i4 = 0; i4 < 100; i4++) {
            Coding codingOf = Coding.of(this.stress.nextInt(5) + 1, this.stress.nextInt(256) + 1, this.stress.nextInt(3));
            if (codingOf.B() == 1) {
                codingOf = codingOf.setH(256);
            }
            if (codingOf.H() == 256 && codingOf.B() >= 5) {
                codingOf = codingOf.setB(4);
            }
            if (this.stress.nextBoolean()) {
                Coding d2 = codingOf.setD(1);
                if (d2.canRepresent(i2, i3)) {
                    return d2;
                }
            }
            if (codingOf.canRepresent(i2, i3)) {
                return codingOf;
            }
        }
        return BandStructure.UNSIGNED5;
    }

    private int stressLen(int i2) {
        if (!$assertionsDisabled && this.stress == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError();
        }
        int iNextInt = this.stress.nextInt(100);
        if (iNextInt < 20) {
            return Math.min(i2 / 5, iNextInt);
        }
        if (iNextInt < 40) {
            return i2;
        }
        return this.stress.nextInt(i2);
    }
}
