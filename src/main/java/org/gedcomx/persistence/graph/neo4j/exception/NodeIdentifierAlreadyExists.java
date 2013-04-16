package org.gedcomx.persistence.graph.neo4j.exception;

public class NodeIdentifierAlreadyExists extends RuntimeException {

    final String id;

    public NodeIdentifierAlreadyExists(final String id) {
        this.id = id;
    }

}
