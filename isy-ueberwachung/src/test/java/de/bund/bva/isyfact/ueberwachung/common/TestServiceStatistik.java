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
package de.bund.bva.isyfact.ueberwachung.common;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import de.bund.bva.isyfact.datetime.test.TestClock;
import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.isyfact.ueberwachung.common.konstanten.EreignisSchluessel;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = { "isy.logging.anwendung.name=Test",
        "isy.logging.anwendung.typ=Test",
        "isy.logging.anwendung.version=0.1" })
public class TestServiceStatistik {

    @Autowired
    private ServiceStatistik serviceStatistik;

    @Autowired
    private MeterRegistry meterRegistry;

    private Random random = new SecureRandom();

    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(TestServiceStatistik.class);

    /**
     * Testet ZaehleAufruf von ServiceStatistik in der ersten Minute.
     */
    @Test
    public void testZaehleAufrufErsteMinute() {
        LOG.debug("Setze Zeit auf Anfang einer vollen Minute");
        DateTimeUtil.setClock(TestClock.at(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));

        final int anzahlAufrufe = 10;
        for (int count = 0; count < anzahlAufrufe; count++) {
            long dauer = random.nextInt(10000);
            boolean erfolgreich = random.nextBoolean();
            boolean fachlichErfolgreich = random.nextBoolean();
            serviceStatistik.zaehleAufruf(dauer, erfolgreich, fachlichErfolgreich);
            LOG.debug("Rufe MBean.zaehleAufruf auf mit ({},{},{})", dauer, erfolgreich, fachlichErfolgreich);
        }

        Gauge anzahlAufrufeLetzteMinute = meterRegistry.get("anzahlAufrufe.LetzteMinute").gauge();
        Gauge anzahlFehlerLetzteMinute = meterRegistry.get("anzahlFehler.LetzteMinute").gauge();
        Gauge durchschnittsDauerLetzteZehnAufrufe =
            meterRegistry.get("durchschnittsDauer.LetzteAufrufe").gauge();

        double durchschnittsDauerLetzteZehnAufrufeReferenz = durchschnittsDauerLetzteZehnAufrufe.value();

        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlAufrufeLetzteMinute           :{}", anzahlAufrufeLetzteMinute.value());
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlFehlerLetzteMinute            :{}",
            anzahlFehlerLetzteMinute.value());
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "DurchschnittsDauerLetzteZehnAufrufe :{}",
            durchschnittsDauerLetzteZehnAufrufeReferenz);
        // the statistics from the current minute are not yet populated
        assertEquals(0, anzahlAufrufeLetzteMinute.value(), 0.0);
        assertEquals(0, anzahlFehlerLetzteMinute.value(), 0.0);
        assertTrue(durchschnittsDauerLetzteZehnAufrufeReferenz > 0.0);

        LOG.debug("Stelle Uhr um 1 Minute nach vorn");
        ((TestClock) DateTimeUtil.getClock()).advanceBy(Duration.ofMinutes(1));

        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlAufrufeLetzteMinute           :{}", anzahlAufrufeLetzteMinute.value());
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlFehlerLetzteMinute            :{}",
            anzahlFehlerLetzteMinute.value());
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "DurchschnittsDauerLetzteZehnAufrufe :{}",
            durchschnittsDauerLetzteZehnAufrufeReferenz);
        assertEquals(anzahlAufrufe, anzahlAufrufeLetzteMinute.value(), 0.0);
        assertTrue(anzahlFehlerLetzteMinute.value() > 0.0);
        assertEquals(durchschnittsDauerLetzteZehnAufrufeReferenz, durchschnittsDauerLetzteZehnAufrufe.value(),
            0.0);
        LOG.info(LogKategorie.JOURNAL, EreignisSchluessel.PLUEB00001, "Prüfungen für 1. Minute erfolgreich");
    }

    /**
     * Testet ZaehleAufruf von ServiceStatistik mit 1. Minute ohne MBean Aufruf.
     */
    @Test
    public void testZaehleAufrufNachAbstand() {
        // wait till the minute starts.
        LOG.debug("Setze Zeit auf Anfang einer vollen Minute");
        DateTimeUtil.setClock(TestClock.at(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
        final int anzahlAufrufe = 10;
        for (int count = 0; count < anzahlAufrufe; count++) {
            long dauer = random.nextInt(10000);
            boolean erfolgreich = random.nextBoolean();
            boolean fachlichErfolgreich = random.nextBoolean();
            serviceStatistik.zaehleAufruf(dauer, erfolgreich, fachlichErfolgreich);
            LOG.debug("Rufe MBean.zaehleAufruf auf mit ({},{},{})", dauer, erfolgreich, fachlichErfolgreich);
        }

        Gauge anzahlAufrufeLetzteMinute = meterRegistry.get("anzahlAufrufe.LetzteMinute").gauge();
        Gauge anzahlFehlerLetzteMinute = meterRegistry.get("anzahlFehler.LetzteMinute").gauge();
        Gauge durchschnittsDauerLetzteZehnAufrufe =
            meterRegistry.get("durchschnittsDauer.LetzteAufrufe").gauge();

        double durchschnittsDauerLetzteZehnAufrufeReferenz = durchschnittsDauerLetzteZehnAufrufe.value();

        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlAufrufeLetzteMinute           :{}", anzahlAufrufeLetzteMinute);
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlFehlerLetzteMinute            :{}",
            anzahlFehlerLetzteMinute);
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "DurchschnittsDauerLetzteZehnAufrufe :{}",
            durchschnittsDauerLetzteZehnAufrufeReferenz);
        // the statistics from the current minute are not yet populated
        assertEquals(0, anzahlAufrufeLetzteMinute.value(), 0.0);
        assertEquals(0, anzahlFehlerLetzteMinute.value(), 0.0);
        assertTrue(durchschnittsDauerLetzteZehnAufrufeReferenz > 0.0);

        // wait 120 seconds and check the values. Since there were no calls in the last minute
        // number of calls and number of erroneous calls should be zero.
        LOG.debug("Stelle Uhr um 2 Minuten nach vorn");
        ((TestClock) DateTimeUtil.getClock()).advanceBy(Duration.ofMinutes(2));

        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001,
            "AnzahlAufrufeLetzteMinute           :{}", anzahlAufrufeLetzteMinute);
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001, "AnzahlFehlerLetzteMinute            :",
            anzahlFehlerLetzteMinute);
        LOG.info(LogKategorie.METRIK, EreignisSchluessel.PLUEB00001, "DurchschnittsDauerLetzteZehnAufrufe :",
            durchschnittsDauerLetzteZehnAufrufeReferenz);
        assertEquals(0, anzahlAufrufeLetzteMinute.value(), 0.0);
        assertEquals(0, anzahlFehlerLetzteMinute.value(), 0.0);
        assertEquals(durchschnittsDauerLetzteZehnAufrufeReferenz, durchschnittsDauerLetzteZehnAufrufe.value(),
            0.0);
        LOG.info(LogKategorie.JOURNAL, EreignisSchluessel.PLUEB00001, "Prüfungen für 2. Minute erfolgreich");
    }
}
