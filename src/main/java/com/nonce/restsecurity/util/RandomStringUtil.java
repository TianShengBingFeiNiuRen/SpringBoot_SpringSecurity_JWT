package com.nonce.restsecurity.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 生成字符串 随机数字字母符号Util
 */
public class RandomStringUtil {

    public static String generateID() {
        return System.currentTimeMillis() + numGenerate(6);
    }

    public static String numGenerate(int length) {
        length = Math.max(length, 3);
        return String.valueOf((int) ((Math.random() * 9 + 1) * (Math.pow(10, length - 1))));
    }

    public static String stringGenerate(int length, boolean number, boolean letter, boolean symbol) {
        length = Math.max(length, 3);
        int divisor = ((number ? 1 : 0) + (letter ? 1 : 0) + (symbol ? 1 : 0));
        divisor = divisor == 0 ? 1 : divisor;
        int average = length / divisor;
        boolean complete = length % divisor == 0;
        StringBuilder sb = new StringBuilder();
        if (number) {
            sb.append(stringGenerate("0123456789", average));
        }
        if (letter) {
            sb.append(stringGenerate("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", average = complete ? average : average + 1));
        }
        if (symbol) {
            sb.append(stringGenerate("./*~!@#$%^&?-_=+[]{}", average));
        }
        List<String> stringList = Arrays.asList(sb.toString().substring(0, length).split(""));
        Collections.shuffle(stringList);
        sb = new StringBuilder();
        for (String s : stringList) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static String stringGenerate(String str, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
