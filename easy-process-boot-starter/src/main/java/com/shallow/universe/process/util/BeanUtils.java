package com.shallow.universe.process.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


/**
 * The type Bean util.
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021 -10-26 10:34:55
 */
@Slf4j
public class BeanUtils {

    /**
     * Gets property.
     *
     * @param target    the target
     * @param fieldName the field name
     * @return the property
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:41
     * @since 1.0.0
     */
    public static Object getProperty(Object target, String fieldName) {
        try {
            return FieldUtils.readField(target, fieldName, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets property.
     *
     * @param target    the target
     * @param fieldName the field name
     * @param value     the value
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:43
     * @since 1.0.0
     */
    public static void setProperty(Object target, String fieldName, Object value) {
        try {
            FieldUtils.writeField(target, fieldName, value, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is impl super boolean.
     *
     * @param beanClass  the bean class
     * @param superClass the super class
     * @return the boolean
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:45
     * @since 1.0.0
     */
    public static boolean isImplSuper(Class<?> beanClass, Class<?> superClass) {
        return Arrays.asList(beanClass.getInterfaces()).contains(superClass);
    }

    /**
     * Is extend super boolean.
     *
     * @param beanClass  the bean class
     * @param superClass the super class
     * @return the boolean
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:47
     * @since 1.0.0
     */
    public static boolean isExtendSuper(Class<?> beanClass, Class<?> superClass) {
        return Objects.equals(beanClass.getSuperclass(), superClass);
    }

    /**
     * Copy.
     *
     * @param from the from
     * @param to   the to
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:49
     * @since 1.0.0
     */
    public static void copy(Object from, Object to) {
        Arrays.asList(FieldUtils.getAllFields(from.getClass())).forEach(field -> {
            if (containsField(to, field.getName())) {
                BeanUtils.setProperty(to, field.getName(), getProperty(from, field.getName()));
            }
        });
    }

    /**
     * Copy.
     *
     * @param from   the from
     * @param to     the to
     * @param fields the fields
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:49
     * @since 1.0.0
     */
    public static void copy(Object from, Object to, String... fields) {
        Arrays.asList(fields).forEach(field -> {
            if (containsField(from, field)) {
                BeanUtils.setProperty(to, field, getProperty(from, field));
            }
        });
    }

    /**
     * Copy.
     *
     * @param from   the from
     * @param to     the to
     * @param fields the fields
     * @author Guo Shuai
     * @date 2021 -10-26 11:12:51
     * @since 1.0.0
     */
    public static void copy(Object from, Object to, Map<String, String> fields) {
        //遍历转换
        fields.forEach((key, value) -> {
            if (containsField(from, key)) {
                //判断类型是否一致
                if (isTypeEquals(from, to, key, value)) {
                    BeanUtils.setProperty(to, value, getProperty(from, key));
                }
            }
        });
    }

    /**
     * Contains field boolean.
     *
     * @param source    the source
     * @param fieldName the fieldName
     * @return the boolean
     * @author Guo Shuai
     * @date 2021 -10-26 10:36:02
     * @since 1.0.0
     */
    public static boolean containsField(Object source, String fieldName) {
        return containsField(source.getClass(), fieldName);
    }

    /**
     * Contains field boolean.
     *
     * @param source    the source
     * @param fieldName the fieldName
     * @return the boolean
     * @author Guo Shuai
     * @date 2021 -10-26 10:36:02
     * @since 1.0.0
     */
    public static boolean containsField(Class<?> source, String fieldName) {
        Field field = FieldUtils.getField(source, fieldName, true);
        if (field == null) {
            field = FieldUtils.getField(source.getSuperclass(), fieldName, true);
        }
        return field != null;
    }

    /**
     * Is type equals boolean.
     *
     * @param source the source
     * @param target the target
     * @return the boolean
     * @author Guo Shuai
     * @date 2021 -10-26 14:00:42
     * @since 1.0.0
     */
    private static boolean isTypeEquals(Object source, Object target, String from, String to) {
        Field sourceField = FieldUtils.getField(source.getClass(), from, true);
        Field targetField = FieldUtils.getField(target.getClass(), to, true);
        return sourceField.getType() == targetField.getType();
    }
}
