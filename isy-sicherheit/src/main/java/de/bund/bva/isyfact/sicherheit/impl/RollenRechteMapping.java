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
package de.bund.bva.isyfact.sicherheit.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bund.bva.isyfact.sicherheit.Rolle;
import de.bund.bva.isyfact.sicherheit.Recht;

/**
 * Diese Klasse dient zum Speichern des Mappings von Rollen zu Rechten für eine Anwendung.
 * 
 */
public class RollenRechteMapping {

    /**
     * Die Id der Anwendung zu der das Mapping gehört.
     */
    private String anwendungsId;

    /**
     * Enthält das anwendungesspezifische Mapping von Rollen zu Rechten.
     */
    private Map<Rolle, List<Recht>> rollenRechteMapping = new HashMap<>();

    /**
     * alle in der Anwendung definierten Rechte.
     */
    private Set<Recht> alleDefiniertenRechte = new HashSet<>();

    /**
     * Getter für das Rollen zu Rechte Mapping.
     * 
     * @return Das Mapping von Rollen zu Rechten
     */
    public Map<Rolle, List<Recht>> getRollenRechteMapping() {
        return rollenRechteMapping;
    }

    /**
     * Setter für das Rollen zu Rechte Mapping.
     * 
     * @param rollenRechteMapping
     *            Das Mapping
     */
    public void setRollenRechteMapping(Map<Rolle, List<Recht>> rollenRechteMapping) {
        this.rollenRechteMapping = rollenRechteMapping;
    }

    /**
     * Getter für die Id der Anwendung zu der das Mapping gehört.
     * 
     * @return Die Id der Anwendung
     */
    public String getAnwendungsId() {
        return anwendungsId;
    }

    /**
     * Setter für die Id der Anwendung zu der das Mapping gehört.
     * 
     * @param anwendungsId
     *            Die Id der Anwendung
     */
    public void setAnwendungsId(String anwendungsId) {
        this.anwendungsId = anwendungsId;
    }

    /**
     * Ausgabe der Instanz als String.
     * 
     * @return Ausgabe des Mappings als String
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("AnwendungsId:").append(anwendungsId).append("\n");
        result.append("RollenRechteMapping:").append(rollenRechteMapping.toString());
        return result.toString();
    }

    /**
     * @return Eine Sammlung aller in der Anwendung (rollenrechte.xml) definierten Rechte.
     */
    public Set<Recht> getAlleDefiniertenRechte() {
        return Collections.unmodifiableSet(alleDefiniertenRechte);
    }

    /**
     * Setzt das Feld {@link #alleDefiniertenRechte}.
     * @param collection
     *            Neuer Wert für alleDefiniertenRechte
     */
    public void setAlleDefiniertenRechte(Collection<Recht> collection) {
        this.alleDefiniertenRechte = new HashSet<>(collection);
    }
}
