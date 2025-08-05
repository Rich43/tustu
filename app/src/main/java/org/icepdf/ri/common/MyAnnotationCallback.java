package org.icepdf.ri.common;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.actions.Action;
import org.icepdf.core.pobjects.actions.GoToAction;
import org.icepdf.core.pobjects.actions.GoToRAction;
import org.icepdf.core.pobjects.actions.LaunchAction;
import org.icepdf.core.pobjects.actions.URIAction;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.core.pobjects.annotations.MarkupAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.PageViewComponent;
import org.icepdf.ri.common.views.annotations.PopupAnnotationComponent;
import org.icepdf.ri.util.BareBonesBrowserLaunch;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/MyAnnotationCallback.class */
public class MyAnnotationCallback implements AnnotationCallback {
    private static final Logger logger = Logger.getLogger(MyAnnotationCallback.class.toString());
    private DocumentViewController documentViewController;

    public MyAnnotationCallback(DocumentViewController documentViewController) {
        this.documentViewController = documentViewController;
    }

    @Override // org.icepdf.ri.common.views.AnnotationCallback
    public void processAnnotationAction(Annotation annotation) throws Exception {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Annotation " + annotation.toString());
            if (annotation.getAction() != null) {
                logger.fine("   Action: " + annotation.getAction().toString());
            }
        }
        if (annotation instanceof LinkAnnotation) {
            LinkAnnotation linkAnnotation = (LinkAnnotation) annotation;
            if (linkAnnotation.getAction() != null) {
                Action action = linkAnnotation.getAction();
                if ((action instanceof GoToAction) && this.documentViewController != null) {
                    this.documentViewController.setDestinationTarget(((GoToAction) action).getDestination());
                    return;
                }
                if (action instanceof URIAction) {
                    BareBonesBrowserLaunch.openURL(((URIAction) action).getURI());
                    return;
                }
                if (!(action instanceof GoToRAction) && (action instanceof LaunchAction)) {
                    LaunchAction launchAction = (LaunchAction) action;
                    String file = launchAction.getExternalFile();
                    String location = this.documentViewController.getDocument().getDocumentLocation();
                    BareBonesBrowserLaunch.openFile(location.substring(0, location.lastIndexOf(File.separator) + 1) + file);
                    return;
                }
                return;
            }
            if (linkAnnotation.getDestination() != null && this.documentViewController != null) {
                this.documentViewController.setDestinationTarget(linkAnnotation.getDestination());
                return;
            }
            return;
        }
        if (annotation.getAction() != null) {
            Action action2 = annotation.getAction();
            if (action2 instanceof GoToAction) {
                this.documentViewController.setDestinationTarget(((GoToAction) action2).getDestination());
            } else if (action2 instanceof URIAction) {
                BareBonesBrowserLaunch.openURL(((URIAction) action2).getURI());
            }
        }
    }

    @Override // org.icepdf.ri.common.views.AnnotationCallback
    public void pageAnnotationsInitialized(Page page) {
    }

    @Override // org.icepdf.ri.common.views.AnnotationCallback
    public void newAnnotation(PageViewComponent pageComponent, AnnotationComponent annotationComponent) {
        Document document = this.documentViewController.getDocument();
        PageTree pageTree = document.getPageTree();
        Page page = pageTree.getPage(pageComponent.getPageIndex());
        page.addAnnotation(annotationComponent.getAnnotation());
        pageComponent.addAnnotation(annotationComponent);
    }

    @Override // org.icepdf.ri.common.views.AnnotationCallback
    public void updateAnnotation(AnnotationComponent annotationComponent) {
        Document document = this.documentViewController.getDocument();
        PageTree pageTree = document.getPageTree();
        Page page = pageTree.getPage(annotationComponent.getPageIndex());
        page.updateAnnotation(annotationComponent.getAnnotation());
    }

    @Override // org.icepdf.ri.common.views.AnnotationCallback
    public void removeAnnotation(PageViewComponent pageComponent, AnnotationComponent annotationComponent) {
        Document document = this.documentViewController.getDocument();
        PageTree pageTree = document.getPageTree();
        Page page = pageTree.getPage(pageComponent.getPageIndex());
        page.deleteAnnotation(annotationComponent.getAnnotation());
        pageComponent.removeAnnotation(annotationComponent);
        if (annotationComponent.getAnnotation() instanceof MarkupAnnotation) {
            MarkupAnnotation markupAnnotation = (MarkupAnnotation) annotationComponent.getAnnotation();
            if (markupAnnotation.getPopupAnnotation() != null) {
                page.deleteAnnotation(markupAnnotation.getPopupAnnotation());
                ArrayList<AnnotationComponent> annotationComponents = ((AbstractPageViewComponent) pageComponent).getAnnotationComponents();
                Reference popupReference = markupAnnotation.getPopupAnnotation().getPObjectReference();
                int max = annotationComponents.size();
                for (int i2 = 0; i2 < max; i2++) {
                    AnnotationComponent annotationComp = annotationComponents.get(i2);
                    Reference compReference = annotationComp.getAnnotation().getPObjectReference();
                    if (compReference.equals(popupReference) && (annotationComp instanceof PopupAnnotationComponent)) {
                        PopupAnnotationComponent popupComponent = (PopupAnnotationComponent) annotationComp;
                        pageComponent.removeAnnotation(popupComponent);
                    }
                }
            }
        }
    }
}
