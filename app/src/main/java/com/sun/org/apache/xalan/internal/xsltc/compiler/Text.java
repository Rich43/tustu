package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Text.class */
final class Text extends Instruction {
    private String _text;
    private boolean _escaping;
    private boolean _ignore;
    private boolean _textElement;

    public Text() {
        this._escaping = true;
        this._ignore = false;
        this._textElement = false;
        this._textElement = true;
    }

    public Text(String text) {
        this._escaping = true;
        this._ignore = false;
        this._textElement = false;
        this._text = text;
    }

    protected String getText() {
        return this._text;
    }

    protected void setText(String text) {
        if (this._text == null) {
            this._text = text;
        } else {
            this._text += text;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Text");
        indent(indent + 4);
        Util.println(this._text);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String str = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DISABLE_OUTPUT_ESCAPING);
        if (str != null && str.equals("yes")) {
            this._escaping = false;
        }
        parseChildren(parser);
        if (this._text == null) {
            if (this._textElement) {
                this._text = "";
                return;
            } else {
                this._ignore = true;
                return;
            }
        }
        if (this._textElement) {
            if (this._text.length() == 0) {
                this._ignore = true;
                return;
            }
            return;
        }
        if (getParent() instanceof LiteralElement) {
            LiteralElement element = (LiteralElement) getParent();
            String space = element.getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_XMLSPACE);
            if (space == null || !space.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                int textLength = this._text.length();
                int i2 = 0;
                while (i2 < textLength) {
                    char c2 = this._text.charAt(i2);
                    if (!isWhitespace(c2)) {
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i2 == textLength) {
                    this._ignore = true;
                    return;
                }
                return;
            }
            return;
        }
        int textLength2 = this._text.length();
        int i3 = 0;
        while (i3 < textLength2) {
            char c3 = this._text.charAt(i3);
            if (!isWhitespace(c3)) {
                break;
            } else {
                i3++;
            }
        }
        if (i3 == textLength2) {
            this._ignore = true;
        }
    }

    public void ignore() {
        this._ignore = true;
    }

    public boolean isIgnore() {
        return this._ignore;
    }

    public boolean isTextElement() {
        return this._textElement;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    protected boolean contextDependent() {
        return false;
    }

    private static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\t' || c2 == '\n' || c2 == '\r';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (!this._ignore) {
            int esc = cpg.addInterfaceMethodref(Constants.OUTPUT_HANDLER, "setEscaping", "(Z)Z");
            if (!this._escaping) {
                il.append(methodGen.loadHandler());
                il.append(new PUSH(cpg, false));
                il.append(new INVOKEINTERFACE(esc, 2));
            }
            il.append(methodGen.loadHandler());
            if (!canLoadAsArrayOffsetLength()) {
                int characters = cpg.addInterfaceMethodref(Constants.OUTPUT_HANDLER, "characters", "(Ljava/lang/String;)V");
                il.append(new PUSH(cpg, this._text));
                il.append(new INVOKEINTERFACE(characters, 2));
            } else {
                int characters2 = cpg.addInterfaceMethodref(Constants.OUTPUT_HANDLER, "characters", "([CII)V");
                loadAsArrayOffsetLength(classGen, methodGen);
                il.append(new INVOKEINTERFACE(characters2, 4));
            }
            if (!this._escaping) {
                il.append(methodGen.loadHandler());
                il.append(SWAP);
                il.append(new INVOKEINTERFACE(esc, 2));
                il.append(POP);
            }
        }
        translateContents(classGen, methodGen);
    }

    public boolean canLoadAsArrayOffsetLength() {
        return this._text.length() <= 21845;
    }

    public void loadAsArrayOffsetLength(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        XSLTC xsltc = classGen.getParser().getXSLTC();
        int offset = xsltc.addCharacterData(this._text);
        this._text.length();
        String charDataFieldName = Constants.STATIC_CHAR_DATA_FIELD + (xsltc.getCharacterDataCount() - 1);
        il.append(new GETSTATIC(cpg.addFieldref(xsltc.getClassName(), charDataFieldName, Constants.STATIC_CHAR_DATA_FIELD_SIG)));
        il.append(new PUSH(cpg, offset));
        il.append(new PUSH(cpg, this._text.length()));
    }
}
