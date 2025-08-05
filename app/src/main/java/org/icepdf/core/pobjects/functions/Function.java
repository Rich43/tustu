package org.icepdf.core.pobjects.functions;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/Function.class */
public abstract class Function {
    private static final Logger logger = Logger.getLogger(Function.class.toString());
    public static final Name FUNCTIONTYPE_NAME = new Name("FunctionType");
    public static final Name DOMAIN_NAME = new Name("Domain");
    public static final Name RANGE_NAME = new Name("Range");
    protected float[] domain;
    protected float[] range;
    protected int functionType;

    public abstract float[] calculate(float[] fArr);

    public static Function getFunction(Library l2, Object o2) {
        Dictionary d2 = null;
        if (o2 instanceof Reference) {
            o2 = l2.getObject((Reference) o2);
        }
        if (o2 instanceof Dictionary) {
            d2 = (Dictionary) o2;
        } else if (o2 instanceof HashMap) {
            d2 = new Dictionary(l2, (HashMap) o2);
        }
        if (d2 != null) {
            int fType = d2.getInt(FUNCTIONTYPE_NAME);
            switch (fType) {
                case 0:
                    return new Function_0(d2);
                case 1:
                default:
                    return null;
                case 2:
                    return new Function_2(d2);
                case 3:
                    return new Function_3(d2);
                case 4:
                    return new Function_4(d2);
            }
        }
        return null;
    }

    protected Function(Dictionary d2) {
        List dom = (List) d2.getObject(DOMAIN_NAME);
        this.domain = new float[dom.size()];
        for (int i2 = 0; i2 < dom.size(); i2++) {
            this.domain[i2] = ((Number) dom.get(i2)).floatValue();
        }
        List r2 = (List) d2.getObject(RANGE_NAME);
        if (r2 != null) {
            this.range = new float[r2.size()];
            for (int i3 = 0; i3 < r2.size(); i3++) {
                this.range[i3] = ((Number) r2.get(i3)).floatValue();
            }
        }
    }

    public int getFunctionType() {
        return this.functionType;
    }

    public static float interpolate(float x2, float xmin, float xmax, float ymin, float ymax) {
        return (((x2 - xmin) * (ymax - ymin)) / (xmax - xmin)) + ymin;
    }

    public float[] getDomain() {
        return this.domain;
    }

    public float[] getRange() {
        return this.range;
    }
}
