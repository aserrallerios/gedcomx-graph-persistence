package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.utils.IndexNodeNames;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GENgraphDAO {

	void beginTransaction();

	void commitTransaction();

	Node createNode();

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode);

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode, Map<RelationshipProperties, ?> properties);

	void delete(Node underlyingNode);

	void endTransaction();

	Node getNode(Long id);

	Object getNodeProperty(Node node, NodeProperties property);

	Iterable<Node> getNodesByRelationship(Node underlyingNode, RelTypes relation, Direction dir);

	Node getReferenceNode();

	Node getSingleNodeByRelationship(Node underlyingNode, RelTypes relation, Direction dir);

	boolean hasRelationship(Node underlyingNode, RelTypes relType);

	boolean hasSingleRelationship(Node node, RelTypes relType, Direction direction);

	void removeNodeFromIndex(IndexNodeNames indexName, Node node, NodeProperties property);

	Node setNodeProperties(Node node, Map<String, ?> metadata);

	Node setNodeProperty(Node underlyingNode, NodeProperties property, Object value);

	void setNodeToIndex(IndexNodeNames indexName, Node node, NodeProperties property, Object value);

	Relationship setRelationshipProperties(Relationship rel, Map<RelationshipProperties, ?> properties);

	Relationship setRelationshipProperty(Relationship rel, RelationshipProperties key, Object value);

}