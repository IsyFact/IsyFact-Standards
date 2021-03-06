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
package de.bund.bva.isyfact.serviceapi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Diese Annotation kennzeichnet Felder oder Parameter, die nicht <code>null</code>, und im Fall eines Strings
 * auch nicht leer sein dürfen.
 * 
 * Die Annotation kann auf zwei Arten verwendet werden:
 * 
 * <ul>
 * <li>Für Felder einer Datenklasse (z.B. einer TO-Klasse). Es wird der <strong>Getter</strong> des Felds
 * annotiert. Der Setter und das Feld selbst erhalten keine Annotation.</li>
 * <li>Für Parameter einer Schnittstelle (z.B. einer RemoteBean-Schnittstelle).</li>
 * </ul>
 * 
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pflichtfeld {
}
