package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/IncrementalSAXSource_Xerces.class */
public class IncrementalSAXSource_Xerces implements IncrementalSAXSource {
    Method fParseSomeSetup;
    Method fParseSome;
    Object fPullParserConfig;
    Method fConfigSetInput;
    Method fConfigParse;
    Method fSetInputSource;
    Constructor fConfigInputSourceCtor;
    Method fConfigSetByteStream;
    Method fConfigSetCharStream;
    Method fConfigSetEncoding;
    Method fReset;
    SAXParser fIncrementalParser;
    private boolean fParseInProgress;
    private static final Object[] noparms = new Object[0];
    private static final Object[] parmsfalse = {Boolean.FALSE};

    public IncrementalSAXSource_Xerces() throws NoSuchMethodException, ConfigurationError {
        this.fParseSomeSetup = null;
        this.fParseSome = null;
        this.fPullParserConfig = null;
        this.fConfigSetInput = null;
        this.fConfigParse = null;
        this.fSetInputSource = null;
        this.fConfigInputSourceCtor = null;
        this.fConfigSetByteStream = null;
        this.fConfigSetCharStream = null;
        this.fConfigSetEncoding = null;
        this.fReset = null;
        this.fParseInProgress = false;
        try {
            Class xniConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration", true);
            Class[] args1 = {xniConfigClass};
            Constructor ctor = SAXParser.class.getConstructor(args1);
            Class xniStdConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.parsers.StandardParserConfiguration", true);
            this.fPullParserConfig = xniStdConfigClass.newInstance();
            Object[] args2 = {this.fPullParserConfig};
            this.fIncrementalParser = (SAXParser) ctor.newInstance(args2);
            Class fXniInputSourceClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource", true);
            Class[] args3 = {fXniInputSourceClass};
            this.fConfigSetInput = xniStdConfigClass.getMethod("setInputSource", args3);
            Class[] args4 = {String.class, String.class, String.class};
            this.fConfigInputSourceCtor = fXniInputSourceClass.getConstructor(args4);
            Class[] args5 = {InputStream.class};
            this.fConfigSetByteStream = fXniInputSourceClass.getMethod("setByteStream", args5);
            Class[] args6 = {Reader.class};
            this.fConfigSetCharStream = fXniInputSourceClass.getMethod("setCharacterStream", args6);
            Class[] args7 = {String.class};
            this.fConfigSetEncoding = fXniInputSourceClass.getMethod("setEncoding", args7);
            Class[] argsb = {Boolean.TYPE};
            this.fConfigParse = xniStdConfigClass.getMethod("parse", argsb);
            Class[] noargs = new Class[0];
            this.fReset = this.fIncrementalParser.getClass().getMethod(Constants.RESET, noargs);
        } catch (Exception e2) {
            IncrementalSAXSource_Xerces dummy = new IncrementalSAXSource_Xerces(new SAXParser());
            this.fParseSomeSetup = dummy.fParseSomeSetup;
            this.fParseSome = dummy.fParseSome;
            this.fIncrementalParser = dummy.fIncrementalParser;
        }
    }

    public IncrementalSAXSource_Xerces(SAXParser parser) throws NoSuchMethodException {
        this.fParseSomeSetup = null;
        this.fParseSome = null;
        this.fPullParserConfig = null;
        this.fConfigSetInput = null;
        this.fConfigParse = null;
        this.fSetInputSource = null;
        this.fConfigInputSourceCtor = null;
        this.fConfigSetByteStream = null;
        this.fConfigSetCharStream = null;
        this.fConfigSetEncoding = null;
        this.fReset = null;
        this.fParseInProgress = false;
        this.fIncrementalParser = parser;
        Class me = parser.getClass();
        Class[] parms = {InputSource.class};
        this.fParseSomeSetup = me.getMethod("parseSomeSetup", parms);
        this.fParseSome = me.getMethod("parseSome", new Class[0]);
    }

    public static IncrementalSAXSource createIncrementalSAXSource() {
        try {
            return new IncrementalSAXSource_Xerces();
        } catch (NoSuchMethodException e2) {
            IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
            iss.setXMLReader(new SAXParser());
            return iss;
        }
    }

