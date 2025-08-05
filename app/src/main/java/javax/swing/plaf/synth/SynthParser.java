package javax.swing.plaf.synth;

import com.sun.beans.decoder.DocumentHandler;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;
import javafx.fxml.FXMLLoader;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ParsedSynthStyle;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import sun.reflect.misc.ReflectUtil;
import sun.swing.plaf.synth.DefaultSynthStyle;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthParser.class */
class SynthParser extends DefaultHandler {
    private static final String ELEMENT_SYNTH = "synth";
    private static final String ELEMENT_STYLE = "style";
    private static final String ELEMENT_STATE = "state";
    private static final String ELEMENT_FONT = "font";
    private static final String ELEMENT_COLOR = "color";
    private static final String ELEMENT_IMAGE_PAINTER = "imagePainter";
    private static final String ELEMENT_PAINTER = "painter";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ELEMENT_SYNTH_GRAPHICS = "graphicsUtils";
    private static final String ELEMENT_IMAGE_ICON = "imageIcon";
    private static final String ELEMENT_BIND = "bind";
    private static final String ELEMENT_BIND_KEY = "bindKey";
    private static final String ELEMENT_INSETS = "insets";
    private static final String ELEMENT_OPAQUE = "opaque";
    private static final String ELEMENT_DEFAULTS_PROPERTY = "defaultsProperty";
    private static final String ELEMENT_INPUT_MAP = "inputMap";
    private static final String ATTRIBUTE_ACTION = "action";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_IDREF = "idref";
    private static final String ATTRIBUTE_CLONE = "clone";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_STYLE = "style";
    private static final String ATTRIBUTE_SIZE = "size";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_TOP = "top";
    private static final String ATTRIBUTE_LEFT = "left";
    private static final String ATTRIBUTE_BOTTOM = "bottom";
    private static final String ATTRIBUTE_RIGHT = "right";
    private static final String ATTRIBUTE_KEY = "key";
    private static final String ATTRIBUTE_SOURCE_INSETS = "sourceInsets";
    private static final String ATTRIBUTE_DEST_INSETS = "destinationInsets";
    private static final String ATTRIBUTE_PATH = "path";
    private static final String ATTRIBUTE_STRETCH = "stretch";
    private static final String ATTRIBUTE_PAINT_CENTER = "paintCenter";
    private static final String ATTRIBUTE_METHOD = "method";
    private static final String ATTRIBUTE_DIRECTION = "direction";
    private static final String ATTRIBUTE_CENTER = "center";
    private DocumentHandler _handler;
    private int _depth;
    private DefaultSynthStyleFactory _factory;
    private ParsedSynthStyle _style;
    private ParsedSynthStyle.StateInfo _stateInfo;
    private String _inputMapID;
    private URL _urlResourceBase;
    private Class<?> _classResourceBase;
    private Map<String, Object> _defaultsMap;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Map<String, Object> _mapping = new HashMap();
    private List<ParsedSynthStyle.StateInfo> _stateInfos = new ArrayList();
    private List<ColorType> _colorTypes = new ArrayList();
    private List<String> _inputMapBindings = new ArrayList();
    private List<ParsedSynthStyle.PainterInfo> _stylePainters = new ArrayList();
    private List<ParsedSynthStyle.PainterInfo> _statePainters = new ArrayList();

    static {
        $assertionsDisabled = !SynthParser.class.desiredAssertionStatus();
    }

    SynthParser() {
    }

    public void parse(InputStream inputStream, DefaultSynthStyleFactory defaultSynthStyleFactory, URL url, Class<?> cls, Map<String, Object> map) throws ParseException, IllegalArgumentException {
        if (inputStream == null || defaultSynthStyleFactory == null || (url == null && cls == null)) {
            throw new IllegalArgumentException("You must supply an InputStream, StyleFactory and Class or URL");
        }
        if (!$assertionsDisabled && url != null && cls != null) {
            throw new AssertionError();
        }
        this._factory = defaultSynthStyleFactory;
        this._classResourceBase = cls;
        this._urlResourceBase = url;
        this._defaultsMap = map;
        try {
            try {
                try {
                    SAXParserFactory.newInstance().newSAXParser().parse(new BufferedInputStream(inputStream), this);
                } catch (ParserConfigurationException e2) {
                    throw new ParseException("Error parsing: " + ((Object) e2), 0);
                } catch (SAXException e3) {
                    throw new ParseException("Error parsing: " + ((Object) e3) + " " + ((Object) e3.getException()), 0);
                }
            } catch (IOException e4) {
                throw new ParseException("Error parsing: " + ((Object) e4), 0);
            }
        } finally {
            reset();
        }
    }

