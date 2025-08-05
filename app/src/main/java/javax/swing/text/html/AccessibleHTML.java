package javax.swing.text.html;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleTable;
import javax.accessibility.AccessibleText;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML.class */
class AccessibleHTML implements Accessible {
    private JEditorPane editor;
    private Document model;
    private DocumentListener docListener;
    private PropertyChangeListener propChangeListener = new PropertyChangeHandler();
    private ElementInfo rootElementInfo;
    private RootHTMLAccessibleContext rootHTMLAccessibleContext;

    public AccessibleHTML(JEditorPane jEditorPane) {
        this.editor = jEditorPane;
        setDocument(this.editor.getDocument());
        this.docListener = new DocumentHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDocument(Document document) {
        if (this.model != null) {
            this.model.removeDocumentListener(this.docListener);
        }
        if (this.editor != null) {
            this.editor.removePropertyChangeListener(this.propChangeListener);
        }
        this.model = document;
        if (this.model != null) {
            if (this.rootElementInfo != null) {
                this.rootElementInfo.invalidate(false);
            }
            buildInfo();
            this.model.addDocumentListener(this.docListener);
        } else {
            this.rootElementInfo = null;
        }
        if (this.editor != null) {
            this.editor.addPropertyChangeListener(this.propChangeListener);
        }
    }

    private Document getDocument() {
        return this.model;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JEditorPane getTextComponent() {
        return this.editor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ElementInfo getRootInfo() {
        return this.rootElementInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getRootView() {
        return getTextComponent().getUI().getRootView(getTextComponent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getRootEditorRect() {
        Rectangle bounds = getTextComponent().getBounds();
        if (bounds.width > 0 && bounds.height > 0) {
            bounds.f12373y = 0;
            bounds.f12372x = 0;
            Insets insets = this.editor.getInsets();
            bounds.f12372x += insets.left;
            bounds.f12373y += insets.top;
            bounds.width -= insets.left + insets.right;
            bounds.height -= insets.top + insets.bottom;
            return bounds;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object lock() {
        Document document = getDocument();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
            return document;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlock(Object obj) {
        if (obj != null) {
            ((AbstractDocument) obj).readUnlock();
        }
    }

    private void buildInfo() {
        Object objLock = lock();
        try {
            this.rootElementInfo = new ElementInfo(this, getDocument().getDefaultRootElement());
            this.rootElementInfo.validate();
            unlock(objLock);
        } catch (Throwable th) {
            unlock(objLock);
            throw th;
        }
    }

    ElementInfo createElementInfo(Element element, ElementInfo elementInfo) {
        AttributeSet attributes = element.getAttributes();
        if (attributes != null) {
            Object attribute = attributes.getAttribute(StyleConstants.NameAttribute);
            if (attribute == HTML.Tag.IMG) {
                return new IconElementInfo(element, elementInfo);
            }
            if (attribute == HTML.Tag.CONTENT || attribute == HTML.Tag.CAPTION) {
                return new TextElementInfo(element, elementInfo);
            }
            if (attribute == HTML.Tag.TABLE) {
                return new TableElementInfo(element, elementInfo);
            }
            return null;
        }
        return null;
    }

    @Override // javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.rootHTMLAccessibleContext == null) {
            this.rootHTMLAccessibleContext = new RootHTMLAccessibleContext(this.rootElementInfo);
        }
        return this.rootHTMLAccessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$RootHTMLAccessibleContext.class */
    private class RootHTMLAccessibleContext extends HTMLAccessibleContext {
        public RootHTMLAccessibleContext(ElementInfo elementInfo) {
            super(elementInfo);
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (AccessibleHTML.this.model != null) {
                return (String) AccessibleHTML.this.model.getProperty("title");
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            return AccessibleHTML.this.editor.getContentType();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TEXT;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$HTMLAccessibleContext.class */
    protected abstract class HTMLAccessibleContext extends AccessibleContext implements Accessible, AccessibleComponent {
        protected ElementInfo elementInfo;

        public HTMLAccessibleContext(ElementInfo elementInfo) {
            this.elementInfo = elementInfo;
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = new AccessibleStateSet();
            JEditorPane textComponent = AccessibleHTML.this.getTextComponent();
            if (textComponent.isEnabled()) {
                accessibleStateSet.add(AccessibleState.ENABLED);
            }
            if ((textComponent instanceof JTextComponent) && textComponent.isEditable()) {
                accessibleStateSet.add(AccessibleState.EDITABLE);
                accessibleStateSet.add(AccessibleState.FOCUSABLE);
            }
            if (textComponent.isVisible()) {
                accessibleStateSet.add(AccessibleState.VISIBLE);
            }
            if (textComponent.isShowing()) {
                accessibleStateSet.add(AccessibleState.SHOWING);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return this.elementInfo.getIndexInParent();
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return this.elementInfo.getChildCount();
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            Object child = this.elementInfo.getChild(i2);
            if (child != null && (child instanceof Accessible)) {
                return (Accessible) child;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() throws IllegalComponentStateException {
            return AccessibleHTML.this.editor.getLocale();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            return this;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getBackground() {
            return AccessibleHTML.this.getTextComponent().getBackground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBackground(Color color) {
            AccessibleHTML.this.getTextComponent().setBackground(color);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getForeground() {
            return AccessibleHTML.this.getTextComponent().getForeground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setForeground(Color color) {
            AccessibleHTML.this.getTextComponent().setForeground(color);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Cursor getCursor() {
            return AccessibleHTML.this.getTextComponent().getCursor();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setCursor(Cursor cursor) {
            AccessibleHTML.this.getTextComponent().setCursor(cursor);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Font getFont() {
            return AccessibleHTML.this.getTextComponent().getFont();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setFont(Font font) {
            AccessibleHTML.this.getTextComponent().setFont(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public FontMetrics getFontMetrics(Font font) {
            return AccessibleHTML.this.getTextComponent().getFontMetrics(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isEnabled() {
            return AccessibleHTML.this.getTextComponent().isEnabled();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setEnabled(boolean z2) {
            AccessibleHTML.this.getTextComponent().setEnabled(z2);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isVisible() {
            return AccessibleHTML.this.getTextComponent().isVisible();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setVisible(boolean z2) {
            AccessibleHTML.this.getTextComponent().setVisible(z2);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isShowing() {
            return AccessibleHTML.this.getTextComponent().isShowing();
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean contains(Point point) {
            Rectangle bounds = getBounds();
            if (bounds != null) {
                return bounds.contains(point.f12370x, point.f12371y);
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocationOnScreen() {
            Point locationOnScreen = AccessibleHTML.this.getTextComponent().getLocationOnScreen();
            Rectangle bounds = getBounds();
            if (bounds != null) {
                return new Point(locationOnScreen.f12370x + bounds.f12372x, locationOnScreen.f12371y + bounds.f12373y);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocation() {
            Rectangle bounds = getBounds();
            if (bounds != null) {
                return new Point(bounds.f12372x, bounds.f12373y);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setLocation(Point point) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Rectangle getBounds() {
            return this.elementInfo.getBounds();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBounds(Rectangle rectangle) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Dimension getSize() {
            Rectangle bounds = getBounds();
            if (bounds != null) {
                return new Dimension(bounds.width, bounds.height);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setSize(Dimension dimension) {
            AccessibleHTML.this.getTextComponent().setSize(dimension);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            Object elementInfoAt = getElementInfoAt(AccessibleHTML.this.rootElementInfo, point);
            if (elementInfoAt instanceof Accessible) {
                return (Accessible) elementInfoAt;
            }
            return null;
        }

        private ElementInfo getElementInfoAt(ElementInfo elementInfo, Point point) {
            ElementInfo captionInfo;
            Rectangle bounds;
            if (elementInfo.getBounds() == null) {
                return null;
            }
            if (elementInfo.getChildCount() == 0 && elementInfo.getBounds().contains(point)) {
                return elementInfo;
            }
            if ((elementInfo instanceof TableElementInfo) && (captionInfo = ((TableElementInfo) elementInfo).getCaptionInfo()) != null && (bounds = captionInfo.getBounds()) != null && bounds.contains(point)) {
                return captionInfo;
            }
            for (int i2 = 0; i2 < elementInfo.getChildCount(); i2++) {
                ElementInfo elementInfoAt = getElementInfoAt(elementInfo.getChild(i2), point);
                if (elementInfoAt != null) {
                    return elementInfoAt;
                }
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isFocusTraversable() {
            JEditorPane textComponent = AccessibleHTML.this.getTextComponent();
            if ((textComponent instanceof JTextComponent) && textComponent.isEditable()) {
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void requestFocus() {
            if (isFocusTraversable()) {
                JEditorPane textComponent = AccessibleHTML.this.getTextComponent();
                if (textComponent instanceof JTextComponent) {
                    textComponent.requestFocusInWindow();
                    try {
                        if (this.elementInfo.validateIfNecessary()) {
                            textComponent.setCaretPosition(this.elementInfo.getElement().getStartOffset());
                            AccessibleHTML.this.editor.getAccessibleContext().firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, new PropertyChangeEvent(this, AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED));
                        }
                    } catch (IllegalArgumentException e2) {
                    }
                }
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public void addFocusListener(FocusListener focusListener) {
            AccessibleHTML.this.getTextComponent().addFocusListener(focusListener);
        }

        @Override // javax.accessibility.AccessibleComponent
        public void removeFocusListener(FocusListener focusListener) {
            AccessibleHTML.this.getTextComponent().removeFocusListener(focusListener);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TextElementInfo.class */
    class TextElementInfo extends ElementInfo implements Accessible {
        private AccessibleContext accessibleContext;

        TextElementInfo(Element element, ElementInfo elementInfo) {
            super(element, elementInfo);
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new TextAccessibleContext(this);
            }
            return this.accessibleContext;
        }

        /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TextElementInfo$TextAccessibleContext.class */
        public class TextAccessibleContext extends HTMLAccessibleContext implements AccessibleText {
            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void removeFocusListener(FocusListener focusListener) {
                super.removeFocusListener(focusListener);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void addFocusListener(FocusListener focusListener) {
                super.addFocusListener(focusListener);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void requestFocus() {
                super.requestFocus();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isFocusTraversable() {
                return super.isFocusTraversable();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Accessible getAccessibleAt(Point point) {
                return super.getAccessibleAt(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setSize(Dimension dimension) {
                super.setSize(dimension);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Dimension getSize() {
                return super.getSize();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setBounds(Rectangle rectangle) {
                super.setBounds(rectangle);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Rectangle getBounds() {
                return super.getBounds();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setLocation(Point point) {
                super.setLocation(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Point getLocation() {
                return super.getLocation();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Point getLocationOnScreen() {
                return super.getLocationOnScreen();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean contains(Point point) {
                return super.contains(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isShowing() {
                return super.isShowing();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setVisible(boolean z2) {
                super.setVisible(z2);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isVisible() {
                return super.isVisible();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setEnabled(boolean z2) {
                super.setEnabled(z2);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isEnabled() {
                return super.isEnabled();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ FontMetrics getFontMetrics(Font font) {
                return super.getFontMetrics(font);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setFont(Font font) {
                super.setFont(font);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Font getFont() {
                return super.getFont();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setCursor(Cursor cursor) {
                super.setCursor(cursor);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Cursor getCursor() {
                return super.getCursor();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setForeground(Color color) {
                super.setForeground(color);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Color getForeground() {
                return super.getForeground();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setBackground(Color color) {
                super.setBackground(color);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Color getBackground() {
                return super.getBackground();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ AccessibleComponent getAccessibleComponent() {
                return super.getAccessibleComponent();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ Locale getLocale() throws IllegalComponentStateException {
                return super.getLocale();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ Accessible getAccessibleChild(int i2) {
                return super.getAccessibleChild(i2);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ int getAccessibleChildrenCount() {
                return super.getAccessibleChildrenCount();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ int getAccessibleIndexInParent() {
                return super.getAccessibleIndexInParent();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ AccessibleStateSet getAccessibleStateSet() {
                return super.getAccessibleStateSet();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.Accessible
            public /* bridge */ /* synthetic */ AccessibleContext getAccessibleContext() {
                return super.getAccessibleContext();
            }

            public TextAccessibleContext(ElementInfo elementInfo) {
                super(elementInfo);
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                if (AccessibleHTML.this.model != null) {
                    return (String) AccessibleHTML.this.model.getProperty("title");
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                return AccessibleHTML.this.editor.getContentType();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.TEXT;
            }

            @Override // javax.accessibility.AccessibleText
            public int getIndexAtPoint(Point point) {
                View view = TextElementInfo.this.getView();
                if (view != null) {
                    return view.viewToModel(point.f12370x, point.f12371y, getBounds());
                }
                return -1;
            }

            @Override // javax.accessibility.AccessibleText
            public Rectangle getCharacterBounds(int i2) {
                try {
                    return AccessibleHTML.this.editor.getUI().modelToView(AccessibleHTML.this.editor, i2);
                } catch (BadLocationException e2) {
                    return null;
                }
            }

            @Override // javax.accessibility.AccessibleText
            public int getCharCount() {
                if (TextElementInfo.this.validateIfNecessary()) {
                    Element element = this.elementInfo.getElement();
                    return element.getEndOffset() - element.getStartOffset();
                }
                return 0;
            }

            @Override // javax.accessibility.AccessibleText
            public int getCaretPosition() {
                Container container;
                View view = TextElementInfo.this.getView();
                if (view != null && (container = view.getContainer()) != null && (container instanceof JTextComponent)) {
                    return ((JTextComponent) container).getCaretPosition();
                }
                return -1;
            }

            /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TextElementInfo$TextAccessibleContext$IndexedSegment.class */
            private class IndexedSegment extends Segment {
                public int modelOffset;

                private IndexedSegment() {
                }
            }

            @Override // javax.accessibility.AccessibleText
            public String getAtIndex(int i2, int i3) {
                return getAtIndex(i2, i3, 0);
            }

            @Override // javax.accessibility.AccessibleText
            public String getAfterIndex(int i2, int i3) {
                return getAtIndex(i2, i3, 1);
            }

            @Override // javax.accessibility.AccessibleText
            public String getBeforeIndex(int i2, int i3) {
                return getAtIndex(i2, i3, -1);
            }

            private String getAtIndex(int i2, int i3, int i4) {
                if (AccessibleHTML.this.model instanceof AbstractDocument) {
                    ((AbstractDocument) AccessibleHTML.this.model).readLock();
                }
                if (i3 >= 0) {
                    try {
                        if (i3 < AccessibleHTML.this.model.getLength()) {
                            switch (i2) {
                                case 1:
                                    if (i3 + i4 < AccessibleHTML.this.model.getLength() && i3 + i4 >= 0) {
                                        String text = AccessibleHTML.this.model.getText(i3 + i4, 1);
                                        if (AccessibleHTML.this.model instanceof AbstractDocument) {
                                            ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                                        }
                                        return text;
                                    }
                                    break;
                                case 2:
                                case 3:
                                    IndexedSegment segmentAt = getSegmentAt(i2, i3);
                                    if (segmentAt != null) {
                                        if (i4 != 0) {
                                            int i5 = i4 < 0 ? segmentAt.modelOffset - 1 : segmentAt.modelOffset + (i4 * segmentAt.count);
                                            segmentAt = (i5 < 0 || i5 > AccessibleHTML.this.model.getLength()) ? null : getSegmentAt(i2, i5);
                                        }
                                        if (segmentAt != null) {
                                            String str = new String(segmentAt.array, segmentAt.offset, segmentAt.count);
                                            if (AccessibleHTML.this.model instanceof AbstractDocument) {
                                                ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                                            }
                                            return str;
                                        }
                                    }
                                    break;
                            }
                            if (!(AccessibleHTML.this.model instanceof AbstractDocument)) {
                                return null;
                            }
                            ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                            return null;
                        }
                    } catch (BadLocationException e2) {
                        if (!(AccessibleHTML.this.model instanceof AbstractDocument)) {
                            return null;
                        }
                        ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                        return null;
                    } catch (Throwable th) {
                        if (AccessibleHTML.this.model instanceof AbstractDocument) {
                            ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                        }
                        throw th;
                    }
                }
                if (AccessibleHTML.this.model instanceof AbstractDocument) {
                    ((AbstractDocument) AccessibleHTML.this.model).readUnlock();
                }
                return null;
            }

            private Element getParagraphElement(int i2) {
                Element element;
                if (!(AccessibleHTML.this.model instanceof PlainDocument)) {
                    if (!(AccessibleHTML.this.model instanceof StyledDocument)) {
                        Element defaultRootElement = AccessibleHTML.this.model.getDefaultRootElement();
                        while (true) {
                            element = defaultRootElement;
                            if (element.isLeaf()) {
                                break;
                            }
                            defaultRootElement = element.getElement(element.getElementIndex(i2));
                        }
                        if (element == null) {
                            return null;
                        }
                        return element.getParentElement();
                    }
                    return ((StyledDocument) AccessibleHTML.this.model).getParagraphElement(i2);
                }
                return ((PlainDocument) AccessibleHTML.this.model).getParagraphElement(i2);
            }

            private IndexedSegment getParagraphElementText(int i2) throws BadLocationException {
                Element paragraphElement = getParagraphElement(i2);
                if (paragraphElement != null) {
                    IndexedSegment indexedSegment = new IndexedSegment();
                    try {
                        AccessibleHTML.this.model.getText(paragraphElement.getStartOffset(), paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), indexedSegment);
                        indexedSegment.modelOffset = paragraphElement.getStartOffset();
                        return indexedSegment;
                    } catch (BadLocationException e2) {
                        return null;
                    }
                }
                return null;
            }

            private IndexedSegment getSegmentAt(int i2, int i3) throws BadLocationException {
                BreakIterator sentenceInstance;
                int iPrevious;
                IndexedSegment paragraphElementText = getParagraphElementText(i3);
                if (paragraphElementText == null) {
                    return null;
                }
                switch (i2) {
                    case 2:
                        sentenceInstance = BreakIterator.getWordInstance(getLocale());
                        break;
                    case 3:
                        sentenceInstance = BreakIterator.getSentenceInstance(getLocale());
                        break;
                    default:
                        return null;
                }
                paragraphElementText.first();
                sentenceInstance.setText(paragraphElementText);
                int iFollowing = sentenceInstance.following((i3 - paragraphElementText.modelOffset) + paragraphElementText.offset);
                if (iFollowing == -1 || iFollowing > paragraphElementText.offset + paragraphElementText.count || (iPrevious = sentenceInstance.previous()) == -1 || iPrevious >= paragraphElementText.offset + paragraphElementText.count) {
                    return null;
                }
                paragraphElementText.modelOffset = (paragraphElementText.modelOffset + iPrevious) - paragraphElementText.offset;
                paragraphElementText.offset = iPrevious;
                paragraphElementText.count = iFollowing - iPrevious;
                return paragraphElementText;
            }

            @Override // javax.accessibility.AccessibleText
            public AttributeSet getCharacterAttribute(int i2) {
                Element characterElement;
                if ((AccessibleHTML.this.model instanceof StyledDocument) && (characterElement = ((StyledDocument) AccessibleHTML.this.model).getCharacterElement(i2)) != null) {
                    return characterElement.getAttributes();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleText
            public int getSelectionStart() {
                return AccessibleHTML.this.editor.getSelectionStart();
            }

            @Override // javax.accessibility.AccessibleText
            public int getSelectionEnd() {
                return AccessibleHTML.this.editor.getSelectionEnd();
            }

            @Override // javax.accessibility.AccessibleText
            public String getSelectedText() {
                return AccessibleHTML.this.editor.getSelectedText();
            }

            private String getText(int i2, int i3) throws BadLocationException {
                if (AccessibleHTML.this.model != null && (AccessibleHTML.this.model instanceof StyledDocument)) {
                    return AccessibleHTML.this.model.getText(i2, i3);
                }
                return null;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$IconElementInfo.class */
    private class IconElementInfo extends ElementInfo implements Accessible {
        private int width;
        private int height;
        private AccessibleContext accessibleContext;

        IconElementInfo(Element element, ElementInfo elementInfo) {
            super(element, elementInfo);
            this.width = -1;
            this.height = -1;
        }

        @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
        protected void invalidate(boolean z2) {
            super.invalidate(z2);
            this.height = -1;
            this.width = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getImageSize(Object obj) {
            Image image;
            if (validateIfNecessary()) {
                int intAttr = getIntAttr(getAttributes(), obj, -1);
                if (intAttr == -1) {
                    View view = getView();
                    intAttr = 0;
                    if ((view instanceof ImageView) && (image = ((ImageView) view).getImage()) != null) {
                        intAttr = obj == HTML.Attribute.WIDTH ? image.getWidth(null) : image.getHeight(null);
                    }
                }
                return intAttr;
            }
            return 0;
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new IconAccessibleContext(this);
            }
            return this.accessibleContext;
        }

        /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$IconElementInfo$IconAccessibleContext.class */
        protected class IconAccessibleContext extends HTMLAccessibleContext implements AccessibleIcon {
            public IconAccessibleContext(ElementInfo elementInfo) {
                super(elementInfo);
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                return getAccessibleIconDescription();
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                return AccessibleHTML.this.editor.getContentType();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.ICON;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleIcon[] getAccessibleIcon() {
                return new AccessibleIcon[]{this};
            }

            @Override // javax.accessibility.AccessibleIcon
            public String getAccessibleIconDescription() {
                return ((ImageView) IconElementInfo.this.getView()).getAltText();
            }

            @Override // javax.accessibility.AccessibleIcon
            public void setAccessibleIconDescription(String str) {
            }

            @Override // javax.accessibility.AccessibleIcon
            public int getAccessibleIconWidth() {
                if (IconElementInfo.this.width == -1) {
                    IconElementInfo.this.width = IconElementInfo.this.getImageSize(HTML.Attribute.WIDTH);
                }
                return IconElementInfo.this.width;
            }

            @Override // javax.accessibility.AccessibleIcon
            public int getAccessibleIconHeight() {
                if (IconElementInfo.this.height == -1) {
                    IconElementInfo.this.height = IconElementInfo.this.getImageSize(HTML.Attribute.HEIGHT);
                }
                return IconElementInfo.this.height;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TableElementInfo.class */
    private class TableElementInfo extends ElementInfo implements Accessible {
        protected ElementInfo caption;
        private TableCellElementInfo[][] grid;
        private AccessibleContext accessibleContext;

        TableElementInfo(Element element, ElementInfo elementInfo) {
            super(element, elementInfo);
        }

        public ElementInfo getCaptionInfo() {
            return this.caption;
        }

        @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
        protected void validate() {
            super.validate();
            updateGrid();
        }

        @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
        protected void loadChildren(Element element) {
            for (int i2 = 0; i2 < element.getElementCount(); i2++) {
                Element element2 = element.getElement(i2);
                AttributeSet attributes = element2.getAttributes();
                if (attributes.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TR) {
                    addChild(new TableRowElementInfo(element2, this, i2));
                } else if (attributes.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.CAPTION) {
                    this.caption = AccessibleHTML.this.createElementInfo(element2, this);
                }
            }
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [javax.swing.text.html.AccessibleHTML$TableElementInfo$TableCellElementInfo[], javax.swing.text.html.AccessibleHTML$TableElementInfo$TableCellElementInfo[][]] */
        private void updateGrid() {
            int iMax = 0;
            int iMax2 = 0;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                TableRowElementInfo row = getRow(i2);
                int iMax3 = 0;
                for (int i3 = 0; i3 < iMax; i3++) {
                    iMax3 = Math.max(iMax3, getRow((i2 - i3) - 1).getColumnCount(i3 + 2));
                }
                iMax = Math.max(row.getRowCount(), iMax) - 1;
                iMax2 = Math.max(iMax2, row.getColumnCount() + iMax3);
            }
            int childCount = getChildCount() + iMax;
            this.grid = new TableCellElementInfo[childCount];
            for (int i4 = 0; i4 < childCount; i4++) {
                this.grid[i4] = new TableCellElementInfo[iMax2];
            }
            for (int i5 = 0; i5 < childCount; i5++) {
                getRow(i5).updateGrid(i5);
            }
        }

        public TableRowElementInfo getRow(int i2) {
            return (TableRowElementInfo) getChild(i2);
        }

        public TableCellElementInfo getCell(int i2, int i3) {
            if (validateIfNecessary() && i2 < this.grid.length && i3 < this.grid[0].length) {
                return this.grid[i2][i3];
            }
            return null;
        }

        public int getRowExtentAt(int i2, int i3) {
            TableCellElementInfo cell = getCell(i2, i3);
            if (cell != null) {
                int rowCount = cell.getRowCount();
                int i4 = 1;
                while (i2 - i4 >= 0 && this.grid[i2 - i4][i3] == cell) {
                    i4++;
                }
                return (rowCount - i4) + 1;
            }
            return 0;
        }

        public int getColumnExtentAt(int i2, int i3) {
            TableCellElementInfo cell = getCell(i2, i3);
            if (cell != null) {
                int columnCount = cell.getColumnCount();
                int i4 = 1;
                while (i3 - i4 >= 0 && this.grid[i2][i3 - i4] == cell) {
                    i4++;
                }
                return (columnCount - i4) + 1;
            }
            return 0;
        }

        public int getRowCount() {
            if (validateIfNecessary()) {
                return this.grid.length;
            }
            return 0;
        }

        public int getColumnCount() {
            if (validateIfNecessary() && this.grid.length > 0) {
                return this.grid[0].length;
            }
            return 0;
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new TableAccessibleContext(this);
            }
            return this.accessibleContext;
        }

        /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TableElementInfo$TableAccessibleContext.class */
        public class TableAccessibleContext extends HTMLAccessibleContext implements AccessibleTable {
            private AccessibleHeadersTable rowHeadersTable;

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void removeFocusListener(FocusListener focusListener) {
                super.removeFocusListener(focusListener);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void addFocusListener(FocusListener focusListener) {
                super.addFocusListener(focusListener);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void requestFocus() {
                super.requestFocus();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isFocusTraversable() {
                return super.isFocusTraversable();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Accessible getAccessibleAt(Point point) {
                return super.getAccessibleAt(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setSize(Dimension dimension) {
                super.setSize(dimension);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Dimension getSize() {
                return super.getSize();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setBounds(Rectangle rectangle) {
                super.setBounds(rectangle);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Rectangle getBounds() {
                return super.getBounds();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setLocation(Point point) {
                super.setLocation(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Point getLocation() {
                return super.getLocation();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Point getLocationOnScreen() {
                return super.getLocationOnScreen();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean contains(Point point) {
                return super.contains(point);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isShowing() {
                return super.isShowing();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setVisible(boolean z2) {
                super.setVisible(z2);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isVisible() {
                return super.isVisible();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setEnabled(boolean z2) {
                super.setEnabled(z2);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ boolean isEnabled() {
                return super.isEnabled();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ FontMetrics getFontMetrics(Font font) {
                return super.getFontMetrics(font);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setFont(Font font) {
                super.setFont(font);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Font getFont() {
                return super.getFont();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setCursor(Cursor cursor) {
                super.setCursor(cursor);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Cursor getCursor() {
                return super.getCursor();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setForeground(Color color) {
                super.setForeground(color);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Color getForeground() {
                return super.getForeground();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ void setBackground(Color color) {
                super.setBackground(color);
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleComponent
            public /* bridge */ /* synthetic */ Color getBackground() {
                return super.getBackground();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ AccessibleComponent getAccessibleComponent() {
                return super.getAccessibleComponent();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ Locale getLocale() throws IllegalComponentStateException {
                return super.getLocale();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public /* bridge */ /* synthetic */ AccessibleStateSet getAccessibleStateSet() {
                return super.getAccessibleStateSet();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.Accessible
            public /* bridge */ /* synthetic */ AccessibleContext getAccessibleContext() {
                return super.getAccessibleContext();
            }

            public TableAccessibleContext(ElementInfo elementInfo) {
                super(elementInfo);
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                return getAccessibleRole().toString();
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                return AccessibleHTML.this.editor.getContentType();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.TABLE;
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return this.elementInfo.getIndexInParent();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                return ((TableElementInfo) this.elementInfo).getRowCount() * ((TableElementInfo) this.elementInfo).getColumnCount();
            }

            @Override // javax.swing.text.html.AccessibleHTML.HTMLAccessibleContext, javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                int rowCount = ((TableElementInfo) this.elementInfo).getRowCount();
                int columnCount = ((TableElementInfo) this.elementInfo).getColumnCount();
                int i3 = i2 / rowCount;
                int i4 = i2 % columnCount;
                if (i3 < 0 || i3 >= rowCount || i4 < 0 || i4 >= columnCount) {
                    return null;
                }
                return getAccessibleAt(i3, i4);
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleTable getAccessibleTable() {
                return this;
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleCaption() {
                if (TableElementInfo.this.getCaptionInfo() instanceof Accessible) {
                    return (Accessible) TableElementInfo.this.caption;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleCaption(Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleSummary() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleSummary(Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleRowCount() {
                return ((TableElementInfo) this.elementInfo).getRowCount();
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleColumnCount() {
                return ((TableElementInfo) this.elementInfo).getColumnCount();
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleAt(int i2, int i3) {
                TableCellElementInfo cell = TableElementInfo.this.getCell(i2, i3);
                if (cell != null) {
                    return cell.getAccessible();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleRowExtentAt(int i2, int i3) {
                return ((TableElementInfo) this.elementInfo).getRowExtentAt(i2, i3);
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleColumnExtentAt(int i2, int i3) {
                return ((TableElementInfo) this.elementInfo).getColumnExtentAt(i2, i3);
            }

            @Override // javax.accessibility.AccessibleTable
            public AccessibleTable getAccessibleRowHeader() {
                return this.rowHeadersTable;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleRowHeader(AccessibleTable accessibleTable) {
            }

            @Override // javax.accessibility.AccessibleTable
            public AccessibleTable getAccessibleColumnHeader() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleColumnHeader(AccessibleTable accessibleTable) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleRowDescription(int i2) {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleRowDescription(int i2, Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleColumnDescription(int i2) {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleColumnDescription(int i2, Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleSelected(int i2, int i3) {
                TableCellElementInfo cell;
                if (TableElementInfo.this.validateIfNecessary() && i2 >= 0 && i2 < getAccessibleRowCount() && i3 >= 0 && i3 < getAccessibleColumnCount() && (cell = TableElementInfo.this.getCell(i2, i3)) != null) {
                    Element element = cell.getElement();
                    return element.getStartOffset() >= AccessibleHTML.this.editor.getSelectionStart() && element.getEndOffset() <= AccessibleHTML.this.editor.getSelectionEnd();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleRowSelected(int i2) {
                if (!TableElementInfo.this.validateIfNecessary() || i2 < 0 || i2 >= getAccessibleRowCount()) {
                    return false;
                }
                int accessibleColumnCount = getAccessibleColumnCount();
                TableCellElementInfo cell = TableElementInfo.this.getCell(i2, 0);
                if (cell == null) {
                    return false;
                }
                int startOffset = cell.getElement().getStartOffset();
                TableCellElementInfo cell2 = TableElementInfo.this.getCell(i2, accessibleColumnCount - 1);
                if (cell2 == null) {
                    return false;
                }
                return startOffset >= AccessibleHTML.this.editor.getSelectionStart() && cell2.getElement().getEndOffset() <= AccessibleHTML.this.editor.getSelectionEnd();
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleColumnSelected(int i2) {
                if (!TableElementInfo.this.validateIfNecessary() || i2 < 0 || i2 >= getAccessibleColumnCount()) {
                    return false;
                }
                int accessibleRowCount = getAccessibleRowCount();
                TableCellElementInfo cell = TableElementInfo.this.getCell(0, i2);
                if (cell == null) {
                    return false;
                }
                int startOffset = cell.getElement().getStartOffset();
                TableCellElementInfo cell2 = TableElementInfo.this.getCell(accessibleRowCount - 1, i2);
                if (cell2 == null) {
                    return false;
                }
                return startOffset >= AccessibleHTML.this.editor.getSelectionStart() && cell2.getElement().getEndOffset() <= AccessibleHTML.this.editor.getSelectionEnd();
            }

            @Override // javax.accessibility.AccessibleTable
            public int[] getSelectedAccessibleRows() {
                if (TableElementInfo.this.validateIfNecessary()) {
                    int accessibleRowCount = getAccessibleRowCount();
                    Vector vector = new Vector();
                    for (int i2 = 0; i2 < accessibleRowCount; i2++) {
                        if (isAccessibleRowSelected(i2)) {
                            vector.addElement(Integer.valueOf(i2));
                        }
                    }
                    int[] iArr = new int[vector.size()];
                    for (int i3 = 0; i3 < iArr.length; i3++) {
                        iArr[i3] = ((Integer) vector.elementAt(i3)).intValue();
                    }
                    return iArr;
                }
                return new int[0];
            }

            @Override // javax.accessibility.AccessibleTable
            public int[] getSelectedAccessibleColumns() {
                if (TableElementInfo.this.validateIfNecessary()) {
                    int accessibleRowCount = getAccessibleRowCount();
                    Vector vector = new Vector();
                    for (int i2 = 0; i2 < accessibleRowCount; i2++) {
                        if (isAccessibleColumnSelected(i2)) {
                            vector.addElement(Integer.valueOf(i2));
                        }
                    }
                    int[] iArr = new int[vector.size()];
                    for (int i3 = 0; i3 < iArr.length; i3++) {
                        iArr[i3] = ((Integer) vector.elementAt(i3)).intValue();
                    }
                    return iArr;
                }
                return new int[0];
            }

            public int getAccessibleRow(int i2) {
                if (!TableElementInfo.this.validateIfNecessary() || i2 >= getAccessibleColumnCount() * getAccessibleRowCount()) {
                    return -1;
                }
                return i2 / getAccessibleColumnCount();
            }

            public int getAccessibleColumn(int i2) {
                if (!TableElementInfo.this.validateIfNecessary() || i2 >= getAccessibleColumnCount() * getAccessibleRowCount()) {
                    return -1;
                }
                return i2 % getAccessibleColumnCount();
            }

            public int getAccessibleIndex(int i2, int i3) {
                if (!TableElementInfo.this.validateIfNecessary() || i2 >= getAccessibleRowCount() || i3 >= getAccessibleColumnCount()) {
                    return -1;
                }
                return (i2 * getAccessibleColumnCount()) + i3;
            }

            public String getAccessibleRowHeader(int i2) {
                View view;
                if (TableElementInfo.this.validateIfNecessary()) {
                    TableCellElementInfo cell = TableElementInfo.this.getCell(i2, 0);
                    if (cell.isHeaderCell() && (view = cell.getView()) != null && AccessibleHTML.this.model != null) {
                        try {
                            return AccessibleHTML.this.model.getText(view.getStartOffset(), view.getEndOffset() - view.getStartOffset());
                        } catch (BadLocationException e2) {
                            return null;
                        }
                    }
                    return null;
                }
                return null;
            }

            public String getAccessibleColumnHeader(int i2) {
                View view;
                if (TableElementInfo.this.validateIfNecessary()) {
                    TableCellElementInfo cell = TableElementInfo.this.getCell(0, i2);
                    if (cell.isHeaderCell() && (view = cell.getView()) != null && AccessibleHTML.this.model != null) {
                        try {
                            return AccessibleHTML.this.model.getText(view.getStartOffset(), view.getEndOffset() - view.getStartOffset());
                        } catch (BadLocationException e2) {
                            return null;
                        }
                    }
                    return null;
                }
                return null;
            }

            public void addRowHeader(TableCellElementInfo tableCellElementInfo, int i2) {
                if (this.rowHeadersTable == null) {
                    this.rowHeadersTable = new AccessibleHeadersTable();
                }
                this.rowHeadersTable.addHeader(tableCellElementInfo, i2);
            }

            /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TableElementInfo$TableAccessibleContext$AccessibleHeadersTable.class */
            protected class AccessibleHeadersTable implements AccessibleTable {
                private Hashtable<Integer, ArrayList<TableCellElementInfo>> headers = new Hashtable<>();
                private int rowCount = 0;
                private int columnCount = 0;

                protected AccessibleHeadersTable() {
                }

                public void addHeader(TableCellElementInfo tableCellElementInfo, int i2) {
                    Integer numValueOf = Integer.valueOf(i2);
                    ArrayList<TableCellElementInfo> arrayList = this.headers.get(numValueOf);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        this.headers.put(numValueOf, arrayList);
                    }
                    arrayList.add(tableCellElementInfo);
                }

                @Override // javax.accessibility.AccessibleTable
                public Accessible getAccessibleCaption() {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleCaption(Accessible accessible) {
                }

                @Override // javax.accessibility.AccessibleTable
                public Accessible getAccessibleSummary() {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleSummary(Accessible accessible) {
                }

                @Override // javax.accessibility.AccessibleTable
                public int getAccessibleRowCount() {
                    return this.rowCount;
                }

                @Override // javax.accessibility.AccessibleTable
                public int getAccessibleColumnCount() {
                    return this.columnCount;
                }

                private TableCellElementInfo getElementInfoAt(int i2, int i3) {
                    ArrayList<TableCellElementInfo> arrayList = this.headers.get(Integer.valueOf(i2));
                    if (arrayList != null) {
                        return arrayList.get(i3);
                    }
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public Accessible getAccessibleAt(int i2, int i3) {
                    Object elementInfoAt = getElementInfoAt(i2, i3);
                    if (elementInfoAt instanceof Accessible) {
                        return (Accessible) elementInfoAt;
                    }
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public int getAccessibleRowExtentAt(int i2, int i3) {
                    TableCellElementInfo elementInfoAt = getElementInfoAt(i2, i3);
                    if (elementInfoAt != null) {
                        return elementInfoAt.getRowCount();
                    }
                    return 0;
                }

                @Override // javax.accessibility.AccessibleTable
                public int getAccessibleColumnExtentAt(int i2, int i3) {
                    TableCellElementInfo elementInfoAt = getElementInfoAt(i2, i3);
                    if (elementInfoAt != null) {
                        return elementInfoAt.getRowCount();
                    }
                    return 0;
                }

                @Override // javax.accessibility.AccessibleTable
                public AccessibleTable getAccessibleRowHeader() {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleRowHeader(AccessibleTable accessibleTable) {
                }

                @Override // javax.accessibility.AccessibleTable
                public AccessibleTable getAccessibleColumnHeader() {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleColumnHeader(AccessibleTable accessibleTable) {
                }

                @Override // javax.accessibility.AccessibleTable
                public Accessible getAccessibleRowDescription(int i2) {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleRowDescription(int i2, Accessible accessible) {
                }

                @Override // javax.accessibility.AccessibleTable
                public Accessible getAccessibleColumnDescription(int i2) {
                    return null;
                }

                @Override // javax.accessibility.AccessibleTable
                public void setAccessibleColumnDescription(int i2, Accessible accessible) {
                }

                @Override // javax.accessibility.AccessibleTable
                public boolean isAccessibleSelected(int i2, int i3) {
                    return false;
                }

                @Override // javax.accessibility.AccessibleTable
                public boolean isAccessibleRowSelected(int i2) {
                    return false;
                }

                @Override // javax.accessibility.AccessibleTable
                public boolean isAccessibleColumnSelected(int i2) {
                    return false;
                }

                @Override // javax.accessibility.AccessibleTable
                public int[] getSelectedAccessibleRows() {
                    return new int[0];
                }

                @Override // javax.accessibility.AccessibleTable
                public int[] getSelectedAccessibleColumns() {
                    return new int[0];
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TableElementInfo$TableRowElementInfo.class */
        private class TableRowElementInfo extends ElementInfo {
            private TableElementInfo parent;
            private int rowNumber;

            TableRowElementInfo(Element element, TableElementInfo tableElementInfo, int i2) {
                super(element, tableElementInfo);
                this.parent = tableElementInfo;
                this.rowNumber = i2;
            }

            @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
            protected void loadChildren(Element element) {
                for (int i2 = 0; i2 < element.getElementCount(); i2++) {
                    AttributeSet attributes = element.getElement(i2).getAttributes();
                    if (attributes.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TH) {
                        TableCellElementInfo tableCellElementInfo = TableElementInfo.this.new TableCellElementInfo(element.getElement(i2), this, true);
                        addChild(tableCellElementInfo);
                        ((TableAccessibleContext) this.parent.getAccessibleContext().getAccessibleTable()).addRowHeader(tableCellElementInfo, this.rowNumber);
                    } else if (attributes.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TD) {
                        addChild(TableElementInfo.this.new TableCellElementInfo(element.getElement(i2), this, false));
                    }
                }
            }

            public int getRowCount() {
                int iMax = 1;
                if (validateIfNecessary()) {
                    for (int i2 = 0; i2 < getChildCount(); i2++) {
                        TableCellElementInfo tableCellElementInfo = (TableCellElementInfo) getChild(i2);
                        if (tableCellElementInfo.validateIfNecessary()) {
                            iMax = Math.max(iMax, tableCellElementInfo.getRowCount());
                        }
                    }
                }
                return iMax;
            }

            public int getColumnCount() {
                int columnCount = 0;
                if (validateIfNecessary()) {
                    for (int i2 = 0; i2 < getChildCount(); i2++) {
                        TableCellElementInfo tableCellElementInfo = (TableCellElementInfo) getChild(i2);
                        if (tableCellElementInfo.validateIfNecessary()) {
                            columnCount += tableCellElementInfo.getColumnCount();
                        }
                    }
                }
                return columnCount;
            }

            @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
            protected void invalidate(boolean z2) {
                super.invalidate(z2);
                getParent().invalidate(true);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void updateGrid(int i2) {
                if (validateIfNecessary()) {
                    boolean z2 = false;
                    while (!z2) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= TableElementInfo.this.grid[i2].length) {
                                break;
                            }
                            if (TableElementInfo.this.grid[i2][i3] != null) {
                                i3++;
                            } else {
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            i2++;
                        }
                    }
                    int columnCount = 0;
                    for (int i4 = 0; i4 < getChildCount(); i4++) {
                        TableCellElementInfo tableCellElementInfo = (TableCellElementInfo) getChild(i4);
                        while (TableElementInfo.this.grid[i2][columnCount] != null) {
                            columnCount++;
                        }
                        for (int rowCount = tableCellElementInfo.getRowCount() - 1; rowCount >= 0; rowCount--) {
                            for (int columnCount2 = tableCellElementInfo.getColumnCount() - 1; columnCount2 >= 0; columnCount2--) {
                                TableElementInfo.this.grid[i2 + rowCount][columnCount + columnCount2] = tableCellElementInfo;
                            }
                        }
                        columnCount += tableCellElementInfo.getColumnCount();
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int getColumnCount(int i2) {
                if (validateIfNecessary()) {
                    int columnCount = 0;
                    for (int i3 = 0; i3 < getChildCount(); i3++) {
                        TableCellElementInfo tableCellElementInfo = (TableCellElementInfo) getChild(i3);
                        if (tableCellElementInfo.getRowCount() >= i2) {
                            columnCount += tableCellElementInfo.getColumnCount();
                        }
                    }
                    return columnCount;
                }
                return 0;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$TableElementInfo$TableCellElementInfo.class */
        private class TableCellElementInfo extends ElementInfo {
            private Accessible accessible;
            private boolean isHeaderCell;

            TableCellElementInfo(Element element, ElementInfo elementInfo) {
                super(element, elementInfo);
                this.isHeaderCell = false;
            }

            TableCellElementInfo(Element element, ElementInfo elementInfo, boolean z2) {
                super(element, elementInfo);
                this.isHeaderCell = z2;
            }

            public boolean isHeaderCell() {
                return this.isHeaderCell;
            }

            public Accessible getAccessible() {
                this.accessible = null;
                getAccessible(this);
                return this.accessible;
            }

            /* JADX WARN: Multi-variable type inference failed */
            private void getAccessible(ElementInfo elementInfo) {
                if (elementInfo instanceof Accessible) {
                    this.accessible = (Accessible) elementInfo;
                    return;
                }
                for (int i2 = 0; i2 < elementInfo.getChildCount(); i2++) {
                    getAccessible(elementInfo.getChild(i2));
                }
            }

            public int getRowCount() {
                if (validateIfNecessary()) {
                    return Math.max(1, getIntAttr(getAttributes(), HTML.Attribute.ROWSPAN, 1));
                }
                return 0;
            }

            public int getColumnCount() {
                if (validateIfNecessary()) {
                    return Math.max(1, getIntAttr(getAttributes(), HTML.Attribute.COLSPAN, 1));
                }
                return 0;
            }

            @Override // javax.swing.text.html.AccessibleHTML.ElementInfo
            protected void invalidate(boolean z2) {
                super.invalidate(z2);
                getParent().invalidate(true);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$ElementInfo.class */
    private class ElementInfo {
        private ArrayList<ElementInfo> children;
        private Element element;
        private ElementInfo parent;
        private boolean isValid;
        private boolean canBeValid;

        ElementInfo(AccessibleHTML accessibleHTML, Element element) {
            this(element, null);
        }

        ElementInfo(Element element, ElementInfo elementInfo) {
            this.element = element;
            this.parent = elementInfo;
            this.isValid = false;
            this.canBeValid = true;
        }

        protected void validate() {
            this.isValid = true;
            loadChildren(getElement());
        }

        protected void loadChildren(Element element) {
            if (!element.isLeaf()) {
                int elementCount = element.getElementCount();
                for (int i2 = 0; i2 < elementCount; i2++) {
                    Element element2 = element.getElement(i2);
                    ElementInfo elementInfoCreateElementInfo = AccessibleHTML.this.createElementInfo(element2, this);
                    if (elementInfoCreateElementInfo != null) {
                        addChild(elementInfoCreateElementInfo);
                    } else {
                        loadChildren(element2);
                    }
                }
            }
        }

        public int getIndexInParent() {
            if (this.parent == null || !this.parent.isValid()) {
                return -1;
            }
            return this.parent.indexOf(this);
        }

        public Element getElement() {
            return this.element;
        }

        public ElementInfo getParent() {
            return this.parent;
        }

        public int indexOf(ElementInfo elementInfo) {
            ArrayList<ElementInfo> arrayList = this.children;
            if (arrayList != null) {
                return arrayList.indexOf(elementInfo);
            }
            return -1;
        }

        public ElementInfo getChild(int i2) {
            ArrayList<ElementInfo> arrayList;
            if (validateIfNecessary() && (arrayList = this.children) != null && i2 >= 0 && i2 < arrayList.size()) {
                return arrayList.get(i2);
            }
            return null;
        }

        public int getChildCount() {
            validateIfNecessary();
            if (this.children == null) {
                return 0;
            }
            return this.children.size();
        }

        protected void addChild(ElementInfo elementInfo) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }
            this.children.add(elementInfo);
        }

        protected View getView() {
            if (validateIfNecessary()) {
                Object objLock = AccessibleHTML.this.lock();
                try {
                    View rootView = AccessibleHTML.this.getRootView();
                    Element element = getElement();
                    int startOffset = element.getStartOffset();
                    if (rootView != null) {
                        View view = getView(rootView, element, startOffset);
                        AccessibleHTML.this.unlock(objLock);
                        return view;
                    }
                    return null;
                } finally {
                    AccessibleHTML.this.unlock(objLock);
                }
            }
            return null;
        }

        public Rectangle getBounds() {
            if (validateIfNecessary()) {
                Object objLock = AccessibleHTML.this.lock();
                try {
                    Rectangle rootEditorRect = AccessibleHTML.this.getRootEditorRect();
                    View rootView = AccessibleHTML.this.getRootView();
                    Element element = getElement();
                    if (rootEditorRect != null && rootView != null) {
                        try {
                            Rectangle bounds = rootView.modelToView(element.getStartOffset(), Position.Bias.Forward, element.getEndOffset(), Position.Bias.Backward, rootEditorRect).getBounds();
                            AccessibleHTML.this.unlock(objLock);
                            return bounds;
                        } catch (BadLocationException e2) {
                        }
                    }
                    return null;
                } finally {
                    AccessibleHTML.this.unlock(objLock);
                }
            }
            return null;
        }

        protected boolean isValid() {
            return this.isValid;
        }

        protected AttributeSet getAttributes() {
            if (validateIfNecessary()) {
                return getElement().getAttributes();
            }
            return null;
        }

        protected AttributeSet getViewAttributes() {
            if (validateIfNecessary()) {
                View view = getView();
                if (view != null) {
                    return view.getElement().getAttributes();
                }
                return getElement().getAttributes();
            }
            return null;
        }

        protected int getIntAttr(AttributeSet attributeSet, Object obj, int i2) {
            int iMax;
            if (attributeSet != null && attributeSet.isDefined(obj)) {
                String str = (String) attributeSet.getAttribute(obj);
                if (str == null) {
                    iMax = i2;
                } else {
                    try {
                        iMax = Math.max(0, Integer.parseInt(str));
                    } catch (NumberFormatException e2) {
                        iMax = i2;
                    }
                }
                return iMax;
            }
            return i2;
        }

        protected boolean validateIfNecessary() {
            if (!isValid() && this.canBeValid) {
                this.children = null;
                Object objLock = AccessibleHTML.this.lock();
                try {
                    validate();
                } finally {
                    AccessibleHTML.this.unlock(objLock);
                }
            }
            return isValid();
        }

        protected void invalidate(boolean z2) {
            if (!isValid()) {
                if (this.canBeValid && !z2) {
                    this.canBeValid = false;
                    return;
                }
                return;
            }
            this.isValid = false;
            this.canBeValid = z2;
            if (this.children != null) {
                Iterator<ElementInfo> it = this.children.iterator();
                while (it.hasNext()) {
                    it.next().invalidate(false);
                }
                this.children = null;
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

        private int getClosestInfoIndex(int i2) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                ElementInfo child = getChild(i3);
                if (i2 < child.getElement().getEndOffset() || i2 == child.getElement().getStartOffset()) {
                    return i3;
                }
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void update(DocumentEvent documentEvent) {
            int closestInfoIndex;
            if (!isValid()) {
                return;
            }
            ElementInfo parent = getParent();
            Element element = getElement();
            while (documentEvent.getChange(element) == null) {
                element = element.getParentElement();
                if (parent == null || element == null || element == parent.getElement()) {
                    if (getChildCount() > 0) {
                        Element element2 = getElement();
                        int offset = documentEvent.getOffset();
                        int closestInfoIndex2 = getClosestInfoIndex(offset);
                        if (closestInfoIndex2 == -1 && documentEvent.getType() == DocumentEvent.EventType.REMOVE && offset >= element2.getEndOffset()) {
                            closestInfoIndex2 = getChildCount() - 1;
                        }
                        ElementInfo child = closestInfoIndex2 >= 0 ? getChild(closestInfoIndex2) : null;
                        if (child != null && child.getElement().getStartOffset() == offset && offset > 0) {
                            closestInfoIndex2 = Math.max(closestInfoIndex2 - 1, 0);
                        }
                        if (documentEvent.getType() != DocumentEvent.EventType.REMOVE) {
                            closestInfoIndex = getClosestInfoIndex(offset + documentEvent.getLength());
                            if (closestInfoIndex < 0) {
                                closestInfoIndex = getChildCount() - 1;
                            }
                        } else {
                            closestInfoIndex = closestInfoIndex2;
                            while (closestInfoIndex + 1 < getChildCount() && getChild(closestInfoIndex + 1).getElement().getEndOffset() == getChild(closestInfoIndex + 1).getElement().getStartOffset()) {
                                closestInfoIndex++;
                            }
                        }
                        for (int iMax = Math.max(closestInfoIndex2, 0); iMax <= closestInfoIndex && isValid(); iMax++) {
                            getChild(iMax).update(documentEvent);
                        }
                        return;
                    }
                    return;
                }
            }
            if (element == getElement()) {
                invalidate(true);
            } else if (parent != null) {
                parent.invalidate(parent == AccessibleHTML.this.getRootInfo());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$DocumentHandler.class */
    private class DocumentHandler implements DocumentListener {
        private DocumentHandler() {
        }

        @Override // javax.swing.event.DocumentListener
        public void insertUpdate(DocumentEvent documentEvent) {
            AccessibleHTML.this.getRootInfo().update(documentEvent);
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            AccessibleHTML.this.getRootInfo().update(documentEvent);
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
            AccessibleHTML.this.getRootInfo().update(documentEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/AccessibleHTML$PropertyChangeHandler.class */
    private class PropertyChangeHandler implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(Constants.DOCUMENT_PNAME)) {
                AccessibleHTML.this.setDocument(AccessibleHTML.this.editor.getDocument());
            }
        }
    }
}
