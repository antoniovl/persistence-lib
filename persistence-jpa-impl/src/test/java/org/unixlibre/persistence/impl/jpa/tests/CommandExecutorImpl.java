package org.unixlibre.persistence.impl.jpa.tests;

import org.unixlibre.persistence.Command;
import org.unixlibre.persistence.CommandExecutor;
import org.unixlibre.persistence.TransactionTypesEnum;
import org.unixlibre.persistence.impl.jpa.JPAExecutorContext;
import org.unixlibre.persistence.impl.jpa.JPATools;

import javax.persistence.EntityManager;

/**
 * CommandExecutor for RESOURCE_LOCAL.
 */
public class CommandExecutorImpl implements CommandExecutor {

    @Override
    public <T> void executeCommand(Command<T> cmd) {
        JPAExecutorContext context = getContext();
        cmd.setTransactionType(TransactionTypesEnum.LOCAL);
        cmd.run(context);
    }

    private JPAExecutorContext getContext() {
        EntityManager entityManager = JPATools.getEntityManager(BaseTest.PU_NAME);
        return JPAExecutorContext.newBuilder()
                .setEntityManager(entityManager)
                .build();
    }
}
