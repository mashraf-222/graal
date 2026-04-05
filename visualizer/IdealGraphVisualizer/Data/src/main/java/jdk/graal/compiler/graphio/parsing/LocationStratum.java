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

package jdk.graal.compiler.graphio.parsing;

import java.util.Objects;

public final class LocationStratum {
    public final String uri;
    public final String file;
    public final String language;
    public final int line;
    public final int startOffset;
    public final int endOffset;

    private static String intern(String a) {
        if (a != null) {
            return a.intern();
        }
        return null;
    }

    LocationStratum(String uri, String file, String language, int line, int startOffset, int endOffset) {
        this.uri = intern(uri);
        this.file = intern(file);
        this.language = intern(language);
        this.line = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.uri);
        hash = 41 * hash + Objects.hashCode(this.file);
        hash = 41 * hash + Objects.hashCode(this.language);
        hash = 41 * hash + this.line;
        hash = 41 * hash + this.startOffset;
        hash = 41 * hash + this.endOffset;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocationStratum other = (LocationStratum) obj;
        if (this.line != other.line) {
            return false;
        }
        if (this.startOffset != other.startOffset) {
            return false;
        }
        if (this.endOffset != other.endOffset) {
            return false;
        }
        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        if (!Objects.equals(this.language, other.language)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Cache fields locally to avoid repeated field access
        final String language = this.language;
        final String uri = this.uri;
        final String file = this.file;
        final int line = this.line;
        final int startOffset = this.startOffset;
        final int endOffset = this.endOffset;

        final boolean hasStart = startOffset > -1;
        final boolean hasEnd = endOffset > -1;

        // Estimate capacity to avoid StringBuilder reallocation
        int estimated = 0;
        estimated += (language != null ? language.length() : 4) + 1; // language + '/'
        String main = uri != null ? uri : file;
        estimated += (main != null ? main.length() : 4) + 1; // uri/file + ':'
        estimated += digitLength(line);
        if (hasStart || hasEnd) {
            estimated += 2; // '(' and ')'
            estimated += hasStart ? digitLength(startOffset) : 0;
            estimated += 1; // '-'
            estimated += hasEnd ? digitLength(endOffset) : 0;
        }

        StringBuilder sb = new StringBuilder(Math.max(estimated, 16));
        sb.append(language).append("/");
        sb.append(main);
        sb.append(":").append(line);
        if (hasStart || hasEnd) {
            sb.append("(");
            if (hasStart) {
                sb.append(startOffset);
            }
            sb.append("-");
            if (hasEnd) {
                sb.append(endOffset);
            }
            sb.append(")");
        }
        return sb.toString();
    }


    private static int digitLength(int v) {
        long lv = v;
        if (lv == 0L) {
            return 1;
        }
        int digits = 0;
        if (lv < 0L) {
            digits++; // for the '-'
            lv = -lv;
        }
        while (lv > 0L) {
            digits++;
            lv /= 10L;
        }
        return digits;
    }

}
