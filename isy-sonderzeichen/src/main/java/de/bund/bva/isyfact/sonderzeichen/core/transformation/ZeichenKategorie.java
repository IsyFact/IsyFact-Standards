/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.sonderzeichen.core.transformation;

public class ZeichenKategorie {

    public static final String LETTER = "LETTER";

    public static final String NUMBER = "NUMBER";

    public static final String OTHER = "OTHER";

    public static final String PUNCTUATION = "PUNCTUATION";

    public static final String SEPARATOR = "SEPARATOR";

    public static final String SYMBOL = "SYMBOL";

    public static final String ALLE = "ALLE";

    public static final String[] ALLE_ZEICHEN_KATEGORIEN =
        new String[] { LETTER, NUMBER, OTHER, PUNCTUATION, SEPARATOR, SYMBOL, ALLE };

}
