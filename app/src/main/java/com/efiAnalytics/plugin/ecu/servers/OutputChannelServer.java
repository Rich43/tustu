package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.OutputChannel;
import com.efiAnalytics.plugin.ecu.OutputChannelClient;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/OutputChannelServer.class */
public interface OutputChannelServer {
    void subscribe(String str, String str2, OutputChannelClient outputChannelClient) throws ControllerException;

    void unsubscribeConfiguration(String str);

    void unsubscribe(OutputChannelClient outputChannelClient);

    String[] getOutputChannels(String str) throws ControllerException;

    OutputChannel getOutputChannel(String str, String str2) throws ControllerException;
}
