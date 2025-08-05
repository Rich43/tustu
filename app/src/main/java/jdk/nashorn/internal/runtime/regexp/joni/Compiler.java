package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/Compiler.class */
abstract class Compiler implements ErrorMessages {
    protected final Analyser analyser;
    protected final Regex regex;

    protected abstract void prepare();

    protected abstract void finish();

    protected abstract void compileAltNode(ConsAltNode consAltNode);

    protected abstract void addCompileString(char[] cArr, int i2, int i3, boolean z2);

    protected abstract void compileCClassNode(CClassNode cClassNode);

    protected abstract void compileAnyCharNode();

    protected abstract void compileBackrefNode(BackRefNode backRefNode);

    protected abstract void compileNonCECQuantifierNode(QuantifierNode quantifierNode);

    protected abstract void compileOptionNode(EncloseNode encloseNode);

    protected abstract void compileEncloseNode(EncloseNode encloseNode);

    protected abstract void compileAnchorNode(AnchorNode anchorNode);

    protected Compiler(Analyser analyser) {
        this.analyser = analyser;
        this.regex = analyser.regex;
    }

    final void compile() {
        prepare();
        compileTree(this.analyser.root);
        finish();
    }

    private void compileStringRawNode(StringNode sn) {
        if (sn.length() <= 0) {
            return;
        }
        addCompileString(sn.chars, sn.f12885p, sn.length(), false);
    }

    private void compileStringNode(StringNode node) {
        if (node.length() <= 0) {
            return;
        }
        boolean ambig = node.isAmbig();
        int p2 = node.f12885p;
        int end = node.end;
        char[] chars = node.chars;
        int slen = 1;
        for (int p3 = p2 + 1; p3 < end; p3++) {
            slen++;
        }
        addCompileString(chars, p2, slen, ambig);
    }

    protected final void compileTree(Node node) {
        ConsAltNode consAltNode;
        switch (node.getType()) {
            case 0:
                StringNode sn = (StringNode) node;
                if (sn.isRaw()) {
                    compileStringRawNode(sn);
                    break;
                } else {
                    compileStringNode(sn);
                    break;
                }
            case 1:
                compileCClassNode((CClassNode) node);
                break;
            case 2:
            default:
                newInternalException(ErrorMessages.ERR_PARSER_BUG);
                break;
            case 3:
                compileAnyCharNode();
                break;
            case 4:
                compileBackrefNode((BackRefNode) node);
                break;
            case 5:
                compileNonCECQuantifierNode((QuantifierNode) node);
                break;
            case 6:
                EncloseNode enode = (EncloseNode) node;
                if (enode.isOption()) {
                    compileOptionNode(enode);
                    break;
                } else {
                    compileEncloseNode(enode);
                    break;
                }
            case 7:
                compileAnchorNode((AnchorNode) node);
                break;
            case 8:
                ConsAltNode lin = (ConsAltNode) node;
                do {
                    compileTree(lin.car);
                    consAltNode = lin.cdr;
                    lin = consAltNode;
                } while (consAltNode != null);
            case 9:
                compileAltNode((ConsAltNode) node);
                break;
        }
    }

    protected final void compileTreeNTimes(Node node, int n2) {
        for (int i2 = 0; i2 < n2; i2++) {
            compileTree(node);
        }
    }

    protected void newSyntaxException(String message) {
        throw new SyntaxException(message);
    }

    protected void newInternalException(String message) {
        throw new InternalException(message);
    }
}
