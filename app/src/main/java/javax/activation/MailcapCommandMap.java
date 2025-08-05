package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:javax/activation/MailcapCommandMap.class */
public class MailcapCommandMap extends CommandMap {
    private MailcapFile[] DB;
    private static final int PROG = 0;

    public MailcapCommandMap() {
        List dbv = new ArrayList(5);
        dbv.add(null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String user_home = System.getProperty("user.home");
            if (user_home != null) {
                String path = user_home + File.separator + ".mailcap";
                MailcapFile mf = loadFile(path);
                if (mf != null) {
                    dbv.add(mf);
                }
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            String system_mailcap = System.getProperty("java.home") + File.separator + "lib" + File.separator + "mailcap";
            MailcapFile mf2 = loadFile(system_mailcap);
            if (mf2 != null) {
                dbv.add(mf2);
            }
        } catch (SecurityException e3) {
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(dbv, "META-INF/mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        MailcapFile mf3 = loadResource("/META-INF/mailcap.default");
        if (mf3 != null) {
            dbv.add(mf3);
        }
        this.DB = new MailcapFile[dbv.size()];
        this.DB = (MailcapFile[]) dbv.toArray(this.DB);
    }

    private MailcapFile loadResource(String name) {
        InputStream clis = null;
        try {
            try {
                InputStream clis2 = SecuritySupport.getResourceAsStream(getClass(), name);
                if (clis2 != null) {
                    MailcapFile mf = new MailcapFile(clis2);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
                    }
                    if (clis2 != null) {
                        try {
                            clis2.close();
                        } catch (IOException e2) {
                        }
                    }
                    return mf;
                }
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
                }
                if (clis2 != null) {
                    try {
                        clis2.close();
                    } catch (IOException e3) {
                        return null;
                    }
                }
                return null;
            } catch (IOException e4) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: can't load " + name, e4);
                }
                if (0 != 0) {
                    try {
                        clis.close();
                    } catch (IOException e5) {
                        return null;
                    }
                }
                return null;
            } catch (SecurityException sex) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: can't load " + name, sex);
                }
                if (0 != 0) {
                    try {
                        clis.close();
                    } catch (IOException e6) {
                        return null;
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    clis.close();
                } catch (IOException e7) {
                    throw th;
                }
            }
            throw th;
        }
    }

    private void loadAllResources(List v2, String name) {
        boolean anyLoaded = false;
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            URL[] urls = cld != null ? SecuritySupport.getResources(cld, name) : SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: URL " + ((Object) url));
                    }
                    try {
                        try {
                            clis = SecuritySupport.openStream(url);
                            if (clis != null) {
                                v2.add(new MailcapFile(clis));
                                anyLoaded = true;
                                if (LogSupport.isLoggable()) {
                                    LogSupport.log("MailcapCommandMap: successfully loaded mailcap file from URL: " + ((Object) url));
                                }
                            } else if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: not loading mailcap file from URL: " + ((Object) url));
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e2) {
                                }
                            }
                        } catch (IOException ioex) {
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: can't load " + ((Object) url), ioex);
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e3) {
                                }
                            }
                        } catch (SecurityException sex) {
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: can't load " + ((Object) url), sex);
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e4) {
                                }
                            }
                        }
                    } catch (Throwable th) {
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e5) {
                                throw th;
                            }
                        }
                        throw th;
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, ex);
            }
        }
        if (anyLoaded) {
            return;
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: !anyLoaded");
        }
        MailcapFile mf = loadResource("/" + name);
        if (mf != null) {
            v2.add(mf);
        }
    }

    private MailcapFile loadFile(String name) {
        MailcapFile mtf = null;
        try {
            mtf = new MailcapFile(name);
        } catch (IOException e2) {
        }
        return mtf;
    }

    public MailcapCommandMap(String fileName) throws IOException {
        this();
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: load PROG from " + fileName);
        }
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile(fileName);
        }
    }

    public MailcapCommandMap(InputStream is) {
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                this.DB[0] = new MailcapFile(is);
            } catch (IOException e2) {
            }
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
        Map cmdMap;
        Map cmdMap2;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap2 = this.DB[i2].getMailcapList(mimeType)) != null) {
                appendPrefCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i3 = 0; i3 < this.DB.length; i3++) {
            if (this.DB[i3] != null && (cmdMap = this.DB[i3].getMailcapFallbackList(mimeType)) != null) {
                appendPrefCmdsToList(cmdMap, cmdList);
            }
        }
        CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
        return (CommandInfo[]) cmdList.toArray(cmdInfos);
    }

    private void appendPrefCmdsToList(Map cmdHash, List cmdList) {
        for (String verb : cmdHash.keySet()) {
            if (!checkForVerb(cmdList, verb)) {
                List cmdList2 = (List) cmdHash.get(verb);
                String className = (String) cmdList2.get(0);
                cmdList.add(new CommandInfo(verb, className));
            }
        }
    }

    private boolean checkForVerb(List cmdList, String verb) {
        Iterator ee = cmdList.iterator();
        while (ee.hasNext()) {
            String enum_verb = ((CommandInfo) ee.next()).getCommandName();
            if (enum_verb.equals(verb)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getAllCommands(String mimeType) {
        Map cmdMap;
        Map cmdMap2;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap2 = this.DB[i2].getMailcapList(mimeType)) != null) {
                appendCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i3 = 0; i3 < this.DB.length; i3++) {
            if (this.DB[i3] != null && (cmdMap = this.DB[i3].getMailcapFallbackList(mimeType)) != null) {
                appendCmdsToList(cmdMap, cmdList);
            }
        }
        CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
        return (CommandInfo[]) cmdList.toArray(cmdInfos);
    }

    private void appendCmdsToList(Map typeHash, List cmdList) {
        for (String verb : typeHash.keySet()) {
            List<String> cmdList2 = (List) typeHash.get(verb);
            for (String cmd : cmdList2) {
                cmdList.add(new CommandInfo(verb, cmd));
            }
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
        Map cmdMap;
        List v2;
        String cmdClassName;
        Map cmdMap2;
        List v3;
        String cmdClassName2;
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap2 = this.DB[i2].getMailcapList(mimeType)) != null && (v3 = (List) cmdMap2.get(cmdName)) != null && (cmdClassName2 = (String) v3.get(0)) != null) {
                return new CommandInfo(cmdName, cmdClassName2);
            }
        }
        for (int i3 = 0; i3 < this.DB.length; i3++) {
            if (this.DB[i3] != null && (cmdMap = this.DB[i3].getMailcapFallbackList(mimeType)) != null && (v2 = (List) cmdMap.get(cmdName)) != null && (cmdClassName = (String) v2.get(0)) != null) {
                return new CommandInfo(cmdName, cmdClassName);
            }
        }
        return null;
    }

    public synchronized void addMailcap(String mail_cap) {
        LogSupport.log("MailcapCommandMap: add to PROG");
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile();
        }
        this.DB[0].appendToMailcap(mail_cap);
    }

    @Override // javax.activation.CommandMap
    public synchronized DataContentHandler createDataContentHandler(String mimeType) {
        List v2;
        List v3;
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
        }
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search DB #" + i2);
                }
                Map cmdMap = this.DB[i2].getMailcapList(mimeType);
                if (cmdMap != null && (v3 = (List) cmdMap.get("content-handler")) != null) {
                    String name = (String) v3.get(0);
                    DataContentHandler dch = getDataContentHandler(name);
                    if (dch != null) {
                        return dch;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < this.DB.length; i3++) {
            if (this.DB[i3] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search fallback DB #" + i3);
                }
                Map cmdMap2 = this.DB[i3].getMailcapFallbackList(mimeType);
                if (cmdMap2 != null && (v2 = (List) cmdMap2.get("content-handler")) != null) {
                    String name2 = (String) v2.get(0);
                    DataContentHandler dch2 = getDataContentHandler(name2);
                    if (dch2 != null) {
                        return dch2;
                    }
                }
            }
        }
        return null;
    }

    private DataContentHandler getDataContentHandler(String name) {
        Class cl;
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("      class " + name);
        }
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            try {
                cl = cld.loadClass(name);
            } catch (Exception e2) {
                cl = Class.forName(name);
            }
            if (cl != null) {
                return (DataContentHandler) cl.newInstance();
            }
            return null;
        } catch (ClassNotFoundException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e3);
                return null;
            }
            return null;
        } catch (IllegalAccessException e4) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e4);
                return null;
            }
            return null;
        } catch (InstantiationException e5) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e5);
                return null;
            }
            return null;
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized String[] getMimeTypes() {
        String[] ts;
        List mtList = new ArrayList();
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (ts = this.DB[i2].getMimeTypes()) != null) {
                for (int j2 = 0; j2 < ts.length; j2++) {
                    if (!mtList.contains(ts[j2])) {
                        mtList.add(ts[j2]);
                    }
                }
            }
        }
        String[] mts = new String[mtList.size()];
        return (String[]) mtList.toArray(mts);
    }

    public synchronized String[] getNativeCommands(String mimeType) {
        String[] cmds;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmds = this.DB[i2].getNativeCommands(mimeType)) != null) {
                for (int j2 = 0; j2 < cmds.length; j2++) {
                    if (!cmdList.contains(cmds[j2])) {
                        cmdList.add(cmds[j2]);
                    }
                }
            }
        }
        return (String[]) cmdList.toArray(new String[cmdList.size()]);
    }
}
