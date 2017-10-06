package de.bund.bva.isyfact.task.handler.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.task.exception.HostNotApplicableException;
import de.bund.bva.isyfact.task.handler.AusfuehrungsplanHandler;
import de.bund.bva.isyfact.task.handler.ExecutionDateTimeHandler;
import de.bund.bva.isyfact.task.handler.HostHandler;
import de.bund.bva.isyfact.task.handler.SecurityHandler;
import de.bund.bva.isyfact.task.handler.TaskHandler;
import de.bund.bva.isyfact.task.jmx.TaskMonitor;
import de.bund.bva.isyfact.task.konfiguration.DurationUtil;
import de.bund.bva.isyfact.task.model.Operation;
import de.bund.bva.isyfact.task.model.Task;
import de.bund.bva.isyfact.task.model.impl.TaskImpl;
import de.bund.bva.isyfact.task.security.SecurityAuthenticator;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;
import org.springframework.context.ApplicationContext;

/**
 * Der TaskHandler ist eine Werkzeugklasse für den Bau von Task-Instanzen.
 *
 * @author Alexander Salvanos, msg systems ag
 */
public class TaskHandlerImpl implements TaskHandler {
    private final static IsyLogger LOG = IsyLoggerFactory.getLogger(TaskHandlerImpl.class);

    /**
     * Erzeugt einen neuen Task aus einem TaskData-Objekt
     *
     * @param konfiguration
     * @param id
     * @return
     */
    @Override
    public synchronized Task createTask(String id, Konfiguration konfiguration,
        ApplicationContext applicationContext) throws HostNotApplicableException {

        Task task = null;
        HostHandler hostHandler = new HostHandlerImpl();
        if (hostHandler.isHostApplicable(id, konfiguration)) {

            SecurityHandler securityHandler = new SecurityHandlerImpl();
            SecurityAuthenticator securityAuthenticator =
                securityHandler.getSecurityAuthenticator(id, konfiguration);

            Operation operation = applicationContext.getBean(id, Operation.class);
            TaskMonitor monitor = applicationContext.getBean(id + "-monitor", TaskMonitor.class);

            AusfuehrungsplanHandler ausfuehrungsplanHandler = new AusfuehrungsplanHandlerImpl();
            AusfuehrungsplanHandlerImpl.Ausfuehrungsplan ausfuehrungsplan =
                ausfuehrungsplanHandler.getAusfuehrungsplan(id, konfiguration);

            ExecutionDateTimeHandler executionDateTimeHandler = new ExecutionDateTimeHandlerImpl();
            LocalDateTime executionDateTime =
                executionDateTimeHandler.getExecutionDateTime(id, konfiguration);

            Duration initialDelay = DurationUtil.leseInitialDelay(id, konfiguration);
            Duration fixedRate = DurationUtil.leseFixedRate(id, konfiguration);
            Duration fixedDelay = DurationUtil.leseFixedDelay(id, konfiguration);

            task = new TaskImpl(id, securityAuthenticator, operation, ausfuehrungsplan, executionDateTime,
                initialDelay, fixedRate, fixedDelay, monitor);
        }
        return task;
    }
}