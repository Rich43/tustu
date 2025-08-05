package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.StringReader;
import java.net.URL;
import java.security.AccessController;
import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;
import sun.security.action.GetBooleanAction;
import sun.swing.SwingAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicHTML.class */
public class BasicHTML {
    private static final String htmlDisable = "html.disable";
    public static final String propertyKey = "html";
    public static final String documentBaseKey = "html.base";
    private static BasicEditorKit basicHTMLFactory;
    private static ViewFactory basicHTMLViewFactory;
    private static final String styleChanges = "p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }";

    public static View createHTMLView(JComponent jComponent, String str) {
        BasicEditorKit factory = getFactory();
        Document documentCreateDefaultDocument = factory.createDefaultDocument(jComponent.getFont(), jComponent.getForeground());
        Object clientProperty = jComponent.getClientProperty(documentBaseKey);
        if (clientProperty instanceof URL) {
            ((HTMLDocument) documentCreateDefaultDocument).setBase((URL) clientProperty);
        }
        try {
            factory.read(new StringReader(str), documentCreateDefaultDocument, 0);
        } catch (Throwable th) {
        }
        ViewFactory viewFactory = factory.getViewFactory();
        return new Renderer(jComponent, viewFactory, viewFactory.create(documentCreateDefaultDocument.getDefaultRootElement()));
    }

    public static int getHTMLBaseline(View view, int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        if (view instanceof Renderer) {
            return getBaseline(view.getView(0), i2, i3);
        }
        return -1;
    }

