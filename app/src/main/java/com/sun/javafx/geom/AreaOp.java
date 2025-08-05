package com.sun.javafx.geom;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp.class */
public abstract class AreaOp {
    public static final int CTAG_LEFT = 0;
    public static final int CTAG_RIGHT = 1;
    public static final int ETAG_IGNORE = 0;
    public static final int ETAG_ENTER = 1;
    public static final int ETAG_EXIT = -1;
    public static final int RSTAG_INSIDE = 1;
    public static final int RSTAG_OUTSIDE = -1;
    private static Comparator YXTopComparator = (o1, o2) -> {
        Curve c1 = ((Edge) o1).getCurve();
        Curve c2 = ((Edge) o2).getCurve();
        double yTop = c1.getYTop();
        double v1 = yTop;
        double yTop2 = c2.getYTop();
        double v2 = yTop2;
        if (yTop == yTop2) {
            double xTop = c1.getXTop();
            v1 = xTop;
            v2 = c2.getXTop();
            if (xTop == xTop) {
                return 0;
            }
        }
        if (v1 < v2) {
            return -1;
        }
        return 1;
    };
    private static final CurveLink[] EmptyLinkList = new CurveLink[2];
    private static final ChainEnd[] EmptyChainList = new ChainEnd[2];

    public abstract void newRow();

    public abstract int classify(Edge edge);

    public abstract int getState();

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$CAGOp.class */
    public static abstract class CAGOp extends AreaOp {
        boolean inLeft;
        boolean inRight;
        boolean inResult;

        public abstract boolean newClassification(boolean z2, boolean z3);

        public CAGOp() {
            super();
        }

