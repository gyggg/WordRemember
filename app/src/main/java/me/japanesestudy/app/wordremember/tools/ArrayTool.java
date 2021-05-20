package me.japanesestudy.app.wordremember.tools;

import java.util.List;

/**
 * Created by guyu on 2018/1/24.
 */

public class ArrayTool {
    public static int[] getIntArray(List<Integer> integers) {
        int[] result = new int[integers.size()];
        for(int i = 0; i < integers.size(); i++)
            result[i] = integers.get(i);
        return result;
    }
    public static int[] getIntArray(Integer[] integers) {
        int[] result = new int[integers.length];
        for(int i = 0; i < integers.length; i++)
            result[i] = integers[i];
        return result;
    }
    public static Integer[] getIntegerArray(List<Integer> integers) {
        Integer []result = integers.toArray(new Integer[0]);
        return result;
    }
}
