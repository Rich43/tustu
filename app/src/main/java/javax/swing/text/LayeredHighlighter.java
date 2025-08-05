package javax.swing.text;

import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.text.Highlighter;

/* loaded from: rt.jar:javax/swing/text/LayeredHighlighter.class */
public abstract class LayeredHighlighter implements Highlighter {

    /* loaded from: rt.jar:javax/swing/text/LayeredHighlighter$LayerPainter.class */
    public static abstract class LayerPainter implements Highlighter.HighlightPainter {
        public abstract Shape paintLayer(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view);
    }

    public abstract void paintLayeredHighlights(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view);
}