    public static IncrementalSAXSource createIncrementalSAXSource(SAXParser parser) {
        try {
            return new IncrementalSAXSource_Xerces(parser);
        } catch (NoSuchMethodException e2) {
            IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
            iss.setXMLReader(parser);
            return iss;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setContentHandler(ContentHandler handler) {
        this.fIncrementalParser.setContentHandler(handler);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setLexicalHandler(LexicalHandler handler) {
        try {
            this.fIncrementalParser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        } catch (SAXNotRecognizedException e2) {
        } catch (SAXNotSupportedException e3) {
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setDTDHandler(DTDHandler handler) {
        this.fIncrementalParser.setDTDHandler(handler);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void startParse(InputSource source) throws SAXException {
        if (this.fIncrementalParser == null) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_NEEDS_SAXPARSER", null));
        }
        if (this.fParseInProgress) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_WHILE_PARSING", null));
        }
        try {
            boolean ok = parseSomeSetup(source);
            if (!ok) {
                throw new SAXException(XMLMessages.createXMLMessage("ER_COULD_NOT_INIT_PARSER", null));
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public Object deliverMoreNodes(boolean parsemore) {
        Object arg;
        if (!parsemore) {
            this.fParseInProgress = false;
            return Boolean.FALSE;
        }
        try {
            boolean keepgoing = parseSome();
            arg = keepgoing ? Boolean.TRUE : Boolean.FALSE;
        } catch (IOException ex) {
            arg = ex;
        } catch (SAXException ex2) {
            arg = ex2;
        } catch (Exception ex3) {
            arg = new SAXException(ex3);
        }
        return arg;
    }

    private boolean parseSomeSetup(InputSource source) throws IllegalAccessException, SAXException, InstantiationException, IOException, IllegalArgumentException, InvocationTargetException {
        if (this.fConfigSetInput != null) {
            Object[] parms1 = {source.getPublicId(), source.getSystemId(), null};
            Object xmlsource = this.fConfigInputSourceCtor.newInstance(parms1);
            Object[] parmsa = {source.getByteStream()};
            this.fConfigSetByteStream.invoke(xmlsource, parmsa);
            parmsa[0] = source.getCharacterStream();
            this.fConfigSetCharStream.invoke(xmlsource, parmsa);
            parmsa[0] = source.getEncoding();
            this.fConfigSetEncoding.invoke(xmlsource, parmsa);
            Object[] noparms2 = new Object[0];
            this.fReset.invoke(this.fIncrementalParser, noparms2);
            parmsa[0] = xmlsource;
            this.fConfigSetInput.invoke(this.fPullParserConfig, parmsa);
            return parseSome();
        }
        Object[] parm = {source};
        Object ret = this.fParseSomeSetup.invoke(this.fIncrementalParser, parm);
        return ((Boolean) ret).booleanValue();
    }

    private boolean parseSome() throws IllegalAccessException, SAXException, IOException, IllegalArgumentException, InvocationTargetException {
        if (this.fConfigSetInput != null) {
            Object ret = this.fConfigParse.invoke(this.fPullParserConfig, parmsfalse);
            return ((Boolean) ret).booleanValue();
        }
        Object ret2 = this.fParseSome.invoke(this.fIncrementalParser, noparms);
        return ((Boolean) ret2).booleanValue();
    }

    public static void _main(String[] args) {
        System.out.println("Starting...");
        CoroutineManager co = new CoroutineManager();
        int appCoroutineID = co.co_joinCoroutineSet(-1);
        if (appCoroutineID == -1) {
            System.out.println("ERROR: Couldn't allocate coroutine number.\n");
            return;
        }
        IncrementalSAXSource parser = createIncrementalSAXSource();
        XMLSerializer trace = new XMLSerializer(System.out, (OutputFormat) null);
        parser.setContentHandler(trace);
        parser.setLexicalHandler(trace);
        int arg = 0;
        while (arg < args.length) {
            try {
                InputSource source = new InputSource(args[arg]);
                boolean more = true;
                parser.startParse(source);
                Object result = parser.deliverMoreNodes(true);
                while (result == Boolean.TRUE) {
                    System.out.println("\nSome parsing successful, trying more.\n");
                    if (arg + 1 < args.length && "!".equals(args[arg + 1])) {
                        arg++;
                        more = false;
                    }
                    result = parser.deliverMoreNodes(more);
                }
                if ((result instanceof Boolean) && ((Boolean) result) == Boolean.FALSE) {
                    System.out.println("\nParser ended (EOF or on request).\n");
                } else if (result == null) {
                    System.out.println("\nUNEXPECTED: Parser says shut down prematurely.\n");
                } else if (result instanceof Exception) {
                    throw new WrappedRuntimeException((Exception) result);
                }
            } catch (SAXException e2) {
                e2.printStackTrace();
            }
            arg++;
        }
    }
}
