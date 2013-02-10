package org.gedcomx.graph.persistence.neo4j.embeded.dao;

import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GENgraphDAO {

	Node addNodeProperties(Node rootNode, Map<String, ?> metadata);

	Node addNodeProperty(Node underlyingNode, NodeProperties property, Object value);

	Node createNode();

	Relationship createRelationship(Node rootNode, RelTypes relType, Node secondNode);

	Node getNode(Long id);

	Object getNodeProperty(Node underlyingNode, NodeProperties property);

	Node getReferenceNode();
}
