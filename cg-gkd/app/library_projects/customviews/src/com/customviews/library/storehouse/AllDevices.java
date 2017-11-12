package com.customviews.library.storehouse;

/**
 * Created by rashed on 4/22/16.
 */
public enum AllDevices {

    /*
    *
    *  for (AllDevices device : AllDevices.values()) {
            Log.d("Device info: ", device.toString());
        }
    *
    *
    * */

    /*
    * System path
    *
    * /sys/class/power_supply/battery/current_now
    *
    * */

    DEVICE_76("DEVICE_76","LGE", "L-01E", true,"/sys/class/power_supply/battery/current_now"),
    DEVICE_75("DEVICE_75","SHARP", "SH-06E", true,"/sys/class/power_supply/battery/current_now"),//AQUOS
    DEVICE_89("DEVICE_89","Sony", "D6503", true,"/sys/class/power_supply/battery/current_now"),//EXPERIA Z2
    DEVICE_95("DEVICE_95","SHARP", "SH-03G", true,"/sys/class/power_supply/battery/current_now"),//AQUOS
    DEVICE_33("DEVICE_33","Sony", "SO-04D", true,"/sys/class/power_supply/battery/current_now"),//Sony
    DEVICE_LG_MARSH_MALOW("DEVICE_LG_MARSH_MALOW","LGE", "Nexus 5", true,"/sys/class/power_supply/battery/current_now"),//Sony
    /*
    * System path
    *
    * /sys/class/power_supply/battery/batt_current_adc
    *
    * */

    DEVICE_60("DEVICE_60","samsung", "SC-06D", true,"/sys/class/power_supply/battery/batt_current_adc"),//Samsung

    /*
    * System path
    *
    * /sys/class/power_supply/battery/batt_current_now
    *
    * */

    DEVICE_41("DEVICE_41","samsung", "SC-02C", true,"/sys/class/power_supply/battery/batt_current_now"),//Samsung

    /*
    * System path
    *
    * /sys/class/power_supply/battery/batt_attr_text
    *
    * */

    DEVICE_87("DEVICE_87","HTC", "HTL22", true,"/sys/class/power_supply/battery/batt_attr_text"),//Htc

    /*
    * System path
    *
    * /sys/class/power_supply/bq27520/current_now
    *
    * */

    DEVICE_25("DEVICE_25","Sony Ericsson", "IS11S", true,"/sys/class/power_supply/bq27520/current_now"),//Sony Ericsson Experia
    DEVICE_58("DEVICE_58","Sony Ericsson", "IS12S", true,"/sys/class/power_supply/bq27520/current_now"),//Sony Ericsson Experia

    /*
       * System path
       *
       * Could not find measurement interface path
       *
       * */
    DEVICE_94("DEVICE_94","FUJITSU", "F-04G", false,"Could not find measurement interface path"),//Fujitsu arrow
    DEVICE_79("DEVICE_79","samsung", "Galaxy Nexus", false,"Could not find measurement interface path"),//Samsung Galaxy Nexus
    DEVICE_05("DEVICE_05","samsung", "Galaxy Nexus", false,"Could not find measurement interface path"),//Samsung Galaxy Nexus
    ;

    private String number="" ;
    private String name="" ;
    private String model="" ;
    private boolean isReadable=false ;
    private String systemPath="";

    private AllDevices(String deviceNumber,String deviceName, String deviceModel, boolean isDeviceReadable,String devicePath) {
        number=deviceNumber;
        name = deviceName;
        model = deviceModel;
        isReadable = isDeviceReadable;
        systemPath=devicePath;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public void setIsReadable(boolean isReadable) {
        this.isReadable = isReadable;
    }

    public String getSystemPath() {
        return systemPath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }

    @Override
    public String toString() {
        return "AllDevices{" +
                "number='" + getNumber() + '\'' +
                ", name='" + getName() + '\'' +
                ", model='" + getModel() + '\'' +
                ", isReadable=" + isReadable() +
                ", systemPath='" + getSystemPath() + '\'' +
                '}';
    }
}
