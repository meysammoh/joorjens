package ir.joorjens.jmx;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.email.EmailSend;
import ir.joorjens.common.file.FileRW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Config implements ConfigMBean {

    //--------------------------------------------------------------------------------------
    public static final int TIME_MIN = 60;
    public static final int TIME_HOUR = 60 * TIME_MIN;
    public static final int TIME_DAY_SEC = 24 * TIME_HOUR;
    public static final int TIME_WEEK_SEC = 7 * TIME_DAY_SEC;
    public static final int TIME_MONTH_SEC = 31 * TIME_DAY_SEC;
    //--------------------------------------------------------------------------------------
    public static final int DISTRIBUTOR_SERIAL_LEN = 5;
    public static final int CART_SERIAL_LEN = 10;
    //--------------------------------------------------------------------------------------
    public static final MetricRegistry METRICS = new MetricRegistry();
    //--------------------------------------------------------------------------------------

    public static final char CHAR_SPLITTER = '#';
    public static final char ADDRESS_SPLITTER = '-';
    public static final String SERIAL_SPLITTER = "-";
    public static String API_PREFIX = setApiPrefix("/api/v1/");
    public static String API_HOST = "localhost";
    public static int API_PORT = setApiPort(9000);
    public static int API_THREAD_MIN = 2;
    public static int API_THREAD_MAX = 10;
    public static int API_TIMEOUT = 30 * 1000;
    public static int apiPaginationMax = 50;

    public static int hourNightFrom = 22; //todo load form db or is constant fo all zone :)
    public static int hourNightTo = 6;

    public static int bgMemoryDbSleep = 30 * 60 * 1000;
    public static int bgLogSleep = 5 * 60 * 1000;
    public static int bgLogListSizeMax = 1000;
    public static int BG_LOG_WORKER_SIZE = 4;
    public static int bgPositionSleep = 5 * 60 * 1000;
    public static int bgPositionListSizeMax = 1000;
    public static int BG_POSITION_WORKER_SIZE = 4;

    static int JMX_PORT = 21391;
    public final static int BATCH_LIMIT = 100;

    //--------------------------------------------------------------------------------------
    public static int setApiPort(int port) {
        API_PORT = port;
        return port;
    }

    private static String setApiPrefix(String tmp) {
        tmp = tmp.trim();
        if (tmp.charAt(0) != '/') {
            tmp = '/' + tmp;
        }
        if (tmp.charAt(tmp.length() - 1) != '/') {
            tmp = tmp + '/';
        }
        API_PREFIX = tmp;
        return API_PREFIX;
    }

    public static final String DEFAULT = "default_";

    public static String baseFolder = "/storage/";
    private static String baseFolderImage = "image_";
    public static String baseFolderAdvertising = baseFolderImage + "advertising/";
    public static String baseFolderBanner = baseFolderImage + "banner/";
    public static String baseFolderCustomer = baseFolderImage + "customer/";
    public static String baseFolderDistributor = baseFolderImage + "distributor/";
    public static String baseFolderDistributorPackage = baseFolderImage + "distributorPackage/";
    public static String baseFolderProduct = baseFolderImage + "product/";
    public static String baseFolderStore = baseFolderImage + "store/";
    public static String TEMPORARY_PREFIX = baseFolderImage + "temporary_";
    public static String DEFAULT_PROFILE_IMAGE_URL = baseFolderImage + Config.baseFolderCustomer + DEFAULT + "profile.png";

    //--------------------------------------------------------------------------------------

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String CONFIG_NAME_FILE = "config.properties";

    public static void loadConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = Config.class.getProtectionDomain().getClassLoader().getResourceAsStream(CONFIG_NAME_FILE);
            prop.load(input);

            String tmp = prop.getProperty("API_PREFIX");
            if (!Utility.isEmpty(tmp)) {
                setApiPrefix(tmp);
            }
            tmp = prop.getProperty("API_HOST");
            if (!Utility.isEmpty(tmp)) {
                API_HOST = tmp;
            }
            tmp = prop.getProperty("API_PORT");
            if (!Utility.isEmpty(tmp)) {
                setApiPort(Integer.parseInt(tmp.trim()));
            }
            tmp = prop.getProperty("API_THREAD_MIN");
            if (!Utility.isEmpty(tmp)) {
                API_THREAD_MIN = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("API_THREAD_MAX");
            if (!Utility.isEmpty(tmp)) {
                API_THREAD_MAX = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("API_TIMEOUT");
            if (!Utility.isEmpty(tmp)) {
                API_TIMEOUT = Integer.parseInt(tmp.trim()) * 1000;
            }
            tmp = prop.getProperty("apiPaginationMax");
            if (!Utility.isEmpty(tmp)) {
                apiPaginationMax = Integer.parseInt(tmp.trim());
            }

            tmp = prop.getProperty("hourNightFrom");
            if (!Utility.isEmpty(tmp)) {
                hourNightFrom = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("hourNightTo");
            if (!Utility.isEmpty(tmp)) {
                hourNightTo = Integer.parseInt(tmp.trim());
            }

            tmp = prop.getProperty("bgMemoryDbSleep");
            if (!Utility.isEmpty(tmp)) {
                bgMemoryDbSleep = Integer.parseInt(tmp.trim()) * 1000;
            }
            tmp = prop.getProperty("bgLogSleep");
            if (!Utility.isEmpty(tmp)) {
                bgLogSleep = Integer.parseInt(tmp.trim()) * 1000;
            }
            tmp = prop.getProperty("bgLogListSizeMax");
            if (!Utility.isEmpty(tmp)) {
                bgLogListSizeMax = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("BG_LOG_WORKER_SIZE");
            if (!Utility.isEmpty(tmp)) {
                BG_LOG_WORKER_SIZE = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("bgPositionSleep");
            if (!Utility.isEmpty(tmp)) {
                bgPositionSleep = Integer.parseInt(tmp.trim()) * 1000;
            }
            tmp = prop.getProperty("bgPositionListSizeMax");
            if (!Utility.isEmpty(tmp)) {
                bgPositionListSizeMax = Integer.parseInt(tmp.trim());
            }
            tmp = prop.getProperty("BG_POSITION_WORKER_SIZE");
            if (!Utility.isEmpty(tmp)) {
                BG_POSITION_WORKER_SIZE = Integer.parseInt(tmp.trim());
            }

            tmp = prop.getProperty("JMX_PORT");
            if (!Utility.isEmpty(tmp)) {
                JMX_PORT = Integer.parseInt(tmp.trim());
            }

            //-------------------------------------------------------------------------

            tmp = prop.getProperty("baseFolder");
            if (!Utility.isEmpty(tmp)) {
                baseFolder = tmp.trim();
                if (!baseFolder.endsWith("/")) {
                    baseFolder += '/';
                }
            }
            FileRW.mkdir(baseFolder + baseFolderAdvertising, false, true);
            FileRW.mkdir(baseFolder + baseFolderBanner, false, false);
            FileRW.mkdir(baseFolder + baseFolderCustomer, false, false);
            FileRW.mkdir(baseFolder + baseFolderDistributor, false, false);
            FileRW.mkdir(baseFolder + baseFolderDistributorPackage, false, false);
            FileRW.mkdir(baseFolder + baseFolderProduct, false, false);
            FileRW.mkdir(baseFolder + baseFolderStore, false, false);

            EmailSend.loadEmailInfo();
            //-------------------------------------------------------------------------
        } catch (IOException ex) {
            logger.error(String.format("Loading configFile was failed. Message: %s", ex.getMessage()));
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(String.format("Closing configFile was failed. Message: %s", e.getMessage()));
                }
            }
        }

        //----------------------------------------------------------------------
        final JmxReporter jmxReporter = JmxReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        jmxReporter.start();
		/*
		ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(METRICS)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.build();
		consoleReporter.start(10, TimeUnit.MINUTES);
		*/
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(METRICS)
                .outputTo(LoggerFactory.getLogger(Config.class))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.MINUTES);
        //----------------------------------------------------------------------

        ConfigManagement configManagement = new ConfigManagement();
        Thread conManThread = new Thread(configManagement, ConfigManagement.class.getSimpleName());
        conManThread.start();
    }

    //---------------------------------------------------------------------------
    @Override
    public String getApiPrefix() {
        return API_PREFIX;
    }

    @Override
    public String getApiHost() {
        return API_HOST;
    }

    @Override
    public int getApiPort() {
        return API_PORT;
    }

    @Override
    public int getApiThreadMin() {
        return API_THREAD_MIN;
    }

    @Override
    public int getApiThreadMax() {
        return API_THREAD_MAX;
    }

    @Override
    public int getApiTimeout() {
        return API_TIMEOUT;
    }

    @Override
    public int getApiPaginationMax() {
        return apiPaginationMax;
    }

    @Override
    public synchronized void setApiPaginationMax(int paginationMax) {
        Config.apiPaginationMax = paginationMax;
    }

    @Override
    public int getHourNightFrom() {
        return hourNightFrom;
    }

    @Override
    public synchronized void setHourNightFrom(int hourNightFrom) {
        Config.hourNightFrom = hourNightFrom;
    }

    @Override
    public int getHourNightTo() {
        return hourNightTo;
    }

    @Override
    public synchronized void setHourNightTo(int hourNightTo) {
        Config.hourNightTo = hourNightTo;
    }

    @Override
    public int getBgMemoryDbSleep() {
        return bgMemoryDbSleep;
    }

    @Override
    public synchronized void setBgMemoryDbSleep(int bgwMemoryDbSleep) {
        Config.bgMemoryDbSleep = bgwMemoryDbSleep;
    }

    @Override
    public int getBgLogSleep() {
        return bgLogSleep;
    }

    @Override
    public synchronized void setBgLogSleep(int bgLogSleep) {
        Config.bgLogSleep = bgLogSleep;
    }

    @Override
    public int getBgLogListSizeMax() {
        return bgLogListSizeMax;
    }

    @Override
    public synchronized void setBgLogListSizeMax(int bgLogListSizeMax) {
        Config.bgLogListSizeMax = bgLogListSizeMax;
    }

    @Override
    public int getBgLogWorkerSize() {
        return BG_LOG_WORKER_SIZE;
    }

    @Override
    public int getBgPositionSleep() {
        return bgPositionSleep;
    }

    @Override
    public synchronized void setBgPositionSleep(int bgPositionSleep) {
        Config.bgPositionSleep = bgPositionSleep;
    }

    @Override
    public int getBgPositionListSizeMax() {
        return bgPositionListSizeMax;
    }

    @Override
    public synchronized void setBgPositionListSizeMax(int bgPositionListSizeMax) {
        Config.bgPositionListSizeMax = bgPositionListSizeMax;
    }

    @Override
    public int getBgPositionWorkerSize() {
        return BG_POSITION_WORKER_SIZE;
    }

    @Override
    public int getJmxPort() {
        return JMX_PORT;
    }

    //--------------------------------------------------------------------------------------

    @Override
    public String getBaseFolder() {
        return baseFolder;
    }

    @Override
    public synchronized void setBaseFolder(String baseFolder) {
        Config.baseFolder = baseFolder;
    }

}