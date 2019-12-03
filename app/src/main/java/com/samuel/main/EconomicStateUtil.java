package com.samuel.main;

/**
 * 经济状态判断工具
 * Created by kimiffy on 2019/1/10.
 */
public class EconomicStateUtil {

    public static final int SPEED_RANGE_MAX = 13;
    public static final int ENERGY_SAVE = 0;//节能
    public static final int NORMAL_NO_ENERGY_SAVE = 1;//一般不节能
    public static final int HIGH_NO_ENERGY_SAVE = 2;//高度不节能
    @SuppressWarnings("unused")
    private static final int NORMAL_WEIGHT_STATE = 1;//基载状态
    private static final int LIGHT_WEIGHT_STATE = 2;//轻载状态
    private static final int HEAVY_WEIGHT_STATE = 3;//重载状态
    private static final double[] EnergySaveArr = new double[]{0.3, 0.5, 0.7, 0.7, 0.2, 0.4, 0.4, 0.6, 0.8, 0.8, 0.3, 0.4};
    private static final double[] noEnergySaveArr = new double[]{0.6, 0.7, 0.85, 0.85, 0.5, 0.7, 0.7, 0.8, 0.9, 0.9, 0.6, 0.7};

    /**
     * 根据条件判断是否需要改变 可变化值1
     *
     * @param type 当前工况类型
     * @return
     */
    private static double getChangeNum1(int type, double speed) {

        double change1 = 0;//可能发生改变的值 ⑴重载状态下，下坡路段各区间-5%，其他路段各区间+5%；轻载状态下，下坡路段各区间+5%，其他路段各区间-5%；

        int state = getCarWeightStat(speed);

        if (state == HEAVY_WEIGHT_STATE) {
            if (type == 4 || type == 10) {
                change1 = -0.05;
            } else {
                change1 = 0.05;
            }
        } else if (state == LIGHT_WEIGHT_STATE) {
            if (type == 4 || type == 10) {
                change1 = 0.05;
            } else {
                change1 = -0.05;
            }
        }
        return change1;
    }

    /**
     * 根据条件判断是否需要改变值 可变化值2
     *
     * @param type  当前工况类型
     * @param speed 当前车速
     * @return
     */
    private static double getChangeNum2(int type, double speed) {
        int speedRegion = getSpeedRegion(speed);
        int ecoSpeedRange = 8; //getEcoSpeedRange();
        double change2 = 0;// 可能发生改变的值 (2)在平路行驶时，如果当前车速低于经济车速区间，则各区间+10%；如果当前车速高于经济车速区间，则各区间-5%。
        if (type == 0 || type == 6) {
            if (speedRegion < ecoSpeedRange) {
                change2 = 0.1;
            } else if (speedRegion > ecoSpeedRange) {
                change2 = -0.05;
            }
        }
        return change2;
    }

    /**
     * 获取经济状态
     *
     * @param speed 当前车速
     * @return 0 节能  1 一般不节能  2 高度不节能
     */
    public static int getEconomicState(double speed, double probability) {
        int type = 0;//当前的道路工况类型
        // int speedRegion = getSpeedRegion(speed);//当前速度获取对应的速度区间
        // double probability = getRealFrRate(type, speedRegion);//本趟行车在对应路况类型下，对应速度区间的油耗概率

        double change1 = getChangeNum1(type,speed);
        double change2 = getChangeNum2(type, speed);
        int economicState;//经济状态
        if (0 <= probability && probability <= (EnergySaveArr[type] + change1 + change2)) {
            economicState = ENERGY_SAVE;
        } else if ((noEnergySaveArr[type] + change1 + change2) < probability) {
            economicState = HIGH_NO_ENERGY_SAVE;
        } else {
            economicState = NORMAL_NO_ENERGY_SAVE;
        }
        return economicState;
    }

    /**
     * 获取当前工况下 节能的判断值
     *
     * @param speed 当前车速
     * @return 判断值
     */
    public static double getEcoGreen(Double speed) {
        int type = 0;//当前的道路工况类型
        double change1 = getChangeNum1(type,speed);
        double change2 = getChangeNum2(type, speed);
        return EnergySaveArr[type] + change1 + change2;
    }

    /**
     * 获取当前工况下 不节能的判断值
     *
     * @param speed 当前车速
     * @return 判断值
     */
    public static double getEcoRed(Double speed) {
        int type = 0;//当前的道路工况类型
        double change1 = getChangeNum1(type,speed);
        double change2 = getChangeNum2(type, speed);
        return noEnergySaveArr[type] + change1 + change2;
    }

    /**
     * 根据当前速度获取对应的速度区间, 20以下的一个区间,20-30为一个区间, 30以上的每5公里为一个区间
     *
     * @return 对应的速度区间(取值范围0到SPEED_RANGE_MAX)
     */
    public static int getSpeedRegion(double speed) {
        if (speed < 20) {
            return 0;
        } else if (speed < 30) {
            return 1;
        } else if (speed > 85) {
            return SPEED_RANGE_MAX;
        }
        int temp = (int) Math.ceil(speed) - 20;
        return Math.min(SPEED_RANGE_MAX, (temp - 1) / 5);
    }

    /**
     * 获取载重状态  默认返回基载
     *
     * @param speed
     * @return 基载:CAR_WEIGHT_NORMAL
     * 轻载:CAR_WEIGHT_LIGHT
     * 重载:CAR_WEIGHT_DEEP
     */
    public static int getCarWeightStat(double speed) {
        int value = (int) (Math.random() * 3);
        if (value == 0) {
            value = 1;
        }
        return Math.min(3, value);
    }

}
