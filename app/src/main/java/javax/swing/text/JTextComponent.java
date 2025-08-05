package javax.swing.text;

import com.sun.beans.util.Cache;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;
import javax.accessibility.AccessibleExtendedText;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleTextSequence;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Position;
import sun.awt.AppContext;
import sun.swing.PrintingStatus;
import sun.swing.SwingAccessor;
import sun.swing.SwingUtilities2;
import sun.swing.text.TextComponentPrintable;

/* loaded from: rt.jar:javax/swing/text/JTextComponent.class */
public abstract class JTextComponent extends JComponent implements Scrollable, Accessible {
    public static final String FOCUS_ACCELERATOR_KEY = "focusAcceleratorKey";
    private Document model;
    private transient Caret caret;
    private NavigationFilter navigationFilter;
    private transient Highlighter highlighter;
    private transient Keymap keymap;
    private transient MutableCaretEvent caretEvent;
    private Color caretColor;
    private Color selectionColor;
    private Color selectedTextColor;
    private Color disabledTextColor;
    private boolean editable;
    private Insets margin;
    private char focusAccelerator;
    private boolean dragEnabled;
    private DropMode dropMode = DropMode.USE_SELECTION;
    private transient DropLocation dropLocation;
    private static DefaultTransferHandler defaultTransferHandler;
    private static Cache<Class<?>, Boolean> METHOD_OVERRIDDEN;
    private static final Object KEYMAP_TABLE;
    private transient InputMethodRequests inputMethodRequestsHandler;
    private SimpleAttributeSet composedTextAttribute;
    private String composedTextContent;
    private Position composedTextStart;
    private Position composedTextEnd;
    private Position latestCommittedTextStart;
    private Position latestCommittedTextEnd;
    private ComposedTextCaret composedTextCaret;
    private transient Caret originalCaret;
    private boolean checkedInputOverride;
    private boolean needToSendKeyTypedEvent;
    private static final Object FOCUSED_COMPONENT;
    public static final String DEFAULT_KEYMAP = "default";

    public JTextComponent() {
        enableEvents(2056L);
        this.caretEvent = new MutableCaretEvent(this);
        addMouseListener(this.caretEvent);
        addFocusListener(this.caretEvent);
        setEditable(true);
        setDragEnabled(false);
        setLayout(null);
        updateUI();
    }

    public TextUI getUI() {
        return (TextUI) this.ui;
    }

