package org.gedcomx.persistence.graph.neo4j.exceptions;

public class NodeIdentifierAlreadyExists extends Exception {

    final String id;

    public NodeIdentifierAlreadyExists(final String id) {
        this.id = id;
    }

}
