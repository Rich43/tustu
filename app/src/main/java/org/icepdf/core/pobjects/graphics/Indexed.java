package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/Indexed.class */
public class Indexed extends PColorSpace {
    public static final Name INDEXED_KEY = new Name(PdfOps.I_NAME);
    public static final Name I_KEY = new Name("I");
    PColorSpace colorSpace;
    int hival;
    byte[] colors;
    private boolean inited;
    private Color[] cols;

    Indexed(Library library, HashMap entries, List dictionary) {
        super(library, entries);
        this.colors = new byte[]{-1, -1, -1, 0, 0, 0};
        this.inited = false;
        this.colorSpace = getColorSpace(library, dictionary.get(1));
        this.hival = ((Number) dictionary.get(2)).intValue();
        if (!(dictionary.get(3) instanceof StringObject)) {
            if (dictionary.get(3) instanceof Reference) {
                this.colors = new byte[this.colorSpace.getNumComponents() * (this.hival + 1)];
                Stream lookup = (Stream) library.getObject((Reference) dictionary.get(3));
                byte[] colorStream = lookup.getDecodedStreamBytes(0);
                int length = this.colors.length < colorStream.length ? this.colors.length : colorStream.length;
                System.arraycopy(colorStream, 0, this.colors, 0, length);
                return;
            }
            return;
        }
        StringObject tmpText = (StringObject) dictionary.get(3);
        String tmp = tmpText.getDecryptedLiteralString(library.securityManager);
        byte[] textBytes = new byte[this.colorSpace.getNumComponents() * (this.hival + 1)];
        for (int i2 = 0; i2 < textBytes.length; i2++) {
            textBytes[i2] = (byte) tmp.charAt(i2);
        }
        this.colors = textBytes;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 1;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public String getDescription() {
        String desc = super.getDescription();
        if (this.colorSpace != null) {
            desc = desc + CallSiteDescriptor.TOKEN_DELIMITER + this.colorSpace.getDescription();
        }
        return desc;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        int numCSComps = this.colorSpace.getNumComponents();
        int[] b1 = new int[numCSComps];
        float[] f1 = new float[numCSComps];
        this.cols = new Color[this.hival + 1];
        for (int j2 = 0; j2 <= this.hival; j2++) {
            for (int i2 = 0; i2 < numCSComps; i2++) {
                b1[(numCSComps - 1) - i2] = 255 & this.colors[(j2 * numCSComps) + i2];
            }
            this.colorSpace.normaliseComponentsToFloats(b1, f1, 255.0f);
            this.cols[j2] = this.colorSpace.getColor(f1, true);
        }
        this.inited = true;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        init();
        int index = (int) (f2[0] * (this.cols.length - 1));
        if (index < this.cols.length) {
            return this.cols[index];
        }
        return this.cols[(int) f2[0]];
    }

    public Color[] accessColorTable() {
        return this.cols;
    }
}
