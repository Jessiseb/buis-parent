package com.buis.common.utils.coding;

import com.buis.common.constant.RegexConstants;
import com.buis.common.exception.ServiceException;

/**
 * <h1 style='font-weight:500'>1.兑换码算法说明：</h1>
 * <p>兑换码分为明文和密文，明文是40位二进制数，密文是长度为8的Base32编码的字符串 </p>
 * <h1 style='font-weight:500'>2.兑换码的明文结构：</h1>
 * <p style='padding: 0 15px'>8(校验码) + 4 (新鲜值) + 28(序列号) </p>
 *   <ul style='padding: 0 15px'>
 *       <li>序列号：一个单调递增的数字，可以通过Redis来生成</li>
 *       <li>新鲜值：可以是优惠券id的最后4位，同一张优惠券的兑换码就会有一个相同标记</li>
 *       <li>载荷：将新鲜值（4位）拼接序列号（28位）得到载荷</li>
 *       <li>校验码：将载荷4位一组，每组乘以加权数，最后累加求和，然后对2^8求余得到</li>
 *   </ul>
 */
public class CodeUtil {
    /**
     * 异或密钥表，用于最后的数据混淆
     */
    private final static long[] XOR_TABLE = {
            45139281L, 61261925L, 58169127L, 27031786L,
            64169927L, 46169126L, 32731286L, 52082227L,
            59169127L, 36169126L, 52082200L, 61261925L,
            32731286L, 27031786L, 56169127L, 34111865L,
            52082216L, 61261925L, 56169127L, 45139282L,
            32731286L, 64169927L, 41390251L, 59169127L,
            64169927L, 55139282L, 34111864L, 46169127L,
            58169127L, 61261925L, 36169126L, 64169927L,
    };
    /**
     * fresh值的偏移位数
     */
    private final static int FRESH_BIT_OFFSET = 28;
    /**
     * 校验码的偏移位数
     */
    private final static int CHECK_CODE_BIT_OFFSET = 32;
    /**
     * fresh值的掩码，4位
     */
    private final static int FRESH_MASK = 0xF;
    /**
     * 验证码的掩码，8位
     */
    private final static int CHECK_CODE_MASK = 0xFF;
    /**
     * 载荷的掩码，32位
     */
    private final static long PAYLOAD_MASK = 0xFFFFFFFFL;
    /**
     * 序列号掩码，28位
     */
    private final static long SERIAL_NUM_MASK = 0xFFFFFFFL;
    /**
     * 序列号加权运算的秘钥表
     */
    private final static int[][] PRIME_TABLE = {
            {23, 59, 241, 61, 607, 67, 977},
            {79, 83, 107, 439, 313, 619, 911},
            {173, 211, 499, 673, 823, 941, 1039},
            {31, 293, 311, 349, 431, 577, 757},
            {353, 23, 367, 499, 599, 661, 719},
            {103, 179, 353, 467, 577, 691, 811},
            {213, 439, 257, 313, 571, 619, 743},
            {31, 151, 241, 349, 607, 677, 769},
            {61, 83, 109, 137, 151, 521, 701},
            {23, 61, 199, 223, 479, 647, 739},
            {31, 109, 311, 467, 613, 743, 821},
            {41, 173, 367, 401, 569, 683, 761},
            {127, 283, 467, 577, 661, 773, 881},
            {59, 137, 257, 347, 439, 547, 641},
            {61, 199, 313, 421, 613, 739, 827},
            {19, 127, 241, 353, 499, 607, 811}
    };

    /**
     * 生成8位兑换码
     *
     * @param serialNum 递增序列号
     * @param fresh 新鲜值
     * @return 8位兑换码
     */
    public static String generateCode(long serialNum, long fresh) {
        // 1.计算新鲜值
        fresh = fresh & FRESH_MASK;
        // 2.拼接payload，fresh（4位） + serialNum（28位）
        long payload = fresh << FRESH_BIT_OFFSET | (serialNum & SERIAL_NUM_MASK);
        // 3.计算验证码
        long checkCode = calcCheckCode(payload, (int) fresh);
        // 4.payload做大质数异或运算，混淆数据
        payload ^= XOR_TABLE[(int) (checkCode & 0b1111)];
        // 5.拼接兑换码明文: 校验码（8位） + payload（32位）
        long code = checkCode << CHECK_CODE_BIT_OFFSET | payload;
        // 6.转码为8位Base32
        return Base32.encode40Bit(code);
    }

    private static long calcCheckCode(long payload, int fresh) {
        // 1.获取码表
        int[] table = PRIME_TABLE[fresh];
        // 2.生成校验码，payload每4位乘加权数，求和，取最后8位结果
        long sum = 0;
        int index = 0;
        while (payload > 0) {
            sum += (payload & 0xf) * table[index++];
            payload >>>= 4;
        }
        return sum & CHECK_CODE_MASK;
    }

    public static long parseCode(String code) {
        if (code == null || !code.matches(RegexConstants.TEAM_CODE_PATTERN)) {
            // 兑换码格式错误
            throw new ServiceException("无效项目编码");
        }
        // 1.Base32解码
        long num = Base32.decode40Bit(code);
        // 2.获取低32位，payload
        long payload = num & PAYLOAD_MASK;
        // 3.获取高8位，校验码
        int checkCode = (int) (num >>> CHECK_CODE_BIT_OFFSET);
        // 4.载荷异或大质数，解析出原来的payload
        payload ^= XOR_TABLE[(checkCode & 0b1111)];
        // 5.获取高4位，fresh
        int fresh = (int) (payload >>> FRESH_BIT_OFFSET & FRESH_MASK);
        // 6.验证格式：
        if (calcCheckCode(payload, fresh) != checkCode) {
            throw new ServiceException("无效项目编码");
        }
        return payload & SERIAL_NUM_MASK;
    }
}