package org.icepdf.core.pobjects.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFGenerator;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.fonts.FontManager;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.TextSprite;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FillDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.PostScriptEncoder;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TextSpriteDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/FreeTextAnnotation.class */
public class FreeTextAnnotation extends MarkupAnnotation {
    public static final int QUADDING_LEFT_JUSTIFIED = 0;
    public static final int QUADDING_CENTER_JUSTIFIED = 1;
    public static final int QUADDING_RIGHT_JUSTIFIED = 2;
    protected static Color defaultFontColor;
    protected static Color defaultFillColor;
    protected static int defaultFontSize;
    protected String defaultAppearance;
    protected int quadding;
    protected String defaultStylingString;
    protected boolean hideRenderedOutput;
    protected String richText;
    private String fontName;
    private int fontStyle;
    private int fontSize;
    private Color fontColor;
    private boolean fillType;
    private Color fillColor;
    private boolean strokeType;
    protected DefaultStyledDocument document;
    protected FontFile fontFile;
    protected boolean fontPropertyChanged;
    public static final String BODY_START = "<?xml version=\"1.0\"?><body xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\" xfa:APIVersion=\"Acrobat:11.0.0\" xfa:spec=\"2.0.2\"  style=\"{0}\">";
    public static final String BODY_END = "</body>";
    private static final Logger logger = Logger.getLogger(FreeTextAnnotation.class.toString());
    public static final Name DA_KEY = new Name("DA");
    public static final Name Q_KEY = new Name("Q");
    public static final Name DS_KEY = new Name("DS");
    public static final Name CL_KEY = new Name("CL");
    public static final Name BE_KEY = new Name("BE");
    public static final Name RD_KEY = new Name("RD");
    public static final Name BS_KEY = new Name("BS");
    public static final Name LE_KEY = new Name("LE");
    public static final Name EMBEDDED_FONT_NAME = new Name("ice1");

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.freeText.font.color", "#000000");
            int colorValue = ColorUtil.convertColor(color);
            defaultFontColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("000000", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading free text annotation font colour");
            }
        }
        try {
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.annotation.freeText.fill.color", "#ffffff");
            int colorValue2 = ColorUtil.convertColor(color2);
            defaultFillColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("ffffff", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading free text annotation fill colour");
            }
        }
        try {
            defaultFontSize = Defs.sysPropertyInt("org.icepdf.core.views.page.annotation.freeText.font.size", 24);
        } catch (NumberFormatException e4) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading free text annotation fill colour");
            }
        }
    }

    public FreeTextAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
        this.quadding = 0;
        this.fontName = RTFGenerator.defaultFontFamily;
        this.fontStyle = 0;
        this.fontSize = defaultFontSize;
        this.fontColor = defaultFontColor;
        this.fillType = false;
        this.fillColor = defaultFillColor;
        this.strokeType = false;
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation, org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        if (this.matrix == null) {
            this.matrix = new AffineTransform();
        }
        if (this.library.getObject(this.entries, COLOR_KEY) != null && getObject(APPEARANCE_STREAM_KEY) != null) {
            if (this.shapes != null) {
                Color currentColor = Color.BLACK;
                Iterator i$ = this.shapes.getShapes().iterator();
                while (i$.hasNext()) {
                    DrawCmd drawCmd = i$.next();
                    if (drawCmd instanceof ColorDrawCmd) {
                        currentColor = ((ColorDrawCmd) drawCmd).getColor();
                    } else if (drawCmd instanceof FillDrawCmd) {
                        this.fillType = true;
                        this.fillColor = new Color(currentColor.getRGB());
                    } else if (drawCmd instanceof DrawDrawCmd) {
                        this.strokeType = true;
                        this.color = new Color(currentColor.getRGB());
                    }
                }
            }
        } else {
            this.color = Color.BLACK;
        }
        this.defaultAppearance = this.library.getString(this.entries, DA_KEY);
        if (this.library.getObject(this.entries, Q_KEY) != null) {
            this.quadding = this.library.getInt(this.entries, Q_KEY);
        }
        Object tmp = this.library.getObject(this.entries, RC_KEY);
        if (tmp != null && (tmp instanceof StringObject)) {
            StringObject tmpRichText = (StringObject) tmp;
            this.richText = tmpRichText.getLiteralString();
        }
        if (this.library.getObject(this.entries, DS_KEY) != null) {
            this.defaultStylingString = this.library.getString(this.entries, DS_KEY);
        }
        if (this.library.getObject(this.entries, Q_KEY) == null) {
            this.entries.put(Q_KEY, 0);
        }
        if (this.library.getObject(this.entries, IT_KEY) == null) {
            this.entries.put(IT_KEY, new Name("FreeText"));
        }
        if (this.defaultStylingString != null) {
            StringTokenizer toker = new StringTokenizer(this.defaultStylingString, ";");
            while (toker.hasMoreElements()) {
                String cssProperty = (String) toker.nextElement2();
                if (cssProperty != null && cssProperty.contains("font-family")) {
                    this.fontName = cssProperty.substring(cssProperty.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim();
                } else if (cssProperty != null && cssProperty.contains("color")) {
                    String colorString = cssProperty.substring(cssProperty.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim();
                    this.fontColor = new Color(ColorUtil.convertColor(colorString));
                } else if (cssProperty != null && cssProperty.contains("font-weight")) {
                    String fontStyle = cssProperty.substring(cssProperty.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim();
                    if (fontStyle.equals("normal")) {
                        this.fontStyle = 0;
                    } else if (fontStyle.equals("italic")) {
                        this.fontStyle = 2;
                    } else if (fontStyle.equals("bold")) {
                        this.fontStyle = 1;
                    }
                } else if (cssProperty != null && cssProperty.contains("font-size")) {
                    String fontSize = cssProperty.substring(cssProperty.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1).trim();
                    String fontSize2 = fontSize.substring(0, fontSize.indexOf(112));
                    try {
                        this.fontSize = (int) Float.parseFloat(fontSize2);
                    } catch (NumberFormatException e2) {
                        logger.finer("Error parsing font size: " + fontSize2);
                    }
                }
            }
        }
    }

    public static FreeTextAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_FREE_TEXT);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        FreeTextAnnotation freeTextAnnotation = new FreeTextAnnotation(library, entries);
        freeTextAnnotation.init();
        freeTextAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        freeTextAnnotation.setNew(true);
        freeTextAnnotation.setFlag(64, false);
        freeTextAnnotation.setFlag(16, false);
        freeTextAnnotation.setFlag(8, false);
        freeTextAnnotation.setFlag(4, true);
        return freeTextAnnotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void render(Graphics2D origG, int renderHintType, float totalRotation, float userZoom, boolean tabSelected) {
        if (!this.hideRenderedOutput) {
            super.render(origG, renderHintType, totalRotation, userZoom, tabSelected);
        }
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
        BasicStroke stroke;
        Font newFont;
        this.matrix = new AffineTransform();
        if (this.shapes == null) {
            this.shapes = new Shapes();
        }
        this.shapes.getShapes().clear();
        AffineTransform af2 = new AffineTransform();
        af2.scale(1.0d, -1.0d);
        af2.translate(-this.bbox.getMinX(), -this.bbox.getMaxY());
        af2.translate(5.0d, -5.0d);
        this.shapes.add(new TransformDrawCmd(af2));
        if (this.fontFile == null || this.fontPropertyChanged) {
            this.fontFile = FontManager.getInstance().getInstance(this.fontName, 0);
            this.fontPropertyChanged = false;
        }
        this.fontFile = this.fontFile.deriveFont(this.fontSize);
        this.fontFile.echarAdvance(' ');
        TextSprite textSprites = new TextSprite(this.fontFile, this.content.length(), new AffineTransform(new AffineTransform()));
        textSprites.setRMode(0);
        textSprites.setStrokeColor(this.fontColor);
        textSprites.setFontName(EMBEDDED_FONT_NAME.toString());
        textSprites.setFontSize(this.fontSize);
        StringBuilder contents = new StringBuilder(this.content);
        float lineHeight = (float) (this.fontFile.getAscent() + this.fontFile.getDescent());
        float advanceX = (float) this.bbox.getMinX();
        float advanceY = (float) this.bbox.getMinY();
        float currentY = advanceY + lineHeight;
        float lastx = 0.0f;
        int max = contents.length();
        for (int i2 = 0; i2 < max; i2++) {
            char currentChar = contents.charAt(i2);
            float newAdvanceX = (float) this.fontFile.echarAdvance(currentChar).getX();
            float currentX = advanceX + lastx;
            lastx += newAdvanceX;
            if (currentChar != '\n' && currentChar != '\r') {
                textSprites.addText(String.valueOf(currentChar), String.valueOf(currentChar), currentX, currentY, newAdvanceX);
            } else {
                currentY += lineHeight;
                advanceX = (float) this.bbox.getMinX();
                lastx = 0.0f;
            }
        }
        if (this.strokeType && this.borderStyle.isStyleDashed()) {
            stroke = new BasicStroke(this.borderStyle.getStrokeWidth(), 0, 0, this.borderStyle.getStrokeWidth() * 2.0f, this.borderStyle.getDashArray(), 0.0f);
        } else {
            stroke = new BasicStroke(this.borderStyle.getStrokeWidth());
        }
        this.shapes.add(new ShapeDrawCmd(new Rectangle2D.Double(this.bbox.getX(), this.bbox.getY() + 10.0d, this.bbox.getWidth() - 10.0d, this.bbox.getHeight() - 10.0d)));
        if (this.fillType) {
            this.shapes.add(new ColorDrawCmd(this.fillColor));
            this.shapes.add(new FillDrawCmd());
        }
        if (this.strokeType) {
            this.shapes.add(new StrokeDrawCmd(stroke));
            this.shapes.add(new ColorDrawCmd(this.color));
            this.shapes.add(new DrawDrawCmd());
        }
        this.shapes.add(new ColorDrawCmd(this.fontColor));
        this.shapes.add(new TextSpriteDrawCmd(textSprites));
        StateManager stateManager = this.library.getStateManager();
        Form form = null;
        if (hasAppearanceStream()) {
            Stream stream = getAppearanceStream();
            if (stream instanceof Form) {
                form = (Form) stream;
            } else if (stream != null) {
                form = new Form(this.library, stream.getEntries(), null);
                form.setPObjectReference(stream.getPObjectReference());
                form.setRawBytes(stream.getDecodedStreamBytes());
                form.init();
            }
        } else {
            HashMap<Object, Object> formEntries = new HashMap<>();
            formEntries.put(Form.TYPE_KEY, Form.TYPE_VALUE);
            formEntries.put(Form.SUBTYPE_KEY, Form.SUB_TYPE_VALUE);
            form = new Form(this.library, formEntries, null);
            form.setPObjectReference(stateManager.getNewReferencNumber());
            this.library.addObject(form, form.getPObjectReference());
        }
        if (form != null) {
            Rectangle2D formBbox = new Rectangle2D.Float(0.0f, 0.0f, (float) this.bbox.getWidth(), (float) this.bbox.getHeight());
            form.setAppearance(this.shapes, this.matrix, formBbox);
            stateManager.addChange(new PObject(form, form.getPObjectReference()));
            form.setRawBytes(PostScriptEncoder.generatePostScript(this.shapes.getShapes()));
            HashMap<Object, Object> appearanceRefs = new HashMap<>();
            appearanceRefs.put(APPEARANCE_STREAM_NORMAL_KEY, form.getPObjectReference());
            this.entries.put(APPEARANCE_STREAM_KEY, appearanceRefs);
            if (compressAppearanceStream) {
                form.getEntries().put(Stream.FILTER_KEY, new Name("FlateDecode"));
            } else {
                form.getEntries().remove(Stream.FILTER_KEY);
            }
            HashMap<Object, Object> fontDictionary = new HashMap<>();
            fontDictionary.put(Font.TYPE_KEY, Font.SUBTYPE_KEY);
            fontDictionary.put(Font.SUBTYPE_KEY, new Name("Type1"));
            fontDictionary.put(Font.NAME_KEY, EMBEDDED_FONT_NAME);
            fontDictionary.put(Font.BASEFONT_KEY, new Name(this.fontName));
            fontDictionary.put(Font.ENCODING_KEY, new Name("WinAnsiEncoding"));
            fontDictionary.put(new Name("FirstChar"), 32);
            fontDictionary.put(new Name("LastChar"), 255);
            if (form.getResources() == null || form.getResources().getFont(EMBEDDED_FONT_NAME) == null) {
                newFont = new org.icepdf.core.pobjects.fonts.ofont.Font(this.library, fontDictionary);
                newFont.setPObjectReference(stateManager.getNewReferencNumber());
                HashMap<Object, Object> fontResources = new HashMap<>();
                fontResources.put(EMBEDDED_FONT_NAME, newFont.getPObjectReference());
                HashMap<Object, Object> resources = new HashMap<>();
                resources.put(new Name("Font"), fontResources);
                form.getEntries().put(new Name("Resources"), resources);
                form.setRawBytes(new String().getBytes());
                form.init();
            } else {
                form.init();
                Reference reference = form.getResources().getFont(EMBEDDED_FONT_NAME).getPObjectReference();
                newFont = new org.icepdf.core.pobjects.fonts.ofont.Font(this.library, fontDictionary);
                newFont.setPObjectReference(reference);
            }
            stateManager.addChange(new PObject(newFont, newFont.getPObjectReference()));
            this.library.addObject(newFont, newFont.getPObjectReference());
        }
        StringBuilder dsString = new StringBuilder("font-size:").append(this.fontSize).append("pt;").append("font-family:").append(this.fontName).append(";").append("color:").append(ColorUtil.convertColorToRGB(this.fontColor)).append(";");
        if (this.fontStyle == 1) {
            dsString.append("font-weight:bold;");
        }
        if (this.fontStyle == 2) {
            dsString.append("font-style:italic;");
        }
        if (this.fontStyle == 0) {
            dsString.append("font-style:normal;");
        }
        this.entries.put(DS_KEY, new LiteralStringObject(dsString.toString()));
        if (this.fillType) {
            Color color = this.color;
            if (this.entries.get(APPEARANCE_STREAM_KEY) == null) {
                color = this.fillColor;
            }
            float[] compArray = new float[3];
            color.getColorComponents(compArray);
            List<Float> colorValues = new ArrayList<>(compArray.length);
            for (float comp : compArray) {
                colorValues.add(Float.valueOf(comp));
            }
            this.entries.put(COLOR_KEY, colorValues);
        } else {
            this.entries.remove(COLOR_KEY);
        }
        this.entries.put(CONTENTS_KEY, new LiteralStringObject(this.content));
        Object[] colorArgument = {dsString};
        MessageFormat formatter = new MessageFormat(BODY_START);
        StringBuilder rcString = new StringBuilder(formatter.format(colorArgument));
        String[] lines = this.content.split("[\\r\\n]+");
        for (String line : lines) {
            rcString.append("<p>").append(line).append("</p>");
        }
        rcString.append(BODY_END);
        this.entries.put(RC_KEY, new LiteralStringObject(rcString.toString()));
    }

    public String getDefaultStylingString() {
        return this.defaultStylingString;
    }

    public void clearShapes() {
        this.shapes = null;
    }

    public void setDocument(DefaultStyledDocument document) {
        this.document = document;
    }

    public boolean isHideRenderedOutput() {
        return this.hideRenderedOutput;
    }

    public void setHideRenderedOutput(boolean hideRenderedOutput) {
        this.hideRenderedOutput = hideRenderedOutput;
    }

    public String getDefaultAppearance() {
        return this.defaultAppearance;
    }

    public void setDefaultAppearance(String defaultAppearance) {
        this.defaultAppearance = defaultAppearance;
    }

    public int getQuadding() {
        return this.quadding;
    }

    public void setQuadding(int quadding) {
        this.quadding = quadding;
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation
    public String getRichText() {
        return this.richText;
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation
    public void setRichText(String richText) {
        this.richText = richText;
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = new Color(fontColor.getRGB());
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = new Color(fillColor.getRGB());
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        this.fontPropertyChanged = true;
    }

    public int getFontStyle() {
        return this.fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.fontPropertyChanged = true;
    }

    public boolean isFillType() {
        return this.fillType;
    }

    public boolean isFontPropertyChanged() {
        return this.fontPropertyChanged;
    }

    public void setFillType(boolean fillType) {
        this.fillType = fillType;
    }

    public boolean isStrokeType() {
        return this.strokeType;
    }

    public void setStrokeType(boolean strokeType) {
        this.strokeType = strokeType;
    }
}
