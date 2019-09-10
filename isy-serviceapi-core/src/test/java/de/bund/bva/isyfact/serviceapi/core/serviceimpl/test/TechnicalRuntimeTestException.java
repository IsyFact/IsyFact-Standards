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
package de.bund.bva.isyfact.serviceapi.core.serviceimpl.test;

import java.util.Arrays;

import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.exception.TechnicalRuntimeException;

public class TechnicalRuntimeTestException extends TechnicalRuntimeException {
    private static final long serialVersionUID = 4944917830826917167L;

    public TechnicalRuntimeTestException(String ausnahmeId, Throwable throwable, String[] parameter) {
        super(ausnahmeId, throwable, new FehlertextProvider() {
            @Override
            public String getMessage(String schluessel, String... parameter) {
                return schluessel + ": " + Arrays.toString(parameter);
            }
        }, parameter);
    }

}
