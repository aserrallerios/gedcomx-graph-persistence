package org.gedcomx.persistence.graph.neo4j.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.neo4j.graphdb.Transaction;

import com.google.common.base.Throwables;
import com.google.inject.Inject;

public class TransactionWrapper implements MethodInterceptor {

    private GENgraphDAO dao;

    @Inject
    public TransactionWrapper() {
    }

    @Override
	public Object invoke(final MethodInvocation invocation) {

        final Transaction t = this.dao.beginTransaction();
        Object o = null;
        try {
            o = invocation.proceed();
            this.dao.commitTransaction(t);
		} catch (final Throwable e) {
            this.dao.rollbackTransaction(t);
			throw Throwables.propagate(e);
        } finally {
            this.dao.endTransaction(t);
        }
        return o;
    }

    @Inject
    public void setDao(final @EmbededDB GENgraphDAO dao) {
        this.dao = dao;
    }
}