    private URL getResource(String str) {
        if (this._classResourceBase != null) {
            return this._classResourceBase.getResource(str);
        }
        try {
            return new URL(this._urlResourceBase, str);
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    private void reset() {
        this._handler = null;
        this._depth = 0;
        this._mapping.clear();
        this._stateInfos.clear();
        this._colorTypes.clear();
        this._statePainters.clear();
        this._stylePainters.clear();
    }

    private boolean isForwarding() {
        return this._depth > 0;
    }

    private DocumentHandler getHandler() {
        if (this._handler == null) {
            this._handler = new DocumentHandler();
            if (this._urlResourceBase != null) {
                this._handler.setClassLoader(new URLClassLoader(new URL[]{getResource(".")}, Thread.currentThread().getContextClassLoader()));
            } else {
                this._handler.setClassLoader(this._classResourceBase.getClassLoader());
            }
            for (String str : this._mapping.keySet()) {
                this._handler.setVariable(str, this._mapping.get(str));
            }
        }
        return this._handler;
    }

    private Object checkCast(Object obj, Class cls) throws SAXException {
        if (!cls.isInstance(obj)) {
            throw new SAXException("Expected type " + ((Object) cls) + " got " + ((Object) obj.getClass()));
        }
        return obj;
    }

    private Object lookup(String str, Class cls) throws SAXException {
        if (this._handler != null && this._handler.hasVariable(str)) {
            return checkCast(this._handler.getVariable(str), cls);
        }
        Object obj = this._mapping.get(str);
        if (obj == null) {
            throw new SAXException("ID " + str + " has not been defined");
        }
        return checkCast(obj, cls);
    }

    private void register(String str, Object obj) throws SAXException {
        if (str != null) {
            if (this._mapping.get(str) != null || (this._handler != null && this._handler.hasVariable(str))) {
                throw new SAXException("ID " + str + " is already defined");
            }
            if (this._handler != null) {
                this._handler.setVariable(str, obj);
            } else {
                this._mapping.put(str, obj);
            }
        }
    }

    private int nextInt(StringTokenizer stringTokenizer, String str) throws SAXException {
        if (!stringTokenizer.hasMoreTokens()) {
            throw new SAXException(str);
        }
        try {
            return Integer.parseInt(stringTokenizer.nextToken());
        } catch (NumberFormatException e2) {
            throw new SAXException(str);
        }
    }

    private Insets parseInsets(String str, String str2) throws SAXException {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        return new Insets(nextInt(stringTokenizer, str2), nextInt(stringTokenizer, str2), nextInt(stringTokenizer, str2), nextInt(stringTokenizer, str2));
    }

    private void startStyle(Attributes attributes) throws SAXException {
        String value = null;
        this._style = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals(ATTRIBUTE_CLONE)) {
                this._style = (ParsedSynthStyle) ((ParsedSynthStyle) lookup(attributes.getValue(length), ParsedSynthStyle.class)).clone();
            } else if (qName.equals("id")) {
                value = attributes.getValue(length);
            }
        }
        if (this._style == null) {
            this._style = new ParsedSynthStyle();
        }
        register(value, this._style);
    }

    private void endStyle() {
        int size = this._stylePainters.size();
        if (size > 0) {
            this._style.setPainters((ParsedSynthStyle.PainterInfo[]) this._stylePainters.toArray(new ParsedSynthStyle.PainterInfo[size]));
            this._stylePainters.clear();
        }
        int size2 = this._stateInfos.size();
        if (size2 > 0) {
            this._style.setStateInfo((DefaultSynthStyle.StateInfo[]) this._stateInfos.toArray(new ParsedSynthStyle.StateInfo[size2]));
            this._stateInfos.clear();
        }
        this._style = null;
    }

