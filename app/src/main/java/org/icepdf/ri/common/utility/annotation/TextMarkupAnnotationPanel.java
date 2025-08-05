package org.icepdf.ri.common.utility.annotation;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/TextMarkupAnnotationPanel.class */
public class TextMarkupAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_TEXT_MARKUP_TYPE = 0;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static ValueLabelItem[] TEXT_MARKUP_TYPE_LIST;
    private JComboBox textMarkupTypes;
    private JButton colorButton;
    private TextMarkupAnnotation annotation;

    public TextMarkupAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(2, 2, 5, 2));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        revalidate();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent newAnnotation) {
        if (newAnnotation == null || newAnnotation.getAnnotation() == null) {
            setEnabled(false);
            return;
        }
        this.currentAnnotationComponent = newAnnotation;
        this.annotation = (TextMarkupAnnotation) this.currentAnnotationComponent.getAnnotation();
        applySelectedValue(this.textMarkupTypes, this.annotation.getSubType());
        this.colorButton.setBackground(this.annotation.getTextMarkupColor());
        safeEnable(this.textMarkupTypes, true);
        safeEnable(this.colorButton, true);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.textMarkupTypes) {
                this.annotation.setSubtype((Name) item.getValue());
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) {
        Color chosenColor;
        if (e2.getSource() == this.colorButton && (chosenColor = JColorChooser.showDialog(this.colorButton, this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.colorChooserTitle"), this.colorButton.getBackground())) != null) {
            this.colorButton.setBackground(chosenColor);
            this.annotation.setTextMarkupColor(chosenColor);
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        if (TEXT_MARKUP_TYPE_LIST == null) {
            TEXT_MARKUP_TYPE_LIST = new ValueLabelItem[]{new ValueLabelItem(TextMarkupAnnotation.SUBTYPE_HIGHLIGHT, "Highlight"), new ValueLabelItem(TextMarkupAnnotation.SUBTYPE_STRIKE_OUT, "Strikeout"), new ValueLabelItem(TextMarkupAnnotation.SUBTYPE_UNDERLINE, "Underline")};
        }
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.appearance.title"), 1, 0));
        this.textMarkupTypes = new JComboBox(TEXT_MARKUP_TYPE_LIST);
        this.textMarkupTypes.setSelectedIndex(0);
        this.textMarkupTypes.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.highlightType")));
        add(this.textMarkupTypes);
        this.colorButton = new JButton();
        this.colorButton.addActionListener(this);
        this.colorButton.setOpaque(true);
        this.colorButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.colorLabel")));
        add(this.colorButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.textMarkupTypes, enabled);
        safeEnable(this.colorButton, enabled);
    }

    protected boolean safeEnable(JComponent comp, boolean enabled) {
        if (comp != null) {
            comp.setEnabled(enabled);
            return true;
        }
        return false;
    }

    private void applySelectedValue(JComboBox comboBox, Object value) {
        comboBox.removeItemListener(this);
        int i2 = 0;
        while (true) {
            if (i2 >= comboBox.getItemCount()) {
                break;
            }
            ValueLabelItem currentItem = (ValueLabelItem) comboBox.getItemAt(i2);
            if (!currentItem.getValue().equals(value)) {
                i2++;
            } else {
                comboBox.setSelectedIndex(i2);
                break;
            }
        }
        comboBox.addItemListener(this);
    }
}
