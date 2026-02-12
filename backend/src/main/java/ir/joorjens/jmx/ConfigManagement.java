package ir.joorjens.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

class ConfigManagement implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManagement.class);

    private enum JMX_STATUS {SUCCESS, FAILED}

    private static JMX_STATUS loadJMXRemoteAgent(MBeanServer mbs, String name, int port, int countCall) {
        try {
            LocateRegistry.createRegistry(port);
            Map<String, Object> env = new HashMap<>();
            env.put("-Dcom.sun.management.jmxremote", "true");
            env.put("-Dcom.sun.management.jmxremote.port", port);
            env.put("-Dcom.sun.management.jmxremote.authenticate", "false");
            env.put("-Dcom.sun.management.jmxremote.ssl", "false");
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);

            // Start the RMI connector server.
            cs.start();
            logger.info(String.format("JMX: %s was successfully loaded on port(%d).", name, port));
            Config.JMX_PORT = port;
            return JMX_STATUS.SUCCESS;
        } catch (IOException e) {
            if (countCall < 20 && e.getMessage().toLowerCase().contains(" already in use")) {
                return loadJMXRemoteAgent(mbs, name, port + 1, countCall + 1);
            } else {
                logger.error(String.format("@loadJMXRemoteAgent: %s on port(%d). Message: %s", name, port, e.getMessage()));
            }
            return JMX_STATUS.FAILED;
        }
    }

    @Override
    public void run() {
        final String NAME = Config.class.getPackage().getName();
        final String TYPE = NAME + ":util=" + Config.class.getSimpleName();
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            loadJMXRemoteAgent(mbs, NAME, Config.JMX_PORT, 0);
            ObjectName name = new ObjectName(TYPE);
            Config mBean = new Config();
            mbs.registerMBean(mBean, name);
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            logger.error(String.format("Exception@run. Message: %s", e.getMessage()));
        }
    }

}
