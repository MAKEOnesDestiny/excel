package com.zhou.demo.excel.test;

import java.util.Arrays;

public class Test {

    //两个有序数据，合并一个有序数据
    //时间复杂度第一
    public static class Problem {

        public int[] merge(int[] a, int[] b) {
            if(a==null || b== null){
                return new int[]{};
            }
            int aLen = a.length;
            int bLen = b.length;
            int aSmall = 0;
            int bSmall = 0;
            int[] res = new int[aLen + bLen];
            int i = 0;
            while (aSmall < aLen && bSmall < bLen) {
                if (a[aSmall] <= b[bSmall]) {
                    res[i++] = a[aSmall++];
                } else {
                    res[i++] = b[bSmall++];
                }
            }
            if (aSmall < aLen) {
                while (aSmall < aLen) {
                    res[i++] = a[aSmall++];
                }
            }
            if (bSmall < bLen) {
                while (bSmall < bLen) {
                    res[i++] = b[bSmall++];
                }
            }
            return res;
        }

    }

    public static void main(String[] args) {
        //        int[] a = new int[]{1, 2, 3};
//        int[] a = new int[]{1, 2, 3, 6};
        int[] a = new int[]{1,2};
//        int[] b = new int[]{4, 5, 7};
        int[] b = new int[]{4};
        int[] res = new Problem().merge(a, b);
        Arrays.stream(res).forEach((t)->{
            System.out.println(t);
        });
    }

}
