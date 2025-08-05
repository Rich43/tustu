package org.icepdf.core.pobjects.graphics.commands;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.graphics.TextSprite;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/PostScriptEncoder.class */
public class PostScriptEncoder {
    private static final Logger logger = Logger.getLogger(PostScriptEncoder.class.toString());
    private static final String SPACE = " ";
    private static final String NEWLINE = "\r\n";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NAME = "/";
    private static final String BEGIN_ARRAY = "[";
    private static final String END_ARRAY = "]";
    private static final String BEGIN_STRING = "(";
    private static final String END_STRING = ")";

    private PostScriptEncoder() {
    }

    public static byte[] generatePostScript(ArrayList<DrawCmd> drawCmds) {
        StringBuilder postScript = new StringBuilder();
        Color color = null;
        Shape currentShape = null;
        if (logger.isLoggable(Level.FINEST)) {
            if (drawCmds != null) {
                logger.finest("PostScriptEncoder processing: " + drawCmds.size() + " commands.");
            } else {
                logger.finest("PostScriptEncoder does not have any shapes to process. ");
            }
        }
        try {
            Iterator i$ = drawCmds.iterator();
            while (i$.hasNext()) {
                DrawCmd drawCmd = i$.next();
                if (drawCmd instanceof TransformDrawCmd) {
                    AffineTransform af2 = ((TransformDrawCmd) drawCmd).getAffineTransform();
                    postScript.append(af2.getScaleX()).append(" ").append(af2.getShearX()).append(" ").append(af2.getShearY()).append(" ").append(af2.getScaleY()).append(" ").append(af2.getTranslateX()).append(" ").append(af2.getTranslateY()).append(" ").append(PdfOps.cm_TOKEN).append("\r\n");
                } else if (drawCmd instanceof TextTransformDrawCmd) {
                    AffineTransform af3 = ((TransformDrawCmd) drawCmd).getAffineTransform();
                    postScript.append(af3.getScaleX()).append(" ").append(af3.getShearX()).append(" ").append(af3.getShearY()).append(" ").append(af3.getScaleY()).append(" ").append(af3.getTranslateX()).append(" ").append(af3.getTranslateY()).append(" ").append(PdfOps.Tm_TOKEN).append("\r\n");
                } else if (drawCmd instanceof ColorDrawCmd) {
                    color = ((ColorDrawCmd) drawCmd).getColor();
                } else if (drawCmd instanceof DrawDrawCmd) {
                    float[] colors = color.getRGBColorComponents(null);
                    postScript.append(colors[0]).append(" ").append(colors[1]).append(" ").append(colors[2]).append(" ").append(PdfOps.RG_TOKEN).append("\r\n");
                    generateShapePostScript(currentShape, postScript);
                    postScript.append(PdfOps.S_TOKEN).append("\r\n");
                } else if (drawCmd instanceof FillDrawCmd) {
                    float[] colors2 = color.getRGBColorComponents(null);
                    postScript.append(colors2[0]).append(" ").append(colors2[1]).append(" ").append(colors2[2]).append(" ").append(PdfOps.rg_TOKEN).append("\r\n");
                    generateShapePostScript(currentShape, postScript);
                    postScript.append(PdfOps.f_TOKEN).append(" ");
                } else if (drawCmd instanceof ShapeDrawCmd) {
                    currentShape = ((ShapeDrawCmd) drawCmd).getShape();
                } else if (drawCmd instanceof StrokeDrawCmd) {
                    BasicStroke stroke = (BasicStroke) ((StrokeDrawCmd) drawCmd).getStroke();
                    postScript.append(stroke.getLineWidth()).append(" ").append(PdfOps.w_TOKEN).append(" ");
                    float[] dashes = stroke.getDashArray();
                    postScript.append(BEGIN_ARRAY);
                    if (dashes != null) {
                        int max = dashes.length;
                        for (int i2 = 0; i2 < max; i2++) {
                            postScript.append(dashes[i2]);
                            if (i2 < max - 1) {
                                postScript.append(" ");
                            }
                        }
                    }
                    postScript.append(END_ARRAY).append(" ");
                    postScript.append(stroke.getDashPhase()).append(" ").append(PdfOps.d_TOKEN).append(" ");
                    if (stroke.getEndCap() == 0) {
                        postScript.append(0).append(" ").append("J").append(" ");
                    } else if (stroke.getEndCap() == 1) {
                        postScript.append(1).append(" ").append("J").append(" ");
                    } else if (stroke.getEndCap() == 2) {
                        postScript.append(2).append(" ").append("J").append(" ");
                    }
                    if (stroke.getMiterLimit() == 0.0f) {
                        postScript.append(0).append(" ").append(PdfOps.j_TOKEN).append(" ");
                    } else if (stroke.getMiterLimit() == 1.0f) {
                        postScript.append(1).append(" ").append(PdfOps.j_TOKEN).append(" ");
                    } else if (stroke.getMiterLimit() == 2.0f) {
                        postScript.append(2).append(" ").append(PdfOps.j_TOKEN).append(" ");
                    }
                    postScript.append("\r\n");
                } else if (drawCmd instanceof GraphicsStateCmd) {
                    postScript.append('/').append((Object) ((GraphicsStateCmd) drawCmd).getGraphicStateName()).append(" ").append(PdfOps.gs_TOKEN).append(" ");
                } else if (drawCmd instanceof TextSpriteDrawCmd) {
                    postScript.append(PdfOps.BT_TOKEN).append("\r\n");
                    TextSpriteDrawCmd textSpriteDrawCmd = (TextSpriteDrawCmd) drawCmd;
                    TextSprite textSprite = textSpriteDrawCmd.getTextSprite();
                    ArrayList<GlyphText> glyphTexts = textSprite.getGlyphSprites();
                    if (glyphTexts.size() > 0) {
                        postScript.append("1 0 0 -1 ").append(glyphTexts.get(0).getX()).append(" ").append(glyphTexts.get(0).getY()).append(" ").append(PdfOps.Tm_TOKEN).append("\r\n");
                        postScript.append("/").append(textSprite.getFontName()).append(" ").append(textSprite.getFontSize()).append(" ").append(PdfOps.Tf_TOKEN).append("\r\n");
                        float[] colors3 = textSprite.getStrokeColor().getRGBColorComponents(null);
                        postScript.append(colors3[0]).append(" ").append(colors3[1]).append(" ").append(colors3[2]).append(" ").append(PdfOps.rg_TOKEN).append("\r\n");
                        float y2 = glyphTexts.get(0).getY();
                        StringBuilder line = new StringBuilder();
                        int max2 = glyphTexts.size();
                        for (int i3 = 0; i3 < max2; i3++) {
                            GlyphText glyphText = glyphTexts.get(i3);
                            if (y2 != glyphText.getY() || i3 == max2 - 1) {
                                if (i3 == max2 - 1) {
                                    line.append(glyphText.getUnicode());
                                }
                                postScript.append(BEGIN_ARRAY).append(BEGIN_STRING).append(new LiteralStringObject(line.toString()).toString()).append(END_STRING).append(END_ARRAY).append(" ").append(PdfOps.TJ_TOKEN).append("\r\n");
                                postScript.append(0).append(" ").append(y2 - glyphText.getY()).append(" ").append(PdfOps.Td_TOKEN).append("\r\n");
                                y2 = glyphText.getY();
                                line = new StringBuilder();
                            }
                            line.append(glyphText.getUnicode());
                        }
                        postScript.append(PdfOps.ET_TOKEN).append("\r\n");
                    }
                }
            }
        } catch (Throwable e2) {
            logger.log(Level.WARNING, "Error encoding PostScript notation ", e2);
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("PostEncoding: " + postScript.toString());
        }
        return postScript.toString().getBytes();
    }

