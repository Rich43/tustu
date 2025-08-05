package sun.java2d.loops;

import java.awt.AlphaComposite;
import java.util.HashMap;

/* loaded from: rt.jar:sun/java2d/loops/CompositeType.class */
public final class CompositeType {
    private int uniqueID;
    private String desc;
    private CompositeType next;
    private static int unusedUID = 1;
    private static final HashMap<String, Integer> compositeUIDMap = new HashMap<>(100);
    public static final String DESC_ANY = "Any CompositeContext";
    public static final CompositeType Any = new CompositeType(null, DESC_ANY);
    public static final CompositeType General = Any;
    public static final String DESC_ANY_ALPHA = "Any AlphaComposite Rule";
    public static final CompositeType AnyAlpha = General.deriveSubType(DESC_ANY_ALPHA);
    public static final String DESC_XOR = "XOR mode";
    public static final CompositeType Xor = General.deriveSubType(DESC_XOR);
    public static final String DESC_CLEAR = "Porter-Duff Clear";
    public static final CompositeType Clear = AnyAlpha.deriveSubType(DESC_CLEAR);
    public static final String DESC_SRC = "Porter-Duff Src";
    public static final CompositeType Src = AnyAlpha.deriveSubType(DESC_SRC);
    public static final String DESC_DST = "Porter-Duff Dst";
    public static final CompositeType Dst = AnyAlpha.deriveSubType(DESC_DST);
    public static final String DESC_SRC_OVER = "Porter-Duff Src Over Dst";
    public static final CompositeType SrcOver = AnyAlpha.deriveSubType(DESC_SRC_OVER);
    public static final String DESC_DST_OVER = "Porter-Duff Dst Over Src";
    public static final CompositeType DstOver = AnyAlpha.deriveSubType(DESC_DST_OVER);
    public static final String DESC_SRC_IN = "Porter-Duff Src In Dst";
    public static final CompositeType SrcIn = AnyAlpha.deriveSubType(DESC_SRC_IN);
    public static final String DESC_DST_IN = "Porter-Duff Dst In Src";
    public static final CompositeType DstIn = AnyAlpha.deriveSubType(DESC_DST_IN);
    public static final String DESC_SRC_OUT = "Porter-Duff Src HeldOutBy Dst";
    public static final CompositeType SrcOut = AnyAlpha.deriveSubType(DESC_SRC_OUT);
    public static final String DESC_DST_OUT = "Porter-Duff Dst HeldOutBy Src";
    public static final CompositeType DstOut = AnyAlpha.deriveSubType(DESC_DST_OUT);
    public static final String DESC_SRC_ATOP = "Porter-Duff Src Atop Dst";
    public static final CompositeType SrcAtop = AnyAlpha.deriveSubType(DESC_SRC_ATOP);
    public static final String DESC_DST_ATOP = "Porter-Duff Dst Atop Src";
    public static final CompositeType DstAtop = AnyAlpha.deriveSubType(DESC_DST_ATOP);
    public static final String DESC_ALPHA_XOR = "Porter-Duff Xor";
    public static final CompositeType AlphaXor = AnyAlpha.deriveSubType(DESC_ALPHA_XOR);
    public static final String DESC_SRC_NO_EA = "Porter-Duff Src, No Extra Alpha";
    public static final CompositeType SrcNoEa = Src.deriveSubType(DESC_SRC_NO_EA);
    public static final String DESC_SRC_OVER_NO_EA = "Porter-Duff SrcOverDst, No Extra Alpha";
    public static final CompositeType SrcOverNoEa = SrcOver.deriveSubType(DESC_SRC_OVER_NO_EA);
    public static final CompositeType OpaqueSrcOverNoEa = SrcOverNoEa.deriveSubType(DESC_SRC).deriveSubType(DESC_SRC_NO_EA);

    public CompositeType deriveSubType(String str) {
        return new CompositeType(this, str);
    }

    public static CompositeType forAlphaComposite(AlphaComposite alphaComposite) {
        switch (alphaComposite.getRule()) {
            case 1:
                return Clear;
            case 2:
                if (alphaComposite.getAlpha() >= 1.0f) {
                    return SrcNoEa;
                }
                return Src;
            case 3:
                if (alphaComposite.getAlpha() >= 1.0f) {
                    return SrcOverNoEa;
                }
                return SrcOver;
            case 4:
                return DstOver;
            case 5:
                return SrcIn;
            case 6:
                return DstIn;
            case 7:
                return SrcOut;
            case 8:
                return DstOut;
            case 9:
                return Dst;
            case 10:
                return SrcAtop;
            case 11:
                return DstAtop;
            case 12:
                return AlphaXor;
            default:
                throw new InternalError("Unrecognized alpha rule");
        }
    }

    private CompositeType(CompositeType compositeType, String str) {
        this.next = compositeType;
        this.desc = str;
        this.uniqueID = makeUniqueID(str);
    }

    public static final synchronized int makeUniqueID(String str) {
        Integer numValueOf = compositeUIDMap.get(str);
        if (numValueOf == null) {
            if (unusedUID > 255) {
                throw new InternalError("composite type id overflow");
            }
            int i2 = unusedUID;
            unusedUID = i2 + 1;
            numValueOf = Integer.valueOf(i2);
            compositeUIDMap.put(str, numValueOf);
        }
        return numValueOf.intValue();
    }

    public int getUniqueID() {
        return this.uniqueID;
    }

    public String getDescriptor() {
        return this.desc;
    }

    public CompositeType getSuperType() {
        return this.next;
    }

    public int hashCode() {
        return this.desc.hashCode();
    }

    public boolean isDerivedFrom(CompositeType compositeType) {
        CompositeType compositeType2 = this;
        while (compositeType2.desc != compositeType.desc) {
            compositeType2 = compositeType2.next;
            if (compositeType2 == null) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        return (obj instanceof CompositeType) && ((CompositeType) obj).uniqueID == this.uniqueID;
    }

    public String toString() {
        return this.desc;
    }
}
