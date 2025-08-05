package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ConstantHTML.class */
final class ConstantHTML implements Constants {
    private String class_name;
    private String class_package;
    private ConstantPool constant_pool;
    private PrintWriter file;
    private String[] constant_ref;
    private Constant[] constants;
    private Method[] methods;

    ConstantHTML(String dir, String class_name, String class_package, Method[] methods, ConstantPool constant_pool) throws IOException, ClassFormatException {
        this.class_name = class_name;
        this.class_package = class_package;
        this.constant_pool = constant_pool;
        this.methods = methods;
        this.constants = constant_pool.getConstantPool();
        this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_cp.html"));
        this.constant_ref = new String[this.constants.length];
        this.constant_ref[0] = "&lt;unknown&gt;";
        this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
        for (int i2 = 1; i2 < this.constants.length; i2++) {
            if (i2 % 2 == 0) {
                this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
            } else {
                this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
            }
            if (this.constants[i2] != null) {
                writeConstant(i2);
            }
            this.file.print("</TD></TR>\n");
        }
        this.file.println("</TABLE></BODY></HTML>");
        this.file.close();
    }

    String referenceConstant(int index) {
        return this.constant_ref[index];
    }

    private void writeConstant(int index) throws ClassFormatException {
        String ref;
        int class_index;
        int name_index;
        String ref2;
        byte tag = this.constants[index].getTag();
        this.file.println("<H4> <A NAME=cp" + index + ">" + index + "</A> " + CONSTANT_NAMES[tag] + "</H4>");
        switch (tag) {
            case 7:
                ConstantClass c4 = (ConstantClass) this.constant_pool.getConstant(index, (byte) 7);
                int name_index2 = c4.getNameIndex();
                String class_name2 = this.constant_pool.constantToString(index, tag);
                String short_class_name = Utility.compactClassName(Utility.compactClassName(class_name2), this.class_package + ".", true);
                String ref3 = "<A HREF=\"" + class_name2 + ".html\" TARGET=_top>" + short_class_name + "</A>";
                this.constant_ref[index] = "<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + short_class_name + "</A>";
                this.file.println("<P><TT>" + ref3 + "</TT><UL><LI><A HREF=\"#cp" + name_index2 + "\">Name index(" + name_index2 + ")</A></UL>\n");
                break;
            case 8:
                ConstantString c5 = (ConstantString) this.constant_pool.getConstant(index, (byte) 8);
                int name_index3 = c5.getStringIndex();
                String str = Class2HTML.toHTML(this.constant_pool.constantToString(index, tag));
                this.file.println("<P><TT>" + str + "</TT><UL><LI><A HREF=\"#cp" + name_index3 + "\">Name index(" + name_index3 + ")</A></UL>\n");
                break;
            case 9:
                ConstantFieldref c3 = (ConstantFieldref) this.constant_pool.getConstant(index, (byte) 9);
                int class_index2 = c3.getClassIndex();
                int name_index4 = c3.getNameAndTypeIndex();
                String field_class = this.constant_pool.constantToString(class_index2, (byte) 7);
                String short_field_class = Utility.compactClassName(Utility.compactClassName(field_class), this.class_package + ".", true);
                String field_name = this.constant_pool.constantToString(name_index4, (byte) 12);
                if (field_class.equals(this.class_name)) {
                    ref = "<A HREF=\"" + field_class + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>";
                } else {
                    ref = "<A HREF=\"" + field_class + ".html\" TARGET=_top>" + short_field_class + "</A>." + field_name + "\n";
                }
                this.constant_ref[index] = "<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index2 + "\" TARGET=Constants>" + short_field_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + field_name + "</A>";
                this.file.println("<P><TT>" + ref + "</TT><BR>\n<UL><LI><A HREF=\"#cp" + class_index2 + "\">Class(" + class_index2 + ")</A><BR>\n<LI><A HREF=\"#cp" + name_index4 + "\">NameAndType(" + name_index4 + ")</A></UL>");
                break;
            case 10:
            case 11:
                if (tag == 10) {
                    ConstantMethodref c2 = (ConstantMethodref) this.constant_pool.getConstant(index, (byte) 10);
                    class_index = c2.getClassIndex();
                    name_index = c2.getNameAndTypeIndex();
                } else {
                    ConstantInterfaceMethodref c1 = (ConstantInterfaceMethodref) this.constant_pool.getConstant(index, (byte) 11);
                    class_index = c1.getClassIndex();
                    name_index = c1.getNameAndTypeIndex();
                }
                String method_name = this.constant_pool.constantToString(name_index, (byte) 12);
                String html_method_name = Class2HTML.toHTML(method_name);
                String method_class = this.constant_pool.constantToString(class_index, (byte) 7);
                Utility.compactClassName(method_class);
                String short_method_class = Utility.compactClassName(Utility.compactClassName(method_class), this.class_package + ".", true);
                ConstantNameAndType c22 = (ConstantNameAndType) this.constant_pool.getConstant(name_index, (byte) 12);
                String signature = this.constant_pool.constantToString(c22.getSignatureIndex(), (byte) 1);
                String[] args = Utility.methodSignatureArgumentTypes(signature, false);
                String type = Utility.methodSignatureReturnType(signature, false);
                String ret_type = Class2HTML.referenceType(type);
                StringBuffer buf = new StringBuffer("(");
                for (int i2 = 0; i2 < args.length; i2++) {
                    buf.append(Class2HTML.referenceType(args[i2]));
                    if (i2 < args.length - 1) {
                        buf.append(",&nbsp;");
                    }
                }
                buf.append(")");
                String arg_types = buf.toString();
                if (method_class.equals(this.class_name)) {
                    ref2 = "<A HREF=\"" + this.class_name + "_code.html#method" + getMethodNumber(method_name + signature) + "\" TARGET=Code>" + html_method_name + "</A>";
                } else {
                    ref2 = "<A HREF=\"" + method_class + ".html\" TARGET=_top>" + short_method_class + "</A>." + html_method_name;
                }
                this.constant_ref[index] = ret_type + "&nbsp;<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index + "\" TARGET=Constants>" + short_method_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + html_method_name + "</A>&nbsp;" + arg_types;
                this.file.println("<P><TT>" + ret_type + "&nbsp;" + ref2 + arg_types + "&nbsp;</TT>\n<UL><LI><A HREF=\"#cp" + class_index + "\">Class index(" + class_index + ")</A>\n<LI><A HREF=\"#cp" + name_index + "\">NameAndType index(" + name_index + ")</A></UL>");
                break;
            case 12:
                ConstantNameAndType c6 = (ConstantNameAndType) this.constant_pool.getConstant(index, (byte) 12);
                int name_index5 = c6.getNameIndex();
                int signature_index = c6.getSignatureIndex();
                this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT><UL><LI><A HREF=\"#cp" + name_index5 + "\">Name index(" + name_index5 + ")</A>\n<LI><A HREF=\"#cp" + signature_index + "\">Signature index(" + signature_index + ")</A></UL>\n");
                break;
            default:
                this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT>\n");
                break;
        }
    }

    private final int getMethodNumber(String str) {
        for (int i2 = 0; i2 < this.methods.length; i2++) {
            String cmp = this.methods[i2].getName() + this.methods[i2].getSignature();
            if (cmp.equals(str)) {
                return i2;
            }
        }
        return -1;
    }
}