    private void startState(Attributes attributes) throws SAXException {
        int i2 = 0;
        String value = null;
        this._stateInfo = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals("id")) {
                value = attributes.getValue(length);
            } else if (qName.equals(ATTRIBUTE_IDREF)) {
                this._stateInfo = (ParsedSynthStyle.StateInfo) lookup(attributes.getValue(length), ParsedSynthStyle.StateInfo.class);
            } else if (qName.equals(ATTRIBUTE_CLONE)) {
                this._stateInfo = (ParsedSynthStyle.StateInfo) ((ParsedSynthStyle.StateInfo) lookup(attributes.getValue(length), ParsedSynthStyle.StateInfo.class)).clone();
            } else if (qName.equals("value")) {
                StringTokenizer stringTokenizer = new StringTokenizer(attributes.getValue(length));
                while (stringTokenizer.hasMoreTokens()) {
                    String strIntern = stringTokenizer.nextToken().toUpperCase().intern();
                    if (strIntern == "ENABLED") {
                        i2 |= 1;
                    } else if (strIntern == "MOUSE_OVER") {
                        i2 |= 2;
                    } else if (strIntern == "PRESSED") {
                        i2 |= 4;
                    } else if (strIntern == "DISABLED") {
                        i2 |= 8;
                    } else if (strIntern == "FOCUSED") {
                        i2 |= 256;
                    } else if (strIntern == "SELECTED") {
                        i2 |= 512;
                    } else if (strIntern == "DEFAULT") {
                        i2 |= 1024;
                    } else if (strIntern != "AND") {
                        throw new SAXException("Unknown state: " + i2);
                    }
                }
            } else {
                continue;
            }
        }
        if (this._stateInfo == null) {
            this._stateInfo = new ParsedSynthStyle.StateInfo();
        }
        this._stateInfo.setComponentState(i2);
        register(value, this._stateInfo);
        this._stateInfos.add(this._stateInfo);
    }

    private void endState() {
        int size = this._statePainters.size();
        if (size > 0) {
            this._stateInfo.setPainters((ParsedSynthStyle.PainterInfo[]) this._statePainters.toArray(new ParsedSynthStyle.PainterInfo[size]));
            this._statePainters.clear();
        }
        this._stateInfo = null;
    }

    private void startFont(Attributes attributes) throws SAXException {
        Font fontUIResource = null;
        int i2 = 0;
        int i3 = 0;
        String value = null;
        String value2 = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals("id")) {
                value = attributes.getValue(length);
            } else if (qName.equals(ATTRIBUTE_IDREF)) {
                fontUIResource = (Font) lookup(attributes.getValue(length), Font.class);
            } else if (qName.equals("name")) {
                value2 = attributes.getValue(length);
            } else if (qName.equals(ATTRIBUTE_SIZE)) {
                try {
                    i3 = Integer.parseInt(attributes.getValue(length));
                } catch (NumberFormatException e2) {
                    throw new SAXException("Invalid font size: " + attributes.getValue(length));
                }
            } else if (qName.equals(Constants.ATTRNAME_STYLE)) {
                StringTokenizer stringTokenizer = new StringTokenizer(attributes.getValue(length));
                while (stringTokenizer.hasMoreTokens()) {
                    String strIntern = stringTokenizer.nextToken().intern();
                    if (strIntern == "BOLD") {
                        i2 = ((i2 | 0) ^ 0) | 1;
                    } else if (strIntern == "ITALIC") {
                        i2 |= 2;
                    }
                }
            }
        }
        if (fontUIResource == null) {
            if (value2 == null) {
                throw new SAXException("You must define a name for the font");
            }
            if (i3 == 0) {
                throw new SAXException("You must define a size for the font");
            }
            fontUIResource = new FontUIResource(value2, i2, i3);
        } else if (value2 != null || i3 != 0 || i2 != 0) {
            throw new SAXException("Name, size and style are not for use with idref");
        }
        register(value, fontUIResource);
        if (this._stateInfo != null) {
            this._stateInfo.setFont(fontUIResource);
        } else if (this._style != null) {
            this._style.setFont(fontUIResource);
        }
    }

    private void startColor(Attributes attributes) throws SAXException {
        Class<?> clsForName;
        int i2;
        int iIntValue;
        boolean z2;
        Color colorUIResource = null;
        String value = null;
        this._colorTypes.clear();
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals("id")) {
                value = attributes.getValue(length);
            } else if (qName.equals(ATTRIBUTE_IDREF)) {
                colorUIResource = (Color) lookup(attributes.getValue(length), Color.class);
            } else if (qName.equals("name")) {
                continue;
            } else if (qName.equals("value")) {
                String value2 = attributes.getValue(length);
                if (value2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                    try {
                        int length2 = value2.length();
                        if (length2 < 8) {
                            iIntValue = Integer.decode(value2).intValue();
                            z2 = false;
                        } else if (length2 == 8) {
                            iIntValue = Integer.decode(value2).intValue();
                            z2 = true;
                        } else if (length2 == 9) {
                            iIntValue = (Integer.decode(value2.substring(0, 3)).intValue() << 24) | Integer.decode('#' + value2.substring(3, 9)).intValue();
                            z2 = true;
                        } else {
                            throw new SAXException("Invalid Color value: " + value2);
                        }
                        colorUIResource = new ColorUIResource(new Color(iIntValue, z2));
                    } catch (NumberFormatException e2) {
                        throw new SAXException("Invalid Color value: " + value2);
                    }
                } else {
                    try {
                        colorUIResource = new ColorUIResource((Color) Color.class.getField(value2.toUpperCase()).get(Color.class));
                    } catch (IllegalAccessException e3) {
                        throw new SAXException("Invalid color name: " + value2);
                    } catch (NoSuchFieldException e4) {
                        throw new SAXException("Invalid color name: " + value2);
                    }
                }
            } else if (qName.equals("type")) {
                StringTokenizer stringTokenizer = new StringTokenizer(attributes.getValue(length));
                while (stringTokenizer.hasMoreTokens()) {
                    String strNextToken = stringTokenizer.nextToken();
                    int iLastIndexOf = strNextToken.lastIndexOf(46);
                    if (iLastIndexOf == -1) {
                        clsForName = ColorType.class;
                        i2 = 0;
                    } else {
                        try {
                            clsForName = ReflectUtil.forName(strNextToken.substring(0, iLastIndexOf));
                            i2 = iLastIndexOf + 1;
                        } catch (ClassNotFoundException e5) {
                            throw new SAXException("Unknown class: " + strNextToken.substring(0, iLastIndexOf));
                        }
                    }
                    try {
                        this._colorTypes.add((ColorType) checkCast(clsForName.getField(strNextToken.substring(i2)).get(clsForName), ColorType.class));
                    } catch (IllegalAccessException e6) {
                        throw new SAXException("Unable to find color type: " + strNextToken);
                    } catch (NoSuchFieldException e7) {
                        throw new SAXException("Unable to find color type: " + strNextToken);
                    }
                }
            } else {
                continue;
            }
        }
        if (colorUIResource == null) {
            throw new SAXException("color: you must specificy a value");
        }
        register(value, colorUIResource);
        if (this._stateInfo != null && this._colorTypes.size() > 0) {
            Color[] colors = this._stateInfo.getColors();
            int iMax = 0;
            for (int size = this._colorTypes.size() - 1; size >= 0; size--) {
                iMax = Math.max(iMax, this._colorTypes.get(size).getID());
            }
            if (colors == null || colors.length <= iMax) {
                Color[] colorArr = new Color[iMax + 1];
                if (colors != null) {
                    System.arraycopy(colors, 0, colorArr, 0, colors.length);
                }
                colors = colorArr;
            }
            for (int size2 = this._colorTypes.size() - 1; size2 >= 0; size2--) {
                colors[this._colorTypes.get(size2).getID()] = colorUIResource;
            }
            this._stateInfo.setColors(colors);
        }
    }

    private void startProperty(Attributes attributes, Object obj) throws SAXException {
        Object num = null;
        String value = null;
        boolean z2 = false;
        String value2 = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals("type")) {
                String upperCase = attributes.getValue(length).toUpperCase();
                if (upperCase.equals(SchemaSymbols.ATTVAL_IDREF)) {
                    z2 = false;
                } else if (upperCase.equals("BOOLEAN")) {
                    z2 = true;
                } else if (upperCase.equals("DIMENSION")) {
                    z2 = 2;
                } else if (upperCase.equals("INSETS")) {
                    z2 = 3;
                } else if (upperCase.equals("INTEGER")) {
                    z2 = 4;
                } else if (upperCase.equals("STRING")) {
                    z2 = 5;
                } else {
                    throw new SAXException(obj + " unknown type, useidref, boolean, dimension, insets or integer");
                }
            } else if (qName.equals("value")) {
                value2 = attributes.getValue(length);
            } else if (qName.equals("key")) {
                value = attributes.getValue(length);
            }
        }
        if (value2 != null) {
            switch (z2) {
                case false:
                    num = lookup(value2, Object.class);
                    break;
                case true:
                    if (value2.toUpperCase().equals("TRUE")) {
                        num = Boolean.TRUE;
                        break;
                    } else {
                        num = Boolean.FALSE;
                        break;
                    }
                case true:
                    StringTokenizer stringTokenizer = new StringTokenizer(value2);
                    num = new DimensionUIResource(nextInt(stringTokenizer, "Invalid dimension"), nextInt(stringTokenizer, "Invalid dimension"));
                    break;
                case true:
                    num = parseInsets(value2, obj + " invalid insets");
                    break;
                case true:
                    try {
                        num = new Integer(Integer.parseInt(value2));
                        break;
                    } catch (NumberFormatException e2) {
                        throw new SAXException(obj + " invalid value");
                    }
                case true:
                    num = value2;
                    break;
            }
        }
        if (num == null || value == null) {
            throw new SAXException(obj + ": you must supply a key and value");
        }
        if (obj == ELEMENT_DEFAULTS_PROPERTY) {
            this._defaultsMap.put(value, num);
            return;
        }
        if (this._stateInfo != null) {
            if (this._stateInfo.getData() == null) {
                this._stateInfo.setData(new HashMap());
            }
            this._stateInfo.getData().put(value, num);
        } else if (this._style != null) {
            if (this._style.getData() == null) {
                this._style.setData(new HashMap());
            }
            this._style.getData().put(value, num);
        }
    }

    private void startGraphics(Attributes attributes) throws SAXException {
        SynthGraphicsUtils synthGraphicsUtils = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            if (attributes.getQName(length).equals(ATTRIBUTE_IDREF)) {
                synthGraphicsUtils = (SynthGraphicsUtils) lookup(attributes.getValue(length), SynthGraphicsUtils.class);
            }
        }
        if (synthGraphicsUtils == null) {
            throw new SAXException("graphicsUtils: you must supply an idref");
        }
        if (this._style != null) {
            this._style.setGraphicsUtils(synthGraphicsUtils);
        }
    }

    private void startInsets(Attributes attributes) throws SAXException {
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        Insets insetsUIResource = null;
        String value = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            try {
                if (qName.equals(ATTRIBUTE_IDREF)) {
                    insetsUIResource = (Insets) lookup(attributes.getValue(length), Insets.class);
                } else if (qName.equals("id")) {
                    value = attributes.getValue(length);
                } else if (qName.equals("top")) {
                    i2 = Integer.parseInt(attributes.getValue(length));
                } else if (qName.equals("left")) {
                    i4 = Integer.parseInt(attributes.getValue(length));
                } else if (qName.equals("bottom")) {
                    i3 = Integer.parseInt(attributes.getValue(length));
                } else if (qName.equals("right")) {
                    i5 = Integer.parseInt(attributes.getValue(length));
                }
            } catch (NumberFormatException e2) {
                throw new SAXException("insets: bad integer value for " + attributes.getValue(length));
            }
        }
        if (insetsUIResource == null) {
            insetsUIResource = new InsetsUIResource(i2, i4, i3, i5);
        }
        register(value, insetsUIResource);
        if (this._style != null) {
            this._style.setInsets(insetsUIResource);
        }
    }

    private void startBind(Attributes attributes) throws SAXException {
        ParsedSynthStyle parsedSynthStyle = null;
        String value = null;
        int i2 = -1;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals(Constants.ATTRNAME_STYLE)) {
                parsedSynthStyle = (ParsedSynthStyle) lookup(attributes.getValue(length), ParsedSynthStyle.class);
            } else if (qName.equals("type")) {
                String upperCase = attributes.getValue(length).toUpperCase();
                if (upperCase.equals("NAME")) {
                    i2 = 0;
                } else if (upperCase.equals("REGION")) {
                    i2 = 1;
                } else {
                    throw new SAXException("bind: unknown type " + upperCase);
                }
            } else if (qName.equals("key")) {
                value = attributes.getValue(length);
            }
        }
        if (parsedSynthStyle == null || value == null || i2 == -1) {
            throw new SAXException("bind: you must specify a style, type and key");
        }
        try {
            this._factory.addStyle(parsedSynthStyle, value, i2);
        } catch (PatternSyntaxException e2) {
            throw new SAXException("bind: " + value + " is not a valid regular expression");
        }
    }

    private void startPainter(Attributes attributes, String str) throws SAXException {
        Insets insets = null;
        Insets insets2 = null;
        String str2 = null;
        boolean zEquals = true;
        boolean zEquals2 = true;
        SynthPainter imagePainter = null;
        String lowerCase = null;
        String str3 = null;
        int i2 = -1;
        boolean zEquals3 = false;
        boolean z2 = false;
        boolean z3 = false;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            String value = attributes.getValue(length);
            if (qName.equals("id")) {
                str3 = value;
            } else if (qName.equals("method")) {
                lowerCase = value.toLowerCase(Locale.ENGLISH);
            } else if (qName.equals(ATTRIBUTE_IDREF)) {
                imagePainter = (SynthPainter) lookup(value, SynthPainter.class);
            } else if (qName.equals(ATTRIBUTE_PATH)) {
                str2 = value;
            } else if (qName.equals(ATTRIBUTE_SOURCE_INSETS)) {
                insets = parseInsets(value, str + ": sourceInsets must be top left bottom right");
            } else if (qName.equals(ATTRIBUTE_DEST_INSETS)) {
                insets2 = parseInsets(value, str + ": destinationInsets must be top left bottom right");
            } else if (qName.equals(ATTRIBUTE_PAINT_CENTER)) {
                zEquals = value.toLowerCase().equals("true");
                z3 = true;
            } else if (qName.equals(ATTRIBUTE_STRETCH)) {
                zEquals2 = value.toLowerCase().equals("true");
                z2 = true;
            } else if (qName.equals(ATTRIBUTE_DIRECTION)) {
                String strIntern = value.toUpperCase().intern();
                if (strIntern == "EAST") {
                    i2 = 3;
                } else if (strIntern == "NORTH") {
                    i2 = 1;
                } else if (strIntern == "SOUTH") {
                    i2 = 5;
                } else if (strIntern == "WEST") {
                    i2 = 7;
                } else if (strIntern == "TOP") {
                    i2 = 1;
                } else if (strIntern == "LEFT") {
                    i2 = 2;
                } else if (strIntern == "BOTTOM") {
                    i2 = 3;
                } else if (strIntern == "RIGHT") {
                    i2 = 4;
                } else if (strIntern == "HORIZONTAL") {
                    i2 = 0;
                } else if (strIntern == "VERTICAL") {
                    i2 = 1;
                } else if (strIntern == "HORIZONTAL_SPLIT") {
                    i2 = 1;
                } else if (strIntern == "VERTICAL_SPLIT") {
                    i2 = 0;
                } else {
                    throw new SAXException(str + ": unknown direction");
                }
            } else if (qName.equals(ATTRIBUTE_CENTER)) {
                zEquals3 = value.toLowerCase().equals("true");
            }
        }
        if (imagePainter == null) {
            if (str == ELEMENT_PAINTER) {
                throw new SAXException(str + ": you must specify an idref");
            }
            if (insets == null && !zEquals3) {
                throw new SAXException("property: you must specify sourceInsets");
            }
            if (str2 == null) {
                throw new SAXException("property: you must specify a path");
            }
            if (zEquals3 && (insets != null || insets2 != null || z3 || z2)) {
                throw new SAXException("The attributes: sourceInsets, destinationInsets, paintCenter and stretch  are not legal when center is true");
            }
            imagePainter = new ImagePainter(!zEquals2, zEquals, insets, insets2, getResource(str2), zEquals3);
        }
        register(str3, imagePainter);
        if (this._stateInfo != null) {
            addPainterOrMerge(this._statePainters, lowerCase, imagePainter, i2);
        } else if (this._style != null) {
            addPainterOrMerge(this._stylePainters, lowerCase, imagePainter, i2);
        }
    }

    private void addPainterOrMerge(List<ParsedSynthStyle.PainterInfo> list, String str, SynthPainter synthPainter, int i2) {
        ParsedSynthStyle.PainterInfo painterInfo = new ParsedSynthStyle.PainterInfo(str, synthPainter, i2);
        for (ParsedSynthStyle.PainterInfo painterInfo2 : list) {
            if (painterInfo.equalsPainter(painterInfo2)) {
                painterInfo2.addPainter(synthPainter);
                return;
            }
        }
        list.add(painterInfo);
    }

    private void startImageIcon(Attributes attributes) throws SAXException {
        String value = null;
        String value2 = null;
        for (int length = attributes.getLength() - 1; length >= 0; length--) {
            String qName = attributes.getQName(length);
            if (qName.equals("id")) {
                value2 = attributes.getValue(length);
            } else if (qName.equals(ATTRIBUTE_PATH)) {
                value = attributes.getValue(length);
            }
        }
        if (value == null) {
            throw new SAXException("imageIcon: you must specify a path");
        }
        register(value2, new LazyImageIcon(getResource(value)));
    }

    private void startOpaque(Attributes attributes) {
        if (this._style != null) {
            this._style.setOpaque(true);
            for (int length = attributes.getLength() - 1; length >= 0; length--) {
                if (attributes.getQName(length).equals("value")) {
                    this._style.setOpaque("true".equals(attributes.getValue(length).toLowerCase()));
                }
            }
        }
    }

    private void startInputMap(Attributes attributes) throws SAXException {
        this._inputMapBindings.clear();
        this._inputMapID = null;
        if (this._style != null) {
            for (int length = attributes.getLength() - 1; length >= 0; length--) {
                if (attributes.getQName(length).equals("id")) {
                    this._inputMapID = attributes.getValue(length);
                }
            }
        }
    }

    private void endInputMap() throws SAXException {
        if (this._inputMapID != null) {
            register(this._inputMapID, new UIDefaults.LazyInputMap(this._inputMapBindings.toArray(new Object[this._inputMapBindings.size()])));
        }
        this._inputMapBindings.clear();
        this._inputMapID = null;
    }

    private void startBindKey(Attributes attributes) throws SAXException {
        if (this._inputMapID != null && this._style != null) {
            String value = null;
            String value2 = null;
            for (int length = attributes.getLength() - 1; length >= 0; length--) {
                String qName = attributes.getQName(length);
                if (qName.equals("key")) {
                    value = attributes.getValue(length);
                } else if (qName.equals(ATTRIBUTE_ACTION)) {
                    value2 = attributes.getValue(length);
                }
            }
            if (value == null || value2 == null) {
                throw new SAXException("bindKey: you must supply a key and action");
            }
            this._inputMapBindings.add(value);
            this._inputMapBindings.add(value2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) throws SAXException, IOException {
        if (isForwarding()) {
            return getHandler().resolveEntity(str, str2);
        }
        return null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void notationDecl(String str, String str2, String str3) throws SAXException {
        if (isForwarding()) {
            getHandler().notationDecl(str, str2, str3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String str, String str2, String str3, String str4) throws SAXException {
        if (isForwarding()) {
            getHandler().unparsedEntityDecl(str, str2, str3, str4);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        if (isForwarding()) {
            getHandler().setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        if (isForwarding()) {
            getHandler().startDocument();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (isForwarding()) {
            getHandler().endDocument();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        String strIntern = str3.intern();
        if (strIntern == Constants.ATTRNAME_STYLE) {
            startStyle(attributes);
            return;
        }
        if (strIntern == ELEMENT_STATE) {
            startState(attributes);
            return;
        }
        if (strIntern == ELEMENT_FONT) {
            startFont(attributes);
            return;
        }
        if (strIntern == ELEMENT_COLOR) {
            startColor(attributes);
            return;
        }
        if (strIntern == ELEMENT_PAINTER) {
            startPainter(attributes, strIntern);
            return;
        }
        if (strIntern == ELEMENT_IMAGE_PAINTER) {
            startPainter(attributes, strIntern);
            return;
        }
        if (strIntern == ELEMENT_PROPERTY) {
            startProperty(attributes, ELEMENT_PROPERTY);
            return;
        }
        if (strIntern == ELEMENT_DEFAULTS_PROPERTY) {
            startProperty(attributes, ELEMENT_DEFAULTS_PROPERTY);
            return;
        }
        if (strIntern == ELEMENT_SYNTH_GRAPHICS) {
            startGraphics(attributes);
            return;
        }
        if (strIntern == ELEMENT_INSETS) {
            startInsets(attributes);
            return;
        }
        if (strIntern == ELEMENT_BIND) {
            startBind(attributes);
            return;
        }
        if (strIntern == ELEMENT_BIND_KEY) {
            startBindKey(attributes);
            return;
        }
        if (strIntern == ELEMENT_IMAGE_ICON) {
            startImageIcon(attributes);
            return;
        }
        if (strIntern == ELEMENT_OPAQUE) {
            startOpaque(attributes);
            return;
        }
        if (strIntern == ELEMENT_INPUT_MAP) {
            startInputMap(attributes);
            return;
        }
        if (strIntern != ELEMENT_SYNTH) {
            int i2 = this._depth;
            this._depth = i2 + 1;
            if (i2 == 0) {
                getHandler().startDocument();
            }
            getHandler().startElement(str, str2, strIntern, attributes);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        if (isForwarding()) {
            getHandler().endElement(str, str2, str3);
            this._depth--;
            if (!isForwarding()) {
                getHandler().startDocument();
                return;
            }
            return;
        }
        String strIntern = str3.intern();
        if (strIntern == Constants.ATTRNAME_STYLE) {
            endStyle();
        } else if (strIntern == ELEMENT_STATE) {
            endState();
        } else if (strIntern == ELEMENT_INPUT_MAP) {
            endInputMap();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) throws SAXException {
        if (isForwarding()) {
            getHandler().characters(cArr, i2, i3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int i2, int i3) throws SAXException {
        if (isForwarding()) {
            getHandler().ignorableWhitespace(cArr, i2, i3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String str, String str2) throws SAXException {
        if (isForwarding()) {
            getHandler().processingInstruction(str, str2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
        if (isForwarding()) {
            getHandler().warning(sAXParseException);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        if (isForwarding()) {
            getHandler().error(sAXParseException);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        if (isForwarding()) {
            getHandler().fatalError(sAXParseException);
        }
        throw sAXParseException;
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthParser$LazyImageIcon.class */
    private static class LazyImageIcon extends ImageIcon implements UIResource {
        private URL location;

        public LazyImageIcon(URL url) {
            this.location = url;
        }

        @Override // javax.swing.ImageIcon, javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (getImage() != null) {
                super.paintIcon(component, graphics, i2, i3);
            }
        }

        @Override // javax.swing.ImageIcon, javax.swing.Icon
        public int getIconWidth() {
            if (getImage() != null) {
                return super.getIconWidth();
            }
            return 0;
        }

        @Override // javax.swing.ImageIcon, javax.swing.Icon
        public int getIconHeight() {
            if (getImage() != null) {
                return super.getIconHeight();
            }
            return 0;
        }

        @Override // javax.swing.ImageIcon
        public Image getImage() {
            if (this.location != null) {
                setImage(Toolkit.getDefaultToolkit().getImage(this.location));
                this.location = null;
            }
            return super.getImage();
        }
    }
}