    public void setUI(TextUI textUI) {
        super.setUI((ComponentUI) textUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((TextUI) UIManager.getUI(this));
        invalidate();
    }

    public void addCaretListener(CaretListener caretListener) {
        this.listenerList.add(CaretListener.class, caretListener);
    }

    public void removeCaretListener(CaretListener caretListener) {
        this.listenerList.remove(CaretListener.class, caretListener);
    }

    public CaretListener[] getCaretListeners() {
        return (CaretListener[]) this.listenerList.getListeners(CaretListener.class);
    }

    protected void fireCaretUpdate(CaretEvent caretEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == CaretListener.class) {
                ((CaretListener) listenerList[length + 1]).caretUpdate(caretEvent);
            }
        }
    }

    public void setDocument(Document document) {
        Document document2 = this.model;
        try {
            if (document2 instanceof AbstractDocument) {
                ((AbstractDocument) document2).readLock();
            }
            if (this.accessibleContext != null) {
                this.model.removeDocumentListener((AccessibleJTextComponent) this.accessibleContext);
            }
            if (this.inputMethodRequestsHandler != null) {
                this.model.removeDocumentListener((DocumentListener) this.inputMethodRequestsHandler);
            }
            this.model = document;
            Boolean bool = getComponentOrientation().isLeftToRight() ? TextAttribute.RUN_DIRECTION_LTR : TextAttribute.RUN_DIRECTION_RTL;
            if (bool != document.getProperty(TextAttribute.RUN_DIRECTION)) {
                document.putProperty(TextAttribute.RUN_DIRECTION, bool);
            }
            firePropertyChange(Constants.DOCUMENT_PNAME, document2, document);
            if (document2 instanceof AbstractDocument) {
                ((AbstractDocument) document2).readUnlock();
            }
            revalidate();
            repaint();
            if (this.accessibleContext != null) {
                this.model.addDocumentListener((AccessibleJTextComponent) this.accessibleContext);
            }
            if (this.inputMethodRequestsHandler != null) {
                this.model.addDocumentListener((DocumentListener) this.inputMethodRequestsHandler);
            }
        } catch (Throwable th) {
            if (document2 instanceof AbstractDocument) {
                ((AbstractDocument) document2).readUnlock();
            }
            throw th;
        }
    }

    public Document getDocument() {
        return this.model;
    }

    @Override // java.awt.Component
    public void setComponentOrientation(ComponentOrientation componentOrientation) {
        Document document = getDocument();
        if (document != null) {
            document.putProperty(TextAttribute.RUN_DIRECTION, componentOrientation.isLeftToRight() ? TextAttribute.RUN_DIRECTION_LTR : TextAttribute.RUN_DIRECTION_RTL);
        }
        super.setComponentOrientation(componentOrientation);
    }

    public Action[] getActions() {
        return getUI().getEditorKit(this).getActions();
    }

    public void setMargin(Insets insets) {
        Insets insets2 = this.margin;
        this.margin = insets;
        firePropertyChange(AbstractButton.MARGIN_CHANGED_PROPERTY, insets2, insets);
        invalidate();
    }

    public Insets getMargin() {
        return this.margin;
    }

    public void setNavigationFilter(NavigationFilter navigationFilter) {
        this.navigationFilter = navigationFilter;
    }

    public NavigationFilter getNavigationFilter() {
        return this.navigationFilter;
    }

    @Transient
    public Caret getCaret() {
        return this.caret;
    }

    public void setCaret(Caret caret) {
        if (this.caret != null) {
            this.caret.removeChangeListener(this.caretEvent);
            this.caret.deinstall(this);
        }
        Caret caret2 = this.caret;
        this.caret = caret;
        if (this.caret != null) {
            this.caret.install(this);
            this.caret.addChangeListener(this.caretEvent);
        }
        firePropertyChange("caret", caret2, this.caret);
    }

    public Highlighter getHighlighter() {
        return this.highlighter;
    }

    public void setHighlighter(Highlighter highlighter) {
        if (this.highlighter != null) {
            this.highlighter.deinstall(this);
        }
        Highlighter highlighter2 = this.highlighter;
        this.highlighter = highlighter;
        if (this.highlighter != null) {
            this.highlighter.install(this);
        }
        firePropertyChange("highlighter", highlighter2, highlighter);
    }

    public void setKeymap(Keymap keymap) {
        Keymap keymap2 = this.keymap;
        this.keymap = keymap;
        firePropertyChange("keymap", keymap2, this.keymap);
        updateInputMap(keymap2, keymap);
    }

    public void setDragEnabled(boolean z2) {
        if (z2 && GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.dragEnabled = z2;
    }

    public boolean getDragEnabled() {
        return this.dragEnabled;
    }

    public final void setDropMode(DropMode dropMode) {
        if (dropMode != null) {
            switch (dropMode) {
                case USE_SELECTION:
                case INSERT:
                    this.dropMode = dropMode;
                    return;
            }
        }
        throw new IllegalArgumentException(((Object) dropMode) + ": Unsupported drop mode for text");
    }

    public final DropMode getDropMode() {
        return this.dropMode;
    }

    static {
        SwingAccessor.setJTextComponentAccessor(new SwingAccessor.JTextComponentAccessor() { // from class: javax.swing.text.JTextComponent.1
            @Override // sun.swing.SwingAccessor.JTextComponentAccessor
            public TransferHandler.DropLocation dropLocationForPoint(JTextComponent jTextComponent, Point point) {
                return jTextComponent.dropLocationForPoint(point);
            }

            @Override // sun.swing.SwingAccessor.JTextComponentAccessor
            public Object setDropLocation(JTextComponent jTextComponent, TransferHandler.DropLocation dropLocation, Object obj, boolean z2) {
                return jTextComponent.setDropLocation(dropLocation, obj, z2);
            }
        });
        METHOD_OVERRIDDEN = new Cache<Class<?>, Boolean>(Cache.Kind.WEAK, Cache.Kind.STRONG) { // from class: javax.swing.text.JTextComponent.4
            @Override // com.sun.beans.util.Cache
            public Boolean create(final Class<?> cls) {
                if (JTextComponent.class == cls) {
                    return Boolean.FALSE;
                }
                if (get(cls.getSuperclass()).booleanValue()) {
                    return Boolean.TRUE;
                }
                return (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.swing.text.JTextComponent.4.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Boolean run2() throws SecurityException {
                        try {
                            cls.getDeclaredMethod("processInputMethodEvent", InputMethodEvent.class);
                            return Boolean.TRUE;
                        } catch (NoSuchMethodException e2) {
                            return Boolean.FALSE;
                        }
                    }
                });
            }
        };
        KEYMAP_TABLE = new StringBuilder("JTextComponent_KeymapTable");
        FOCUSED_COMPONENT = new StringBuilder("JTextComponent_FocusedComponent");
    }

    DropLocation dropLocationForPoint(Point point) {
        Position.Bias[] biasArr = new Position.Bias[1];
        int iViewToModel = getUI().viewToModel(this, point, biasArr);
        if (biasArr[0] == null) {
            biasArr[0] = Position.Bias.Forward;
        }
        return new DropLocation(point, iViewToModel, biasArr[0]);
    }

    Object setDropLocation(TransferHandler.DropLocation dropLocation, Object obj, boolean z2) {
        boolean zIsVisible;
        Object objValueOf = null;
        DropLocation dropLocation2 = (DropLocation) dropLocation;
        if (this.dropMode == DropMode.USE_SELECTION) {
            if (dropLocation2 == null) {
                if (obj != null) {
                    Object[] objArr = (Object[]) obj;
                    if (!z2) {
                        if (this.caret instanceof DefaultCaret) {
                            ((DefaultCaret) this.caret).setDot(((Integer) objArr[0]).intValue(), (Position.Bias) objArr[3]);
                            ((DefaultCaret) this.caret).moveDot(((Integer) objArr[1]).intValue(), (Position.Bias) objArr[4]);
                        } else {
                            this.caret.setDot(((Integer) objArr[0]).intValue());
                            this.caret.moveDot(((Integer) objArr[1]).intValue());
                        }
                    }
                    this.caret.setVisible(((Boolean) objArr[2]).booleanValue());
                }
            } else {
                if (this.dropLocation == null) {
                    if (this.caret instanceof DefaultCaret) {
                        DefaultCaret defaultCaret = (DefaultCaret) this.caret;
                        objValueOf = new Object[]{Integer.valueOf(defaultCaret.getMark()), Integer.valueOf(defaultCaret.getDot()), Boolean.valueOf(defaultCaret.isActive()), defaultCaret.getMarkBias(), defaultCaret.getDotBias()};
                    } else {
                        objValueOf = new Object[]{Integer.valueOf(this.caret.getMark()), Integer.valueOf(this.caret.getDot()), Boolean.valueOf(this.caret.isVisible())};
                    }
                    this.caret.setVisible(true);
                } else {
                    objValueOf = obj;
                }
                if (this.caret instanceof DefaultCaret) {
                    ((DefaultCaret) this.caret).setDot(dropLocation2.getIndex(), dropLocation2.getBias());
                } else {
                    this.caret.setDot(dropLocation2.getIndex());
                }
            }
        } else if (dropLocation2 == null) {
            if (obj != null) {
                this.caret.setVisible(((Boolean) obj).booleanValue());
            }
        } else if (this.dropLocation == null) {
            if (this.caret instanceof DefaultCaret) {
                zIsVisible = ((DefaultCaret) this.caret).isActive();
            } else {
                zIsVisible = this.caret.isVisible();
            }
            objValueOf = Boolean.valueOf(zIsVisible);
            this.caret.setVisible(false);
        } else {
            objValueOf = obj;
        }
        DropLocation dropLocation3 = this.dropLocation;
        this.dropLocation = dropLocation2;
        firePropertyChange("dropLocation", dropLocation3, this.dropLocation);
        return objValueOf;
    }

    public final DropLocation getDropLocation() {
        return this.dropLocation;
    }

    void updateInputMap(Keymap keymap, Keymap keymap2) {
        InputMap inputMap;
        ActionMap actionMap;
        InputMap inputMap2 = getInputMap(0);
        InputMap inputMap3 = inputMap2;
        while (inputMap2 != null && !(inputMap2 instanceof KeymapWrapper)) {
            inputMap3 = inputMap2;
            inputMap2 = inputMap2.getParent();
        }
        if (inputMap2 != null) {
            if (keymap2 == null) {
                if (inputMap3 != inputMap2) {
                    inputMap3.setParent(inputMap2.getParent());
                } else {
                    inputMap3.setParent(null);
                }
            } else {
                KeymapWrapper keymapWrapper = new KeymapWrapper(keymap2);
                inputMap3.setParent(keymapWrapper);
                if (inputMap3 != inputMap2) {
                    keymapWrapper.setParent(inputMap2.getParent());
                }
            }
        } else if (keymap2 != null && (inputMap = getInputMap(0)) != null) {
            KeymapWrapper keymapWrapper2 = new KeymapWrapper(keymap2);
            keymapWrapper2.setParent(inputMap.getParent());
            inputMap.setParent(keymapWrapper2);
        }
        ActionMap actionMap2 = getActionMap();
        ActionMap actionMap3 = actionMap2;
        while (actionMap2 != null && !(actionMap2 instanceof KeymapActionMap)) {
            actionMap3 = actionMap2;
            actionMap2 = actionMap2.getParent();
        }
        if (actionMap2 != null) {
            if (keymap2 == null) {
                if (actionMap3 != actionMap2) {
                    actionMap3.setParent(actionMap2.getParent());
                    return;
                } else {
                    actionMap3.setParent(null);
                    return;
                }
            }
            KeymapActionMap keymapActionMap = new KeymapActionMap(keymap2);
            actionMap3.setParent(keymapActionMap);
            if (actionMap3 != actionMap2) {
                keymapActionMap.setParent(actionMap2.getParent());
                return;
            }
            return;
        }
        if (keymap2 != null && (actionMap = getActionMap()) != null) {
            KeymapActionMap keymapActionMap2 = new KeymapActionMap(keymap2);
            keymapActionMap2.setParent(actionMap.getParent());
            actionMap.setParent(keymapActionMap2);
        }
    }

    public Keymap getKeymap() {
        return this.keymap;
    }

    public static Keymap addKeymap(String str, Keymap keymap) {
        DefaultKeymap defaultKeymap = new DefaultKeymap(str, keymap);
        if (str != null) {
            getKeymapTable().put(str, defaultKeymap);
        }
        return defaultKeymap;
    }

    public static Keymap removeKeymap(String str) {
        return getKeymapTable().remove(str);
    }

    public static Keymap getKeymap(String str) {
        return getKeymapTable().get(str);
    }

    private static HashMap<String, Keymap> getKeymapTable() {
        HashMap<String, Keymap> map;
        synchronized (KEYMAP_TABLE) {
            AppContext appContext = AppContext.getAppContext();
            HashMap<String, Keymap> map2 = (HashMap) appContext.get(KEYMAP_TABLE);
            if (map2 == null) {
                map2 = new HashMap<>(17);
                appContext.put(KEYMAP_TABLE, map2);
                addKeymap("default", null).setDefaultAction(new DefaultEditorKit.DefaultKeyTypedAction());
            }
            map = map2;
        }
        return map;
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$KeyBinding.class */
    public static class KeyBinding {
        public KeyStroke key;
        public String actionName;

        public KeyBinding(KeyStroke keyStroke, String str) {
            this.key = keyStroke;
            this.actionName = str;
        }
    }

    public static void loadKeymap(Keymap keymap, KeyBinding[] keyBindingArr, Action[] actionArr) {
        Hashtable hashtable = new Hashtable();
        for (Action action : actionArr) {
            String str = (String) action.getValue("Name");
            hashtable.put(str != null ? str : "", action);
        }
        for (KeyBinding keyBinding : keyBindingArr) {
            Action action2 = (Action) hashtable.get(keyBinding.actionName);
            if (action2 != null) {
                keymap.addActionForKeyStroke(keyBinding.key, action2);
            }
        }
    }

    public Color getCaretColor() {
        return this.caretColor;
    }

    public void setCaretColor(Color color) {
        Color color2 = this.caretColor;
        this.caretColor = color;
        firePropertyChange("caretColor", color2, this.caretColor);
    }

    public Color getSelectionColor() {
        return this.selectionColor;
    }

    public void setSelectionColor(Color color) {
        Color color2 = this.selectionColor;
        this.selectionColor = color;
        firePropertyChange("selectionColor", color2, this.selectionColor);
    }

    public Color getSelectedTextColor() {
        return this.selectedTextColor;
    }

    public void setSelectedTextColor(Color color) {
        Color color2 = this.selectedTextColor;
        this.selectedTextColor = color;
        firePropertyChange("selectedTextColor", color2, this.selectedTextColor);
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public void setDisabledTextColor(Color color) {
        Color color2 = this.disabledTextColor;
        this.disabledTextColor = color;
        firePropertyChange("disabledTextColor", color2, this.disabledTextColor);
    }

    public void replaceSelection(String str) {
        Document document = getDocument();
        if (document != null) {
            try {
                boolean zSaveComposedText = saveComposedText(this.caret.getDot());
                int iMin = Math.min(this.caret.getDot(), this.caret.getMark());
                int iMax = Math.max(this.caret.getDot(), this.caret.getMark());
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).replace(iMin, iMax - iMin, str, null);
                } else {
                    if (iMin != iMax) {
                        document.remove(iMin, iMax - iMin);
                    }
                    if (str != null && str.length() > 0) {
                        document.insertString(iMin, str, null);
                    }
                }
                if (zSaveComposedText) {
                    restoreComposedText();
                }
            } catch (BadLocationException e2) {
                UIManager.getLookAndFeel().provideErrorFeedback(this);
            }
        }
    }

    public String getText(int i2, int i3) throws BadLocationException {
        return getDocument().getText(i2, i3);
    }

    public Rectangle modelToView(int i2) throws BadLocationException {
        return getUI().modelToView(this, i2);
    }

    public int viewToModel(Point point) {
        return getUI().viewToModel(this, point);
    }

    public void cut() {
        if (isEditable() && isEnabled()) {
            invokeAction("cut", TransferHandler.getCutAction());
        }
    }

    public void copy() {
        invokeAction("copy", TransferHandler.getCopyAction());
    }

    public void paste() {
        if (isEditable() && isEnabled()) {
            invokeAction("paste", TransferHandler.getPasteAction());
        }
    }

    private void invokeAction(String str, Action action) {
        ActionMap actionMap = getActionMap();
        Action action2 = null;
        if (actionMap != null) {
            action2 = actionMap.get(str);
        }
        if (action2 == null) {
            installDefaultTransferHandlerIfNecessary();
            action2 = action;
        }
        action2.actionPerformed(new ActionEvent(this, 1001, (String) action2.getValue("Name"), EventQueue.getMostRecentEventTime(), getCurrentEventModifiers()));
    }

    private void installDefaultTransferHandlerIfNecessary() {
        if (getTransferHandler() == null) {
            if (defaultTransferHandler == null) {
                defaultTransferHandler = new DefaultTransferHandler();
            }
            setTransferHandler(defaultTransferHandler);
        }
    }

    public void moveCaretPosition(int i2) {
        Document document = getDocument();
        if (document != null) {
            if (i2 > document.getLength() || i2 < 0) {
                throw new IllegalArgumentException("bad position: " + i2);
            }
            this.caret.moveDot(i2);
        }
    }

    public void setFocusAccelerator(char c2) {
        char upperCase = Character.toUpperCase(c2);
        char c3 = this.focusAccelerator;
        this.focusAccelerator = upperCase;
        firePropertyChange(FOCUS_ACCELERATOR_KEY, c3, this.focusAccelerator);
        firePropertyChange("focusAccelerator", c3, this.focusAccelerator);
    }

    public char getFocusAccelerator() {
        return this.focusAccelerator;
    }

    public void read(Reader reader, Object obj) throws IOException {
        EditorKit editorKit = getUI().getEditorKit(this);
        Document documentCreateDefaultDocument = editorKit.createDefaultDocument();
        if (obj != null) {
            documentCreateDefaultDocument.putProperty(Document.StreamDescriptionProperty, obj);
        }
        try {
            editorKit.read(reader, documentCreateDefaultDocument, 0);
            setDocument(documentCreateDefaultDocument);
        } catch (BadLocationException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public void write(Writer writer) throws IOException {
        Document document = getDocument();
        try {
            getUI().getEditorKit(this).write(writer, document, 0, document.getLength());
        } catch (BadLocationException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        if (getFocusedComponent() == this) {
            AppContext.getAppContext().remove(FOCUSED_COMPONENT);
        }
    }

    public void setCaretPosition(int i2) {
        Document document = getDocument();
        if (document != null) {
            if (i2 > document.getLength() || i2 < 0) {
                throw new IllegalArgumentException("bad position: " + i2);
            }
            this.caret.setDot(i2);
        }
    }

    @Transient
    public int getCaretPosition() {
        return this.caret.getDot();
    }

    public void setText(String str) {
        try {
            Document document = getDocument();
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).replace(0, document.getLength(), str, null);
            } else {
                document.remove(0, document.getLength());
                document.insertString(0, str, null);
            }
        } catch (BadLocationException e2) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        }
    }

    public String getText() {
        String text;
        Document document = getDocument();
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException e2) {
            text = null;
        }
        return text;
    }

    public String getSelectedText() {
        String text = null;
        int iMin = Math.min(this.caret.getDot(), this.caret.getMark());
        int iMax = Math.max(this.caret.getDot(), this.caret.getMark());
        if (iMin != iMax) {
            try {
                text = getDocument().getText(iMin, iMax - iMin);
            } catch (BadLocationException e2) {
                throw new IllegalArgumentException(e2.getMessage());
            }
        }
        return text;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean z2) {
        if (z2 != this.editable) {
            boolean z3 = this.editable;
            this.editable = z2;
            enableInputMethods(this.editable);
            firePropertyChange(JTree.EDITABLE_PROPERTY, Boolean.valueOf(z3), Boolean.valueOf(this.editable));
            repaint();
        }
    }

    @Transient
    public int getSelectionStart() {
        return Math.min(this.caret.getDot(), this.caret.getMark());
    }

    public void setSelectionStart(int i2) {
        select(i2, getSelectionEnd());
    }

    @Transient
    public int getSelectionEnd() {
        return Math.max(this.caret.getDot(), this.caret.getMark());
    }

    public void setSelectionEnd(int i2) {
        select(getSelectionStart(), i2);
    }

    public void select(int i2, int i3) {
        int length = getDocument().getLength();
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > length) {
            i2 = length;
        }
        if (i3 > length) {
            i3 = length;
        }
        if (i3 < i2) {
            i3 = i2;
        }
        setCaretPosition(i2);
        moveCaretPosition(i3);
    }

    public void selectAll() {
        Document document = getDocument();
        if (document != null) {
            setCaretPosition(0);
            moveCaretPosition(document.getLength());
        }
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        TextUI ui;
        String toolTipText = super.getToolTipText(mouseEvent);
        if (toolTipText == null && (ui = getUI()) != null) {
            toolTipText = ui.getToolTipText(this, new Point(mouseEvent.getX(), mouseEvent.getY()));
        }
        return toolTipText;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        switch (i2) {
            case 0:
                return rectangle.width / 10;
            case 1:
                return rectangle.height / 10;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + i2);
        }
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        switch (i2) {
            case 0:
                return rectangle.width;
            case 1:
                return rectangle.height;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + i2);
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getWidth() > getPreferredSize().width;
    }

    public boolean getScrollableTracksViewportHeight() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return (unwrappedParent instanceof JViewport) && unwrappedParent.getHeight() > getPreferredSize().height;
    }

    public boolean print() throws PrinterException {
        return print(null, null, true, null, null, true);
    }

    public boolean print(MessageFormat messageFormat, MessageFormat messageFormat2) throws PrinterException {
        return print(messageFormat, messageFormat2, true, null, null, true);
    }

    public boolean print(MessageFormat messageFormat, MessageFormat messageFormat2, boolean z2, PrintService printService, PrintRequestAttributeSet printRequestAttributeSet, boolean z3) throws PrinterException {
        PrintingStatus printingStatusCreatePrintingStatus;
        Printable printableCreateNotificationPrintable;
        final PrinterJob printerJob = PrinterJob.getPrinterJob();
        boolean zIsHeadless = GraphicsEnvironment.isHeadless();
        final boolean zIsEventDispatchThread = SwingUtilities.isEventDispatchThread();
        Printable printable = getPrintable(messageFormat, messageFormat2);
        if (z3 && !zIsHeadless) {
            printingStatusCreatePrintingStatus = PrintingStatus.createPrintingStatus(this, printerJob);
            printableCreateNotificationPrintable = printingStatusCreatePrintingStatus.createNotificationPrintable(printable);
        } else {
            printingStatusCreatePrintingStatus = null;
            printableCreateNotificationPrintable = printable;
        }
        if (printService != null) {
            printerJob.setPrintService(printService);
        }
        printerJob.setPrintable(printableCreateNotificationPrintable);
        final PrintRequestAttributeSet hashPrintRequestAttributeSet = printRequestAttributeSet == null ? new HashPrintRequestAttributeSet() : printRequestAttributeSet;
        if (z2 && !zIsHeadless && !printerJob.printDialog(hashPrintRequestAttributeSet)) {
            return false;
        }
        final PrintingStatus printingStatus = printingStatusCreatePrintingStatus;
        final FutureTask futureTask = new FutureTask(new Callable<Object>() { // from class: javax.swing.text.JTextComponent.2
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                try {
                    printerJob.print(hashPrintRequestAttributeSet);
                } finally {
                    if (printingStatus != null) {
                        printingStatus.dispose();
                    }
                }
            }
        });
        Runnable runnable = new Runnable() { // from class: javax.swing.text.JTextComponent.3
            @Override // java.lang.Runnable
            public void run() {
                boolean zBooleanValue = false;
                if (zIsEventDispatchThread) {
                    if (JTextComponent.this.isEnabled()) {
                        zBooleanValue = true;
                        JTextComponent.this.setEnabled(false);
                    }
                } else {
                    try {
                        zBooleanValue = ((Boolean) SwingUtilities2.submit(new Callable<Boolean>() { // from class: javax.swing.text.JTextComponent.3.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public Boolean call() throws Exception {
                                boolean zIsEnabled = JTextComponent.this.isEnabled();
                                if (zIsEnabled) {
                                    JTextComponent.this.setEnabled(false);
                                }
                                return Boolean.valueOf(zIsEnabled);
                            }
                        }).get()).booleanValue();
                    } catch (InterruptedException e2) {
                        throw new RuntimeException(e2);
                    } catch (ExecutionException e3) {
                        Throwable cause = e3.getCause();
                        if (cause instanceof Error) {
                            throw ((Error) cause);
                        }
                        if (cause instanceof RuntimeException) {
                            throw ((RuntimeException) cause);
                        }
                        throw new AssertionError(cause);
                    }
                }
                JTextComponent.this.getDocument().render(futureTask);
                if (zBooleanValue) {
                    if (zIsEventDispatchThread) {
                        JTextComponent.this.setEnabled(true);
                        return;
                    }
                    try {
                        SwingUtilities2.submit(new Runnable() { // from class: javax.swing.text.JTextComponent.3.2
                            @Override // java.lang.Runnable
                            public void run() {
                                JTextComponent.this.setEnabled(true);
                            }
                        }, null).get();
                    } catch (InterruptedException e4) {
                        throw new RuntimeException(e4);
                    } catch (ExecutionException e5) {
                        Throwable cause2 = e5.getCause();
                        if (cause2 instanceof Error) {
                            throw ((Error) cause2);
                        }
                        if (cause2 instanceof RuntimeException) {
                            throw ((RuntimeException) cause2);
                        }
                        throw new AssertionError(cause2);
                    }
                }
            }
        };
        if (!z3 || zIsHeadless) {
            runnable.run();
        } else if (zIsEventDispatchThread) {
            new Thread(runnable).start();
            printingStatusCreatePrintingStatus.showModal(true);
        } else {
            printingStatusCreatePrintingStatus.showModal(false);
            runnable.run();
        }
        try {
            futureTask.get();
            return true;
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        } catch (ExecutionException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof PrinterAbortException) {
                if (printingStatusCreatePrintingStatus != null && printingStatusCreatePrintingStatus.isAborted()) {
                    return false;
                }
                throw ((PrinterAbortException) cause);
            }
            if (cause instanceof PrinterException) {
                throw ((PrinterException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new AssertionError(cause);
        }
    }

    public Printable getPrintable(MessageFormat messageFormat, MessageFormat messageFormat2) {
        return TextComponentPrintable.getPrintable(this, messageFormat, messageFormat2);
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTextComponent();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$AccessibleJTextComponent.class */
    public class AccessibleJTextComponent extends JComponent.AccessibleJComponent implements AccessibleText, CaretListener, DocumentListener, AccessibleAction, AccessibleEditableText, AccessibleExtendedText {
        int caretPos;
        Point oldLocationOnScreen;

        public AccessibleJTextComponent() {
            super();
            Document document = JTextComponent.this.getDocument();
            if (document != null) {
                document.addDocumentListener(this);
            }
            JTextComponent.this.addCaretListener(this);
            this.caretPos = getCaretPosition();
            try {
                this.oldLocationOnScreen = getLocationOnScreen();
            } catch (IllegalComponentStateException e2) {
            }
            JTextComponent.this.addComponentListener(new ComponentAdapter() { // from class: javax.swing.text.JTextComponent.AccessibleJTextComponent.1
                @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
                public void componentMoved(ComponentEvent componentEvent) {
                    try {
                        Point locationOnScreen = AccessibleJTextComponent.this.getLocationOnScreen();
                        AccessibleJTextComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, AccessibleJTextComponent.this.oldLocationOnScreen, locationOnScreen);
                        AccessibleJTextComponent.this.oldLocationOnScreen = locationOnScreen;
                    } catch (IllegalComponentStateException e3) {
                    }
                }
            });
        }

        @Override // javax.swing.event.CaretListener
        public void caretUpdate(CaretEvent caretEvent) {
            int dot = caretEvent.getDot();
            int mark = caretEvent.getMark();
            if (this.caretPos != dot) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_CARET_PROPERTY, new Integer(this.caretPos), new Integer(dot));
                this.caretPos = dot;
                try {
                    this.oldLocationOnScreen = getLocationOnScreen();
                } catch (IllegalComponentStateException e2) {
                }
            }
            if (mark != dot) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, null, getSelectedText());
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void insertUpdate(DocumentEvent documentEvent) {
            final Integer num = new Integer(documentEvent.getOffset());
            if (SwingUtilities.isEventDispatchThread()) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
            } else {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.JTextComponent.AccessibleJTextComponent.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AccessibleJTextComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
                    }
                });
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            final Integer num = new Integer(documentEvent.getOffset());
            if (SwingUtilities.isEventDispatchThread()) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
            } else {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.JTextComponent.AccessibleJTextComponent.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AccessibleJTextComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
                    }
                });
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
            final Integer num = new Integer(documentEvent.getOffset());
            if (SwingUtilities.isEventDispatchThread()) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
            } else {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.JTextComponent.AccessibleJTextComponent.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AccessibleJTextComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, num);
                    }
                });
            }
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JTextComponent.this.isEditable()) {
                accessibleStateSet.add(AccessibleState.EDITABLE);
            }
            return accessibleStateSet;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TEXT;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            return this;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            if (point == null) {
                return -1;
            }
            return JTextComponent.this.viewToModel(point);
        }

        Rectangle getRootEditorRect() {
            Rectangle bounds = JTextComponent.this.getBounds();
            if (bounds.width > 0 && bounds.height > 0) {
                bounds.f12373y = 0;
                bounds.f12372x = 0;
                Insets insets = JTextComponent.this.getInsets();
                bounds.f12372x += insets.left;
                bounds.f12373y += insets.top;
                bounds.width -= insets.left + insets.right;
                bounds.height -= insets.top + insets.bottom;
                return bounds;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            TextUI ui;
            if (i2 < 0 || i2 > JTextComponent.this.model.getLength() - 1 || (ui = JTextComponent.this.getUI()) == null) {
                return null;
            }
            Rectangle bounds = null;
            Rectangle rootEditorRect = getRootEditorRect();
            if (rootEditorRect != null) {
                if (JTextComponent.this.model instanceof AbstractDocument) {
                    ((AbstractDocument) JTextComponent.this.model).readLock();
                }
                try {
                    View rootView = ui.getRootView(JTextComponent.this);
                    if (rootView != null) {
                        rootView.setSize(rootEditorRect.width, rootEditorRect.height);
                        Shape shapeModelToView = rootView.modelToView(i2, Position.Bias.Forward, i2 + 1, Position.Bias.Backward, rootEditorRect);
                        bounds = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                    }
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                } catch (BadLocationException e2) {
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                } catch (Throwable th) {
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                    throw th;
                }
                return bounds;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            return JTextComponent.this.model.getLength();
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            return JTextComponent.this.getCaretPosition();
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            if (JTextComponent.this.model instanceof AbstractDocument) {
                ((AbstractDocument) JTextComponent.this.model).readLock();
            }
            try {
                Element defaultRootElement = JTextComponent.this.model.getDefaultRootElement();
                while (!defaultRootElement.isLeaf()) {
                    defaultRootElement = defaultRootElement.getElement(defaultRootElement.getElementIndex(i2));
                }
                return defaultRootElement.getAttributes();
            } finally {
                if (JTextComponent.this.model instanceof AbstractDocument) {
                    ((AbstractDocument) JTextComponent.this.model).readUnlock();
                }
            }
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            return JTextComponent.this.getSelectionStart();
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            return JTextComponent.this.getSelectionEnd();
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            return JTextComponent.this.getSelectedText();
        }

        /* loaded from: rt.jar:javax/swing/text/JTextComponent$AccessibleJTextComponent$IndexedSegment.class */
        private class IndexedSegment extends Segment {
            public int modelOffset;

            private IndexedSegment() {
            }
        }

        public String getAtIndex(int i2, int i3) {
            return getAtIndex(i2, i3, 0);
        }

        public String getAfterIndex(int i2, int i3) {
            return getAtIndex(i2, i3, 1);
        }

        public String getBeforeIndex(int i2, int i3) {
            return getAtIndex(i2, i3, -1);
        }

        private String getAtIndex(int i2, int i3, int i4) {
            if (JTextComponent.this.model instanceof AbstractDocument) {
                ((AbstractDocument) JTextComponent.this.model).readLock();
            }
            if (i3 >= 0) {
                try {
                    if (i3 < JTextComponent.this.model.getLength()) {
                        switch (i2) {
                            case 1:
                                if (i3 + i4 < JTextComponent.this.model.getLength() && i3 + i4 >= 0) {
                                    String text = JTextComponent.this.model.getText(i3 + i4, 1);
                                    if (JTextComponent.this.model instanceof AbstractDocument) {
                                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
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
                                        segmentAt = (i5 < 0 || i5 > JTextComponent.this.model.getLength()) ? null : getSegmentAt(i2, i5);
                                    }
                                    if (segmentAt != null) {
                                        String str = new String(segmentAt.array, segmentAt.offset, segmentAt.count);
                                        if (JTextComponent.this.model instanceof AbstractDocument) {
                                            ((AbstractDocument) JTextComponent.this.model).readUnlock();
                                        }
                                        return str;
                                    }
                                }
                                break;
                        }
                        if (!(JTextComponent.this.model instanceof AbstractDocument)) {
                            return null;
                        }
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                        return null;
                    }
                } catch (BadLocationException e2) {
                    if (!(JTextComponent.this.model instanceof AbstractDocument)) {
                        return null;
                    }
                    ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    return null;
                } catch (Throwable th) {
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                    throw th;
                }
            }
            if (JTextComponent.this.model instanceof AbstractDocument) {
                ((AbstractDocument) JTextComponent.this.model).readUnlock();
            }
            return null;
        }

        private Element getParagraphElement(int i2) {
            Element element;
            if (!(JTextComponent.this.model instanceof PlainDocument)) {
                if (!(JTextComponent.this.model instanceof StyledDocument)) {
                    Element defaultRootElement = JTextComponent.this.model.getDefaultRootElement();
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
                return ((StyledDocument) JTextComponent.this.model).getParagraphElement(i2);
            }
            return ((PlainDocument) JTextComponent.this.model).getParagraphElement(i2);
        }

        private IndexedSegment getParagraphElementText(int i2) throws BadLocationException {
            Element paragraphElement = getParagraphElement(i2);
            if (paragraphElement != null) {
                IndexedSegment indexedSegment = new IndexedSegment();
                try {
                    JTextComponent.this.model.getText(paragraphElement.getStartOffset(), paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), indexedSegment);
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

        @Override // javax.accessibility.AccessibleContext
        public AccessibleEditableText getAccessibleEditableText() {
            return this;
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void setTextContents(String str) {
            JTextComponent.this.setText(str);
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void insertTextAtIndex(int i2, String str) {
            Document document = JTextComponent.this.getDocument();
            if (document != null) {
                if (str != null) {
                    try {
                        if (str.length() > 0) {
                            boolean zSaveComposedText = JTextComponent.this.saveComposedText(i2);
                            document.insertString(i2, str, null);
                            if (zSaveComposedText) {
                                JTextComponent.this.restoreComposedText();
                            }
                        }
                    } catch (BadLocationException e2) {
                        UIManager.getLookAndFeel().provideErrorFeedback(JTextComponent.this);
                    }
                }
            }
        }

        public String getTextRange(int i2, int i3) {
            String text = null;
            int iMin = Math.min(i2, i3);
            int iMax = Math.max(i2, i3);
            if (iMin != iMax) {
                try {
                    text = JTextComponent.this.getDocument().getText(iMin, iMax - iMin);
                } catch (BadLocationException e2) {
                    throw new IllegalArgumentException(e2.getMessage());
                }
            }
            return text;
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void delete(int i2, int i3) {
            if (JTextComponent.this.isEditable() && isEnabled()) {
                try {
                    int iMin = Math.min(i2, i3);
                    int iMax = Math.max(i2, i3);
                    if (iMin != iMax) {
                        JTextComponent.this.getDocument().remove(iMin, iMax - iMin);
                    }
                    return;
                } catch (BadLocationException e2) {
                    return;
                }
            }
            UIManager.getLookAndFeel().provideErrorFeedback(JTextComponent.this);
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void cut(int i2, int i3) {
            selectText(i2, i3);
            JTextComponent.this.cut();
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void paste(int i2) {
            JTextComponent.this.setCaretPosition(i2);
            JTextComponent.this.paste();
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void replaceText(int i2, int i3, String str) {
            selectText(i2, i3);
            JTextComponent.this.replaceSelection(str);
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void selectText(int i2, int i3) {
            JTextComponent.this.select(i2, i3);
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void setAttributes(int i2, int i3, AttributeSet attributeSet) {
            Document document = JTextComponent.this.getDocument();
            if (document != null && (document instanceof StyledDocument)) {
                ((StyledDocument) document).setCharacterAttributes(i2, i3 - i2, attributeSet, true);
            }
        }

        private AccessibleTextSequence getSequenceAtIndex(int i2, int i3, int i4) {
            int i5;
            if (i3 >= 0 && i3 < JTextComponent.this.model.getLength() && i4 >= -1 && i4 <= 1) {
                switch (i2) {
                    case 1:
                        if (JTextComponent.this.model instanceof AbstractDocument) {
                            ((AbstractDocument) JTextComponent.this.model).readLock();
                        }
                        AccessibleTextSequence accessibleTextSequence = null;
                        try {
                            if (i3 + i4 < JTextComponent.this.model.getLength() && i3 + i4 >= 0) {
                                accessibleTextSequence = new AccessibleTextSequence(i3 + i4, i3 + i4 + 1, JTextComponent.this.model.getText(i3 + i4, 1));
                            }
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (BadLocationException e2) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (Throwable th) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            throw th;
                        }
                        return accessibleTextSequence;
                    case 2:
                    case 3:
                        if (JTextComponent.this.model instanceof AbstractDocument) {
                            ((AbstractDocument) JTextComponent.this.model).readLock();
                        }
                        AccessibleTextSequence accessibleTextSequence2 = null;
                        try {
                            IndexedSegment segmentAt = getSegmentAt(i2, i3);
                            if (segmentAt != null) {
                                if (i4 != 0) {
                                    if (i4 < 0) {
                                        i5 = segmentAt.modelOffset - 1;
                                    } else {
                                        i5 = segmentAt.modelOffset + segmentAt.count;
                                    }
                                    if (i5 >= 0 && i5 <= JTextComponent.this.model.getLength()) {
                                        segmentAt = getSegmentAt(i2, i5);
                                    } else {
                                        segmentAt = null;
                                    }
                                }
                                if (segmentAt != null && segmentAt.offset + segmentAt.count <= JTextComponent.this.model.getLength()) {
                                    accessibleTextSequence2 = new AccessibleTextSequence(segmentAt.offset, segmentAt.offset + segmentAt.count, new String(segmentAt.array, segmentAt.offset, segmentAt.count));
                                }
                            }
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (BadLocationException e3) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (Throwable th2) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            throw th2;
                        }
                        return accessibleTextSequence2;
                    case 4:
                        AccessibleTextSequence accessibleTextSequence3 = null;
                        if (JTextComponent.this.model instanceof AbstractDocument) {
                            ((AbstractDocument) JTextComponent.this.model).readLock();
                        }
                        try {
                            int rowStart = Utilities.getRowStart(JTextComponent.this, i3);
                            int rowEnd = Utilities.getRowEnd(JTextComponent.this, i3);
                            if (rowStart >= 0 && rowEnd >= rowStart) {
                                if (i4 == 0) {
                                    accessibleTextSequence3 = new AccessibleTextSequence(rowStart, rowEnd, JTextComponent.this.model.getText(rowStart, (rowEnd - rowStart) + 1));
                                } else if (i4 == -1 && rowStart > 0) {
                                    int rowEnd2 = Utilities.getRowEnd(JTextComponent.this, rowStart - 1);
                                    int rowStart2 = Utilities.getRowStart(JTextComponent.this, rowStart - 1);
                                    if (rowStart2 >= 0 && rowEnd2 >= rowStart2) {
                                        accessibleTextSequence3 = new AccessibleTextSequence(rowStart2, rowEnd2, JTextComponent.this.model.getText(rowStart2, (rowEnd2 - rowStart2) + 1));
                                    }
                                } else if (i4 == 1 && rowEnd < JTextComponent.this.model.getLength()) {
                                    int rowStart3 = Utilities.getRowStart(JTextComponent.this, rowEnd + 1);
                                    int rowEnd3 = Utilities.getRowEnd(JTextComponent.this, rowEnd + 1);
                                    if (rowStart3 >= 0 && rowEnd3 >= rowStart3) {
                                        accessibleTextSequence3 = new AccessibleTextSequence(rowStart3, rowEnd3, JTextComponent.this.model.getText(rowStart3, (rowEnd3 - rowStart3) + 1));
                                    }
                                }
                            }
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (BadLocationException e4) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                        } catch (Throwable th3) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            throw th3;
                        }
                        return accessibleTextSequence3;
                    case 5:
                        if (JTextComponent.this.model instanceof AbstractDocument) {
                            ((AbstractDocument) JTextComponent.this.model).readLock();
                        }
                        int runEdge = Integer.MIN_VALUE;
                        int runEdge2 = Integer.MIN_VALUE;
                        int i6 = i3;
                        try {
                            switch (i4) {
                                case -1:
                                    runEdge = getRunEdge(i3, i4);
                                    i6 = runEdge - 1;
                                    break;
                                case 0:
                                    break;
                                case 1:
                                    runEdge2 = getRunEdge(i3, i4);
                                    i6 = runEdge2;
                                    break;
                                default:
                                    throw new AssertionError(i4);
                            }
                            int runEdge3 = runEdge2 != Integer.MIN_VALUE ? runEdge2 : getRunEdge(i6, -1);
                            int runEdge4 = runEdge != Integer.MIN_VALUE ? runEdge : getRunEdge(i6, 1);
                            String text = JTextComponent.this.model.getText(runEdge3, runEdge4 - runEdge3);
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            return new AccessibleTextSequence(runEdge3, runEdge4, text);
                        } catch (BadLocationException e5) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            return null;
                        } catch (Throwable th4) {
                            if (JTextComponent.this.model instanceof AbstractDocument) {
                                ((AbstractDocument) JTextComponent.this.model).readUnlock();
                            }
                            throw th4;
                        }
                    default:
                        return null;
                }
            }
            return null;
        }

        private int getRunEdge(int i2, int i3) throws BadLocationException {
            Element element;
            if (i2 < 0 || i2 >= JTextComponent.this.model.getLength()) {
                throw new BadLocationException("Location out of bounds", i2);
            }
            int elementIndex = -1;
            Element defaultRootElement = JTextComponent.this.model.getDefaultRootElement();
            while (true) {
                element = defaultRootElement;
                if (element.isLeaf()) {
                    break;
                }
                elementIndex = element.getElementIndex(i2);
                defaultRootElement = element.getElement(elementIndex);
            }
            if (elementIndex == -1) {
                throw new AssertionError(i2);
            }
            AttributeSet attributes = element.getAttributes();
            Element parentElement = element.getParentElement();
            switch (i3) {
                case -1:
                case 1:
                    int i4 = elementIndex;
                    int elementCount = parentElement.getElementCount();
                    while (i4 + i3 > 0 && i4 + i3 < elementCount && parentElement.getElement(i4 + i3).getAttributes().isEqual(attributes)) {
                        i4 += i3;
                    }
                    Element element2 = parentElement.getElement(i4);
                    switch (i3) {
                        case -1:
                            return element2.getStartOffset();
                        case 1:
                            return element2.getEndOffset();
                        default:
                            return Integer.MIN_VALUE;
                    }
                default:
                    throw new AssertionError(i3);
            }
        }

        public AccessibleTextSequence getTextSequenceAt(int i2, int i3) {
            return getSequenceAtIndex(i2, i3, 0);
        }

        public AccessibleTextSequence getTextSequenceAfter(int i2, int i3) {
            return getSequenceAtIndex(i2, i3, 1);
        }

        public AccessibleTextSequence getTextSequenceBefore(int i2, int i3) {
            return getSequenceAtIndex(i2, i3, -1);
        }

        @Override // javax.accessibility.AccessibleExtendedText
        public Rectangle getTextBounds(int i2, int i3) {
            TextUI ui;
            if (i2 < 0 || i2 > JTextComponent.this.model.getLength() - 1 || i3 < 0 || i3 > JTextComponent.this.model.getLength() - 1 || i2 > i3 || (ui = JTextComponent.this.getUI()) == null) {
                return null;
            }
            Rectangle bounds = null;
            Rectangle rootEditorRect = getRootEditorRect();
            if (rootEditorRect != null) {
                if (JTextComponent.this.model instanceof AbstractDocument) {
                    ((AbstractDocument) JTextComponent.this.model).readLock();
                }
                try {
                    View rootView = ui.getRootView(JTextComponent.this);
                    if (rootView != null) {
                        Shape shapeModelToView = rootView.modelToView(i2, Position.Bias.Forward, i3, Position.Bias.Backward, rootEditorRect);
                        bounds = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                    }
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                } catch (BadLocationException e2) {
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                } catch (Throwable th) {
                    if (JTextComponent.this.model instanceof AbstractDocument) {
                        ((AbstractDocument) JTextComponent.this.model).readUnlock();
                    }
                    throw th;
                }
                return bounds;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return JTextComponent.this.getActions().length;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            Action[] actions = JTextComponent.this.getActions();
            if (i2 < 0 || i2 >= actions.length) {
                return null;
            }
            return (String) actions[i2].getValue("Name");
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            Action[] actions = JTextComponent.this.getActions();
            if (i2 < 0 || i2 >= actions.length) {
                return false;
            }
            actions[i2].actionPerformed(new ActionEvent(JTextComponent.this, 1001, null, EventQueue.getMostRecentEventTime(), JTextComponent.this.getCurrentEventModifiers()));
            return true;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.caretEvent = new MutableCaretEvent(this);
        addMouseListener(this.caretEvent);
        addFocusListener(this.caretEvent);
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$DropLocation.class */
    public static final class DropLocation extends TransferHandler.DropLocation {
        private final int index;
        private final Position.Bias bias;

        private DropLocation(Point point, int i2, Position.Bias bias) {
            super(point);
            this.index = i2;
            this.bias = bias;
        }

        public int getIndex() {
            return this.index;
        }

        public Position.Bias getBias() {
            return this.bias;
        }

        @Override // javax.swing.TransferHandler.DropLocation
        public String toString() {
            return getClass().getName() + "[dropPoint=" + ((Object) getDropPoint()) + ",index=" + this.index + ",bias=" + ((Object) this.bias) + "]";
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str = this.editable ? "true" : "false";
        return super.paramString() + ",caretColor=" + (this.caretColor != null ? this.caretColor.toString() : "") + ",disabledTextColor=" + (this.disabledTextColor != null ? this.disabledTextColor.toString() : "") + ",editable=" + str + ",margin=" + (this.margin != null ? this.margin.toString() : "") + ",selectedTextColor=" + (this.selectedTextColor != null ? this.selectedTextColor.toString() : "") + ",selectionColor=" + (this.selectionColor != null ? this.selectionColor.toString() : "");
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$DefaultTransferHandler.class */
    static class DefaultTransferHandler extends TransferHandler implements UIResource {
        DefaultTransferHandler() {
        }

        @Override // javax.swing.TransferHandler
        public void exportToClipboard(JComponent jComponent, Clipboard clipboard, int i2) throws IllegalStateException {
            JTextComponent jTextComponent;
            int selectionStart;
            int selectionEnd;
            if ((jComponent instanceof JTextComponent) && (selectionStart = (jTextComponent = (JTextComponent) jComponent).getSelectionStart()) != (selectionEnd = jTextComponent.getSelectionEnd())) {
                try {
                    Document document = jTextComponent.getDocument();
                    clipboard.setContents(new StringSelection(document.getText(selectionStart, selectionEnd - selectionStart)), null);
                    if (i2 == 2) {
                        document.remove(selectionStart, selectionEnd - selectionStart);
                    }
                } catch (BadLocationException e2) {
                }
            }
        }

        @Override // javax.swing.TransferHandler
        public boolean importData(JComponent jComponent, Transferable transferable) {
            DataFlavor flavor;
            if ((jComponent instanceof JTextComponent) && (flavor = getFlavor(transferable.getTransferDataFlavors())) != null) {
                InputContext inputContext = jComponent.getInputContext();
                if (inputContext != null) {
                    inputContext.endComposition();
                }
                try {
                    ((JTextComponent) jComponent).replaceSelection((String) transferable.getTransferData(flavor));
                    return true;
                } catch (UnsupportedFlavorException e2) {
                    return false;
                } catch (IOException e3) {
                    return false;
                }
            }
            return false;
        }

        @Override // javax.swing.TransferHandler
        public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavorArr) {
            JTextComponent jTextComponent = (JTextComponent) jComponent;
            return jTextComponent.isEditable() && jTextComponent.isEnabled() && getFlavor(dataFlavorArr) != null;
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            return 0;
        }

        private DataFlavor getFlavor(DataFlavor[] dataFlavorArr) {
            if (dataFlavorArr != null) {
                for (DataFlavor dataFlavor : dataFlavorArr) {
                    if (dataFlavor.equals(DataFlavor.stringFlavor)) {
                        return dataFlavor;
                    }
                }
                return null;
            }
            return null;
        }
    }

    static final JTextComponent getFocusedComponent() {
        return (JTextComponent) AppContext.getAppContext().get(FOCUSED_COMPONENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentEventModifiers() {
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        return modifiers;
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$DefaultKeymap.class */
    static class DefaultKeymap implements Keymap {
        String nm;
        Keymap parent;
        Hashtable<KeyStroke, Action> bindings = new Hashtable<>();
        Action defaultAction;

        DefaultKeymap(String str, Keymap keymap) {
            this.nm = str;
            this.parent = keymap;
        }

        @Override // javax.swing.text.Keymap
        public Action getDefaultAction() {
            if (this.defaultAction != null) {
                return this.defaultAction;
            }
            if (this.parent != null) {
                return this.parent.getDefaultAction();
            }
            return null;
        }

        @Override // javax.swing.text.Keymap
        public void setDefaultAction(Action action) {
            this.defaultAction = action;
        }

        @Override // javax.swing.text.Keymap
        public String getName() {
            return this.nm;
        }

        @Override // javax.swing.text.Keymap
        public Action getAction(KeyStroke keyStroke) {
            Action action = this.bindings.get(keyStroke);
            if (action == null && this.parent != null) {
                action = this.parent.getAction(keyStroke);
            }
            return action;
        }

        @Override // javax.swing.text.Keymap
        public KeyStroke[] getBoundKeyStrokes() {
            KeyStroke[] keyStrokeArr = new KeyStroke[this.bindings.size()];
            int i2 = 0;
            Enumeration<KeyStroke> enumerationKeys = this.bindings.keys();
            while (enumerationKeys.hasMoreElements()) {
                int i3 = i2;
                i2++;
                keyStrokeArr[i3] = enumerationKeys.nextElement2();
            }
            return keyStrokeArr;
        }

        @Override // javax.swing.text.Keymap
        public Action[] getBoundActions() {
            Action[] actionArr = new Action[this.bindings.size()];
            int i2 = 0;
            Enumeration<Action> enumerationElements = this.bindings.elements();
            while (enumerationElements.hasMoreElements()) {
                int i3 = i2;
                i2++;
                actionArr[i3] = enumerationElements.nextElement2();
            }
            return actionArr;
        }

        @Override // javax.swing.text.Keymap
        public KeyStroke[] getKeyStrokesForAction(Action action) {
            KeyStroke[] keyStrokesForAction;
            if (action == null) {
                return null;
            }
            KeyStroke[] keyStrokeArr = null;
            Vector vector = null;
            Enumeration<KeyStroke> enumerationKeys = this.bindings.keys();
            while (enumerationKeys.hasMoreElements()) {
                KeyStroke keyStrokeNextElement = enumerationKeys.nextElement2();
                if (this.bindings.get(keyStrokeNextElement) == action) {
                    if (vector == null) {
                        vector = new Vector();
                    }
                    vector.addElement(keyStrokeNextElement);
                }
            }
            if (this.parent != null && (keyStrokesForAction = this.parent.getKeyStrokesForAction(action)) != null) {
                int i2 = 0;
                for (int length = keyStrokesForAction.length - 1; length >= 0; length--) {
                    if (isLocallyDefined(keyStrokesForAction[length])) {
                        keyStrokesForAction[length] = null;
                        i2++;
                    }
                }
                if (i2 > 0 && i2 < keyStrokesForAction.length) {
                    if (vector == null) {
                        vector = new Vector();
                    }
                    for (int length2 = keyStrokesForAction.length - 1; length2 >= 0; length2--) {
                        if (keyStrokesForAction[length2] != null) {
                            vector.addElement(keyStrokesForAction[length2]);
                        }
                    }
                } else if (i2 == 0) {
                    if (vector == null) {
                        keyStrokeArr = keyStrokesForAction;
                    } else {
                        keyStrokeArr = new KeyStroke[vector.size() + keyStrokesForAction.length];
                        vector.copyInto(keyStrokeArr);
                        System.arraycopy(keyStrokesForAction, 0, keyStrokeArr, vector.size(), keyStrokesForAction.length);
                        vector = null;
                    }
                }
            }
            if (vector != null) {
                keyStrokeArr = new KeyStroke[vector.size()];
                vector.copyInto(keyStrokeArr);
            }
            return keyStrokeArr;
        }

        @Override // javax.swing.text.Keymap
        public boolean isLocallyDefined(KeyStroke keyStroke) {
            return this.bindings.containsKey(keyStroke);
        }

        @Override // javax.swing.text.Keymap
        public void addActionForKeyStroke(KeyStroke keyStroke, Action action) {
            this.bindings.put(keyStroke, action);
        }

        @Override // javax.swing.text.Keymap
        public void removeKeyStrokeBinding(KeyStroke keyStroke) {
            this.bindings.remove(keyStroke);
        }

        @Override // javax.swing.text.Keymap
        public void removeBindings() {
            this.bindings.clear();
        }

        @Override // javax.swing.text.Keymap
        public Keymap getResolveParent() {
            return this.parent;
        }

        @Override // javax.swing.text.Keymap
        public void setResolveParent(Keymap keymap) {
            this.parent = keymap;
        }

        public String toString() {
            return "Keymap[" + this.nm + "]" + ((Object) this.bindings);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$KeymapWrapper.class */
    static class KeymapWrapper extends InputMap {
        static final Object DefaultActionKey = new Object();
        private Keymap keymap;

        KeymapWrapper(Keymap keymap) {
            this.keymap = keymap;
        }

        @Override // javax.swing.InputMap
        public KeyStroke[] keys() {
            KeyStroke[] keyStrokeArrKeys = super.keys();
            KeyStroke[] boundKeyStrokes = this.keymap.getBoundKeyStrokes();
            int length = keyStrokeArrKeys == null ? 0 : keyStrokeArrKeys.length;
            int length2 = boundKeyStrokes == null ? 0 : boundKeyStrokes.length;
            if (length == 0) {
                return boundKeyStrokes;
            }
            if (length2 == 0) {
                return keyStrokeArrKeys;
            }
            KeyStroke[] keyStrokeArr = new KeyStroke[length + length2];
            System.arraycopy(keyStrokeArrKeys, 0, keyStrokeArr, 0, length);
            System.arraycopy(boundKeyStrokes, 0, keyStrokeArr, length, length2);
            return keyStrokeArr;
        }

        @Override // javax.swing.InputMap
        public int size() {
            KeyStroke[] boundKeyStrokes = this.keymap.getBoundKeyStrokes();
            return super.size() + (boundKeyStrokes == null ? 0 : boundKeyStrokes.length);
        }

        @Override // javax.swing.InputMap
        public Object get(KeyStroke keyStroke) {
            Object action = this.keymap.getAction(keyStroke);
            if (action == null) {
                action = super.get(keyStroke);
                if (action == null && keyStroke.getKeyChar() != 65535 && this.keymap.getDefaultAction() != null) {
                    action = DefaultActionKey;
                }
            }
            return action;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$KeymapActionMap.class */
    static class KeymapActionMap extends ActionMap {
        private Keymap keymap;

        KeymapActionMap(Keymap keymap) {
            this.keymap = keymap;
        }

        @Override // javax.swing.ActionMap
        public Object[] keys() {
            Object[] objArrKeys = super.keys();
            Action[] boundActions = this.keymap.getBoundActions();
            int length = objArrKeys == null ? 0 : objArrKeys.length;
            int length2 = boundActions == null ? 0 : boundActions.length;
            boolean z2 = this.keymap.getDefaultAction() != null;
            if (z2) {
                length2++;
            }
            if (length == 0) {
                if (z2) {
                    Object[] objArr = new Object[length2];
                    if (length2 > 1) {
                        System.arraycopy(boundActions, 0, objArr, 0, length2 - 1);
                    }
                    objArr[length2 - 1] = KeymapWrapper.DefaultActionKey;
                    return objArr;
                }
                return boundActions;
            }
            if (length2 == 0) {
                return objArrKeys;
            }
            Object[] objArr2 = new Object[length + length2];
            System.arraycopy(objArrKeys, 0, objArr2, 0, length);
            if (z2) {
                if (length2 > 1) {
                    System.arraycopy(boundActions, 0, objArr2, length, length2 - 1);
                }
                objArr2[(length + length2) - 1] = KeymapWrapper.DefaultActionKey;
            } else {
                System.arraycopy(boundActions, 0, objArr2, length, length2);
            }
            return objArr2;
        }

        @Override // javax.swing.ActionMap
        public int size() {
            Action[] boundActions = this.keymap.getBoundActions();
            int length = boundActions == null ? 0 : boundActions.length;
            if (this.keymap.getDefaultAction() != null) {
                length++;
            }
            return super.size() + length;
        }

        @Override // javax.swing.ActionMap
        public Action get(Object obj) {
            Action defaultAction = super.get(obj);
            if (defaultAction == null) {
                if (obj == KeymapWrapper.DefaultActionKey) {
                    defaultAction = this.keymap.getDefaultAction();
                } else if (obj instanceof Action) {
                    defaultAction = (Action) obj;
                }
            }
            return defaultAction;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$MutableCaretEvent.class */
    static class MutableCaretEvent extends CaretEvent implements ChangeListener, FocusListener, MouseListener {
        private boolean dragActive;
        private int dot;
        private int mark;

        MutableCaretEvent(JTextComponent jTextComponent) {
            super(jTextComponent);
        }

        final void fire() {
            JTextComponent jTextComponent = (JTextComponent) getSource();
            if (jTextComponent != null) {
                Caret caret = jTextComponent.getCaret();
                this.dot = caret.getDot();
                this.mark = caret.getMark();
                jTextComponent.fireCaretUpdate(this);
            }
        }

        @Override // java.util.EventObject
        public final String toString() {
            return "dot=" + this.dot + ",mark=" + this.mark;
        }

        @Override // javax.swing.event.CaretEvent
        public final int getDot() {
            return this.dot;
        }

        @Override // javax.swing.event.CaretEvent
        public final int getMark() {
            return this.mark;
        }

        @Override // javax.swing.event.ChangeListener
        public final void stateChanged(ChangeEvent changeEvent) {
            if (!this.dragActive) {
                fire();
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            AppContext.getAppContext().put(JTextComponent.FOCUSED_COMPONENT, focusEvent.getSource());
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
        }

        @Override // java.awt.event.MouseListener
        public final void mousePressed(MouseEvent mouseEvent) {
            this.dragActive = true;
        }

        @Override // java.awt.event.MouseListener
        public final void mouseReleased(MouseEvent mouseEvent) {
            this.dragActive = false;
            fire();
        }

        @Override // java.awt.event.MouseListener
        public final void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public final void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public final void mouseExited(MouseEvent mouseEvent) {
        }
    }

    @Override // java.awt.Component
    protected void processInputMethodEvent(InputMethodEvent inputMethodEvent) {
        super.processInputMethodEvent(inputMethodEvent);
        if (inputMethodEvent.isConsumed() || !isEditable()) {
            return;
        }
        switch (inputMethodEvent.getID()) {
            case 1100:
                replaceInputMethodText(inputMethodEvent);
            case 1101:
                setInputMethodCaretPosition(inputMethodEvent);
                break;
        }
        inputMethodEvent.consume();
    }

    @Override // java.awt.Component
    public InputMethodRequests getInputMethodRequests() {
        if (this.inputMethodRequestsHandler == null) {
            this.inputMethodRequestsHandler = new InputMethodRequestsHandler();
            Document document = getDocument();
            if (document != null) {
                document.addDocumentListener((DocumentListener) this.inputMethodRequestsHandler);
            }
        }
        return this.inputMethodRequestsHandler;
    }

    @Override // java.awt.Component
    public void addInputMethodListener(InputMethodListener inputMethodListener) {
        super.addInputMethodListener(inputMethodListener);
        if (inputMethodListener != null) {
            this.needToSendKeyTypedEvent = false;
            this.checkedInputOverride = true;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$InputMethodRequestsHandler.class */
    class InputMethodRequestsHandler implements InputMethodRequests, DocumentListener {
        InputMethodRequestsHandler() {
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributeArr) {
            Document document = JTextComponent.this.getDocument();
            if (document != null && JTextComponent.this.latestCommittedTextStart != null && !JTextComponent.this.latestCommittedTextStart.equals(JTextComponent.this.latestCommittedTextEnd)) {
                try {
                    int offset = JTextComponent.this.latestCommittedTextStart.getOffset();
                    int offset2 = JTextComponent.this.latestCommittedTextEnd.getOffset();
                    String text = document.getText(offset, offset2 - offset);
                    document.remove(offset, offset2 - offset);
                    return new AttributedString(text).getIterator();
                } catch (BadLocationException e2) {
                    return null;
                }
            }
            return null;
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator getCommittedText(int i2, int i3, AttributedCharacterIterator.Attribute[] attributeArr) {
            String text;
            int offset = 0;
            int offset2 = 0;
            if (JTextComponent.this.composedTextExists()) {
                offset = JTextComponent.this.composedTextStart.getOffset();
                offset2 = JTextComponent.this.composedTextEnd.getOffset();
            }
            try {
                if (i2 < offset) {
                    if (i3 <= offset) {
                        text = JTextComponent.this.getText(i2, i3 - i2);
                    } else {
                        int i4 = offset - i2;
                        text = JTextComponent.this.getText(i2, i4) + JTextComponent.this.getText(offset2, (i3 - i2) - i4);
                    }
                } else {
                    text = JTextComponent.this.getText(i2 + (offset2 - offset), i3 - i2);
                }
                return new AttributedString(text).getIterator();
            } catch (BadLocationException e2) {
                throw new IllegalArgumentException("Invalid range");
            }
        }

        @Override // java.awt.im.InputMethodRequests
        public int getCommittedTextLength() {
            Document document = JTextComponent.this.getDocument();
            int length = 0;
            if (document != null) {
                length = document.getLength();
                if (JTextComponent.this.composedTextContent != null) {
                    if (JTextComponent.this.composedTextEnd == null || JTextComponent.this.composedTextStart == null) {
                        length -= JTextComponent.this.composedTextContent.length();
                    } else {
                        length -= JTextComponent.this.composedTextEnd.getOffset() - JTextComponent.this.composedTextStart.getOffset();
                    }
                }
            }
            return length;
        }

        @Override // java.awt.im.InputMethodRequests
        public int getInsertPositionOffset() {
            int offset = 0;
            int offset2 = 0;
            if (JTextComponent.this.composedTextExists()) {
                offset = JTextComponent.this.composedTextStart.getOffset();
                offset2 = JTextComponent.this.composedTextEnd.getOffset();
            }
            int caretPosition = JTextComponent.this.getCaretPosition();
            if (caretPosition < offset) {
                return caretPosition;
            }
            if (caretPosition < offset2) {
                return offset;
            }
            return caretPosition - (offset2 - offset);
        }

        @Override // java.awt.im.InputMethodRequests
        public TextHitInfo getLocationOffset(int i2, int i3) {
            if (JTextComponent.this.composedTextAttribute == null) {
                return null;
            }
            Point locationOnScreen = JTextComponent.this.getLocationOnScreen();
            locationOnScreen.f12370x = i2 - locationOnScreen.f12370x;
            locationOnScreen.f12371y = i3 - locationOnScreen.f12371y;
            int iViewToModel = JTextComponent.this.viewToModel(locationOnScreen);
            if (iViewToModel >= JTextComponent.this.composedTextStart.getOffset() && iViewToModel <= JTextComponent.this.composedTextEnd.getOffset()) {
                return TextHitInfo.leading(iViewToModel - JTextComponent.this.composedTextStart.getOffset());
            }
            return null;
        }

        @Override // java.awt.im.InputMethodRequests
        public Rectangle getTextLocation(TextHitInfo textHitInfo) {
            Rectangle rectangle;
            try {
                rectangle = JTextComponent.this.modelToView(JTextComponent.this.getCaretPosition());
                if (rectangle != null) {
                    Point locationOnScreen = JTextComponent.this.getLocationOnScreen();
                    rectangle.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                }
            } catch (BadLocationException e2) {
                rectangle = null;
            }
            if (rectangle == null) {
                rectangle = new Rectangle();
            }
            return rectangle;
        }

        @Override // java.awt.im.InputMethodRequests
        public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributeArr) {
            String selectedText = JTextComponent.this.getSelectedText();
            if (selectedText != null) {
                return new AttributedString(selectedText).getIterator();
            }
            return null;
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
            JTextComponent.this.latestCommittedTextStart = JTextComponent.this.latestCommittedTextEnd = null;
        }

        @Override // javax.swing.event.DocumentListener
        public void insertUpdate(DocumentEvent documentEvent) {
            JTextComponent.this.latestCommittedTextStart = JTextComponent.this.latestCommittedTextEnd = null;
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            JTextComponent.this.latestCommittedTextStart = JTextComponent.this.latestCommittedTextEnd = null;
        }
    }

    private void replaceInputMethodText(InputMethodEvent inputMethodEvent) {
        int committedCharacterCount = inputMethodEvent.getCommittedCharacterCount();
        AttributedCharacterIterator text = inputMethodEvent.getText();
        Document document = getDocument();
        if (composedTextExists()) {
            try {
                document.remove(this.composedTextStart.getOffset(), this.composedTextEnd.getOffset() - this.composedTextStart.getOffset());
            } catch (BadLocationException e2) {
            }
            this.composedTextEnd = null;
            this.composedTextStart = null;
            this.composedTextAttribute = null;
            this.composedTextContent = null;
        }
        if (text != null) {
            text.first();
            int dot = 0;
            int dot2 = 0;
            if (committedCharacterCount > 0) {
                dot = this.caret.getDot();
                if (shouldSynthensizeKeyEvents()) {
                    char cCurrent = text.current();
                    while (committedCharacterCount > 0) {
                        processKeyEvent(new KeyEvent(this, 400, EventQueue.getMostRecentEventTime(), 0, 0, cCurrent));
                        cCurrent = text.next();
                        committedCharacterCount--;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    char cCurrent2 = text.current();
                    while (committedCharacterCount > 0) {
                        sb.append(cCurrent2);
                        cCurrent2 = text.next();
                        committedCharacterCount--;
                    }
                    mapCommittedTextToAction(sb.toString());
                }
                dot2 = this.caret.getDot();
            }
            int index = text.getIndex();
            if (index < text.getEndIndex()) {
                createComposedTextAttribute(index, text);
                try {
                    replaceSelection(null);
                    document.insertString(this.caret.getDot(), this.composedTextContent, this.composedTextAttribute);
                    this.composedTextStart = document.createPosition(this.caret.getDot() - this.composedTextContent.length());
                    this.composedTextEnd = document.createPosition(this.caret.getDot());
                } catch (BadLocationException e3) {
                    this.composedTextEnd = null;
                    this.composedTextStart = null;
                    this.composedTextAttribute = null;
                    this.composedTextContent = null;
                }
            }
            if (dot != dot2) {
                try {
                    this.latestCommittedTextStart = document.createPosition(dot);
                    this.latestCommittedTextEnd = document.createPosition(dot2);
                    return;
                } catch (BadLocationException e4) {
                    this.latestCommittedTextEnd = null;
                    this.latestCommittedTextStart = null;
                    return;
                }
            }
            this.latestCommittedTextEnd = null;
            this.latestCommittedTextStart = null;
        }
    }

    private void createComposedTextAttribute(int i2, AttributedCharacterIterator attributedCharacterIterator) {
        getDocument();
        StringBuilder sb = new StringBuilder();
        char index = attributedCharacterIterator.setIndex(i2);
        while (true) {
            char c2 = index;
            if (c2 != 65535) {
                sb.append(c2);
                index = attributedCharacterIterator.next();
            } else {
                this.composedTextContent = sb.toString();
                this.composedTextAttribute = new SimpleAttributeSet();
                this.composedTextAttribute.addAttribute(StyleConstants.ComposedTextAttribute, new AttributedString(attributedCharacterIterator, i2, attributedCharacterIterator.getEndIndex()));
                return;
            }
        }
    }

    protected boolean saveComposedText(int i2) {
        if (composedTextExists()) {
            int offset = this.composedTextStart.getOffset();
            int offset2 = this.composedTextEnd.getOffset() - this.composedTextStart.getOffset();
            if (i2 >= offset && i2 <= offset + offset2) {
                try {
                    getDocument().remove(offset, offset2);
                    return true;
                } catch (BadLocationException e2) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    protected void restoreComposedText() {
        Document document = getDocument();
        try {
            document.insertString(this.caret.getDot(), this.composedTextContent, this.composedTextAttribute);
            this.composedTextStart = document.createPosition(this.caret.getDot() - this.composedTextContent.length());
            this.composedTextEnd = document.createPosition(this.caret.getDot());
        } catch (BadLocationException e2) {
        }
    }

    private void mapCommittedTextToAction(String str) {
        Keymap keymap = getKeymap();
        if (keymap != null) {
            Action defaultAction = null;
            if (str.length() == 1) {
                defaultAction = keymap.getAction(KeyStroke.getKeyStroke(str.charAt(0)));
            }
            if (defaultAction == null) {
                defaultAction = keymap.getDefaultAction();
            }
            if (defaultAction != null) {
                defaultAction.actionPerformed(new ActionEvent(this, 1001, str, EventQueue.getMostRecentEventTime(), getCurrentEventModifiers()));
            }
        }
    }

    private void setInputMethodCaretPosition(InputMethodEvent inputMethodEvent) {
        if (!composedTextExists()) {
            if (this.caret instanceof ComposedTextCaret) {
                int dot = this.caret.getDot();
                exchangeCaret(this.caret, this.originalCaret);
                this.caret.setDot(dot);
                return;
            }
            return;
        }
        int offset = this.composedTextStart.getOffset();
        if (!(this.caret instanceof ComposedTextCaret)) {
            if (this.composedTextCaret == null) {
                this.composedTextCaret = new ComposedTextCaret();
            }
            this.originalCaret = this.caret;
            exchangeCaret(this.originalCaret, this.composedTextCaret);
        }
        TextHitInfo caret = inputMethodEvent.getCaret();
        if (caret != null) {
            int insertionIndex = caret.getInsertionIndex();
            offset += insertionIndex;
            if (insertionIndex == 0) {
                try {
                    Rectangle rectangleModelToView = modelToView(offset);
                    rectangleModelToView.f12372x += Math.min(modelToView(this.composedTextEnd.getOffset()).f12372x - rectangleModelToView.f12372x, getBounds().width);
                    scrollRectToVisible(rectangleModelToView);
                } catch (BadLocationException e2) {
                }
            }
        }
        this.caret.setDot(offset);
    }

    private void exchangeCaret(Caret caret, Caret caret2) {
        int blinkRate = caret.getBlinkRate();
        setCaret(caret2);
        this.caret.setBlinkRate(blinkRate);
        this.caret.setVisible(hasFocus());
    }

    private boolean shouldSynthensizeKeyEvents() {
        if (!this.checkedInputOverride) {
            this.needToSendKeyTypedEvent = !METHOD_OVERRIDDEN.get(getClass()).booleanValue();
            this.checkedInputOverride = true;
        }
        return this.needToSendKeyTypedEvent;
    }

    boolean composedTextExists() {
        return this.composedTextStart != null;
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$ComposedTextCaret.class */
    class ComposedTextCaret extends DefaultCaret implements Serializable {

        /* renamed from: bg, reason: collision with root package name */
        Color f12838bg;

        ComposedTextCaret() {
        }

        @Override // javax.swing.text.DefaultCaret, javax.swing.text.Caret
        public void install(JTextComponent jTextComponent) {
            super.install(jTextComponent);
            Document document = jTextComponent.getDocument();
            if (document instanceof StyledDocument) {
                StyledDocument styledDocument = (StyledDocument) document;
                this.f12838bg = styledDocument.getBackground(styledDocument.getCharacterElement(jTextComponent.composedTextStart.getOffset()).getAttributes());
            }
            if (this.f12838bg == null) {
                this.f12838bg = jTextComponent.getBackground();
            }
        }

        @Override // javax.swing.text.DefaultCaret, javax.swing.text.Caret
        public void paint(Graphics graphics) {
            if (isVisible()) {
                try {
                    Rectangle rectangleModelToView = this.component.modelToView(getDot());
                    graphics.setXORMode(this.f12838bg);
                    graphics.drawLine(rectangleModelToView.f12372x, rectangleModelToView.f12373y, rectangleModelToView.f12372x, (rectangleModelToView.f12373y + rectangleModelToView.height) - 1);
                    graphics.setPaintMode();
                } catch (BadLocationException e2) {
                }
            }
        }

        @Override // javax.swing.text.DefaultCaret
        protected void positionCaret(MouseEvent mouseEvent) {
            JTextComponent jTextComponent = this.component;
            int iViewToModel = jTextComponent.viewToModel(new Point(mouseEvent.getX(), mouseEvent.getY()));
            if (iViewToModel < jTextComponent.composedTextStart.getOffset() || iViewToModel > JTextComponent.this.composedTextEnd.getOffset()) {
                try {
                    Position positionCreatePosition = jTextComponent.getDocument().createPosition(iViewToModel);
                    jTextComponent.getInputContext().endComposition();
                    EventQueue.invokeLater(JTextComponent.this.new DoSetCaretPosition(jTextComponent, positionCreatePosition));
                    return;
                } catch (BadLocationException e2) {
                    System.err.println(e2);
                    return;
                }
            }
            super.positionCaret(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/JTextComponent$DoSetCaretPosition.class */
    private class DoSetCaretPosition implements Runnable {
        JTextComponent host;
        Position newPos;

        DoSetCaretPosition(JTextComponent jTextComponent, Position position) {
            this.host = jTextComponent;
            this.newPos = position;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.host.setCaretPosition(this.newPos.getOffset());
        }
    }
}
