package javax.swing.text;

import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/NavigationFilter.class */
public class NavigationFilter {

    /* loaded from: rt.jar:javax/swing/text/NavigationFilter$FilterBypass.class */
    public static abstract class FilterBypass {
        public abstract Caret getCaret();

        public abstract void setDot(int i2, Position.Bias bias);

        public abstract void moveDot(int i2, Position.Bias bias);
    }

    public void setDot(FilterBypass filterBypass, int i2, Position.Bias bias) {
        filterBypass.setDot(i2, bias);
    }

    public void moveDot(FilterBypass filterBypass, int i2, Position.Bias bias) {
        filterBypass.moveDot(i2, bias);
    }

    public int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException {
        return jTextComponent.getUI().getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
    }
}
