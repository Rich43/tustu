package org.icepdf.ri.common.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.icepdf.core.pobjects.annotations.LineAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/LineArrowAnnotationHandler.class */
public class LineArrowAnnotationHandler extends LineAnnotationHandler {
    public LineArrowAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        startLineEnding = LineAnnotation.LINE_END_OPEN_ARROW;
        endLineEnding = LineAnnotation.LINE_END_NONE;
    }

    @Override // org.icepdf.ri.common.tools.LineAnnotationHandler, org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        if (this.startOfLine != null && this.endOfLine != null) {
            Graphics2D gg = (Graphics2D) g2;
            Color oldColor = gg.getColor();
            Stroke oldStroke = gg.getStroke();
            g2.setColor(lineColor);
            gg.setStroke(stroke);
            gg.drawLine((int) this.startOfLine.getX(), (int) this.startOfLine.getY(), (int) this.endOfLine.getX(), (int) this.endOfLine.getY());
            if (!startLineEnding.equals(LineAnnotation.LINE_END_NONE)) {
                LineAnnotation.drawLineStart(gg, startLineEnding, this.startOfLine, this.endOfLine, lineColor, internalColor);
            }
            if (!endLineEnding.equals(LineAnnotation.LINE_END_NONE)) {
                LineAnnotation.drawLineEnd(gg, endLineEnding, this.endOfLine, this.endOfLine, lineColor, internalColor);
            }
            g2.setColor(oldColor);
            gg.setStroke(oldStroke);
        }
    }
}
