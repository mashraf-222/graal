/*
 * Copyright (c) 2013, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
package org.graalvm.visualizer.util;

public class StringUtils {

    public static String escapeHTML(String s) {
        StringBuilder str = null;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '&' || c == '<' || c == '>' || c == '"' || c == '\'') {
                if (str == null) {
                    // Pre-size to avoid frequent resizing: original length plus a small slack.
                    str = new StringBuilder(len + 8);
                    str.append(s, 0, i);
                }
                switch (c) {
                    case '&':
                        str.append("&amp;");
                        break;
                    case '<':
                        str.append("&lt;");
                        break;
                    case '>':
                        str.append("&gt;");
                        break;
                    case '"':
                        str.append("&quot;");
                        break;
                    case '\'':
                        str.append("&apos;");
                        break;
                    default:
                        assert false;
                }
            } else if (c <= 0x1F && c != '\t' && c != '\n' && c != '\r') {
                if (str == null) {
                    str = new StringBuilder(len + 8);
                    str.append(s, 0, i);
                }
                str.append("'0x").append(Integer.toHexString(c));
            } else {
                if (str != null) {
                    str.append(c);
                }
            }
        }
        if (str == null) {
            return s;
        } else {
            return str.toString();
        }
    }

}
