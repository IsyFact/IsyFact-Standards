package de.bund.bva.isyfact.serviceapi.core.httpinvoker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.util.LogHelper;
import de.bund.bva.isyfact.serviceapi.core.aufrufkontext.DefaultAufrufKontextToResolver;
import de.bund.bva.isyfact.serviceapi.service.httpinvoker.v1_0_0.AufrufKontextTo;

@RunWith(MockitoJUnitRunner.class)
public class IsyHttpInvokerClientInterceptorTest {

    @Mock
    private AufrufKontextTo aufrufKontextTo;

    @Mock
    private MethodInvocation methodInvocation;

    @Mock
    private LogHelper logHelper;

    @Test
    public void invokeMitIsyFactLogging() throws Throwable {

        IsyHttpInvokerClientInterceptor isyHttpInvokerClientInterceptor =
            new IsyHttpInvokerClientInterceptor();

        Method toStringMethod = Object.class.getMethod("toString");

        when(logHelper.ermittleAktuellenZeitpunkt()).thenReturn(1L).thenReturn(2L);
        when(methodInvocation.getArguments()).thenReturn(new Object[] { aufrufKontextTo });
        when(methodInvocation.getMethod()).thenReturn(toStringMethod);
        when(aufrufKontextTo.getKorrelationsId()).thenReturn("korrelationsId");

        isyHttpInvokerClientInterceptor.setAufrufKontextToResolver(new DefaultAufrufKontextToResolver());
        isyHttpInvokerClientInterceptor.setLogHelper(logHelper);
        isyHttpInvokerClientInterceptor.setRemoteSystemName("remoteSystem");

        isyHttpInvokerClientInterceptor.invoke(methodInvocation);

        verify(aufrufKontextTo).setKorrelationsId(anyString());
        verify(logHelper)
            .loggeNachbarsystemAufruf(any(IsyLogger.class), eq(toStringMethod), eq("remoteSystem"),
                eq(null));
        verify(logHelper)
            .loggeNachbarsystemErgebnis(any(IsyLogger.class), eq(toStringMethod), eq("remoteSystem"),
                eq((String) null), eq(true));
        verify(logHelper)
            .loggeNachbarsystemDauer(any(IsyLogger.class), eq(toStringMethod), eq(1L), eq("remoteSystem"),
                eq((String) null), eq(true));
    }
}
