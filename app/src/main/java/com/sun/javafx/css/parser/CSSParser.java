package com.sun.javafx.css.parser;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.CompoundSelector;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javafx.css.ParsedValue;
import javafx.css.Styleable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javax.swing.JSplitPane;
import javax.swing.plaf.nimbus.NimbusStyle;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.PdfOps;
import sun.font.Font2D;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSParser.class */
public final class CSSParser {
    private String stylesheetAsText;
    private String sourceOfStylesheet;
    private Styleable sourceOfInlineStyle;
    private static final PlatformLogger LOGGER;
    private static final ParsedValueImpl<Size, Size> ZERO_PERCENT;
    private static final ParsedValueImpl<Size, Size> FIFTY_PERCENT;
    private static final ParsedValueImpl<Size, Size> ONE_HUNDRED_PERCENT;
    public static final String SPECIAL_REGION_URL_PREFIX = "SPECIAL-REGION-URL:";
    private static Stack<String> imports;
    static final /* synthetic */ boolean $assertionsDisabled;
    Token currentToken = null;
    private final Map<String, String> properties = new HashMap();

    static {
        $assertionsDisabled = !CSSParser.class.desiredAssertionStatus();
        LOGGER = Logging.getCSSLogger();
        ZERO_PERCENT = new ParsedValueImpl<>(new Size(0.0d, SizeUnits.PERCENT), null);
        FIFTY_PERCENT = new ParsedValueImpl<>(new Size(50.0d, SizeUnits.PERCENT), null);
        ONE_HUNDRED_PERCENT = new ParsedValueImpl<>(new Size(100.0d, SizeUnits.PERCENT), null);
    }

    @Deprecated
    public static CSSParser getInstance() {
        return new CSSParser();
    }

    private void setInputSource(String url, String str) {
        this.stylesheetAsText = str;
        this.sourceOfStylesheet = url;
        this.sourceOfInlineStyle = null;
    }

    private void setInputSource(String str) {
        this.stylesheetAsText = str;
        this.sourceOfStylesheet = null;
        this.sourceOfInlineStyle = null;
    }

