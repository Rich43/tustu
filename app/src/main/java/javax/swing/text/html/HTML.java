package javax.swing.text.html;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.icepdf.core.util.PdfOps;
import sun.security.x509.PolicyMappingsExtension;

/* loaded from: rt.jar:javax/swing/text/html/HTML.class */
public class HTML {
    private static final Hashtable<String, Tag> tagHashtable = new Hashtable<>(73);
    private static final Hashtable<Object, Tag> scMapping = new Hashtable<>(8);
    public static final String NULL_ATTRIBUTE_VALUE = "#DEFAULT";
    private static final Hashtable<String, Attribute> attHashtable;

    /* loaded from: rt.jar:javax/swing/text/html/HTML$Tag.class */
    public static class Tag {
        boolean blockTag;
        boolean breakTag;
        String name;
        boolean unknown;

        /* renamed from: A, reason: collision with root package name */
        public static final Tag f12846A = new Tag("a");
        public static final Tag ADDRESS = new Tag("address");
        public static final Tag APPLET = new Tag("applet");
        public static final Tag AREA = new Tag("area");

        /* renamed from: B, reason: collision with root package name */
        public static final Tag f12847B = new Tag(PdfOps.b_TOKEN);
        public static final Tag BASE = new Tag("base");
        public static final Tag BASEFONT = new Tag("basefont");
        public static final Tag BIG = new Tag("big");
        public static final Tag BLOCKQUOTE = new Tag("blockquote", true, true);
        public static final Tag BODY = new Tag("body", true, true);
        public static final Tag BR = new Tag("br", true, false);
        public static final Tag CAPTION = new Tag("caption");
        public static final Tag CENTER = new Tag("center", true, false);
        public static final Tag CITE = new Tag("cite");
        public static final Tag CODE = new Tag("code");
        public static final Tag DD = new Tag("dd", true, true);
        public static final Tag DFN = new Tag("dfn");
        public static final Tag DIR = new Tag("dir", true, true);
        public static final Tag DIV = new Tag("div", true, true);
        public static final Tag DL = new Tag("dl", true, true);
        public static final Tag DT = new Tag("dt", true, true);
        public static final Tag EM = new Tag("em");
        public static final Tag FONT = new Tag("font");
        public static final Tag FORM = new Tag("form", true, false);
        public static final Tag FRAME = new Tag("frame");
        public static final Tag FRAMESET = new Tag("frameset");
        public static final Tag H1 = new Tag("h1", true, true);
        public static final Tag H2 = new Tag("h2", true, true);
        public static final Tag H3 = new Tag("h3", true, true);
        public static final Tag H4 = new Tag("h4", true, true);
        public static final Tag H5 = new Tag("h5", true, true);
        public static final Tag H6 = new Tag("h6", true, true);
        public static final Tag HEAD = new Tag("head", true, true);
        public static final Tag HR = new Tag("hr", true, false);
        public static final Tag HTML = new Tag("html", true, false);

        /* renamed from: I, reason: collision with root package name */
        public static final Tag f12848I = new Tag(PdfOps.i_TOKEN);
        public static final Tag IMG = new Tag("img");
        public static final Tag INPUT = new Tag("input");
        public static final Tag ISINDEX = new Tag("isindex", true, false);
        public static final Tag KBD = new Tag("kbd");
        public static final Tag LI = new Tag("li", true, true);
        public static final Tag LINK = new Tag("link");
        public static final Tag MAP = new Tag(PolicyMappingsExtension.MAP);
        public static final Tag MENU = new Tag("menu", true, true);
        public static final Tag META = new Tag("meta");
        static final Tag NOBR = new Tag("nobr");
        public static final Tag NOFRAMES = new Tag("noframes", true, true);
        public static final Tag OBJECT = new Tag("object");
        public static final Tag OL = new Tag("ol", true, true);
        public static final Tag OPTION = new Tag("option");

        /* renamed from: P, reason: collision with root package name */
        public static final Tag f12849P = new Tag("p", true, true);
        public static final Tag PARAM = new Tag(Constants.ELEMNAME_PARAMVARIABLE_STRING);
        public static final Tag PRE = new Tag("pre", true, true);
        public static final Tag SAMP = new Tag("samp");
        public static final Tag SCRIPT = new Tag("script");
        public static final Tag SELECT = new Tag(Constants.ATTRNAME_SELECT);
        public static final Tag SMALL = new Tag(NimbusStyle.SMALL_KEY);
        public static final Tag SPAN = new Tag("span");
        public static final Tag STRIKE = new Tag("strike");

