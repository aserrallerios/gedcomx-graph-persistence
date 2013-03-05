package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.utils.IndexNodeNames;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GENgraphDAO {

	Node createNode();

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode);

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode, Map<RelationshipProperties, ?> properties);

	Node getNode(Long id);

	Object getNodeProperty(Node node, NodeProperties property);

	Node getReferenceNode();

	boolean hasRelationship(Node underlyingNode, RelTypes relType);

	void removeNodeFromIndex(IndexNodeNames indexName, Node node, NodeProperties property);

	Node setNodeProperties(Node node, Map<String, ?> metadata);

	Node setNodeProperty(Node underlyingNode, NodeProperties property, Object value);

	void setNodeToIndex(IndexNodeNames indexName, Node node, NodeProperties property, Object value);

	Relationship setRelationshipProperties(Relationship rel, Map<RelationshipProperties, ?> properties);

	Relationship setRelationshipProperty(Relationship rel, RelationshipProperties key, Object value);

}
