package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AttributeValueTemplate.class */
final class AttributeValueTemplate extends AttributeValue {
    static final int OUT_EXPR = 0;
    static final int IN_EXPR = 1;
    static final int IN_EXPR_SQUOTES = 2;
    static final int IN_EXPR_DQUOTES = 3;
    static final String DELIMITER = "\ufffe";

    public AttributeValueTemplate(String value, Parser parser, SyntaxTreeNode parent) {
        setParent(parent);
        setParser(parser);
        try {
            parseAVTemplate(value, parser);
        } catch (NoSuchElementException e2) {
            reportError(parent, parser, ErrorMsg.ATTR_VAL_TEMPLATE_ERR, value);
        }
    }

    private void parseAVTemplate(String text, Parser parser) {
        String t2;
        StringTokenizer tokenizer = new StringTokenizer(text, "{}\"'", true);
        String lookahead = null;
        StringBuffer buffer = new StringBuffer();
        int state = 0;
        while (tokenizer.hasMoreTokens()) {
            if (lookahead != null) {
                t2 = lookahead;
                lookahead = null;
            } else {
                t2 = tokenizer.nextToken();
            }
            if (t2.length() == 1) {
                switch (t2.charAt(0)) {
                    case '\"':
                        switch (state) {
                            case 1:
                                state = 3;
                                break;
                            case 3:
                                state = 1;
                                break;
                        }
                        buffer.append(t2);
                        break;
                    case '\'':
                        switch (state) {
                            case 1:
                                state = 2;
                                break;
                            case 2:
                                state = 1;
                                break;
                        }
                        buffer.append(t2);
                        break;
                    case '{':
                        switch (state) {
                            case 0:
                                lookahead = tokenizer.nextToken();
                                if (lookahead.equals(VectorFormat.DEFAULT_PREFIX)) {
                                    buffer.append(lookahead);
                                    lookahead = null;
                                    break;
                                } else {
                                    buffer.append(DELIMITER);
                                    state = 1;
                                    break;
                                }
                            case 1:
                            case 2:
                            case 3:
                                reportError(getParent(), parser, ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
                                break;
                        }
                    case '}':
                        switch (state) {
                            case 0:
                                lookahead = tokenizer.nextToken();
                                if (lookahead.equals("}")) {
                                    buffer.append(lookahead);
                                    lookahead = null;
                                    break;
                                } else {
                                    reportError(getParent(), parser, ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
                                    break;
                                }
                            case 1:
                                buffer.append(DELIMITER);
                                state = 0;
                                break;
                            case 2:
                            case 3:
                                buffer.append(t2);
                                break;
                        }
                    default:
                        buffer.append(t2);
                        break;
                }
            } else {
                buffer.append(t2);
            }
        }
        if (state != 0) {
            reportError(getParent(), parser, ErrorMsg.ATTR_VAL_TEMPLATE_ERR, text);
        }
        StringTokenizer tokenizer2 = new StringTokenizer(buffer.toString(), DELIMITER, true);
        while (tokenizer2.hasMoreTokens()) {
            String t3 = tokenizer2.nextToken();
            if (t3.equals(DELIMITER)) {
                addElement(parser.parseExpression(this, tokenizer2.nextToken()));
                tokenizer2.nextToken();
            } else {
                addElement(new LiteralExpr(t3));
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        List<SyntaxTreeNode> contents = getContents();
        int n2 = contents.size();
        for (int i2 = 0; i2 < n2; i2++) {
            Expression exp = (Expression) contents.get(i2);
            if (!exp.typeCheck(stable).identicalTo(Type.String)) {
                contents.set(i2, new CastExpr(exp, Type.String));
            }
        }
        Type type = Type.String;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        StringBuffer buffer = new StringBuffer("AVT:[");
        int count = elementCount();
        for (int i2 = 0; i2 < count; i2++) {
            buffer.append(elementAt(i2).toString());
            if (i2 < count - 1) {
                buffer.append(' ');
            }
        }
        return buffer.append(']').toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (elementCount() == 1) {
            Expression exp = (Expression) elementAt(0);
            exp.translate(classGen, methodGen);
            return;
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int initBuffer = cpg.addMethodref(Constants.STRING_BUFFER_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V");
        com.sun.org.apache.bcel.internal.generic.Instruction append = new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_BUFFER_CLASS, "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
        int toString = cpg.addMethodref(Constants.STRING_BUFFER_CLASS, "toString", "()Ljava/lang/String;");
        il.append(new NEW(cpg.addClass(Constants.STRING_BUFFER_CLASS)));
        il.append(DUP);
        il.append(new INVOKESPECIAL(initBuffer));
        Iterator<SyntaxTreeNode> elements = elements();
        while (elements.hasNext()) {
            Expression exp2 = (Expression) elements.next();
            exp2.translate(classGen, methodGen);
            il.append(append);
        }
        il.append(new INVOKEVIRTUAL(toString));
    }
}
