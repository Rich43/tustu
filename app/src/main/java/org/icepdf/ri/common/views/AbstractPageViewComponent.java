package org.icepdf.ri.common.views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLayeredPane;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.ri.common.tools.AnnotationSelectionHandler;
import org.icepdf.ri.common.tools.CircleAnnotationHandler;
import org.icepdf.ri.common.tools.FreeTextAnnotationHandler;
import org.icepdf.ri.common.tools.HighLightAnnotationHandler;
import org.icepdf.ri.common.tools.InkAnnotationHandler;
import org.icepdf.ri.common.tools.LineAnnotationHandler;
import org.icepdf.ri.common.tools.LineArrowAnnotationHandler;
import org.icepdf.ri.common.tools.LinkAnnotationHandler;
import org.icepdf.ri.common.tools.SquareAnnotationHandler;
import org.icepdf.ri.common.tools.StrikeOutAnnotationHandler;
import org.icepdf.ri.common.tools.TextAnnotationHandler;
import org.icepdf.ri.common.tools.TextSelectionPageHandler;
import org.icepdf.ri.common.tools.ToolHandler;
import org.icepdf.ri.common.tools.UnderLineAnnotationHandler;
import org.icepdf.ri.common.tools.ZoomInPageHandler;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;
import org.icepdf.ri.common.views.annotations.PopupAnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/AbstractPageViewComponent.class */
public abstract class AbstractPageViewComponent extends JLayeredPane implements PageViewComponent {
    protected DocumentView parentDocumentView;
    protected DocumentViewModel documentViewModel;
    protected DocumentViewController documentViewController;
    protected ToolHandler currentToolHandler;
    protected ArrayList<AnnotationComponent> annotationComponents;

    public abstract Page getPage();

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void setToolMode(int viewToolMode) {
        if (this.currentToolHandler != null) {
            this.currentToolHandler.uninstallTool();
            removeMouseListener(this.currentToolHandler);
            removeMouseMotionListener(this.currentToolHandler);
        }
        switch (viewToolMode) {
            case 2:
                this.currentToolHandler = new ZoomInPageHandler(this.documentViewController, this, this.documentViewModel);
                break;
            case 3:
            case 4:
            case 10:
            default:
                this.currentToolHandler = null;
                break;
            case 5:
                this.currentToolHandler = new TextSelectionPageHandler(this.documentViewController, this, this.documentViewModel);
                break;
            case 6:
                this.currentToolHandler = new AnnotationSelectionHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 7:
                this.currentToolHandler = new LinkAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 8:
                this.currentToolHandler = new HighLightAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                ((HighLightAnnotationHandler) this.currentToolHandler).createTextMarkupAnnotation(null);
                this.documentViewController.clearSelectedText();
                break;
            case 9:
                this.currentToolHandler = new UnderLineAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                ((UnderLineAnnotationHandler) this.currentToolHandler).createTextMarkupAnnotation(null);
                this.documentViewController.clearSelectedText();
                break;
            case 11:
                this.currentToolHandler = new StrikeOutAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                ((StrikeOutAnnotationHandler) this.currentToolHandler).createTextMarkupAnnotation(null);
                this.documentViewController.clearSelectedText();
                break;
            case 12:
                this.currentToolHandler = new LineAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 13:
                this.currentToolHandler = new LineArrowAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 14:
                this.currentToolHandler = new SquareAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 15:
                this.currentToolHandler = new CircleAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 16:
                this.currentToolHandler = new InkAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 17:
                this.currentToolHandler = new FreeTextAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
            case 18:
                this.currentToolHandler = new TextAnnotationHandler(this.documentViewController, this, this.documentViewModel);
                this.documentViewController.clearSelectedText();
                break;
        }
        if (this.currentToolHandler != null) {
            this.currentToolHandler.installTool();
            addMouseListener(this.currentToolHandler);
            addMouseMotionListener(this.currentToolHandler);
        }
    }

    public void refreshAnnotationComponents(Page page) {
        List<Annotation> annotations;
        if (page != null && (annotations = page.getAnnotations()) != null && annotations.size() > 0 && this.annotationComponents == null) {
            this.annotationComponents = new ArrayList<>(annotations.size());
            for (Annotation annotation : annotations) {
                AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this, this.documentViewModel);
                this.annotationComponents.add(comp);
                if (comp instanceof PopupAnnotationComponent) {
                    add(comp, JLayeredPane.POPUP_LAYER);
                } else {
                    add(comp, JLayeredPane.DEFAULT_LAYER);
                }
            }
        }
    }

    public ArrayList<AnnotationComponent> getAnnotationComponents() {
        return this.annotationComponents;
    }

    public static boolean isAnnotationTool(int displayTool) {
        return displayTool == 6 || displayTool == 7 || displayTool == 8 || displayTool == 10 || displayTool == 11 || displayTool == 9;
    }
}
