/*
 * Copyright (c) 2013, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.graal.compiler.graphio.parsing.model;

import java.lang.reflect.Array;
import java.util.Objects;

public class Property<T> {
    private final String name;
    private final T value;

    public Property(String name, T value) {
        if (name == null) {
            throw new IllegalArgumentException("Property name must not be null!");
        }

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return toString(name, value);
    }

    public static <T> String toString(String name, T value) {
        StringBuilder sb = new StringBuilder(name).append("=");
        if (value == null || !value.getClass().isArray()) {
            sb.append(value);
        } else {
            // maybe better pattern should be used
            sb.append("[");
            int length = Array.getLength(value);
            for (int i = 0; i < length; ++i) {
                sb.append(Array.get(value, i)).append(", ");
            }
            sb.setLength(sb.length() == 1 ? 1 : sb.length() - 2);
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Property<?>)) {
            return false;
        }
        Property<?> p2 = (Property<?>) o;

        // Quick name check
        if (!name.equals(p2.name)) {
            return false;
        }

        Object v1 = value;
        Object v2 = p2.value;

        // Fast identity/null checks
        if (v1 == v2) {
            return true;
        }
        if (v1 == null || v2 == null) {
            return false;
        }

        Class<?> c1 = v1.getClass();
        Class<?> c2 = v2.getClass();

        // If both are arrays, handle arrays efficiently (including primitive arrays).
        if (c1.isArray() && c2.isArray()) {
            Class<?> comp1 = c1.getComponentType();
            Class<?> comp2 = c2.getComponentType();

            // If component types differ and one is primitive, they can't be equal.
            if (comp1.isPrimitive() || comp2.isPrimitive()) {
                if (comp1 != comp2) {
                    return false;
                }
                // Both are arrays of the same primitive type — use the appropriate Arrays.equals
                if (comp1 == int.class) {
                    return java.util.Arrays.equals((int[]) v1, (int[]) v2);
                } else if (comp1 == long.class) {
                    return java.util.Arrays.equals((long[]) v1, (long[]) v2);
                } else if (comp1 == short.class) {
                    return java.util.Arrays.equals((short[]) v1, (short[]) v2);
                } else if (comp1 == char.class) {
                    return java.util.Arrays.equals((char[]) v1, (char[]) v2);
                } else if (comp1 == byte.class) {
                    return java.util.Arrays.equals((byte[]) v1, (byte[]) v2);
                } else if (comp1 == boolean.class) {
                    return java.util.Arrays.equals((boolean[]) v1, (boolean[]) v2);
                } else if (comp1 == float.class) {
                    return java.util.Arrays.equals((float[]) v1, (float[]) v2);
                } else if (comp1 == double.class) {
                    return java.util.Arrays.equals((double[]) v1, (double[]) v2);
                } else {
                    // Fallback: should not be reachable, but maintain safety by delegating to Objects.deepEquals
                    return Objects.deepEquals(v1, v2);
                }
            } else {
                // Both are object (possibly multi-dimensional) arrays — use deepEquals
                return java.util.Arrays.deepEquals((Object[]) v1, (Object[]) v2);
            }
        }

        // Non-array fallback
        return Objects.equals(v1, v2);
    }

    @Override
    public int hashCode() {
        return makeHash(name, value);
    }

    protected static <T> int makeHash(String name, T value) {
        int hash = name.hashCode();
        if (value == null || !value.getClass().isArray()) {
            return hash * 3 + Objects.hashCode(value);
        }
        int length = Array.getLength(value);
        for (int i = 0; i < length; ++i) {
            hash = hash * 3 + Objects.hashCode(Array.get(value, i));
        }
        return hash;
    }
}
