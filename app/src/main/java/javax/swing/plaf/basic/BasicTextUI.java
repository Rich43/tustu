package javax.swing.plaf.basic;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.DragRecognitionSupport;
import javax.swing.plaf.synth.SynthUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.TextAction;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import jdk.jfr.Enabled;
import sun.awt.AppContext;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI.class */
public abstract class BasicTextUI extends TextUI implements ViewFactory {
    transient JTextComponent editor;
    private DefaultCaret dropCaret;
    private static BasicCursor textCursor = new BasicCursor(2);
    private static final EditorKit defaultKit = new DefaultEditorKit();
    private static final TransferHandler defaultTransferHandler = new TextTransferHandler();
    private static final Position.Bias[] discardBias = new Position.Bias[1];
    transient RootView rootView = new RootView();
    transient UpdateHandler updateHandler = new UpdateHandler();
    private final DragListener dragListener = getDragListener();
    transient boolean painted = false;

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$BasicCaret.class */
    public static class BasicCaret extends DefaultCaret implements UIResource {
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$BasicHighlighter.class */
    public static class BasicHighlighter extends DefaultHighlighter implements UIResource {
    }

    protected abstract String getPropertyPrefix();

    protected Caret createCaret() {
        return new BasicCaret();
    }

    protected Highlighter createHighlighter() {
        return new BasicHighlighter();
    }

    protected String getKeymapName() {
        String name = getClass().getName();
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf >= 0) {
            name = name.substring(iLastIndexOf + 1, name.length());
        }
        return name;
    }

