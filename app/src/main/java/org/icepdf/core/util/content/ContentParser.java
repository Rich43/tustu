package org.icepdf.core.util.content;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.Shapes;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/content/ContentParser.class */
public interface ContentParser {
    Shapes getShapes();

    Stack<Object> getStack();

    GraphicsState getGraphicsState();

    void setGraphicsState(GraphicsState graphicsState);

    ContentParser parse(byte[][] bArr, Page page) throws InterruptedException, IOException;

    Shapes parseTextBlocks(byte[][] bArr) throws UnsupportedEncodingException;

    void setGlyph2UserSpaceScale(float f2);
}
