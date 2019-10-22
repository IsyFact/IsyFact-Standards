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
package de.bund.bva.isyfact.serviceapi.core.aufrufkontext;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextFactory;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.aufrufkontext.impl.AufrufKontextFactoryImpl;
import de.bund.bva.isyfact.serviceapi.core.aufrufkontext.helper.DebugAufrufKontextVerwalter;
import de.bund.bva.isyfact.serviceapi.core.aop.test.AufrufKontextSstTestBean;
import de.bund.bva.isyfact.serviceapi.service.httpinvoker.v1_0_0.AufrufKontextTo;
import junit.framework.AssertionFailedError;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Testet die Funktionalität von {@link StelltAufrufKontextBereitInterceptor}.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AllgemeinerAufrufKontextInterceptorAnnotationTest.TestConfig.class)
public class AllgemeinerAufrufKontextInterceptorAnnotationTest {

    /**
     * Zugriff auf den AufrufKontextVerwalter, um zu überprüfen, ob der AufrufKontext korrekt gesetzt / nicht
     * gesetzt wurde.
     */
    @Autowired
    public DebugAufrufKontextVerwalter aufrufKontextVerwalter;

    /** Simulation einer Service-Schnittstelle, auf die Aufrufe getätigt werden. */
    @Autowired
    public AufrufKontextSstTestBean sst;

    /**
     * erzeugt ein AufrufKontext Transport Objekt.
     *
     * @return
     */
    private AufrufKontextTo createAufrufKontextTo() {
        AufrufKontextTo aufrufKontextTo = new AufrufKontextTo();
        aufrufKontextTo.setDurchfuehrendeBehoerde("TEST");
        aufrufKontextTo.setDurchfuehrenderBenutzerKennung("TEST");
        aufrufKontextTo.setDurchfuehrenderBenutzerPasswort("TEST");
        aufrufKontextTo.setDurchfuehrenderSachbearbeiterName("TEST");
        aufrufKontextTo.setKorrelationsId("TEST");
        aufrufKontextTo.setRolle(new String[] { "TEST" });
        aufrufKontextTo.setRollenErmittelt(true);
        return aufrufKontextTo;
    }

    /**
     * Vergleicht ein AufrufKontextTo mit einem AufrufKontext.
     * <p>
     * Wirft einen {@link AssertionFailedError}, wenn die übergebenen Objekte nicht überein stimmen.
     *
     * @param to
     *            Transportobjekt
     * @param kontext
     *            AufrufKontext
     */
    private void assertEqualData(AufrufKontextTo to, AufrufKontext kontext) {
        assertEquals(to.getDurchfuehrendeBehoerde(), kontext.getDurchfuehrendeBehoerde());
        assertEquals(to.getDurchfuehrenderBenutzerKennung(), kontext.getDurchfuehrenderBenutzerKennung());
        assertEquals(to.getDurchfuehrenderSachbearbeiterName(),
            kontext.getDurchfuehrenderSachbearbeiterName());
        assertEquals(to.getDurchfuehrenderBenutzerPasswort(), kontext.getDurchfuehrenderBenutzerPasswort());
        assertEquals(to.getKorrelationsId(), kontext.getKorrelationsId());
        assertArrayEquals(to.getRolle(), kontext.getRolle());
        assertEquals(to.isRollenErmittelt(), kontext.isRollenErmittelt());
    }

    /**
     * setzt den AufrufKontext vor jedem Test zurück.
     */
    @Before
    public void leereAufrufKontext() {
        aufrufKontextVerwalter.resetAufrufKontext();
    }

    @After
    public void isAufrufKontextLeer() {
        // der AufrufKontext darf nach Beendigung des Aufrufs nicht mehr gesetzt sein.
        assertNull(aufrufKontextVerwalter.getAufrufKontext());
    }

    @Test
    public void stelltAufrufKontextNichtBereitOhneParameter() {
        sst.stelltAufrufKontextNichtBereitOhneParameter();
        assertNull(aufrufKontextVerwalter.getLetzterAufrufKontext());
    }

    @Test
    public void stelltAufrufKontextNichtBereitMitParameter() {
        sst.stelltAufrufKontextNichtBereitMitParameter(createAufrufKontextTo());
        assertNull(aufrufKontextVerwalter.getLetzterAufrufKontext());
    }

    @Test
    public void stelltAufrufKontextBereitMitParameter() {
        AufrufKontextTo aufrufKontextTo = createAufrufKontextTo();
        sst.stelltAufrufKontextBereitMitParameter(aufrufKontextTo);
        assertEqualData(aufrufKontextTo, aufrufKontextVerwalter.getLetzterAufrufKontext());
    }

    @Test
    public void stelltAufrufKontextBereitMitMehrerenParametern() {
        AufrufKontextTo aufrufKontextTo = createAufrufKontextTo();
        sst.stelltAufrufKontextBereitMitMehrerenParametern(aufrufKontextTo, "42");
        assertEqualData(aufrufKontextTo, aufrufKontextVerwalter.getLetzterAufrufKontext());
    }

    @Test
    public void stelltAufrufKontextBereitMitMehrerenParameterMehrereKontexte() {
        AufrufKontextTo aufrufKontextTo = createAufrufKontextTo();
        sst.stelltAufrufKontextBereitMitMehrerenParameterMehrereKontexte(aufrufKontextTo,
            createAufrufKontextTo());
        assertEqualData(aufrufKontextTo, aufrufKontextVerwalter.getLetzterAufrufKontext());
    }

    @Test
    public void stelltNullAufrufKontextBereitOhneParameter() {
        sst.stelltAufrufKontextBereitOhneParameter();
        assertNull(aufrufKontextVerwalter.getAufrufKontext());
    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestConfig {

        @Bean
        public MapperFacade mapperFacade() {
            return new DefaultMapperFactory.Builder().build().getMapperFacade();
        }

        @Bean
        public StelltAllgemeinenAufrufKontextBereitInterceptor interceptor(AufrufKontextFactory factory,
            AufrufKontextVerwalter verwalter, MapperFacade mapper) {
            return new StelltAllgemeinenAufrufKontextBereitInterceptor(mapper, factory, verwalter);
        }

        @Bean
        public Advisor stelltAufrufKontextBereitAdvisor(
            StelltAllgemeinenAufrufKontextBereitInterceptor interceptor) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(
                "@annotation(de.bund.bva.isyfact.serviceapi.core.aufrufkontext.StelltAufrufKontextBereit)");
            return new DefaultPointcutAdvisor(pointcut, interceptor);
        }

        @Bean
        public AufrufKontextVerwalter aufrufKontextVerwalter() {
            return new DebugAufrufKontextVerwalter();
        }

        @Bean
        public AufrufKontextFactory aufrufKontextFactory() {
            return new AufrufKontextFactoryImpl();
        }

        @Bean
        public AufrufKontextSstTestBean bereitgestellteSchnittstelle() {
            return new AufrufKontextSstTestBean();
        }
    }

}
