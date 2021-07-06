package io.jhxstudio;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jhx
 * @date 2020/11/3
 **/
public class TestKs {

    public static void main(String[] args) {

    }

    public int[] twoNum(int[] nums, int target) {
        List<Integer> tmp = new ArrayList<>();
        for (int i = 0;i < nums.length;i++) {
            for (int j = 0; j < nums.length; j++) {
                if (tmp.contains(i) || tmp.contains(j)) {
                    continue;
                }
                if (i == j) {
                    continue;
                }
                if (nums[i] + nums[j] == target) {
                    tmp.add(i);
                    tmp.add(j);
                }
            }

        }

        return tmp.stream().mapToInt(Integer::valueOf).toArray();
    }
}