        /* renamed from: S, reason: collision with root package name */
        public static final Tag f12850S = new Tag(PdfOps.s_TOKEN);
        public static final Tag STRONG = new Tag("strong");
        public static final Tag STYLE = new Tag(Constants.ATTRNAME_STYLE);
        public static final Tag SUB = new Tag("sub");
        public static final Tag SUP = new Tag("sup");
        public static final Tag TABLE = new Tag("table", false, true);
        public static final Tag TD = new Tag("td", true, true);
        public static final Tag TEXTAREA = new Tag("textarea");
        public static final Tag TH = new Tag("th", true, true);
        public static final Tag TITLE = new Tag("title", true, true);
        public static final Tag TR = new Tag("tr", false, true);
        public static final Tag TT = new Tag("tt");

        /* renamed from: U, reason: collision with root package name */
        public static final Tag f12851U = new Tag("u");
        public static final Tag UL = new Tag("ul", true, true);
        public static final Tag VAR = new Tag("var");
        public static final Tag IMPLIED = new Tag("p-implied");
        public static final Tag CONTENT = new Tag(AbstractDocument.ContentElementName);
        public static final Tag COMMENT = new Tag("comment");
        static final Tag[] allTags = {f12846A, ADDRESS, APPLET, AREA, f12847B, BASE, BASEFONT, BIG, BLOCKQUOTE, BODY, BR, CAPTION, CENTER, CITE, CODE, DD, DFN, DIR, DIV, DL, DT, EM, FONT, FORM, FRAME, FRAMESET, H1, H2, H3, H4, H5, H6, HEAD, HR, HTML, f12848I, IMG, INPUT, ISINDEX, KBD, LI, LINK, MAP, MENU, META, NOBR, NOFRAMES, OBJECT, OL, OPTION, f12849P, PARAM, PRE, SAMP, SCRIPT, SELECT, SMALL, SPAN, STRIKE, f12850S, STRONG, STYLE, SUB, SUP, TABLE, TD, TEXTAREA, TH, TITLE, TR, TT, f12851U, UL, VAR};

        public Tag() {
        }

        protected Tag(String str) {
            this(str, false, false);
        }

        protected Tag(String str, boolean z2, boolean z3) {
            this.name = str;
            this.breakTag = z2;
            this.blockTag = z3;
        }

        public boolean isBlock() {
            return this.blockTag;
        }

        public boolean breaksFlow() {
            return this.breakTag;
        }

        public boolean isPreformatted() {
            return this == PRE || this == TEXTAREA;
        }

        public String toString() {
            return this.name;
        }

        boolean isParagraph() {
            return this == f12849P || this == IMPLIED || this == DT || this == H1 || this == H2 || this == H3 || this == H4 || this == H5 || this == H6;
        }