    private static void generateShapePostScript(Shape currentShape, StringBuilder postScript) {
        PathIterator pathIterator = currentShape.getPathIterator(null);
        float[] segment = new float[6];
        while (!pathIterator.isDone()) {
            int segmentType = pathIterator.currentSegment(segment);
            switch (segmentType) {
                case 0:
                    postScript.append(segment[0]).append(" ").append(segment[1]).append(" ").append(PdfOps.m_TOKEN).append("\r\n");
                    break;
                case 1:
                    postScript.append(segment[0]).append(" ").append(segment[1]).append(" ").append(PdfOps.l_TOKEN).append("\r\n");
                    break;
                case 2:
                    postScript.append(segment[0]).append(" ").append(segment[1]).append(" ").append(segment[2]).append(" ").append(segment[3]).append(" ").append(PdfOps.v_TOKEN).append("\r\n");
                    break;
                case 3:
                    postScript.append(segment[0]).append(" ").append(segment[1]).append(" ").append(segment[2]).append(" ").append(segment[3]).append(" ").append(segment[4]).append(" ").append(segment[5]).append(" ").append(PdfOps.c_TOKEN).append("\r\n");
                    break;
                case 4:
                    postScript.append(PdfOps.h_TOKEN).append(" ");
                    break;
            }
            pathIterator.next();
        }
    }
}
