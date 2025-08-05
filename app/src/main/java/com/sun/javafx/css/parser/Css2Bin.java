package com.sun.javafx.css.parser;

import com.sun.javafx.css.Stylesheet;
import java.io.File;
import java.io.IOException;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/Css2Bin.class */
public final class Css2Bin {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("expected file name as argument");
        }
        try {
            String ifname = args[0];
            String ofname = args.length > 1 ? args[1] : ifname.substring(0, ifname.lastIndexOf(46) + 1).concat("bss");
            convertToBinary(ifname, ofname);
        } catch (Exception e2) {
            System.err.println(e2.toString());
            e2.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public static void convertToBinary(String ifname, String ofname) throws IOException {
        if (ifname == null || ofname == null) {
            throw new IllegalArgumentException("parameters cannot be null");
        }
        if (ifname.equals(ofname)) {
            throw new IllegalArgumentException("input file and output file cannot be the same");
        }
        File source = new File(ifname);
        File destination = new File(ofname);
        Stylesheet.convertToBinary(source, destination);
    }
}
