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
package de.bund.bva.isyfact.batchrahmen.batch.protokoll;

/**
 * Enum für Meldungstypen.
 * 
 *
 */
public enum MeldungTyp {
    /** Fehler. */
    FEHLER("F"),
    /** Warnungen. */
    WARNUNG("W"),
    /** Infos. */
    INFO("I");

    /** Kürzel für das Protokoll. */
    private String kuerzel;

    /**
     * Erzeugt MeldungTyp mit Kürzel.
     * @param kuerzel
     *            Das Kürzek fürs Protokoll.
     */
    private MeldungTyp(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    /**
     * Das Kürzel für diesen Meldungstyp.
     * @return Das Kürzel für diesen Meldungstyp.
     */
    public String getKuerzel() {
        return kuerzel;
    }
}
