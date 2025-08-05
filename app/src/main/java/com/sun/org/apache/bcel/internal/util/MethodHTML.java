package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/MethodHTML.class */
final class MethodHTML implements Constants {
    private String class_name;
    private PrintWriter file;
    private ConstantHTML constant_html;
    private AttributeHTML attribute_html;

    MethodHTML(String dir, String class_name, Method[] methods, Field[] fields, ConstantHTML constant_html, AttributeHTML attribute_html) throws IOException, ClassFormatException {
        this.class_name = class_name;
        this.attribute_html = attribute_html;
        this.constant_html = constant_html;
        this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_methods.html"));
        this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
        this.file.println("<TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Type</TH><TH ALIGN=LEFT>Field&nbsp;name</TH></TR>");
        for (Field field : fields) {
            writeField(field);
        }
        this.file.println("</TABLE>");
        this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Return&nbsp;type</TH><TH ALIGN=LEFT>Method&nbsp;name</TH><TH ALIGN=LEFT>Arguments</TH></TR>");
        for (int i2 = 0; i2 < methods.length; i2++) {
            writeMethod(methods[i2], i2);
        }
        this.file.println("</TABLE></BODY></HTML>");
        this.file.close();
    }

    private void writeField(Field field) throws IOException, ClassFormatException {
        String type = Utility.signatureToString(field.getSignature());
        String name = field.getName();
        String access = Utility.accessToString(field.getAccessFlags());
        this.file.print("<TR><TD><FONT COLOR=\"#FF0000\">" + Utility.replace(access, " ", "&nbsp;") + "</FONT></TD>\n<TD>" + Class2HTML.referenceType(type) + "</TD><TD><A NAME=\"field" + name + "\">" + name + "</A></TD>");
        Attribute[] attributes = field.getAttributes();
        for (int i2 = 0; i2 < attributes.length; i2++) {
            this.attribute_html.writeAttribute(attributes[i2], name + "@" + i2);
        }
        int i3 = 0;
        while (true) {
            if (i3 >= attributes.length) {
                break;
            }
            if (attributes[i3].getTag() != 1) {
                i3++;
            } else {
                String str = ((ConstantValue) attributes[i3]).toString();
                this.file.print("<TD>= <A HREF=\"" + this.class_name + "_attributes.html#" + name + "@" + i3 + "\" TARGET=\"Attributes\">" + str + "</TD>\n");
                break;
            }
        }
        this.file.println("</TR>");
    }

    private final void writeMethod(Method method, int method_number) throws IOException, ClassFormatException {
        String signature = method.getSignature();
        String[] args = Utility.methodSignatureArgumentTypes(signature, false);
        String type = Utility.methodSignatureReturnType(signature, false);
        String name = method.getName();
        String access = Utility.accessToString(method.getAccessFlags());
        Attribute[] attributes = method.getAttributes();
        String access2 = Utility.replace(access, " ", "&nbsp;");
        String html_name = Class2HTML.toHTML(name);
        this.file.print("<TR VALIGN=TOP><TD><FONT COLOR=\"#FF0000\"><A NAME=method" + method_number + ">" + access2 + "</A></FONT></TD>");
        this.file.print("<TD>" + Class2HTML.referenceType(type) + "</TD><TD><A HREF=" + this.class_name + "_code.html#method" + method_number + " TARGET=Code>" + html_name + "</A></TD>\n<TD>(");
        for (int i2 = 0; i2 < args.length; i2++) {
            this.file.print(Class2HTML.referenceType(args[i2]));
            if (i2 < args.length - 1) {
                this.file.print(", ");
            }
        }
        this.file.print(")</TD></TR>");
        for (int i3 = 0; i3 < attributes.length; i3++) {
            this.attribute_html.writeAttribute(attributes[i3], "method" + method_number + "@" + i3, method_number);
            byte tag = attributes[i3].getTag();
            if (tag == 3) {
                this.file.print("<TR VALIGN=TOP><TD COLSPAN=2></TD><TH ALIGN=LEFT>throws</TH><TD>");
                int[] exceptions = ((ExceptionTable) attributes[i3]).getExceptionIndexTable();
                for (int j2 = 0; j2 < exceptions.length; j2++) {
                    this.file.print(this.constant_html.referenceConstant(exceptions[j2]));
                    if (j2 < exceptions.length - 1) {
                        this.file.print(", ");
                    }
                }
                this.file.println("</TD></TR>");
            } else if (tag == 2) {
                Attribute[] c_a = ((Code) attributes[i3]).getAttributes();
                for (int j3 = 0; j3 < c_a.length; j3++) {
                    this.attribute_html.writeAttribute(c_a[j3], "method" + method_number + "@" + i3 + "@" + j3, method_number);
                }
            }
        }
    }
}
