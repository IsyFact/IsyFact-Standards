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
package de.bund.bva.pliscommon.serviceapi.core.httpinvoker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;

import org.springframework.http.HttpHeaders;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocationResult;

import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.pliscommon.serviceapi.common.konstanten.EreignisSchluessel;

/**
 * Extends Spring's {@link SimpleHttpInvokerRequestExecutor}, which allows configuring
 * the timeout and the number of times a request should be retried.
 */
public class TimeoutWiederholungHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    /** Isy-Logger. */
    private static final IsyLogger LOG = IsyLoggerFactory
        .getLogger(TimeoutWiederholungHttpInvokerRequestExecutor.class);

    /** AufrufKontextVerwalter for setting the bearer token. */
    private final AufrufKontextVerwalter<?> aufrufKontextVerwalter;

    /** Timeout for the request. */
    private int timeout;

    /** Number of times a request should be tried after a timeout. */
    private int anzahlWiederholungen;

    /** Pause between retries. */
    private int wiederholungenAbstand;

    /**
     * Creates a SimpleHttpInvokerRequestExecutor with configurable timeout and number of retries.
     *
     * @param aufrufKontextVerwalter for setting the bearer token
     */
    public TimeoutWiederholungHttpInvokerRequestExecutor(AufrufKontextVerwalter<?> aufrufKontextVerwalter) {
        this.aufrufKontextVerwalter = aufrufKontextVerwalter;
    }

    @Override
    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config,
                                                      ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
        int versuch = 0;
        while (true) {
            try {
                return super.doExecuteRequest(config, baos);
            } catch (InterruptedIOException requestException) {
                LOG.info(LogKategorie.PROFILING, EreignisSchluessel.TIMEOUT,
                    "Beim Aufrufen des Services [{}] ist ein Timeout aufgetreten.", config.getServiceUrl());
                versuch++;
                if (versuch == this.anzahlWiederholungen) {
                    LOG.info(LogKategorie.PROFILING, EreignisSchluessel.TIMEOUT_ABBRUCH,
                        "Aufruf nach Timeout abgebrochen.");
                    throw requestException;
                }
                try {
                    if (this.wiederholungenAbstand > 0) {
                        LOG.info(LogKategorie.PROFILING, EreignisSchluessel.TIMEOUT_WARTEZEIT,
                            "Warte {}ms bis zur Wiederholung des Aufrufs.", this.wiederholungenAbstand);
                        Thread.sleep(this.wiederholungenAbstand);
                    }
                } catch (InterruptedException ex) {
                    LOG.info(LogKategorie.PROFILING, EreignisSchluessel.TIMEOUT_WARTEZEIT_ABBRUCH,
                        "Warten auf Aufrufwiederholung abgebrochen", ex);
                    throw requestException;
                }
                LOG.info(LogKategorie.PROFILING, EreignisSchluessel.TIMEOUT_WIEDERHOLUNG,
                    "Wiederhole Aufruf...");
            }
        }
    }

    @Override
    protected void prepareConnection(HttpURLConnection con, int contentLength) throws IOException {
        super.prepareConnection(con, contentLength);
        if (aufrufKontextVerwalter.getBearerToken() != null) {
            con.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + aufrufKontextVerwalter.getBearerToken());
        }
        con.setReadTimeout(this.timeout);
        con.setConnectTimeout(this.timeout);
    }

    /**
     * Sets the timeout in milliseconds. The timeout is used while establishing and reading via the HTTP connection.
     *
     * @param timeout
     *         timeout in milliseconds
     * @see HttpURLConnection#setConnectTimeout(int)
     * @see HttpURLConnection#setReadTimeout(int)
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Sets number of times a request should be retried after a it timed out. The default is "0".
     *
     * @param anzahlWiederholungen
     *         number of retries
     */
    public void setAnzahlWiederholungen(int anzahlWiederholungen) {
        this.anzahlWiederholungen = anzahlWiederholungen;
    }

    /**
     * Sets the time to wait between two retries. Default is "0".
     *
     * @param wiederholungenAbstand
     *         pause between two retries in milliseconds
     * @see #setAnzahlWiederholungen(int)
     */
    public void setWiederholungenAbstand(int wiederholungenAbstand) {
        this.wiederholungenAbstand = wiederholungenAbstand;
    }

}
