package com.sun.org.apache.xalan.internal.xsltc.cmdline;

import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;
import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/cmdline/Transform.class */
public final class Transform {
    private SerializationHandler _handler;
    private String _fileName;
    private String _className;
    private String _jarFileSrc;
    private boolean _isJarFileSpecified = false;
    private Vector _params = null;
    private boolean _uri;
    private boolean _debug;
    private int _iterations;

    public Transform(String className, String fileName, boolean uri, boolean debug, int iterations) {
        this._fileName = fileName;
        this._className = className;
        this._uri = uri;
        this._debug = debug;
        this._iterations = iterations;
    }

    public String getFileName() {
        return this._fileName;
    }

    public String getClassName() {
        return this._className;
    }

    public void setParameters(Vector params) {
        this._params = params;
    }

    private void setJarFileInputSrc(boolean flag, String jarFile) {
        this._isJarFileSpecified = flag;
        this._jarFileSrc = jarFile;
    }

    private void doTransform() throws ConfigurationError {
        try {
            Class clazz = ObjectFactory.findProviderClass(this._className, true);
            AbstractTranslet translet = (AbstractTranslet) clazz.newInstance();
            translet.postInitialization();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                factory.setFeature("http://xml.org/sax/features/namespaces", true);
            } catch (Exception e2) {
                factory.setNamespaceAware(true);
            }
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XSLTCDTMManager dtmManager = XSLTCDTMManager.createNewDTMManagerInstance();
            DTMWSFilter wsfilter = (translet == null || !(translet instanceof StripFilter)) ? null : new DOMWSFilter(translet);
            DOMEnhancedForDTM dom = (DOMEnhancedForDTM) dtmManager.getDTM(new SAXSource(reader, new InputSource(this._fileName)), false, wsfilter, true, false, translet.hasIdCall());
            dom.setDocumentURI(this._fileName);
            translet.prepassDocument(dom);
            int n2 = this._params.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Parameter param = (Parameter) this._params.elementAt(i2);
                translet.addParameter(param._name, param._value);
            }
            TransletOutputHandlerFactory tohFactory = TransletOutputHandlerFactory.newInstance();
            tohFactory.setOutputType(0);
            tohFactory.setEncoding(translet._encoding);
            tohFactory.setOutputMethod(translet._method);
            if (this._iterations == -1) {
                translet.transform(dom, tohFactory.getSerializationHandler());
            } else if (this._iterations > 0) {
                long mm = System.currentTimeMillis();
                for (int i3 = 0; i3 < this._iterations; i3++) {
                    translet.transform(dom, tohFactory.getSerializationHandler());
                }
                long mm2 = System.currentTimeMillis() - mm;
                System.err.println("\n<!--");
                System.err.println("  transform  = " + (mm2 / this._iterations) + " ms");
                System.err.println("  throughput = " + (1000.0d / (mm2 / this._iterations)) + " tps");
                System.err.println("-->");
            }
        } catch (TransletException e3) {
            if (this._debug) {
                e3.printStackTrace();
            }
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + e3.getMessage());
        } catch (FileNotFoundException e4) {
            if (this._debug) {
                e4.printStackTrace();
            }
            ErrorMsg err = new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, this._fileName);
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + err.toString());
        } catch (ClassNotFoundException e5) {
            if (this._debug) {
                e5.printStackTrace();
            }
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, this._className);
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + err2.toString());
        } catch (RuntimeException e6) {
            if (this._debug) {
                e6.printStackTrace();
            }
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + e6.getMessage());
        } catch (MalformedURLException e7) {
            if (this._debug) {
                e7.printStackTrace();
            }
            ErrorMsg err3 = new ErrorMsg(ErrorMsg.INVALID_URI_ERR, this._fileName);
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + err3.toString());
        } catch (UnknownHostException e8) {
            if (this._debug) {
                e8.printStackTrace();
            }
            ErrorMsg err4 = new ErrorMsg(ErrorMsg.INVALID_URI_ERR, this._fileName);
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + err4.toString());
        } catch (SAXException e9) {
            Exception ex = e9.getException();
            if (this._debug) {
                if (ex != null) {
                    ex.printStackTrace();
                }
                e9.printStackTrace();
            }
            System.err.print(new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY));
            if (ex != null) {
                System.err.println(ex.getMessage());
            } else {
                System.err.println(e9.getMessage());
            }
        } catch (Exception e10) {
            if (this._debug) {
                e10.printStackTrace();
            }
            System.err.println(((Object) new ErrorMsg(ErrorMsg.RUNTIME_ERROR_KEY)) + e10.getMessage());
        }
    }

    public static void printUsage() {
        System.err.println(new ErrorMsg(ErrorMsg.TRANSFORM_USAGE_STR));
    }

    public static void main(String[] args) throws ConfigurationError {
        try {
            if (args.length > 0) {
                int iterations = -1;
                boolean uri = false;
                boolean debug = false;
                boolean isJarFileSpecified = false;
                String jarFile = null;
                int i2 = 0;
                while (i2 < args.length && args[i2].charAt(0) == '-') {
                    if (args[i2].equals("-u")) {
                        uri = true;
                    } else if (args[i2].equals("-x")) {
                        debug = true;
                    } else if (args[i2].equals("-j")) {
                        isJarFileSpecified = true;
                        i2++;
                        jarFile = args[i2];
                    } else if (args[i2].equals("-n")) {
                        try {
                            i2++;
                            iterations = Integer.parseInt(args[i2]);
                        } catch (NumberFormatException e2) {
                        }
                    } else {
                        printUsage();
                    }
                    i2++;
                }
                if (args.length - i2 < 2) {
                    printUsage();
                }
                Transform handler = new Transform(args[i2 + 1], args[i2], uri, debug, iterations);
                handler.setJarFileInputSrc(isJarFileSpecified, jarFile);
                Vector params = new Vector();
                int i3 = i2 + 2;
                while (i3 < args.length) {
                    int equal = args[i3].indexOf(61);
                    if (equal > 0) {
                        String name = args[i3].substring(0, equal);
                        String value = args[i3].substring(equal + 1);
                        params.addElement(new Parameter(name, value));
                    } else {
                        printUsage();
                    }
                    i3++;
                }
                if (i3 == args.length) {
                    handler.setParameters(params);
                    handler.doTransform();
                }
            } else {
                printUsage();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}
