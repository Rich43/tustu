package com.sun.org.apache.xalan.internal.xslt;

import com.sun.org.apache.xalan.internal.Version;
import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xml.internal.utils.res.XResourceBundle;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xslt/Process.class */
public class Process {
    protected static void printArgOptions(ResourceBundle resbundle) {
        System.out.println(resbundle.getString("xslProc_option"));
        System.out.println("\n\t\t\t" + resbundle.getString("xslProc_common_options") + "\n");
        System.out.println(resbundle.getString("optionXSLTC"));
        System.out.println(resbundle.getString("optionIN"));
        System.out.println(resbundle.getString("optionXSL"));
        System.out.println(resbundle.getString("optionOUT"));
        System.out.println(resbundle.getString("optionV"));
        System.out.println(resbundle.getString("optionEDUMP"));
        System.out.println(resbundle.getString("optionXML"));
        System.out.println(resbundle.getString("optionTEXT"));
        System.out.println(resbundle.getString("optionHTML"));
        System.out.println(resbundle.getString("optionPARAM"));
        System.out.println(resbundle.getString("optionMEDIA"));
        System.out.println(resbundle.getString("optionFLAVOR"));
        System.out.println(resbundle.getString("optionDIAG"));
        System.out.println(resbundle.getString("optionURIRESOLVER"));
        System.out.println(resbundle.getString("optionENTITYRESOLVER"));
        waitForReturnKey(resbundle);
        System.out.println(resbundle.getString("optionCONTENTHANDLER"));
        System.out.println(resbundle.getString("optionSECUREPROCESSING"));
        System.out.println("\n\t\t\t" + resbundle.getString("xslProc_xsltc_options") + "\n");
        System.out.println(resbundle.getString("optionXO"));
        waitForReturnKey(resbundle);
        System.out.println(resbundle.getString("optionXD"));
        System.out.println(resbundle.getString("optionXJ"));
        System.out.println(resbundle.getString("optionXP"));
        System.out.println(resbundle.getString("optionXN"));
        System.out.println(resbundle.getString("optionXX"));
        System.out.println(resbundle.getString("optionXT"));
    }

