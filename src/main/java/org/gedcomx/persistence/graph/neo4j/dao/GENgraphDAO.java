package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.utils.IndexNodeNames;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public interface GENgraphDAO {

	Transaction beginTransaction();

	void commitTransaction(Transaction transaction);

	Node createNode();

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode);

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode, Map<RelationshipProperties, ?> properties);

	void delete(Node node);

	void delete(Relationship rel);

	void endTransaction(Transaction transaction);

	Node getNode(Long id);

	Object getNodeProperty(Node node, NodeProperties property);

	Iterable<Node> getNodesByRelationship(Node node, RelTypes relation, Direction dir, boolean ordered, RelationshipProperties index);

	Node getReferenceNode();

	Iterable<Relationship> getRelationships(Node node, Direction dir);

	Node getSingleNodeByRelationship(Node node, RelTypes relation, Direction dir);

	boolean hasRelationship(Node node, RelTypes relType, Direction dir);

	boolean hasSingleRelationship(Node node, RelTypes relType, Direction dir);

	void removeNodeFromIndex(IndexNodeNames indexName, Node node, NodeProperties property);

	void removeNodeProperty(Node node, NodeProperties property);

	Node setNodeProperties(Node node, Map<String, ?> metadata);

	Node setNodeProperty(Node node, NodeProperties property, Object value);

	void setNodeToIndex(IndexNodeNames indexName, Node node, NodeProperties property, Object value);

	Relationship setRelationshipProperties(Relationship rel, Map<RelationshipProperties, ?> properties);

	Relationship setRelationshipProperty(Relationship rel, RelationshipProperties key, Object value);

}
