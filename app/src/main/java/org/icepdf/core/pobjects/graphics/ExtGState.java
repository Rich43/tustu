package org.icepdf.core.pobjects.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ExtGState.class */
public class ExtGState extends Dictionary {
    private static final Logger logger = Logger.getLogger(ExtGState.class.toString());
    public static final Name SMASK_KEY = new Name("SMask");
    public static final Name LW_KEY = new Name(PdfOps.LW_TOKEN);
    public static final Name LC_KEY = new Name("LC");
    public static final Name LJ_KEY = new Name("LJ");
    public static final Name ML_KEY = new Name("ML");
    public static final Name CA_KEY = new Name("CA");
    public static final Name ca_KEY = new Name("ca");
    public static final Name BM_KEY = new Name("BM");
    public static final Name OP_KEY = new Name("OP");
    public static final Name op_KEY = new Name("op");
    public static final Name OPM_KEY = new Name("OPM");
    public static final Name D_KEY = new Name(PdfOps.D_TOKEN);

    public ExtGState(Library library, HashMap graphicsState) {
        super(library, graphicsState);
    }

    Number getLineWidth() {
        return getNumber(LW_KEY);
    }

    Number getLineCapStyle() {
        return getNumber(LC_KEY);
    }

    Name getBlendingMode() {
        return this.library.getName(this.entries, BM_KEY);
    }

    Number getLineJoinStyle() {
        return getNumber(LJ_KEY);
    }

    Number getMiterLimit() {
        return getNumber(ML_KEY);
    }

    List getLineDashPattern() {
        float[] dashArray;
        List<Object> dashPattern = null;
        if (this.entries.containsKey(D_KEY)) {
            try {
                List dashData = (List) this.entries.get(D_KEY);
                Number dashPhase = (Number) dashData.get(1);
                List dashVector = (List) dashData.get(0);
                if (dashVector.size() > 0) {
                    int sz = dashVector.size();
                    dashArray = new float[sz];
                    for (int i2 = 0; i2 < sz; i2++) {
                        dashArray[i2] = ((Number) dashVector.get(i2)).floatValue();
                    }
                } else {
                    dashPhase = Float.valueOf(0.0f);
                    dashArray = null;
                }
                dashPattern = new ArrayList<>(2);
                dashPattern.add(dashArray);
                dashPattern.add(dashPhase);
            } catch (ClassCastException e2) {
                logger.log(Level.FINE, "Dash pattern syntax error: ", (Throwable) e2);
            }
        }
        return dashPattern;
    }

    Number getStrokingAlphConstant() {
        return getNumber(CA_KEY);
    }

    Number getNonStrokingAlphConstant() {
        return getNumber(ca_KEY);
    }

    Boolean getOverprint() {
        Object o2 = getObject(OP_KEY);
        if (o2 instanceof String) {
            return Boolean.valueOf((String) o2);
        }
        if (o2 instanceof Boolean) {
            return (Boolean) o2;
        }
        return null;
    }

    Boolean getOverprintFill() {
        Object o2 = getObject(op_KEY);
        if (o2 instanceof String) {
            return Boolean.valueOf((String) o2);
        }
        if (o2 instanceof Boolean) {
            return (Boolean) o2;
        }
        return null;
    }

    Number getOverprintMode() {
        return getNumber(OPM_KEY);
    }

    public SoftMask getSMask() {
        Object tmp = this.library.getObject(this.entries, SMASK_KEY);
        if (tmp != null && (tmp instanceof HashMap)) {
            SoftMask softMask = new SoftMask(this.library, (HashMap) tmp);
            return softMask;
        }
        return null;
    }
}
