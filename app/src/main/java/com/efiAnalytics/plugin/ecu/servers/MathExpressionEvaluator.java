package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.MathException;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/MathExpressionEvaluator.class */
public interface MathExpressionEvaluator {
    double evaluateExpression(String str, String str2) throws MathException;
}
