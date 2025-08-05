package com.sun.xml.internal.bind.v2.model.impl;

import android.R;
import com.sun.istack.internal.ByteArrayDataSource;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import com.sun.xml.internal.bind.v2.util.DataSourceSource;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.bind.MarshalException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl.class */
public abstract class RuntimeBuiltinLeafInfoImpl<T> extends BuiltinLeafInfoImpl<Type, Class> implements RuntimeBuiltinLeafInfo, Transducer<T> {
    public static final Map<Type, RuntimeBuiltinLeafInfoImpl<?>> LEAVES = new HashMap();
    public static final RuntimeBuiltinLeafInfoImpl<String> STRING;
    private static final String DATE = "date";
    public static final List<RuntimeBuiltinLeafInfoImpl<?>> builtinBeanInfos;
    public static final String MAP_ANYURI_TO_URI = "mapAnyUriToUri";
    private static final Map<QName, String> xmlGregorianCalendarFormatString;
    private static final Map<QName, Integer> xmlGregorianCalendarFieldRef;

    private RuntimeBuiltinLeafInfoImpl(Class type, QName... typeNames) {
        super(type, typeNames);
        LEAVES.put(type, this);
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo
    public final Class getClazz() {
        return (Class) getType2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo, com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement
    public final Transducer getTransducer() {
        return this;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public final boolean isDefault() {
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void declareNamespace(T o2, XMLSerializer w2) throws AccessorException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public QName getTypeName(T instance) {
        return null;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$StringImpl.class */
    private static abstract class StringImpl<T> extends RuntimeBuiltinLeafInfoImpl<T> {
        public abstract String print(T t2) throws AccessorException;

        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException {
            return print((StringImpl<T>) obj);
        }

        protected StringImpl(Class type, QName... typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public void writeText(XMLSerializer w2, T o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
            w2.text(print((StringImpl<T>) o2), fieldName);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public void writeLeafElement(XMLSerializer w2, Name tagName, T o2, String fieldName) throws AccessorException, XMLStreamException, SAXException, IOException {
            w2.leafElement(tagName, print((StringImpl<T>) o2), fieldName);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$PcdataImpl.class */
    private static abstract class PcdataImpl<T> extends RuntimeBuiltinLeafInfoImpl<T> {
        public abstract Pcdata print(T t2) throws AccessorException;

        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException {
            return print((PcdataImpl<T>) obj);
        }

        protected PcdataImpl(Class type, QName... typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public final void writeText(XMLSerializer w2, T o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
            w2.text(print((PcdataImpl<T>) o2), fieldName);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public final void writeLeafElement(XMLSerializer w2, Name tagName, T o2, String fieldName) throws AccessorException, XMLStreamException, SAXException, IOException {
            w2.leafElement(tagName, print((PcdataImpl<T>) o2), fieldName);
        }
    }

    static {
        String MAP_ANYURI_TO_URI_VALUE = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty(RuntimeBuiltinLeafInfoImpl.MAP_ANYURI_TO_URI);
            }
        });
        QName[] qnames = MAP_ANYURI_TO_URI_VALUE == null ? new QName[]{createXS("string"), createXS(SchemaSymbols.ATTVAL_ANYSIMPLETYPE), createXS(SchemaSymbols.ATTVAL_NORMALIZEDSTRING), createXS(SchemaSymbols.ATTVAL_ANYURI), createXS(SchemaSymbols.ATTVAL_TOKEN), createXS("language"), createXS("Name"), createXS(SchemaSymbols.ATTVAL_NCNAME), createXS(SchemaSymbols.ATTVAL_NMTOKEN), createXS(SchemaSymbols.ATTVAL_ENTITY)} : new QName[]{createXS("string"), createXS(SchemaSymbols.ATTVAL_ANYSIMPLETYPE), createXS(SchemaSymbols.ATTVAL_NORMALIZEDSTRING), createXS(SchemaSymbols.ATTVAL_TOKEN), createXS("language"), createXS("Name"), createXS(SchemaSymbols.ATTVAL_NCNAME), createXS(SchemaSymbols.ATTVAL_NMTOKEN), createXS(SchemaSymbols.ATTVAL_ENTITY)};
        STRING = new StringImplImpl(String.class, qnames);
        ArrayList<RuntimeBuiltinLeafInfoImpl<?>> secondaryList = new ArrayList<>();
        secondaryList.add(new StringImpl<Character>(Character.class, createXS(SchemaSymbols.ATTVAL_UNSIGNEDSHORT)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.2
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Character parse(CharSequence text) {
                return Character.valueOf((char) DatatypeConverterImpl._parseInt(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Character v2) {
                return Integer.toString(v2.charValue());
            }
        });
        secondaryList.add(new StringImpl<Calendar>(Calendar.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.3
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Calendar parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString());
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Calendar v2) {
                return DatatypeConverterImpl._printDateTime(v2);
            }
        });
        secondaryList.add(new StringImpl<GregorianCalendar>(GregorianCalendar.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.4
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public GregorianCalendar parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString());
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(GregorianCalendar v2) {
                return DatatypeConverterImpl._printDateTime(v2);
            }
        });
        secondaryList.add(new StringImpl<Date>(Date.class, DatatypeConstants.DATETIME) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.5
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Date parse(CharSequence text) {
                return DatatypeConverterImpl._parseDateTime(text.toString()).getTime();
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Date v2) {
                XMLSerializer xs = XMLSerializer.getInstance();
                QName type = xs.getSchemaType();
                GregorianCalendar cal = new GregorianCalendar(0, 0, 0);
                cal.setTime(v2);
                if (type != null && "http://www.w3.org/2001/XMLSchema".equals(type.getNamespaceURI()) && "date".equals(type.getLocalPart())) {
                    return DatatypeConverterImpl._printDate(cal);
                }
                return DatatypeConverterImpl._printDateTime(cal);
            }
        });
        secondaryList.add(new StringImpl<File>(File.class, createXS("string")) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.6
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public File parse(CharSequence text) {
                return new File(WhiteSpaceProcessor.trim(text).toString());
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(File v2) {
                return v2.getPath();
            }
        });
        secondaryList.add(new StringImpl<URL>(URL.class, createXS(SchemaSymbols.ATTVAL_ANYURI)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.7
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public URL parse(CharSequence text) throws SAXException {
                TODO.checkSpec("JSR222 Issue #42");
                try {
                    return new URL(WhiteSpaceProcessor.trim(text).toString());
                } catch (MalformedURLException e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(URL v2) {
                return v2.toExternalForm();
            }
        });
        if (MAP_ANYURI_TO_URI_VALUE == null) {
            secondaryList.add(new StringImpl<URI>(URI.class, createXS("string")) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.8
                @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
                public URI parse(CharSequence text) throws SAXException {
                    try {
                        return new URI(text.toString());
                    } catch (URISyntaxException e2) {
                        UnmarshallingContext.getInstance().handleError(e2);
                        return null;
                    }
                }

                @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
                public String print(URI v2) {
                    return v2.toString();
                }
            });
        }
        secondaryList.add(new StringImpl<Class>(Class.class, createXS("string")) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.9
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Class parse(CharSequence text) throws SAXException {
                TODO.checkSpec("JSR222 Issue #42");
                try {
                    String name = WhiteSpaceProcessor.trim(text).toString();
                    ClassLoader cl = UnmarshallingContext.getInstance().classLoader;
                    if (cl == null) {
                        cl = Thread.currentThread().getContextClassLoader();
                    }
                    if (cl != null) {
                        return cl.loadClass(name);
                    }
                    return Class.forName(name);
                } catch (ClassNotFoundException e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Class v2) {
                return v2.getName();
            }
        });
        secondaryList.add(new PcdataImpl<Image>(Image.class, createXS(SchemaSymbols.ATTVAL_BASE64BINARY)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.10
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Image parse(CharSequence text) throws SAXException {
                InputStream is;
                try {
                    if (text instanceof Base64Data) {
                        is = ((Base64Data) text).getInputStream();
                    } else {
                        is = new ByteArrayInputStream(RuntimeBuiltinLeafInfoImpl.decodeBase64(text));
                    }
                    try {
                        BufferedImage bufferedImage = ImageIO.read(is);
                        is.close();
                        return bufferedImage;
                    } catch (Throwable th) {
                        is.close();
                        throw th;
                    }
                } catch (IOException e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            private BufferedImage convertToBufferedImage(Image image) throws IOException {
                if (image instanceof BufferedImage) {
                    return (BufferedImage) image;
                }
                MediaTracker tracker = new MediaTracker(new Component() { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.10.1
                });
                tracker.addImage(image, 0);
                try {
                    tracker.waitForAll();
                    BufferedImage bufImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
                    Graphics g2 = bufImage.createGraphics();
                    g2.drawImage(image, 0, 0, null);
                    return bufImage;
                } catch (InterruptedException e2) {
                    throw new IOException(e2.getMessage());
                }
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public Base64Data print(Image v2) {
                ByteArrayOutputStreamEx imageData = new ByteArrayOutputStreamEx();
                XMLSerializer xs = XMLSerializer.getInstance();
                String mimeType = xs.getXMIMEContentType();
                if (mimeType == null || mimeType.startsWith("image/*")) {
                    mimeType = "image/png";
                }
                try {
                    Iterator<ImageWriter> itr = ImageIO.getImageWritersByMIMEType(mimeType);
                    if (itr.hasNext()) {
                        ImageWriter w2 = itr.next();
                        ImageOutputStream os = ImageIO.createImageOutputStream(imageData);
                        w2.setOutput(os);
                        w2.write(convertToBufferedImage(v2));
                        os.close();
                        w2.dispose();
                        Base64Data bd2 = new Base64Data();
                        imageData.set(bd2, mimeType);
                        return bd2;
                    }
                    xs.handleEvent(new ValidationEventImpl(1, Messages.NO_IMAGE_WRITER.format(mimeType), xs.getCurrentLocation(null)));
                    throw new RuntimeException("no encoder for MIME type " + mimeType);
                } catch (IOException e2) {
                    xs.handleError(e2);
                    throw new RuntimeException(e2);
                }
            }
        });
        secondaryList.add(new PcdataImpl<DataHandler>(DataHandler.class, createXS(SchemaSymbols.ATTVAL_BASE64BINARY)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.11
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public DataHandler parse(CharSequence text) {
                if (text instanceof Base64Data) {
                    return ((Base64Data) text).getDataHandler();
                }
                return new DataHandler(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public Base64Data print(DataHandler v2) {
                Base64Data bd2 = new Base64Data();
                bd2.set(v2);
                return bd2;
            }
        });
        secondaryList.add(new PcdataImpl<Source>(Source.class, createXS(SchemaSymbols.ATTVAL_BASE64BINARY)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.12
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Source parse(CharSequence text) throws SAXException {
                try {
                    if (text instanceof Base64Data) {
                        return new DataSourceSource(((Base64Data) text).getDataHandler());
                    }
                    return new DataSourceSource(new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
                } catch (MimeTypeParseException e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public Base64Data print(Source v2) throws IllegalArgumentException {
                DataSource ds;
                String dsct;
                XMLSerializer xs = XMLSerializer.getInstance();
                Base64Data bd2 = new Base64Data();
                String contentType = xs.getXMIMEContentType();
                MimeType mt = null;
                if (contentType != null) {
                    try {
                        mt = new MimeType(contentType);
                    } catch (MimeTypeParseException e2) {
                        xs.handleError(e2);
                    }
                }
                if ((v2 instanceof DataSourceSource) && (dsct = (ds = ((DataSourceSource) v2).getDataSource()).getContentType()) != null && (contentType == null || contentType.equals(dsct))) {
                    bd2.set(new DataHandler(ds));
                    return bd2;
                }
                String charset = null;
                if (mt != null) {
                    charset = mt.getParameter("charset");
                }
                if (charset == null) {
                    charset = "UTF-8";
                }
                try {
                    ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx();
                    Transformer tr = xs.getIdentityTransformer();
                    String defaultEncoding = tr.getOutputProperty("encoding");
                    tr.setOutputProperty("encoding", charset);
                    tr.transform(v2, new StreamResult(new OutputStreamWriter(baos, charset)));
                    tr.setOutputProperty("encoding", defaultEncoding);
                    baos.set(bd2, "application/xml; charset=" + charset);
                    return bd2;
                } catch (UnsupportedEncodingException e3) {
                    xs.handleError(e3);
                    bd2.set(new byte[0], XMLCodec.XML_APPLICATION_MIME_TYPE);
                    return bd2;
                } catch (TransformerException e4) {
                    xs.handleError(e4);
                    bd2.set(new byte[0], XMLCodec.XML_APPLICATION_MIME_TYPE);
                    return bd2;
                }
            }
        });
        secondaryList.add(new StringImpl<XMLGregorianCalendar>(XMLGregorianCalendar.class, createXS(SchemaSymbols.ATTVAL_ANYSIMPLETYPE), DatatypeConstants.DATE, DatatypeConstants.DATETIME, DatatypeConstants.TIME, DatatypeConstants.GMONTH, DatatypeConstants.GDAY, DatatypeConstants.GYEAR, DatatypeConstants.GYEARMONTH, DatatypeConstants.GMONTHDAY) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.13
            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(XMLGregorianCalendar cal) {
                XMLSerializer xs = XMLSerializer.getInstance();
                QName type = xs.getSchemaType();
                if (type != null) {
                    try {
                        RuntimeBuiltinLeafInfoImpl.checkXmlGregorianCalendarFieldRef(type, cal);
                        String format = (String) RuntimeBuiltinLeafInfoImpl.xmlGregorianCalendarFormatString.get(type);
                        if (format != null) {
                            return format(format, cal);
                        }
                    } catch (MarshalException e2) {
                        xs.handleEvent(new ValidationEventImpl(0, e2.getMessage(), xs.getCurrentLocation(null)));
                        return "";
                    }
                }
                return cal.toXMLFormat();
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public XMLGregorianCalendar parse(CharSequence lexical) throws SAXException {
                try {
                    return DatatypeConverterImpl.getDatatypeFactory().newXMLGregorianCalendar(lexical.toString().trim());
                } catch (Exception e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            private String format(String format, XMLGregorianCalendar value) {
                StringBuilder buf = new StringBuilder();
                int fidx = 0;
                int flen = format.length();
                while (fidx < flen) {
                    int i2 = fidx;
                    fidx++;
                    char fch = format.charAt(i2);
                    if (fch != '%') {
                        buf.append(fch);
                    } else {
                        fidx++;
                        switch (format.charAt(fidx)) {
                            case 'D':
                                printNumber(buf, value.getDay(), 2);
                                break;
                            case 'M':
                                printNumber(buf, value.getMonth(), 2);
                                break;
                            case 'Y':
                                printNumber(buf, value.getEonAndYear(), 4);
                                break;
                            case 'h':
                                printNumber(buf, value.getHour(), 2);
                                break;
                            case 'm':
                                printNumber(buf, value.getMinute(), 2);
                                break;
                            case 's':
                                printNumber(buf, value.getSecond(), 2);
                                if (value.getFractionalSecond() == null) {
                                    break;
                                } else {
                                    String frac = value.getFractionalSecond().toPlainString();
                                    buf.append(frac.substring(1, frac.length()));
                                    break;
                                }
                            case 'z':
                                int offset = value.getTimezone();
                                if (offset == 0) {
                                    buf.append('Z');
                                    break;
                                } else if (offset != Integer.MIN_VALUE) {
                                    if (offset < 0) {
                                        buf.append('-');
                                        offset *= -1;
                                    } else {
                                        buf.append('+');
                                    }
                                    printNumber(buf, offset / 60, 2);
                                    buf.append(':');
                                    printNumber(buf, offset % 60, 2);
                                    break;
                                } else {
                                    break;
                                }
                            default:
                                throw new InternalError();
                        }
                    }
                }
                return buf.toString();
            }

            private void printNumber(StringBuilder out, BigInteger number, int nDigits) {
                String s2 = number.toString();
                for (int i2 = s2.length(); i2 < nDigits; i2++) {
                    out.append('0');
                }
                out.append(s2);
            }

            private void printNumber(StringBuilder out, int number, int nDigits) {
                String s2 = String.valueOf(number);
                for (int i2 = s2.length(); i2 < nDigits; i2++) {
                    out.append('0');
                }
                out.append(s2);
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public QName getTypeName(XMLGregorianCalendar cal) {
                return cal.getXMLSchemaType();
            }
        });
        ArrayList<RuntimeBuiltinLeafInfoImpl<?>> primaryList = new ArrayList<>();
        primaryList.add(STRING);
        primaryList.add(new StringImpl<Boolean>(Boolean.class, createXS("boolean")) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.14
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Boolean parse(CharSequence text) {
                return DatatypeConverterImpl._parseBoolean(text);
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Boolean v2) {
                return v2.toString();
            }
        });
        primaryList.add(new PcdataImpl<byte[]>(byte[].class, createXS(SchemaSymbols.ATTVAL_BASE64BINARY), createXS(SchemaSymbols.ATTVAL_HEXBINARY)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.15
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public byte[] parse(CharSequence text) {
                return RuntimeBuiltinLeafInfoImpl.decodeBase64(text);
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.PcdataImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public Base64Data print(byte[] v2) {
                XMLSerializer w2 = XMLSerializer.getInstance();
                Base64Data bd2 = new Base64Data();
                String mimeType = w2.getXMIMEContentType();
                bd2.set(v2, mimeType);
                return bd2;
            }
        });
        primaryList.add(new StringImpl<Byte>(Byte.class, createXS(SchemaSymbols.ATTVAL_BYTE)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.16
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Byte parse(CharSequence text) {
                return Byte.valueOf(DatatypeConverterImpl._parseByte(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Byte v2) {
                return DatatypeConverterImpl._printByte(v2.byteValue());
            }
        });
        primaryList.add(new StringImpl<Short>(Short.class, createXS(SchemaSymbols.ATTVAL_SHORT), createXS(SchemaSymbols.ATTVAL_UNSIGNEDBYTE)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.17
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Short parse(CharSequence text) {
                return Short.valueOf(DatatypeConverterImpl._parseShort(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Short v2) {
                return DatatypeConverterImpl._printShort(v2.shortValue());
            }
        });
        primaryList.add(new StringImpl<Integer>(Integer.class, createXS("int"), createXS(SchemaSymbols.ATTVAL_UNSIGNEDSHORT)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.18
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Integer parse(CharSequence text) {
                return Integer.valueOf(DatatypeConverterImpl._parseInt(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Integer v2) {
                return DatatypeConverterImpl._printInt(v2.intValue());
            }
        });
        primaryList.add(new StringImpl<Long>(Long.class, createXS(SchemaSymbols.ATTVAL_LONG), createXS(SchemaSymbols.ATTVAL_UNSIGNEDINT)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.19
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Long parse(CharSequence text) {
                return Long.valueOf(DatatypeConverterImpl._parseLong(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Long v2) {
                return DatatypeConverterImpl._printLong(v2.longValue());
            }
        });
        primaryList.add(new StringImpl<Float>(Float.class, createXS(SchemaSymbols.ATTVAL_FLOAT)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.20
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Float parse(CharSequence text) {
                return Float.valueOf(DatatypeConverterImpl._parseFloat(text.toString()));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Float v2) {
                return DatatypeConverterImpl._printFloat(v2.floatValue());
            }
        });
        primaryList.add(new StringImpl<Double>(Double.class, createXS(SchemaSymbols.ATTVAL_DOUBLE)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.21
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Double parse(CharSequence text) {
                return Double.valueOf(DatatypeConverterImpl._parseDouble(text));
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Double v2) {
                return DatatypeConverterImpl._printDouble(v2.doubleValue());
            }
        });
        primaryList.add(new StringImpl<BigInteger>(BigInteger.class, createXS(SchemaSymbols.ATTVAL_INTEGER), createXS(SchemaSymbols.ATTVAL_POSITIVEINTEGER), createXS(SchemaSymbols.ATTVAL_NEGATIVEINTEGER), createXS(SchemaSymbols.ATTVAL_NONPOSITIVEINTEGER), createXS(SchemaSymbols.ATTVAL_NONNEGATIVEINTEGER), createXS(SchemaSymbols.ATTVAL_UNSIGNEDLONG)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.22
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public BigInteger parse(CharSequence text) {
                return DatatypeConverterImpl._parseInteger(text);
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(BigInteger v2) {
                return DatatypeConverterImpl._printInteger(v2);
            }
        });
        primaryList.add(new StringImpl<BigDecimal>(BigDecimal.class, createXS(SchemaSymbols.ATTVAL_DECIMAL)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.23
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public BigDecimal parse(CharSequence text) {
                return DatatypeConverterImpl._parseDecimal(text.toString());
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(BigDecimal v2) {
                return DatatypeConverterImpl._printDecimal(v2);
            }
        });
        primaryList.add(new StringImpl<QName>(QName.class, createXS(SchemaSymbols.ATTVAL_QNAME)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.24
            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public QName parse(CharSequence text) throws SAXException {
                try {
                    return DatatypeConverterImpl._parseQName(text.toString(), UnmarshallingContext.getInstance());
                } catch (IllegalArgumentException e2) {
                    UnmarshallingContext.getInstance().handleError(e2);
                    return null;
                }
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(QName v2) {
                return DatatypeConverterImpl._printQName(v2, XMLSerializer.getInstance().getNamespaceContext());
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public boolean useNamespace() {
                return true;
            }

            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public void declareNamespace(QName v2, XMLSerializer w2) {
                w2.getNamespaceContext().declareNamespace(v2.getNamespaceURI(), v2.getPrefix(), false);
            }
        });
        if (MAP_ANYURI_TO_URI_VALUE != null) {
            primaryList.add(new StringImpl<URI>(URI.class, createXS(SchemaSymbols.ATTVAL_ANYURI)) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.25
                @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
                public URI parse(CharSequence text) throws SAXException {
                    try {
                        return new URI(text.toString());
                    } catch (URISyntaxException e2) {
                        UnmarshallingContext.getInstance().handleError(e2);
                        return null;
                    }
                }

                @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
                public String print(URI v2) {
                    return v2.toString();
                }
            });
        }
        primaryList.add(new StringImpl<Duration>(Duration.class, createXS("duration")) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.26
            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Duration duration) {
                return duration.toString();
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Duration parse(CharSequence lexical) {
                TODO.checkSpec("JSR222 Issue #42");
                return DatatypeConverterImpl.getDatatypeFactory().newDuration(lexical.toString());
            }
        });
        primaryList.add(new StringImpl<Void>(Void.class, new QName[0]) { // from class: com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.27
            @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
            public String print(Void value) {
                return "";
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
            public Void parse(CharSequence lexical) {
                return null;
            }
        });
        List<RuntimeBuiltinLeafInfoImpl<?>> l2 = new ArrayList<>(secondaryList.size() + primaryList.size() + 1);
        l2.addAll(secondaryList);
        try {
            l2.add(new UUIDImpl());
        } catch (LinkageError e2) {
        }
        l2.addAll(primaryList);
        builtinBeanInfos = Collections.unmodifiableList(l2);
        xmlGregorianCalendarFormatString = new HashMap();
        Map<QName, String> m2 = xmlGregorianCalendarFormatString;
        m2.put(DatatypeConstants.DATETIME, "%Y-%M-%DT%h:%m:%s%z");
        m2.put(DatatypeConstants.DATE, "%Y-%M-%D%z");
        m2.put(DatatypeConstants.TIME, "%h:%m:%s%z");
        m2.put(DatatypeConstants.GMONTH, "--%M--%z");
        m2.put(DatatypeConstants.GDAY, "---%D%z");
        m2.put(DatatypeConstants.GYEAR, "%Y%z");
        m2.put(DatatypeConstants.GYEARMONTH, "%Y-%M%z");
        m2.put(DatatypeConstants.GMONTHDAY, "--%M-%D%z");
        xmlGregorianCalendarFieldRef = new HashMap();
        Map<QName, Integer> f2 = xmlGregorianCalendarFieldRef;
        f2.put(DatatypeConstants.DATETIME, 17895697);
        f2.put(DatatypeConstants.DATE, 17895424);
        f2.put(DatatypeConstants.TIME, 16777489);
        f2.put(DatatypeConstants.GDAY, 16781312);
        f2.put(DatatypeConstants.GMONTH, Integer.valueOf(R.attr.theme));
        f2.put(DatatypeConstants.GYEAR, Integer.valueOf(R.raw.loaderror));
        f2.put(DatatypeConstants.GYEARMONTH, Integer.valueOf(R.bool.config_sendPackageName));
        f2.put(DatatypeConstants.GMONTHDAY, 16846848);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static QName createXS(String typeName) {
        return new QName("http://www.w3.org/2001/XMLSchema", typeName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] decodeBase64(CharSequence text) {
        if (text instanceof Base64Data) {
            Base64Data base64Data = (Base64Data) text;
            return base64Data.getExact();
        }
        return DatatypeConverterImpl._parseBase64Binary(text.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkXmlGregorianCalendarFieldRef(QName type, XMLGregorianCalendar cal) throws MarshalException {
        StringBuilder buf = new StringBuilder();
        int bitField = xmlGregorianCalendarFieldRef.get(type).intValue();
        int pos = 0;
        while (bitField != 0) {
            int bit = bitField & 1;
            bitField >>>= 4;
            pos++;
            if (bit == 1) {
                switch (pos) {
                    case 1:
                        if (cal.getSecond() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_SEC);
                            break;
                        }
                    case 2:
                        if (cal.getMinute() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_MIN);
                            break;
                        }
                    case 3:
                        if (cal.getHour() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_HR);
                            break;
                        }
                    case 4:
                        if (cal.getDay() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_DAY);
                            break;
                        }
                    case 5:
                        if (cal.getMonth() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_MONTH);
                            break;
                        }
                    case 6:
                        if (cal.getYear() != Integer.MIN_VALUE) {
                            break;
                        } else {
                            buf.append(Constants.INDENT).append((Object) Messages.XMLGREGORIANCALENDAR_YEAR);
                            break;
                        }
                }
            }
        }
        if (buf.length() > 0) {
            throw new MarshalException(Messages.XMLGREGORIANCALENDAR_INVALID.format(type.getLocalPart()) + buf.toString());
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$UUIDImpl.class */
    private static class UUIDImpl extends StringImpl<UUID> {
        public UUIDImpl() {
            super(UUID.class, RuntimeBuiltinLeafInfoImpl.createXS("string"));
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public UUID parse(CharSequence text) throws SAXException {
            TODO.checkSpec("JSR222 Issue #42");
            try {
                return UUID.fromString(WhiteSpaceProcessor.trim(text).toString());
            } catch (IllegalArgumentException e2) {
                UnmarshallingContext.getInstance().handleError(e2);
                return null;
            }
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
        public String print(UUID v2) {
            return v2.toString();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeBuiltinLeafInfoImpl$StringImplImpl.class */
    private static class StringImplImpl extends StringImpl<String> {
        public StringImplImpl(Class type, QName[] typeNames) {
            super(type, typeNames);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public String parse(CharSequence text) {
            return text.toString();
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
        public String print(String s2) {
            return s2;
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
        public final void writeText(XMLSerializer w2, String o2, String fieldName) throws SAXException, XMLStreamException, IOException {
            w2.text(o2, fieldName);
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl.StringImpl, com.sun.xml.internal.bind.v2.runtime.Transducer
        public final void writeLeafElement(XMLSerializer w2, Name tagName, String o2, String fieldName) throws XMLStreamException, SAXException, IOException {
            w2.leafElement(tagName, o2, fieldName);
        }
    }
}
