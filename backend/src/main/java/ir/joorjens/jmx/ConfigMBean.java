package ir.joorjens.jmx;

/**
 * Created by dev on 12/12/16.
 */
public interface ConfigMBean {

    String getApiPrefix();

    String getApiHost();

    int getApiPort();

    int getApiThreadMin();

    int getApiThreadMax();

    int getApiTimeout();

    int getApiPaginationMax();

    void setApiPaginationMax(int paginationMax);

    int getHourNightFrom();

    void setHourNightFrom(int hourNightFrom);

    int getHourNightTo();

    void setHourNightTo(int hourNightTo);

    int getBgMemoryDbSleep();

    void setBgMemoryDbSleep(int bgwMemoryDbSleep);

    int getBgLogSleep();

    void setBgLogSleep(int bgLogSleep);

    int getBgLogListSizeMax();

    void setBgLogListSizeMax(int bgLogListSizeMax);

    int getBgLogWorkerSize();

    int getBgPositionSleep();

    void setBgPositionSleep(int bgPositionSleep);

    int getBgPositionListSizeMax();

    void setBgPositionListSizeMax(int bgPositionListSizeMax);

    int getBgPositionWorkerSize();

    int getJmxPort();

    String getBaseFolder();

    void setBaseFolder(String baseFolder);

}