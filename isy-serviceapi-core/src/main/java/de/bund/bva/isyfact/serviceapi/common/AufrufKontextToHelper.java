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
package de.bund.bva.isyfact.serviceapi.common;

import de.bund.bva.isyfact.serviceapi.service.httpinvoker.v1_0_0.AufrufKontextTo;

/**
 * Hilfsklasse zum Ermitteln des AufrufKontextTo aus den Serviceparametern.
 * 
 */
public class AufrufKontextToHelper {

    /**
     * Lädt den ersten gefundenen {@link AufrufKontextTo} aus den Parametern der aufgerufenen Funktion.
     * 
     * @param args
     *            die Argumente der Service-Operation
     * 
     * @return das AufrufKontextTo Objekt
     */
    public static AufrufKontextTo leseAufrufKontextTo(Object[] args) {

        if (args != null && args.length > 0) {
            for (Object parameter : args) {
                if (parameter instanceof AufrufKontextTo) {
                    return (AufrufKontextTo) parameter;
                }
            }
        }

        return null;
    }

}
