package org.unixlibre.persistence.impl.jpa;

import org.unixlibre.persistence.ExecutorContext;

import javax.persistence.EntityManager;

/**
 * Created by antoniovl on 12/05/17.
 */
public class JPAExecutorContext implements ExecutorContext {

    private EntityManager entityManager;
    private boolean supportsNestedTransaction = false;

    public JPAExecutorContext() {
    }

    public JPAExecutorContext(EntityManager entityManager, boolean supportsNestedTransaction) {
        this.entityManager = entityManager;
        this.supportsNestedTransaction = supportsNestedTransaction;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isSupportsNestedTransaction() {
        return supportsNestedTransaction;
    }

    public void setSupportsNestedTransaction(boolean supportsNestedTransaction) {
        this.supportsNestedTransaction = supportsNestedTransaction;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private EntityManager entityManager;
        private boolean supportsNestedTransactions;

        public Builder setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
            return this;
        }

        public Builder setSupportsNestedTransactions(boolean supportsNestedTransactions) {
            this.supportsNestedTransactions = supportsNestedTransactions;
            return this;
        }

        public JPAExecutorContext build() {
            return new JPAExecutorContext(entityManager, supportsNestedTransactions);
        }
    }

    public static JPAExecutorContext fromExecutorContext(ExecutorContext executorContext) {
        if (executorContext instanceof JPAExecutorContext) {
            return (JPAExecutorContext)executorContext;
        }
        return null;
    }
}
