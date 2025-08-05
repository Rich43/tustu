package org.icepdf.core.util.content;

import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.ByteDoubleArrayInputStream;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.graphics.GlyphOutlineClip;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.ImageReference;
import org.icepdf.core.pobjects.graphics.InlineImageStreamReference;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.commands.GlyphOutlineDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ImageDrawCmd;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/content/OContentParser.class */
public class OContentParser extends AbstractContentParser {
    private static final Logger logger = Logger.getLogger(OContentParser.class.toString());

    public OContentParser(Library l2, Resources r2) {
        super(l2, r2);
    }

    /* JADX WARN: Removed duplicated region for block: B:231:0x0814  */
    @Override // org.icepdf.core.util.content.AbstractContentParser, org.icepdf.core.util.content.ContentParser
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.icepdf.core.util.content.ContentParser parse(byte[][] r10, org.icepdf.core.pobjects.Page r11) throws java.lang.InterruptedException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 2123
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.util.content.OContentParser.parse(byte[][], org.icepdf.core.pobjects.Page):org.icepdf.core.util.content.ContentParser");
    }

    @Override // org.icepdf.core.util.content.AbstractContentParser, org.icepdf.core.util.content.ContentParser
    public Shapes parseTextBlocks(byte[][] source) throws UnsupportedEncodingException {
        Parser parser = new Parser(new ByteDoubleArrayInputStream(source));
        Shapes shapes = new Shapes();
        if (this.graphicState == null) {
            this.graphicState = new GraphicsState(shapes);
        }
        try {
            Stack<Object> stack = new Stack<>();
            double yBTstart = 0.0d;
            for (Object tok = parser.getStreamObject(); tok != null; tok = parser.getStreamObject()) {
                if (tok instanceof String) {
                    if (tok.equals(PdfOps.BT_TOKEN)) {
                        yBTstart = parseText(parser, shapes, yBTstart);
                        stack.clear();
                    } else if (tok.equals(PdfOps.Tf_TOKEN)) {
                        consume_Tf(this.graphicState, stack, this.resources);
                        stack.clear();
                    } else if (tok.equals(PdfOps.Do_TOKEN)) {
                        consume_Do(this.graphicState, stack, shapes, this.resources, false, new AtomicInteger(0), null);
                        stack.clear();
                    }
                } else {
                    stack.push(tok);
                }
            }
            stack.clear();
        } catch (IOException e2) {
            logger.finer("End of Content Stream");
        }
        shapes.contract();
        return shapes;
    }

    float parseText(Parser parser, Shapes shapes, double previousBTStart) throws IOException {
        this.inTextBlock = true;
        TextMetrics textMetrics = new TextMetrics();
        this.textBlockBase = new AffineTransform(this.graphicState.getCTM());
        this.graphicState.getTextState().tmatrix = new AffineTransform();
        this.graphicState.getTextState().tlmatrix = new AffineTransform();
        this.graphicState.scale(1.0d, -1.0d);
        PageText pageText = shapes.getPageText();
        GlyphOutlineClip glyphOutlineClip = new GlyphOutlineClip();
        Object streamObject = parser.getStreamObject();
        while (true) {
            Object nextToken = streamObject;
            if (nextToken.equals(PdfOps.ET_TOKEN)) {
                break;
            }
            if (nextToken instanceof String) {
                if (nextToken.equals(PdfOps.Tj_TOKEN)) {
                    consume_Tj(this.graphicState, this.stack, shapes, textMetrics, glyphOutlineClip, this.oCGs);
                } else if (nextToken.equals(PdfOps.Tc_TOKEN)) {
                    consume_Tc(this.graphicState, this.stack);
                } else if (nextToken.equals(PdfOps.Tw_TOKEN)) {
                    consume_Tw(this.graphicState, this.stack);
                } else if (nextToken.equals(PdfOps.Td_TOKEN)) {
                    consume_Td(this.graphicState, this.stack, textMetrics, pageText, previousBTStart, this.oCGs);
                } else if (nextToken.equals(PdfOps.Tm_TOKEN)) {
                    consume_tm(this.graphicState, this.stack, textMetrics, pageText, previousBTStart, this.textBlockBase, this.oCGs);
                } else if (nextToken.equals(PdfOps.Tf_TOKEN)) {
                    consume_Tf(this.graphicState, this.stack, this.resources);
                } else if (nextToken.equals(PdfOps.TJ_TOKEN)) {
                    consume_TJ(this.graphicState, this.stack, shapes, textMetrics, glyphOutlineClip, this.oCGs);
                } else if (nextToken.equals(PdfOps.TD_TOKEN)) {
                    consume_TD(this.graphicState, this.stack, textMetrics, pageText, this.oCGs);
                } else if (nextToken.equals(PdfOps.TL_TOKEN)) {
                    consume_TL(this.graphicState, this.stack);
                } else if (nextToken.equals(PdfOps.q_TOKEN)) {
                    this.graphicState = consume_q(this.graphicState);
                } else if (nextToken.equals("Q")) {
                    this.graphicState = consume_Q(this.graphicState, shapes);
                } else if (nextToken.equals(PdfOps.cm_TOKEN)) {
                    consume_cm(this.graphicState, this.stack, this.inTextBlock, this.textBlockBase);
                } else if (nextToken.equals(PdfOps.T_STAR_TOKEN)) {
                    consume_T_star(this.graphicState, textMetrics, pageText, this.oCGs);
                } else if (nextToken.equals(PdfOps.BDC_TOKEN)) {
                    consume_BDC(this.stack, shapes, this.oCGs, this.resources);
                } else if (nextToken.equals(PdfOps.EMC_TOKEN)) {
                    consume_EMC(shapes, this.oCGs);
                } else if (nextToken.equals(PdfOps.gs_TOKEN)) {
                    consume_gs(this.graphicState, this.stack, this.resources);
                } else if (nextToken.equals(PdfOps.w_TOKEN) || nextToken.equals(PdfOps.LW_TOKEN)) {
                    consume_w(this.graphicState, this.stack, shapes, this.glyph2UserSpaceScale);
                } else if (nextToken.equals(PdfOps.sc_TOKEN)) {
                    consume_sc(this.graphicState, this.stack, this.library, this.resources, false);
                } else if (nextToken.equals(PdfOps.scn_TOKEN)) {
                    consume_sc(this.graphicState, this.stack, this.library, this.resources, true);
                } else if (nextToken.equals(PdfOps.k_TOKEN)) {
                    consume_k(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals(PdfOps.g_TOKEN)) {
                    consume_g(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals(PdfOps.i_TOKEN)) {
                    consume_i(this.stack);
                } else if (nextToken.equals(PdfOps.M_TOKEN)) {
                    consume_M(this.graphicState, this.stack, shapes);
                } else if (nextToken.equals("J")) {
                    consume_J(this.graphicState, this.stack, shapes);
                } else if (nextToken.equals(PdfOps.rg_TOKEN)) {
                    consume_rg(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals(PdfOps.d_TOKEN)) {
                    consume_d(this.graphicState, this.stack, shapes);
                } else if (nextToken.equals(PdfOps.j_TOKEN)) {
                    consume_j(this.graphicState, this.stack, shapes);
                } else if (nextToken.equals(PdfOps.cs_TOKEN)) {
                    consume_cs(this.graphicState, this.stack, this.resources);
                } else if (nextToken.equals(PdfOps.ri_TOKEN)) {
                    consume_ri(this.stack);
                } else if (nextToken.equals(PdfOps.SC_TOKEN)) {
                    consume_SC(this.graphicState, this.stack, this.library, this.resources, false);
                } else if (nextToken.equals(PdfOps.SCN_TOKEN)) {
                    consume_SC(this.graphicState, this.stack, this.library, this.resources, true);
                } else if (nextToken.equals(PdfOps.K_TOKEN)) {
                    consume_K(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals("G")) {
                    consume_G(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals(PdfOps.RG_TOKEN)) {
                    consume_RG(this.graphicState, this.stack, this.library);
                } else if (nextToken.equals(PdfOps.CS_TOKEN)) {
                    consume_CS(this.graphicState, this.stack, this.resources);
                } else if (nextToken.equals(PdfOps.Tr_TOKEN)) {
                    consume_Tr(this.graphicState, this.stack);
                } else if (nextToken.equals(PdfOps.Tz_TOKEN)) {
                    consume_Tz(this.graphicState, this.stack);
                } else if (nextToken.equals(PdfOps.Ts_TOKEN)) {
                    consume_Ts(this.graphicState, this.stack);
                } else if (!nextToken.equals(PdfOps.BX_TOKEN) && !nextToken.equals(PdfOps.EX_TOKEN)) {
                    if (nextToken.equals(PdfOps.SINGLE_QUOTE_TOKEN)) {
                        consume_single_quote(this.graphicState, this.stack, shapes, textMetrics, glyphOutlineClip, this.oCGs);
                    } else if (nextToken.equals(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                        consume_double_quote(this.graphicState, this.stack, shapes, textMetrics, glyphOutlineClip, this.oCGs);
                    }
                }
            } else {
                this.stack.push(nextToken);
            }
            streamObject = parser.getStreamObject();
        }
        if (!glyphOutlineClip.isEmpty()) {
            this.graphicState.setClip(glyphOutlineClip.getGlyphOutlineClip());
            shapes.add(new GlyphOutlineDrawCmd(glyphOutlineClip));
        }
        while (!this.stack.isEmpty()) {
            String tmp = this.stack.pop().toString();
            if (logger.isLoggable(Level.FINE)) {
                logger.warning("Text=" + tmp);
            }
        }
        this.graphicState.set(this.textBlockBase);
        this.inTextBlock = false;
        return textMetrics.getyBTStart();
    }

    private void parseInlineImage(Parser p2, Shapes shapes) throws IOException {
        try {
            HashMap<Object, Object> iih = new HashMap<>();
            Object tok = p2.getStreamObject();
            while (!tok.equals("ID")) {
                if (tok.equals(PdfOps.BPC_TOKEN)) {
                    tok = new Name(PdfOps.BPC_NAME);
                } else if (tok.equals(PdfOps.CS_TOKEN)) {
                    tok = new Name(PdfOps.CS_NAME);
                } else if (tok.equals(PdfOps.D_TOKEN)) {
                    tok = new Name(PdfOps.D_NAME);
                } else if (tok.equals(PdfOps.DP_TOKEN)) {
                    tok = new Name(PdfOps.DP_NAME);
                } else if (tok.equals(PdfOps.F_TOKEN)) {
                    tok = new Name(PdfOps.F_NAME);
                } else if (tok.equals(PdfOps.H_TOKEN)) {
                    tok = new Name("Height");
                } else if (tok.equals(PdfOps.IM_TOKEN)) {
                    tok = new Name(PdfOps.IM_NAME);
                } else if (tok.equals("I")) {
                    tok = new Name(PdfOps.I_NAME);
                } else if (tok.equals(PdfOps.W_TOKEN)) {
                    tok = new Name("Width");
                }
                Object tok1 = p2.getStreamObject();
                iih.put(tok, tok1);
                tok = p2.getStreamObject();
            }
            ByteArrayOutputStream buf = new ByteArrayOutputStream(4096);
            Object tok2 = p2.peek2();
            boolean ateEI = false;
            while (tok2 != null && !tok2.equals(" EI")) {
                ateEI = p2.readLineForInlineImage(buf);
                if (ateEI) {
                    break;
                } else {
                    tok2 = p2.peek2();
                }
            }
            if (!ateEI) {
                p2.getToken();
            }
            buf.flush();
            buf.close();
            byte[] data = buf.toByteArray();
            ImageStream st = new ImageStream(this.library, iih, data);
            ImageReference imageStreamReference = new InlineImageStreamReference(st, this.graphicState.getFillColor(), this.resources, 0, null);
            AffineTransform af2 = new AffineTransform(this.graphicState.getCTM());
            this.graphicState.scale(1.0d, -1.0d);
            this.graphicState.translate(0.0d, -1.0d);
            shapes.add(new ImageDrawCmd(imageStreamReference));
            this.graphicState.set(af2);
        } catch (IOException e2) {
            throw e2;
        } catch (Exception e3) {
            logger.log(Level.FINE, "Error parsing inline image.", (Throwable) e3);
        }
    }
}
