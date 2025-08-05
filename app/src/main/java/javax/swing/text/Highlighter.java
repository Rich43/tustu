package javax.swing.text;

import java.awt.Graphics;
import java.awt.Shape;

/* loaded from: rt.jar:javax/swing/text/Highlighter.class */
public interface Highlighter {

    /* loaded from: rt.jar:javax/swing/text/Highlighter$Highlight.class */
    public interface Highlight {
        int getStartOffset();

        int getEndOffset();

        HighlightPainter getPainter();
    }

    /* loaded from: rt.jar:javax/swing/text/Highlighter$HighlightPainter.class */
    public interface HighlightPainter {
        void paint(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent);
    }

    void install(JTextComponent jTextComponent);

    void deinstall(JTextComponent jTextComponent);

    void paint(Graphics graphics);

    Object addHighlight(int i2, int i3, HighlightPainter highlightPainter) throws BadLocationException;

    void removeHighlight(Object obj);

    void removeAllHighlights();

    void changeHighlight(Object obj, int i2, int i3) throws BadLocationException;

    Highlight[] getHighlights();
}
