package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import com.google.common.base.Optional;

public interface GENgraphDAO {

	Transaction beginTransaction();

	void commitTransaction(Transaction transaction);

	Node createNode();

	Node createNode(Label... labels);

	Relationship createRelationship(Node node, RelationshipType relType,
			Node secondNode);

	Relationship createRelationship(Node node, RelationshipType relType,
			Node secondNode, Map<String, ?> properties);

	void delete(Node node);

	void delete(Relationship rel);

	@Deprecated
	void endTransaction(Transaction transaction);

	ExecutionResult executeCypherQuery(String query, Map<String, Object> params);

	Iterable<Node> getAllNodes();

	Iterable<Relationship> getAllRelationships();

	Node getNode(Long id);

	Object getNodeProperty(Node node, String property);

	Iterable<Node> getNodesByRelationship(Node node, RelationshipType relation,
			Direction dir, boolean ordered, String index);

	Iterator<Node> getNodesFromIndex(String indexName, String property,
			String value);

	@Deprecated
	Node getReferenceNode();

	Object getRelationshipProperty(Relationship rel, String property);

	Iterable<Relationship> getRelationships(Node node, Direction dir);

	Iterable<Relationship> getRelationships(Node node,
			RelationshipType relType, Direction dir);

	Node getSingleNodeByRelationship(Node node, RelationshipType relation,
			Direction dir);

	Node getSingleNodeFromIndex(String indexName, String property, String value);

	Relationship getSingleRelationship(Node node, RelationshipType rel,
			Direction dir);

	boolean hasRelationship(Node node, RelationshipType relType, Direction dir);

	boolean hasSingleRelationship(Node node, RelationshipType relType,
			Direction dir);

	Node removeNodeProperty(Node node, String property);

	Relationship removeRelationshipProperty(Relationship rel, String property);

	void rollbackTransaction(Transaction transaction);

	Node setNodeProperties(Node node, Map<String, ?> metadata);

	Node setNodeProperty(Node node, String property, Optional<Object> value,
			boolean indexed, boolean unique, String indexName);

	Relationship setRelationshipProperties(Relationship rel,
			Map<String, ?> properties);

	Relationship setRelationshipProperty(Relationship rel, String key,
			Optional<Object> value);
}
