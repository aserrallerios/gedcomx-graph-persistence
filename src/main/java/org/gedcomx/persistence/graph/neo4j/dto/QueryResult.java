package org.gedcomx.persistence.graph.neo4j.dto;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class QueryResult {

    private final List<Node> nodes;
    private final List<Relationship> relationships;

    public QueryResult(final List<Node> nodes,
            final List<Relationship> relationships) {
        this.nodes = nodes;
        this.relationships = relationships;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public List<Relationship> getRelationships() {
        return this.relationships;
    }
}