    private void setInputSource(Styleable styleable) {
        this.stylesheetAsText = styleable != null ? styleable.getStyle() : null;
        this.sourceOfStylesheet = null;
        this.sourceOfInlineStyle = styleable;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSParser$ParseException.class */
    private static final class ParseException extends Exception {
        private final Token tok;
        private final String source;

        ParseException(String message) {
            this(message, null, null);
        }

        ParseException(String message, Token tok, CSSParser parser) {
            super(message);
            this.tok = tok;
            if (parser.sourceOfStylesheet == null) {
                if (parser.sourceOfInlineStyle == null) {
                    if (parser.stylesheetAsText != null) {
                        this.source = parser.stylesheetAsText;
                        return;
                    } else {
                        this.source = "?";
                        return;
                    }
                }
                this.source = parser.sourceOfInlineStyle.toString();
                return;
            }
            this.source = parser.sourceOfStylesheet;
        }

        @Override // java.lang.Throwable
        public String toString() {
            StringBuilder builder = new StringBuilder(super.getMessage());
            builder.append(this.source);
            if (this.tok != null) {
                builder.append(": ").append(this.tok.toString());
            }
            return builder.toString();
        }
    }

    public Stylesheet parse(String stylesheetText) {
        Stylesheet stylesheet = new Stylesheet();
        if (stylesheetText != null && !stylesheetText.trim().isEmpty()) {
            setInputSource(stylesheetText);
            try {
                Reader reader = new CharArrayReader(stylesheetText.toCharArray());
                Throwable th = null;
                try {
                    try {
                        parse(stylesheet, reader);
                        if (reader != null) {
                            if (0 != 0) {
                                try {
                                    reader.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                reader.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (IOException e2) {
            }
        }
        return stylesheet;
    }

    public Stylesheet parse(String docbase, String stylesheetText) throws IOException {
        Stylesheet stylesheet = new Stylesheet(docbase);
        if (stylesheetText != null && !stylesheetText.trim().isEmpty()) {
            setInputSource(docbase, stylesheetText);
            Reader reader = new CharArrayReader(stylesheetText.toCharArray());
            Throwable th = null;
            try {
                try {
                    parse(stylesheet, reader);
                    if (reader != null) {
                        if (0 != 0) {
                            try {
                                reader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            reader.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th3) {
                if (reader != null) {
                    if (th != null) {
                        try {
                            reader.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        reader.close();
                    }
                }
                throw th3;
            }
        }
        return stylesheet;
    }

    public Stylesheet parse(URL url) throws IOException {
        String path = url != null ? url.toExternalForm() : null;
        Stylesheet stylesheet = new Stylesheet(path);
        if (url != null) {
            setInputSource(path, null);
            Reader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Throwable th = null;
            try {
                try {
                    parse(stylesheet, reader);
                    if (reader != null) {
                        if (0 != 0) {
                            try {
                                reader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            reader.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th3) {
                if (reader != null) {
                    if (th != null) {
                        try {
                            reader.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        reader.close();
                    }
                }
                throw th3;
            }
        }
        return stylesheet;
    }

    private void parse(Stylesheet stylesheet, Reader reader) {
        CSSLexer lex = new CSSLexer();
        lex.setReader(reader);
        try {
            parse(stylesheet, lex);
        } catch (Exception ex) {
            reportException(ex);
        }
    }

    public Stylesheet parseInlineStyle(Styleable node) {
        Reader reader;
        Throwable th;
        Stylesheet stylesheet = new Stylesheet();
        String stylesheetText = node != null ? node.getStyle() : null;
        if (stylesheetText != null && !stylesheetText.trim().isEmpty()) {
            setInputSource(node);
            List<Rule> rules = new ArrayList<>();
            try {
                reader = new CharArrayReader(stylesheetText.toCharArray());
                th = null;
            } catch (IOException e2) {
            } catch (Exception ex) {
                reportException(ex);
            }
            try {
                try {
                    CSSLexer lexer = CSSLexer.getInstance();
                    lexer.setReader(reader);
                    this.currentToken = nextToken(lexer);
                    List<Declaration> declarations = declarations(lexer);
                    if (declarations != null && !declarations.isEmpty()) {
                        Selector selector = Selector.getUniversalSelector();
                        Rule rule = new Rule(Collections.singletonList(selector), declarations);
                        rules.add(rule);
                    }
                    if (reader != null) {
                        if (0 != 0) {
                            try {
                                reader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            reader.close();
                        }
                    }
                    stylesheet.getRules().addAll(rules);
                } finally {
                }
            } finally {
            }
        }
        setInputSource((Styleable) null);
        return stylesheet;
    }

    public ParsedValueImpl parseExpr(String property, String expr) {
        Reader reader;
        Throwable th;
        if (property == null || expr == null) {
            return null;
        }
        ParsedValueImpl value = null;
        setInputSource(null, property + ": " + expr);
        char[] buf = new char[expr.length() + 1];
        System.arraycopy(expr.toCharArray(), 0, buf, 0, expr.length());
        buf[buf.length - 1] = ';';
        try {
            reader = new CharArrayReader(buf);
            th = null;
        } catch (ParseException e2) {
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(PdfOps.DOUBLE_QUOTE__TOKEN + property + ": " + expr + "\" " + e2.toString());
            }
        } catch (IOException e3) {
        } catch (Exception ex) {
            reportException(ex);
        }
        try {
            CSSLexer lex = CSSLexer.getInstance();
            lex.setReader(reader);
            this.currentToken = nextToken(lex);
            Term term = expr(lex);
            value = valueFor(property, term, lex);
            if (reader != null) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    reader.close();
                }
            }
            return value;
        } catch (Throwable th3) {
            if (reader != null) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    reader.close();
                }
            }
            throw th3;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSParser$Term.class */
    static class Term {
        final Token token;
        Term nextInSeries;
        Term nextLayer;
        Term firstArg;
        Term nextArg;

        Term(Token token) {
            this.token = token;
            this.nextLayer = null;
            this.nextInSeries = null;
            this.firstArg = null;
            this.nextArg = null;
        }

        Term() {
            this(null);
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            if (this.token != null) {
                buf.append(String.valueOf(this.token.getText()));
            }
            if (this.nextInSeries != null) {
                buf.append("<nextInSeries>");
                buf.append(this.nextInSeries.toString());
                buf.append("</nextInSeries>\n");
            }
            if (this.nextLayer != null) {
                buf.append("<nextLayer>");
                buf.append(this.nextLayer.toString());
                buf.append("</nextLayer>\n");
            }
            if (this.firstArg != null) {
                buf.append("<args>");
                buf.append(this.firstArg.toString());
                if (this.nextArg != null) {
                    buf.append(this.nextArg.toString());
                }
                buf.append("</args>");
            }
            return buf.toString();
        }
    }

    private CssError createError(String msg) {
        CssError error;
        if (this.sourceOfStylesheet != null) {
            error = new CssError.StylesheetParsingError(this.sourceOfStylesheet, msg);
        } else if (this.sourceOfInlineStyle != null) {
            error = new CssError.InlineStyleParsingError(this.sourceOfInlineStyle, msg);
        } else {
            error = new CssError.StringParsingError(this.stylesheetAsText, msg);
        }
        return error;
    }

    private void reportError(CssError error) {
        List<CssError> errors = StyleManager.getErrors();
        if (errors != null) {
            errors.add(error);
        }
    }

    private void error(Term root, String msg) throws ParseException {
        Token token = root != null ? root.token : null;
        ParseException pe = new ParseException(msg, token, this);
        reportError(createError(pe.toString()));
        throw pe;
    }

    private void reportException(Exception exception) {
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            StackTraceElement[] stea = exception.getStackTrace();
            if (stea.length > 0) {
                StringBuilder buf = new StringBuilder("Please report ");
                buf.append(exception.getClass().getName()).append(" at:");
                int end = 0;
                while (end < stea.length && getClass().getName().equals(stea[end].getClassName())) {
                    int i2 = end;
                    end++;
                    buf.append("\n\t").append(stea[i2].toString());
                }
                LOGGER.warning(buf.toString());
            }
        }
    }

    private String formatDeprecatedMessage(Term root, String syntax) {
        StringBuilder buf = new StringBuilder("Using deprecated syntax for ");
        buf.append(syntax);
        if (this.sourceOfStylesheet != null) {
            buf.append(" at ").append(this.sourceOfStylesheet).append("[").append(root.token.getLine()).append(',').append(root.token.getOffset()).append("]");
        }
        buf.append(". Refer to the CSS Reference Guide.");
        return buf.toString();
    }

    private ParsedValueImpl<Color, Color> colorValueOfString(String str) {
        if (str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) || str.startsWith("0x")) {
            double a2 = 1.0d;
            String c2 = str;
            int prefixLength = str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) ? 1 : 2;
            int len = c2.length();
            if (len - prefixLength == 4) {
                a2 = Integer.parseInt(c2.substring(len - 1), 16) / 15.0f;
                c2 = c2.substring(0, len - 1);
            } else if (len - prefixLength == 8) {
                a2 = Integer.parseInt(c2.substring(len - 2), 16) / 255.0f;
                c2 = c2.substring(0, len - 2);
            }
            return new ParsedValueImpl<>(Color.web(c2, a2), null);
        }
        try {
            return new ParsedValueImpl<>(Color.web(str), null);
        } catch (IllegalArgumentException | NullPointerException e2) {
            return null;
        }
    }

    private String stripQuotes(String string) {
        return Utils.stripQuotes(string);
    }

    private double clamp(double min, double val, double max) {
        return val < min ? min : max < val ? max : val;
    }

    private boolean isSize(Token token) {
        int ttype = token.getType();
        switch (ttype) {
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
                return true;
            default:
                return token.getType() == 11;
        }
    }

    private Size size(Token token) throws ParseException {
        SizeUnits units;
        SizeUnits sizeUnits = SizeUnits.PX;
        int trim = 2;
        String sval = token.getText().trim();
        int len = sval.length();
        int ttype = token.getType();
        switch (ttype) {
            case 13:
                units = SizeUnits.PX;
                trim = 0;
                break;
            case 14:
                units = SizeUnits.CM;
                break;
            case 15:
                units = SizeUnits.EM;
                break;
            case 16:
                units = SizeUnits.EX;
                break;
            case 17:
                units = SizeUnits.IN;
                break;
            case 18:
                units = SizeUnits.MM;
                break;
            case 19:
                units = SizeUnits.PC;
                break;
            case 20:
                units = SizeUnits.PT;
                break;
            case 21:
                units = SizeUnits.PX;
                break;
            case 22:
                units = SizeUnits.PERCENT;
                trim = 1;
                break;
            case 23:
                units = SizeUnits.DEG;
                trim = 3;
                break;
            case 24:
                units = SizeUnits.GRAD;
                trim = 4;
                break;
            case 25:
                units = SizeUnits.RAD;
                trim = 3;
                break;
            case 26:
                units = SizeUnits.TURN;
                trim = 4;
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            default:
                if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
                    LOGGER.finest("Expected '<number>'");
                }
                ParseException re = new ParseException("Expected '<number>'", token, this);
                reportError(createError(re.toString()));
                throw re;
            case 45:
                units = SizeUnits.S;
                trim = 1;
                break;
            case 46:
                units = SizeUnits.MS;
                break;
        }
        return new Size(Double.parseDouble(sval.substring(0, len - trim)), units);
    }

    private int numberOfTerms(Term root) {
        if (root == null) {
            return 0;
        }
        int nTerms = 0;
        Term term = root;
        do {
            nTerms++;
            term = term.nextInSeries;
        } while (term != null);
        return nTerms;
    }

    private int numberOfLayers(Term root) {
        if (root == null) {
            return 0;
        }
        int nLayers = 0;
        Term term = root;
        do {
            nLayers++;
            while (term.nextInSeries != null) {
                term = term.nextInSeries;
            }
            term = term.nextLayer;
        } while (term != null);
        return nLayers;
    }

    private int numberOfArgs(Term root) {
        if (root == null) {
            return 0;
        }
        int nArgs = 0;
        Term term = root.firstArg;
        while (true) {
            Term term2 = term;
            if (term2 != null) {
                nArgs++;
                term = term2.nextArg;
            } else {
                return nArgs;
            }
        }
    }

    private Term nextLayer(Term root) {
        if (root == null) {
            return null;
        }
        Term term = root;
        while (true) {
            Term term2 = term;
            if (term2.nextInSeries != null) {
                term = term2.nextInSeries;
            } else {
                return term2.nextLayer;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:176:0x038c A[PHI: r12
  0x038c: PHI (r12v1 'str' java.lang.String) = 
  (r12v0 'str' java.lang.String)
  (r12v3 'str' java.lang.String)
  (r12v3 'str' java.lang.String)
  (r12v0 'str' java.lang.String)
 binds: [B:167:0x035e, B:173:0x0381, B:175:0x0389, B:171:0x0374] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.javafx.css.ParsedValueImpl valueFor(java.lang.String r8, com.sun.javafx.css.parser.CSSParser.Term r9, com.sun.javafx.css.parser.CSSLexer r10) throws com.sun.javafx.css.parser.CSSParser.ParseException {
        /*
            Method dump skipped, instructions count: 937
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSParser.valueFor(java.lang.String, com.sun.javafx.css.parser.CSSParser$Term, com.sun.javafx.css.parser.CSSLexer):com.sun.javafx.css.ParsedValueImpl");
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x024d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.sun.javafx.css.ParsedValueImpl parse(com.sun.javafx.css.parser.CSSParser.Term r7) throws com.sun.javafx.css.parser.CSSParser.ParseException {
        /*
            Method dump skipped, instructions count: 714
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSParser.parse(com.sun.javafx.css.parser.CSSParser$Term):com.sun.javafx.css.ParsedValueImpl");
    }

    private ParsedValueImpl<?, Size> parseSize(Term root) throws ParseException {
        ParsedValueImpl<?, Size> value;
        if (root.token == null || !isSize(root.token)) {
            error(root, "Expected '<size>'");
        }
        if (root.token.getType() != 11) {
            Size size = size(root.token);
            value = new ParsedValueImpl<>(size, null);
        } else {
            String key = root.token.getText();
            value = new ParsedValueImpl<>(key, null, true);
        }
        return value;
    }

    private ParsedValueImpl<?, Color> parseColor(Term root) throws ParseException {
        ParsedValueImpl<?, Color> color = null;
        if (root.token != null && (root.token.getType() == 11 || root.token.getType() == 37 || root.token.getType() == 12)) {
            color = parse(root);
        } else {
            error(root, "Expected '<color>'");
        }
        return color;
    }

    private ParsedValueImpl rgb(Term root) throws ParseException {
        Token atok;
        double rval;
        double gval;
        double bval;
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"rgb".regionMatches(true, 0, fn, 0, 3)) {
            error(root, "Expected 'rgb' or 'rgba'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<number>' or '<percentage>'");
        }
        Token rtok = arg.token;
        if (rtok == null || (rtok.getType() != 13 && rtok.getType() != 22)) {
            error(arg, "Expected '<number>' or '<percentage>'");
        }
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<number>' or '<percentage>'");
        }
        Token gtok = arg2.token;
        if (gtok == null || (gtok.getType() != 13 && gtok.getType() != 22)) {
            error(arg2, "Expected '<number>' or '<percentage>'");
        }
        Term arg3 = arg2.nextArg;
        if (arg3 == null) {
            error(arg2, "Expected '<number>' or '<percentage>'");
        }
        Token btok = arg3.token;
        if (btok == null || (btok.getType() != 13 && btok.getType() != 22)) {
            error(arg3, "Expected '<number>' or '<percentage>'");
        }
        Term arg4 = arg3.nextArg;
        if (arg4 != null) {
            Token token = arg4.token;
            atok = token;
            if (token == null || atok.getType() != 13) {
                error(arg4, "Expected '<number>'");
            }
        } else {
            atok = null;
        }
        int argType = rtok.getType();
        if (argType != gtok.getType() || argType != btok.getType() || (argType != 13 && argType != 22)) {
            error(arg3, "Argument type mistmatch");
        }
        String rtext = rtok.getText();
        String gtext = gtok.getText();
        String btext = btok.getText();
        if (argType == 13) {
            rval = clamp(0.0d, Double.parseDouble(rtext) / 255.0d, 1.0d);
            gval = clamp(0.0d, Double.parseDouble(gtext) / 255.0d, 1.0d);
            bval = clamp(0.0d, Double.parseDouble(btext) / 255.0d, 1.0d);
        } else {
            rval = clamp(0.0d, Double.parseDouble(rtext.substring(0, rtext.length() - 1)) / 100.0d, 1.0d);
            gval = clamp(0.0d, Double.parseDouble(gtext.substring(0, gtext.length() - 1)) / 100.0d, 1.0d);
            bval = clamp(0.0d, Double.parseDouble(btext.substring(0, btext.length() - 1)) / 100.0d, 1.0d);
        }
        String atext = atok != null ? atok.getText() : null;
        double aval = atext != null ? clamp(0.0d, Double.parseDouble(atext), 1.0d) : 1.0d;
        return new ParsedValueImpl(Color.color(rval, gval, bval, aval), null);
    }

    private ParsedValueImpl hsb(Term root) throws ParseException {
        Token atok;
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"hsb".regionMatches(true, 0, fn, 0, 3)) {
            error(root, "Expected 'hsb' or 'hsba'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<number>'");
        }
        Token htok = arg.token;
        if (htok == null || htok.getType() != 13) {
            error(arg, "Expected '<number>'");
        }
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<percent>'");
        }
        Token stok = arg2.token;
        if (stok == null || stok.getType() != 22) {
            error(arg2, "Expected '<percent>'");
        }
        Term arg3 = arg2.nextArg;
        if (arg3 == null) {
            error(arg2, "Expected '<percent>'");
        }
        Token btok = arg3.token;
        if (btok == null || btok.getType() != 22) {
            error(arg3, "Expected '<percent>'");
        }
        Term arg4 = arg3.nextArg;
        if (arg4 != null) {
            Token token = arg4.token;
            atok = token;
            if (token == null || atok.getType() != 13) {
                error(arg4, "Expected '<number>'");
            }
        } else {
            atok = null;
        }
        Size hval = size(htok);
        Size sval = size(stok);
        Size bval = size(btok);
        double hue = hval.pixels();
        double saturation = clamp(0.0d, sval.pixels(), 1.0d);
        double brightness = clamp(0.0d, bval.pixels(), 1.0d);
        Size aval = atok != null ? size(atok) : null;
        double opacity = aval != null ? clamp(0.0d, aval.pixels(), 1.0d) : 1.0d;
        return new ParsedValueImpl(Color.hsb(hue, saturation, brightness, opacity), null);
    }

    private ParsedValueImpl<ParsedValue[], Color> derive(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"derive".regionMatches(true, 0, fn, 0, 6)) {
            error(root, "Expected 'derive'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> color = parseColor(arg);
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<percent'");
        }
        ParsedValueImpl<?, Size> brightness = parseSize(arg2);
        ParsedValueImpl[] values = {color, brightness};
        return new ParsedValueImpl<>(values, DeriveColorConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Color> ladder(Term root) throws ParseException {
        Term prev;
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"ladder".regionMatches(true, 0, fn, 0, 6)) {
            error(root, "Expected 'ladder'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(formatDeprecatedMessage(root, "ladder"));
        }
        Term term = root.nextInSeries;
        if (term == null) {
            error(root, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> color = parse(term);
        Term term2 = term.nextInSeries;
        if (term2 == null) {
            error(term, "Expected 'stops'");
        }
        if (term2.token == null || term2.token.getType() != 11 || !"stops".equalsIgnoreCase(term2.token.getText())) {
            error(term2, "Expected 'stops'");
        }
        Term term3 = term2.nextInSeries;
        Term term4 = term3;
        if (term3 == null) {
            error(term2, "Expected '(<number>, <color>)'");
        }
        int nStops = 0;
        Term temp = term4;
        do {
            nStops++;
            Term term5 = temp.nextInSeries;
            temp = term5;
            if (term5 == null || temp.token == null) {
                break;
            }
        } while (temp.token.getType() == 34);
        ParsedValueImpl[] values = new ParsedValueImpl[nStops + 1];
        values[0] = color;
        int stopIndex = 1;
        do {
            ParsedValueImpl<ParsedValue[], Stop> stop = stop(term4);
            if (stop != null) {
                int i2 = stopIndex;
                stopIndex++;
                values[i2] = stop;
            }
            prev = term4;
            Term term6 = term4.nextInSeries;
            term4 = term6;
            if (term6 == null) {
                break;
            }
        } while (term4.token.getType() == 34);
        if (term4 != null) {
            root.nextInSeries = term4;
        } else {
            root.nextInSeries = null;
            root.nextLayer = prev.nextLayer;
        }
        return new ParsedValueImpl<>(values, LadderConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Color> parseLadder(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"ladder".regionMatches(true, 0, fn, 0, 6)) {
            error(root, "Expected 'ladder'");
        }
        Term term = root.firstArg;
        if (term == null) {
            error(root, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> color = parse(term);
        Term term2 = term.nextArg;
        if (term2 == null) {
            error(term, "Expected '<color-stop>[, <color-stop>]+'");
        }
        ParsedValueImpl<ParsedValue[], Stop>[] stops = parseColorStops(term2);
        ParsedValueImpl[] values = new ParsedValueImpl[stops.length + 1];
        values[0] = color;
        System.arraycopy(stops, 0, values, 1, stops.length);
        return new ParsedValueImpl<>(values, LadderConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Stop> stop(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"(".equals(fn)) {
            error(root, "Expected '('");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> size = parseSize(arg);
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> color = parseColor(arg2);
        ParsedValueImpl[] values = {size, color};
        return new ParsedValueImpl<>(values, StopConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Stop>[] parseColorStops(Term root) throws ParseException {
        Term term;
        int nArgs = 1;
        Term term2 = root;
        while (true) {
            Term temp = term2;
            if (temp == null) {
                break;
            }
            if (temp.nextArg != null) {
                nArgs++;
                term2 = temp.nextArg;
            } else {
                if (temp.nextInSeries == null) {
                    break;
                }
                term2 = temp.nextInSeries;
            }
        }
        if (nArgs < 2) {
            error(root, "Expected '<color-stop>'");
        }
        ParsedValueImpl<?, Color>[] colors = new ParsedValueImpl[nArgs];
        Size[] positions = new Size[nArgs];
        Arrays.fill(positions, (Object) null);
        Term stop = root;
        for (int n2 = 0; n2 < nArgs; n2++) {
            colors[n2] = parseColor(stop);
            Term prev = stop;
            Term term3 = stop.nextInSeries;
            if (term3 != null) {
                if (isSize(term3.token)) {
                    positions[n2] = size(term3.token);
                    if (0 != 0 && null != positions[n2].getUnits()) {
                        error(term3, "Parser unable to handle mixed '<percent>' and '<length>'");
                    }
                } else {
                    error(prev, "Expected '<percent>' or '<length>'");
                }
                term = term3.nextArg;
            } else {
                term = stop.nextArg;
            }
            stop = term;
        }
        if (positions[0] == null) {
            positions[0] = new Size(0.0d, SizeUnits.PERCENT);
        }
        if (positions[nArgs - 1] == null) {
            positions[nArgs - 1] = new Size(100.0d, SizeUnits.PERCENT);
        }
        Size max = null;
        for (int n3 = 1; n3 < nArgs; n3++) {
            Size pos0 = positions[n3 - 1];
            if (pos0 != null) {
                if (max == null || max.getValue() < pos0.getValue()) {
                    max = pos0;
                }
                Size pos1 = positions[n3];
                if (pos1 != null && pos1.getValue() < max.getValue()) {
                    positions[n3] = max;
                }
            }
        }
        Size preceding = null;
        int withoutIndex = -1;
        for (int n4 = 0; n4 < nArgs; n4++) {
            Size pos = positions[n4];
            if (pos == null) {
                if (withoutIndex == -1) {
                    withoutIndex = n4;
                }
            } else if (withoutIndex > -1) {
                int nWithout = n4 - withoutIndex;
                double precedingValue = preceding.getValue();
                double delta = (pos.getValue() - precedingValue) / (nWithout + 1);
                while (withoutIndex < n4) {
                    precedingValue += delta;
                    int i2 = withoutIndex;
                    withoutIndex++;
                    positions[i2] = new Size(precedingValue, pos.getUnits());
                }
                withoutIndex = -1;
                preceding = pos;
            } else {
                preceding = pos;
            }
        }
        ParsedValueImpl<ParsedValue[], Stop>[] stops = new ParsedValueImpl[nArgs];
        for (int n5 = 0; n5 < nArgs; n5++) {
            stops[n5] = new ParsedValueImpl<>(new ParsedValueImpl[]{new ParsedValueImpl(positions[n5], null), colors[n5]}, StopConverter.getInstance());
        }
        return stops;
    }

    private ParsedValueImpl[] point(Term root) throws ParseException {
        if (root.token == null || root.token.getType() != 34) {
            error(root, "Expected '(<number>, <number>)'");
        }
        String fn = root.token.getText();
        if (fn == null || !"(".equalsIgnoreCase(fn)) {
            error(root, "Expected '('");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> ptX = parseSize(arg);
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> ptY = parseSize(arg2);
        return new ParsedValueImpl[]{ptX, ptY};
    }

    private ParsedValueImpl parseFunction(Term root) throws ParseException {
        String fcn = root.token != null ? root.token.getText() : null;
        if (fcn == null) {
            error(root, "Expected function name");
            return null;
        }
        if ("rgb".regionMatches(true, 0, fcn, 0, 3)) {
            return rgb(root);
        }
        if ("hsb".regionMatches(true, 0, fcn, 0, 3)) {
            return hsb(root);
        }
        if ("derive".regionMatches(true, 0, fcn, 0, 6)) {
            return derive(root);
        }
        if ("innershadow".regionMatches(true, 0, fcn, 0, 11)) {
            return innershadow(root);
        }
        if ("dropshadow".regionMatches(true, 0, fcn, 0, 10)) {
            return dropshadow(root);
        }
        if ("linear-gradient".regionMatches(true, 0, fcn, 0, 15)) {
            return parseLinearGradient(root);
        }
        if ("radial-gradient".regionMatches(true, 0, fcn, 0, 15)) {
            return parseRadialGradient(root);
        }
        if ("image-pattern".regionMatches(true, 0, fcn, 0, 13)) {
            return parseImagePattern(root);
        }
        if ("repeating-image-pattern".regionMatches(true, 0, fcn, 0, 23)) {
            return parseRepeatingImagePattern(root);
        }
        if ("ladder".regionMatches(true, 0, fcn, 0, 6)) {
            return parseLadder(root);
        }
        if ("region".regionMatches(true, 0, fcn, 0, 6)) {
            return parseRegion(root);
        }
        error(root, "Unexpected function '" + fcn + PdfOps.SINGLE_QUOTE_TOKEN);
        return null;
    }

    private ParsedValueImpl<String, BlurType> blurType(Term root) throws ParseException {
        if (root == null) {
            return null;
        }
        if (root.token == null || root.token.getType() != 11 || root.token.getText() == null || root.token.getText().isEmpty()) {
            error(root, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
        }
        String blurStr = root.token.getText().toLowerCase(Locale.ROOT);
        BlurType blurType = BlurType.THREE_PASS_BOX;
        if ("gaussian".equals(blurStr)) {
            blurType = BlurType.GAUSSIAN;
        } else if ("one-pass-box".equals(blurStr)) {
            blurType = BlurType.ONE_PASS_BOX;
        } else if ("two-pass-box".equals(blurStr)) {
            blurType = BlurType.TWO_PASS_BOX;
        } else if ("three-pass-box".equals(blurStr)) {
            blurType = BlurType.THREE_PASS_BOX;
        } else {
            error(root, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
        }
        return new ParsedValueImpl<>(blurType.name(), new EnumConverter(BlurType.class));
    }

    private ParsedValueImpl innershadow(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"innershadow".regionMatches(true, 0, fn, 0, 11)) {
            error(root, "Expected 'innershadow'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<blur-type>'");
        }
        ParsedValueImpl<String, BlurType> blurVal = blurType(arg);
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> colorVal = parseColor(arg2);
        Term arg3 = arg2.nextArg;
        if (arg3 == null) {
            error(arg2, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> radiusVal = parseSize(arg3);
        Term arg4 = arg3.nextArg;
        if (arg4 == null) {
            error(arg3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> chokeVal = parseSize(arg4);
        Term arg5 = arg4.nextArg;
        if (arg5 == null) {
            error(arg4, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> offsetXVal = parseSize(arg5);
        Term arg6 = arg5.nextArg;
        if (arg6 == null) {
            error(arg5, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> offsetYVal = parseSize(arg6);
        ParsedValueImpl[] values = {blurVal, colorVal, radiusVal, chokeVal, offsetXVal, offsetYVal};
        return new ParsedValueImpl(values, EffectConverter.InnerShadowConverter.getInstance());
    }

    private ParsedValueImpl dropshadow(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"dropshadow".regionMatches(true, 0, fn, 0, 10)) {
            error(root, "Expected 'dropshadow'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected '<blur-type>'");
        }
        ParsedValueImpl<String, BlurType> blurVal = blurType(arg);
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> colorVal = parseColor(arg2);
        Term arg3 = arg2.nextArg;
        if (arg3 == null) {
            error(arg2, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> radiusVal = parseSize(arg3);
        Term arg4 = arg3.nextArg;
        if (arg4 == null) {
            error(arg3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> spreadVal = parseSize(arg4);
        Term arg5 = arg4.nextArg;
        if (arg5 == null) {
            error(arg4, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> offsetXVal = parseSize(arg5);
        Term arg6 = arg5.nextArg;
        if (arg6 == null) {
            error(arg5, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> offsetYVal = parseSize(arg6);
        ParsedValueImpl[] values = {blurVal, colorVal, radiusVal, spreadVal, offsetXVal, offsetYVal};
        return new ParsedValueImpl(values, EffectConverter.DropShadowConverter.getInstance());
    }

    private ParsedValueImpl<String, CycleMethod> cycleMethod(Term root) {
        CycleMethod cycleMethod = null;
        if (root != null && root.token.getType() == 11) {
            String text = root.token.getText().toLowerCase(Locale.ROOT);
            if ("repeat".equals(text)) {
                cycleMethod = CycleMethod.REPEAT;
            } else if ("reflect".equals(text)) {
                cycleMethod = CycleMethod.REFLECT;
            } else if ("no-cycle".equals(text)) {
                cycleMethod = CycleMethod.NO_CYCLE;
            }
        }
        if (cycleMethod != null) {
            return new ParsedValueImpl<>(cycleMethod.name(), new EnumConverter(CycleMethod.class));
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], Paint> linearGradient(Term root) throws ParseException {
        Term prev;
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"linear".equalsIgnoreCase(fn)) {
            error(root, "Expected 'linear'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(formatDeprecatedMessage(root, "linear gradient"));
        }
        Term term = root.nextInSeries;
        if (term == null) {
            error(root, "Expected '(<number>, <number>)'");
        }
        ParsedValueImpl<?, Size>[] startPt = point(term);
        Term term2 = term.nextInSeries;
        if (term2 == null) {
            error(term, "Expected 'to'");
        }
        if (term2.token == null || term2.token.getType() != 11 || !"to".equalsIgnoreCase(term2.token.getText())) {
            error(root, "Expected 'to'");
        }
        Term term3 = term2.nextInSeries;
        if (term3 == null) {
            error(term2, "Expected '(<number>, <number>)'");
        }
        ParsedValueImpl<?, Size>[] endPt = point(term3);
        Term term4 = term3.nextInSeries;
        if (term4 == null) {
            error(term3, "Expected 'stops'");
        }
        if (term4.token == null || term4.token.getType() != 11 || !"stops".equalsIgnoreCase(term4.token.getText())) {
            error(term4, "Expected 'stops'");
        }
        Term term5 = term4.nextInSeries;
        Term term6 = term5;
        if (term5 == null) {
            error(term4, "Expected '(<number>, <number>)'");
        }
        int nStops = 0;
        Term temp = term6;
        do {
            nStops++;
            Term term7 = temp.nextInSeries;
            temp = term7;
            if (term7 == null || temp.token == null) {
                break;
            }
        } while (temp.token.getType() == 34);
        ParsedValueImpl[] stops = new ParsedValueImpl[nStops];
        int stopIndex = 0;
        do {
            ParsedValueImpl<ParsedValue[], Stop> stop = stop(term6);
            if (stop != null) {
                int i2 = stopIndex;
                stopIndex++;
                stops[i2] = stop;
            }
            prev = term6;
            Term term8 = term6.nextInSeries;
            term6 = term8;
            if (term8 == null) {
                break;
            }
        } while (term6.token.getType() == 34);
        ParsedValueImpl<String, CycleMethod> cycleMethod = cycleMethod(term6);
        if (cycleMethod == null) {
            cycleMethod = new ParsedValueImpl<>(CycleMethod.NO_CYCLE.name(), new EnumConverter(CycleMethod.class));
            if (term6 != null) {
                root.nextInSeries = term6;
            } else {
                root.nextInSeries = null;
                root.nextLayer = prev.nextLayer;
            }
        } else {
            root.nextInSeries = term6.nextInSeries;
            root.nextLayer = term6.nextLayer;
        }
        ParsedValueImpl[] values = new ParsedValueImpl[5 + stops.length];
        int index = 0 + 1;
        values[0] = startPt != null ? startPt[0] : null;
        int index2 = index + 1;
        values[index] = startPt != null ? startPt[1] : null;
        int index3 = index2 + 1;
        values[index2] = endPt != null ? endPt[0] : null;
        int index4 = index3 + 1;
        values[index3] = endPt != null ? endPt[1] : null;
        int index5 = index4 + 1;
        values[index4] = cycleMethod;
        for (ParsedValueImpl parsedValueImpl : stops) {
            int i3 = index5;
            index5++;
            values[i3] = parsedValueImpl;
        }
        return new ParsedValueImpl<>(values, PaintConverter.LinearGradientConverter.getInstance());
    }

    private ParsedValueImpl parseLinearGradient(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"linear-gradient".regionMatches(true, 0, fn, 0, 15)) {
            error(root, "Expected 'linear-gradient'");
        }
        Term term = root.firstArg;
        Term arg = term;
        if (term == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(root, "Expected 'from <point> to <point>' or 'to <side-or-corner>' or '<cycle-method>' or '<color-stop>'");
        }
        Term prev = arg;
        ParsedValueImpl<?, Size>[] startPt = null;
        ParsedValueImpl<?, Size>[] endPt = null;
        if (Constants.ATTRNAME_FROM.equalsIgnoreCase(arg.token.getText())) {
            Term arg2 = arg.nextInSeries;
            if (arg2 == null) {
                error(arg, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptX = parseSize(arg2);
            Term arg3 = arg2.nextInSeries;
            if (arg3 == null) {
                error(arg2, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptY = parseSize(arg3);
            startPt = new ParsedValueImpl[]{ptX, ptY};
            Term arg4 = arg3.nextInSeries;
            if (arg4 == null) {
                error(arg3, "Expected 'to'");
            }
            if (arg4.token == null || arg4.token.getType() != 11 || !"to".equalsIgnoreCase(arg4.token.getText())) {
                error(arg3, "Expected 'to'");
            }
            Term arg5 = arg4.nextInSeries;
            if (arg5 == null) {
                error(arg4, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptX2 = parseSize(arg5);
            Term arg6 = arg5.nextInSeries;
            if (arg6 == null) {
                error(arg5, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptY2 = parseSize(arg6);
            endPt = new ParsedValueImpl[]{ptX2, ptY2};
            prev = arg6;
            arg = arg6.nextArg;
        } else if ("to".equalsIgnoreCase(arg.token.getText())) {
            Term term2 = arg.nextInSeries;
            Term arg7 = term2;
            if (term2 == null || arg7.token == null || arg7.token.getType() != 11 || arg7.token.getText().isEmpty()) {
                error(arg, "Expected '<side-or-corner>'");
            }
            int startX = 0;
            int startY = 0;
            int endX = 0;
            int endY = 0;
            String sideOrCorner1 = arg7.token.getText().toLowerCase(Locale.ROOT);
            if (JSplitPane.TOP.equals(sideOrCorner1)) {
                startY = 100;
                endY = 0;
            } else if (JSplitPane.BOTTOM.equals(sideOrCorner1)) {
                startY = 0;
                endY = 100;
            } else if (JSplitPane.RIGHT.equals(sideOrCorner1)) {
                startX = 0;
                endX = 100;
            } else if (JSplitPane.LEFT.equals(sideOrCorner1)) {
                startX = 100;
                endX = 0;
            } else {
                error(arg7, "Invalid '<side-or-corner>'");
            }
            if (arg7.nextInSeries != null) {
                arg7 = arg7.nextInSeries;
                if (arg7.token != null && arg7.token.getType() == 11 && !arg7.token.getText().isEmpty()) {
                    String sideOrCorner2 = arg7.token.getText().toLowerCase(Locale.ROOT);
                    if (JSplitPane.RIGHT.equals(sideOrCorner2) && startX == 0 && endX == 0) {
                        startX = 0;
                        endX = 100;
                    } else if (JSplitPane.LEFT.equals(sideOrCorner2) && startX == 0 && endX == 0) {
                        startX = 100;
                        endX = 0;
                    } else if (JSplitPane.TOP.equals(sideOrCorner2) && startY == 0 && endY == 0) {
                        startY = 100;
                        endY = 0;
                    } else if (JSplitPane.BOTTOM.equals(sideOrCorner2) && startY == 0 && endY == 0) {
                        startY = 0;
                        endY = 100;
                    } else {
                        error(arg7, "Invalid '<side-or-corner>'");
                    }
                } else {
                    error(arg7, "Expected '<side-or-corner>'");
                }
            }
            startPt = new ParsedValueImpl[]{new ParsedValueImpl<>(new Size(startX, SizeUnits.PERCENT), null), new ParsedValueImpl<>(new Size(startY, SizeUnits.PERCENT), null)};
            endPt = new ParsedValueImpl[]{new ParsedValueImpl<>(new Size(endX, SizeUnits.PERCENT), null), new ParsedValueImpl<>(new Size(endY, SizeUnits.PERCENT), null)};
            prev = arg7;
            arg = arg7.nextArg;
        }
        if (startPt == null && endPt == null) {
            startPt = new ParsedValueImpl[]{new ParsedValueImpl<>(new Size(0.0d, SizeUnits.PERCENT), null), new ParsedValueImpl<>(new Size(0.0d, SizeUnits.PERCENT), null)};
            endPt = new ParsedValueImpl[]{new ParsedValueImpl<>(new Size(0.0d, SizeUnits.PERCENT), null), new ParsedValueImpl<>(new Size(100.0d, SizeUnits.PERCENT), null)};
        }
        if (arg == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(prev, "Expected '<cycle-method>' or '<color-stop>'");
        }
        CycleMethod cycleMethod = CycleMethod.NO_CYCLE;
        if ("reflect".equalsIgnoreCase(arg.token.getText()) || "repeat".equalsIgnoreCase(arg.token.getText())) {
            cycleMethod = CycleMethod.REFLECT;
            prev = arg;
            arg = arg.nextArg;
        }
        if (arg == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(prev, "Expected '<color-stop>'");
        }
        ParsedValueImpl<ParsedValue[], Stop>[] stops = parseColorStops(arg);
        ParsedValueImpl[] values = new ParsedValueImpl[5 + stops.length];
        int index = 0 + 1;
        values[0] = startPt != null ? startPt[0] : null;
        int index2 = index + 1;
        values[index] = startPt != null ? startPt[1] : null;
        int index3 = index2 + 1;
        values[index2] = endPt != null ? endPt[0] : null;
        int index4 = index3 + 1;
        values[index3] = endPt != null ? endPt[1] : null;
        int index5 = index4 + 1;
        values[index4] = new ParsedValueImpl(cycleMethod.name(), new EnumConverter(CycleMethod.class));
        for (ParsedValueImpl<ParsedValue[], Stop> parsedValueImpl : stops) {
            int i2 = index5;
            index5++;
            values[i2] = parsedValueImpl;
        }
        return new ParsedValueImpl(values, PaintConverter.LinearGradientConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> radialGradient(Term root) throws ParseException {
        Term prev;
        String fn = root.token != null ? root.token.getText() : null;
        if (fn == null || !"radial".equalsIgnoreCase(fn)) {
            error(root, "Expected 'radial'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(formatDeprecatedMessage(root, "radial gradient"));
        }
        Term term = root.nextInSeries;
        Term term2 = term;
        if (term == null) {
            error(root, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
        }
        if (term2.token == null) {
            error(term2, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
        }
        ParsedValueImpl<?, Size> focusAngle = null;
        if (term2.token.getType() == 11) {
            String keyword = term2.token.getText().toLowerCase(Locale.ROOT);
            if ("focus-angle".equals(keyword)) {
                Term term3 = term2.nextInSeries;
                if (term3 == null) {
                    error(term2, "Expected '<number>'");
                }
                if (term3.token == null) {
                    error(term2, "Expected '<number>'");
                }
                focusAngle = parseSize(term3);
                Term term4 = term3.nextInSeries;
                term2 = term4;
                if (term4 == null) {
                    error(term3, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
                }
                if (term2.token == null) {
                    error(term2, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
                }
            }
        }
        ParsedValueImpl<?, Size> focusDistance = null;
        if (term2.token.getType() == 11) {
            String keyword2 = term2.token.getText().toLowerCase(Locale.ROOT);
            if ("focus-distance".equals(keyword2)) {
                Term prev2 = term2;
                Term term5 = term2.nextInSeries;
                if (term5 == null) {
                    error(prev2, "Expected '<number>'");
                }
                if (term5.token == null) {
                    error(prev2, "Expected '<number>'");
                }
                focusDistance = parseSize(term5);
                Term term6 = term5.nextInSeries;
                term2 = term6;
                if (term6 == null) {
                    error(term5, "Expected  'center (<number>,<number>)' or '<size>'");
                }
                if (term2.token == null) {
                    error(term2, "Expected  'center (<number>,<number>)' or '<size>'");
                }
            }
        }
        ParsedValueImpl<?, Size>[] centerPoint = null;
        if (term2.token.getType() == 11) {
            String keyword3 = term2.token.getText().toLowerCase(Locale.ROOT);
            if ("center".equals(keyword3)) {
                Term prev3 = term2;
                Term term7 = term2.nextInSeries;
                if (term7 == null) {
                    error(prev3, "Expected '(<number>,<number>)'");
                }
                if (term7.token == null || term7.token.getType() != 34) {
                    error(term7, "Expected '(<number>,<number>)'");
                }
                centerPoint = point(term7);
                Term term8 = term7.nextInSeries;
                term2 = term8;
                if (term8 == null) {
                    error(term7, "Expected '<size>'");
                }
                if (term2.token == null) {
                    error(term2, "Expected '<size>'");
                }
            }
        }
        ParsedValueImpl<?, Size> radius = parseSize(term2);
        Term prev4 = term2;
        Term term9 = term2.nextInSeries;
        if (term9 == null) {
            error(prev4, "Expected 'stops' keyword");
        }
        if (term9.token == null || term9.token.getType() != 11) {
            error(term9, "Expected 'stops' keyword");
        }
        if (!"stops".equalsIgnoreCase(term9.token.getText())) {
            error(term9, "Expected 'stops'");
        }
        Term term10 = term9.nextInSeries;
        Term term11 = term10;
        if (term10 == null) {
            error(term9, "Expected '(<number>, <number>)'");
        }
        int nStops = 0;
        Term temp = term11;
        do {
            nStops++;
            Term term12 = temp.nextInSeries;
            temp = term12;
            if (term12 == null || temp.token == null) {
                break;
            }
        } while (temp.token.getType() == 34);
        ParsedValueImpl[] stops = new ParsedValueImpl[nStops];
        int stopIndex = 0;
        do {
            ParsedValueImpl<ParsedValue[], Stop> stop = stop(term11);
            if (stop != null) {
                int i2 = stopIndex;
                stopIndex++;
                stops[i2] = stop;
            }
            prev = term11;
            Term term13 = term11.nextInSeries;
            term11 = term13;
            if (term13 == null) {
                break;
            }
        } while (term11.token.getType() == 34);
        ParsedValueImpl<String, CycleMethod> cycleMethod = cycleMethod(term11);
        if (cycleMethod == null) {
            cycleMethod = new ParsedValueImpl<>(CycleMethod.NO_CYCLE.name(), new EnumConverter(CycleMethod.class));
            if (term11 != null) {
                root.nextInSeries = term11;
            } else {
                root.nextInSeries = null;
                root.nextLayer = prev.nextLayer;
            }
        } else {
            root.nextInSeries = term11.nextInSeries;
            root.nextLayer = term11.nextLayer;
        }
        ParsedValueImpl[] values = new ParsedValueImpl[6 + stops.length];
        int index = 0 + 1;
        values[0] = focusAngle;
        int index2 = index + 1;
        values[index] = focusDistance;
        int index3 = index2 + 1;
        values[index2] = centerPoint != null ? centerPoint[0] : null;
        int index4 = index3 + 1;
        values[index3] = centerPoint != null ? centerPoint[1] : null;
        int index5 = index4 + 1;
        values[index4] = radius;
        int index6 = index5 + 1;
        values[index5] = cycleMethod;
        for (ParsedValueImpl parsedValueImpl : stops) {
            int i3 = index6;
            index6++;
            values[i3] = parsedValueImpl;
        }
        return new ParsedValueImpl<>(values, PaintConverter.RadialGradientConverter.getInstance());
    }

    private ParsedValueImpl parseRadialGradient(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"radial-gradient".regionMatches(true, 0, fn, 0, 15)) {
            error(root, "Expected 'radial-gradient'");
        }
        Term term = root.firstArg;
        Term arg = term;
        if (term == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(root, "Expected 'focus-angle <angle>' or 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
        }
        Term prev = arg;
        ParsedValueImpl<?, Size> focusAngle = null;
        ParsedValueImpl<?, Size> focusDistance = null;
        ParsedValueImpl<?, Size>[] centerPoint = null;
        ParsedValueImpl<?, Size> radius = null;
        if ("focus-angle".equalsIgnoreCase(arg.token.getText())) {
            Term arg2 = arg.nextInSeries;
            if (arg2 == null || !isSize(arg2.token)) {
                error(arg, "Expected '<angle>'");
            }
            Size angle = size(arg2.token);
            switch (angle.getUnits()) {
                case DEG:
                case RAD:
                case GRAD:
                case TURN:
                case PX:
                    break;
                default:
                    error(arg2, "Expected [deg | rad | grad | turn ]");
                    break;
            }
            focusAngle = new ParsedValueImpl<>(angle, null);
            prev = arg2;
            Term term2 = arg2.nextArg;
            arg = term2;
            if (term2 == null) {
                error(prev, "Expected 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
            }
        }
        if ("focus-distance".equalsIgnoreCase(arg.token.getText())) {
            Term prev2 = arg;
            Term arg3 = arg.nextInSeries;
            if (arg3 == null || !isSize(arg3.token)) {
                error(prev2, "Expected '<percentage>'");
            }
            Size distance = size(arg3.token);
            switch (distance.getUnits()) {
                case PERCENT:
                    break;
                default:
                    error(arg3, "Expected '%'");
                    break;
            }
            focusDistance = new ParsedValueImpl<>(distance, null);
            prev = arg3;
            Term term3 = arg3.nextArg;
            arg = term3;
            if (term3 == null) {
                error(prev, "Expected 'center <center>' or 'radius <length>'");
            }
        }
        if ("center".equalsIgnoreCase(arg.token.getText())) {
            Term prev3 = arg;
            Term arg4 = arg.nextInSeries;
            if (arg4 == null) {
                error(prev3, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptX = parseSize(arg4);
            Term arg5 = arg4.nextInSeries;
            if (arg5 == null) {
                error(arg4, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> ptY = parseSize(arg5);
            centerPoint = new ParsedValueImpl[]{ptX, ptY};
            prev = arg5;
            Term term4 = arg5.nextArg;
            arg = term4;
            if (term4 == null) {
                error(prev, "Expected 'radius [<length> | <percentage>]'");
            }
        }
        if ("radius".equalsIgnoreCase(arg.token.getText())) {
            Term prev4 = arg;
            Term arg6 = arg.nextInSeries;
            if (arg6 == null || !isSize(arg6.token)) {
                error(prev4, "Expected '[<length> | <percentage>]'");
            }
            radius = parseSize(arg6);
            prev = arg6;
            Term term5 = arg6.nextArg;
            arg = term5;
            if (term5 == null) {
                error(prev, "Expected 'radius [<length> | <percentage>]'");
            }
        }
        CycleMethod cycleMethod = CycleMethod.NO_CYCLE;
        if ("reflect".equalsIgnoreCase(arg.token.getText()) || "repeat".equalsIgnoreCase(arg.token.getText())) {
            cycleMethod = CycleMethod.REFLECT;
            prev = arg;
            arg = arg.nextArg;
        }
        if (arg == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(prev, "Expected '<color-stop>'");
        }
        ParsedValueImpl<ParsedValue[], Stop>[] stops = parseColorStops(arg);
        ParsedValueImpl[] values = new ParsedValueImpl[6 + stops.length];
        int index = 0 + 1;
        values[0] = focusAngle;
        int index2 = index + 1;
        values[index] = focusDistance;
        int index3 = index2 + 1;
        values[index2] = centerPoint != null ? centerPoint[0] : null;
        int index4 = index3 + 1;
        values[index3] = centerPoint != null ? centerPoint[1] : null;
        int index5 = index4 + 1;
        values[index4] = radius;
        int index6 = index5 + 1;
        values[index5] = new ParsedValueImpl(cycleMethod.name(), new EnumConverter(CycleMethod.class));
        for (ParsedValueImpl<ParsedValue[], Stop> parsedValueImpl : stops) {
            int i2 = index6;
            index6++;
            values[i2] = parsedValueImpl;
        }
        return new ParsedValueImpl(values, PaintConverter.RadialGradientConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> parseImagePattern(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"image-pattern".regionMatches(true, 0, fn, 0, 13)) {
            error(root, "Expected 'image-pattern'");
        }
        Term arg = root.firstArg;
        if (arg == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(root, "Expected '<uri-string>'");
        }
        String uri = arg.token.getText();
        ParsedValueImpl[] uriValues = {new ParsedValueImpl(uri, StringConverter.getInstance()), null};
        ParsedValueImpl parsedURI = new ParsedValueImpl(uriValues, URLConverter.getInstance());
        if (arg.nextArg == null) {
            ParsedValueImpl[] values = {parsedURI};
            return new ParsedValueImpl<>(values, PaintConverter.ImagePatternConverter.getInstance());
        }
        Term arg2 = arg.nextArg;
        if (arg2 == null) {
            error(arg, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> x2 = parseSize(arg2);
        Term arg3 = arg2.nextArg;
        if (arg3 == null) {
            error(arg2, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> y2 = parseSize(arg3);
        Term arg4 = arg3.nextArg;
        if (arg4 == null) {
            error(arg3, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> w2 = parseSize(arg4);
        Term arg5 = arg4.nextArg;
        if (arg5 == null) {
            error(arg4, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> h2 = parseSize(arg5);
        if (arg5.nextArg == null) {
            ParsedValueImpl[] values2 = {parsedURI, x2, y2, w2, h2};
            return new ParsedValueImpl<>(values2, PaintConverter.ImagePatternConverter.getInstance());
        }
        Term arg6 = arg5.nextArg;
        if (arg6 == null) {
            error(arg5, "Expected '<boolean>'");
        }
        Token token = arg6.token;
        if (token == null || token.getText() == null) {
            error(arg6, "Expected '<boolean>'");
        }
        ParsedValueImpl[] values3 = {parsedURI, x2, y2, w2, h2, new ParsedValueImpl(Boolean.valueOf(Boolean.parseBoolean(token.getText())), null)};
        return new ParsedValueImpl<>(values3, PaintConverter.ImagePatternConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> parseRepeatingImagePattern(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"repeating-image-pattern".regionMatches(true, 0, fn, 0, 23)) {
            error(root, "Expected 'repeating-image-pattern'");
        }
        Term arg = root.firstArg;
        if (arg == null || arg.token == null || arg.token.getText().isEmpty()) {
            error(root, "Expected '<uri-string>'");
        }
        String uri = arg.token.getText();
        ParsedValueImpl[] uriValues = {new ParsedValueImpl(uri, StringConverter.getInstance()), null};
        ParsedValueImpl parsedURI = new ParsedValueImpl(uriValues, URLConverter.getInstance());
        ParsedValueImpl[] values = {parsedURI};
        return new ParsedValueImpl<>(values, PaintConverter.RepeatingImagePatternConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]> parsePaintLayers(Term root) throws ParseException {
        int nPaints = numberOfLayers(root);
        ParsedValueImpl<?, Paint>[] paints = new ParsedValueImpl[nPaints];
        Term temp = root;
        int paint = 0;
        do {
            if (temp.token == null || temp.token.getText() == null || temp.token.getText().isEmpty()) {
                error(temp, "Expected '<paint>'");
            }
            int i2 = paint;
            paint++;
            paints[i2] = parse(temp);
            temp = nextLayer(temp);
        } while (temp != null);
        return new ParsedValueImpl<>(paints, PaintConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<?, Size>[] parseSize1to4(Term root) throws ParseException {
        ParsedValueImpl<?, Size>[] sides = new ParsedValueImpl[4];
        int side = 0;
        for (Term temp = root; side < 4 && temp != null; temp = temp.nextInSeries) {
            int i2 = side;
            side++;
            sides[i2] = parseSize(temp);
        }
        if (side < 2) {
            sides[1] = sides[0];
        }
        if (side < 3) {
            sides[2] = sides[0];
        }
        if (side < 4) {
            sides[3] = sides[1];
        }
        return sides;
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]> parseInsetsLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        Term temp = root;
        int layer = 0;
        ParsedValueImpl<ParsedValue[], Insets>[] layers = new ParsedValueImpl[nLayers];
        while (temp != null) {
            ParsedValueImpl<?, Size>[] sides = parseSize1to4(temp);
            int i2 = layer;
            layer++;
            layers[i2] = new ParsedValueImpl<>(sides, InsetsConverter.getInstance());
            while (temp.nextInSeries != null) {
                temp = temp.nextInSeries;
            }
            temp = nextLayer(temp);
        }
        return new ParsedValueImpl<>(layers, InsetsConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Insets> parseInsetsLayer(Term root) throws ParseException {
        Term temp = root;
        ParsedValueImpl<ParsedValue[], Insets> layer = null;
        while (temp != null) {
            ParsedValueImpl<?, Size>[] sides = parseSize1to4(temp);
            layer = new ParsedValueImpl<>(sides, InsetsConverter.getInstance());
            while (temp.nextInSeries != null) {
                temp = temp.nextInSeries;
            }
            temp = nextLayer(temp);
        }
        return layer;
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], Margins>[], Margins[]> parseMarginsLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        Term temp = root;
        int layer = 0;
        ParsedValueImpl<ParsedValue[], Margins>[] layers = new ParsedValueImpl[nLayers];
        while (temp != null) {
            ParsedValueImpl<?, Size>[] sides = parseSize1to4(temp);
            int i2 = layer;
            layer++;
            layers[i2] = new ParsedValueImpl<>(sides, Margins.Converter.getInstance());
            while (temp.nextInSeries != null) {
                temp = temp.nextInSeries;
            }
            temp = nextLayer(temp);
        }
        return new ParsedValueImpl<>(layers, Margins.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<Size, Size>[] parseSizeSeries(Term root) throws ParseException {
        if (root.token == null) {
            error(root, "Parse error");
        }
        List<ParsedValueImpl<Size, Size>> sizes = new ArrayList<>();
        Term term = root;
        while (true) {
            Term term2 = term;
            if (term2 != null) {
                Token token = term2.token;
                int ttype = token.getType();
                switch (ttype) {
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        ParsedValueImpl sizeValue = new ParsedValueImpl(size(token), null);
                        sizes.add(sizeValue);
                        break;
                    default:
                        error(root, "expected series of <size>");
                        break;
                }
                term = term2.nextInSeries;
            } else {
                return (ParsedValueImpl[]) sizes.toArray(new ParsedValueImpl[sizes.size()]);
            }
        }
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> parseCornerRadius(Term root) throws ParseException {
        Term temp;
        int nLayers = numberOfLayers(root);
        Term term = root;
        int layer = 0;
        ParsedValueImpl<ParsedValue<?, Size>[][], CornerRadii>[] layers = new ParsedValueImpl[nLayers];
        while (term != null) {
            int nHorizontalTerms = 0;
            Term term2 = term;
            while (true) {
                temp = term2;
                if (temp == null) {
                    break;
                }
                if (temp.token.getType() == 32) {
                    temp = temp.nextInSeries;
                    break;
                }
                nHorizontalTerms++;
                term2 = temp.nextInSeries;
            }
            int nVerticalTerms = 0;
            while (true) {
                if (temp == null) {
                    break;
                }
                if (temp.token.getType() == 32) {
                    error(temp, "unexpected SOLIDUS");
                    break;
                }
                nVerticalTerms++;
                temp = temp.nextInSeries;
            }
            if (nHorizontalTerms == 0 || nHorizontalTerms > 4 || nVerticalTerms > 4) {
                error(root, "expected [<length>|<percentage>]{1,4} [/ [<length>|<percentage>]{1,4}]?");
            }
            int orientation = 0;
            ParsedValueImpl<?, Size>[][] radii = new ParsedValueImpl[2][4];
            ParsedValueImpl<?, Size> zero = new ParsedValueImpl<>(new Size(0.0d, SizeUnits.PX), null);
            for (int r2 = 0; r2 < 4; r2++) {
                radii[0][r2] = zero;
                radii[1][r2] = zero;
            }
            int hr = 0;
            int vr = 0;
            Term lastTerm = term;
            while (hr <= 4 && vr <= 4 && term != null) {
                if (term.token.getType() == 32) {
                    orientation++;
                } else {
                    ParsedValueImpl<?, Size> parsedValue = parseSize(term);
                    if (orientation == 0) {
                        int i2 = hr;
                        hr++;
                        radii[orientation][i2] = parsedValue;
                    } else {
                        int i3 = vr;
                        vr++;
                        radii[orientation][i3] = parsedValue;
                    }
                }
                lastTerm = term;
                term = term.nextInSeries;
            }
            if (hr != 0) {
                if (hr < 2) {
                    radii[0][1] = radii[0][0];
                }
                if (hr < 3) {
                    radii[0][2] = radii[0][0];
                }
                if (hr < 4) {
                    radii[0][3] = radii[0][1];
                }
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            if (vr != 0) {
                if (vr < 2) {
                    radii[1][1] = radii[1][0];
                }
                if (vr < 3) {
                    radii[1][2] = radii[1][0];
                }
                if (vr < 4) {
                    radii[1][3] = radii[1][1];
                }
            } else {
                radii[1][0] = radii[0][0];
                radii[1][1] = radii[0][1];
                radii[1][2] = radii[0][2];
                radii[1][3] = radii[0][3];
            }
            if (zero.equals(radii[0][0]) || zero.equals(radii[1][0])) {
                ParsedValueImpl<?, Size>[] parsedValueImplArr = radii[1];
                radii[0][0] = zero;
                parsedValueImplArr[0] = zero;
            }
            if (zero.equals(radii[0][1]) || zero.equals(radii[1][1])) {
                ParsedValueImpl<?, Size>[] parsedValueImplArr2 = radii[1];
                radii[0][1] = zero;
                parsedValueImplArr2[1] = zero;
            }
            if (zero.equals(radii[0][2]) || zero.equals(radii[1][2])) {
                ParsedValueImpl<?, Size>[] parsedValueImplArr3 = radii[1];
                radii[0][2] = zero;
                parsedValueImplArr3[2] = zero;
            }
            if (zero.equals(radii[0][3]) || zero.equals(radii[1][3])) {
                ParsedValueImpl<?, Size>[] parsedValueImplArr4 = radii[1];
                radii[0][3] = zero;
                parsedValueImplArr4[3] = zero;
            }
            int i4 = layer;
            layer++;
            layers[i4] = new ParsedValueImpl<>(radii, null);
            term = nextLayer(lastTerm);
        }
        return new ParsedValueImpl<>(layers, CornerRadiiConverter.getInstance());
    }

    private static boolean isPositionKeyWord(String value) {
        return "center".equalsIgnoreCase(value) || JSplitPane.TOP.equalsIgnoreCase(value) || JSplitPane.BOTTOM.equalsIgnoreCase(value) || JSplitPane.LEFT.equalsIgnoreCase(value) || JSplitPane.RIGHT.equalsIgnoreCase(value);
    }

    private ParsedValueImpl<ParsedValue[], BackgroundPosition> parseBackgroundPosition(Term term) throws ParseException {
        if (term.token == null || term.token.getText() == null || term.token.getText().isEmpty()) {
            error(term, "Expected '<bg-position>'");
        }
        Term termOne = term;
        Token valueOne = term.token;
        Term termTwo = termOne.nextInSeries;
        Token valueTwo = termTwo != null ? termTwo.token : null;
        Term termThree = termTwo != null ? termTwo.nextInSeries : null;
        Token valueThree = termThree != null ? termThree.token : null;
        Term termFour = termThree != null ? termThree.nextInSeries : null;
        Token valueFour = termFour != null ? termFour.token : null;
        if (valueOne != null && valueTwo != null && valueThree == null && valueFour == null) {
            String v1 = valueOne.getText();
            String v2 = valueTwo.getText();
            if ((JSplitPane.TOP.equals(v1) || JSplitPane.BOTTOM.equals(v1)) && (JSplitPane.LEFT.equals(v2) || JSplitPane.RIGHT.equals(v2) || "center".equals(v2))) {
                valueTwo = valueOne;
                valueOne = valueTwo;
                termTwo = termOne;
                termOne = termTwo;
            }
        } else if (valueOne != null && valueTwo != null && valueThree != null) {
            Term[] termArray = null;
            Token[] tokeArray = null;
            if (valueFour != null) {
                if ((JSplitPane.TOP.equals(valueOne.getText()) || JSplitPane.BOTTOM.equals(valueOne.getText())) && (JSplitPane.LEFT.equals(valueThree.getText()) || JSplitPane.RIGHT.equals(valueThree.getText()))) {
                    termArray = new Term[]{termThree, termFour, termOne, termTwo};
                    tokeArray = new Token[]{valueThree, valueFour, valueOne, valueTwo};
                }
            } else if (JSplitPane.TOP.equals(valueOne.getText()) || JSplitPane.BOTTOM.equals(valueOne.getText())) {
                if (JSplitPane.LEFT.equals(valueTwo.getText()) || JSplitPane.RIGHT.equals(valueTwo.getText())) {
                    termArray = new Term[]{termTwo, termThree, termOne, null};
                    tokeArray = new Token[]{valueTwo, valueThree, valueOne, null};
                } else {
                    termArray = new Term[]{termThree, termOne, termTwo, null};
                    tokeArray = new Token[]{valueThree, valueOne, valueTwo, null};
                }
            }
            if (termArray != null) {
                termOne = termArray[0];
                termTwo = termArray[1];
                termThree = termArray[2];
                termFour = termArray[3];
                valueOne = tokeArray[0];
                valueTwo = tokeArray[1];
                valueThree = tokeArray[2];
                valueFour = tokeArray[3];
            }
        }
        ParsedValueImpl<Size, Size> parsedValueImpl = ZERO_PERCENT;
        ParsedValueImpl<?, Size> left = parsedValueImpl;
        ParsedValueImpl<?, Size> bottom = parsedValueImpl;
        ParsedValueImpl<?, Size> right = parsedValueImpl;
        ParsedValueImpl<?, Size> top = parsedValueImpl;
        if (valueOne == null && valueTwo == null && valueThree == null && valueFour == null) {
            error(term, "No value found for background-position");
        } else if (valueOne != null && valueTwo == null && valueThree == null && valueFour == null) {
            String v12 = valueOne.getText();
            if ("center".equals(v12)) {
                left = FIFTY_PERCENT;
                right = ZERO_PERCENT;
                top = FIFTY_PERCENT;
                bottom = ZERO_PERCENT;
            } else if (JSplitPane.LEFT.equals(v12)) {
                left = ZERO_PERCENT;
                right = ZERO_PERCENT;
                top = FIFTY_PERCENT;
                bottom = ZERO_PERCENT;
            } else if (JSplitPane.RIGHT.equals(v12)) {
                left = ONE_HUNDRED_PERCENT;
                right = ZERO_PERCENT;
                top = FIFTY_PERCENT;
                bottom = ZERO_PERCENT;
            } else if (JSplitPane.TOP.equals(v12)) {
                left = FIFTY_PERCENT;
                right = ZERO_PERCENT;
                top = ZERO_PERCENT;
                bottom = ZERO_PERCENT;
            } else if (JSplitPane.BOTTOM.equals(v12)) {
                left = FIFTY_PERCENT;
                right = ZERO_PERCENT;
                top = ONE_HUNDRED_PERCENT;
                bottom = ZERO_PERCENT;
            } else {
                left = parseSize(termOne);
                right = ZERO_PERCENT;
                top = FIFTY_PERCENT;
                bottom = ZERO_PERCENT;
            }
        } else if (valueOne != null && valueTwo != null && valueThree == null && valueFour == null) {
            String v13 = valueOne.getText().toLowerCase(Locale.ROOT);
            String v22 = valueTwo.getText().toLowerCase(Locale.ROOT);
            if (!isPositionKeyWord(v13)) {
                left = parseSize(termOne);
                right = ZERO_PERCENT;
                if (JSplitPane.TOP.equals(v22)) {
                    top = ZERO_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if (JSplitPane.BOTTOM.equals(v22)) {
                    top = ONE_HUNDRED_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if ("center".equals(v22)) {
                    top = FIFTY_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if (!isPositionKeyWord(v22)) {
                    top = parseSize(termTwo);
                    bottom = ZERO_PERCENT;
                } else {
                    error(termTwo, "Expected 'top', 'bottom', 'center' or <size>");
                }
            } else if (v13.equals(JSplitPane.LEFT) || v13.equals(JSplitPane.RIGHT)) {
                left = v13.equals(JSplitPane.RIGHT) ? ONE_HUNDRED_PERCENT : ZERO_PERCENT;
                right = ZERO_PERCENT;
                if (!isPositionKeyWord(v22)) {
                    top = parseSize(termTwo);
                    bottom = ZERO_PERCENT;
                } else if (v22.equals(JSplitPane.TOP) || v22.equals(JSplitPane.BOTTOM) || v22.equals("center")) {
                    if (v22.equals(JSplitPane.TOP)) {
                        top = ZERO_PERCENT;
                        bottom = ZERO_PERCENT;
                    } else if (v22.equals("center")) {
                        top = FIFTY_PERCENT;
                        bottom = ZERO_PERCENT;
                    } else {
                        top = ONE_HUNDRED_PERCENT;
                        bottom = ZERO_PERCENT;
                    }
                } else {
                    error(termTwo, "Expected 'top', 'bottom', 'center' or <size>");
                }
            } else if (v13.equals("center")) {
                left = FIFTY_PERCENT;
                right = ZERO_PERCENT;
                if (v22.equals(JSplitPane.TOP)) {
                    top = ZERO_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if (v22.equals(JSplitPane.BOTTOM)) {
                    top = ONE_HUNDRED_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if (v22.equals("center")) {
                    top = FIFTY_PERCENT;
                    bottom = ZERO_PERCENT;
                } else if (!isPositionKeyWord(v22)) {
                    top = parseSize(termTwo);
                    bottom = ZERO_PERCENT;
                } else {
                    error(termTwo, "Expected 'top', 'bottom', 'center' or <size>");
                }
            }
        } else if (valueOne != null && valueTwo != null && valueThree != null && valueFour == null) {
            String v14 = valueOne.getText().toLowerCase(Locale.ROOT);
            String v23 = valueTwo.getText().toLowerCase(Locale.ROOT);
            String v3 = valueThree.getText().toLowerCase(Locale.ROOT);
            if (!isPositionKeyWord(v14) || "center".equals(v14)) {
                if ("center".equals(v14)) {
                    left = FIFTY_PERCENT;
                } else {
                    left = parseSize(termOne);
                }
                right = ZERO_PERCENT;
                if (!isPositionKeyWord(v3)) {
                    if (JSplitPane.TOP.equals(v23)) {
                        top = parseSize(termThree);
                        bottom = ZERO_PERCENT;
                    } else if (JSplitPane.BOTTOM.equals(v23)) {
                        top = ZERO_PERCENT;
                        bottom = parseSize(termThree);
                    } else {
                        error(termTwo, "Expected 'top' or 'bottom'");
                    }
                } else {
                    error(termThree, "Expected <size>");
                }
            } else if (JSplitPane.LEFT.equals(v14) || JSplitPane.RIGHT.equals(v14)) {
                if (!isPositionKeyWord(v23)) {
                    if (JSplitPane.LEFT.equals(v14)) {
                        left = parseSize(termTwo);
                        right = ZERO_PERCENT;
                    } else {
                        left = ZERO_PERCENT;
                        right = parseSize(termTwo);
                    }
                    if (JSplitPane.TOP.equals(v3)) {
                        top = ZERO_PERCENT;
                        bottom = ZERO_PERCENT;
                    } else if (JSplitPane.BOTTOM.equals(v3)) {
                        top = ONE_HUNDRED_PERCENT;
                        bottom = ZERO_PERCENT;
                    } else if ("center".equals(v3)) {
                        top = FIFTY_PERCENT;
                        bottom = ZERO_PERCENT;
                    } else {
                        error(termThree, "Expected 'top', 'bottom' or 'center'");
                    }
                } else {
                    if (JSplitPane.LEFT.equals(v14)) {
                        left = ZERO_PERCENT;
                        right = ZERO_PERCENT;
                    } else {
                        left = ONE_HUNDRED_PERCENT;
                        right = ZERO_PERCENT;
                    }
                    if (!isPositionKeyWord(v3)) {
                        if (JSplitPane.TOP.equals(v23)) {
                            top = parseSize(termThree);
                            bottom = ZERO_PERCENT;
                        } else if (JSplitPane.BOTTOM.equals(v23)) {
                            top = ZERO_PERCENT;
                            bottom = parseSize(termThree);
                        } else {
                            error(termTwo, "Expected 'top' or 'bottom'");
                        }
                    } else {
                        error(termThree, "Expected <size>");
                    }
                }
            }
        } else {
            String v15 = valueOne.getText().toLowerCase(Locale.ROOT);
            String v24 = valueTwo.getText().toLowerCase(Locale.ROOT);
            String v32 = valueThree.getText().toLowerCase(Locale.ROOT);
            String v4 = valueFour.getText().toLowerCase(Locale.ROOT);
            if ((v15.equals(JSplitPane.LEFT) || v15.equals(JSplitPane.RIGHT)) && ((v32.equals(JSplitPane.TOP) || v32.equals(JSplitPane.BOTTOM)) && !isPositionKeyWord(v24) && !isPositionKeyWord(v4))) {
                if (v15.equals(JSplitPane.LEFT)) {
                    left = parseSize(termTwo);
                    right = ZERO_PERCENT;
                } else {
                    left = ZERO_PERCENT;
                    right = parseSize(termTwo);
                }
                if (v32.equals(JSplitPane.TOP)) {
                    top = parseSize(termFour);
                    bottom = ZERO_PERCENT;
                } else {
                    top = ZERO_PERCENT;
                    bottom = parseSize(termFour);
                }
            } else {
                error(term, "Expected 'left' or 'right' followed by <size> followed by 'top' or 'bottom' followed by <size>");
            }
        }
        ParsedValueImpl<?, Size>[] values = {top, right, bottom, left};
        return new ParsedValueImpl<>(values, BackgroundPositionConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> parseBackgroundPositionLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue[], BackgroundPosition>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBackgroundPosition(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, LayeredBackgroundPositionConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<String, BackgroundRepeat>[] parseRepeatStyle(Term root) throws ParseException {
        BackgroundRepeat backgroundRepeat = BackgroundRepeat.NO_REPEAT;
        BackgroundRepeat yAxis = backgroundRepeat;
        BackgroundRepeat xAxis = backgroundRepeat;
        if (root.token == null || root.token.getType() != 11 || root.token.getText() == null || root.token.getText().isEmpty()) {
            error(root, "Expected '<repeat-style>'");
        }
        String text = root.token.getText().toLowerCase(Locale.ROOT);
        if ("repeat-x".equals(text)) {
            xAxis = BackgroundRepeat.REPEAT;
            yAxis = BackgroundRepeat.NO_REPEAT;
        } else if ("repeat-y".equals(text)) {
            xAxis = BackgroundRepeat.NO_REPEAT;
            yAxis = BackgroundRepeat.REPEAT;
        } else if ("repeat".equals(text)) {
            xAxis = BackgroundRepeat.REPEAT;
            yAxis = BackgroundRepeat.REPEAT;
        } else if ("space".equals(text)) {
            xAxis = BackgroundRepeat.SPACE;
            yAxis = BackgroundRepeat.SPACE;
        } else if (Keywords.FUNC_ROUND_STRING.equals(text)) {
            xAxis = BackgroundRepeat.ROUND;
            yAxis = BackgroundRepeat.ROUND;
        } else if ("no-repeat".equals(text) || "stretch".equals(text)) {
            xAxis = BackgroundRepeat.NO_REPEAT;
            yAxis = BackgroundRepeat.NO_REPEAT;
        } else {
            error(root, "Expected  '<repeat-style>' " + text);
        }
        Term term = root.nextInSeries;
        if (term != null && term.token != null && term.token.getType() == 11 && term.token.getText() != null && !term.token.getText().isEmpty()) {
            String text2 = term.token.getText().toLowerCase(Locale.ROOT);
            if ("repeat-x".equals(text2)) {
                error(term, "Unexpected 'repeat-x'");
            } else if ("repeat-y".equals(text2)) {
                error(term, "Unexpected 'repeat-y'");
            } else if ("repeat".equals(text2)) {
                yAxis = BackgroundRepeat.REPEAT;
            } else if ("space".equals(text2)) {
                yAxis = BackgroundRepeat.SPACE;
            } else if (Keywords.FUNC_ROUND_STRING.equals(text2)) {
                yAxis = BackgroundRepeat.ROUND;
            } else if ("no-repeat".equals(text2) || "stretch".equals(text2)) {
                yAxis = BackgroundRepeat.NO_REPEAT;
            } else {
                error(term, "Expected  '<repeat-style>'");
            }
        }
        return new ParsedValueImpl[]{new ParsedValueImpl<>(xAxis.name(), new EnumConverter(BackgroundRepeat.class)), new ParsedValueImpl<>(yAxis.name(), new EnumConverter(BackgroundRepeat.class))};
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parseBorderImageRepeatStyleLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl[] parsedValueImplArr = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                parsedValueImplArr[i2] = parseRepeatStyle(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(parsedValueImplArr, RepeatStructConverter.getInstance());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parseBackgroundRepeatStyleLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl[] parsedValueImplArr = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                parsedValueImplArr[i2] = parseRepeatStyle(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(parsedValueImplArr, RepeatStructConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<ParsedValue[], BackgroundSize> parseBackgroundSize(Term root) throws ParseException {
        ParsedValueImpl<?, Size> height = null;
        ParsedValueImpl<?, Size> width = null;
        boolean cover = false;
        boolean contain = false;
        if (root.token == null) {
            error(root, "Expected '<bg-size>'");
        }
        if (root.token.getType() != 11) {
            if (isSize(root.token)) {
                width = parseSize(root);
                height = null;
            } else {
                error(root, "Expected '<bg-size>'");
            }
        } else {
            String text = root.token.getText() != null ? root.token.getText().toLowerCase(Locale.ROOT) : null;
            if (!"auto".equals(text)) {
                if ("cover".equals(text)) {
                    cover = true;
                } else if ("contain".equals(text)) {
                    contain = true;
                } else if ("stretch".equals(text)) {
                    width = ONE_HUNDRED_PERCENT;
                    height = ONE_HUNDRED_PERCENT;
                } else {
                    error(root, "Expected 'auto', 'cover', 'contain', or  'stretch'");
                }
            }
        }
        Term term = root.nextInSeries;
        if (term != null) {
            if (cover || contain) {
                error(term, "Unexpected '<bg-size>'");
            }
            if (term.token.getType() == 11) {
                String text2 = term.token.getText() != null ? term.token.getText().toLowerCase(Locale.ROOT) : null;
                if ("auto".equals(text2)) {
                    height = null;
                } else if ("cover".equals(text2)) {
                    error(term, "Unexpected 'cover'");
                } else if ("contain".equals(text2)) {
                    error(term, "Unexpected 'contain'");
                } else if ("stretch".equals(text2)) {
                    height = ONE_HUNDRED_PERCENT;
                } else {
                    error(term, "Expected 'auto' or 'stretch'");
                }
            } else if (isSize(term.token)) {
                height = parseSize(term);
            } else {
                error(term, "Expected '<bg-size>'");
            }
        }
        ParsedValueImpl[] values = new ParsedValueImpl[4];
        values[0] = width;
        values[1] = height;
        values[2] = new ParsedValueImpl(cover ? "true" : "false", BooleanConverter.getInstance());
        values[3] = new ParsedValueImpl(contain ? "true" : "false", BooleanConverter.getInstance());
        return new ParsedValueImpl<>(values, BackgroundSizeConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> parseBackgroundSizeLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue[], BackgroundSize>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBackgroundSize(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, LayeredBackgroundSizeConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]> parseBorderPaint(Term root) throws ParseException {
        ParsedValueImpl<?, Paint>[] paints = new ParsedValueImpl[4];
        int paint = 0;
        for (Term term = root; term != null; term = term.nextInSeries) {
            if (term.token == null || paints.length <= paint) {
                error(term, "Expected '<paint>'");
            }
            int i2 = paint;
            paint++;
            paints[i2] = parse(term);
        }
        if (paint < 2) {
            paints[1] = paints[0];
        }
        if (paint < 3) {
            paints[2] = paints[0];
        }
        if (paint < 4) {
            paints[3] = paints[1];
        }
        return new ParsedValueImpl<>(paints, StrokeBorderPaintConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> parseBorderPaintLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBorderPaint(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, LayeredBorderPaintConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> parseBorderStyleSeries(Term root) throws ParseException {
        ParsedValueImpl<ParsedValue[], BorderStrokeStyle>[] borders = new ParsedValueImpl[4];
        int border = 0;
        for (Term term = root; term != null; term = term.nextInSeries) {
            int i2 = border;
            border++;
            borders[i2] = parseBorderStyle(term);
        }
        if (border < 2) {
            borders[1] = borders[0];
        }
        if (border < 3) {
            borders[2] = borders[0];
        }
        if (border < 4) {
            borders[3] = borders[1];
        }
        return new ParsedValueImpl<>(borders, BorderStrokeStyleSequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> parseBorderStyleLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBorderStyleSeries(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, LayeredBorderStyleConverter.getInstance());
            }
        }
    }

    private String getKeyword(Term term) {
        if (term != null && term.token != null && term.token.getType() == 11 && term.token.getText() != null && !term.token.getText().isEmpty()) {
            return term.token.getText().toLowerCase(Locale.ROOT);
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], BorderStrokeStyle> parseBorderStyle(Term root) throws ParseException {
        ParsedValue<ParsedValue<?, Size>, Number> dashPhase = null;
        ParsedValue<ParsedValue<?, Size>, Number> strokeLineJoin = null;
        ParsedValue<ParsedValue<?, Size>, Number> strokeMiterLimit = null;
        ParsedValue<String, StrokeLineCap> strokeLineCap = null;
        ParsedValue<ParsedValue[], Number[]> dashStyle = dashStyle(root);
        Term prev = root;
        Term term = root.nextInSeries;
        if ("phase".equals(getKeyword(term))) {
            Term term2 = term.nextInSeries;
            if (term2 == null || term2.token == null || !isSize(term2.token)) {
                error(term2, "Expected '<size>'");
            }
            ParsedValueImpl<?, Size> sizeVal = parseSize(term2);
            dashPhase = new ParsedValueImpl<>(sizeVal, SizeConverter.getInstance());
            prev = term2;
            term = term2.nextInSeries;
        }
        ParsedValue<String, StrokeType> strokeType = parseStrokeType(term);
        if (strokeType != null) {
            prev = term;
            term = term.nextInSeries;
        }
        String keyword = getKeyword(term);
        if ("line-join".equals(keyword)) {
            Term term3 = term.nextInSeries;
            ParsedValue<ParsedValue<?, Size>, Number>[] lineJoinValues = parseStrokeLineJoin(term3);
            if (lineJoinValues != null) {
                strokeLineJoin = lineJoinValues[0];
                strokeMiterLimit = lineJoinValues[1];
            } else {
                error(term3, "Expected 'miter <size>?', 'bevel' or 'round'");
            }
            prev = term3;
            term = term3.nextInSeries;
            keyword = getKeyword(term);
        }
        if ("line-cap".equals(keyword)) {
            Term term4 = term.nextInSeries;
            strokeLineCap = parseStrokeLineCap(term4);
            if (strokeLineCap == null) {
                error(term4, "Expected 'square', 'butt' or 'round'");
            }
            prev = term4;
            term = term4.nextInSeries;
        }
        if (term != null) {
            root.nextInSeries = term;
        } else {
            root.nextInSeries = null;
            root.nextLayer = prev.nextLayer;
        }
        ParsedValue[] values = {dashStyle, dashPhase, strokeType, strokeLineJoin, strokeMiterLimit, strokeLineCap};
        return new ParsedValueImpl<>(values, BorderStyleConverter.getInstance());
    }

    private ParsedValue<ParsedValue[], Number[]> dashStyle(Term root) throws ParseException {
        if (root.token == null) {
            error(root, "Expected '<dash-style>'");
        }
        int ttype = root.token.getType();
        ParsedValue<ParsedValue[], Number[]> segments = null;
        if (ttype == 11) {
            segments = borderStyle(root);
        } else if (ttype == 12) {
            segments = segments(root);
        } else {
            error(root, "Expected '<dash-style>'");
        }
        return segments;
    }

    private ParsedValue<ParsedValue[], Number[]> borderStyle(Term root) throws ParseException {
        if (root.token == null || root.token.getType() != 11 || root.token.getText() == null || root.token.getText().isEmpty()) {
            error(root, "Expected '<border-style>'");
        }
        String text = root.token.getText().toLowerCase(Locale.ROOT);
        if (Separation.COLORANT_NONE.equals(text)) {
            return BorderStyleConverter.NONE;
        }
        if ("hidden".equals(text)) {
            return BorderStyleConverter.NONE;
        }
        if ("dotted".equals(text)) {
            return BorderStyleConverter.DOTTED;
        }
        if ("dashed".equals(text)) {
            return BorderStyleConverter.DASHED;
        }
        if ("solid".equals(text)) {
            return BorderStyleConverter.SOLID;
        }
        if (SchemaSymbols.ATTVAL_DOUBLE.equals(text)) {
            error(root, "Unsupported <border-style> 'double'");
        } else if ("groove".equals(text)) {
            error(root, "Unsupported <border-style> 'groove'");
        } else if ("ridge".equals(text)) {
            error(root, "Unsupported <border-style> 'ridge'");
        } else if ("inset".equals(text)) {
            error(root, "Unsupported <border-style> 'inset'");
        } else if ("outset".equals(text)) {
            error(root, "Unsupported <border-style> 'outset'");
        } else {
            error(root, "Unsupported <border-style> '" + text + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return BorderStyleConverter.SOLID;
    }

    private ParsedValueImpl<ParsedValue[], Number[]> segments(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"segments".regionMatches(true, 0, fn, 0, 8)) {
            error(root, "Expected 'segments'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(null, "Expected '<size>'");
        }
        int nArgs = numberOfArgs(root);
        ParsedValueImpl<?, Size>[] segments = new ParsedValueImpl[nArgs];
        int segment = 0;
        while (arg != null) {
            int i2 = segment;
            segment++;
            segments[i2] = parseSize(arg);
            arg = arg.nextArg;
        }
        return new ParsedValueImpl<>(segments, SizeConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<String, StrokeType> parseStrokeType(Term root) throws ParseException {
        String keyword = getKeyword(root);
        if ("centered".equals(keyword) || "inside".equals(keyword) || "outside".equals(keyword)) {
            return new ParsedValueImpl<>(keyword, new EnumConverter(StrokeType.class));
        }
        return null;
    }

    private ParsedValueImpl[] parseStrokeLineJoin(Term root) throws ParseException {
        Term next;
        String keyword = getKeyword(root);
        if ("miter".equals(keyword) || "bevel".equals(keyword) || Keywords.FUNC_ROUND_STRING.equals(keyword)) {
            ParsedValueImpl<String, StrokeLineJoin> strokeLineJoin = new ParsedValueImpl<>(keyword, new EnumConverter(StrokeLineJoin.class));
            ParsedValueImpl<ParsedValue<?, Size>, Number> strokeMiterLimit = null;
            if ("miter".equals(keyword) && (next = root.nextInSeries) != null && next.token != null && isSize(next.token)) {
                root.nextInSeries = next.nextInSeries;
                ParsedValueImpl<?, Size> sizeVal = parseSize(next);
                strokeMiterLimit = new ParsedValueImpl<>(sizeVal, SizeConverter.getInstance());
            }
            return new ParsedValueImpl[]{strokeLineJoin, strokeMiterLimit};
        }
        return null;
    }

    private ParsedValueImpl<String, StrokeLineCap> parseStrokeLineCap(Term root) throws ParseException {
        String keyword = getKeyword(root);
        if ("square".equals(keyword) || "butt".equals(keyword) || Keywords.FUNC_ROUND_STRING.equals(keyword)) {
            return new ParsedValueImpl<>(keyword, new EnumConverter(StrokeLineCap.class));
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], BorderImageSlices> parseBorderImageSlice(Term root) throws ParseException {
        Term term = root;
        if (term.token == null || !isSize(term.token)) {
            error(term, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size>[] insets = new ParsedValueImpl[4];
        Boolean fill = Boolean.FALSE;
        int inset = 0;
        while (true) {
            if (inset < 4 && term != null) {
                int i2 = inset;
                inset++;
                insets[i2] = parseSize(term);
                Term term2 = term.nextInSeries;
                term = term2;
                if (term2 != null && term.token != null && term.token.getType() == 11 && "fill".equalsIgnoreCase(term.token.getText())) {
                    fill = Boolean.TRUE;
                    break;
                }
            } else {
                break;
            }
        }
        if (inset < 2) {
            insets[1] = insets[0];
        }
        if (inset < 3) {
            insets[2] = insets[0];
        }
        if (inset < 4) {
            insets[3] = insets[1];
        }
        ParsedValueImpl[] values = {new ParsedValueImpl(insets, InsetsConverter.getInstance()), new ParsedValueImpl(fill, null)};
        return new ParsedValueImpl<>(values, BorderImageSliceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> parseBorderImageSliceLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue[], BorderImageSlices>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBorderImageSlice(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, SliceSequenceConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<ParsedValue[], BorderWidths> parseBorderImageWidth(Term root) throws ParseException {
        Term term = root;
        if (term.token == null || !isSize(term.token)) {
            error(term, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size>[] insets = new ParsedValueImpl[4];
        int inset = 0;
        while (inset < 4 && term != null) {
            int i2 = inset;
            inset++;
            insets[i2] = parseSize(term);
            Term term2 = term.nextInSeries;
            term = term2;
            if (term2 == null || term.token == null || term.token.getType() == 11) {
            }
        }
        if (inset < 2) {
            insets[1] = insets[0];
        }
        if (inset < 3) {
            insets[2] = insets[0];
        }
        if (inset < 4) {
            insets[3] = insets[1];
        }
        return new ParsedValueImpl<>(insets, BorderImageWidthConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> parseBorderImageWidthLayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        ParsedValueImpl<ParsedValue[], BorderWidths>[] layers = new ParsedValueImpl[nLayers];
        int layer = 0;
        Term termNextLayer = root;
        while (true) {
            Term term = termNextLayer;
            if (term != null) {
                int i2 = layer;
                layer++;
                layers[i2] = parseBorderImageWidth(term);
                termNextLayer = nextLayer(term);
            } else {
                return new ParsedValueImpl<>(layers, BorderImageWidthsSequenceConverter.getInstance());
            }
        }
    }

    private ParsedValueImpl<String, String> parseRegion(Term root) throws ParseException {
        String fn = root.token != null ? root.token.getText() : null;
        if (!"region".regionMatches(true, 0, fn, 0, 6)) {
            error(root, "Expected 'region'");
        }
        Term arg = root.firstArg;
        if (arg == null) {
            error(root, "Expected 'region(\"<styleclass-or-id-string>\")'");
        }
        if (arg.token == null || arg.token.getType() != 10 || arg.token.getText() == null || arg.token.getText().isEmpty()) {
            error(root, "Expected 'region(\"<styleclass-or-id-string>\")'");
        }
        String styleClassOrId = SPECIAL_REGION_URL_PREFIX + Utils.stripQuotes(arg.token.getText());
        return new ParsedValueImpl<>(styleClassOrId, StringConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], String> parseURI(Term root) throws ParseException {
        if (root == null) {
            error(root, "Expected 'url(\"<uri-string>\")'");
        }
        if (root.token == null || root.token.getType() != 43 || root.token.getText() == null || root.token.getText().isEmpty()) {
            error(root, "Expected 'url(\"<uri-string>\")'");
        }
        String uri = root.token.getText();
        ParsedValueImpl[] uriValues = {new ParsedValueImpl(uri, StringConverter.getInstance()), null};
        return new ParsedValueImpl<>(uriValues, URLConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], String>[], String[]> parseURILayers(Term root) throws ParseException {
        int nLayers = numberOfLayers(root);
        Term temp = root;
        int layer = 0;
        ParsedValueImpl<ParsedValue[], String>[] layers = new ParsedValueImpl[nLayers];
        while (temp != null) {
            int i2 = layer;
            layer++;
            layers[i2] = parseURI(temp);
            temp = nextLayer(temp);
        }
        return new ParsedValueImpl<>(layers, URLConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<?, Size>, Number> parseFontSize(Term root) throws ParseException {
        if (root == null) {
            return null;
        }
        Token token = root.token;
        if (token == null || !isSize(token)) {
            error(root, "Expected '<font-size>'");
        }
        Size size = null;
        if (token.getType() == 11) {
            String ident = token.getText().toLowerCase(Locale.ROOT);
            double value = -1.0d;
            if ("inherit".equals(ident)) {
                value = 100.0d;
            } else if ("xx-small".equals(ident)) {
                value = 60.0d;
            } else if ("x-small".equals(ident)) {
                value = 75.0d;
            } else if (NimbusStyle.SMALL_KEY.equals(ident)) {
                value = 80.0d;
            } else if ("medium".equals(ident)) {
                value = 100.0d;
            } else if (NimbusStyle.LARGE_KEY.equals(ident)) {
                value = 120.0d;
            } else if ("x-large".equals(ident)) {
                value = 150.0d;
            } else if ("xx-large".equals(ident)) {
                value = 200.0d;
            } else if ("smaller".equals(ident)) {
                value = 80.0d;
            } else if ("larger".equals(ident)) {
                value = 120.0d;
            }
            if (value > -1.0d) {
                size = new Size(value, SizeUnits.PERCENT);
            }
        }
        if (size == null) {
            size = size(token);
        }
        ParsedValueImpl<?, Size> svalue = new ParsedValueImpl<>(size, null);
        return new ParsedValueImpl<>(svalue, FontConverter.FontSizeConverter.getInstance());
    }

    private ParsedValueImpl<String, FontPosture> parseFontStyle(Term root) throws ParseException {
        String posture;
        if (root == null) {
            return null;
        }
        Token token = root.token;
        if (token == null || token.getType() != 11 || token.getText() == null || token.getText().isEmpty()) {
            error(root, "Expected '<font-style>'");
        }
        String ident = token.getText().toLowerCase(Locale.ROOT);
        FontPosture.REGULAR.name();
        if ("normal".equals(ident)) {
            posture = FontPosture.REGULAR.name();
        } else if ("italic".equals(ident) || "oblique".equals(ident)) {
            posture = FontPosture.ITALIC.name();
        } else if ("inherit".equals(ident)) {
            posture = "inherit";
        } else {
            return null;
        }
        return new ParsedValueImpl<>(posture, FontConverter.FontStyleConverter.getInstance());
    }

    private ParsedValueImpl<String, FontWeight> parseFontWeight(Term root) throws ParseException {
        if (root == null) {
            return null;
        }
        Token token = root.token;
        if (token == null || token.getText() == null || token.getText().isEmpty()) {
            error(root, "Expected '<font-weight>'");
        }
        String ident = token.getText().toLowerCase(Locale.ROOT);
        String weight = FontWeight.NORMAL.name();
        if ("inherit".equals(ident) || "normal".equals(ident)) {
            weight = FontWeight.NORMAL.name();
        } else if ("bold".equals(ident) || "bolder".equals(ident)) {
            weight = FontWeight.BOLD.name();
        } else if ("lighter".equals(ident)) {
            weight = FontWeight.LIGHT.name();
        } else if ("100".equals(ident)) {
            weight = FontWeight.findByWeight(100).name();
        } else if ("200".equals(ident)) {
            weight = FontWeight.findByWeight(200).name();
        } else if ("300".equals(ident)) {
            weight = FontWeight.findByWeight(300).name();
        } else if ("400".equals(ident)) {
            weight = FontWeight.findByWeight(400).name();
        } else if ("500".equals(ident)) {
            weight = FontWeight.findByWeight(500).name();
        } else if ("600".equals(ident)) {
            weight = FontWeight.findByWeight(600).name();
        } else if ("700".equals(ident)) {
            weight = FontWeight.findByWeight(Font2D.FWEIGHT_BOLD).name();
        } else if ("800".equals(ident)) {
            weight = FontWeight.findByWeight(800).name();
        } else if ("900".equals(ident)) {
            weight = FontWeight.findByWeight(900).name();
        } else {
            error(root, "Expected '<font-weight>'");
        }
        return new ParsedValueImpl<>(weight, FontConverter.FontWeightConverter.getInstance());
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0033 A[PHI: r8
  0x0033: PHI (r8v1 'text' java.lang.String) = 
  (r8v0 'text' java.lang.String)
  (r8v3 'text' java.lang.String)
  (r8v3 'text' java.lang.String)
  (r8v0 'text' java.lang.String)
 binds: [B:7:0x000e, B:13:0x0029, B:15:0x0030, B:11:0x0020] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.sun.javafx.css.ParsedValueImpl<java.lang.String, java.lang.String> parseFontFamily(com.sun.javafx.css.parser.CSSParser.Term r6) throws com.sun.javafx.css.parser.CSSParser.ParseException {
        /*
            r5 = this;
            r0 = r6
            if (r0 != 0) goto L6
            r0 = 0
            return r0
        L6:
            r0 = r6
            com.sun.javafx.css.parser.Token r0 = r0.token
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = r7
            if (r0 == 0) goto L33
            r0 = r7
            int r0 = r0.getType()
            r1 = 11
            if (r0 == r1) goto L23
            r0 = r7
            int r0 = r0.getType()
            r1 = 10
            if (r0 != r1) goto L33
        L23:
            r0 = r7
            java.lang.String r0 = r0.getText()
            r1 = r0
            r8 = r1
            if (r0 == 0) goto L33
            r0 = r8
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L3b
        L33:
            r0 = r5
            r1 = r6
            java.lang.String r2 = "Expected '<font-family>'"
            r0.error(r1, r2)
        L3b:
            r0 = r5
            r1 = r8
            java.util.Locale r2 = java.util.Locale.ROOT
            java.lang.String r1 = r1.toLowerCase(r2)
            java.lang.String r0 = r0.stripQuotes(r1)
            r9 = r0
            java.lang.String r0 = "inherit"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L5f
            com.sun.javafx.css.ParsedValueImpl r0 = new com.sun.javafx.css.ParsedValueImpl
            r1 = r0
            java.lang.String r2 = "inherit"
            javafx.css.StyleConverter r3 = com.sun.javafx.css.converters.StringConverter.getInstance()
            r1.<init>(r2, r3)
            return r0
        L5f:
            java.lang.String r0 = "serif"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "sans-serif"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "cursive"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "fantasy"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "monospace"
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La3
        L96:
            com.sun.javafx.css.ParsedValueImpl r0 = new com.sun.javafx.css.ParsedValueImpl
            r1 = r0
            r2 = r9
            javafx.css.StyleConverter r3 = com.sun.javafx.css.converters.StringConverter.getInstance()
            r1.<init>(r2, r3)
            return r0
        La3:
            com.sun.javafx.css.ParsedValueImpl r0 = new com.sun.javafx.css.ParsedValueImpl
            r1 = r0
            r2 = r7
            java.lang.String r2 = r2.getText()
            javafx.css.StyleConverter r3 = com.sun.javafx.css.converters.StringConverter.getInstance()
            r1.<init>(r2, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSParser.parseFontFamily(com.sun.javafx.css.parser.CSSParser$Term):com.sun.javafx.css.ParsedValueImpl");
    }

    private ParsedValueImpl<ParsedValue[], Font> parseFont(Term root) throws ParseException {
        Term next = root.nextInSeries;
        root.nextInSeries = null;
        while (next != null) {
            Term temp = next.nextInSeries;
            next.nextInSeries = root;
            root = next;
            next = temp;
        }
        Token token = root.token;
        int ttype = token.getType();
        if (ttype != 11 && ttype != 10) {
            error(root, "Expected '<font-family>'");
        }
        ParsedValueImpl<String, String> ffamily = parseFontFamily(root);
        Term term = root.nextInSeries;
        Term term2 = term;
        if (term == null) {
            error(root, "Expected '<size>'");
        }
        if (term2.token == null || !isSize(term2.token)) {
            error(term2, "Expected '<size>'");
        }
        Term temp2 = term2.nextInSeries;
        if (temp2 != null && temp2.token != null && temp2.token.getType() == 32) {
            root = temp2;
            Term term3 = temp2.nextInSeries;
            term2 = term3;
            if (term3 == null) {
                error(root, "Expected '<size>'");
            }
            if (term2.token == null || !isSize(term2.token)) {
                error(term2, "Expected '<size>'");
            }
            Token token2 = term2.token;
        }
        ParsedValueImpl<ParsedValue<?, Size>, Number> fsize = parseFontSize(term2);
        if (fsize == null) {
            error(root, "Expected '<size>'");
        }
        ParsedValueImpl<String, FontPosture> fstyle = null;
        ParsedValueImpl<String, FontWeight> fweight = null;
        String fvariant = null;
        while (true) {
            Term term4 = term2.nextInSeries;
            term2 = term4;
            if (term4 != null) {
                if (term2.token == null || term2.token.getType() != 11 || term2.token.getText() == null || term2.token.getText().isEmpty()) {
                    error(term2, "Expected '<font-weight>', '<font-style>' or '<font-variant>'");
                }
                if (fstyle == null) {
                    ParsedValueImpl<String, FontPosture> fontStyle = parseFontStyle(term2);
                    fstyle = fontStyle;
                    if (fontStyle != null) {
                    }
                }
                if (fvariant == null && "small-caps".equalsIgnoreCase(term2.token.getText())) {
                    fvariant = term2.token.getText();
                } else if (fweight == null) {
                    ParsedValueImpl<String, FontWeight> fontWeight = parseFontWeight(term2);
                    fweight = fontWeight;
                    if (fontWeight != null) {
                    }
                }
            } else {
                ParsedValueImpl[] values = {ffamily, fsize, fweight, fstyle};
                return new ParsedValueImpl<>(values, FontConverter.getInstance());
            }
        }
    }

    private Token nextToken(CSSLexer lexer) {
        Token token;
        while (true) {
            token = lexer.nextToken();
            if (token == null || token.getType() != 40) {
                if (token.getType() != 41) {
                    break;
                }
            }
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
            LOGGER.finest(token.toString());
        }
        return token;
    }

    private void parse(Stylesheet stylesheet, CSSLexer lexer) {
        this.currentToken = nextToken(lexer);
        while (this.currentToken != null && this.currentToken.getType() == 47) {
            this.currentToken = nextToken(lexer);
            if (this.currentToken == null || this.currentToken.getType() != 11) {
                ParseException parseException = new ParseException("Expected IDENT", this.currentToken, this);
                String msg = parseException.toString();
                CssError error = createError(msg);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(error.toString());
                }
                reportError(error);
                while (true) {
                    this.currentToken = lexer.nextToken();
                    if (this.currentToken == null || this.currentToken.getType() != 30) {
                        if (this.currentToken.getType() == 40 || this.currentToken.getType() == 41) {
                        }
                    }
                }
            } else {
                String keyword = this.currentToken.getText().toLowerCase(Locale.ROOT);
                if ("font-face".equals(keyword)) {
                    FontFace fontFace = fontFace(lexer);
                    if (fontFace != null) {
                        stylesheet.getFontFaces().add(fontFace);
                    }
                    this.currentToken = nextToken(lexer);
                } else if ("import".equals(keyword)) {
                    if (imports == null) {
                        imports = new Stack<>();
                    }
                    if (!imports.contains(this.sourceOfStylesheet)) {
                        imports.push(this.sourceOfStylesheet);
                        Stylesheet importedStylesheet = handleImport(lexer);
                        if (importedStylesheet != null) {
                            stylesheet.importStylesheet(importedStylesheet);
                        }
                        imports.pop();
                        if (imports.isEmpty()) {
                            imports = null;
                        }
                    } else {
                        int line = this.currentToken.getLine();
                        int pos = this.currentToken.getOffset();
                        String msg2 = MessageFormat.format("Recursive @import at {2} [{0,number,#},{1,number,#}]", Integer.valueOf(line), Integer.valueOf(pos), imports.peek());
                        CssError error2 = createError(msg2);
                        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                            LOGGER.warning(error2.toString());
                        }
                        reportError(error2);
                    }
                    while (true) {
                        this.currentToken = lexer.nextToken();
                        if (this.currentToken == null || this.currentToken.getType() != 30) {
                            if (this.currentToken.getType() == 40 || this.currentToken.getType() == 41) {
                            }
                        }
                    }
                }
            }
        }
        while (this.currentToken != null && this.currentToken.getType() != -1) {
            List<Selector> selectors = selectors(lexer);
            if (selectors == null) {
                return;
            }
            if (this.currentToken == null || this.currentToken.getType() != 28) {
                int line2 = this.currentToken != null ? this.currentToken.getLine() : -1;
                int pos2 = this.currentToken != null ? this.currentToken.getOffset() : -1;
                String msg3 = MessageFormat.format("Expected LBRACE at [{0,number,#},{1,number,#}]", Integer.valueOf(line2), Integer.valueOf(pos2));
                CssError error3 = createError(msg3);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(error3.toString());
                }
                reportError(error3);
                this.currentToken = null;
                return;
            }
            this.currentToken = nextToken(lexer);
            List<Declaration> declarations = declarations(lexer);
            if (declarations == null) {
                return;
            }
            if (this.currentToken != null && this.currentToken.getType() != 29) {
                int line3 = this.currentToken.getLine();
                int pos3 = this.currentToken.getOffset();
                String msg4 = MessageFormat.format("Expected RBRACE at [{0,number,#},{1,number,#}]", Integer.valueOf(line3), Integer.valueOf(pos3));
                CssError error4 = createError(msg4);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(error4.toString());
                }
                reportError(error4);
                this.currentToken = null;
                return;
            }
            stylesheet.getRules().add(new Rule(selectors, declarations));
            this.currentToken = nextToken(lexer);
        }
        this.currentToken = null;
    }

    private FontFace fontFace(CSSLexer lexer) {
        Map<String, String> descriptors = new HashMap<>();
        List<FontFace.FontFaceSrc> sources = new ArrayList<>();
        do {
            this.currentToken = nextToken(lexer);
            if (this.currentToken.getType() == 11) {
                String key = this.currentToken.getText();
                this.currentToken = nextToken(lexer);
                this.currentToken = nextToken(lexer);
                if ("src".equalsIgnoreCase(key)) {
                    while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
                        if (this.currentToken.getType() == 11) {
                            sources.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.REFERENCE, this.currentToken.getText()));
                        } else if (this.currentToken.getType() == 43) {
                            ParsedValueImpl[] uriValues = {new ParsedValueImpl(this.currentToken.getText(), StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, null)};
                            ParsedValue<ParsedValue[], String> parsedValue = new ParsedValueImpl<>(uriValues, URLConverter.getInstance());
                            String urlStr = parsedValue.convert(null);
                            URL url = null;
                            try {
                                URI fontUri = new URI(urlStr);
                                url = fontUri.toURL();
                            } catch (MalformedURLException | URISyntaxException e2) {
                                int line = this.currentToken.getLine();
                                int pos = this.currentToken.getOffset();
                                String msg = MessageFormat.format("Could not resolve @font-face url [{2}] at [{0,number,#},{1,number,#}]", Integer.valueOf(line), Integer.valueOf(pos), urlStr);
                                CssError error = createError(msg);
                                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                                    LOGGER.warning(error.toString());
                                }
                                reportError(error);
                                while (this.currentToken != null) {
                                    int ttype = this.currentToken.getType();
                                    if (ttype == 29 || ttype == -1) {
                                        return null;
                                    }
                                    this.currentToken = nextToken(lexer);
                                }
                            }
                            String format = null;
                            while (true) {
                                this.currentToken = nextToken(lexer);
                                int ttype2 = this.currentToken != null ? this.currentToken.getType() : -1;
                                if (ttype2 == 12) {
                                    if (!"format(".equalsIgnoreCase(this.currentToken.getText())) {
                                        break;
                                    }
                                } else if (ttype2 == 11 || ttype2 == 10) {
                                    format = Utils.stripQuotes(this.currentToken.getText());
                                } else if (ttype2 != 35) {
                                    break;
                                }
                            }
                            sources.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.URL, url.toExternalForm(), format));
                        } else if (this.currentToken.getType() == 12) {
                            if ("local(".equalsIgnoreCase(this.currentToken.getText())) {
                                this.currentToken = nextToken(lexer);
                                StringBuilder localSb = new StringBuilder();
                                while (this.currentToken != null && this.currentToken.getType() != 35 && this.currentToken.getType() != -1) {
                                    localSb.append(this.currentToken.getText());
                                    this.currentToken = nextToken(lexer);
                                }
                                int start = 0;
                                int end = localSb.length();
                                if (localSb.charAt(0) == '\'' || localSb.charAt(0) == '\"') {
                                    start = 0 + 1;
                                }
                                if (localSb.charAt(end - 1) == '\'' || localSb.charAt(end - 1) == '\"') {
                                    end--;
                                }
                                String local = localSb.substring(start, end);
                                sources.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.LOCAL, local));
                            } else {
                                int line2 = this.currentToken.getLine();
                                int pos2 = this.currentToken.getOffset();
                                String msg2 = MessageFormat.format("Unknown @font-face src type [" + this.currentToken.getText() + ")] at [{0,number,#},{1,number,#}]", Integer.valueOf(line2), Integer.valueOf(pos2));
                                CssError error2 = createError(msg2);
                                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                                    LOGGER.warning(error2.toString());
                                }
                                reportError(error2);
                            }
                        } else if (this.currentToken.getType() != 36) {
                            int line3 = this.currentToken.getLine();
                            int pos3 = this.currentToken.getOffset();
                            String msg3 = MessageFormat.format("Unexpected TOKEN [" + this.currentToken.getText() + "] at [{0,number,#},{1,number,#}]", Integer.valueOf(line3), Integer.valueOf(pos3));
                            CssError error3 = createError(msg3);
                            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                                LOGGER.warning(error3.toString());
                            }
                            reportError(error3);
                        }
                        this.currentToken = nextToken(lexer);
                    }
                } else {
                    StringBuilder descriptorVal = new StringBuilder();
                    while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != -1) {
                        descriptorVal.append(this.currentToken.getText());
                        this.currentToken = nextToken(lexer);
                    }
                    descriptors.put(key, descriptorVal.toString());
                }
            }
            if (this.currentToken == null || this.currentToken.getType() == 29) {
                break;
            }
        } while (this.currentToken.getType() != -1);
        return new FontFace(descriptors, sources);
    }

    private Stylesheet handleImport(CSSLexer lexer) {
        this.currentToken = nextToken(lexer);
        if (this.currentToken == null || this.currentToken.getType() == -1) {
            return null;
        }
        int ttype = this.currentToken.getType();
        String fname = null;
        if (ttype == 10 || ttype == 43) {
            fname = this.currentToken.getText();
        }
        Stylesheet importedStylesheet = null;
        String _sourceOfStylesheet = this.sourceOfStylesheet;
        if (fname != null) {
            ParsedValueImpl[] uriValues = {new ParsedValueImpl(fname, StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, null)};
            ParsedValue<ParsedValue[], String> parsedValue = new ParsedValueImpl<>(uriValues, URLConverter.getInstance());
            String urlString = parsedValue.convert(null);
            importedStylesheet = StyleManager.loadStylesheet(urlString);
            this.sourceOfStylesheet = _sourceOfStylesheet;
        }
        if (importedStylesheet == null) {
            String msg = MessageFormat.format("Could not import {0}", fname);
            CssError error = createError(msg);
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(error.toString());
            }
            reportError(error);
        }
        return importedStylesheet;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0057, code lost:
    
        r4.currentToken = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005d, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<com.sun.javafx.css.Selector> selectors(com.sun.javafx.css.parser.CSSLexer r5) {
        /*
            r4 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r6 = r0
        L8:
            r0 = r4
            r1 = r5
            com.sun.javafx.css.Selector r0 = r0.selector(r1)
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L5e
        L12:
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            if (r0 == 0) goto L3c
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            int r0 = r0.getType()
            r1 = 29
            if (r0 == r1) goto L3c
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            int r0 = r0.getType()
            r1 = -1
            if (r0 == r1) goto L3c
            r0 = r4
            r1 = r4
            r2 = r5
            com.sun.javafx.css.parser.Token r1 = r1.nextToken(r2)
            r0.currentToken = r1
            goto L12
        L3c:
            r0 = r4
            r1 = r4
            r2 = r5
            com.sun.javafx.css.parser.Token r1 = r1.nextToken(r2)
            r0.currentToken = r1
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            if (r0 == 0) goto L57
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            int r0 = r0.getType()
            r1 = -1
            if (r0 != r1) goto L8
        L57:
            r0 = r4
            r1 = 0
            r0.currentToken = r1
            r0 = 0
            return r0
        L5e:
            r0 = r6
            r1 = r7
            boolean r0 = r0.add(r1)
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            if (r0 == 0) goto L85
            r0 = r4
            com.sun.javafx.css.parser.Token r0 = r0.currentToken
            int r0 = r0.getType()
            r1 = 36
            if (r0 != r1) goto L85
            r0 = r4
            r1 = r4
            r2 = r5
            com.sun.javafx.css.parser.Token r1 = r1.nextToken(r2)
            r0.currentToken = r1
            goto L8
        L85:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSParser.selectors(com.sun.javafx.css.parser.CSSLexer):java.util.List");
    }

    private Selector selector(CSSLexer lexer) {
        List<Combinator> combinators = null;
        List<SimpleSelector> sels = null;
        SimpleSelector ancestor = simpleSelector(lexer);
        if (ancestor == null) {
            return null;
        }
        while (true) {
            Combinator comb = combinator(lexer);
            if (comb != null) {
                if (combinators == null) {
                    combinators = new ArrayList<>();
                }
                combinators.add(comb);
                SimpleSelector descendant = simpleSelector(lexer);
                if (descendant == null) {
                    return null;
                }
                if (sels == null) {
                    sels = new ArrayList<>();
                    sels.add(ancestor);
                }
                sels.add(descendant);
            } else {
                if (this.currentToken != null && this.currentToken.getType() == 41) {
                    this.currentToken = nextToken(lexer);
                }
                if (sels == null) {
                    return ancestor;
                }
                return new CompoundSelector(sels, combinators);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0127, code lost:
    
        r7.currentToken = com.sun.javafx.css.parser.Token.INVALID_TOKEN;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x012f, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.sun.javafx.css.SimpleSelector simpleSelector(com.sun.javafx.css.parser.CSSLexer r8) {
        /*
            Method dump skipped, instructions count: 482
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSParser.simpleSelector(com.sun.javafx.css.parser.CSSLexer):com.sun.javafx.css.SimpleSelector");
    }

    private String functionalPseudo(CSSLexer lexer) {
        StringBuilder pclass = new StringBuilder(this.currentToken.getText());
        while (true) {
            this.currentToken = nextToken(lexer);
            switch (this.currentToken.getType()) {
                case 10:
                case 11:
                    pclass.append(this.currentToken.getText());
                case 35:
                    pclass.append(')');
                    return pclass.toString();
                default:
                    this.currentToken = Token.INVALID_TOKEN;
                    return null;
            }
        }
    }

    private Combinator combinator(CSSLexer lexer) {
        Combinator combinator = null;
        while (true) {
            int ttype = this.currentToken != null ? this.currentToken.getType() : 0;
            switch (ttype) {
                case 11:
                case 31:
                case 33:
                case 37:
                case 38:
                    return combinator;
                case 27:
                    combinator = Combinator.CHILD;
                    break;
                case 40:
                    if (combinator == null && " ".equals(this.currentToken.getText())) {
                        combinator = Combinator.DESCENDANT;
                        break;
                    }
                    break;
                default:
                    return null;
            }
            this.currentToken = lexer.nextToken();
            if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
                LOGGER.finest(this.currentToken.toString());
            }
        }
    }

    private List<Declaration> declarations(CSSLexer lexer) {
        List<Declaration> declarations = new ArrayList<>();
        do {
            Declaration decl = declaration(lexer);
            if (decl != null) {
                declarations.add(decl);
            } else {
                while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
                    this.currentToken = nextToken(lexer);
                }
                if (this.currentToken != null && this.currentToken.getType() != 30) {
                    return declarations;
                }
            }
            while (this.currentToken != null && this.currentToken.getType() == 30) {
                this.currentToken = nextToken(lexer);
            }
            if (this.currentToken == null) {
                break;
            }
        } while (this.currentToken.getType() == 11);
        return declarations;
    }

    private Declaration declaration(CSSLexer lexer) {
        ParsedValueImpl parsedValueImplValueFor;
        int type = this.currentToken != null ? this.currentToken.getType() : 0;
        if (this.currentToken == null || this.currentToken.getType() != 11) {
            return null;
        }
        String property = this.currentToken.getText();
        this.currentToken = nextToken(lexer);
        if (this.currentToken == null || this.currentToken.getType() != 31) {
            int line = this.currentToken.getLine();
            int pos = this.currentToken.getOffset();
            String msg = MessageFormat.format("Expected COLON at [{0,number,#},{1,number,#}]", Integer.valueOf(line), Integer.valueOf(pos));
            CssError error = createError(msg);
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(error.toString());
            }
            reportError(error);
            return null;
        }
        this.currentToken = nextToken(lexer);
        Term root = expr(lexer);
        if (root != null) {
            try {
                parsedValueImplValueFor = valueFor(property, root, lexer);
            } catch (ParseException re) {
                Token badToken = re.tok;
                int line2 = badToken != null ? badToken.getLine() : -1;
                int pos2 = badToken != null ? badToken.getOffset() : -1;
                String msg2 = MessageFormat.format("{2} while parsing ''{3}'' at [{0,number,#},{1,number,#}]", Integer.valueOf(line2), Integer.valueOf(pos2), re.getMessage(), property);
                CssError error2 = createError(msg2);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(error2.toString());
                }
                reportError(error2);
                return null;
            }
        } else {
            parsedValueImplValueFor = null;
        }
        ParsedValueImpl value = parsedValueImplValueFor;
        boolean important = this.currentToken.getType() == 39;
        if (important) {
            this.currentToken = nextToken(lexer);
        }
        Declaration decl = value != null ? new Declaration(property.toLowerCase(Locale.ROOT), value, important) : null;
        return decl;
    }

    private Term expr(CSSLexer lexer) {
        Term expr = term(lexer);
        Term term = expr;
        while (true) {
            Term current = term;
            int ttype = (current == null || this.currentToken == null) ? 0 : this.currentToken.getType();
            if (ttype == 0) {
                skipExpr(lexer);
                return null;
            }
            if (ttype == 30 || ttype == 39 || ttype == 29 || ttype == -1) {
                break;
            }
            if (ttype == 36) {
                this.currentToken = nextToken(lexer);
                Term term2 = term(lexer);
                term = term2;
                current.nextLayer = term2;
            } else {
                Term term3 = term(lexer);
                term = term3;
                current.nextInSeries = term3;
            }
        }
        return expr;
    }

    private void skipExpr(CSSLexer lexer) {
        int ttype;
        do {
            this.currentToken = nextToken(lexer);
            ttype = this.currentToken != null ? this.currentToken.getType() : 0;
            if (ttype == 30 || ttype == 29) {
                return;
            }
        } while (ttype != -1);
    }

    private Term term(CSSLexer lexer) {
        int ttype = this.currentToken != null ? this.currentToken.getType() : 0;
        switch (ttype) {
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 32:
            case 37:
            case 43:
            case 45:
            case 46:
                Term term = new Term(this.currentToken);
                this.currentToken = nextToken(lexer);
                return term;
            case 12:
            case 34:
                Term function = new Term(this.currentToken);
                this.currentToken = nextToken(lexer);
                Term arg = term(lexer);
                function.firstArg = arg;
                while (true) {
                    int operator = this.currentToken != null ? this.currentToken.getType() : 0;
                    if (operator == 35) {
                        this.currentToken = nextToken(lexer);
                        return function;
                    }
                    if (operator == 36) {
                        this.currentToken = nextToken(lexer);
                        Term term2 = term(lexer);
                        arg.nextArg = term2;
                        arg = term2;
                    } else {
                        Term term3 = term(lexer);
                        arg.nextInSeries = term3;
                        arg = term3;
                    }
                }
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 35:
            case 36:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 44:
            default:
                int line = this.currentToken != null ? this.currentToken.getLine() : -1;
                int pos = this.currentToken != null ? this.currentToken.getOffset() : -1;
                String text = this.currentToken != null ? this.currentToken.getText() : "";
                String msg = MessageFormat.format("Unexpected token {0}{1}{0} at [{2,number,#},{3,number,#}]", PdfOps.SINGLE_QUOTE_TOKEN, text, Integer.valueOf(line), Integer.valueOf(pos));
                CssError error = createError(msg);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(error.toString());
                }
                reportError(error);
                return null;
        }
    }
}
