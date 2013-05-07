package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public interface GENgraphDAO {

    Transaction beginTransaction();

    void commitTransaction(Transaction transaction);

    Node createNode();

    Relationship createRelationship(Node node, RelationshipType relType,
            Node secondNode);

    Relationship createRelationship(Node node, RelationshipType relType,
            Node secondNode, Map<String, ?> properties);

    void delete(Node node);

    void delete(Relationship rel);

    void endTransaction(Transaction transaction);

    ExecutionResult executeCypherQuery(String query);

    void rollbackTransaction(Transaction transaction);

    Iterable<Node> getAllNodes();

    Iterable<Relationship> getAllRelationships();

    Node getNode(Long id);

    Object getNodeProperty(Node node, String property);

    Iterable<Node> getNodesByRelationship(Node node, RelationshipType relation,
            Direction dir, boolean ordered, String index);

    Iterator<Node> getNodesFromIndex(String indexName, String property,
            String value);

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

    void removeNodeProperty(Node node, String property);

    Node setNodeProperties(Node node, Map<String, ?> metadata);

    Node setNodeProperty(Node node, String property, Object value,
            boolean indexed, boolean unique, String indexName);

    Relationship setRelationshipProperties(Relationship rel,
            Map<String, ?> properties);

    Relationship setRelationshipProperty(Relationship rel, String key,
            Object value);

}