        @Override // com.sun.javafx.geom.AreaOp
        public void newRow() {
            this.inLeft = false;
            this.inRight = false;
            this.inResult = false;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int classify(Edge e2) {
            if (e2.getCurveTag() == 0) {
                this.inLeft = !this.inLeft;
            } else {
                this.inRight = !this.inRight;
            }
            boolean newClass = newClassification(this.inLeft, this.inRight);
            if (this.inResult == newClass) {
                return 0;
            }
            this.inResult = newClass;
            return newClass ? 1 : -1;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int getState() {
            return this.inResult ? 1 : -1;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$AddOp.class */
    public static class AddOp extends CAGOp {
        @Override // com.sun.javafx.geom.AreaOp.CAGOp
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return inLeft || inRight;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$SubOp.class */
    public static class SubOp extends CAGOp {
        @Override // com.sun.javafx.geom.AreaOp.CAGOp
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return inLeft && !inRight;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$IntOp.class */
    public static class IntOp extends CAGOp {
        @Override // com.sun.javafx.geom.AreaOp.CAGOp
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return inLeft && inRight;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$XorOp.class */
    public static class XorOp extends CAGOp {
        @Override // com.sun.javafx.geom.AreaOp.CAGOp
        public boolean newClassification(boolean inLeft, boolean inRight) {
            return inLeft != inRight;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$NZWindOp.class */
    public static class NZWindOp extends AreaOp {
        private int count;

        public NZWindOp() {
            super();
        }

        @Override // com.sun.javafx.geom.AreaOp
        public void newRow() {
            this.count = 0;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int classify(Edge e2) {
            int newCount = this.count;
            int type = newCount == 0 ? 1 : 0;
            int newCount2 = newCount + e2.getCurve().getDirection();
            this.count = newCount2;
            if (newCount2 == 0) {
                return -1;
            }
            return type;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int getState() {
            return this.count == 0 ? -1 : 1;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/AreaOp$EOWindOp.class */
    public static class EOWindOp extends AreaOp {
        private boolean inside;

        public EOWindOp() {
            super();
        }

        @Override // com.sun.javafx.geom.AreaOp
        public void newRow() {
            this.inside = false;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int classify(Edge e2) {
            boolean newInside = !this.inside;
            this.inside = newInside;
            return newInside ? 1 : -1;
        }

        @Override // com.sun.javafx.geom.AreaOp
        public int getState() {
            return this.inside ? 1 : -1;
        }
    }

    private AreaOp() {
    }

    public Vector calculate(Vector left, Vector right) {
        Vector edges = new Vector();
        addEdges(edges, left, 0);
        addEdges(edges, right, 1);
        return pruneEdges(edges);
    }

    private static void addEdges(Vector edges, Vector curves, int curvetag) {
        Enumeration enum_ = curves.elements();
        while (enum_.hasMoreElements()) {
            Curve c2 = (Curve) enum_.nextElement2();
            if (c2.getOrder() > 0) {
                edges.add(new Edge(c2, curvetag));
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
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.AreaOp.pruneEdges(java.util.Vector):java.util.Vector");
    }

    public static void finalizeSubCurves(Vector subcurves, Vector chains) {
        int numchains = chains.size();
        if (numchains == 0) {
            return;
        }
        if ((numchains & 1) != 0) {
            throw new InternalError("Odd number of chains!");
        }
        ChainEnd[] endlist = new ChainEnd[numchains];
        chains.toArray(endlist);
        for (int i2 = 1; i2 < numchains; i2 += 2) {
            ChainEnd open = endlist[i2 - 1];
            ChainEnd close = endlist[i2];
            CurveLink subcurve = open.linkTo(close);
            if (subcurve != null) {
                subcurves.add(subcurve);
            }
        }
        chains.clear();
    }

    public static void resolveLinks(Vector subcurves, Vector chains, Vector links) {
        CurveLink[] linklist;
        ChainEnd[] endlist;
        int numlinks = links.size();
        if (numlinks == 0) {
            linklist = EmptyLinkList;
        } else {
            if ((numlinks & 1) != 0) {
                throw new InternalError("Odd number of new curves!");
            }
            linklist = new CurveLink[numlinks + 2];
            links.toArray(linklist);
        }
        int numchains = chains.size();
        if (numchains == 0) {
            endlist = EmptyChainList;
        } else {
            if ((numchains & 1) != 0) {
                throw new InternalError("Odd number of chains!");
            }
            endlist = new ChainEnd[numchains + 2];
            chains.toArray(endlist);
        }
        int curchain = 0;
        int curlink = 0;
        chains.clear();
        ChainEnd chain = endlist[0];
        ChainEnd nextchain = endlist[1];
        CurveLink link = linklist[0];
        CurveLink nextlink = linklist[1];
        while (true) {
            if (chain == null && link == null) {
                break;
            }
            boolean connectchains = link == null;
            boolean connectlinks = chain == null;
            if (!connectchains && !connectlinks) {
                connectchains = (curchain & 1) == 0 && chain.getX() == nextchain.getX();
                connectlinks = (curlink & 1) == 0 && link.getX() == nextlink.getX();
                if (!connectchains && !connectlinks) {
                    double cx = chain.getX();
                    double lx = link.getX();
                    connectchains = nextchain != null && cx < lx && obstructs(nextchain.getX(), lx, curchain);
                    connectlinks = nextlink != null && lx < cx && obstructs(nextlink.getX(), cx, curlink);
                }
            }
            if (connectchains) {
                CurveLink subcurve = chain.linkTo(nextchain);
                if (subcurve != null) {
                    subcurves.add(subcurve);
                }
                curchain += 2;
                chain = endlist[curchain];
                nextchain = endlist[curchain + 1];
            }
            if (connectlinks) {
                ChainEnd openend = new ChainEnd(link, null);
                ChainEnd closeend = new ChainEnd(nextlink, openend);
                openend.setOtherEnd(closeend);
                chains.add(openend);
                chains.add(closeend);
                curlink += 2;
                link = linklist[curlink];
                nextlink = linklist[curlink + 1];
            }
            if (!connectchains && !connectlinks) {
                chain.addLink(link);
                chains.add(chain);
                curchain++;
                chain = nextchain;
                nextchain = endlist[curchain + 1];
                curlink++;
                link = nextlink;
                nextlink = linklist[curlink + 1];
            }
        }
        if ((chains.size() & 1) != 0) {
            System.out.println("Odd number of chains!");
        }
    }

    public static boolean obstructs(double v1, double v2, int phase) {
        return (phase & 1) == 0 ? v1 <= v2 : v1 < v2;
    }
}
