package com.sun.prism.impl.ps;

import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.BasicEllipseRep;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.ps.ShaderFactory;
import com.sun.prism.shape.ShapeRep;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderFactory.class */
public abstract class BaseShaderFactory extends BaseResourceFactory implements ShaderFactory {
    public BaseShaderFactory() {
    }

    public BaseShaderFactory(Map<Image, Texture> clampTexCache, Map<Image, Texture> repeatTexCache, Map<Image, Texture> mipmapTexCache) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createPathRep() {
        return PrismSettings.cacheComplexShapes ? new CachingShapeRep() : new BasicShapeRep();
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createRoundRectRep() {
        return PrismSettings.cacheSimpleShapes ? new CachingRoundRectRep() : new BasicRoundRectRep();
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createEllipseRep() {
        return PrismSettings.cacheSimpleShapes ? new CachingEllipseRep() : new BasicEllipseRep();
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createArcRep() {
        return PrismSettings.cacheComplexShapes ? new CachingShapeRep() : new BasicShapeRep();
    }
}
