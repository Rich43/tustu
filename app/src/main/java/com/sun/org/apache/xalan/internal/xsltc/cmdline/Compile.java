package com.sun.org.apache.xalan.internal.xsltc.cmdline;

import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOptsException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import jdk.xml.internal.JdkXmlFeatures;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/cmdline/Compile.class */
public final class Compile {
    private static int VERSION_MAJOR = 1;
    private static int VERSION_MINOR = 4;
    private static int VERSION_DELTA = 0;
    private static boolean _allowExit = true;

    public static void printUsage() {
        System.err.println("XSLTC version " + VERSION_MAJOR + "." + VERSION_MINOR + (VERSION_DELTA > 0 ? "." + VERSION_DELTA : "") + "\n" + ((Object) new ErrorMsg(ErrorMsg.COMPILE_USAGE_STR)));
        if (_allowExit) {
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        boolean compileOK;
        URL url;
        try {
            boolean inputIsURL = false;
            boolean useStdIn = false;
            boolean classNameSet = false;
            GetOpt getopt = new GetOpt(args, "o:d:j:p:uxhsinv");
            if (args.length < 1) {
                printUsage();
            }
            XSLTC xsltc = new XSLTC(new JdkXmlFeatures(false));
            xsltc.init();
            while (true) {
                int c2 = getopt.getNextOption();
                if (c2 != -1) {
                    switch (c2) {
                        case 100:
                            xsltc.setDestDirectory(getopt.getOptionArg());
                            break;
                        case 101:
                        case 102:
                        case 103:
                        case 104:
                        case 107:
                        case 108:
                        case 109:
                        case 113:
                        case 114:
                        case 116:
                        case 118:
                        case 119:
                        default:
                            printUsage();
                            break;
                        case 105:
                            useStdIn = true;
                            break;
                        case 106:
                            xsltc.setJarFileName(getopt.getOptionArg());
                            break;
                        case 110:
                            xsltc.setTemplateInlining(true);
                            break;
                        case 111:
                            xsltc.setClassName(getopt.getOptionArg());
                            classNameSet = true;
                            break;
                        case 112:
                            xsltc.setPackageName(getopt.getOptionArg());
                            break;
                        case 115:
                            _allowExit = false;
                            break;
                        case 117:
                            inputIsURL = true;
                            break;
                        case 120:
                            xsltc.setDebug(true);
                            break;
                    }
                } else {
                    if (useStdIn) {
                        if (!classNameSet) {
                            System.err.println(new ErrorMsg(ErrorMsg.COMPILE_STDIN_ERR));
                            if (_allowExit) {
                                System.exit(-1);
                            }
                        }
                        compileOK = xsltc.compile(System.in, xsltc.getClassName());
                    } else {
                        String[] stylesheetNames = getopt.getCmdArgs();
                        Vector stylesheetVector = new Vector();
                        for (String name : stylesheetNames) {
                            if (inputIsURL) {
                                url = new URL(name);
                            } else {
                                url = new File(name).toURI().toURL();
                            }
                            stylesheetVector.addElement(url);
                        }
                        compileOK = xsltc.compile(stylesheetVector);
                    }
                    if (compileOK) {
                        xsltc.printWarnings();
                        if (xsltc.getJarFileName() != null) {
                            xsltc.outputToJar();
                        }
                        if (_allowExit) {
                            System.exit(0);
                        }
                    } else {
                        xsltc.printWarnings();
                        xsltc.printErrors();
                        if (_allowExit) {
                            System.exit(-1);
                        }
                    }
                    return;
                }
            }
        } catch (GetOptsException ex) {
            System.err.println(ex);
            printUsage();
        } catch (Exception e2) {
            e2.printStackTrace();
            if (_allowExit) {
                System.exit(-1);
            }
        }
    }
}
