package com.buis.common.utils.coding;

/**
 * Base32 编码工具（大写字母，区分大小写）
 * 字符集排除易混淆字符：0,1,O,I
 */
public class Base32 {
    // 大写Base32字符集（共32个字符，排除0/1/O/I）
    private static final String BASE32_CHARS = "6CSB7H8DAKXZF3N95RTMVUQG2YE4JWPL";

    /**
     * 将long值编码为Base32字符串（固定长度8位）
     */
    public static String encode40Bit(long value) {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int) (value & 0b11111); // 取最后5位
            sb.append(BASE32_CHARS.charAt(index));
            value >>>= 5; // 右移5位
        }
        return sb.reverse().toString(); // 反转得到正确顺序
    }

    /**
     * 将8位Base32字符串解码为long值（严格区分大小写）
     */
    public static long decode40Bit(String code) {
        if (code == null || code.length() != 8) {
            throw new IllegalArgumentException("Invalid Base32 code length");
        }

        long result = 0;
        for (int i = 0; i < 8; i++) {
            char c = code.charAt(i);
            int index = BASE32_CHARS.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }
            result = (result << 5) | index; // 左移5位后合并新值
        }
        return result;
    }

    // ---------------------- 其他工具方法（按需保留） ----------------------
    /**
     * 通用long转Base32（长度不固定）
     */
    public static String encode(long value) {
        if (value == 0) return String.valueOf(BASE32_CHARS.charAt(0));
        StringBuilder sb = new StringBuilder();
        while (value != 0) {
            int index = (int) (value & 0b11111);
            sb.append(BASE32_CHARS.charAt(index));
            value >>>= 5;
        }
        return sb.reverse().toString();
    }

    /**
     * 通用Base32转long（区分大小写）
     */
    public static long decode(String code) {
        long result = 0;
        for (char c : code.toCharArray()) {
            int index = BASE32_CHARS.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }
            result = (result << 5) | index;
        }
        return result;
    }
}