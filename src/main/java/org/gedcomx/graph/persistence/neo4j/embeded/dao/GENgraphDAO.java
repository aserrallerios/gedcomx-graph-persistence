package org.gedcomx.graph.persistence.neo4j.embeded.dao;

import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.utils.IndexNodeNames;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelationshipProperties;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GENgraphDAO {

	Node createNode();

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode);

	Relationship createRelationship(Node node, RelTypes relType, Node secondNode, Map<RelationshipProperties, ?> properties);

	Node getNode(Long id);

	Object getNodeProperty(Node node, NodeProperties property);

	Node getReferenceNode();

	Node setNodeProperties(Node node, Map<String, ?> metadata);

	Node setNodeProperty(Node underlyingNode, NodeProperties property, Object value);

	void setNodeToIndex(IndexNodeNames indexName, Node node, NodeProperties property, Object value);

	Relationship setRelationshipProperties(Relationship rel, Map<RelationshipProperties, ?> properties);

	Relationship setRelationshipProperty(Relationship rel, RelationshipProperties key, Object value);

}
