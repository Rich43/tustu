package com.sun.xml.internal.org.jvnet.fastinfoset;

import java.io.OutputStream;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/FastInfosetSerializer.class */
public interface FastInfosetSerializer {
    public static final String IGNORE_DTD_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/DTD";
    public static final String IGNORE_COMMENTS_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/comments";
    public static final String IGNORE_PROCESSING_INSTRUCTIONS_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/processingInstructions";
    public static final String IGNORE_WHITE_SPACE_TEXT_CONTENT_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/whiteSpaceTextContent";
    public static final String BUFFER_SIZE_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/buffer-size";
    public static final String REGISTERED_ENCODING_ALGORITHMS_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms";
    public static final String EXTERNAL_VOCABULARIES_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/external-vocabularies";
    public static final int MIN_CHARACTER_CONTENT_CHUNK_SIZE = 0;
    public static final int MAX_CHARACTER_CONTENT_CHUNK_SIZE = 32;
    public static final int CHARACTER_CONTENT_CHUNK_MAP_MEMORY_CONSTRAINT = Integer.MAX_VALUE;
    public static final int MIN_ATTRIBUTE_VALUE_SIZE = 0;
    public static final int MAX_ATTRIBUTE_VALUE_SIZE = 32;
    public static final int ATTRIBUTE_VALUE_MAP_MEMORY_CONSTRAINT = Integer.MAX_VALUE;
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16BE = "UTF-16BE";

    void setIgnoreDTD(boolean z2);

    boolean getIgnoreDTD();

    void setIgnoreComments(boolean z2);

    boolean getIgnoreComments();

    void setIgnoreProcesingInstructions(boolean z2);

    boolean getIgnoreProcesingInstructions();

    void setIgnoreWhiteSpaceTextContent(boolean z2);

    boolean getIgnoreWhiteSpaceTextContent();

    void setCharacterEncodingScheme(String str);

    String getCharacterEncodingScheme();

    void setRegisteredEncodingAlgorithms(Map map);

    Map getRegisteredEncodingAlgorithms();

    int getMinCharacterContentChunkSize();

    void setMinCharacterContentChunkSize(int i2);

    int getMaxCharacterContentChunkSize();

    void setMaxCharacterContentChunkSize(int i2);

    int getCharacterContentChunkMapMemoryLimit();

    void setCharacterContentChunkMapMemoryLimit(int i2);

    int getMinAttributeValueSize();

    void setMinAttributeValueSize(int i2);

    int getMaxAttributeValueSize();

    void setMaxAttributeValueSize(int i2);

    int getAttributeValueMapMemoryLimit();

    void setAttributeValueMapMemoryLimit(int i2);

    void setExternalVocabulary(ExternalVocabulary externalVocabulary);

    void setVocabularyApplicationData(VocabularyApplicationData vocabularyApplicationData);

    VocabularyApplicationData getVocabularyApplicationData();

    void reset();

    void setOutputStream(OutputStream outputStream);
}
