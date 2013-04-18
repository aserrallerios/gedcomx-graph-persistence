package org.gedcomx.persistence.graph.neo4j.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.gedcomx.persistence.graph.neo4j.annotations.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.neo4j.graphdb.Transaction;

import com.google.inject.Inject;

public class TransactionWrapper implements MethodInterceptor {

    GENgraphDAO dao;

    @Inject
    public TransactionWrapper() {
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        final Transaction t = this.dao.beginTransaction();
        Object o = null;
        try {
            o = invocation.proceed();
            t.success();
        } finally {
            t.finish();
        }
        return o;
    }

    @Inject
    public void setDao(final @EmbededDB GENgraphDAO dao) {
        this.dao = dao;
    }
}
