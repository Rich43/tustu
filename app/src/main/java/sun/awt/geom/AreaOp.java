package sun.awt.geom;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:sun/awt/geom/AreaOp.class */
public abstract class AreaOp {
    public static final int CTAG_LEFT = 0;
    public static final int CTAG_RIGHT = 1;
    public static final int ETAG_IGNORE = 0;
    public static final int ETAG_ENTER = 1;
    public static final int ETAG_EXIT = -1;
    public static final int RSTAG_INSIDE = 1;
    public static final int RSTAG_OUTSIDE = -1;
    private static Comparator YXTopComparator = new Comparator() { // from class: sun.awt.geom.AreaOp.1
        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            Curve curve = ((Edge) obj).getCurve();
            Curve curve2 = ((Edge) obj2).getCurve();
            double yTop = curve.getYTop();
            double d2 = yTop;
            double yTop2 = curve2.getYTop();
            double xTop = yTop2;
            if (yTop == yTop2) {
                double xTop2 = curve.getXTop();
                d2 = xTop2;
                xTop = curve2.getXTop();
                if (xTop2 == xTop2) {
                    return 0;
                }
            }
            if (d2 < xTop) {
                return -1;
            }
            return 1;
        }
    };
    private static CurveLink[] EmptyLinkList = new CurveLink[2];
    private static ChainEnd[] EmptyChainList = new ChainEnd[2];

    public abstract void newRow();

    public abstract int classify(Edge edge);

    public abstract int getState();

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$CAGOp.class */
    public static abstract class CAGOp extends AreaOp {
        boolean inLeft;
        boolean inRight;
        boolean inResult;

        public abstract boolean newClassification(boolean z2, boolean z3);

        public CAGOp() {
            super();
        }

        @Override // sun.awt.geom.AreaOp
        public void newRow() {
            this.inLeft = false;
            this.inRight = false;
            this.inResult = false;
        }

        @Override // sun.awt.geom.AreaOp
        public int classify(Edge edge) {
            if (edge.getCurveTag() == 0) {
                this.inLeft = !this.inLeft;
            } else {
                this.inRight = !this.inRight;
            }
            boolean zNewClassification = newClassification(this.inLeft, this.inRight);
            if (this.inResult == zNewClassification) {
                return 0;
            }
            this.inResult = zNewClassification;
            return zNewClassification ? 1 : -1;
        }

        @Override // sun.awt.geom.AreaOp
        public int getState() {
            return this.inResult ? 1 : -1;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$AddOp.class */
    public static class AddOp extends CAGOp {
        @Override // sun.awt.geom.AreaOp.CAGOp
        public boolean newClassification(boolean z2, boolean z3) {
            return z2 || z3;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$SubOp.class */
    public static class SubOp extends CAGOp {
        @Override // sun.awt.geom.AreaOp.CAGOp
        public boolean newClassification(boolean z2, boolean z3) {
            return z2 && !z3;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$IntOp.class */
    public static class IntOp extends CAGOp {
        @Override // sun.awt.geom.AreaOp.CAGOp
        public boolean newClassification(boolean z2, boolean z3) {
            return z2 && z3;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$XorOp.class */
    public static class XorOp extends CAGOp {
        @Override // sun.awt.geom.AreaOp.CAGOp
        public boolean newClassification(boolean z2, boolean z3) {
            return z2 != z3;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$NZWindOp.class */
    public static class NZWindOp extends AreaOp {
        private int count;

        public NZWindOp() {
            super();
        }

        @Override // sun.awt.geom.AreaOp
        public void newRow() {
            this.count = 0;
        }

        @Override // sun.awt.geom.AreaOp
        public int classify(Edge edge) {
            int i2 = this.count;
            int i3 = i2 == 0 ? 1 : 0;
            int direction = i2 + edge.getCurve().getDirection();
            this.count = direction;
            if (direction == 0) {
                return -1;
            }
            return i3;
        }

        @Override // sun.awt.geom.AreaOp
        public int getState() {
            return this.count == 0 ? -1 : 1;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/AreaOp$EOWindOp.class */
    public static class EOWindOp extends AreaOp {
        private boolean inside;

        public EOWindOp() {
            super();
        }

        @Override // sun.awt.geom.AreaOp
        public void newRow() {
            this.inside = false;
        }

        @Override // sun.awt.geom.AreaOp
        public int classify(Edge edge) {
            boolean z2 = !this.inside;
            this.inside = z2;
            return z2 ? 1 : -1;
        }

        @Override // sun.awt.geom.AreaOp
        public int getState() {
            return this.inside ? 1 : -1;
        }
    }

    private AreaOp() {
    }

    public Vector calculate(Vector vector, Vector vector2) {
        Vector vector3 = new Vector();
        addEdges(vector3, vector, 0);
        addEdges(vector3, vector2, 1);
        return pruneEdges(vector3);
    }

    private static void addEdges(Vector vector, Vector vector2, int i2) {
        Enumeration enumerationElements = vector2.elements();
        while (enumerationElements.hasMoreElements()) {
            Curve curve = (Curve) enumerationElements.nextElement2();
            if (curve.getOrder() > 0) {
                vector.add(new Edge(curve, i2));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x01c8, code lost:
    
        r0[r18] = r0;
        r17 = r17 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.Vector pruneEdges(java.util.Vector r11) {
        /*
            Method dump skipped, instructions count: 1075
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.geom.AreaOp.pruneEdges(java.util.Vector):java.util.Vector");
    }

    public static void finalizeSubCurves(Vector vector, Vector vector2) {
        int size = vector2.size();
        if (size == 0) {
            return;
        }
        if ((size & 1) != 0) {
            throw new InternalError("Odd number of chains!");
        }
        ChainEnd[] chainEndArr = new ChainEnd[size];
        vector2.toArray(chainEndArr);
        for (int i2 = 1; i2 < size; i2 += 2) {
            CurveLink curveLinkLinkTo = chainEndArr[i2 - 1].linkTo(chainEndArr[i2]);
            if (curveLinkLinkTo != null) {
                vector.add(curveLinkLinkTo);
            }
        }
        vector2.clear();
    }

    public static void resolveLinks(Vector vector, Vector vector2, Vector vector3) {
        CurveLink[] curveLinkArr;
        ChainEnd[] chainEndArr;
        int size = vector3.size();
        if (size == 0) {
            curveLinkArr = EmptyLinkList;
        } else {
            if ((size & 1) != 0) {
                throw new InternalError("Odd number of new curves!");
            }
            curveLinkArr = new CurveLink[size + 2];
            vector3.toArray(curveLinkArr);
        }
        int size2 = vector2.size();
        if (size2 == 0) {
            chainEndArr = EmptyChainList;
        } else {
            if ((size2 & 1) != 0) {
                throw new InternalError("Odd number of chains!");
            }
            chainEndArr = new ChainEnd[size2 + 2];
            vector2.toArray(chainEndArr);
        }
        int i2 = 0;
        int i3 = 0;
        vector2.clear();
        ChainEnd chainEnd = chainEndArr[0];
        ChainEnd chainEnd2 = chainEndArr[1];
        CurveLink curveLink = curveLinkArr[0];
        CurveLink curveLink2 = curveLinkArr[1];
        while (true) {
            if (chainEnd == null && curveLink == null) {
                break;
            }
            boolean z2 = curveLink == null;
            boolean z3 = chainEnd == null;
            if (!z2 && !z3) {
                z2 = (i2 & 1) == 0 && chainEnd.getX() == chainEnd2.getX();
                z3 = (i3 & 1) == 0 && curveLink.getX() == curveLink2.getX();
                if (!z2 && !z3) {
                    double x2 = chainEnd.getX();
                    double x3 = curveLink.getX();
                    z2 = chainEnd2 != null && x2 < x3 && obstructs(chainEnd2.getX(), x3, i2);
                    z3 = curveLink2 != null && x3 < x2 && obstructs(curveLink2.getX(), x2, i3);
                }
            }
            if (z2) {
                CurveLink curveLinkLinkTo = chainEnd.linkTo(chainEnd2);
                if (curveLinkLinkTo != null) {
                    vector.add(curveLinkLinkTo);
                }
                i2 += 2;
                chainEnd = chainEndArr[i2];
                chainEnd2 = chainEndArr[i2 + 1];
            }
            if (z3) {
                ChainEnd chainEnd3 = new ChainEnd(curveLink, null);
                ChainEnd chainEnd4 = new ChainEnd(curveLink2, chainEnd3);
                chainEnd3.setOtherEnd(chainEnd4);
                vector2.add(chainEnd3);
                vector2.add(chainEnd4);
                i3 += 2;
                curveLink = curveLinkArr[i3];
                curveLink2 = curveLinkArr[i3 + 1];
            }
            if (!z2 && !z3) {
                chainEnd.addLink(curveLink);
                vector2.add(chainEnd);
                i2++;
                chainEnd = chainEnd2;
                chainEnd2 = chainEndArr[i2 + 1];
                i3++;
                curveLink = curveLink2;
                curveLink2 = curveLinkArr[i3 + 1];
            }
        }
        if ((vector2.size() & 1) != 0) {
            System.out.println("Odd number of chains!");
        }
    }

    public static boolean obstructs(double d2, double d3, int i2) {
        return (i2 & 1) == 0 ? d2 <= d3 : d2 < d3;
    }
}
