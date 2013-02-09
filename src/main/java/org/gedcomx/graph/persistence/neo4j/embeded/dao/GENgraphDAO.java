package org.gedcomx.graph.persistence.neo4j.embeded.dao;

import org.neo4j.graphdb.Node;

public interface GENgraphDAO {

	Node createNode();

	Node getNode(Long id);

	Node getReferenceNode();
}
