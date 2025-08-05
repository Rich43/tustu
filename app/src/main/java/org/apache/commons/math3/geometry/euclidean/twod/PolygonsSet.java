package org.apache.commons.math3.geometry.euclidean.twod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/PolygonsSet.class */
public class PolygonsSet extends AbstractRegion<Euclidean2D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private Vector2D[][] vertices;

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ AbstractRegion buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean2D>) bSPTree);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public /* bridge */ /* synthetic */ Region buildNew(BSPTree bSPTree) {
        return buildNew((BSPTree<Euclidean2D>) bSPTree);
    }

    public PolygonsSet(double tolerance) {
        super(tolerance);
    }

    public PolygonsSet(BSPTree<Euclidean2D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public PolygonsSet(Collection<SubHyperplane<Euclidean2D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public PolygonsSet(double xMin, double xMax, double yMin, double yMax, double tolerance) {
        super(boxBoundary(xMin, xMax, yMin, yMax, tolerance), tolerance);
    }

    public PolygonsSet(double hyperplaneThickness, Vector2D... vertices) {
        super(verticesToTree(hyperplaneThickness, vertices), hyperplaneThickness);
    }

    @Deprecated
    public PolygonsSet() {
        this(1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(BSPTree<Euclidean2D> tree) {
        this(tree, 1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(Collection<SubHyperplane<Euclidean2D>> boundary) {
        this(boundary, 1.0E-10d);
    }

    @Deprecated
    public PolygonsSet(double xMin, double xMax, double yMin, double yMax) {
        this(xMin, xMax, yMin, yMax, 1.0E-10d);
    }

    private static Line[] boxBoundary(double xMin, double xMax, double yMin, double yMax, double tolerance) {
        if (xMin >= xMax - tolerance || yMin >= yMax - tolerance) {
            return null;
        }
        Vector2D minMin = new Vector2D(xMin, yMin);
        Vector2D minMax = new Vector2D(xMin, yMax);
        Vector2D maxMin = new Vector2D(xMax, yMin);
        Vector2D maxMax = new Vector2D(xMax, yMax);
        return new Line[]{new Line(minMin, maxMin, tolerance), new Line(maxMin, maxMax, tolerance), new Line(maxMax, minMax, tolerance), new Line(minMax, minMin, tolerance)};
    }

    private static BSPTree<Euclidean2D> verticesToTree(double hyperplaneThickness, Vector2D... vertices) {
        int n2 = vertices.length;
        if (n2 == 0) {
            return new BSPTree<>(Boolean.TRUE);
        }
        Vertex[] vArray = new Vertex[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            vArray[i2] = new Vertex(vertices[i2]);
        }
        List<Edge> edges = new ArrayList<>(n2);
        for (int i3 = 0; i3 < n2; i3++) {
            Vertex start = vArray[i3];
            Vertex end = vArray[(i3 + 1) % n2];
            Line line = start.sharedLineWith(end);
            if (line == null) {
                line = new Line(start.getLocation(), end.getLocation(), hyperplaneThickness);
            }
            edges.add(new Edge(start, end, line));
            for (Vertex vertex : vArray) {
                if (vertex != start && vertex != end && FastMath.abs(line.getOffset((Point<Euclidean2D>) vertex.getLocation())) <= hyperplaneThickness) {
                    vertex.bindWith(line);
                }
            }
        }
        BSPTree<Euclidean2D> tree = new BSPTree<>();
        insertEdges(hyperplaneThickness, tree, edges);
        return tree;
    }

    private static void insertEdges(double hyperplaneThickness, BSPTree<Euclidean2D> node, List<Edge> edges) {
        int index = 0;
        Edge inserted = null;
        while (inserted == null && index < edges.size()) {
            int i2 = index;
            index++;
            inserted = edges.get(i2);
            if (inserted.getNode() == null) {
                if (node.insertCut(inserted.getLine())) {
                    inserted.setNode(node);
                } else {
                    inserted = null;
                }
            } else {
                inserted = null;
            }
        }
        if (inserted == null) {
            BSPTree<S> parent = node.getParent();
            if (parent == 0 || node == parent.getMinus()) {
                node.setAttribute(Boolean.TRUE);
                return;
            } else {
                node.setAttribute(Boolean.FALSE);
                return;
            }
        }
        List<Edge> plusList = new ArrayList<>();
        List<Edge> minusList = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge != inserted) {
                double startOffset = inserted.getLine().getOffset((Point<Euclidean2D>) edge.getStart().getLocation());
                double endOffset = inserted.getLine().getOffset((Point<Euclidean2D>) edge.getEnd().getLocation());
                Side startSide = FastMath.abs(startOffset) <= hyperplaneThickness ? Side.HYPER : startOffset < 0.0d ? Side.MINUS : Side.PLUS;
                Side endSide = FastMath.abs(endOffset) <= hyperplaneThickness ? Side.HYPER : endOffset < 0.0d ? Side.MINUS : Side.PLUS;
                switch (startSide) {
                    case PLUS:
                        if (endSide == Side.MINUS) {
                            Vertex splitPoint = edge.split(inserted.getLine());
                            minusList.add(splitPoint.getOutgoing());
                            plusList.add(splitPoint.getIncoming());
                            break;
                        } else {
                            plusList.add(edge);
                            break;
                        }
                    case MINUS:
                        if (endSide == Side.PLUS) {
                            Vertex splitPoint2 = edge.split(inserted.getLine());
                            minusList.add(splitPoint2.getIncoming());
                            plusList.add(splitPoint2.getOutgoing());
                            break;
                        } else {
                            minusList.add(edge);
                            break;
                        }
                    default:
                        if (endSide == Side.PLUS) {
                            plusList.add(edge);
                            break;
                        } else if (endSide == Side.MINUS) {
                            minusList.add(edge);
                            break;
                        } else {
                            break;
                        }
                }
            }
        }
        if (!plusList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getPlus(), plusList);
        } else {
            node.getPlus().setAttribute(Boolean.FALSE);
        }
        if (!minusList.isEmpty()) {
            insertEdges(hyperplaneThickness, node.getMinus(), minusList);
        } else {
            node.getMinus().setAttribute(Boolean.TRUE);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/PolygonsSet$Vertex.class */
    private static class Vertex {
        private final Vector2D location;
        private Edge incoming = null;
        private Edge outgoing = null;
        private final List<Line> lines = new ArrayList();

        Vertex(Vector2D location) {
            this.location = location;
        }

        public Vector2D getLocation() {
            return this.location;
        }

        public void bindWith(Line line) {
            this.lines.add(line);
        }

        public Line sharedLineWith(Vertex vertex) {
            for (Line line1 : this.lines) {
                for (Line line2 : vertex.lines) {
                    if (line1 == line2) {
                        return line1;
                    }
                }
            }
            return null;
        }

        public void setIncoming(Edge incoming) {
            this.incoming = incoming;
            bindWith(incoming.getLine());
        }

        public Edge getIncoming() {
            return this.incoming;
        }

        public void setOutgoing(Edge outgoing) {
            this.outgoing = outgoing;
            bindWith(outgoing.getLine());
        }

        public Edge getOutgoing() {
            return this.outgoing;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/PolygonsSet$Edge.class */
    private static class Edge {
        private final Vertex start;
        private final Vertex end;
        private final Line line;
        private BSPTree<Euclidean2D> node = null;

        Edge(Vertex start, Vertex end, Line line) {
            this.start = start;
            this.end = end;
            this.line = line;
            start.setOutgoing(this);
            end.setIncoming(this);
        }

        public Vertex getStart() {
            return this.start;
        }

        public Vertex getEnd() {
            return this.end;
        }

        public Line getLine() {
            return this.line;
        }

        public void setNode(BSPTree<Euclidean2D> node) {
            this.node = node;
        }

        public BSPTree<Euclidean2D> getNode() {
            return this.node;
        }

        public Vertex split(Line splitLine) {
            Vertex splitVertex = new Vertex(this.line.intersection(splitLine));
            splitVertex.bindWith(splitLine);
            Edge startHalf = new Edge(this.start, splitVertex, this.line);
            Edge endHalf = new Edge(splitVertex, this.end, this.line);
            startHalf.node = this.node;
            endHalf.node = this.node;
            return splitVertex;
        }
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion, org.apache.commons.math3.geometry.partitioning.Region
    public PolygonsSet buildNew(BSPTree<Euclidean2D> tree) {
        return new PolygonsSet(tree, getTolerance());
    }

    @Override // org.apache.commons.math3.geometry.partitioning.AbstractRegion
    protected void computeGeometricalProperties() {
        Vector2D[][] v2 = getVertices();
        if (v2.length == 0) {
            BSPTree<Euclidean2D> tree = getTree(false);
            if (tree.getCut() == null && ((Boolean) tree.getAttribute()).booleanValue()) {
                setSize(Double.POSITIVE_INFINITY);
                setBarycenter((Point) Vector2D.NaN);
                return;
            } else {
                setSize(0.0d);
                setBarycenter((Point) new Vector2D(0.0d, 0.0d));
                return;
            }
        }
        if (v2[0][0] == null) {
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point) Vector2D.NaN);
            return;
        }
        double sum = 0.0d;
        double sumX = 0.0d;
        double sumY = 0.0d;
        for (Vector2D[] loop : v2) {
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (Vector2D point : loop) {
                double x0 = x1;
                double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                double factor = (x0 * y1) - (y0 * x1);
                sum += factor;
                sumX += factor * (x0 + x1);
                sumY += factor * (y0 + y1);
            }
        }
        if (sum < 0.0d) {
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter((Point) Vector2D.NaN);
        } else {
            setSize(sum / 2.0d);
            setBarycenter((Point) new Vector2D(sumX / (3.0d * sum), sumY / (3.0d * sum)));
        }
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [org.apache.commons.math3.geometry.euclidean.twod.Vector2D[], org.apache.commons.math3.geometry.euclidean.twod.Vector2D[][]] */
    /* JADX WARN: Type inference failed for: r1v50, types: [org.apache.commons.math3.geometry.euclidean.twod.Vector2D[], org.apache.commons.math3.geometry.euclidean.twod.Vector2D[][]] */
    public Vector2D[][] getVertices() {
        if (this.vertices == null) {
            if (getTree(false).getCut() == null) {
                this.vertices = new Vector2D[0];
            } else {
                SegmentsBuilder visitor = new SegmentsBuilder(getTolerance());
                getTree(true).visit(visitor);
                List<ConnectableSegment> segments = visitor.getSegments();
                int pending = segments.size() - naturalFollowerConnections(segments);
                if (pending > 0) {
                    pending -= splitEdgeConnections(segments);
                }
                if (pending > 0) {
                    int iCloseVerticesConnections = pending - closeVerticesConnections(segments);
                }
                ArrayList<List<Segment>> loops = new ArrayList<>();
                ConnectableSegment unprocessed = getUnprocessed(segments);
                while (true) {
                    ConnectableSegment s2 = unprocessed;
                    if (s2 == null) {
                        break;
                    }
                    List<Segment> loop = followLoop(s2);
                    if (loop != null) {
                        if (loop.get(0).getStart() == null) {
                            loops.add(0, loop);
                        } else {
                            loops.add(loop);
                        }
                    }
                    unprocessed = getUnprocessed(segments);
                }
                this.vertices = new Vector2D[loops.size()];
                int i2 = 0;
                Iterator i$ = loops.iterator();
                while (i$.hasNext()) {
                    List<Segment> loop2 = i$.next();
                    if (loop2.size() < 2 || (loop2.size() == 2 && loop2.get(0).getStart() == null && loop2.get(1).getEnd() == null)) {
                        Line line = loop2.get(0).getLine();
                        int i3 = i2;
                        i2++;
                        this.vertices[i3] = new Vector2D[]{null, line.toSpace((Point<Euclidean1D>) new Vector1D(-3.4028234663852886E38d)), line.toSpace((Point<Euclidean1D>) new Vector1D(3.4028234663852886E38d))};
                    } else if (loop2.get(0).getStart() == null) {
                        Vector2D[] array = new Vector2D[loop2.size() + 2];
                        int j2 = 0;
                        for (Segment segment : loop2) {
                            if (j2 == 0) {
                                double x2 = segment.getLine().toSubSpace((Point<Euclidean2D>) segment.getEnd()).getX();
                                double x3 = x2 - FastMath.max(1.0d, FastMath.abs(x2 / 2.0d));
                                int i4 = j2;
                                int j3 = j2 + 1;
                                array[i4] = null;
                                j2 = j3 + 1;
                                array[j3] = segment.getLine().toSpace((Point<Euclidean1D>) new Vector1D(x3));
                            }
                            if (j2 < array.length - 1) {
                                int i5 = j2;
                                j2++;
                                array[i5] = segment.getEnd();
                            }
                            if (j2 == array.length - 1) {
                                double x4 = segment.getLine().toSubSpace((Point<Euclidean2D>) segment.getStart()).getX();
                                int i6 = j2;
                                j2++;
                                array[i6] = segment.getLine().toSpace((Point<Euclidean1D>) new Vector1D(x4 + FastMath.max(1.0d, FastMath.abs(x4 / 2.0d))));
                            }
                        }
                        int i7 = i2;
                        i2++;
                        this.vertices[i7] = array;
                    } else {
                        Vector2D[] array2 = new Vector2D[loop2.size()];
                        int j4 = 0;
                        Iterator i$2 = loop2.iterator();
                        while (i$2.hasNext()) {
                            int i8 = j4;
                            j4++;
                            array2[i8] = i$2.next().getStart();
                        }
                        int i9 = i2;
                        i2++;
                        this.vertices[i9] = array2;
                    }
                }
            }
        }
        return (Vector2D[][]) this.vertices.clone();
    }

    private int naturalFollowerConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null) {
                BSPTree<Euclidean2D> node = segment.getNode();
                BSPTree<Euclidean2D> end = segment.getEndNode();
                Iterator i$ = segments.iterator();
                while (true) {
                    if (i$.hasNext()) {
                        ConnectableSegment candidateNext = i$.next();
                        if (candidateNext.getPrevious() == null && candidateNext.getNode() == end && candidateNext.getStartNode() == node) {
                            segment.setNext(candidateNext);
                            candidateNext.setPrevious(segment);
                            connected++;
                            break;
                        }
                    }
                }
            }
        }
        return connected;
    }

    private int splitEdgeConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null) {
                Hyperplane<Euclidean2D> hyperplane = segment.getNode().getCut().getHyperplane();
                BSPTree<Euclidean2D> end = segment.getEndNode();
                Iterator i$ = segments.iterator();
                while (true) {
                    if (i$.hasNext()) {
                        ConnectableSegment candidateNext = i$.next();
                        if (candidateNext.getPrevious() == null && candidateNext.getNode().getCut().getHyperplane() == hyperplane && candidateNext.getStartNode() == end) {
                            segment.setNext(candidateNext);
                            candidateNext.setPrevious(segment);
                            connected++;
                            break;
                        }
                    }
                }
            }
        }
        return connected;
    }

    private int closeVerticesConnections(List<ConnectableSegment> segments) {
        int connected = 0;
        for (ConnectableSegment segment : segments) {
            if (segment.getNext() == null && segment.getEnd() != null) {
                Vector2D end = segment.getEnd();
                ConnectableSegment selectedNext = null;
                double min = Double.POSITIVE_INFINITY;
                for (ConnectableSegment candidateNext : segments) {
                    if (candidateNext.getPrevious() == null && candidateNext.getStart() != null) {
                        double distance = Vector2D.distance(end, candidateNext.getStart());
                        if (distance < min) {
                            selectedNext = candidateNext;
                            min = distance;
                        }
                    }
                }
                if (min <= getTolerance()) {
                    segment.setNext(selectedNext);
                    selectedNext.setPrevious(segment);
                    connected++;
                }
            }
        }
        return connected;
    }

    private ConnectableSegment getUnprocessed(List<ConnectableSegment> segments) {
        for (ConnectableSegment segment : segments) {
            if (!segment.isProcessed()) {
                return segment;
            }
        }
        return null;
    }

    private List<Segment> followLoop(ConnectableSegment defining) {
        ConnectableSegment next;
        List<Segment> loop = new ArrayList<>();
        loop.add(defining);
        defining.setProcessed(true);
        ConnectableSegment next2 = defining.getNext();
        while (true) {
            next = next2;
            if (next == defining || next == null) {
                break;
            }
            loop.add(next);
            next.setProcessed(true);
            next2 = next.getNext();
        }
        if (next == null) {
            ConnectableSegment previous = defining.getPrevious();
            while (true) {
                ConnectableSegment previous2 = previous;
                if (previous2 == null) {
                    break;
                }
                loop.add(0, previous2);
                previous2.setProcessed(true);
                previous = previous2.getPrevious();
            }
        }
        filterSpuriousVertices(loop);
        if (loop.size() == 2 && loop.get(0).getStart() != null) {
            return null;
        }
        return loop;
    }

    private void filterSpuriousVertices(List<Segment> loop) {
        int i2 = 0;
        while (i2 < loop.size()) {
            Segment previous = loop.get(i2);
            int j2 = (i2 + 1) % loop.size();
            Segment next = loop.get(j2);
            if (next != null && Precision.equals(previous.getLine().getAngle(), next.getLine().getAngle(), Precision.EPSILON)) {
                loop.set(j2, new Segment(previous.getStart(), next.getEnd(), previous.getLine()));
                int i3 = i2;
                i2--;
                loop.remove(i3);
            }
            i2++;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/PolygonsSet$ConnectableSegment.class */
    private static class ConnectableSegment extends Segment {
        private final BSPTree<Euclidean2D> node;
        private final BSPTree<Euclidean2D> startNode;
        private final BSPTree<Euclidean2D> endNode;
        private ConnectableSegment previous;
        private ConnectableSegment next;
        private boolean processed;

        ConnectableSegment(Vector2D start, Vector2D end, Line line, BSPTree<Euclidean2D> node, BSPTree<Euclidean2D> startNode, BSPTree<Euclidean2D> endNode) {
            super(start, end, line);
            this.node = node;
            this.startNode = startNode;
            this.endNode = endNode;
            this.previous = null;
            this.next = null;
            this.processed = false;
        }

        public BSPTree<Euclidean2D> getNode() {
            return this.node;
        }

        public BSPTree<Euclidean2D> getStartNode() {
            return this.startNode;
        }

        public BSPTree<Euclidean2D> getEndNode() {
            return this.endNode;
        }

        public ConnectableSegment getPrevious() {
            return this.previous;
        }

        public void setPrevious(ConnectableSegment previous) {
            this.previous = previous;
        }

        public ConnectableSegment getNext() {
            return this.next;
        }

        public void setNext(ConnectableSegment next) {
            this.next = next;
        }

        public void setProcessed(boolean processed) {
            this.processed = processed;
        }

        public boolean isProcessed() {
            return this.processed;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/PolygonsSet$SegmentsBuilder.class */
    private static class SegmentsBuilder implements BSPTreeVisitor<Euclidean2D> {
        private final double tolerance;
        private final List<ConnectableSegment> segments = new ArrayList();

        SegmentsBuilder(double tolerance) {
            this.tolerance = tolerance;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean2D> node) {
            return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public void visitInternalNode(BSPTree<Euclidean2D> node) {
            BoundaryAttribute<Euclidean2D> attribute = (BoundaryAttribute) node.getAttribute();
            Iterable<BSPTree<Euclidean2D>> splitters = attribute.getSplitters();
            if (attribute.getPlusOutside() != null) {
                addContribution(attribute.getPlusOutside(), node, splitters, false);
            }
            if (attribute.getPlusInside() != null) {
                addContribution(attribute.getPlusInside(), node, splitters, true);
            }
        }

        @Override // org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor
        public void visitLeafNode(BSPTree<Euclidean2D> node) {
        }

        private void addContribution(SubHyperplane<Euclidean2D> sub, BSPTree<Euclidean2D> node, Iterable<BSPTree<Euclidean2D>> splitters, boolean reversed) {
            AbstractSubHyperplane<Euclidean2D, Euclidean1D> absSub = (AbstractSubHyperplane) sub;
            Line line = (Line) sub.getHyperplane();
            List<Interval> intervals = ((IntervalsSet) absSub.getRemainingRegion()).asList();
            for (Interval i2 : intervals) {
                Vector2D startV = Double.isInfinite(i2.getInf()) ? null : line.toSpace((Point<Euclidean1D>) new Vector1D(i2.getInf()));
                Vector2D endV = Double.isInfinite(i2.getSup()) ? null : line.toSpace((Point<Euclidean1D>) new Vector1D(i2.getSup()));
                BSPTree<Euclidean2D> startN = selectClosest(startV, splitters);
                BSPTree<Euclidean2D> endN = selectClosest(endV, splitters);
                if (reversed) {
                    this.segments.add(new ConnectableSegment(endV, startV, line.getReverse(), node, endN, startN));
                } else {
                    this.segments.add(new ConnectableSegment(startV, endV, line, node, startN, endN));
                }
            }
        }

        private BSPTree<Euclidean2D> selectClosest(Vector2D point, Iterable<BSPTree<Euclidean2D>> candidates) {
            BSPTree<Euclidean2D> selected = null;
            double min = Double.POSITIVE_INFINITY;
            for (BSPTree<Euclidean2D> node : candidates) {
                double distance = FastMath.abs(node.getCut().getHyperplane().getOffset(point));
                if (distance < min) {
                    selected = node;
                    min = distance;
                }
            }
            if (min <= this.tolerance) {
                return selected;
            }
            return null;
        }

        public List<ConnectableSegment> getSegments() {
            return this.segments;
        }
    }
}
