package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.CodeException;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
import com.sun.org.apache.bcel.internal.classfile.InnerClass;
import com.sun.org.apache.bcel.internal.classfile.InnerClasses;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/AttributeHTML.class */
final class AttributeHTML implements Constants {
    private String class_name;
    private PrintWriter file;
    private int attr_count = 0;
    private ConstantHTML constant_html;
    private ConstantPool constant_pool;

    AttributeHTML(String dir, String class_name, ConstantPool constant_pool, ConstantHTML constant_html) throws IOException {
        this.class_name = class_name;
        this.constant_pool = constant_pool;
        this.constant_html = constant_html;
        this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_attributes.html"));
        this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
    }

    private final String codeLink(int link, int method_number) {
        return "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + link + "\" TARGET=Code>" + link + "</A>";
    }

    final void close() {
        this.file.println("</TABLE></BODY></HTML>");
        this.file.close();
    }

    final void writeAttribute(Attribute attribute, String anchor) throws IOException {
        writeAttribute(attribute, anchor, 0);
    }

    final void writeAttribute(Attribute attribute, String anchor, int method_number) throws IOException {
        String bytes;
        byte tag = attribute.getTag();
        if (tag == -1) {
            return;
        }
        this.attr_count++;
        if (this.attr_count % 2 == 0) {
            this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
        } else {
            this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
        }
        this.file.println("<H4><A NAME=\"" + anchor + "\">" + this.attr_count + " " + ATTRIBUTE_NAMES[tag] + "</A></H4>");
        switch (tag) {
            case 0:
                int index = ((SourceFile) attribute).getSourceFileIndex();
                this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">Source file index(" + index + ")</A></UL>\n");
                break;
            case 1:
                int index2 = ((ConstantValue) attribute).getConstantValueIndex();
                this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index2 + "\" TARGET=\"ConstantPool\">Constant value index(" + index2 + ")</A></UL>\n");
                break;
            case 2:
                Code c2 = (Code) attribute;
                this.file.print("<UL><LI>Maximum stack size = " + c2.getMaxStack() + "</LI>\n<LI>Number of local variables = " + c2.getMaxLocals() + "</LI>\n<LI><A HREF=\"" + this.class_name + "_code.html#method" + method_number + "\" TARGET=Code>Byte code</A></LI></UL>\n");
                CodeException[] ce = c2.getExceptionTable();
                int len = ce.length;
                if (len > 0) {
                    this.file.print("<P><B>Exceptions handled</B><UL>");
                    for (int i2 = 0; i2 < len; i2++) {
                        int catch_type = ce[i2].getCatchType();
                        this.file.print("<LI>");
                        if (catch_type != 0) {
                            this.file.print(this.constant_html.referenceConstant(catch_type));
                        } else {
                            this.file.print("Any Exception");
                        }
                        this.file.print("<BR>(Ranging from lines " + codeLink(ce[i2].getStartPC(), method_number) + " to " + codeLink(ce[i2].getEndPC(), method_number) + ", handled at line " + codeLink(ce[i2].getHandlerPC(), method_number) + ")</LI>");
                    }
                    this.file.print("</UL>");
                    break;
                }
                break;
            case 3:
                int[] indices = ((ExceptionTable) attribute).getExceptionIndexTable();
                this.file.print("<UL>");
                for (int i3 = 0; i3 < indices.length; i3++) {
                    this.file.print("<LI><A HREF=\"" + this.class_name + "_cp.html#cp" + indices[i3] + "\" TARGET=\"ConstantPool\">Exception class index(" + indices[i3] + ")</A>\n");
                }
                this.file.print("</UL>\n");
                break;
            case 4:
                LineNumber[] line_numbers = ((LineNumberTable) attribute).getLineNumberTable();
                this.file.print("<P>");
                for (int i4 = 0; i4 < line_numbers.length; i4++) {
                    this.file.print("(" + line_numbers[i4].getStartPC() + ",&nbsp;" + line_numbers[i4].getLineNumber() + ")");
                    if (i4 < line_numbers.length - 1) {
                        this.file.print(", ");
                    }
                }
                break;
            case 5:
                LocalVariable[] vars = ((LocalVariableTable) attribute).getLocalVariableTable();
                this.file.print("<UL>");
                for (int i5 = 0; i5 < vars.length; i5++) {
                    String signature = ((ConstantUtf8) this.constant_pool.getConstant(vars[i5].getSignatureIndex(), (byte) 1)).getBytes();
                    String signature2 = Utility.signatureToString(signature, false);
                    int start = vars[i5].getStartPC();
                    int end = start + vars[i5].getLength();
                    this.file.println("<LI>" + Class2HTML.referenceType(signature2) + "&nbsp;<B>" + vars[i5].getName() + "</B> in slot %" + vars[i5].getIndex() + "<BR>Valid from lines <A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + start + "\" TARGET=Code>" + start + "</A> to <A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + end + "\" TARGET=Code>" + end + "</A></LI>");
                }
                this.file.print("</UL>\n");
                break;
            case 6:
                InnerClass[] classes = ((InnerClasses) attribute).getInnerClasses();
                this.file.print("<UL>");
                for (int i6 = 0; i6 < classes.length; i6++) {
                    int index3 = classes[i6].getInnerNameIndex();
                    if (index3 > 0) {
                        bytes = ((ConstantUtf8) this.constant_pool.getConstant(index3, (byte) 1)).getBytes();
                    } else {
                        bytes = "&lt;anonymous&gt;";
                    }
                    String name = bytes;
                    String access = Utility.accessToString(classes[i6].getInnerAccessFlags());
                    this.file.print("<LI><FONT COLOR=\"#FF0000\">" + access + "</FONT> " + this.constant_html.referenceConstant(classes[i6].getInnerClassIndex()) + " in&nbsp;class " + this.constant_html.referenceConstant(classes[i6].getOuterClassIndex()) + " named " + name + "</LI>\n");
                }
                this.file.print("</UL>\n");
                break;
            default:
                this.file.print("<P>" + attribute.toString());
                break;
        }
        this.file.println("</TD></TR>");
        this.file.flush();
    }
}