    public static void _main(String[] argv) throws NumberFormatException {
        TransformerFactory tfactory;
        StreamResult strResult;
        boolean doStackDumpOnError = false;
        boolean doDiag = false;
        boolean isSecureProcessing = false;
        PrintWriter diagnosticsWriter = new PrintWriter((OutputStream) System.err, true);
        PrintWriter dumpWriter = diagnosticsWriter;
        ResourceBundle resbundle = SecuritySupport.getResourceBundle(XResourceBundle.ERROR_RESOURCES);
        String flavor = "s2s";
        if (argv.length < 1) {
            printArgOptions(resbundle);
            return;
        }
        boolean useXSLTC = true;
        for (String str : argv) {
            if ("-XSLTC".equalsIgnoreCase(str)) {
                useXSLTC = true;
            }
        }
        if (useXSLTC) {
            Properties props = System.getProperties();
            props.put("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
            System.setProperties(props);
        }
        try {
            tfactory = TransformerFactory.newInstance();
            tfactory.setErrorListener(new DefaultErrorHandler());
        } catch (TransformerFactoryConfigurationError pfe) {
            pfe.printStackTrace(dumpWriter);
            String msg = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", null);
            diagnosticsWriter.println(msg);
            tfactory = null;
            doExit(msg);
        }
        String inFileName = null;
        String outFileName = null;
        String dumpFileName = null;
        String xslFileName = null;
        String outputType = null;
        String media = null;
        Vector params = new Vector();
        URIResolver uriResolver = null;
        EntityResolver entityResolver = null;
        ContentHandler contentHandler = null;
        int i2 = 0;
        while (i2 < argv.length) {
            if (!"-XSLTC".equalsIgnoreCase(argv[i2])) {
                if ("-INDENT".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                        i2++;
                        Integer.parseInt(argv[i2]);
                    }
                } else if ("-IN".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                        i2++;
                        inFileName = argv[i2];
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-IN"}));
                    }
                } else if ("-MEDIA".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length) {
                        i2++;
                        media = argv[i2];
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-MEDIA"}));
                    }
                } else if ("-OUT".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                        i2++;
                        outFileName = argv[i2];
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-OUT"}));
                    }
                } else if ("-XSL".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                        i2++;
                        xslFileName = argv[i2];
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XSL"}));
                    }
                } else if ("-FLAVOR".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 1 < argv.length) {
                        i2++;
                        flavor = argv[i2];
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-FLAVOR"}));
                    }
                } else if ("-PARAM".equalsIgnoreCase(argv[i2])) {
                    if (i2 + 2 < argv.length) {
                        int i3 = i2 + 1;
                        String name = argv[i3];
                        params.addElement(name);
                        i2 = i3 + 1;
                        String expression = argv[i2];
                        params.addElement(expression);
                    } else {
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-PARAM"}));
                    }
                } else if (!"-E".equalsIgnoreCase(argv[i2])) {
                    if ("-V".equalsIgnoreCase(argv[i2])) {
                        diagnosticsWriter.println(resbundle.getString("version") + Version.getVersion() + ", " + resbundle.getString("version2"));
                    } else if (!"-Q".equalsIgnoreCase(argv[i2])) {
                        if ("-DIAG".equalsIgnoreCase(argv[i2])) {
                            doDiag = true;
                        } else if ("-XML".equalsIgnoreCase(argv[i2])) {
                            outputType = "xml";
                        } else if ("-TEXT".equalsIgnoreCase(argv[i2])) {
                            outputType = "text";
                        } else if ("-HTML".equalsIgnoreCase(argv[i2])) {
                            outputType = "html";
                        } else if ("-EDUMP".equalsIgnoreCase(argv[i2])) {
                            doStackDumpOnError = true;
                            if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                i2++;
                                dumpFileName = argv[i2];
                            }
                        } else if ("-URIRESOLVER".equalsIgnoreCase(argv[i2])) {
                            if (i2 + 1 < argv.length) {
                                try {
                                    i2++;
                                    uriResolver = (URIResolver) ObjectFactory.newInstance(argv[i2], true);
                                    tfactory.setURIResolver(uriResolver);
                                } catch (ConfigurationError e2) {
                                    String msg2 = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-URIResolver"});
                                    System.err.println(msg2);
                                    doExit(msg2);
                                }
                            } else {
                                String msg3 = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-URIResolver"});
                                System.err.println(msg3);
                                doExit(msg3);
                            }
                        } else if ("-ENTITYRESOLVER".equalsIgnoreCase(argv[i2])) {
                            if (i2 + 1 < argv.length) {
                                try {
                                    i2++;
                                    entityResolver = (EntityResolver) ObjectFactory.newInstance(argv[i2], true);
                                } catch (ConfigurationError e3) {
                                    String msg4 = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-EntityResolver"});
                                    System.err.println(msg4);
                                    doExit(msg4);
                                }
                            } else {
                                String msg5 = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-EntityResolver"});
                                System.err.println(msg5);
                                doExit(msg5);
                            }
                        } else if ("-CONTENTHANDLER".equalsIgnoreCase(argv[i2])) {
                            if (i2 + 1 < argv.length) {
                                try {
                                    i2++;
                                    contentHandler = (ContentHandler) ObjectFactory.newInstance(argv[i2], true);
                                } catch (ConfigurationError e4) {
                                    String msg6 = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-ContentHandler"});
                                    System.err.println(msg6);
                                    doExit(msg6);
                                }
                            } else {
                                String msg7 = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-ContentHandler"});
                                System.err.println(msg7);
                                doExit(msg7);
                            }
                        } else if ("-XO".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    tfactory.setAttribute(TransformerFactoryImpl.GENERATE_TRANSLET, "true");
                                    i2++;
                                    tfactory.setAttribute(TransformerFactoryImpl.TRANSLET_NAME, argv[i2]);
                                } else {
                                    tfactory.setAttribute(TransformerFactoryImpl.GENERATE_TRANSLET, "true");
                                }
                            } else {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                }
                                printInvalidXalanOption("-XO");
                            }
                        } else if ("-XD".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                    tfactory.setAttribute(TransformerFactoryImpl.DESTINATION_DIRECTORY, argv[i2]);
                                } else {
                                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XD"}));
                                }
                            } else {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                }
                                printInvalidXalanOption("-XD");
                            }
                        } else if ("-XJ".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    tfactory.setAttribute(TransformerFactoryImpl.GENERATE_TRANSLET, "true");
                                    i2++;
                                    tfactory.setAttribute(TransformerFactoryImpl.JAR_NAME, argv[i2]);
                                } else {
                                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XJ"}));
                                }
                            } else {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                }
                                printInvalidXalanOption("-XJ");
                            }
                        } else if ("-XP".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                    tfactory.setAttribute(TransformerFactoryImpl.PACKAGE_NAME, argv[i2]);
                                } else {
                                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XP"}));
                                }
                            } else {
                                if (i2 + 1 < argv.length && argv[i2 + 1].charAt(0) != '-') {
                                    i2++;
                                }
                                printInvalidXalanOption("-XP");
                            }
                        } else if ("-XN".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                tfactory.setAttribute(TransformerFactoryImpl.ENABLE_INLINING, "true");
                            } else {
                                printInvalidXalanOption("-XN");
                            }
                        } else if ("-XX".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                tfactory.setAttribute(TransformerFactoryImpl.DEBUG, "true");
                            } else {
                                printInvalidXalanOption("-XX");
                            }
                        } else if ("-XT".equalsIgnoreCase(argv[i2])) {
                            if (useXSLTC) {
                                tfactory.setAttribute(TransformerFactoryImpl.AUTO_TRANSLET, "true");
                            } else {
                                printInvalidXalanOption("-XT");
                            }
                        } else if ("-SECURE".equalsIgnoreCase(argv[i2])) {
                            isSecureProcessing = true;
                            try {
                                tfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                            } catch (TransformerConfigurationException e5) {
                            }
                        } else {
                            System.err.println(XSLMessages.createMessage("ER_INVALID_OPTION", new Object[]{argv[i2]}));
                        }
                    }
                }
            }
            i2++;
        }
        if (inFileName == null && xslFileName == null) {
            String msg8 = resbundle.getString("xslProc_no_input");
            System.err.println(msg8);
            doExit(msg8);
        }
        try {
            long start = System.currentTimeMillis();
            if (null != dumpFileName) {
                dumpWriter = new PrintWriter(new FileWriter(dumpFileName));
            }
            Templates stylesheet = null;
            if (null != xslFileName) {
                if (flavor.equals("d2d")) {
                    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                    dfactory.setNamespaceAware(true);
                    if (isSecureProcessing) {
                        try {
                            dfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                        } catch (ParserConfigurationException e6) {
                        }
                    }
                    Node xslDOM = dfactory.newDocumentBuilder().parse(new InputSource(xslFileName));
                    stylesheet = tfactory.newTemplates(new DOMSource(xslDOM, xslFileName));
                } else {
                    stylesheet = tfactory.newTemplates(new StreamSource(xslFileName));
                }
            }
            if (null != outFileName) {
                strResult = new StreamResult(new FileOutputStream(outFileName));
                strResult.setSystemId(outFileName);
            } else {
                strResult = new StreamResult(System.out);
            }
            SAXTransformerFactory stf = (SAXTransformerFactory) tfactory;
            if (null == stylesheet) {
                Source source = stf.getAssociatedStylesheet(new StreamSource(inFileName), media, null, null);
                if (null != source) {
                    stylesheet = tfactory.newTemplates(source);
                } else {
                    if (null != media) {
                        throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_IN_MEDIA", new Object[]{inFileName, media}));
                    }
                    throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_PI", new Object[]{inFileName}));
                }
            }
            if (null != stylesheet) {
                Transformer transformer = flavor.equals("th") ? null : stylesheet.newTransformer();
                transformer.setErrorListener(new DefaultErrorHandler());
                if (null != outputType) {
                    transformer.setOutputProperty("method", outputType);
                }
                int nParams = params.size();
                for (int i4 = 0; i4 < nParams; i4 += 2) {
                    transformer.setParameter((String) params.elementAt(i4), (String) params.elementAt(i4 + 1));
                }
                if (uriResolver != null) {
                    transformer.setURIResolver(uriResolver);
                }
                if (null != inFileName) {
                    if (flavor.equals("d2d")) {
                        DocumentBuilderFactory dfactory2 = DocumentBuilderFactory.newInstance();
                        dfactory2.setCoalescing(true);
                        dfactory2.setNamespaceAware(true);
                        if (isSecureProcessing) {
                            try {
                                dfactory2.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                            } catch (ParserConfigurationException e7) {
                            }
                        }
                        DocumentBuilder docBuilder = dfactory2.newDocumentBuilder();
                        if (entityResolver != null) {
                            docBuilder.setEntityResolver(entityResolver);
                        }
                        Node xmlDoc = docBuilder.parse(new InputSource(inFileName));
                        Document doc = docBuilder.newDocument();
                        DocumentFragment outNode = doc.createDocumentFragment();
                        transformer.transform(new DOMSource(xmlDoc, inFileName), new DOMResult(outNode));
                        Transformer serializer = stf.newTransformer();
                        serializer.setErrorListener(new DefaultErrorHandler());
                        Properties serializationProps = stylesheet.getOutputProperties();
                        serializer.setOutputProperties(serializationProps);
                        if (contentHandler != null) {
                            SAXResult result = new SAXResult(contentHandler);
                            serializer.transform(new DOMSource(outNode), result);
                        } else {
                            serializer.transform(new DOMSource(outNode), strResult);
                        }
                    } else if (flavor.equals("th")) {
                        for (int i5 = 0; i5 < 1; i5++) {
                            XMLReader reader = null;
                            try {
                                SAXParserFactory factory = SAXParserFactory.newInstance();
                                factory.setNamespaceAware(true);
                                if (isSecureProcessing) {
                                    try {
                                        factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                    } catch (SAXException e8) {
                                    }
                                }
                                SAXParser jaxpParser = factory.newSAXParser();
                                reader = jaxpParser.getXMLReader();
                            } catch (AbstractMethodError e9) {
                            } catch (NoSuchMethodError e10) {
                            } catch (FactoryConfigurationError ex1) {
                                throw new SAXException(ex1.toString());
                            } catch (ParserConfigurationException ex) {
                                throw new SAXException(ex);
                            }
                            if (null == reader) {
                                reader = XMLReaderFactory.createXMLReader();
                            }
                            TransformerHandler th = stf.newTransformerHandler(stylesheet);
                            reader.setContentHandler(th);
                            reader.setDTDHandler(th);
                            if (th instanceof ErrorHandler) {
                                reader.setErrorHandler((ErrorHandler) th);
                            }
                            try {
                                reader.setProperty("http://xml.org/sax/properties/lexical-handler", th);
                            } catch (SAXNotRecognizedException e11) {
                            } catch (SAXNotSupportedException e12) {
                            }
                            try {
                                reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                            } catch (SAXException e13) {
                            }
                            th.setResult(strResult);
                            reader.parse(new InputSource(inFileName));
                        }
                    } else if (entityResolver != null) {
                        XMLReader reader2 = null;
                        try {
                            SAXParserFactory factory2 = SAXParserFactory.newInstance();
                            factory2.setNamespaceAware(true);
                            if (isSecureProcessing) {
                                try {
                                    factory2.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                } catch (SAXException e14) {
                                }
                            }
                            SAXParser jaxpParser2 = factory2.newSAXParser();
                            reader2 = jaxpParser2.getXMLReader();
                        } catch (AbstractMethodError e15) {
                        } catch (NoSuchMethodError e16) {
                        } catch (FactoryConfigurationError ex12) {
                            throw new SAXException(ex12.toString());
                        } catch (ParserConfigurationException ex2) {
                            throw new SAXException(ex2);
                        }
                        if (null == reader2) {
                            reader2 = XMLReaderFactory.createXMLReader();
                        }
                        reader2.setEntityResolver(entityResolver);
                        if (contentHandler != null) {
                            SAXResult result2 = new SAXResult(contentHandler);
                            transformer.transform(new SAXSource(reader2, new InputSource(inFileName)), result2);
                        } else {
                            transformer.transform(new SAXSource(reader2, new InputSource(inFileName)), strResult);
                        }
                    } else if (contentHandler != null) {
                        SAXResult result3 = new SAXResult(contentHandler);
                        transformer.transform(new StreamSource(inFileName), result3);
                    } else {
                        transformer.transform(new StreamSource(inFileName), strResult);
                    }
                } else {
                    transformer.transform(new StreamSource(new StringReader("<?xml version=\"1.0\"?> <doc/>")), strResult);
                }
            } else {
                String msg9 = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", null);
                diagnosticsWriter.println(msg9);
                doExit(msg9);
            }
            if (null != outFileName && strResult != null) {
                OutputStream out = strResult.getOutputStream();
                Writer writer = strResult.getWriter();
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e17) {
                    }
                }
                if (writer != null) {
                    writer.close();
                }
            }
            long stop = System.currentTimeMillis();
            long millisecondsDuration = stop - start;
            if (doDiag) {
                Object[] msgArgs = {inFileName, xslFileName, new Long(millisecondsDuration)};
                String msg10 = XSLMessages.createMessage("diagTiming", msgArgs);
                diagnosticsWriter.println('\n');
                diagnosticsWriter.println(msg10);
            }
        } catch (Throwable th2) {
            throwable = th2;
            while (throwable instanceof WrappedRuntimeException) {
                throwable = ((WrappedRuntimeException) throwable).getException();
            }
            if ((throwable instanceof NullPointerException) || (throwable instanceof ClassCastException)) {
                doStackDumpOnError = true;
            }
            diagnosticsWriter.println();
            if (doStackDumpOnError) {
                throwable.printStackTrace(dumpWriter);
            } else {
                DefaultErrorHandler.printLocation(diagnosticsWriter, throwable);
                diagnosticsWriter.println(XSLMessages.createMessage("ER_XSLT_ERROR", null) + " (" + throwable.getClass().getName() + "): " + throwable.getMessage());
            }
            if (null != dumpFileName) {
                dumpWriter.close();
            }
            doExit(throwable.getMessage());
        }
        if (null != dumpFileName) {
            dumpWriter.close();
        }
        if (null != diagnosticsWriter) {
        }
    }

    static void doExit(String msg) {
        throw new RuntimeException(msg);
    }

    private static void waitForReturnKey(ResourceBundle resbundle) {
        System.out.println(resbundle.getString("xslProc_return_to_continue"));
        do {
            try {
            } catch (IOException e2) {
                return;
            }
        } while (System.in.read() != 10);
    }

    private static void printInvalidXSLTCOption(String option) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xsltc_option", new Object[]{option}));
    }

    private static void printInvalidXalanOption(String option) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xalan_option", new Object[]{option}));
    }
}
