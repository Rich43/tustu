package javax.swing.text;

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Highlighter;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import jdk.jfr.Enabled;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/DefaultCaret.class */
public class DefaultCaret extends Rectangle implements Caret, FocusListener, MouseListener, MouseMotionListener {
    public static final int UPDATE_WHEN_ON_EDT = 0;
    public static final int NEVER_UPDATE = 1;
    public static final int ALWAYS_UPDATE = 2;
    JTextComponent component;
    boolean visible;
    boolean active;
    int dot;
    int mark;
    Object selectionTag;
    boolean selectionVisible;
    Timer flasher;
    Point magicCaretPosition;
    transient Position.Bias dotBias;
    transient Position.Bias markBias;
    boolean dotLTR;
    boolean markLTR;
    private transient NavigationFilter.FilterBypass filterBypass;
    private static transient Action selectWord = null;
    private static transient Action selectLine = null;
    private boolean ownsSelection;
    private boolean forceCaretPositionChange;
    private transient boolean shouldHandleRelease;
    protected EventListenerList listenerList = new EventListenerList();
    protected transient ChangeEvent changeEvent = null;
    int updatePolicy = 0;
    transient Handler handler = new Handler();
    private transient int[] flagXPoints = new int[3];
    private transient int[] flagYPoints = new int[3];
    private transient MouseEvent selectedWordEvent = null;
    private int caretWidth = -1;
    private float aspectRatio = -1.0f;

    public void setUpdatePolicy(int i2) {
        this.updatePolicy = i2;
    }

    public int getUpdatePolicy() {
        return this.updatePolicy;
    }

    protected final JTextComponent getComponent() {
        return this.component;
    }

    protected final synchronized void repaint() {
        if (this.component != null) {
            this.component.repaint(this.f12372x, this.f12373y, this.width, this.height);
        }
    }

    protected synchronized void damage(Rectangle rectangle) {
        if (rectangle != null) {
            int caretWidth = getCaretWidth(rectangle.height);
            this.f12372x = (rectangle.f12372x - 4) - (caretWidth >> 1);
            this.f12373y = rectangle.f12373y;
            this.width = 9 + caretWidth;
            this.height = rectangle.height;
            repaint();
        }
    }

    protected void adjustVisibility(Rectangle rectangle) {
        if (this.component == null) {
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            this.component.scrollRectToVisible(rectangle);
        } else {
            SwingUtilities.invokeLater(new SafeScroller(rectangle));
        }
    }

    protected Highlighter.HighlightPainter getSelectionPainter() {
        return DefaultHighlighter.DefaultPainter;
    }

    protected void positionCaret(MouseEvent mouseEvent) {
        Position.Bias[] biasArr = new Position.Bias[1];
        int iViewToModel = this.component.getUI().viewToModel(this.component, new Point(mouseEvent.getX(), mouseEvent.getY()), biasArr);
        if (biasArr[0] == null) {
            biasArr[0] = Position.Bias.Forward;
        }
        if (iViewToModel >= 0) {
            setDot(iViewToModel, biasArr[0]);
        }
    }

    protected void moveCaret(MouseEvent mouseEvent) {
        Position.Bias[] biasArr = new Position.Bias[1];
        int iViewToModel = this.component.getUI().viewToModel(this.component, new Point(mouseEvent.getX(), mouseEvent.getY()), biasArr);
        if (biasArr[0] == null) {
            biasArr[0] = Position.Bias.Forward;
        }
        if (iViewToModel >= 0) {
            moveDot(iViewToModel, biasArr[0]);
        }
    }

    public void focusGained(FocusEvent focusEvent) {
        if (this.component.isEnabled()) {
            if (this.component.isEditable()) {
                setVisible(true);
            }
            setSelectionVisible(true);
        }
    }

    public void focusLost(FocusEvent focusEvent) {
        setVisible(false);
        setSelectionVisible(this.ownsSelection || focusEvent.isTemporary());
    }

