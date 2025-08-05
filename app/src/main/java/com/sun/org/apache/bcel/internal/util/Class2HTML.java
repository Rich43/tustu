package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/Class2HTML.class */
public class Class2HTML implements Constants {
    private JavaClass java_class;
    private String dir;
    private static String class_package;
    private static String class_name;
    private static ConstantPool constant_pool;

    public Class2HTML(JavaClass java_class, String dir) throws IOException {
        Method[] methods = java_class.getMethods();
        this.java_class = java_class;
        this.dir = dir;
        class_name = java_class.getClassName();
        constant_pool = java_class.getConstantPool();
        int index = class_name.lastIndexOf(46);
        if (index > -1) {
            class_package = class_name.substring(0, index);
        } else {
            class_package = "";
        }
        ConstantHTML constant_html = new ConstantHTML(dir, class_name, class_package, methods, constant_pool);
        AttributeHTML attribute_html = new AttributeHTML(dir, class_name, constant_pool, constant_html);
        new MethodHTML(dir, class_name, methods, java_class.getFields(), constant_html, attribute_html);
        writeMainHTML(attribute_html);
        new CodeHTML(dir, class_name, methods, constant_pool, constant_html);
        attribute_html.close();
    }

    public static void _main(String[] argv) {
        ClassParser parser;
        String[] file_name = new String[argv.length];
        int files = 0;
        String zip_file = null;
        char sep = SecuritySupport.getSystemProperty("file.separator").toCharArray()[0];
        String dir = "." + sep;
        int i2 = 0;
        while (i2 < argv.length) {
            try {
                if (argv[i2].charAt(0) == '-') {
                    if (argv[i2].equals("-d")) {
                        i2++;
                        dir = argv[i2];
                        if (!dir.endsWith("" + sep)) {
                            dir = dir + sep;
                        }
                        new File(dir).mkdirs();
                    } else if (argv[i2].equals("-zip")) {
                        i2++;
                        zip_file = argv[i2];
                    } else {
                        System.out.println("Unknown option " + argv[i2]);
                    }
                } else {
                    int i3 = files;
                    files++;
                    file_name[i3] = argv[i2];
                }
                i2++;
            } catch (Exception e2) {
                System.out.println(e2);
                e2.printStackTrace(System.out);
                return;
            }
        }
        if (files == 0) {
            System.err.println("Class2HTML: No input files specified.");
        } else {
            for (int i4 = 0; i4 < files; i4++) {
                System.out.print("Processing " + file_name[i4] + "...");
                if (zip_file == null) {
                    parser = new ClassParser(file_name[i4]);
                } else {
                    parser = new ClassParser(zip_file, file_name[i4]);
                }
                JavaClass java_class = parser.parse();
                new Class2HTML(java_class, dir);
                System.out.println("Done.");
            }
        }
    }

    static String referenceClass(int index) {
        String str = constant_pool.getConstantString(index, (byte) 7);
        return "<A HREF=\"" + class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + Utility.compactClassName(Utility.compactClassName(str), class_package + ".", true) + "</A>";
    }

    static final String referenceType(String type) {
        String short_type = Utility.compactClassName(type);
        String short_type2 = Utility.compactClassName(short_type, class_package + ".", true);
        int index = type.indexOf(91);
        if (index > -1) {
            type = type.substring(0, index);
        }
        if (type.equals("int") || type.equals(SchemaSymbols.ATTVAL_SHORT) || type.equals("boolean") || type.equals("void") || type.equals("char") || type.equals(SchemaSymbols.ATTVAL_BYTE) || type.equals(SchemaSymbols.ATTVAL_LONG) || type.equals(SchemaSymbols.ATTVAL_DOUBLE) || type.equals(SchemaSymbols.ATTVAL_FLOAT)) {
            return "<FONT COLOR=\"#00FF00\">" + type + "</FONT>";
        }
        return "<A HREF=\"" + type + ".html\" TARGET=_top>" + short_type2 + "</A>";
    }

    static String toHTML(String str) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < str.length(); i2++) {
            try {
                char ch = str.charAt(i2);
                switch (ch) {
                    case '\n':
                        buf.append("\\n");
                        break;
                    case '\r':
                        buf.append("\\r");
                        break;
                    case '<':
                        buf.append(SerializerConstants.ENTITY_LT);
                        break;
                    case '>':
                        buf.append(SerializerConstants.ENTITY_GT);
                        break;
                    default:
                        buf.append(ch);
                        break;
                }
            } catch (StringIndexOutOfBoundsException e2) {
            }
        }
        return buf.toString();
    }

    private void writeMainHTML(AttributeHTML attribute_html) throws IOException {
        PrintWriter file = new PrintWriter(new FileOutputStream(this.dir + class_name + ".html"));
        Attribute[] attributes = this.java_class.getAttributes();
        file.println("<HTML>\n<HEAD><TITLE>Documentation for " + class_name + "</TITLE></HEAD>\n<FRAMESET BORDER=1 cols=\"30%,*\">\n<FRAMESET BORDER=1 rows=\"80%,*\">\n<FRAME NAME=\"ConstantPool\" SRC=\"" + class_name + "_cp.html\"\n MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n<FRAME NAME=\"Attributes\" SRC=\"" + class_name + "_attributes.html\"\n MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n</FRAMESET>\n<FRAMESET BORDER=1 rows=\"80%,*\">\n<FRAME NAME=\"Code\" SRC=\"" + class_name + "_code.html\"\n MARGINWIDTH=0 MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n<FRAME NAME=\"Methods\" SRC=\"" + class_name + "_methods.html\"\n MARGINWIDTH=0 MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n</FRAMESET></FRAMESET></HTML>");
        file.close();
        for (int i2 = 0; i2 < attributes.length; i2++) {
            attribute_html.writeAttribute(attributes[i2], com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_CLASS + i2);
        }
    }
}
