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
package de.bund.bva.isyfact.sicherheit.annotation.bean;

import de.bund.bva.isyfact.sicherheit.annotation.Gesichert;
import de.bund.bva.isyfact.sicherheit.annotation.NutzerAuthentifizierung;

/**
 * Ein Bean, welches abgesicherte Methoden ohne Funktionalität bereitstellt.
 * <p>
 * Es dient zum Testen, dass die korrekten Exceptions geworfen werden.
 *
 *
 */
public class ServiceImpl implements ServiceIntf {

    @Override
    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    @Gesichert({})
    public void gesichertDurch_Nichts() {
        // noop
    }

    @Override
    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    @Gesichert("Recht_A")
    public void gesichertDurch_RechtA() {
        // noop
    }

    @Override
    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    @Gesichert("Recht_B")
    public void gesichertDurch_RechtB() {
        // noop
    }

    @Override
    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    @Gesichert({ "Recht_A", "Recht_B" })
    public void gesichertDurch_RechtAundB() {
        // noop
    }

    @Override
    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    @Gesichert("")
    public void gesichertDurch_leeresRecht() {
        // noop
    }

    @NutzerAuthentifizierung(benutzer = "testBenutzer")
    public void methodeGesichertDurchNutzerAnnotation() {
        // noop
    }

    @Override
    public void nichtGesichert() {
        // noop
    }

    @Override
    public void gesichertAmInterface() {
        // noop
    }
}
