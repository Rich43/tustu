package org.icepdf.ri.util;

import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.ri.common.FileExtensionUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/SVG.class */
public class SVG {
    private static final Logger logger = Logger.getLogger(SVG.class.toString());

    public static void createSVG(Document pdfDocument, int pageNumber, Writer out) throws DOMException {
        if (pdfDocument != null && pageNumber >= 0) {
            try {
                if (pageNumber < pdfDocument.getNumberOfPages()) {
                    DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
                    org.w3c.dom.Document document = domImpl.createDocument(null, FileExtensionUtils.svg, null);
                    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
                    PDimension pdfDimension = pdfDocument.getPageDimension(pageNumber, 0.0f, 1.0f);
                    svgGenerator.setSVGCanvasSize(pdfDimension.toDimension());
                    pdfDocument.paintPage(pageNumber, svgGenerator, 2, 2, 0.0f, 1.0f);
                    svgGenerator.stream(out, true);
                }
            } catch (SVGGraphics2DIOException e2) {
                logger.log(Level.SEVERE, "Error creating svg document.", e2);
            }
        }
    }
}
