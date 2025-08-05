package javax.imageio;

import javax.imageio.metadata.IIOMetadata;

/* loaded from: rt.jar:javax/imageio/ImageTranscoder.class */
public interface ImageTranscoder {
    IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam);

    IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam);
}
