package jdk.nashorn.internal.codegen;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.ir.CompileUnitHolder;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ReplaceCompileUnits.class */
abstract class ReplaceCompileUnits extends SimpleNodeVisitor {
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract CompileUnit getReplacement(CompileUnit compileUnit);

    ReplaceCompileUnits() {
    }

    static {
        $assertionsDisabled = !ReplaceCompileUnits.class.desiredAssertionStatus();
    }

    CompileUnit getExistingReplacement(CompileUnitHolder node) {
        CompileUnit oldUnit = node.getCompileUnit();
        if (!$assertionsDisabled && oldUnit == null) {
            throw new AssertionError();
        }
        CompileUnit newUnit = getReplacement(oldUnit);
        if ($assertionsDisabled || newUnit != null) {
            return newUnit;
        }
        throw new AssertionError();
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveFunctionNode(FunctionNode node) {
        return node.setCompileUnit(this.lc, getExistingReplacement(node));
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveLiteralNode(LiteralNode<?> node) {
        if (node instanceof LiteralNode.ArrayLiteralNode) {
            LiteralNode.ArrayLiteralNode aln = (LiteralNode.ArrayLiteralNode) node;
            if (aln.getSplitRanges() == null) {
                return node;
            }
            List<Splittable.SplitRange> newArrayUnits = new ArrayList<>();
            for (Splittable.SplitRange au2 : aln.getSplitRanges()) {
                newArrayUnits.add(new Splittable.SplitRange(getExistingReplacement(au2), au2.getLow(), au2.getHigh()));
            }
            return aln.setSplitRanges(this.lc, newArrayUnits);
        }
        return node;
    }

    @Override // jdk.nashorn.internal.ir.visitor.NodeVisitor
    public Node leaveObjectNode(ObjectNode objectNode) {
        List<Splittable.SplitRange> ranges = objectNode.getSplitRanges();
        if (ranges != null) {
            List<Splittable.SplitRange> newRanges = new ArrayList<>();
            for (Splittable.SplitRange range : ranges) {
                newRanges.add(new Splittable.SplitRange(getExistingReplacement(range), range.getLow(), range.getHigh()));
            }
            return objectNode.setSplitRanges(this.lc, newRanges);
        }
        return super.leaveObjectNode(objectNode);
    }
}