        static {
            HTML.getTag("html");
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTML$UnknownTag.class */
    public static class UnknownTag extends Tag implements Serializable {
        public UnknownTag(String str) {
            super(str);
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof UnknownTag) {
                return toString().equals(obj.toString());
            }
            return false;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeBoolean(this.blockTag);
            objectOutputStream.writeBoolean(this.breakTag);
            objectOutputStream.writeBoolean(this.unknown);
            objectOutputStream.writeObject(this.name);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.blockTag = objectInputStream.readBoolean();
            this.breakTag = objectInputStream.readBoolean();
            this.unknown = objectInputStream.readBoolean();
            this.name = (String) objectInputStream.readObject();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTML$Attribute.class */
    public static final class Attribute {
        private String name;
        public static final Attribute SIZE = new Attribute("size");
        public static final Attribute COLOR = new Attribute("color");
        public static final Attribute CLEAR = new Attribute(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.CLEAR_ATTRIBUTES);
        public static final Attribute BACKGROUND = new Attribute("background");
        public static final Attribute BGCOLOR = new Attribute("bgcolor");
        public static final Attribute TEXT = new Attribute("text");
        public static final Attribute LINK = new Attribute("link");
        public static final Attribute VLINK = new Attribute("vlink");
        public static final Attribute ALINK = new Attribute("alink");
        public static final Attribute WIDTH = new Attribute(MetadataParser.WIDTH_TAG_NAME);
        public static final Attribute HEIGHT = new Attribute(MetadataParser.HEIGHT_TAG_NAME);
        public static final Attribute ALIGN = new Attribute("align");
        public static final Attribute NAME = new Attribute("name");
        public static final Attribute HREF = new Attribute(Constants.ATTRNAME_HREF);
        public static final Attribute REL = new Attribute("rel");
        public static final Attribute REV = new Attribute("rev");
        public static final Attribute TITLE = new Attribute("title");
        public static final Attribute TARGET = new Attribute("target");
        public static final Attribute SHAPE = new Attribute("shape");
        public static final Attribute COORDS = new Attribute("coords");
        public static final Attribute ISMAP = new Attribute("ismap");
        public static final Attribute NOHREF = new Attribute("nohref");
        public static final Attribute ALT = new Attribute("alt");
        public static final Attribute ID = new Attribute("id");
        public static final Attribute SRC = new Attribute("src");
        public static final Attribute HSPACE = new Attribute("hspace");
        public static final Attribute VSPACE = new Attribute("vspace");
        public static final Attribute USEMAP = new Attribute("usemap");
        public static final Attribute LOWSRC = new Attribute("lowsrc");
        public static final Attribute CODEBASE = new Attribute(Constants.ATTRNAME_CODEBASE);
        public static final Attribute CODE = new Attribute("code");
        public static final Attribute ARCHIVE = new Attribute(Constants.ATTRNAME_ARCHIVE);
        public static final Attribute VALUE = new Attribute("value");
        public static final Attribute VALUETYPE = new Attribute("valuetype");
        public static final Attribute TYPE = new Attribute("type");
        public static final Attribute CLASS = new Attribute(Constants.ATTRNAME_CLASS);
        public static final Attribute STYLE = new Attribute(Constants.ATTRNAME_STYLE);
        public static final Attribute LANG = new Attribute("lang");
        public static final Attribute FACE = new Attribute("face");
        public static final Attribute DIR = new Attribute("dir");
        public static final Attribute DECLARE = new Attribute("declare");
        public static final Attribute CLASSID = new Attribute(Constants.ATTRNAME_CLASSID);
        public static final Attribute DATA = new Attribute("data");
        public static final Attribute CODETYPE = new Attribute("codetype");
        public static final Attribute STANDBY = new Attribute("standby");
        public static final Attribute BORDER = new Attribute("border");
        public static final Attribute SHAPES = new Attribute("shapes");
        public static final Attribute NOSHADE = new Attribute("noshade");
        public static final Attribute COMPACT = new Attribute("compact");
        public static final Attribute START = new Attribute("start");
        public static final Attribute ACTION = new Attribute("action");
        public static final Attribute METHOD = new Attribute("method");
        public static final Attribute ENCTYPE = new Attribute("enctype");
        public static final Attribute CHECKED = new Attribute("checked");
        public static final Attribute MAXLENGTH = new Attribute("maxlength");
        public static final Attribute MULTIPLE = new Attribute(Constants.ATTRVAL_MULTI);
        public static final Attribute SELECTED = new Attribute(JInternalFrame.IS_SELECTED_PROPERTY);
        public static final Attribute ROWS = new Attribute("rows");
        public static final Attribute COLS = new Attribute("cols");
        public static final Attribute DUMMY = new Attribute("dummy");
        public static final Attribute CELLSPACING = new Attribute("cellspacing");
        public static final Attribute CELLPADDING = new Attribute("cellpadding");
        public static final Attribute VALIGN = new Attribute("valign");
        public static final Attribute HALIGN = new Attribute("halign");
        public static final Attribute NOWRAP = new Attribute("nowrap");
        public static final Attribute ROWSPAN = new Attribute("rowspan");
        public static final Attribute COLSPAN = new Attribute("colspan");
        public static final Attribute PROMPT = new Attribute("prompt");
        public static final Attribute HTTPEQUIV = new Attribute("http-equiv");
        public static final Attribute CONTENT = new Attribute(AbstractDocument.ContentElementName);
        public static final Attribute LANGUAGE = new Attribute("language");
        public static final Attribute VERSION = new Attribute("version");

        /* renamed from: N, reason: collision with root package name */
        public static final Attribute f12845N = new Attribute(PdfOps.n_TOKEN);
        public static final Attribute FRAMEBORDER = new Attribute("frameborder");
        public static final Attribute MARGINWIDTH = new Attribute("marginwidth");
        public static final Attribute MARGINHEIGHT = new Attribute("marginheight");
        public static final Attribute SCROLLING = new Attribute("scrolling");
        public static final Attribute NORESIZE = new Attribute("noresize");
        public static final Attribute ENDTAG = new Attribute("endtag");
        public static final Attribute COMMENT = new Attribute("comment");
        static final Attribute MEDIA = new Attribute("media");
        static final Attribute[] allAttributes = {FACE, COMMENT, SIZE, COLOR, CLEAR, BACKGROUND, BGCOLOR, TEXT, LINK, VLINK, ALINK, WIDTH, HEIGHT, ALIGN, NAME, HREF, REL, REV, TITLE, TARGET, SHAPE, COORDS, ISMAP, NOHREF, ALT, ID, SRC, HSPACE, VSPACE, USEMAP, LOWSRC, CODEBASE, CODE, ARCHIVE, VALUE, VALUETYPE, TYPE, CLASS, STYLE, LANG, DIR, DECLARE, CLASSID, DATA, CODETYPE, STANDBY, BORDER, SHAPES, NOSHADE, COMPACT, START, ACTION, METHOD, ENCTYPE, CHECKED, MAXLENGTH, MULTIPLE, SELECTED, ROWS, COLS, DUMMY, CELLSPACING, CELLPADDING, VALIGN, HALIGN, NOWRAP, ROWSPAN, COLSPAN, PROMPT, HTTPEQUIV, CONTENT, LANGUAGE, VERSION, f12845N, FRAMEBORDER, MARGINWIDTH, MARGINHEIGHT, SCROLLING, NORESIZE, MEDIA, ENDTAG};

        Attribute(String str) {
            this.name = str;
        }

        public String toString() {
            return this.name;
        }
    }

    static {
        for (int i2 = 0; i2 < Tag.allTags.length; i2++) {
            tagHashtable.put(Tag.allTags[i2].toString(), Tag.allTags[i2]);
            StyleContext.registerStaticAttributeKey(Tag.allTags[i2]);
        }
        StyleContext.registerStaticAttributeKey(Tag.IMPLIED);
        StyleContext.registerStaticAttributeKey(Tag.CONTENT);
        StyleContext.registerStaticAttributeKey(Tag.COMMENT);
        for (int i3 = 0; i3 < Attribute.allAttributes.length; i3++) {
            StyleContext.registerStaticAttributeKey(Attribute.allAttributes[i3]);
        }
        StyleContext.registerStaticAttributeKey(NULL_ATTRIBUTE_VALUE);
        scMapping.put(StyleConstants.Bold, Tag.f12847B);
        scMapping.put(StyleConstants.Italic, Tag.f12848I);
        scMapping.put(StyleConstants.Underline, Tag.f12851U);
        scMapping.put(StyleConstants.StrikeThrough, Tag.STRIKE);
        scMapping.put(StyleConstants.Superscript, Tag.SUP);
        scMapping.put(StyleConstants.Subscript, Tag.SUB);
        scMapping.put(StyleConstants.FontFamily, Tag.FONT);
        scMapping.put(StyleConstants.FontSize, Tag.FONT);
        attHashtable = new Hashtable<>(77);
        for (int i4 = 0; i4 < Attribute.allAttributes.length; i4++) {
            attHashtable.put(Attribute.allAttributes[i4].toString(), Attribute.allAttributes[i4]);
        }
    }

    public static Tag[] getAllTags() {
        Tag[] tagArr = new Tag[Tag.allTags.length];
        System.arraycopy(Tag.allTags, 0, tagArr, 0, Tag.allTags.length);
        return tagArr;
    }

    public static Tag getTag(String str) {
        Tag tag = tagHashtable.get(str);
        if (tag == null) {
            return null;
        }
        return tag;
    }

    static Tag getTagForStyleConstantsKey(StyleConstants styleConstants) {
        return scMapping.get(styleConstants);
    }

    public static int getIntegerAttributeValue(AttributeSet attributeSet, Attribute attribute, int i2) {
        int iIntValue = i2;
        String str = (String) attributeSet.getAttribute(attribute);
        if (str != null) {
            try {
                iIntValue = Integer.valueOf(str).intValue();
            } catch (NumberFormatException e2) {
                iIntValue = i2;
            }
        }
        return iIntValue;
    }

    public static Attribute[] getAllAttributeKeys() {
        Attribute[] attributeArr = new Attribute[Attribute.allAttributes.length];
        System.arraycopy(Attribute.allAttributes, 0, attributeArr, 0, Attribute.allAttributes.length);
        return attributeArr;
    }

    public static Attribute getAttributeKey(String str) {
        Attribute attribute = attHashtable.get(str);
        if (attribute == null) {
            return null;
        }
        return attribute;
    }
}
