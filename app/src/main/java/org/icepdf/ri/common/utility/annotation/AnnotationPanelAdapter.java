package org.icepdf.ri.common.utility.annotation;

import java.util.ResourceBundle;
import javax.swing.JPanel;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.DocumentViewController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/AnnotationPanelAdapter.class */
public abstract class AnnotationPanelAdapter extends JPanel implements AnnotationProperties {
    protected AnnotationComponent currentAnnotationComponent;
    protected DocumentViewController documentViewController;
    protected SwingController controller;
    protected ResourceBundle messageBundle;
    protected static ValueLabelItem[] VISIBLE_TYPE_LIST;
    protected static ValueLabelItem[] LINE_THICKNESS_LIST;
    protected static ValueLabelItem[] LINE_STYLE_LIST;

    protected AnnotationPanelAdapter(SwingController controller) {
        setDoubleBuffered(true);
        this.controller = controller;
        this.documentViewController = controller.getDocumentViewController();
        this.messageBundle = controller.getMessageBundle();
        if (LINE_THICKNESS_LIST == null) {
            LINE_THICKNESS_LIST = new ValueLabelItem[]{new ValueLabelItem(Float.valueOf(1.0f), this.messageBundle.getString("viewer.common.number.one")), new ValueLabelItem(Float.valueOf(2.0f), this.messageBundle.getString("viewer.common.number.two")), new ValueLabelItem(Float.valueOf(3.0f), this.messageBundle.getString("viewer.common.number.three")), new ValueLabelItem(Float.valueOf(4.0f), this.messageBundle.getString("viewer.common.number.four")), new ValueLabelItem(Float.valueOf(5.0f), this.messageBundle.getString("viewer.common.number.five")), new ValueLabelItem(Float.valueOf(10.0f), this.messageBundle.getString("viewer.common.number.ten")), new ValueLabelItem(Float.valueOf(15.0f), this.messageBundle.getString("viewer.common.number.fifteen"))};
        }
        if (VISIBLE_TYPE_LIST == null) {
            VISIBLE_TYPE_LIST = new ValueLabelItem[]{new ValueLabelItem(true, this.messageBundle.getString("viewer.utilityPane.annotation.border.borderType.visibleRectangle")), new ValueLabelItem(false, this.messageBundle.getString("viewer.utilityPane.annotation.border.borderType.invisibleRectangle"))};
        }
        if (LINE_STYLE_LIST == null) {
            LINE_STYLE_LIST = new ValueLabelItem[]{new ValueLabelItem(BorderStyle.BORDER_STYLE_SOLID, this.messageBundle.getString("viewer.utilityPane.annotation.border.solid")), new ValueLabelItem(BorderStyle.BORDER_STYLE_DASHED, this.messageBundle.getString("viewer.utilityPane.annotation.border.dashed"))};
        }
    }

    protected void updateCurrentAnnotation() {
        if (this.documentViewController.getAnnotationCallback() != null) {
            this.documentViewController.getAnnotationCallback().updateAnnotation(this.currentAnnotationComponent);
        }
    }
}
