package com.shallow.universe.process.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\5 0005
 */
public class GenericsUtils {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Type getInterfaceGenericType(Class<?> clazz) {
        return getInterfaceGenericType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param type  clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Type getInterfaceGenericType(Class<?> type, int index)
            throws IndexOutOfBoundsException {
        Type[] genType = ((ParameterizedType) type.getGenericInterfaces()[0]).getActualTypeArguments();
        if (index >= genType.length || index < 0) {
            return Object.class;
        }
        if (!(genType[index] instanceof Class)) {
            return Object.class;
        }
        return genType[index];
    }
}
