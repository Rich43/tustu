package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.GlyphView;
import javax.swing.text.JTextComponent;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextFieldUI.class */
public class BasicTextFieldUI extends BasicTextUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "TextField";
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.text.ViewFactory
    public View create(Element element) {
        String name;
        if (Boolean.TRUE.equals(element.getDocument().getProperty("i18n")) && (name = element.getName()) != null) {
            if (name.equals(AbstractDocument.ContentElementName)) {
                return new GlyphView(element) { // from class: javax.swing.plaf.basic.BasicTextFieldUI.1
                    @Override // javax.swing.text.GlyphView, javax.swing.text.View
                    public float getMinimumSpan(int i2) {
                        return getPreferredSpan(i2);
                    }
                };
            }
            if (name.equals(AbstractDocument.ParagraphElementName)) {
                return new I18nFieldView(element);
            }
        }
        return new FieldView(element);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int ascent;
        super.getBaseline(jComponent, i2, i3);
        View rootView = getRootView((JTextComponent) jComponent);
        if (rootView.getViewCount() > 0) {
            Insets insets = jComponent.getInsets();
            int i4 = (i3 - insets.top) - insets.bottom;
            if (i4 > 0) {
                int i5 = insets.top;
                View view = rootView.getView(0);
                int preferredSpan = (int) view.getPreferredSpan(1);
                if (i4 != preferredSpan) {
                    i5 += (i4 - preferredSpan) / 2;
                }
                if (view instanceof I18nFieldView) {
                    int baseline = BasicHTML.getBaseline(view, (i2 - insets.left) - insets.right, i4);
                    if (baseline < 0) {
                        return -1;
                    }
                    ascent = i5 + baseline;
                } else {
                    ascent = i5 + jComponent.getFontMetrics(jComponent.getFont()).getAscent();
                }
                return ascent;
            }
            return -1;
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CENTER_OFFSET;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextFieldUI$I18nFieldView.class */
    static class I18nFieldView extends ParagraphView {
        I18nFieldView(Element element) {
            super(element);
        }

        @Override // javax.swing.text.ParagraphView, javax.swing.text.FlowView
        public int getFlowSpan(int i2) {
            return Integer.MAX_VALUE;
        }

        @Override // javax.swing.text.ParagraphView
        protected void setJustification(int i2) {
        }

        static boolean isLeftToRight(Component component) {
            return component.getComponentOrientation().isLeftToRight();
        }

        Shape adjustAllocation(Shape shape) {
            if (shape != null) {
                Rectangle bounds = shape.getBounds();
                int preferredSpan = (int) getPreferredSpan(1);
                int preferredSpan2 = (int) getPreferredSpan(0);
                if (bounds.height != preferredSpan) {
                    int i2 = bounds.height - preferredSpan;
                    bounds.f12373y += i2 / 2;
                    bounds.height -= i2;
                }
                Container container = getContainer();
                if (container instanceof JTextField) {
                    BoundedRangeModel horizontalVisibility = ((JTextField) container).getHorizontalVisibility();
                    int iMax = Math.max(preferredSpan2, bounds.width);
                    int value = horizontalVisibility.getValue();
                    int iMin = Math.min(iMax, bounds.width - 1);
                    if (value + iMin > iMax) {
                        value = iMax - iMin;
                    }
                    horizontalVisibility.setRangeProperties(value, iMin, horizontalVisibility.getMinimum(), iMax, false);
                    if (preferredSpan2 < bounds.width) {
                        int i3 = (bounds.width - 1) - preferredSpan2;
                        int horizontalAlignment = ((JTextField) container).getHorizontalAlignment();
                        if (isLeftToRight(container)) {
                            if (horizontalAlignment == 10) {
                                horizontalAlignment = 2;
                            } else if (horizontalAlignment == 11) {
                                horizontalAlignment = 4;
                            }
                        } else if (horizontalAlignment == 10) {
                            horizontalAlignment = 4;
                        } else if (horizontalAlignment == 11) {
                            horizontalAlignment = 2;
                        }
                        switch (horizontalAlignment) {
                            case 0:
                                bounds.f12372x += i3 / 2;
                                bounds.width -= i3;
                                break;
                            case 4:
                                bounds.f12372x += i3;
                                bounds.width -= i3;
                                break;
                        }
                    } else {
                        bounds.width = preferredSpan2;
                        bounds.f12372x -= horizontalVisibility.getValue();
                    }
                }
                return bounds;
            }
            return null;
        }

        void updateVisibilityModel() {
            Container container = getContainer();
            if (container instanceof JTextField) {
                BoundedRangeModel horizontalVisibility = ((JTextField) container).getHorizontalVisibility();
                int preferredSpan = (int) getPreferredSpan(0);
                int extent = horizontalVisibility.getExtent();
                int iMax = Math.max(preferredSpan, extent);
                int i2 = extent == 0 ? iMax : extent;
                int i3 = iMax - i2;
                int value = horizontalVisibility.getValue();
                if (value + i2 > iMax) {
                    value = iMax - i2;
                }
                horizontalVisibility.setRangeProperties(Math.max(0, Math.min(i3, value)), i2, 0, iMax, false);
            }
        }

        @Override // javax.swing.text.ParagraphView, javax.swing.text.BoxView, javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            Rectangle rectangle = (Rectangle) shape;
            graphics.clipRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
            super.paint(graphics, adjustAllocation(shape));
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public int getResizeWeight(int i2) {
            if (i2 == 0) {
                return 1;
            }
            return 0;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            return super.modelToView(i2, adjustAllocation(shape), bias);
        }

        @Override // javax.swing.text.CompositeView, javax.swing.text.View
        public Shape modelToView(int i2, Position.Bias bias, int i3, Position.Bias bias2, Shape shape) throws BadLocationException {
            return super.modelToView(i2, bias, i3, bias2, adjustAllocation(shape));
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
        public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
            return super.viewToModel(f2, f3, adjustAllocation(shape), biasArr);
        }

        @Override // javax.swing.text.FlowView, javax.swing.text.View
        public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            super.insertUpdate(documentEvent, adjustAllocation(shape), viewFactory);
            updateVisibilityModel();
        }

        @Override // javax.swing.text.FlowView, javax.swing.text.View
        public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            super.removeUpdate(documentEvent, adjustAllocation(shape), viewFactory);
            updateVisibilityModel();
        }
    }
}