    private void selectWord(MouseEvent mouseEvent) {
        if (this.selectedWordEvent != null && this.selectedWordEvent.getX() == mouseEvent.getX() && this.selectedWordEvent.getY() == mouseEvent.getY()) {
            return;
        }
        Action action = null;
        ActionMap actionMap = getComponent().getActionMap();
        if (actionMap != null) {
            action = actionMap.get(DefaultEditorKit.selectWordAction);
        }
        if (action == null) {
            if (selectWord == null) {
                selectWord = new DefaultEditorKit.SelectWordAction();
            }
            action = selectWord;
        }
        action.actionPerformed(new ActionEvent(getComponent(), 1001, null, mouseEvent.getWhen(), mouseEvent.getModifiers()));
        this.selectedWordEvent = mouseEvent;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        JComponent jComponent;
        if (getComponent() == null) {
            return;
        }
        int adjustedClickCount = SwingUtilities2.getAdjustedClickCount(getComponent(), mouseEvent);
        if (!mouseEvent.isConsumed()) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                if (adjustedClickCount == 1) {
                    this.selectedWordEvent = null;
                    return;
                }
                if (adjustedClickCount == 2 && SwingUtilities2.canEventAccessSystemClipboard(mouseEvent)) {
                    selectWord(mouseEvent);
                    this.selectedWordEvent = null;
                    return;
                }
                if (adjustedClickCount == 3 && SwingUtilities2.canEventAccessSystemClipboard(mouseEvent)) {
                    Action action = null;
                    ActionMap actionMap = getComponent().getActionMap();
                    if (actionMap != null) {
                        action = actionMap.get(DefaultEditorKit.selectLineAction);
                    }
                    if (action == null) {
                        if (selectLine == null) {
                            selectLine = new DefaultEditorKit.SelectLineAction();
                        }
                        action = selectLine;
                    }
                    action.actionPerformed(new ActionEvent(getComponent(), 1001, null, mouseEvent.getWhen(), mouseEvent.getModifiers()));
                    return;
                }
                return;
            }
            if (SwingUtilities.isMiddleMouseButton(mouseEvent) && adjustedClickCount == 1 && this.component.isEditable() && this.component.isEnabled() && SwingUtilities2.canEventAccessSystemClipboard(mouseEvent) && (jComponent = (JTextComponent) mouseEvent.getSource()) != null) {
                try {
                    Clipboard systemSelection = jComponent.getToolkit().getSystemSelection();
                    if (systemSelection != null) {
                        adjustCaret(mouseEvent);
                        TransferHandler transferHandler = jComponent.getTransferHandler();
                        if (transferHandler != null) {
                            Transferable contents = null;
                            try {
                                contents = systemSelection.getContents(null);
                            } catch (IllegalStateException e2) {
                                UIManager.getLookAndFeel().provideErrorFeedback(jComponent);
                            }
                            if (contents != null) {
                                transferHandler.importData(jComponent, contents);
                            }
                        }
                        adjustFocus(true);
                    }
                } catch (HeadlessException e3) {
                }
            }
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        int adjustedClickCount = SwingUtilities2.getAdjustedClickCount(getComponent(), mouseEvent);
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            if (mouseEvent.isConsumed()) {
                this.shouldHandleRelease = true;
                return;
            }
            this.shouldHandleRelease = false;
            adjustCaretAndFocus(mouseEvent);
            if (adjustedClickCount == 2 && SwingUtilities2.canEventAccessSystemClipboard(mouseEvent)) {
                selectWord(mouseEvent);
            }
        }
    }

    void adjustCaretAndFocus(MouseEvent mouseEvent) {
        adjustCaret(mouseEvent);
        adjustFocus(false);
    }

    private void adjustCaret(MouseEvent mouseEvent) {
        if ((mouseEvent.getModifiers() & 1) != 0 && getDot() != -1) {
            moveCaret(mouseEvent);
        } else if (!mouseEvent.isPopupTrigger()) {
            positionCaret(mouseEvent);
        }
    }

    private void adjustFocus(boolean z2) {
        if (this.component != null && this.component.isEnabled() && this.component.isRequestFocusEnabled()) {
            if (z2) {
                this.component.requestFocusInWindow();
            } else {
                this.component.requestFocus();
            }
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (!mouseEvent.isConsumed() && this.shouldHandleRelease && SwingUtilities.isLeftMouseButton(mouseEvent)) {
            adjustCaretAndFocus(mouseEvent);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (!mouseEvent.isConsumed() && SwingUtilities.isLeftMouseButton(mouseEvent)) {
            moveCaret(mouseEvent);
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    public void paint(Graphics graphics) {
        Element bidiRootElement;
        if (isVisible()) {
            try {
                Rectangle rectangleModelToView = this.component.getUI().modelToView(this.component, this.dot, this.dotBias);
                if (rectangleModelToView != null) {
                    if (rectangleModelToView.width == 0 && rectangleModelToView.height == 0) {
                        return;
                    }
                    if (this.width > 0 && this.height > 0 && !_contains(rectangleModelToView.f12372x, rectangleModelToView.f12373y, rectangleModelToView.width, rectangleModelToView.height)) {
                        Rectangle clipBounds = graphics.getClipBounds();
                        if (clipBounds != null && !clipBounds.contains((Rectangle) this)) {
                            repaint();
                        }
                        damage(rectangleModelToView);
                    }
                    graphics.setColor(this.component.getCaretColor());
                    int caretWidth = getCaretWidth(rectangleModelToView.height);
                    rectangleModelToView.f12372x -= caretWidth >> 1;
                    graphics.fillRect(rectangleModelToView.f12372x, rectangleModelToView.f12373y, caretWidth, rectangleModelToView.height);
                    Document document = this.component.getDocument();
                    if ((document instanceof AbstractDocument) && (bidiRootElement = ((AbstractDocument) document).getBidiRootElement()) != null && bidiRootElement.getElementCount() > 1) {
                        this.flagXPoints[0] = rectangleModelToView.f12372x + (this.dotLTR ? caretWidth : 0);
                        this.flagYPoints[0] = rectangleModelToView.f12373y;
                        this.flagXPoints[1] = this.flagXPoints[0];
                        this.flagYPoints[1] = this.flagYPoints[0] + 4;
                        this.flagXPoints[2] = this.flagXPoints[0] + (this.dotLTR ? 4 : -4);
                        this.flagYPoints[2] = this.flagYPoints[0];
                        graphics.fillPolygon(this.flagXPoints, this.flagYPoints, 3);
                    }
                }
            } catch (BadLocationException e2) {
            }
        }
    }

    @Override // javax.swing.text.Caret
    public void install(JTextComponent jTextComponent) {
        this.component = jTextComponent;
        Document document = jTextComponent.getDocument();
        this.mark = 0;
        this.dot = 0;
        this.markLTR = true;
        this.dotLTR = true;
        Position.Bias bias = Position.Bias.Forward;
        this.markBias = bias;
        this.dotBias = bias;
        if (document != null) {
            document.addDocumentListener(this.handler);
        }
        jTextComponent.addPropertyChangeListener(this.handler);
        jTextComponent.addFocusListener(this);
        jTextComponent.addMouseListener(this);
        jTextComponent.addMouseMotionListener(this);
        if (this.component.hasFocus()) {
            focusGained(null);
        }
        Number number = (Number) jTextComponent.getClientProperty("caretAspectRatio");
        if (number != null) {
            this.aspectRatio = number.floatValue();
        } else {
            this.aspectRatio = -1.0f;
        }
        Integer num = (Integer) jTextComponent.getClientProperty("caretWidth");
        if (num != null) {
            this.caretWidth = num.intValue();
        } else {
            this.caretWidth = -1;
        }
    }

    @Override // javax.swing.text.Caret
    public void deinstall(JTextComponent jTextComponent) {
        jTextComponent.removeMouseListener(this);
        jTextComponent.removeMouseMotionListener(this);
        jTextComponent.removeFocusListener(this);
        jTextComponent.removePropertyChangeListener(this.handler);
        Document document = jTextComponent.getDocument();
        if (document != null) {
            document.removeDocumentListener(this.handler);
        }
        synchronized (this) {
            this.component = null;
        }
        if (this.flasher != null) {
            this.flasher.stop();
        }
    }

    @Override // javax.swing.text.Caret
    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    @Override // javax.swing.text.Caret
    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    @Override // javax.swing.text.Caret
    public void setSelectionVisible(boolean z2) {
        if (z2 != this.selectionVisible) {
            this.selectionVisible = z2;
            if (this.selectionVisible) {
                Highlighter highlighter = this.component.getHighlighter();
                if (this.dot != this.mark && highlighter != null && this.selectionTag == null) {
                    try {
                        this.selectionTag = highlighter.addHighlight(Math.min(this.dot, this.mark), Math.max(this.dot, this.mark), getSelectionPainter());
                        return;
                    } catch (BadLocationException e2) {
                        this.selectionTag = null;
                        return;
                    }
                }
                return;
            }
            if (this.selectionTag != null) {
                this.component.getHighlighter().removeHighlight(this.selectionTag);
                this.selectionTag = null;
            }
        }
    }

    @Override // javax.swing.text.Caret
    public boolean isSelectionVisible() {
        return this.selectionVisible;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override // javax.swing.text.Caret
    public boolean isVisible() {
        return this.visible;
    }

    @Override // javax.swing.text.Caret
    public void setVisible(boolean z2) {
        this.active = z2;
        if (this.component != null) {
            TextUI ui = this.component.getUI();
            if (this.visible != z2) {
                this.visible = z2;
                try {
                    damage(ui.modelToView(this.component, this.dot, this.dotBias));
                } catch (BadLocationException e2) {
                }
            }
        }
        if (this.flasher != null) {
            if (this.visible) {
                this.flasher.start();
            } else {
                this.flasher.stop();
            }
        }
    }

    @Override // javax.swing.text.Caret
    public void setBlinkRate(int i2) {
        if (i2 != 0) {
            if (this.flasher == null) {
                this.flasher = new Timer(i2, this.handler);
            }
            this.flasher.setDelay(i2);
        } else if (this.flasher != null) {
            this.flasher.stop();
            this.flasher.removeActionListener(this.handler);
            this.flasher = null;
        }
    }

    @Override // javax.swing.text.Caret
    public int getBlinkRate() {
        if (this.flasher == null) {
            return 0;
        }
        return this.flasher.getDelay();
    }

    @Override // javax.swing.text.Caret
    public int getDot() {
        return this.dot;
    }

    @Override // javax.swing.text.Caret
    public int getMark() {
        return this.mark;
    }

    @Override // javax.swing.text.Caret
    public void setDot(int i2) {
        setDot(i2, Position.Bias.Forward);
    }

    @Override // javax.swing.text.Caret
    public void moveDot(int i2) {
        moveDot(i2, Position.Bias.Forward);
    }

    public void moveDot(int i2, Position.Bias bias) {
        if (bias == null) {
            throw new IllegalArgumentException("null bias");
        }
        if (!this.component.isEnabled()) {
            setDot(i2, bias);
            return;
        }
        if (i2 != this.dot) {
            NavigationFilter navigationFilter = this.component.getNavigationFilter();
            if (navigationFilter != null) {
                navigationFilter.moveDot(getFilterBypass(), i2, bias);
            } else {
                handleMoveDot(i2, bias);
            }
        }
    }

    void handleMoveDot(int i2, Position.Bias bias) {
        Highlighter highlighter;
        changeCaretPosition(i2, bias);
        if (this.selectionVisible && (highlighter = this.component.getHighlighter()) != null) {
            int iMin = Math.min(i2, this.mark);
            int iMax = Math.max(i2, this.mark);
            if (iMin == iMax) {
                if (this.selectionTag != null) {
                    highlighter.removeHighlight(this.selectionTag);
                    this.selectionTag = null;
                    return;
                }
                return;
            }
            try {
                if (this.selectionTag != null) {
                    highlighter.changeHighlight(this.selectionTag, iMin, iMax);
                } else {
                    this.selectionTag = highlighter.addHighlight(iMin, iMax, getSelectionPainter());
                }
            } catch (BadLocationException e2) {
                throw new StateInvariantError("Bad caret position");
            }
        }
    }

    public void setDot(int i2, Position.Bias bias) {
        if (bias == null) {
            throw new IllegalArgumentException("null bias");
        }
        NavigationFilter navigationFilter = this.component.getNavigationFilter();
        if (navigationFilter != null) {
            navigationFilter.setDot(getFilterBypass(), i2, bias);
        } else {
            handleSetDot(i2, bias);
        }
    }

    void handleSetDot(int i2, Position.Bias bias) {
        Document document = this.component.getDocument();
        if (document != null) {
            i2 = Math.min(i2, document.getLength());
        }
        int iMax = Math.max(i2, 0);
        if (iMax == 0) {
            bias = Position.Bias.Forward;
        }
        this.mark = iMax;
        if (this.dot != iMax || this.dotBias != bias || this.selectionTag != null || this.forceCaretPositionChange) {
            changeCaretPosition(iMax, bias);
        }
        this.markBias = this.dotBias;
        this.markLTR = this.dotLTR;
        Highlighter highlighter = this.component.getHighlighter();
        if (highlighter != null && this.selectionTag != null) {
            highlighter.removeHighlight(this.selectionTag);
            this.selectionTag = null;
        }
    }

    public Position.Bias getDotBias() {
        return this.dotBias;
    }

    public Position.Bias getMarkBias() {
        return this.markBias;
    }

    boolean isDotLeftToRight() {
        return this.dotLTR;
    }

    boolean isMarkLeftToRight() {
        return this.markLTR;
    }

    boolean isPositionLTR(int i2, Position.Bias bias) {
        Document document = this.component.getDocument();
        if (bias == Position.Bias.Backward) {
            i2--;
            if (i2 < 0) {
                i2 = 0;
            }
        }
        return AbstractDocument.isLeftToRight(document, i2, i2);
    }

    Position.Bias guessBiasForOffset(int i2, Position.Bias bias, boolean z2) {
        if (z2 != isPositionLTR(i2, bias)) {
            bias = Position.Bias.Backward;
        } else if (bias != Position.Bias.Backward && z2 != isPositionLTR(i2, Position.Bias.Backward)) {
            bias = Position.Bias.Backward;
        }
        if (bias == Position.Bias.Backward && i2 > 0) {
            try {
                Segment segment = new Segment();
                this.component.getDocument().getText(i2 - 1, 1, segment);
                if (segment.count > 0 && segment.array[segment.offset] == '\n') {
                    bias = Position.Bias.Forward;
                }
            } catch (BadLocationException e2) {
            }
        }
        return bias;
    }

    void changeCaretPosition(int i2, Position.Bias bias) {
        repaint();
        if (this.flasher != null && this.flasher.isRunning()) {
            this.visible = true;
            this.flasher.restart();
        }
        this.dot = i2;
        this.dotBias = bias;
        this.dotLTR = isPositionLTR(i2, bias);
        fireStateChanged();
        updateSystemSelection();
        setMagicCaretPosition(null);
        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.DefaultCaret.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultCaret.this.repaintNewCaret();
            }
        });
    }

    void repaintNewCaret() {
        Rectangle rectangleModelToView;
        if (this.component != null) {
            TextUI ui = this.component.getUI();
            Document document = this.component.getDocument();
            if (ui != null && document != null) {
                try {
                    rectangleModelToView = ui.modelToView(this.component, this.dot, this.dotBias);
                } catch (BadLocationException e2) {
                    rectangleModelToView = null;
                }
                if (rectangleModelToView != null) {
                    adjustVisibility(rectangleModelToView);
                    if (getMagicCaretPosition() == null) {
                        setMagicCaretPosition(new Point(rectangleModelToView.f12372x, rectangleModelToView.f12373y));
                    }
                }
                damage(rectangleModelToView);
            }
        }
    }

    private void updateSystemSelection() {
        Clipboard systemSelection;
        String selectedText;
        if (SwingUtilities2.canCurrentEventAccessSystemClipboard() && this.dot != this.mark && this.component != null && this.component.hasFocus() && (systemSelection = getSystemSelection()) != null) {
            if ((this.component instanceof JPasswordField) && this.component.getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE) {
                StringBuilder sb = null;
                char echoChar = ((JPasswordField) this.component).getEchoChar();
                int iMin = Math.min(getDot(), getMark());
                int iMax = Math.max(getDot(), getMark());
                for (int i2 = iMin; i2 < iMax; i2++) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append(echoChar);
                }
                selectedText = sb != null ? sb.toString() : null;
            } else {
                selectedText = this.component.getSelectedText();
            }
            try {
                systemSelection.setContents(new StringSelection(selectedText), getClipboardOwner());
                this.ownsSelection = true;
            } catch (IllegalStateException e2) {
            }
        }
    }

    private Clipboard getSystemSelection() {
        try {
            return this.component.getToolkit().getSystemSelection();
        } catch (HeadlessException | SecurityException e2) {
            return null;
        }
    }

    private ClipboardOwner getClipboardOwner() {
        return this.handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureValidPosition() {
        int length = this.component.getDocument().getLength();
        if (this.dot > length || this.mark > length) {
            handleSetDot(length, Position.Bias.Forward);
        }
    }

    @Override // javax.swing.text.Caret
    public void setMagicCaretPosition(Point point) {
        this.magicCaretPosition = point;
    }

    @Override // javax.swing.text.Caret
    public Point getMagicCaretPosition() {
        return this.magicCaretPosition;
    }

    @Override // java.awt.Rectangle, java.awt.geom.Rectangle2D
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override // java.awt.Rectangle
    public String toString() {
        return ("Dot=(" + this.dot + ", " + ((Object) this.dotBias) + ")") + " Mark=(" + this.mark + ", " + ((Object) this.markBias) + ")";
    }

    private NavigationFilter.FilterBypass getFilterBypass() {
        if (this.filterBypass == null) {
            this.filterBypass = new DefaultFilterBypass();
        }
        return this.filterBypass;
    }

    private boolean _contains(int i2, int i3, int i4, int i5) {
        int i6 = this.width;
        int i7 = this.height;
        if ((i6 | i7 | i4 | i5) < 0) {
            return false;
        }
        int i8 = this.f12372x;
        int i9 = this.f12373y;
        if (i2 < i8 || i3 < i9) {
            return false;
        }
        if (i4 > 0) {
            int i10 = i6 + i8;
            int i11 = i4 + i2;
            if (i11 <= i2) {
                if (i10 >= i8 || i11 > i10) {
                    return false;
                }
            } else if (i10 >= i8 && i11 > i10) {
                return false;
            }
        } else if (i8 + i6 < i2) {
            return false;
        }
        if (i5 > 0) {
            int i12 = i7 + i9;
            int i13 = i5 + i3;
            return i13 <= i3 ? i12 < i9 && i13 <= i12 : i12 < i9 || i13 <= i12;
        }
        if (i9 + i7 < i3) {
            return false;
        }
        return true;
    }

    int getCaretWidth(int i2) {
        if (this.aspectRatio > -1.0f) {
            return ((int) (this.aspectRatio * i2)) + 1;
        }
        if (this.caretWidth > -1) {
            return this.caretWidth;
        }
        Object obj = UIManager.get("Caret.width");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return 1;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.handler = new Handler();
        if (!objectInputStream.readBoolean()) {
            this.dotBias = Position.Bias.Forward;
        } else {
            this.dotBias = Position.Bias.Backward;
        }
        if (!objectInputStream.readBoolean()) {
            this.markBias = Position.Bias.Forward;
        } else {
            this.markBias = Position.Bias.Backward;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeBoolean(this.dotBias == Position.Bias.Backward);
        objectOutputStream.writeBoolean(this.markBias == Position.Bias.Backward);
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultCaret$SafeScroller.class */
    class SafeScroller implements Runnable {

        /* renamed from: r, reason: collision with root package name */
        Rectangle f12833r;

        SafeScroller(Rectangle rectangle) {
            this.f12833r = rectangle;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DefaultCaret.this.component != null) {
                DefaultCaret.this.component.scrollRectToVisible(this.f12833r);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultCaret$Handler.class */
    class Handler implements PropertyChangeListener, DocumentListener, ActionListener, ClipboardOwner {
        Handler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if ((DefaultCaret.this.width == 0 || DefaultCaret.this.height == 0) && DefaultCaret.this.component != null) {
                try {
                    Rectangle rectangleModelToView = DefaultCaret.this.component.getUI().modelToView(DefaultCaret.this.component, DefaultCaret.this.dot, DefaultCaret.this.dotBias);
                    if (rectangleModelToView != null && rectangleModelToView.width != 0 && rectangleModelToView.height != 0) {
                        DefaultCaret.this.damage(rectangleModelToView);
                    }
                } catch (BadLocationException e2) {
                }
            }
            DefaultCaret.this.visible = !DefaultCaret.this.visible;
            DefaultCaret.this.repaint();
        }

        /* JADX WARN: Removed duplicated region for block: B:38:0x0139  */
        @Override // javax.swing.event.DocumentListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void insertUpdate(javax.swing.event.DocumentEvent r7) {
            /*
                Method dump skipped, instructions count: 417
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.DefaultCaret.Handler.insertUpdate(javax.swing.event.DocumentEvent):void");
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            if (DefaultCaret.this.getUpdatePolicy() == 1 || (DefaultCaret.this.getUpdatePolicy() == 0 && !SwingUtilities.isEventDispatchThread())) {
                int length = DefaultCaret.this.component.getDocument().getLength();
                DefaultCaret.this.dot = Math.min(DefaultCaret.this.dot, length);
                DefaultCaret.this.mark = Math.min(DefaultCaret.this.mark, length);
                if ((documentEvent.getOffset() < DefaultCaret.this.dot || documentEvent.getOffset() < DefaultCaret.this.mark) && DefaultCaret.this.selectionTag != null) {
                    try {
                        DefaultCaret.this.component.getHighlighter().changeHighlight(DefaultCaret.this.selectionTag, Math.min(DefaultCaret.this.dot, DefaultCaret.this.mark), Math.max(DefaultCaret.this.dot, DefaultCaret.this.mark));
                        return;
                    } catch (BadLocationException e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                return;
            }
            int offset = documentEvent.getOffset();
            int length2 = offset + documentEvent.getLength();
            int i2 = DefaultCaret.this.dot;
            boolean z2 = false;
            int i3 = DefaultCaret.this.mark;
            boolean z3 = false;
            if (documentEvent instanceof AbstractDocument.UndoRedoDocumentEvent) {
                DefaultCaret.this.setDot(offset);
                return;
            }
            if (i2 >= length2) {
                i2 -= length2 - offset;
                if (i2 == length2) {
                    z2 = true;
                }
            } else if (i2 >= offset) {
                i2 = offset;
                z2 = true;
            }
            if (i3 >= length2) {
                i3 -= length2 - offset;
                if (i3 == length2) {
                    z3 = true;
                }
            } else if (i3 >= offset) {
                i3 = offset;
                z3 = true;
            }
            if (i3 == i2) {
                DefaultCaret.this.forceCaretPositionChange = true;
                try {
                    DefaultCaret.this.setDot(i2, DefaultCaret.this.guessBiasForOffset(i2, DefaultCaret.this.dotBias, DefaultCaret.this.dotLTR));
                    DefaultCaret.this.forceCaretPositionChange = false;
                    DefaultCaret.this.ensureValidPosition();
                    return;
                } catch (Throwable th) {
                    DefaultCaret.this.forceCaretPositionChange = false;
                    throw th;
                }
            }
            Position.Bias biasGuessBiasForOffset = DefaultCaret.this.dotBias;
            Position.Bias biasGuessBiasForOffset2 = DefaultCaret.this.markBias;
            if (z2) {
                biasGuessBiasForOffset = DefaultCaret.this.guessBiasForOffset(i2, biasGuessBiasForOffset, DefaultCaret.this.dotLTR);
            }
            if (z3) {
                biasGuessBiasForOffset2 = DefaultCaret.this.guessBiasForOffset(DefaultCaret.this.mark, biasGuessBiasForOffset2, DefaultCaret.this.markLTR);
            }
            DefaultCaret.this.setDot(i3, biasGuessBiasForOffset2);
            if (DefaultCaret.this.getDot() == i3) {
                DefaultCaret.this.moveDot(i2, biasGuessBiasForOffset);
            }
            DefaultCaret.this.ensureValidPosition();
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
            if (DefaultCaret.this.getUpdatePolicy() != 1) {
                if ((DefaultCaret.this.getUpdatePolicy() != 0 || SwingUtilities.isEventDispatchThread()) && (documentEvent instanceof AbstractDocument.UndoRedoDocumentEvent)) {
                    DefaultCaret.this.setDot(documentEvent.getOffset() + documentEvent.getLength());
                }
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object oldValue = propertyChangeEvent.getOldValue();
            Object newValue = propertyChangeEvent.getNewValue();
            if ((oldValue instanceof Document) || (newValue instanceof Document)) {
                DefaultCaret.this.setDot(0);
                if (oldValue != null) {
                    ((Document) oldValue).removeDocumentListener(this);
                }
                if (newValue != null) {
                    ((Document) newValue).addDocumentListener(this);
                    return;
                }
                return;
            }
            if (Enabled.NAME.equals(propertyChangeEvent.getPropertyName())) {
                Boolean bool = (Boolean) propertyChangeEvent.getNewValue();
                if (DefaultCaret.this.component.isFocusOwner()) {
                    if (bool == Boolean.TRUE) {
                        if (DefaultCaret.this.component.isEditable()) {
                            DefaultCaret.this.setVisible(true);
                        }
                        DefaultCaret.this.setSelectionVisible(true);
                        return;
                    } else {
                        DefaultCaret.this.setVisible(false);
                        DefaultCaret.this.setSelectionVisible(false);
                        return;
                    }
                }
                return;
            }
            if ("caretWidth".equals(propertyChangeEvent.getPropertyName())) {
                Integer num = (Integer) propertyChangeEvent.getNewValue();
                if (num == null) {
                    DefaultCaret.this.caretWidth = -1;
                } else {
                    DefaultCaret.this.caretWidth = num.intValue();
                }
                DefaultCaret.this.repaint();
                return;
            }
            if ("caretAspectRatio".equals(propertyChangeEvent.getPropertyName())) {
                Number number = (Number) propertyChangeEvent.getNewValue();
                if (number == null) {
                    DefaultCaret.this.aspectRatio = -1.0f;
                } else {
                    DefaultCaret.this.aspectRatio = number.floatValue();
                }
                DefaultCaret.this.repaint();
            }
        }

        @Override // java.awt.datatransfer.ClipboardOwner
        public void lostOwnership(Clipboard clipboard, Transferable transferable) {
            if (DefaultCaret.this.ownsSelection) {
                DefaultCaret.this.ownsSelection = false;
                if (DefaultCaret.this.component != null && !DefaultCaret.this.component.hasFocus()) {
                    DefaultCaret.this.setSelectionVisible(false);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultCaret$DefaultFilterBypass.class */
    private class DefaultFilterBypass extends NavigationFilter.FilterBypass {
        private DefaultFilterBypass() {
        }

        @Override // javax.swing.text.NavigationFilter.FilterBypass
        public Caret getCaret() {
            return DefaultCaret.this;
        }

        @Override // javax.swing.text.NavigationFilter.FilterBypass
        public void setDot(int i2, Position.Bias bias) {
            DefaultCaret.this.handleSetDot(i2, bias);
        }

        @Override // javax.swing.text.NavigationFilter.FilterBypass
        public void moveDot(int i2, Position.Bias bias) {
            DefaultCaret.this.handleMoveDot(i2, bias);
        }
    }
}
