package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.functions.Function;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/DeviceN.class */
public class DeviceN extends PColorSpace {
    public static final Name DEVICEN_KEY = new Name("DeviceN");
    public static final Name COLORANTS_KEY = new Name("Colorants");
    List<Name> names;
    PColorSpace alternate;
    Function tintTransform;
    HashMap<Object, Object> colorants;
    PColorSpace[] colorspaces;
    boolean foundCMYK;

    DeviceN(Library l2, HashMap h2, Object o1, Object o2, Object o3, Object o4) {
        super(l2, h2);
        this.colorants = new HashMap<>();
        this.names = (List) o1;
        this.alternate = getColorSpace(l2, o2);
        this.tintTransform = Function.getFunction(l2, l2.getObject(o3));
        if (o4 != null) {
            HashMap h1 = (HashMap) this.library.getObject(o4);
            HashMap h22 = (HashMap) this.library.getObject(h1, COLORANTS_KEY);
            if (h22 != null) {
                Set e2 = h22.keySet();
                for (Object o5 : e2) {
                    Object oo = h22.get(o5);
                    this.colorants.put(o5, getColorSpace(this.library, this.library.getObject(oo)));
                }
            }
        }
        this.colorspaces = new PColorSpace[this.names.size()];
        for (int i2 = 0; i2 < this.colorspaces.length; i2++) {
            this.colorspaces[i2] = (PColorSpace) this.colorants.get(this.names.get(i2).toString());
        }
        int cmykCount = 0;
        this.foundCMYK = true;
        for (Name name : this.names) {
            if (name.getName().toLowerCase().startsWith(PdfOps.c_TOKEN)) {
                cmykCount++;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.m_TOKEN)) {
                cmykCount++;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.y_TOKEN)) {
                cmykCount++;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.k_TOKEN)) {
                cmykCount++;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.b_TOKEN)) {
                cmykCount++;
            }
        }
        if (cmykCount < 1) {
            this.foundCMYK = false;
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return this.names.size();
    }

    private float[] assignCMYK(float[] f2) {
        float[] f22 = new float[4];
        int i2 = 0;
        int max = this.names.size();
        while (i2 < max) {
            Name name = this.names.get(i2);
            if (name.getName().toLowerCase().startsWith(PdfOps.c_TOKEN)) {
                f22[0] = i2 < f2.length ? f2[i2] : 0.0f;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.m_TOKEN)) {
                f22[1] = i2 < f2.length ? f2[i2] : 0.0f;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.y_TOKEN)) {
                f22[2] = i2 < f2.length ? f2[i2] : 0.0f;
            } else if (name.getName().toLowerCase().startsWith(PdfOps.b_TOKEN) || name.getName().toLowerCase().startsWith(PdfOps.k_TOKEN)) {
                f22[3] = i2 < f2.length ? f2[i2] : 0.0f;
            }
            i2++;
        }
        if (f2.length != 4) {
            f22 = reverse(f22);
        }
        return f22;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        if (this.foundCMYK && (f2.length == 4 || !fillAndStroke)) {
            return new DeviceCMYK(null, null).getColor(assignCMYK(f2));
        }
        float[] y2 = this.tintTransform.calculate(reverse(f2));
        return this.alternate.getColor(reverse(y2));
    }
}
