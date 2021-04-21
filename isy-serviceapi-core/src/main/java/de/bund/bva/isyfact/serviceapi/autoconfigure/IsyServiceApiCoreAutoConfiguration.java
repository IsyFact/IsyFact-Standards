package de.bund.bva.isyfact.serviceapi.autoconfigure;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontextFactory;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.serviceapi.common.AufrufKontextToResolver;
import de.bund.bva.isyfact.serviceapi.core.aop.StelltLoggingKontextBereitInterceptor;
import de.bund.bva.isyfact.serviceapi.core.aufrufkontext.StelltAufrufKontextBereitInterceptor;

@Configuration
public class IsyServiceApiCoreAutoConfiguration {

    @Bean
    public StelltLoggingKontextBereitInterceptor stelltLoggingKontextBereitInterceptor() {
        return new StelltLoggingKontextBereitInterceptor();
    }

    @Bean
    public Advisor stelltLoggingKontextBereitAdvisor(StelltLoggingKontextBereitInterceptor interceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
            "@annotation(de.bund.bva.isyfact.serviceapi.core.aop.StelltLoggingKontextBereit) || @within(de.bund.bva.isyfact.serviceapi.core.aop.StelltLoggingKontextBereit)");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, interceptor);
        advisor.setOrder(50);
        return advisor;
    }

    @Bean
    @ConditionalOnBean({ AufrufKontextFactory.class, AufrufKontextVerwalter.class })
    public StelltAufrufKontextBereitInterceptor stelltAufrufKontextBereitInterceptor(
        AufrufKontextFactory factory,
        AufrufKontextVerwalter verwalter,
        AufrufKontextToResolver resolver) {
        return new StelltAufrufKontextBereitInterceptor(factory, verwalter, resolver);
    }

    @Bean
    @ConditionalOnBean(StelltAufrufKontextBereitInterceptor.class)
    public Advisor stelltAufrufKontextBereitAdvisor(StelltAufrufKontextBereitInterceptor interceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
            "@annotation(de.bund.bva.isyfact.serviceapi.core.aufrufkontext.StelltAufrufKontextBereit) || @within(de.bund.bva.isyfact.serviceapi.core.aufrufkontext.StelltAufrufKontextBereit)");
        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public AufrufKontextToResolver aufrufKontextToResolver() {
        return new AufrufKontextToResolver();
    }
}
