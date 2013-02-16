package org.gedcomx.graph.persistence.neo4j.embeded.dao;

import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.utils.IndexNodeNames;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelationshipProperties;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GENgraphDAO {

	Node addNodeProperties(Node node, Map<String, ?> metadata);

	Node addNodeProperty(Node underlyingNode, NodeProperties property, Object value);

	void addNodeToIndex(IndexNodeNames indexName, Node node, NodeProperties property, Object value);

	Relationship addRelationshipProperties(Relationship rel, Map<RelationshipProperties, ?> properties);

	Relationship addRelationshipProperty(Relationship rel, RelationshipProperties key, Object value);

	Node createNode();

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode);

	Node getNode(Long id);

	Object getNodeProperty(Node node, NodeProperties property);

	Node getReferenceNode();

}
