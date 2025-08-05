package com.efiAnalytics.plugin.ecu;

import com.efiAnalytics.plugin.ecu.servers.BurnExecutor;
import com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer;
import com.efiAnalytics.plugin.ecu.servers.EcuConfigurationNameServer;
import com.efiAnalytics.plugin.ecu.servers.MathExpressionEvaluator;
import com.efiAnalytics.plugin.ecu.servers.OutputChannelServer;
import com.efiAnalytics.plugin.ecu.servers.UiSettingServer;
import com.efiAnalytics.plugin.ecu.servers.UiSettingServerProvider;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/ControllerAccess.class */
public class ControllerAccess {
    private static ControllerAccess me = null;
    private OutputChannelServer outputChannelServer = null;
    private ControllerParameterServer controllerParameterServer = null;
    private EcuConfigurationNameServer configurationNameProvider = null;
    private UiSettingServerProvider uiComponentServer = null;
    private BurnExecutor burnExecutor = null;
    private MathExpressionEvaluator mathExpressionEvaluator = null;

    private ControllerAccess() {
    }

    public static void initialize(OutputChannelServer outputServer, ControllerParameterServer parameterServer) {
        me = new ControllerAccess();
        me.outputChannelServer = outputServer;
        me.controllerParameterServer = parameterServer;
    }

    public static ControllerAccess getInstance() {
        return me;
    }

    public OutputChannelServer getOutputChannelServer() {
        return this.outputChannelServer;
    }

    public ControllerParameterServer getControllerParameterServer() {
        return this.controllerParameterServer;
    }

    public String[] getEcuConfigurationNames() {
        return this.configurationNameProvider.getAllConfigurationNames();
    }

    public void setConfigurationNameProvider(EcuConfigurationNameServer configurationNameProvider) {
        this.configurationNameProvider = configurationNameProvider;
    }

    public double evaluateExpression(String configurationName, String expression) throws MathException {
        if (this.mathExpressionEvaluator == null) {
            throw new MathException("Math Parsing Engine not initialized");
        }
        return this.mathExpressionEvaluator.evaluateExpression(configurationName, expression);
    }

    public void sendBurnCommand(String configurationName) throws ControllerException {
        if (this.burnExecutor == null) {
            throw new ControllerException("BurnExecutor not initialized.");
        }
        this.burnExecutor.burnData(configurationName);
    }

    public void setMathExpressionEvaluator(MathExpressionEvaluator mathExpressionEvaluator) {
        this.mathExpressionEvaluator = mathExpressionEvaluator;
    }

    public UiSettingServer getUiComponentServer(String configurationName) throws ControllerException {
        return this.uiComponentServer.getUiComponentServer(configurationName);
    }

    public void setUiComponentServerProvider(UiSettingServerProvider uiComponentServer) {
        this.uiComponentServer = uiComponentServer;
    }

    public void setBurnExecutor(BurnExecutor burnExecutor) {
        this.burnExecutor = burnExecutor;
    }
}