    static int getBaseline(JComponent jComponent, int i2, int i3, int i4, int i5) {
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            int hTMLBaseline = getHTMLBaseline(view, i4, i5);
            if (hTMLBaseline < 0) {
                return hTMLBaseline;
            }
            return i2 + hTMLBaseline;
        }
        return i2 + i3;
    }

    static int getBaseline(View view, int i2, int i3) {
        if (hasParagraph(view)) {
            view.setSize(i2, i3);
            return getBaseline(view, new Rectangle(0, 0, i2, i3));
        }
        return -1;
    }

    private static int getBaseline(View view, Shape shape) {
        Rectangle bounds;
        if (view.getViewCount() == 0) {
            return -1;
        }
        AttributeSet attributes = view.getElement().getAttributes();
        Object attribute = null;
        if (attributes != null) {
            attribute = attributes.getAttribute(StyleConstants.NameAttribute);
        }
        int i2 = 0;
        if (attribute == HTML.Tag.HTML && view.getViewCount() > 1) {
            i2 = 0 + 1;
        }
        Shape childAllocation = view.getChildAllocation(i2, shape);
        if (childAllocation == null) {
            return -1;
        }
        View view2 = view.getView(i2);
        if (view instanceof ParagraphView) {
            if (childAllocation instanceof Rectangle) {
                bounds = (Rectangle) childAllocation;
            } else {
                bounds = childAllocation.getBounds();
            }
            return bounds.f12373y + ((int) (bounds.height * view2.getAlignment(1)));
        }
        return getBaseline(view2, childAllocation);
    }

    private static boolean hasParagraph(View view) {
        if (view instanceof ParagraphView) {
            return true;
        }
        if (view.getViewCount() == 0) {
            return false;
        }
        AttributeSet attributes = view.getElement().getAttributes();
        Object attribute = null;
        if (attributes != null) {
            attribute = attributes.getAttribute(StyleConstants.NameAttribute);
        }
        int i2 = 0;
        if (attribute == HTML.Tag.HTML && view.getViewCount() > 1) {
            i2 = 1;
        }
        return hasParagraph(view.getView(i2));
    }

    public static boolean isHTMLString(String str) {
        if (str != null && str.length() >= 6 && str.charAt(0) == '<' && str.charAt(5) == '>') {
            return str.substring(1, 5).equalsIgnoreCase("html");
        }
        return false;
    }

    public static void updateRenderer(JComponent jComponent, String str) {
        View viewCreateHTMLView = null;
        View view = (View) jComponent.getClientProperty("html");
        if (!Boolean.TRUE.equals((Boolean) jComponent.getClientProperty(htmlDisable)) && isHTMLString(str)) {
            viewCreateHTMLView = createHTMLView(jComponent, str);
        }
        if (viewCreateHTMLView != view && view != null) {
            for (int i2 = 0; i2 < view.getViewCount(); i2++) {
                view.getView(i2).setParent(null);
            }
        }
        jComponent.putClientProperty("html", viewCreateHTMLView);
        String str2 = (String) jComponent.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
        String strTrim = null;
        if (str2 != null && view != null) {
            try {
                strTrim = view.getDocument().getText(0, view.getDocument().getLength()).trim();
            } catch (BadLocationException e2) {
            }
        }
        if (str2 == null || str2.equals(strTrim)) {
            String strTrim2 = null;
            if (viewCreateHTMLView != null) {
                try {
                    strTrim2 = viewCreateHTMLView.getDocument().getText(0, viewCreateHTMLView.getDocument().getLength()).trim();
                } catch (BadLocationException e3) {
                }
            }
            jComponent.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, strTrim2);
        }
    }

    static BasicEditorKit getFactory() {
        if (basicHTMLFactory == null) {
            basicHTMLViewFactory = new BasicHTMLViewFactory();
            basicHTMLFactory = new BasicEditorKit();
        }
        return basicHTMLFactory;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicHTML$BasicEditorKit.class */
    static class BasicEditorKit extends HTMLEditorKit {
        private static StyleSheet defaultStyles;

        BasicEditorKit() {
        }

        @Override // javax.swing.text.html.HTMLEditorKit
        public StyleSheet getStyleSheet() {
            if (defaultStyles == null) {
                defaultStyles = new StyleSheet();
                StringReader stringReader = new StringReader(BasicHTML.styleChanges);
                try {
                    defaultStyles.loadRules(stringReader, null);
                } catch (Throwable th) {
                }
                stringReader.close();
                defaultStyles.addStyleSheet(super.getStyleSheet());
            }
            return defaultStyles;
        }

        public Document createDefaultDocument(Font font, Color color) {
            StyleSheet styleSheet = getStyleSheet();
            StyleSheet styleSheet2 = new StyleSheet();
            styleSheet2.addStyleSheet(styleSheet);
            BasicDocument basicDocument = new BasicDocument(styleSheet2, font, color);
            basicDocument.setAsynchronousLoadPriority(Integer.MAX_VALUE);
            basicDocument.setPreservesUnknownTags(false);
            return basicDocument;
        }

        @Override // javax.swing.text.html.HTMLEditorKit, javax.swing.text.StyledEditorKit, javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
        public ViewFactory getViewFactory() {
            return BasicHTML.basicHTMLViewFactory;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicHTML$BasicHTMLViewFactory.class */
    static class BasicHTMLViewFactory extends HTMLEditorKit.HTMLFactory {
        private static Boolean useOV = null;

        BasicHTMLViewFactory() {
        }

        @Override // javax.swing.text.html.HTMLEditorKit.HTMLFactory, javax.swing.text.ViewFactory
        public View create(Element element) {
            try {
                setAllowHTMLObject();
                View viewCreate = super.create(element);
                clearAllowHTMLObject();
                if (viewCreate instanceof ImageView) {
                    ((ImageView) viewCreate).setLoadsSynchronously(true);
                }
                return viewCreate;
            } catch (Throwable th) {
                clearAllowHTMLObject();
                throw th;
            }
        }

        private static void setAllowHTMLObject() {
            if (useOV == null) {
                useOV = (Boolean) AccessController.doPrivileged(new GetBooleanAction("swing.html.object"));
            }
            SwingAccessor.setAllowHTMLObject(useOV);
        }

        private static void clearAllowHTMLObject() {
            SwingAccessor.setAllowHTMLObject(null);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicHTML$BasicDocument.class */
    static class BasicDocument extends HTMLDocument {
        BasicDocument(StyleSheet styleSheet, Font font, Color color) {
            super(styleSheet);
            setPreservesUnknownTags(false);
            setFontAndColor(font, color);
        }

        private void setFontAndColor(Font font, Color color) {
            getStyleSheet().addRule(SwingUtilities2.displayPropertiesToCSS(font, color));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicHTML$Renderer.class */
    static class Renderer extends View {
        private int width;
        private View view;
        private ViewFactory factory;
        private JComponent host;

        Renderer(JComponent jComponent, ViewFactory viewFactory, View view) {
            super(null);
            this.host = jComponent;
            this.factory = viewFactory;
            this.view = view;
            this.view.setParent(this);
            setSize(this.view.getPreferredSpan(0), this.view.getPreferredSpan(1));
        }

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            return null;
        }

        @Override // javax.swing.text.View
        public float getPreferredSpan(int i2) {
            if (i2 == 0) {
                return this.width;
            }
            return this.view.getPreferredSpan(i2);
        }

        @Override // javax.swing.text.View
        public float getMinimumSpan(int i2) {
            return this.view.getMinimumSpan(i2);
        }

        @Override // javax.swing.text.View
        public float getMaximumSpan(int i2) {
            return 2.1474836E9f;
        }

        @Override // javax.swing.text.View
        public void preferenceChanged(View view, boolean z2, boolean z3) {
            this.host.revalidate();
            this.host.repaint();
        }

        @Override // javax.swing.text.View
        public float getAlignment(int i2) {
            return this.view.getAlignment(i2);
        }

        @Override // javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            Rectangle bounds = shape.getBounds();
            this.view.setSize(bounds.width, bounds.height);
            this.view.paint(graphics, shape);
        }

        @Override // javax.swing.text.View
        public void setParent(View view) {
            throw new Error("Can't set parent on root view");
        }

        @Override // javax.swing.text.View
        public int getViewCount() {
            return 1;
        }

        @Override // javax.swing.text.View
        public View getView(int i2) {
            return this.view;
        }

        @Override // javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            return this.view.modelToView(i2, shape, bias);
        }

        @Override // javax.swing.text.View
        public Shape modelToView(int i2, Position.Bias bias, int i3, Position.Bias bias2, Shape shape) throws BadLocationException {
            return this.view.modelToView(i2, bias, i3, bias2, shape);
        }

        @Override // javax.swing.text.View
        public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
            return this.view.viewToModel(f2, f3, shape, biasArr);
        }

        @Override // javax.swing.text.View
        public Document getDocument() {
            return this.view.getDocument();
        }

        @Override // javax.swing.text.View
        public int getStartOffset() {
            return this.view.getStartOffset();
        }

        @Override // javax.swing.text.View
        public int getEndOffset() {
            return this.view.getEndOffset();
        }

        @Override // javax.swing.text.View
        public Element getElement() {
            return this.view.getElement();
        }

        @Override // javax.swing.text.View
        public void setSize(float f2, float f3) {
            this.width = (int) f2;
            this.view.setSize(f2, f3);
        }

        @Override // javax.swing.text.View
        public Container getContainer() {
            return this.host;
        }

        @Override // javax.swing.text.View
        public ViewFactory getViewFactory() {
            return this.factory;
        }
    }
}
