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
    private static final String[] ASCII_REPLACEMENTS = createAsciiReplacements();


    private static String[] createAsciiReplacements() {
        String[] rep = new String[128];
        // HTML escapes
        rep['&'] = "&amp;";
        rep['<'] = "&lt;";
        rep['>'] = "&gt;";
        rep['"'] = "&quot;";
        rep['\''] = "&apos;";
        // Control characters 0x00-0x1F except HT(0x09), LF(0x0A), CR(0x0D)
        for (int c = 0; c <= 0x1F; c++) {
            if (c == 0x09 || c == 0x0A || c == 0x0D) {
                continue;
            }
            rep[c] = "'0x" + Integer.toHexString(c);
        }
        return rep;
    }

    public static String escapeHTML(String s) {
        StringBuilder str = null;
        int len = s.length();
        if (len == 0) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0; i < len; i++) {
            char c = chars[i];
            String repl = (c < ASCII_REPLACEMENTS.length) ? ASCII_REPLACEMENTS[c] : null;
            if (repl != null) {
                if (str == null) {
                    // Pre-size builder to avoid further resizes: original length plus a small margin.
                    str = new StringBuilder(len + 8);
                    str.append(s, 0, i);
                }
                str.append(repl);
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
