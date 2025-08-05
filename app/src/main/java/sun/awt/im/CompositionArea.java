package sun.awt.im;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/* loaded from: rt.jar:sun/awt/im/CompositionArea.class */
public final class CompositionArea extends JPanel implements InputMethodListener {
    private CompositionAreaHandler handler;
    private TextLayout composedTextLayout;
    private TextHitInfo caret = null;
    private JFrame compositionWindow = (JFrame) InputMethodContext.createInputMethodWindow(Toolkit.getProperty("AWT.CompositionWindowTitle", "Input Window"), null, true);
    private static final int TEXT_ORIGIN_X = 5;
    private static final int TEXT_ORIGIN_Y = 15;
    private static final int PASSIVE_WIDTH = 480;
    private static final int WIDTH_MARGIN = 10;
    private static final int HEIGHT_MARGIN = 3;
    private static final long serialVersionUID = -1057247068746557444L;

    CompositionArea() throws HeadlessException {
        setOpaque(true);
        setBorder(LineBorder.createGrayLineBorder());
        setForeground(Color.black);
        setBackground(Color.white);
        enableInputMethods(true);
        enableEvents(8L);
        this.compositionWindow.getContentPane().add(this);
        this.compositionWindow.addWindowListener(new FrameWindowAdapter());
        addInputMethodListener(this);
        this.compositionWindow.enableInputMethods(false);
        this.compositionWindow.pack();
        Dimension size = this.compositionWindow.getSize();
        Dimension screenSize = getToolkit().getScreenSize();
        this.compositionWindow.setLocation((screenSize.width - size.width) - 20, (screenSize.height - size.height) - 100);
        this.compositionWindow.setVisible(false);
    }

    synchronized void setHandlerInfo(CompositionAreaHandler compositionAreaHandler, InputContext inputContext) {
        this.handler = compositionAreaHandler;
        ((InputMethodWindow) this.compositionWindow).setInputContext(inputContext);
    }

    @Override // java.awt.Component
    public InputMethodRequests getInputMethodRequests() {
        return this.handler;
    }

    private Rectangle getCaretRectangle(TextHitInfo textHitInfo) {
        int iRound = 0;
        TextLayout textLayout = this.composedTextLayout;
        if (textLayout != null) {
            iRound = Math.round(textLayout.getCaretInfo(textHitInfo)[0]);
        }
        Graphics graphics = getGraphics();
        try {
            FontMetrics fontMetrics = graphics.getFontMetrics();
            graphics.dispose();
            return new Rectangle(5 + iRound, 15 - fontMetrics.getAscent(), 0, fontMetrics.getAscent() + fontMetrics.getDescent());
        } catch (Throwable th) {
            graphics.dispose();
            throw th;
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(getForeground());
        TextLayout textLayout = this.composedTextLayout;
        if (textLayout != null) {
            textLayout.draw((Graphics2D) graphics, 5.0f, 15.0f);
        }
        if (this.caret != null) {
            Rectangle caretRectangle = getCaretRectangle(this.caret);
            graphics.setXORMode(getBackground());
            graphics.fillRect(caretRectangle.f12372x, caretRectangle.f12373y, 1, caretRectangle.height);
            graphics.setPaintMode();
        }
    }

    void setCompositionAreaVisible(boolean z2) {
        this.compositionWindow.setVisible(z2);
    }

    boolean isCompositionAreaVisible() {
        return this.compositionWindow.isVisible();
    }

    /* loaded from: rt.jar:sun/awt/im/CompositionArea$FrameWindowAdapter.class */
    class FrameWindowAdapter extends WindowAdapter {
        FrameWindowAdapter() {
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
            CompositionArea.this.requestFocus();
        }
    }

    @Override // java.awt.event.InputMethodListener
    public void inputMethodTextChanged(InputMethodEvent inputMethodEvent) {
        this.handler.inputMethodTextChanged(inputMethodEvent);
    }

    @Override // java.awt.event.InputMethodListener
    public void caretPositionChanged(InputMethodEvent inputMethodEvent) {
        this.handler.caretPositionChanged(inputMethodEvent);
    }

    void setText(AttributedCharacterIterator attributedCharacterIterator, TextHitInfo textHitInfo) {
        this.composedTextLayout = null;
        if (attributedCharacterIterator == null) {
            this.compositionWindow.setVisible(false);
            this.caret = null;
            return;
        }
        if (!this.compositionWindow.isVisible()) {
            this.compositionWindow.setVisible(true);
        }
        Graphics graphics = getGraphics();
        if (graphics == null) {
            return;
        }
        try {
            updateWindowLocation();
            this.composedTextLayout = new TextLayout(attributedCharacterIterator, ((Graphics2D) graphics).getFontRenderContext());
            Rectangle2D bounds = this.composedTextLayout.getBounds();
            this.caret = textHitInfo;
            int height = ((int) graphics.getFontMetrics().getMaxCharBounds(graphics).getHeight()) + 3;
            int i2 = height + this.compositionWindow.getInsets().top + this.compositionWindow.getInsets().bottom;
            int width = this.handler.getClientInputMethodRequests() == null ? 480 : ((int) bounds.getWidth()) + 10;
            int i3 = width + this.compositionWindow.getInsets().left + this.compositionWindow.getInsets().right;
            setPreferredSize(new Dimension(width, height));
            this.compositionWindow.setSize(new Dimension(i3, i2));
            paint(graphics);
            graphics.dispose();
        } catch (Throwable th) {
            graphics.dispose();
            throw th;
        }
    }

    void setCaret(TextHitInfo textHitInfo) {
        this.caret = textHitInfo;
        if (this.compositionWindow.isVisible()) {
            Graphics graphics = getGraphics();
            try {
                paint(graphics);
            } finally {
                graphics.dispose();
            }
        }
    }

    void updateWindowLocation() throws HeadlessException {
        InputMethodRequests clientInputMethodRequests = this.handler.getClientInputMethodRequests();
        if (clientInputMethodRequests == null) {
            return;
        }
        Point point = new Point();
        Rectangle textLocation = clientInputMethodRequests.getTextLocation(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = this.compositionWindow.getSize();
        if (textLocation.f12372x + size.width > screenSize.width) {
            point.f12370x = screenSize.width - size.width;
        } else {
            point.f12370x = textLocation.f12372x;
        }
        if (textLocation.f12373y + textLocation.height + 2 + size.height > screenSize.height) {
            point.f12371y = (textLocation.f12373y - 2) - size.height;
        } else {
            point.f12371y = textLocation.f12373y + textLocation.height + 2;
        }
        this.compositionWindow.setLocation(point);
    }

    Rectangle getTextLocation(TextHitInfo textHitInfo) {
        Rectangle caretRectangle = getCaretRectangle(textHitInfo);
        Point locationOnScreen = getLocationOnScreen();
        caretRectangle.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
        return caretRectangle;
    }

    TextHitInfo getLocationOffset(int i2, int i3) {
        TextLayout textLayout = this.composedTextLayout;
        if (textLayout == null) {
            return null;
        }
        Point locationOnScreen = getLocationOnScreen();
        int i4 = i2 - (locationOnScreen.f12370x + 5);
        int i5 = i3 - (locationOnScreen.f12371y + 15);
        if (textLayout.getBounds().contains(i4, i5)) {
            return textLayout.hitTestChar(i4, i5);
        }
        return null;
    }

    void setCompositionAreaUndecorated(boolean z2) {
        if (this.compositionWindow.isDisplayable()) {
            this.compositionWindow.removeNotify();
        }
        this.compositionWindow.setUndecorated(z2);
        this.compositionWindow.pack();
    }
}
