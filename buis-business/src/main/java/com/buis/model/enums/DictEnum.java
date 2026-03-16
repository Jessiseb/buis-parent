package com.buis.model.enums;

/**
 * 通用字典枚举接口
 */
public interface DictEnum {
    /**
     * 获取字典编码
     */
    Integer getCode();

    /**
     * 获取字典名称
     */
    String getName();

    /**
     * 通用工具方法：根据编码获取枚举实例
     * @param enumClass 枚举类
     * @param code 编码
     * @return 枚举实例，无匹配则返回null
     */
    static <T extends DictEnum> T getByCode(Class<T> enumClass, Integer code) {
        if (code == null || enumClass == null) {
            return null;
        }
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (code.equals(enumConstant.getCode())) {
                return enumConstant;
            }
        }
        return null;
    }

    /**
     * 通用工具方法：根据编码获取字典名称
     * @param enumClass 枚举类
     * @param code 编码
     * @param defaultName 默认名称（无匹配时返回）
     * @return 字典名称
     */
    static <T extends DictEnum> String getNameByCode(Class<T> enumClass, Integer code, String defaultName) {
        T enumInstance = getByCode(enumClass, code);
        return enumInstance != null ? enumInstance.getName() : defaultName;
    }
}