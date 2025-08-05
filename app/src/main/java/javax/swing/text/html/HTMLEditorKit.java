package javax.swing.text.html;

import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import javafx.fxml.FXMLLoader;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JViewport;
import javax.swing.SizeRequirements;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.Highlighter;
import javax.swing.text.IconView;
import javax.swing.text.JTextComponent;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TextAction;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import org.apache.commons.net.ftp.FTP;
import sun.awt.AppContext;
import sun.swing.SwingAccessor;

/* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit.class */
public class HTMLEditorKit extends StyledEditorKit implements Accessible {
    private JEditorPane theEditor;
    public static final String DEFAULT_CSS = "default.css";
    private AccessibleContext accessibleContext;
    MutableAttributeSet input;
    public static final String BOLD_ACTION = "html-bold-action";
    public static final String ITALIC_ACTION = "html-italic-action";
    public static final String PARA_INDENT_LEFT = "html-para-indent-left";
    public static final String PARA_INDENT_RIGHT = "html-para-indent-right";
    public static final String FONT_CHANGE_BIGGER = "html-font-bigger";
    public static final String FONT_CHANGE_SMALLER = "html-font-smaller";
    public static final String COLOR_ACTION = "html-color-action";
    public static final String LOGICAL_STYLE_ACTION = "html-logical-style-action";
    public static final String IMG_ALIGN_TOP = "html-image-align-top";
    public static final String IMG_ALIGN_MIDDLE = "html-image-align-middle";
    public static final String IMG_ALIGN_BOTTOM = "html-image-align-bottom";
    public static final String IMG_BORDER = "html-image-border";
    private static final String INSERT_HR_HTML = "<hr>";
    private Object linkNavigationTag;
    private static final Cursor MoveCursor = Cursor.getPredefinedCursor(12);
    private static final Cursor DefaultCursor = Cursor.getPredefinedCursor(0);
    private static final ViewFactory defaultFactory = new HTMLFactory();
    private static final Object DEFAULT_STYLES_KEY = new Object();
    private static Parser defaultParser = null;
    private static final NavigateLinkAction nextLinkAction = new NavigateLinkAction("next-link-action");
    private static final NavigateLinkAction previousLinkAction = new NavigateLinkAction("previous-link-action");
    private static final ActivateLinkAction activateLinkAction = new ActivateLinkAction("activate-link-action");
    private static final String INSERT_TABLE_HTML = "<table border=1><tr><td></td></tr></table>";
    private static final String INSERT_UL_HTML = "<ul><li></li></ul>";
    private static final String INSERT_OL_HTML = "<ol><li></li></ol>";
    private static final String INSERT_PRE_HTML = "<pre></pre>";
    private static final Action[] defaultActions = {new InsertHTMLTextAction("InsertTable", INSERT_TABLE_HTML, HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertTableRow", INSERT_TABLE_HTML, HTML.Tag.TABLE, HTML.Tag.TR, HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertTableDataCell", INSERT_TABLE_HTML, HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.BODY, HTML.Tag.TABLE), new InsertHTMLTextAction("InsertUnorderedList", INSERT_UL_HTML, HTML.Tag.BODY, HTML.Tag.UL), new InsertHTMLTextAction("InsertUnorderedListItem", INSERT_UL_HTML, HTML.Tag.UL, HTML.Tag.LI, HTML.Tag.BODY, HTML.Tag.UL), new InsertHTMLTextAction("InsertOrderedList", INSERT_OL_HTML, HTML.Tag.BODY, HTML.Tag.OL), new InsertHTMLTextAction("InsertOrderedListItem", INSERT_OL_HTML, HTML.Tag.OL, HTML.Tag.LI, HTML.Tag.BODY, HTML.Tag.OL), new InsertHRAction(), new InsertHTMLTextAction("InsertPre", INSERT_PRE_HTML, HTML.Tag.BODY, HTML.Tag.PRE), nextLinkAction, previousLinkAction, activateLinkAction, new BeginAction(DefaultEditorKit.beginAction, false), new BeginAction(DefaultEditorKit.selectionBeginAction, true)};
    private LinkController linkHandler = new LinkController();
    private Cursor defaultCursor = DefaultCursor;
    private Cursor linkCursor = MoveCursor;
    private boolean isAutoFormSubmission = true;
    private boolean foundLink = false;
    private int prevHypertextOffset = -1;

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$Parser.class */
    public static abstract class Parser {
        public abstract void parse(Reader reader, ParserCallback parserCallback, boolean z2) throws IOException;
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public String getContentType() {
        return Clipboard.HTML_TYPE;
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public Document createDefaultDocument() {
        StyleSheet styleSheet = getStyleSheet();
        StyleSheet styleSheet2 = new StyleSheet();
        styleSheet2.addStyleSheet(styleSheet);
        HTMLDocument hTMLDocument = new HTMLDocument(styleSheet2);
        hTMLDocument.setParser(getParser());
        hTMLDocument.setAsynchronousLoadPriority(4);
        hTMLDocument.setTokenThreshold(100);
        return hTMLDocument;
    }

    private Parser ensureParser(HTMLDocument hTMLDocument) throws IOException {
        Parser parser = hTMLDocument.getParser();
        if (parser == null) {
            parser = getParser();
        }
        if (parser == null) {
            throw new IOException("Can't load parser");
        }
        return parser;
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void read(Reader reader, Document document, int i2) throws IOException, BadLocationException {
        if (document instanceof HTMLDocument) {
            HTMLDocument hTMLDocument = (HTMLDocument) document;
            if (i2 > document.getLength()) {
                throw new BadLocationException("Invalid location", i2);
            }
            Parser parserEnsureParser = ensureParser(hTMLDocument);
            ParserCallback reader2 = hTMLDocument.getReader(i2);
            Boolean bool = (Boolean) document.getProperty("IgnoreCharsetDirective");
            parserEnsureParser.parse(reader, reader2, bool == null ? false : bool.booleanValue());
            reader2.flush();
            return;
        }
        super.read(reader, document, i2);
    }

    public void insertHTML(HTMLDocument hTMLDocument, int i2, String str, int i3, int i4, HTML.Tag tag) throws IOException, BadLocationException {
        if (i2 > hTMLDocument.getLength()) {
            throw new BadLocationException("Invalid location", i2);
        }
        Parser parserEnsureParser = ensureParser(hTMLDocument);
        ParserCallback reader = hTMLDocument.getReader(i2, i3, i4, tag);
        Boolean bool = (Boolean) hTMLDocument.getProperty("IgnoreCharsetDirective");
        parserEnsureParser.parse(new StringReader(str), reader, bool == null ? false : bool.booleanValue());
        reader.flush();
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void write(Writer writer, Document document, int i2, int i3) throws IOException, BadLocationException {
        if (document instanceof HTMLDocument) {
            new HTMLWriter(writer, (HTMLDocument) document, i2, i3).write();
        } else if (document instanceof StyledDocument) {
            new MinimalHTMLWriter(writer, (StyledDocument) document, i2, i3).write();
        } else {
            super.write(writer, document, i2, i3);
        }
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.EditorKit
    public void install(JEditorPane jEditorPane) {
        jEditorPane.addMouseListener(this.linkHandler);
        jEditorPane.addMouseMotionListener(this.linkHandler);
        jEditorPane.addCaretListener(nextLinkAction);
        super.install(jEditorPane);
        this.theEditor = jEditorPane;
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.EditorKit
    public void deinstall(JEditorPane jEditorPane) {
        jEditorPane.removeMouseListener(this.linkHandler);
        jEditorPane.removeMouseMotionListener(this.linkHandler);
        jEditorPane.removeCaretListener(nextLinkAction);
        super.deinstall(jEditorPane);
        this.theEditor = null;
    }

    public void setStyleSheet(StyleSheet styleSheet) {
        if (styleSheet == null) {
            AppContext.getAppContext().remove(DEFAULT_STYLES_KEY);
        } else {
            AppContext.getAppContext().put(DEFAULT_STYLES_KEY, styleSheet);
        }
    }

    public StyleSheet getStyleSheet() {
        AppContext appContext = AppContext.getAppContext();
        StyleSheet styleSheet = (StyleSheet) appContext.get(DEFAULT_STYLES_KEY);
        if (styleSheet == null) {
            styleSheet = new StyleSheet();
            appContext.put(DEFAULT_STYLES_KEY, styleSheet);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResourceAsStream(DEFAULT_CSS), FTP.DEFAULT_CONTROL_ENCODING));
                styleSheet.loadRules(bufferedReader, null);
                bufferedReader.close();
            } catch (Throwable th) {
            }
        }
        return styleSheet;
    }

    static InputStream getResourceAsStream(final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: javax.swing.text.html.HTMLEditorKit.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InputStream run2() {
                return HTMLEditorKit.class.getResourceAsStream(str);
            }
        });
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), defaultActions);
    }

    @Override // javax.swing.text.StyledEditorKit
    protected void createInputAttributes(Element element, MutableAttributeSet mutableAttributeSet) {
        mutableAttributeSet.removeAttributes(mutableAttributeSet);
        mutableAttributeSet.addAttributes(element.getAttributes());
        mutableAttributeSet.removeAttribute(StyleConstants.ComposedTextAttribute);
        Object attribute = mutableAttributeSet.getAttribute(StyleConstants.NameAttribute);
        if (attribute instanceof HTML.Tag) {
            HTML.Tag tag = (HTML.Tag) attribute;
            if (tag == HTML.Tag.IMG) {
                mutableAttributeSet.removeAttribute(HTML.Attribute.SRC);
                mutableAttributeSet.removeAttribute(HTML.Attribute.HEIGHT);
                mutableAttributeSet.removeAttribute(HTML.Attribute.WIDTH);
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                return;
            }
            if (tag == HTML.Tag.HR || tag == HTML.Tag.BR) {
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                return;
            }
            if (tag == HTML.Tag.COMMENT) {
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                mutableAttributeSet.removeAttribute(HTML.Attribute.COMMENT);
            } else if (tag == HTML.Tag.INPUT) {
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                mutableAttributeSet.removeAttribute(HTML.Tag.INPUT);
            } else if (tag instanceof HTML.UnknownTag) {
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                mutableAttributeSet.removeAttribute(HTML.Attribute.ENDTAG);
            }
        }
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.DefaultEditorKit
    public MutableAttributeSet getInputAttributes() {
        if (this.input == null) {
            this.input = getStyleSheet().addStyle(null, null);
        }
        return this.input;
    }

    public void setDefaultCursor(Cursor cursor) {
        this.defaultCursor = cursor;
    }

    public Cursor getDefaultCursor() {
        return this.defaultCursor;
    }

    public void setLinkCursor(Cursor cursor) {
        this.linkCursor = cursor;
    }

    public Cursor getLinkCursor() {
        return this.linkCursor;
    }

    public boolean isAutoFormSubmission() {
        return this.isAutoFormSubmission;
    }

    public void setAutoFormSubmission(boolean z2) {
        this.isAutoFormSubmission = z2;
    }

    @Override // javax.swing.text.StyledEditorKit, javax.swing.text.EditorKit
    public Object clone() {
        HTMLEditorKit hTMLEditorKit = (HTMLEditorKit) super.clone();
        if (hTMLEditorKit != null) {
            hTMLEditorKit.input = null;
            hTMLEditorKit.linkHandler = new LinkController();
        }
        return hTMLEditorKit;
    }

    protected Parser getParser() {
        if (defaultParser == null) {
            try {
                defaultParser = (Parser) Class.forName("javax.swing.text.html.parser.ParserDelegator").newInstance();
            } catch (Throwable th) {
            }
        }
        return defaultParser;
    }

    @Override // javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.theEditor == null) {
            return null;
        }
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleHTML(this.theEditor).getAccessibleContext();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$LinkController.class */
    public static class LinkController extends MouseAdapter implements MouseMotionListener, Serializable {
        private Element curElem = null;
        private boolean curElemImage = false;
        private String href = null;
        private transient Position.Bias[] bias = new Position.Bias[1];
        private int curOffset;

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            int iViewToModel;
            JEditorPane jEditorPane = (JEditorPane) mouseEvent.getSource();
            if (!jEditorPane.isEditable() && jEditorPane.isEnabled() && SwingUtilities.isLeftMouseButton(mouseEvent) && (iViewToModel = jEditorPane.viewToModel(new Point(mouseEvent.getX(), mouseEvent.getY()))) >= 0) {
                activateLink(iViewToModel, jEditorPane, mouseEvent);
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            JEditorPane jEditorPane = (JEditorPane) mouseEvent.getSource();
            if (!jEditorPane.isEnabled()) {
                return;
            }
            HTMLEditorKit hTMLEditorKit = (HTMLEditorKit) jEditorPane.getEditorKit();
            boolean z2 = true;
            Cursor defaultCursor = hTMLEditorKit.getDefaultCursor();
            if (!jEditorPane.isEditable()) {
                int iViewToModel = jEditorPane.getUI().viewToModel(jEditorPane, new Point(mouseEvent.getX(), mouseEvent.getY()), this.bias);
                if (this.bias[0] == Position.Bias.Backward && iViewToModel > 0) {
                    iViewToModel--;
                }
                if (iViewToModel >= 0 && (jEditorPane.getDocument() instanceof HTMLDocument)) {
                    HTMLDocument hTMLDocument = (HTMLDocument) jEditorPane.getDocument();
                    Element characterElement = hTMLDocument.getCharacterElement(iViewToModel);
                    if (!doesElementContainLocation(jEditorPane, characterElement, iViewToModel, mouseEvent.getX(), mouseEvent.getY())) {
                        characterElement = null;
                    }
                    if (this.curElem != characterElement || this.curElemImage) {
                        Element element = this.curElem;
                        this.curElem = characterElement;
                        String mapHREF = null;
                        this.curElemImage = false;
                        if (characterElement != null) {
                            AttributeSet attributes = characterElement.getAttributes();
                            AttributeSet attributeSet = (AttributeSet) attributes.getAttribute(HTML.Tag.f12846A);
                            if (attributeSet == null) {
                                this.curElemImage = attributes.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.IMG;
                                if (this.curElemImage) {
                                    mapHREF = getMapHREF(jEditorPane, hTMLDocument, characterElement, attributes, iViewToModel, mouseEvent.getX(), mouseEvent.getY());
                                }
                            } else {
                                mapHREF = (String) attributeSet.getAttribute(HTML.Attribute.HREF);
                            }
                        }
                        if (mapHREF != this.href) {
                            fireEvents(jEditorPane, hTMLDocument, mapHREF, element, mouseEvent);
                            this.href = mapHREF;
                            if (mapHREF != null) {
                                defaultCursor = hTMLEditorKit.getLinkCursor();
                            }
                        } else {
                            z2 = false;
                        }
                    } else {
                        z2 = false;
                    }
                    this.curOffset = iViewToModel;
                }
            }
            if (z2 && jEditorPane.getCursor() != defaultCursor) {
                jEditorPane.setCursor(defaultCursor);
            }
        }

        private String getMapHREF(JEditorPane jEditorPane, HTMLDocument hTMLDocument, Element element, AttributeSet attributeSet, int i2, int i3, int i4) {
            Map map;
            Rectangle bounds;
            AttributeSet area;
            Object attribute = attributeSet.getAttribute(HTML.Attribute.USEMAP);
            if (attribute != null && (attribute instanceof String) && (map = hTMLDocument.getMap((String) attribute)) != null && i2 < hTMLDocument.getLength()) {
                TextUI ui = jEditorPane.getUI();
                try {
                    Rectangle rectangleModelToView = ui.modelToView(jEditorPane, i2, Position.Bias.Forward);
                    Rectangle rectangleModelToView2 = ui.modelToView(jEditorPane, i2 + 1, Position.Bias.Backward);
                    bounds = rectangleModelToView.getBounds();
                    bounds.add(rectangleModelToView2 instanceof Rectangle ? rectangleModelToView2 : rectangleModelToView2.getBounds());
                } catch (BadLocationException e2) {
                    bounds = null;
                }
                if (bounds != null && (area = map.getArea(i3 - bounds.f12372x, i4 - bounds.f12373y, bounds.width, bounds.height)) != null) {
                    return (String) area.getAttribute(HTML.Attribute.HREF);
                }
                return null;
            }
            return null;
        }

        private boolean doesElementContainLocation(JEditorPane jEditorPane, Element element, int i2, int i3, int i4) {
            if (element != null && i2 > 0 && element.getStartOffset() == i2) {
                try {
                    TextUI ui = jEditorPane.getUI();
                    Rectangle rectangleModelToView = ui.modelToView(jEditorPane, i2, Position.Bias.Forward);
                    if (rectangleModelToView == null) {
                        return false;
                    }
                    Rectangle bounds = rectangleModelToView instanceof Rectangle ? rectangleModelToView : rectangleModelToView.getBounds();
                    Rectangle rectangleModelToView2 = ui.modelToView(jEditorPane, element.getEndOffset(), Position.Bias.Backward);
                    if (rectangleModelToView2 != null) {
                        bounds.add(rectangleModelToView2 instanceof Rectangle ? rectangleModelToView2 : rectangleModelToView2.getBounds());
                    }
                    return bounds.contains(i3, i4);
                } catch (BadLocationException e2) {
                    return true;
                }
            }
            return true;
        }

        protected void activateLink(int i2, JEditorPane jEditorPane) {
            activateLink(i2, jEditorPane, null);
        }

        void activateLink(int i2, JEditorPane jEditorPane, MouseEvent mouseEvent) {
            Document document = jEditorPane.getDocument();
            if (document instanceof HTMLDocument) {
                HTMLDocument hTMLDocument = (HTMLDocument) document;
                Element characterElement = hTMLDocument.getCharacterElement(i2);
                AttributeSet attributes = characterElement.getAttributes();
                AttributeSet attributeSet = (AttributeSet) attributes.getAttribute(HTML.Tag.f12846A);
                HyperlinkEvent hyperlinkEventCreateHyperlinkEvent = null;
                int x2 = -1;
                int y2 = -1;
                if (mouseEvent != null) {
                    x2 = mouseEvent.getX();
                    y2 = mouseEvent.getY();
                }
                if (attributeSet == null) {
                    this.href = getMapHREF(jEditorPane, hTMLDocument, characterElement, attributes, i2, x2, y2);
                } else {
                    this.href = (String) attributeSet.getAttribute(HTML.Attribute.HREF);
                }
                if (this.href != null) {
                    hyperlinkEventCreateHyperlinkEvent = createHyperlinkEvent(jEditorPane, hTMLDocument, this.href, attributeSet, characterElement, mouseEvent);
                }
                if (hyperlinkEventCreateHyperlinkEvent != null) {
                    jEditorPane.fireHyperlinkUpdate(hyperlinkEventCreateHyperlinkEvent);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v20, types: [javax.swing.event.HyperlinkEvent] */
        HyperlinkEvent createHyperlinkEvent(JEditorPane jEditorPane, HTMLDocument hTMLDocument, String str, AttributeSet attributeSet, Element element, MouseEvent mouseEvent) {
            URL url;
            HTMLFrameHyperlinkEvent hTMLFrameHyperlinkEvent;
            try {
                URL base = hTMLDocument.getBase();
                url = new URL(base, str);
                if (str != null && DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol()) && str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                    String file = base.getFile();
                    String file2 = url.getFile();
                    if (file != null && file2 != null && !file2.startsWith(file)) {
                        url = new URL(base, file + str);
                    }
                }
            } catch (MalformedURLException e2) {
                url = null;
            }
            if (!hTMLDocument.isFrameDocument()) {
                hTMLFrameHyperlinkEvent = new HyperlinkEvent(jEditorPane, HyperlinkEvent.EventType.ACTIVATED, url, str, element, mouseEvent);
            } else {
                String baseTarget = attributeSet != null ? (String) attributeSet.getAttribute(HTML.Attribute.TARGET) : null;
                if (baseTarget == null || baseTarget.equals("")) {
                    baseTarget = hTMLDocument.getBaseTarget();
                }
                if (baseTarget == null || baseTarget.equals("")) {
                    baseTarget = "_self";
                }
                hTMLFrameHyperlinkEvent = new HTMLFrameHyperlinkEvent(jEditorPane, HyperlinkEvent.EventType.ACTIVATED, url, str, element, mouseEvent, baseTarget);
            }
            return hTMLFrameHyperlinkEvent;
        }

        void fireEvents(JEditorPane jEditorPane, HTMLDocument hTMLDocument, String str, Element element, MouseEvent mouseEvent) {
            URL url;
            URL url2;
            if (this.href != null) {
                try {
                    url = new URL(hTMLDocument.getBase(), this.href);
                } catch (MalformedURLException e2) {
                    url = null;
                }
                jEditorPane.fireHyperlinkUpdate(new HyperlinkEvent(jEditorPane, HyperlinkEvent.EventType.EXITED, url, this.href, element, mouseEvent));
            }
            if (str != null) {
                try {
                    url2 = new URL(hTMLDocument.getBase(), str);
                } catch (MalformedURLException e3) {
                    url2 = null;
                }
                jEditorPane.fireHyperlinkUpdate(new HyperlinkEvent(jEditorPane, HyperlinkEvent.EventType.ENTERED, url2, str, this.curElem, mouseEvent));
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$ParserCallback.class */
    public static class ParserCallback {
        public static final Object IMPLIED = "_implied_";

        public void flush() throws BadLocationException {
        }

        public void handleText(char[] cArr, int i2) {
        }

        public void handleComment(char[] cArr, int i2) {
        }

        public void handleStartTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i2) {
        }

        public void handleEndTag(HTML.Tag tag, int i2) {
        }

        public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i2) {
        }

        public void handleError(String str, int i2) {
        }

        public void handleEndOfLineString(String str) {
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$HTMLFactory.class */
    public static class HTMLFactory implements ViewFactory {
        @Override // javax.swing.text.ViewFactory
        public View create(Element element) {
            AttributeSet attributes = element.getAttributes();
            Object attribute = attributes.getAttribute(AbstractDocument.ElementNameAttribute);
            Object attribute2 = attribute != null ? null : attributes.getAttribute(StyleConstants.NameAttribute);
            if (attribute2 instanceof HTML.Tag) {
                HTML.Tag tag = (HTML.Tag) attribute2;
                if (tag == HTML.Tag.CONTENT) {
                    return new InlineView(element);
                }
                if (tag == HTML.Tag.IMPLIED) {
                    String str = (String) element.getAttributes().getAttribute(CSS.Attribute.WHITE_SPACE);
                    if (str != null && str.equals("pre")) {
                        return new LineView(element);
                    }
                    return new ParagraphView(element);
                }
                if (tag == HTML.Tag.f12849P || tag == HTML.Tag.H1 || tag == HTML.Tag.H2 || tag == HTML.Tag.H3 || tag == HTML.Tag.H4 || tag == HTML.Tag.H5 || tag == HTML.Tag.H6 || tag == HTML.Tag.DT) {
                    return new ParagraphView(element);
                }
                if (tag == HTML.Tag.MENU || tag == HTML.Tag.DIR || tag == HTML.Tag.UL || tag == HTML.Tag.OL) {
                    return new ListView(element);
                }
                if (tag == HTML.Tag.BODY) {
                    return new BodyBlockView(element);
                }
                if (tag == HTML.Tag.HTML) {
                    return new BlockView(element, 1);
                }
                if (tag == HTML.Tag.LI || tag == HTML.Tag.CENTER || tag == HTML.Tag.DL || tag == HTML.Tag.DD || tag == HTML.Tag.DIV || tag == HTML.Tag.BLOCKQUOTE || tag == HTML.Tag.PRE || tag == HTML.Tag.FORM) {
                    return new BlockView(element, 1);
                }
                if (tag == HTML.Tag.NOFRAMES) {
                    return new NoFramesView(element, 1);
                }
                if (tag == HTML.Tag.IMG) {
                    return new ImageView(element);
                }
                if (tag == HTML.Tag.ISINDEX) {
                    return new IsindexView(element);
                }
                if (tag == HTML.Tag.HR) {
                    return new HRuleView(element);
                }
                if (tag == HTML.Tag.BR) {
                    return new BRView(element);
                }
                if (tag == HTML.Tag.TABLE) {
                    return new TableView(element);
                }
                if (tag == HTML.Tag.INPUT || tag == HTML.Tag.SELECT || tag == HTML.Tag.TEXTAREA) {
                    return new FormView(element);
                }
                if (tag == HTML.Tag.OBJECT) {
                    if (SwingAccessor.getAllowHTMLObject().booleanValue()) {
                        return new ObjectView(element);
                    }
                    return new ObjectView(element, false);
                }
                if (tag == HTML.Tag.FRAMESET) {
                    if (element.getAttributes().isDefined(HTML.Attribute.ROWS)) {
                        return new FrameSetView(element, 1);
                    }
                    if (element.getAttributes().isDefined(HTML.Attribute.COLS)) {
                        return new FrameSetView(element, 0);
                    }
                    throw new RuntimeException("Can't build a" + ((Object) tag) + ", " + ((Object) element) + ":no ROWS or COLS defined.");
                }
                if (tag == HTML.Tag.FRAME) {
                    return new FrameView(element);
                }
                if (tag instanceof HTML.UnknownTag) {
                    return new HiddenTagView(element);
                }
                if (tag == HTML.Tag.COMMENT) {
                    return new CommentView(element);
                }
                if (tag == HTML.Tag.HEAD) {
                    return new BlockView(element, 0) { // from class: javax.swing.text.html.HTMLEditorKit.HTMLFactory.1
                        @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
                        public float getPreferredSpan(int i2) {
                            return 0.0f;
                        }

                        @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
                        public float getMinimumSpan(int i2) {
                            return 0.0f;
                        }

                        @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
                        public float getMaximumSpan(int i2) {
                            return 0.0f;
                        }

                        @Override // javax.swing.text.CompositeView
                        protected void loadChildren(ViewFactory viewFactory) {
                        }

                        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
                        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
                            return shape;
                        }

                        @Override // javax.swing.text.CompositeView, javax.swing.text.View
                        public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) {
                            return getElement().getEndOffset();
                        }
                    };
                }
                if (tag == HTML.Tag.TITLE || tag == HTML.Tag.META || tag == HTML.Tag.LINK || tag == HTML.Tag.STYLE || tag == HTML.Tag.SCRIPT || tag == HTML.Tag.AREA || tag == HTML.Tag.MAP || tag == HTML.Tag.PARAM || tag == HTML.Tag.APPLET) {
                    return new HiddenTagView(element);
                }
            }
            String name = attribute != null ? (String) attribute : element.getName();
            if (name != null) {
                if (name.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(element);
                }
                if (name.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(element);
                }
                if (name.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(element, 1);
                }
                if (name.equals("component")) {
                    return new ComponentView(element);
                }
                if (name.equals("icon")) {
                    return new IconView(element);
                }
            }
            return new LabelView(element);
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$HTMLFactory$BodyBlockView.class */
        static class BodyBlockView extends BlockView implements ComponentListener {
            private Reference<JViewport> cachedViewPort;
            private boolean isListening;
            private int viewVisibleWidth;
            private int componentVisibleWidth;

            public BodyBlockView(Element element) {
                super(element, 1);
                this.cachedViewPort = null;
                this.isListening = false;
                this.viewVisibleWidth = Integer.MAX_VALUE;
                this.componentVisibleWidth = Integer.MAX_VALUE;
            }

            @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView
            protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
                SizeRequirements sizeRequirementsCalculateMajorAxisRequirements = super.calculateMajorAxisRequirements(i2, sizeRequirements);
                sizeRequirementsCalculateMajorAxisRequirements.maximum = Integer.MAX_VALUE;
                return sizeRequirementsCalculateMajorAxisRequirements;
            }

            @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView
            protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
                Container parent;
                Container container = getContainer();
                if (container != null && (container instanceof JEditorPane) && (parent = container.getParent()) != null && (parent instanceof JViewport)) {
                    JViewport jViewport = (JViewport) parent;
                    if (this.cachedViewPort != null) {
                        JViewport jViewport2 = this.cachedViewPort.get();
                        if (jViewport2 != null) {
                            if (jViewport2 != jViewport) {
                                jViewport2.removeComponentListener(this);
                            }
                        } else {
                            this.cachedViewPort = null;
                        }
                    }
                    if (this.cachedViewPort == null) {
                        jViewport.addComponentListener(this);
                        this.cachedViewPort = new WeakReference(jViewport);
                    }
                    this.componentVisibleWidth = jViewport.getExtentSize().width;
                    if (this.componentVisibleWidth > 0) {
                        this.viewVisibleWidth = (this.componentVisibleWidth - container.getInsets().left) - getLeftInset();
                        i2 = Math.min(i2, this.viewVisibleWidth);
                    }
                } else if (this.cachedViewPort != null) {
                    JViewport jViewport3 = this.cachedViewPort.get();
                    if (jViewport3 != null) {
                        jViewport3.removeComponentListener(this);
                    }
                    this.cachedViewPort = null;
                }
                super.layoutMinorAxis(i2, i3, iArr, iArr2);
            }

            @Override // javax.swing.text.html.BlockView, javax.swing.text.CompositeView, javax.swing.text.View
            public void setParent(View view) {
                if (view == null && this.cachedViewPort != null) {
                    JViewport jViewport = this.cachedViewPort.get();
                    if (jViewport != null) {
                        jViewport.removeComponentListener(this);
                    }
                    this.cachedViewPort = null;
                }
                super.setParent(view);
            }

            @Override // java.awt.event.ComponentListener
            public void componentResized(ComponentEvent componentEvent) {
                if (!(componentEvent.getSource() instanceof JViewport)) {
                    return;
                }
                if (this.componentVisibleWidth != ((JViewport) componentEvent.getSource()).getExtentSize().width && (getDocument() instanceof AbstractDocument)) {
                    AbstractDocument abstractDocument = (AbstractDocument) getDocument();
                    abstractDocument.readLock();
                    try {
                        layoutChanged(0);
                        preferenceChanged(null, true, true);
                        abstractDocument.readUnlock();
                    } catch (Throwable th) {
                        abstractDocument.readUnlock();
                        throw th;
                    }
                }
            }

            @Override // java.awt.event.ComponentListener
            public void componentHidden(ComponentEvent componentEvent) {
            }

            @Override // java.awt.event.ComponentListener
            public void componentMoved(ComponentEvent componentEvent) {
            }

            @Override // java.awt.event.ComponentListener
            public void componentShown(ComponentEvent componentEvent) {
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$HTMLTextAction.class */
    public static abstract class HTMLTextAction extends StyledEditorKit.StyledTextAction {
        public HTMLTextAction(String str) {
            super(str);
        }

        protected HTMLDocument getHTMLDocument(JEditorPane jEditorPane) {
            Document document = jEditorPane.getDocument();
            if (document instanceof HTMLDocument) {
                return (HTMLDocument) document;
            }
            throw new IllegalArgumentException("document must be HTMLDocument");
        }

        protected HTMLEditorKit getHTMLEditorKit(JEditorPane jEditorPane) {
            EditorKit editorKit = jEditorPane.getEditorKit();
            if (editorKit instanceof HTMLEditorKit) {
                return (HTMLEditorKit) editorKit;
            }
            throw new IllegalArgumentException("EditorKit must be HTMLEditorKit");
        }

        protected Element[] getElementsAt(HTMLDocument hTMLDocument, int i2) {
            return getElementsAt(hTMLDocument.getDefaultRootElement(), i2, 0);
        }

        private Element[] getElementsAt(Element element, int i2, int i3) {
            if (element.isLeaf()) {
                Element[] elementArr = new Element[i3 + 1];
                elementArr[i3] = element;
                return elementArr;
            }
            Element[] elementsAt = getElementsAt(element.getElement(element.getElementIndex(i2)), i2, i3 + 1);
            elementsAt[i3] = element;
            return elementsAt;
        }

        protected int elementCountToTag(HTMLDocument hTMLDocument, int i2, HTML.Tag tag) {
            int i3 = -1;
            Element characterElement = hTMLDocument.getCharacterElement(i2);
            while (characterElement != null && characterElement.getAttributes().getAttribute(StyleConstants.NameAttribute) != tag) {
                characterElement = characterElement.getParentElement();
                i3++;
            }
            if (characterElement == null) {
                return -1;
            }
            return i3;
        }

        protected Element findElementMatchingTag(HTMLDocument hTMLDocument, int i2, HTML.Tag tag) {
            Element defaultRootElement = hTMLDocument.getDefaultRootElement();
            Element element = null;
            while (defaultRootElement != null) {
                if (defaultRootElement.getAttributes().getAttribute(StyleConstants.NameAttribute) == tag) {
                    element = defaultRootElement;
                }
                defaultRootElement = defaultRootElement.getElement(defaultRootElement.getElementIndex(i2));
            }
            return element;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$InsertHTMLTextAction.class */
    public static class InsertHTMLTextAction extends HTMLTextAction {
        protected String html;
        protected HTML.Tag parentTag;
        protected HTML.Tag addTag;
        protected HTML.Tag alternateParentTag;
        protected HTML.Tag alternateAddTag;
        boolean adjustSelection;

        public InsertHTMLTextAction(String str, String str2, HTML.Tag tag, HTML.Tag tag2) {
            this(str, str2, tag, tag2, null, null);
        }

        public InsertHTMLTextAction(String str, String str2, HTML.Tag tag, HTML.Tag tag2, HTML.Tag tag3, HTML.Tag tag4) {
            this(str, str2, tag, tag2, tag3, tag4, true);
        }

        InsertHTMLTextAction(String str, String str2, HTML.Tag tag, HTML.Tag tag2, HTML.Tag tag3, HTML.Tag tag4, boolean z2) {
            super(str);
            this.html = str2;
            this.parentTag = tag;
            this.addTag = tag2;
            this.alternateParentTag = tag3;
            this.alternateAddTag = tag4;
            this.adjustSelection = z2;
        }

        protected void insertHTML(JEditorPane jEditorPane, HTMLDocument hTMLDocument, int i2, String str, int i3, int i4, HTML.Tag tag) {
            try {
                getHTMLEditorKit(jEditorPane).insertHTML(hTMLDocument, i2, str, i3, i4, tag);
            } catch (IOException e2) {
                throw new RuntimeException("Unable to insert: " + ((Object) e2));
            } catch (BadLocationException e3) {
                throw new RuntimeException("Unable to insert: " + ((Object) e3));
            }
        }

        protected void insertAtBoundary(JEditorPane jEditorPane, HTMLDocument hTMLDocument, int i2, Element element, String str, HTML.Tag tag, HTML.Tag tag2) {
            insertAtBoundry(jEditorPane, hTMLDocument, i2, element, str, tag, tag2);
        }

        @Deprecated
        protected void insertAtBoundry(JEditorPane jEditorPane, HTMLDocument hTMLDocument, int i2, Element element, String str, HTML.Tag tag, HTML.Tag tag2) {
            Element element2;
            Element parentElement;
            boolean z2 = i2 == 0;
            if (i2 > 0 || element == null) {
                Element defaultRootElement = hTMLDocument.getDefaultRootElement();
                while (true) {
                    element2 = defaultRootElement;
                    if (element2 == null || element2.getStartOffset() == i2 || element2.isLeaf()) {
                        break;
                    } else {
                        defaultRootElement = element2.getElement(element2.getElementIndex(i2));
                    }
                }
                parentElement = element2 != null ? element2.getParentElement() : null;
            } else {
                parentElement = element;
            }
            if (parentElement != null) {
                int i3 = 0;
                int i4 = 0;
                if (z2 && element != null) {
                    Element element3 = parentElement;
                    while (element3 != null && !element3.isLeaf()) {
                        element3 = element3.getElement(element3.getElementIndex(i2));
                        i3++;
                    }
                } else {
                    Element element4 = parentElement;
                    int i5 = i2 - 1;
                    while (element4 != null && !element4.isLeaf()) {
                        element4 = element4.getElement(element4.getElementIndex(i5));
                        i3++;
                    }
                    Element element5 = parentElement;
                    i2 = i5 + 1;
                    while (element5 != null && element5 != element) {
                        element5 = element5.getElement(element5.getElementIndex(i2));
                        i4++;
                    }
                }
                insertHTML(jEditorPane, hTMLDocument, i2, str, Math.max(0, i3 - 1), i4, tag2);
            }
        }

        boolean insertIntoTag(JEditorPane jEditorPane, HTMLDocument hTMLDocument, int i2, HTML.Tag tag, HTML.Tag tag2) {
            int iElementCountToTag;
            Element elementFindElementMatchingTag = findElementMatchingTag(hTMLDocument, i2, tag);
            if (elementFindElementMatchingTag != null && elementFindElementMatchingTag.getStartOffset() == i2) {
                insertAtBoundary(jEditorPane, hTMLDocument, i2, elementFindElementMatchingTag, this.html, tag, tag2);
                return true;
            }
            if (i2 > 0 && (iElementCountToTag = elementCountToTag(hTMLDocument, i2 - 1, tag)) != -1) {
                insertHTML(jEditorPane, hTMLDocument, i2, this.html, iElementCountToTag, 0, tag2);
                return true;
            }
            return false;
        }

        void adjustSelection(JEditorPane jEditorPane, HTMLDocument hTMLDocument, int i2, int i3) {
            String text;
            int length = hTMLDocument.getLength();
            if (length != i3 && i2 < length) {
                if (i2 > 0) {
                    try {
                        text = hTMLDocument.getText(i2 - 1, 1);
                    } catch (BadLocationException e2) {
                        text = null;
                    }
                    if (text != null && text.length() > 0 && text.charAt(0) == '\n') {
                        jEditorPane.select(i2, i2);
                        return;
                    } else {
                        jEditorPane.select(i2 + 1, i2 + 1);
                        return;
                    }
                }
                jEditorPane.select(1, 1);
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            boolean zInsertIntoTag;
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                HTMLDocument hTMLDocument = getHTMLDocument(editor);
                int selectionStart = editor.getSelectionStart();
                int length = hTMLDocument.getLength();
                if (!insertIntoTag(editor, hTMLDocument, selectionStart, this.parentTag, this.addTag) && this.alternateParentTag != null) {
                    zInsertIntoTag = insertIntoTag(editor, hTMLDocument, selectionStart, this.alternateParentTag, this.alternateAddTag);
                } else {
                    zInsertIntoTag = true;
                }
                if (this.adjustSelection && zInsertIntoTag) {
                    adjustSelection(editor, hTMLDocument, selectionStart, length);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$InsertHRAction.class */
    static class InsertHRAction extends InsertHTMLTextAction {
        InsertHRAction() {
            super("InsertHR", HTMLEditorKit.INSERT_HR_HTML, null, HTML.Tag.IMPLIED, null, null, false);
        }

        @Override // javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction, java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane editor = getEditor(actionEvent);
            if (editor != null) {
                Element paragraphElement = getHTMLDocument(editor).getParagraphElement(editor.getSelectionStart());
                if (paragraphElement.getParentElement() != null) {
                    this.parentTag = (HTML.Tag) paragraphElement.getParentElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
                    super.actionPerformed(actionEvent);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getAttrValue(AttributeSet attributeSet, HTML.Attribute attribute) {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement2 = attributeNames.nextElement2();
            Object attribute2 = attributeSet.getAttribute(objNextElement2);
            if (attribute2 instanceof AttributeSet) {
                Object attrValue = getAttrValue((AttributeSet) attribute2, attribute);
                if (attrValue != null) {
                    return attrValue;
                }
            } else if (objNextElement2 == attribute) {
                return attribute2;
            }
        }
        return null;
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$NavigateLinkAction.class */
    static class NavigateLinkAction extends TextAction implements CaretListener {
        private static final FocusHighlightPainter focusPainter = new FocusHighlightPainter(null);
        private final boolean focusBack;

        public NavigateLinkAction(String str) {
            super(str);
            this.focusBack = "previous-link-action".equals(str);
        }

        @Override // javax.swing.event.CaretListener
        public void caretUpdate(CaretEvent caretEvent) {
            JTextComponent jTextComponent;
            HTMLEditorKit hTMLEditorKit;
            Object source = caretEvent.getSource();
            if ((source instanceof JTextComponent) && (hTMLEditorKit = getHTMLEditorKit((jTextComponent = (JTextComponent) source))) != null && hTMLEditorKit.foundLink) {
                hTMLEditorKit.foundLink = false;
                jTextComponent.getAccessibleContext().firePropertyChange(AccessibleContext.ACCESSIBLE_HYPERTEXT_OFFSET, Integer.valueOf(hTMLEditorKit.prevHypertextOffset), Integer.valueOf(caretEvent.getDot()));
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent == null || textComponent.isEditable()) {
                return;
            }
            Document document = textComponent.getDocument();
            HTMLEditorKit hTMLEditorKit = getHTMLEditorKit(textComponent);
            if (document == null || hTMLEditorKit == null) {
                return;
            }
            ElementIterator elementIterator = new ElementIterator(document);
            int caretPosition = textComponent.getCaretPosition();
            int startOffset = -1;
            int endOffset = -1;
            while (true) {
                Element next = elementIterator.next();
                if (next != null) {
                    String name = next.getName();
                    Object attrValue = HTMLEditorKit.getAttrValue(next.getAttributes(), HTML.Attribute.HREF);
                    if (name.equals(HTML.Tag.OBJECT.toString()) || attrValue != null) {
                        int startOffset2 = next.getStartOffset();
                        if (this.focusBack) {
                            if (startOffset2 >= caretPosition && startOffset >= 0) {
                                hTMLEditorKit.foundLink = true;
                                textComponent.setCaretPosition(startOffset);
                                moveCaretPosition(textComponent, hTMLEditorKit, startOffset, endOffset);
                                hTMLEditorKit.prevHypertextOffset = startOffset;
                                return;
                            }
                        } else if (startOffset2 > caretPosition) {
                            hTMLEditorKit.foundLink = true;
                            textComponent.setCaretPosition(startOffset2);
                            moveCaretPosition(textComponent, hTMLEditorKit, startOffset2, next.getEndOffset());
                            hTMLEditorKit.prevHypertextOffset = startOffset2;
                            return;
                        }
                        startOffset = next.getStartOffset();
                        endOffset = next.getEndOffset();
                    }
                } else {
                    if (this.focusBack && startOffset >= 0) {
                        hTMLEditorKit.foundLink = true;
                        textComponent.setCaretPosition(startOffset);
                        moveCaretPosition(textComponent, hTMLEditorKit, startOffset, endOffset);
                        hTMLEditorKit.prevHypertextOffset = startOffset;
                        return;
                    }
                    return;
                }
            }
        }

        private void moveCaretPosition(JTextComponent jTextComponent, HTMLEditorKit hTMLEditorKit, int i2, int i3) {
            Highlighter highlighter = jTextComponent.getHighlighter();
            if (highlighter != null) {
                int iMin = Math.min(i3, i2);
                int iMax = Math.max(i3, i2);
                try {
                    if (hTMLEditorKit.linkNavigationTag == null) {
                        hTMLEditorKit.linkNavigationTag = highlighter.addHighlight(iMin, iMax, focusPainter);
                    } else {
                        highlighter.changeHighlight(hTMLEditorKit.linkNavigationTag, iMin, iMax);
                    }
                } catch (BadLocationException e2) {
                }
            }
        }

        private HTMLEditorKit getHTMLEditorKit(JTextComponent jTextComponent) {
            if (jTextComponent instanceof JEditorPane) {
                EditorKit editorKit = ((JEditorPane) jTextComponent).getEditorKit();
                if (editorKit instanceof HTMLEditorKit) {
                    return (HTMLEditorKit) editorKit;
                }
                return null;
            }
            return null;
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$NavigateLinkAction$FocusHighlightPainter.class */
        static class FocusHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
            FocusHighlightPainter(Color color) {
                super(color);
            }

            @Override // javax.swing.text.DefaultHighlighter.DefaultHighlightPainter, javax.swing.text.LayeredHighlighter.LayerPainter
            public Shape paintLayer(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view) {
                Rectangle bounds;
                Color color = getColor();
                if (color == null) {
                    graphics.setColor(jTextComponent.getSelectionColor());
                } else {
                    graphics.setColor(color);
                }
                if (i2 == view.getStartOffset() && i3 == view.getEndOffset()) {
                    if (shape instanceof Rectangle) {
                        bounds = (Rectangle) shape;
                    } else {
                        bounds = shape.getBounds();
                    }
                    graphics.drawRect(bounds.f12372x, bounds.f12373y, bounds.width - 1, bounds.height);
                    return bounds;
                }
                try {
                    Shape shapeModelToView = view.modelToView(i2, Position.Bias.Forward, i3, Position.Bias.Backward, shape);
                    Rectangle bounds2 = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                    graphics.drawRect(bounds2.f12372x, bounds2.f12373y, bounds2.width - 1, bounds2.height);
                    return bounds2;
                } catch (BadLocationException e2) {
                    return null;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$ActivateLinkAction.class */
    static class ActivateLinkAction extends TextAction {
        public ActivateLinkAction(String str) {
            super(str);
        }

        private void activateLink(String str, HTMLDocument hTMLDocument, JEditorPane jEditorPane, int i2) {
            try {
                URL url = new URL((URL) hTMLDocument.getProperty(Document.StreamDescriptionProperty), str);
                jEditorPane.fireHyperlinkUpdate(new HyperlinkEvent(jEditorPane, HyperlinkEvent.EventType.ACTIVATED, url, url.toExternalForm(), hTMLDocument.getCharacterElement(i2)));
            } catch (MalformedURLException e2) {
            }
        }

        private void doObjectAction(JEditorPane jEditorPane, Element element) {
            Component component;
            AccessibleContext accessibleContext;
            AccessibleAction accessibleAction;
            View view = getView(jEditorPane, element);
            if (view != null && (view instanceof ObjectView) && (component = ((ObjectView) view).getComponent()) != null && (component instanceof Accessible) && (accessibleContext = component.getAccessibleContext()) != null && (accessibleAction = accessibleContext.getAccessibleAction()) != null) {
                accessibleAction.doAccessibleAction(0);
            }
        }

        private View getRootView(JEditorPane jEditorPane) {
            return jEditorPane.getUI().getRootView(jEditorPane);
        }

        private View getView(JEditorPane jEditorPane, Element element) {
            Object objLock = lock(jEditorPane);
            try {
                View rootView = getRootView(jEditorPane);
                int startOffset = element.getStartOffset();
                if (rootView != null) {
                    View view = getView(rootView, element, startOffset);
                    unlock(objLock);
                    return view;
                }
                return null;
            } finally {
                unlock(objLock);
            }
        }

        private View getView(View view, Element element, int i2) {
            if (view.getElement() == element) {
                return view;
            }
            int viewIndex = view.getViewIndex(i2, Position.Bias.Forward);
            if (viewIndex != -1 && viewIndex < view.getViewCount()) {
                return getView(view.getView(viewIndex), element, i2);
            }
            return null;
        }

        private Object lock(JEditorPane jEditorPane) {
            Document document = jEditorPane.getDocument();
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
                return document;
            }
            return null;
        }

        private void unlock(Object obj) {
            if (obj != null) {
                ((AbstractDocument) obj).readUnlock();
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JEditorPane jEditorPane;
            Document document;
            JTextComponent textComponent = getTextComponent(actionEvent);
            if (textComponent.isEditable() || !(textComponent instanceof JEditorPane) || (document = (jEditorPane = (JEditorPane) textComponent).getDocument()) == null || !(document instanceof HTMLDocument)) {
                return;
            }
            HTMLDocument hTMLDocument = (HTMLDocument) document;
            ElementIterator elementIterator = new ElementIterator(hTMLDocument);
            int caretPosition = jEditorPane.getCaretPosition();
            while (true) {
                Element next = elementIterator.next();
                if (next != null) {
                    String name = next.getName();
                    AttributeSet attributes = next.getAttributes();
                    Object attrValue = HTMLEditorKit.getAttrValue(attributes, HTML.Attribute.HREF);
                    if (attrValue != null) {
                        if (caretPosition >= next.getStartOffset() && caretPosition <= next.getEndOffset()) {
                            activateLink((String) attrValue, hTMLDocument, jEditorPane, caretPosition);
                            return;
                        }
                    } else if (name.equals(HTML.Tag.OBJECT.toString()) && HTMLEditorKit.getAttrValue(attributes, HTML.Attribute.CLASSID) != null && caretPosition >= next.getStartOffset() && caretPosition <= next.getEndOffset()) {
                        doObjectAction(jEditorPane, next);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getBodyElementStart(JTextComponent jTextComponent) {
        Element element = jTextComponent.getDocument().getRootElements()[0];
        for (int i2 = 0; i2 < element.getElementCount(); i2++) {
            Element element2 = element.getElement(i2);
            if ("body".equals(element2.getName())) {
                return element2.getStartOffset();
            }
        }
        return 0;
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLEditorKit$BeginAction.class */
    static class BeginAction extends TextAction {
        private boolean select;

        BeginAction(String str, boolean z2) {
            super(str);
            this.select = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTextComponent textComponent = getTextComponent(actionEvent);
            int bodyElementStart = HTMLEditorKit.getBodyElementStart(textComponent);
            if (textComponent != null) {
                if (this.select) {
                    textComponent.moveCaretPosition(bodyElementStart);
                } else {
                    textComponent.setCaretPosition(bodyElementStart);
                }
            }
        }
    }
}
