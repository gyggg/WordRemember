package me.japanesestudy.app.wordremember.tools;

import java.math.BigDecimal;

/**
 * Created by guyu on 2018/1/18.
 */

public class NumberTool {
    public static double saveNumberPoint(double src, int savePoint) {
        BigDecimal b = new BigDecimal(src);
        double res = b.setScale(savePoint, BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }
}