    protected Keymap createKeymap() {
        String keymapName = getKeymapName();
        Keymap keymap = JTextComponent.getKeymap(keymapName);
        if (keymap == null) {
            keymap = JTextComponent.addKeymap(keymapName, JTextComponent.getKeymap("default"));
            Object obj = DefaultLookup.get(this.editor, this, getPropertyPrefix() + ".keyBindings");
            if (obj != null && (obj instanceof JTextComponent.KeyBinding[])) {
                JTextComponent.loadKeymap(keymap, (JTextComponent.KeyBinding[]) obj, getComponent().getActions());
            }
        }
        return keymap;
    }

    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals(JTree.EDITABLE_PROPERTY) || propertyChangeEvent.getPropertyName().equals(Enabled.NAME)) {
            updateBackground((JTextComponent) propertyChangeEvent.getSource());
        }
    }

    private void updateBackground(JTextComponent jTextComponent) {
        if ((this instanceof SynthUI) || (jTextComponent instanceof JTextArea)) {
            return;
        }
        Color background = jTextComponent.getBackground();
        if (background instanceof UIResource) {
            String propertyPrefix = getPropertyPrefix();
            Color color = DefaultLookup.getColor(jTextComponent, this, propertyPrefix + ".disabledBackground", null);
            Color color2 = DefaultLookup.getColor(jTextComponent, this, propertyPrefix + ".inactiveBackground", null);
            Color color3 = DefaultLookup.getColor(jTextComponent, this, propertyPrefix + ".background", null);
            if (((jTextComponent instanceof JTextArea) || (jTextComponent instanceof JEditorPane)) && background != color && background != color2 && background != color3) {
                return;
            }
            Color color4 = null;
            if (!jTextComponent.isEnabled()) {
                color4 = color;
            }
            if (color4 == null && !jTextComponent.isEditable()) {
                color4 = color2;
            }
            if (color4 == null) {
                color4 = color3;
            }
            if (color4 != null && color4 != background) {
                jTextComponent.setBackground(color4);
            }
        }
    }

    protected void installDefaults() {
        String propertyPrefix = getPropertyPrefix();
        Font font = this.editor.getFont();
        if (font == null || (font instanceof UIResource)) {
            this.editor.setFont(UIManager.getFont(propertyPrefix + ".font"));
        }
        Color background = this.editor.getBackground();
        if (background == null || (background instanceof UIResource)) {
            this.editor.setBackground(UIManager.getColor(propertyPrefix + ".background"));
        }
        Color foreground = this.editor.getForeground();
        if (foreground == null || (foreground instanceof UIResource)) {
            this.editor.setForeground(UIManager.getColor(propertyPrefix + ".foreground"));
        }
        Color caretColor = this.editor.getCaretColor();
        if (caretColor == null || (caretColor instanceof UIResource)) {
            this.editor.setCaretColor(UIManager.getColor(propertyPrefix + ".caretForeground"));
        }
        Color selectionColor = this.editor.getSelectionColor();
        if (selectionColor == null || (selectionColor instanceof UIResource)) {
            this.editor.setSelectionColor(UIManager.getColor(propertyPrefix + ".selectionBackground"));
        }
        Color selectedTextColor = this.editor.getSelectedTextColor();
        if (selectedTextColor == null || (selectedTextColor instanceof UIResource)) {
            this.editor.setSelectedTextColor(UIManager.getColor(propertyPrefix + ".selectionForeground"));
        }
        Color disabledTextColor = this.editor.getDisabledTextColor();
        if (disabledTextColor == null || (disabledTextColor instanceof UIResource)) {
            this.editor.setDisabledTextColor(UIManager.getColor(propertyPrefix + ".inactiveForeground"));
        }
        Border border = this.editor.getBorder();
        if (border == null || (border instanceof UIResource)) {
            this.editor.setBorder(UIManager.getBorder(propertyPrefix + ".border"));
        }
        Insets margin = this.editor.getMargin();
        if (margin == null || (margin instanceof UIResource)) {
            this.editor.setMargin(UIManager.getInsets(propertyPrefix + ".margin"));
        }
        updateCursor();
    }

    private void installDefaults2() {
        this.editor.addMouseListener(this.dragListener);
        this.editor.addMouseMotionListener(this.dragListener);
        String propertyPrefix = getPropertyPrefix();
        Caret caret = this.editor.getCaret();
        if (caret == null || (caret instanceof UIResource)) {
            Caret caretCreateCaret = createCaret();
            this.editor.setCaret(caretCreateCaret);
            caretCreateCaret.setBlinkRate(DefaultLookup.getInt(getComponent(), this, propertyPrefix + ".caretBlinkRate", 500));
        }
        Highlighter highlighter = this.editor.getHighlighter();
        if (highlighter == null || (highlighter instanceof UIResource)) {
            this.editor.setHighlighter(createHighlighter());
        }
        TransferHandler transferHandler = this.editor.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            this.editor.setTransferHandler(getTransferHandler());
        }
    }

    protected void uninstallDefaults() {
        this.editor.removeMouseListener(this.dragListener);
        this.editor.removeMouseMotionListener(this.dragListener);
        if (this.editor.getCaretColor() instanceof UIResource) {
            this.editor.setCaretColor(null);
        }
        if (this.editor.getSelectionColor() instanceof UIResource) {
            this.editor.setSelectionColor(null);
        }
        if (this.editor.getDisabledTextColor() instanceof UIResource) {
            this.editor.setDisabledTextColor(null);
        }
        if (this.editor.getSelectedTextColor() instanceof UIResource) {
            this.editor.setSelectedTextColor(null);
        }
        if (this.editor.getBorder() instanceof UIResource) {
            this.editor.setBorder(null);
        }
        if (this.editor.getMargin() instanceof UIResource) {
            this.editor.setMargin(null);
        }
        if (this.editor.getCaret() instanceof UIResource) {
            this.editor.setCaret(null);
        }
        if (this.editor.getHighlighter() instanceof UIResource) {
            this.editor.setHighlighter(null);
        }
        if (this.editor.getTransferHandler() instanceof UIResource) {
            this.editor.setTransferHandler(null);
        }
        if (this.editor.getCursor() instanceof UIResource) {
            this.editor.setCursor(null);
        }
    }

    protected void installListeners() {
    }

    protected void uninstallListeners() {
    }

    protected void installKeyboardActions() {
        this.editor.setKeymap(createKeymap());
        InputMap inputMap = getInputMap();
        if (inputMap != null) {
            SwingUtilities.replaceUIInputMap(this.editor, 0, inputMap);
        }
        ActionMap actionMap = getActionMap();
        if (actionMap != null) {
            SwingUtilities.replaceUIActionMap(this.editor, actionMap);
        }
        updateFocusAcceleratorBinding(false);
    }

    InputMap getInputMap() {
        InputMapUIResource inputMapUIResource = new InputMapUIResource();
        InputMap inputMap = (InputMap) DefaultLookup.get(this.editor, this, getPropertyPrefix() + ".focusInputMap");
        if (inputMap != null) {
            inputMapUIResource.setParent(inputMap);
        }
        return inputMapUIResource;
    }

    void updateFocusAcceleratorBinding(boolean z2) {
        char focusAccelerator = this.editor.getFocusAccelerator();
        if (z2 || focusAccelerator != 0) {
            InputMap uIInputMap = SwingUtilities.getUIInputMap(this.editor, 2);
            if (uIInputMap == null && focusAccelerator != 0) {
                uIInputMap = new ComponentInputMapUIResource(this.editor);
                SwingUtilities.replaceUIInputMap(this.editor, 2, uIInputMap);
                SwingUtilities.replaceUIActionMap(this.editor, getActionMap());
            }
            if (uIInputMap != null) {
                uIInputMap.clear();
                if (focusAccelerator != 0) {
                    uIInputMap.put(KeyStroke.getKeyStroke(focusAccelerator, BasicLookAndFeel.getFocusAcceleratorKeyMask()), "requestFocus");
                    uIInputMap.put(KeyStroke.getKeyStroke(focusAccelerator, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask())), "requestFocus");
                }
            }
        }
    }

    void updateFocusTraversalKeys() {
        EditorKit editorKit = getEditorKit(this.editor);
        if (editorKit != null && (editorKit instanceof DefaultEditorKit)) {
            Set<AWTKeyStroke> focusTraversalKeys = this.editor.getFocusTraversalKeys(0);
            Set<AWTKeyStroke> focusTraversalKeys2 = this.editor.getFocusTraversalKeys(1);
            HashSet hashSet = new HashSet(focusTraversalKeys);
            HashSet hashSet2 = new HashSet(focusTraversalKeys2);
            if (this.editor.isEditable()) {
                hashSet.remove(KeyStroke.getKeyStroke(9, 0));
                hashSet2.remove(KeyStroke.getKeyStroke(9, 1));
            } else {
                hashSet.add(KeyStroke.getKeyStroke(9, 0));
                hashSet2.add(KeyStroke.getKeyStroke(9, 1));
            }
            LookAndFeel.installProperty(this.editor, "focusTraversalKeysForward", hashSet);
            LookAndFeel.installProperty(this.editor, "focusTraversalKeysBackward", hashSet2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCursor() {
        if (!this.editor.isCursorSet() || (this.editor.getCursor() instanceof UIResource)) {
            this.editor.setCursor(this.editor.isEditable() ? textCursor : null);
        }
    }

    TransferHandler getTransferHandler() {
        return defaultTransferHandler;
    }

    ActionMap getActionMap() {
        Action action;
        String str = getPropertyPrefix() + ".actionMap";
        ActionMap actionMapCreateActionMap = (ActionMap) UIManager.get(str);
        if (actionMapCreateActionMap == null) {
            actionMapCreateActionMap = createActionMap();
            if (actionMapCreateActionMap != null) {
                UIManager.getLookAndFeelDefaults().put(str, actionMapCreateActionMap);
            }
        }
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        actionMapUIResource.put("requestFocus", new FocusAction());
        if ((getEditorKit(this.editor) instanceof DefaultEditorKit) && actionMapCreateActionMap != null && (action = actionMapCreateActionMap.get(DefaultEditorKit.insertBreakAction)) != null && (action instanceof DefaultEditorKit.InsertBreakAction)) {
            TextActionWrapper textActionWrapper = new TextActionWrapper((TextAction) action);
            actionMapUIResource.put(textActionWrapper.getValue("Name"), textActionWrapper);
        }
        if (actionMapCreateActionMap != null) {
            actionMapUIResource.setParent(actionMapCreateActionMap);
        }
        return actionMapUIResource;
    }

    ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        for (Action action : this.editor.getActions()) {
            actionMapUIResource.put(action.getValue("Name"), action);
        }
        actionMapUIResource.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
        actionMapUIResource.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
        actionMapUIResource.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
        return actionMapUIResource;
    }

    protected void uninstallKeyboardActions() {
        this.editor.setKeymap(null);
        SwingUtilities.replaceUIInputMap(this.editor, 2, null);
        SwingUtilities.replaceUIActionMap(this.editor, null);
    }

    protected void paintBackground(Graphics graphics) {
        graphics.setColor(this.editor.getBackground());
        graphics.fillRect(0, 0, this.editor.getWidth(), this.editor.getHeight());
    }

    protected final JTextComponent getComponent() {
        return this.editor;
    }

    protected void modelChanged() {
        setView(this.rootView.getViewFactory().create(this.editor.getDocument().getDefaultRootElement()));
    }

    protected final void setView(View view) {
        this.rootView.setView(view);
        this.painted = false;
        this.editor.revalidate();
        this.editor.repaint();
    }

    protected void paintSafely(Graphics graphics) {
        this.painted = true;
        Highlighter highlighter = this.editor.getHighlighter();
        Caret caret = this.editor.getCaret();
        if (this.editor.isOpaque()) {
            paintBackground(graphics);
        }
        if (highlighter != null) {
            highlighter.paint(graphics);
        }
        Rectangle visibleEditorRect = getVisibleEditorRect();
        if (visibleEditorRect != null) {
            this.rootView.paint(graphics, visibleEditorRect);
        }
        if (caret != null) {
            caret.paint(graphics);
        }
        if (this.dropCaret != null) {
            this.dropCaret.paint(graphics);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        if (jComponent instanceof JTextComponent) {
            this.editor = (JTextComponent) jComponent;
            LookAndFeel.installProperty(this.editor, "opaque", Boolean.TRUE);
            LookAndFeel.installProperty(this.editor, "autoscrolls", Boolean.TRUE);
            installDefaults();
            installDefaults2();
            this.editor.addPropertyChangeListener(this.updateHandler);
            Document document = this.editor.getDocument();
            if (document == null) {
                this.editor.setDocument(getEditorKit(this.editor).createDefaultDocument());
            } else {
                document.addDocumentListener(this.updateHandler);
                modelChanged();
            }
            installListeners();
            installKeyboardActions();
            LayoutManager layout = this.editor.getLayout();
            if (layout == null || (layout instanceof UIResource)) {
                this.editor.setLayout(this.updateHandler);
            }
            updateBackground(this.editor);
            return;
        }
        throw new Error("TextUI needs JTextComponent");
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.editor.removePropertyChangeListener(this.updateHandler);
        this.editor.getDocument().removeDocumentListener(this.updateHandler);
        this.painted = false;
        uninstallDefaults();
        this.rootView.setView(null);
        jComponent.removeAll();
        if (jComponent.getLayout() instanceof UIResource) {
            jComponent.setLayout(null);
        }
        uninstallKeyboardActions();
        uninstallListeners();
        this.editor = null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public final void paint(Graphics graphics, JComponent jComponent) {
        if (this.rootView.getViewCount() > 0 && this.rootView.getView(0) != null) {
            Document document = this.editor.getDocument();
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
            }
            try {
                paintSafely(graphics);
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
            } catch (Throwable th) {
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
                throw th;
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Document document = this.editor.getDocument();
        Insets insets = jComponent.getInsets();
        Dimension size = jComponent.getSize();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            if (size.width > insets.left + insets.right && size.height > insets.top + insets.bottom) {
                this.rootView.setSize((size.width - insets.left) - insets.right, (size.height - insets.top) - insets.bottom);
            } else if (size.width == 0 && size.height == 0) {
                this.rootView.setSize(2.1474836E9f, 2.1474836E9f);
            }
            size.width = (int) Math.min(((long) this.rootView.getPreferredSpan(0)) + insets.left + insets.right, 2147483647L);
            size.height = (int) Math.min(((long) this.rootView.getPreferredSpan(1)) + insets.top + insets.bottom, 2147483647L);
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            return size;
        } catch (Throwable th) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            throw th;
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Document document = this.editor.getDocument();
        Insets insets = jComponent.getInsets();
        Dimension dimension = new Dimension();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            dimension.width = ((int) this.rootView.getMinimumSpan(0)) + insets.left + insets.right;
            dimension.height = ((int) this.rootView.getMinimumSpan(1)) + insets.top + insets.bottom;
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            return dimension;
        } catch (Throwable th) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            throw th;
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Document document = this.editor.getDocument();
        Insets insets = jComponent.getInsets();
        Dimension dimension = new Dimension();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            dimension.width = (int) Math.min(((long) this.rootView.getMaximumSpan(0)) + insets.left + insets.right, 2147483647L);
            dimension.height = (int) Math.min(((long) this.rootView.getMaximumSpan(1)) + insets.top + insets.bottom, 2147483647L);
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            return dimension;
        } catch (Throwable th) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            throw th;
        }
    }

    protected Rectangle getVisibleEditorRect() {
        Rectangle bounds = this.editor.getBounds();
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

    @Override // javax.swing.plaf.TextUI
    public Rectangle modelToView(JTextComponent jTextComponent, int i2) throws BadLocationException {
        return modelToView(jTextComponent, i2, Position.Bias.Forward);
    }

    @Override // javax.swing.plaf.TextUI
    public Rectangle modelToView(JTextComponent jTextComponent, int i2, Position.Bias bias) throws BadLocationException {
        Document document = this.editor.getDocument();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            Rectangle visibleEditorRect = getVisibleEditorRect();
            if (visibleEditorRect != null) {
                this.rootView.setSize(visibleEditorRect.width, visibleEditorRect.height);
                Shape shapeModelToView = this.rootView.modelToView(i2, visibleEditorRect, bias);
                if (shapeModelToView != null) {
                    Rectangle bounds = shapeModelToView.getBounds();
                    if (document instanceof AbstractDocument) {
                        ((AbstractDocument) document).readUnlock();
                    }
                    return bounds;
                }
            }
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
                return null;
            }
            return null;
        } catch (Throwable th) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            throw th;
        }
    }

    @Override // javax.swing.plaf.TextUI
    public int viewToModel(JTextComponent jTextComponent, Point point) {
        return viewToModel(jTextComponent, point, discardBias);
    }

    @Override // javax.swing.plaf.TextUI
    public int viewToModel(JTextComponent jTextComponent, Point point, Position.Bias[] biasArr) {
        int iViewToModel = -1;
        Document document = this.editor.getDocument();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            Rectangle visibleEditorRect = getVisibleEditorRect();
            if (visibleEditorRect != null) {
                this.rootView.setSize(visibleEditorRect.width, visibleEditorRect.height);
                iViewToModel = this.rootView.viewToModel(point.f12370x, point.f12371y, visibleEditorRect, biasArr);
            }
            return iViewToModel;
        } finally {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
        }
    }

    @Override // javax.swing.plaf.TextUI
    public int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException {
        Document document = this.editor.getDocument();
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).readLock();
        }
        try {
            if (this.painted) {
                Rectangle visibleEditorRect = getVisibleEditorRect();
                if (visibleEditorRect != null) {
                    this.rootView.setSize(visibleEditorRect.width, visibleEditorRect.height);
                }
                int nextVisualPositionFrom = this.rootView.getNextVisualPositionFrom(i2, bias, visibleEditorRect, i3, biasArr);
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
                return nextVisualPositionFrom;
            }
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
                return -1;
            }
            return -1;
        } catch (Throwable th) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
            }
            throw th;
        }
    }

    @Override // javax.swing.plaf.TextUI
    public void damageRange(JTextComponent jTextComponent, int i2, int i3) {
        damageRange(jTextComponent, i2, i3, Position.Bias.Forward, Position.Bias.Backward);
    }

    @Override // javax.swing.plaf.TextUI
    public void damageRange(JTextComponent jTextComponent, int i2, int i3, Position.Bias bias, Position.Bias bias2) {
        Rectangle visibleEditorRect;
        if (this.painted && (visibleEditorRect = getVisibleEditorRect()) != null) {
            Document document = jTextComponent.getDocument();
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
            }
            try {
                this.rootView.setSize(visibleEditorRect.width, visibleEditorRect.height);
                Shape shapeModelToView = this.rootView.modelToView(i2, bias, i3, bias2, visibleEditorRect);
                Rectangle bounds = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                this.editor.repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
            } catch (BadLocationException e2) {
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
            } catch (Throwable th) {
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
                throw th;
            }
        }
    }

    @Override // javax.swing.plaf.TextUI
    public EditorKit getEditorKit(JTextComponent jTextComponent) {
        return defaultKit;
    }

    @Override // javax.swing.plaf.TextUI
    public View getRootView(JTextComponent jTextComponent) {
        return this.rootView;
    }

    @Override // javax.swing.plaf.TextUI
    public String getToolTipText(JTextComponent jTextComponent, Point point) {
        if (!this.painted) {
            return null;
        }
        Document document = this.editor.getDocument();
        String toolTipText = null;
        Rectangle visibleEditorRect = getVisibleEditorRect();
        if (visibleEditorRect != null) {
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
            }
            try {
                toolTipText = this.rootView.getToolTipText(point.f12370x, point.f12371y, visibleEditorRect);
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
            } catch (Throwable th) {
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readUnlock();
                }
                throw th;
            }
        }
        return toolTipText;
    }

    public View create(Element element) {
        return null;
    }

    public View create(Element element, int i2, int i3) {
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$BasicCursor.class */
    static class BasicCursor extends Cursor implements UIResource {
        BasicCursor(int i2) {
            super(i2);
        }

        BasicCursor(String str) {
            super(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$RootView.class */
    class RootView extends View {
        private View view;

        RootView() {
            super(null);
        }

        void setView(View view) {
            View view2 = this.view;
            this.view = null;
            if (view2 != null) {
                view2.setParent(null);
            }
            if (view != null) {
                view.setParent(this);
            }
            this.view = view;
        }

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            return null;
        }

        @Override // javax.swing.text.View
        public float getPreferredSpan(int i2) {
            if (this.view != null) {
                return this.view.getPreferredSpan(i2);
            }
            return 10.0f;
        }

        @Override // javax.swing.text.View
        public float getMinimumSpan(int i2) {
            if (this.view != null) {
                return this.view.getMinimumSpan(i2);
            }
            return 10.0f;
        }

        @Override // javax.swing.text.View
        public float getMaximumSpan(int i2) {
            return 2.1474836E9f;
        }

        @Override // javax.swing.text.View
        public void preferenceChanged(View view, boolean z2, boolean z3) {
            BasicTextUI.this.editor.revalidate();
        }

        @Override // javax.swing.text.View
        public float getAlignment(int i2) {
            if (this.view != null) {
                return this.view.getAlignment(i2);
            }
            return 0.0f;
        }

        @Override // javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            if (this.view != null) {
                Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
                setSize(bounds.width, bounds.height);
                this.view.paint(graphics, shape);
            }
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
        public int getViewIndex(int i2, Position.Bias bias) {
            return 0;
        }

        @Override // javax.swing.text.View
        public Shape getChildAllocation(int i2, Shape shape) {
            return shape;
        }

        @Override // javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            if (this.view != null) {
                return this.view.modelToView(i2, shape, bias);
            }
            return null;
        }

        @Override // javax.swing.text.View
        public Shape modelToView(int i2, Position.Bias bias, int i3, Position.Bias bias2, Shape shape) throws BadLocationException {
            if (this.view != null) {
                return this.view.modelToView(i2, bias, i3, bias2, shape);
            }
            return null;
        }

        @Override // javax.swing.text.View
        public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
            if (this.view != null) {
                return this.view.viewToModel(f2, f3, shape, biasArr);
            }
            return -1;
        }

        @Override // javax.swing.text.View
        public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
            if (i2 < -1) {
                throw new BadLocationException("invalid position", i2);
            }
            if (this.view != null) {
                int nextVisualPositionFrom = this.view.getNextVisualPositionFrom(i2, bias, shape, i3, biasArr);
                if (nextVisualPositionFrom != -1) {
                    i2 = nextVisualPositionFrom;
                } else {
                    biasArr[0] = bias;
                }
            }
            return i2;
        }

        @Override // javax.swing.text.View
        public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (this.view != null) {
                this.view.insertUpdate(documentEvent, shape, viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (this.view != null) {
                this.view.removeUpdate(documentEvent, shape, viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (this.view != null) {
                this.view.changedUpdate(documentEvent, shape, viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public Document getDocument() {
            return BasicTextUI.this.editor.getDocument();
        }

        @Override // javax.swing.text.View
        public int getStartOffset() {
            if (this.view != null) {
                return this.view.getStartOffset();
            }
            return getElement().getStartOffset();
        }

        @Override // javax.swing.text.View
        public int getEndOffset() {
            if (this.view != null) {
                return this.view.getEndOffset();
            }
            return getElement().getEndOffset();
        }

        @Override // javax.swing.text.View
        public Element getElement() {
            if (this.view != null) {
                return this.view.getElement();
            }
            return BasicTextUI.this.editor.getDocument().getDefaultRootElement();
        }

        public View breakView(int i2, float f2, Shape shape) {
            throw new Error("Can't break root view");
        }

        @Override // javax.swing.text.View
        public int getResizeWeight(int i2) {
            if (this.view != null) {
                return this.view.getResizeWeight(i2);
            }
            return 0;
        }

        @Override // javax.swing.text.View
        public void setSize(float f2, float f3) {
            if (this.view != null) {
                this.view.setSize(f2, f3);
            }
        }

        @Override // javax.swing.text.View
        public Container getContainer() {
            return BasicTextUI.this.editor;
        }

        @Override // javax.swing.text.View
        public ViewFactory getViewFactory() {
            ViewFactory viewFactory = BasicTextUI.this.getEditorKit(BasicTextUI.this.editor).getViewFactory();
            if (viewFactory != null) {
                return viewFactory;
            }
            return BasicTextUI.this;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$UpdateHandler.class */
    class UpdateHandler implements PropertyChangeListener, DocumentListener, LayoutManager2, UIResource {
        private Hashtable<Component, Object> constraints;
        private boolean i18nView = false;

        UpdateHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public final void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object oldValue = propertyChangeEvent.getOldValue();
            Object newValue = propertyChangeEvent.getNewValue();
            String propertyName = propertyChangeEvent.getPropertyName();
            if ((oldValue instanceof Document) || (newValue instanceof Document)) {
                if (oldValue != null) {
                    ((Document) oldValue).removeDocumentListener(this);
                    this.i18nView = false;
                }
                if (newValue != null) {
                    ((Document) newValue).addDocumentListener(this);
                    if (Constants.DOCUMENT_PNAME == propertyName) {
                        BasicTextUI.this.setView(null);
                        BasicTextUI.this.propertyChange(propertyChangeEvent);
                        BasicTextUI.this.modelChanged();
                        return;
                    }
                }
                BasicTextUI.this.modelChanged();
            }
            if ("focusAccelerator" == propertyName) {
                BasicTextUI.this.updateFocusAcceleratorBinding(true);
            } else if ("componentOrientation" == propertyName || "font" == propertyName) {
                BasicTextUI.this.modelChanged();
            } else if ("dropLocation" == propertyName) {
                dropIndexChanged();
            } else if (JTree.EDITABLE_PROPERTY == propertyName) {
                BasicTextUI.this.updateCursor();
                BasicTextUI.this.modelChanged();
            }
            BasicTextUI.this.propertyChange(propertyChangeEvent);
        }

        private void dropIndexChanged() {
            if (BasicTextUI.this.editor.getDropMode() == DropMode.USE_SELECTION) {
                return;
            }
            JTextComponent.DropLocation dropLocation = BasicTextUI.this.editor.getDropLocation();
            if (dropLocation == null) {
                if (BasicTextUI.this.dropCaret != null) {
                    BasicTextUI.this.dropCaret.deinstall(BasicTextUI.this.editor);
                    BasicTextUI.this.editor.repaint(BasicTextUI.this.dropCaret);
                    BasicTextUI.this.dropCaret = null;
                    return;
                }
                return;
            }
            if (BasicTextUI.this.dropCaret == null) {
                BasicTextUI.this.dropCaret = new BasicCaret();
                BasicTextUI.this.dropCaret.install(BasicTextUI.this.editor);
                BasicTextUI.this.dropCaret.setVisible(true);
            }
            BasicTextUI.this.dropCaret.setDot(dropLocation.getIndex(), dropLocation.getBias());
        }

        @Override // javax.swing.event.DocumentListener
        public final void insertUpdate(DocumentEvent documentEvent) {
            Object property = documentEvent.getDocument().getProperty("i18n");
            if (property instanceof Boolean) {
                Boolean bool = (Boolean) property;
                if (bool.booleanValue() != this.i18nView) {
                    this.i18nView = bool.booleanValue();
                    BasicTextUI.this.modelChanged();
                    return;
                }
            }
            BasicTextUI.this.rootView.insertUpdate(documentEvent, BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null, BasicTextUI.this.rootView.getViewFactory());
        }

        @Override // javax.swing.event.DocumentListener
        public final void removeUpdate(DocumentEvent documentEvent) {
            BasicTextUI.this.rootView.removeUpdate(documentEvent, BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null, BasicTextUI.this.rootView.getViewFactory());
        }

        @Override // javax.swing.event.DocumentListener
        public final void changedUpdate(DocumentEvent documentEvent) {
            BasicTextUI.this.rootView.changedUpdate(documentEvent, BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null, BasicTextUI.this.rootView.getViewFactory());
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            if (this.constraints != null) {
                this.constraints.remove(component);
            }
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return null;
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return null;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            Rectangle visibleEditorRect;
            if (this.constraints != null && !this.constraints.isEmpty() && (visibleEditorRect = BasicTextUI.this.getVisibleEditorRect()) != null) {
                Document document = BasicTextUI.this.editor.getDocument();
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).readLock();
                }
                try {
                    BasicTextUI.this.rootView.setSize(visibleEditorRect.width, visibleEditorRect.height);
                    Enumeration<Component> enumerationKeys = this.constraints.keys();
                    while (enumerationKeys.hasMoreElements()) {
                        Component componentNextElement2 = enumerationKeys.nextElement2();
                        Shape shapeCalculateViewPosition = calculateViewPosition(visibleEditorRect, (View) this.constraints.get(componentNextElement2));
                        if (shapeCalculateViewPosition != null) {
                            componentNextElement2.setBounds(shapeCalculateViewPosition instanceof Rectangle ? (Rectangle) shapeCalculateViewPosition : shapeCalculateViewPosition.getBounds());
                        }
                    }
                } finally {
                    if (document instanceof AbstractDocument) {
                        ((AbstractDocument) document).readUnlock();
                    }
                }
            }
        }

        Shape calculateViewPosition(Shape shape, View view) {
            int startOffset = view.getStartOffset();
            View view2 = null;
            View view3 = BasicTextUI.this.rootView;
            while (true) {
                View view4 = view3;
                if (view4 == null || view4 == view) {
                    break;
                }
                int viewIndex = view4.getViewIndex(startOffset, Position.Bias.Forward);
                shape = view4.getChildAllocation(viewIndex, shape);
                view2 = view4.getView(viewIndex);
                view3 = view2;
            }
            if (view2 != null) {
                return shape;
            }
            return null;
        }

        @Override // java.awt.LayoutManager2
        public void addLayoutComponent(Component component, Object obj) {
            if (obj instanceof View) {
                if (this.constraints == null) {
                    this.constraints = new Hashtable<>(7);
                }
                this.constraints.put(component, obj);
            }
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            return null;
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentX(Container container) {
            return 0.5f;
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentY(Container container) {
            return 0.5f;
        }

        @Override // java.awt.LayoutManager2
        public void invalidateLayout(Container container) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$TextActionWrapper.class */
    class TextActionWrapper extends TextAction {
        TextAction action;

        public TextActionWrapper(TextAction textAction) {
            super((String) textAction.getValue("Name"));
            this.action = null;
            this.action = textAction;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.action.actionPerformed(actionEvent);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            if (BasicTextUI.this.editor == null || BasicTextUI.this.editor.isEditable()) {
                return this.action.isEnabled();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$FocusAction.class */
    class FocusAction extends AbstractAction {
        FocusAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicTextUI.this.editor.requestFocus();
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return BasicTextUI.this.editor.isEditable();
        }
    }

    private static DragListener getDragListener() {
        DragListener dragListener;
        synchronized (DragListener.class) {
            DragListener dragListener2 = (DragListener) AppContext.getAppContext().get(DragListener.class);
            if (dragListener2 == null) {
                dragListener2 = new DragListener();
                AppContext.getAppContext().put(DragListener.class, dragListener2);
            }
            dragListener = dragListener2;
        }
        return dragListener;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$DragListener.class */
    static class DragListener extends MouseInputAdapter implements DragRecognitionSupport.BeforeDrag {
        private boolean dragStarted;

        DragListener() {
        }

        @Override // javax.swing.plaf.basic.DragRecognitionSupport.BeforeDrag
        public void dragStarting(MouseEvent mouseEvent) {
            this.dragStarted = true;
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (((JTextComponent) mouseEvent.getSource()).getDragEnabled()) {
                this.dragStarted = false;
                if (isDragPossible(mouseEvent) && DragRecognitionSupport.mousePressed(mouseEvent)) {
                    mouseEvent.consume();
                }
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (((JTextComponent) mouseEvent.getSource()).getDragEnabled()) {
                if (this.dragStarted) {
                    mouseEvent.consume();
                }
                DragRecognitionSupport.mouseReleased(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (((JTextComponent) mouseEvent.getSource()).getDragEnabled()) {
                if (this.dragStarted || DragRecognitionSupport.mouseDragged(mouseEvent, this)) {
                    mouseEvent.consume();
                }
            }
        }

        protected boolean isDragPossible(MouseEvent mouseEvent) {
            Caret caret;
            int dot;
            int mark;
            JTextComponent jTextComponent = (JTextComponent) mouseEvent.getSource();
            if (jTextComponent.isEnabled() && (dot = (caret = jTextComponent.getCaret()).getDot()) != (mark = caret.getMark())) {
                int iViewToModel = jTextComponent.viewToModel(new Point(mouseEvent.getX(), mouseEvent.getY()));
                int iMin = Math.min(dot, mark);
                int iMax = Math.max(dot, mark);
                if (iViewToModel >= iMin && iViewToModel < iMax) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$TextTransferHandler.class */
    static class TextTransferHandler extends TransferHandler implements UIResource {
        private JTextComponent exportComp;
        private boolean shouldRemove;
        private int p0;
        private int p1;
        private boolean modeBetween = false;
        private boolean isDrop = false;
        private int dropAction = 2;
        private Position.Bias dropBias;

        TextTransferHandler() {
        }

        protected DataFlavor getImportFlavor(DataFlavor[] dataFlavorArr, JTextComponent jTextComponent) {
            DataFlavor dataFlavor = null;
            DataFlavor dataFlavor2 = null;
            DataFlavor dataFlavor3 = null;
            if (jTextComponent instanceof JEditorPane) {
                for (int i2 = 0; i2 < dataFlavorArr.length; i2++) {
                    String mimeType = dataFlavorArr[i2].getMimeType();
                    if (mimeType.startsWith(((JEditorPane) jTextComponent).getEditorKit().getContentType())) {
                        return dataFlavorArr[i2];
                    }
                    if (dataFlavor == null && mimeType.startsWith(Clipboard.TEXT_TYPE)) {
                        dataFlavor = dataFlavorArr[i2];
                    } else if (dataFlavor2 == null && mimeType.startsWith(DataFlavor.javaJVMLocalObjectMimeType) && dataFlavorArr[i2].getRepresentationClass() == String.class) {
                        dataFlavor2 = dataFlavorArr[i2];
                    } else if (dataFlavor3 == null && dataFlavorArr[i2].equals(DataFlavor.stringFlavor)) {
                        dataFlavor3 = dataFlavorArr[i2];
                    }
                }
                if (dataFlavor != null) {
                    return dataFlavor;
                }
                if (dataFlavor2 != null) {
                    return dataFlavor2;
                }
                if (dataFlavor3 != null) {
                    return dataFlavor3;
                }
                return null;
            }
            for (int i3 = 0; i3 < dataFlavorArr.length; i3++) {
                String mimeType2 = dataFlavorArr[i3].getMimeType();
                if (mimeType2.startsWith(Clipboard.TEXT_TYPE)) {
                    return dataFlavorArr[i3];
                }
                if (dataFlavor2 == null && mimeType2.startsWith(DataFlavor.javaJVMLocalObjectMimeType) && dataFlavorArr[i3].getRepresentationClass() == String.class) {
                    dataFlavor2 = dataFlavorArr[i3];
                } else if (dataFlavor3 == null && dataFlavorArr[i3].equals(DataFlavor.stringFlavor)) {
                    dataFlavor3 = dataFlavorArr[i3];
                }
            }
            if (dataFlavor2 != null) {
                return dataFlavor2;
            }
            if (dataFlavor3 != null) {
                return dataFlavor3;
            }
            return null;
        }

        protected void handleReaderImport(Reader reader, JTextComponent jTextComponent, boolean z2) throws IOException, BadLocationException {
            if (z2) {
                int selectionStart = jTextComponent.getSelectionStart();
                int selectionEnd = jTextComponent.getSelectionEnd() - selectionStart;
                EditorKit editorKit = jTextComponent.getUI().getEditorKit(jTextComponent);
                Document document = jTextComponent.getDocument();
                if (selectionEnd > 0) {
                    document.remove(selectionStart, selectionEnd);
                }
                editorKit.read(reader, document, selectionStart);
                return;
            }
            char[] cArr = new char[1024];
            boolean z3 = false;
            StringBuffer stringBuffer = null;
            while (true) {
                int i2 = reader.read(cArr, 0, cArr.length);
                if (i2 != -1) {
                    if (stringBuffer == null) {
                        stringBuffer = new StringBuffer(i2);
                    }
                    int i3 = 0;
                    for (int i4 = 0; i4 < i2; i4++) {
                        switch (cArr[i4]) {
                            case '\n':
                                if (z3) {
                                    if (i4 > i3 + 1) {
                                        stringBuffer.append(cArr, i3, (i4 - i3) - 1);
                                    }
                                    z3 = false;
                                    i3 = i4;
                                    break;
                                } else {
                                    break;
                                }
                            case '\r':
                                if (z3) {
                                    if (i4 == 0) {
                                        stringBuffer.append('\n');
                                        break;
                                    } else {
                                        cArr[i4 - 1] = '\n';
                                        break;
                                    }
                                } else {
                                    z3 = true;
                                    break;
                                }
                            default:
                                if (z3) {
                                    if (i4 == 0) {
                                        stringBuffer.append('\n');
                                    } else {
                                        cArr[i4 - 1] = '\n';
                                    }
                                    z3 = false;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    if (i3 < i2) {
                        if (z3) {
                            if (i3 < i2 - 1) {
                                stringBuffer.append(cArr, i3, (i2 - i3) - 1);
                            }
                        } else {
                            stringBuffer.append(cArr, i3, i2 - i3);
                        }
                    }
                } else {
                    if (z3) {
                        stringBuffer.append('\n');
                    }
                    jTextComponent.replaceSelection(stringBuffer != null ? stringBuffer.toString() : "");
                    return;
                }
            }
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            if (!(jComponent instanceof JPasswordField) || jComponent.getClientProperty("JPasswordField.cutCopyAllowed") == Boolean.TRUE) {
                return ((JTextComponent) jComponent).isEditable() ? 3 : 1;
            }
            return 0;
        }

        @Override // javax.swing.TransferHandler
        protected Transferable createTransferable(JComponent jComponent) {
            this.exportComp = (JTextComponent) jComponent;
            this.shouldRemove = true;
            this.p0 = this.exportComp.getSelectionStart();
            this.p1 = this.exportComp.getSelectionEnd();
            if (this.p0 != this.p1) {
                return new TextTransferable(this.exportComp, this.p0, this.p1);
            }
            return null;
        }

        @Override // javax.swing.TransferHandler
        protected void exportDone(JComponent jComponent, Transferable transferable, int i2) {
            if (this.shouldRemove && i2 == 2) {
                ((TextTransferable) transferable).removeText();
            }
            this.exportComp = null;
        }

        @Override // javax.swing.TransferHandler
        public boolean importData(TransferHandler.TransferSupport transferSupport) {
            this.isDrop = transferSupport.isDrop();
            if (this.isDrop) {
                this.modeBetween = ((JTextComponent) transferSupport.getComponent()).getDropMode() == DropMode.INSERT;
                this.dropBias = ((JTextComponent.DropLocation) transferSupport.getDropLocation()).getBias();
                this.dropAction = transferSupport.getDropAction();
            }
            try {
                return super.importData(transferSupport);
            } finally {
                this.isDrop = false;
                this.modeBetween = false;
                this.dropBias = null;
                this.dropAction = 2;
            }
        }

        @Override // javax.swing.TransferHandler
        public boolean importData(JComponent jComponent, Transferable transferable) {
            JTextComponent jTextComponent = (JTextComponent) jComponent;
            int index = this.modeBetween ? jTextComponent.getDropLocation().getIndex() : jTextComponent.getCaretPosition();
            if (this.dropAction == 2 && jTextComponent == this.exportComp && index >= this.p0 && index <= this.p1) {
                this.shouldRemove = false;
                return true;
            }
            boolean z2 = false;
            DataFlavor importFlavor = getImportFlavor(transferable.getTransferDataFlavors(), jTextComponent);
            if (importFlavor != null) {
                try {
                    boolean z3 = false;
                    if (jComponent instanceof JEditorPane) {
                        JEditorPane jEditorPane = (JEditorPane) jComponent;
                        if (!jEditorPane.getContentType().startsWith(Clipboard.TEXT_TYPE) && importFlavor.getMimeType().startsWith(jEditorPane.getContentType())) {
                            z3 = true;
                        }
                    }
                    InputContext inputContext = jTextComponent.getInputContext();
                    if (inputContext != null) {
                        inputContext.endComposition();
                    }
                    Reader readerForText = importFlavor.getReaderForText(transferable);
                    if (this.modeBetween) {
                        Caret caret = jTextComponent.getCaret();
                        if (caret instanceof DefaultCaret) {
                            ((DefaultCaret) caret).setDot(index, this.dropBias);
                        } else {
                            jTextComponent.setCaretPosition(index);
                        }
                    }
                    handleReaderImport(readerForText, jTextComponent, z3);
                    if (this.isDrop) {
                        jTextComponent.requestFocus();
                        Caret caret2 = jTextComponent.getCaret();
                        if (caret2 instanceof DefaultCaret) {
                            int dot = caret2.getDot();
                            Position.Bias dotBias = ((DefaultCaret) caret2).getDotBias();
                            ((DefaultCaret) caret2).setDot(index, this.dropBias);
                            ((DefaultCaret) caret2).moveDot(dot, dotBias);
                        } else {
                            jTextComponent.select(index, jTextComponent.getCaretPosition());
                        }
                    }
                    z2 = true;
                } catch (UnsupportedFlavorException e2) {
                } catch (IOException e3) {
                } catch (BadLocationException e4) {
                }
            }
            return z2;
        }

        @Override // javax.swing.TransferHandler
        public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavorArr) {
            JTextComponent jTextComponent = (JTextComponent) jComponent;
            return jTextComponent.isEditable() && jTextComponent.isEnabled() && getImportFlavor(dataFlavorArr, jTextComponent) != null;
        }

        /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextUI$TextTransferHandler$TextTransferable.class */
        static class TextTransferable extends BasicTransferable {
            Position p0;
            Position p1;
            String mimeType;
            String richText;

            /* renamed from: c, reason: collision with root package name */
            JTextComponent f12818c;

            TextTransferable(JTextComponent jTextComponent, int i2, int i3) {
                super(null, null);
                this.f12818c = jTextComponent;
                Document document = jTextComponent.getDocument();
                try {
                    this.p0 = document.createPosition(i2);
                    this.p1 = document.createPosition(i3);
                    this.plainData = jTextComponent.getSelectedText();
                    if (jTextComponent instanceof JEditorPane) {
                        JEditorPane jEditorPane = (JEditorPane) jTextComponent;
                        this.mimeType = jEditorPane.getContentType();
                        if (this.mimeType.startsWith(Clipboard.TEXT_TYPE)) {
                            return;
                        }
                        StringWriter stringWriter = new StringWriter(this.p1.getOffset() - this.p0.getOffset());
                        jEditorPane.getEditorKit().write(stringWriter, document, this.p0.getOffset(), this.p1.getOffset() - this.p0.getOffset());
                        if (this.mimeType.startsWith(Clipboard.HTML_TYPE)) {
                            this.htmlData = stringWriter.toString();
                        } else {
                            this.richText = stringWriter.toString();
                        }
                    }
                } catch (IOException e2) {
                } catch (BadLocationException e3) {
                }
            }

            void removeText() {
                if (this.p0 != null && this.p1 != null && this.p0.getOffset() != this.p1.getOffset()) {
                    try {
                        this.f12818c.getDocument().remove(this.p0.getOffset(), this.p1.getOffset() - this.p0.getOffset());
                    } catch (BadLocationException e2) {
                    }
                }
            }

            @Override // javax.swing.plaf.basic.BasicTransferable
            protected DataFlavor[] getRicherFlavors() {
                if (this.richText == null) {
                    return null;
                }
                try {
                    return new DataFlavor[]{new DataFlavor(this.mimeType + ";class=java.lang.String"), new DataFlavor(this.mimeType + ";class=java.io.Reader"), new DataFlavor(this.mimeType + ";class=java.io.InputStream;charset=unicode")};
                } catch (ClassNotFoundException e2) {
                    return null;
                }
            }

            @Override // javax.swing.plaf.basic.BasicTransferable
            protected Object getRicherData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
                if (this.richText == null) {
                    return null;
                }
                if (String.class.equals(dataFlavor.getRepresentationClass())) {
                    return this.richText;
                }
                if (Reader.class.equals(dataFlavor.getRepresentationClass())) {
                    return new StringReader(this.richText);
                }
                if (InputStream.class.equals(dataFlavor.getRepresentationClass())) {
                    return new StringBufferInputStream(this.richText);
                }
                throw new UnsupportedFlavorException(dataFlavor);
            }
        }
    }
}
