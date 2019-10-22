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
package de.bund.bva.isyfact.serviceapi.core.serviceimpl;

import java.lang.reflect.Method;

import de.bund.bva.isyfact.exception.BaseException;
import de.bund.bva.isyfact.exception.service.TechnicalToException;
import de.bund.bva.isyfact.exception.service.ToException;

/**
 * Ermittelt die Abbildung von Exceptions per Reflection.
 * 
 * <p>
 * Diese Klasse erwartet, dass zu jeder AWK-Exception genau eine zugehörige TO-Exception in der
 * RemoteBean-Operation deklariert ist, die sich nur im Namenssuffix "ToException" (vs. "Exception")
 * unterscheidet. Weiterhin erwartet sie, dass in der RemoteBean-Operation genau eine TechnicalToException
 * deklariert ist, die als generische technische Exception fungiert.
 * </p>
 * 
 */
public class ReflectiveExceptionMappingSource implements ExceptionMappingSource {

    /**
     * {@inheritDoc}
     */
    public Class<? extends ToException> getToExceptionClass(Method remoteBeanMethod,
        Class<? extends BaseException> exceptionClass) {

        String coreExceptionName = removeEnd(exceptionClass.getSimpleName(), "Exception");

        for (Class<?> toExceptionClass : remoteBeanMethod.getExceptionTypes()) {
            if (ToException.class.isAssignableFrom(toExceptionClass)) {
                String toExceptionName = removeEnd(toExceptionClass.getSimpleName(), "ToException");
                if (coreExceptionName.equals(toExceptionName)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends ToException> castToExceptionClass =
                        (Class<? extends ToException>) toExceptionClass;
                    return castToExceptionClass;
                }
            }
        }

        throw new IllegalStateException("Keine TO-Exception für die AWK-Exception " + exceptionClass
            + " in Serviceoperation " + getMethodSignatureString(remoteBeanMethod));
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends TechnicalToException> getGenericTechnicalToException(Method remoteBeanMethod) {
        Class<? extends TechnicalToException> genericTechnicalToException = null;
        for (Class<?> toExceptionClass : remoteBeanMethod.getExceptionTypes()) {
            if (TechnicalToException.class.isAssignableFrom(toExceptionClass)) {
                if (genericTechnicalToException != null) {
                    throw new IllegalStateException(
                        "Mehr als eine Technical-TO-Exception in Serviceoperation "
                            + getMethodSignatureString(remoteBeanMethod));
                }
                @SuppressWarnings("unchecked")
                Class<? extends TechnicalToException> castToExceptionClass =
                    (Class<? extends TechnicalToException>) toExceptionClass;
                genericTechnicalToException = castToExceptionClass;
            }
        }

        if (genericTechnicalToException != null) {
            return genericTechnicalToException;
        }

        throw new IllegalStateException("Keine Technical-TO-Exception in Serviceoperation "
            + getMethodSignatureString(remoteBeanMethod));
    }

    /**
     * Liefert die Methodensignatur als String.
     * 
     * @param method
     *            die Methode
     * @return die Methodensignatur als String.
     */
    protected String getMethodSignatureString(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    private static String removeEnd(String str, String remove) {
        if (str == null || str.length() == 0 || remove == null || remove.length() == 0) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

}
