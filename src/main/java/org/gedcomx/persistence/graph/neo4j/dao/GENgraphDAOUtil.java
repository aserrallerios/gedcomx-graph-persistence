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

public class GENgraphDAOUtil {

	public static Transaction beginTransaction() {
		return GENgraphDAOUtil.getDao().beginTransaction();
	}

	public static void commitTransaction(final Transaction transaction) {
		GENgraphDAOUtil.getDao().commitTransaction(transaction);
	}

	public static Node createNode() {
		return GENgraphDAOUtil.getDao().createNode();
	}

	public static Relationship createRelationship(final Node node, final RelTypes relType, final Node secondNode) {
		return GENgraphDAOUtil.getDao().createRelationship(node, relType, secondNode);
	}

	public static Relationship createRelationship(final Node node, final RelTypes relType, final Node secondNode,
			final Map<RelationshipProperties, ?> properties) {
		return GENgraphDAOUtil.getDao().createRelationship(node, relType, secondNode, properties);
	}

	public static void delete(final Node node) {
		GENgraphDAOUtil.getDao().delete(node);
	}

	public static void delete(final Relationship rel) {
		GENgraphDAOUtil.getDao().delete(rel);

	}

	public static void endTransaction(final Transaction transaction) {
		GENgraphDAOUtil.getDao().endTransaction(transaction);
	}

	private static GENgraphDAO getDao() {
		return GENgraphDAOImpl.getInstance();
	}

	public static Node getNode(final Long id) {
		return GENgraphDAOUtil.getDao().getNode(id);
	}

	public static Object getNodeProperty(final Node node, final NodeProperties property) {
		return GENgraphDAOUtil.getDao().getNodeProperty(node, property);
	}

	public static Iterable<Node> getNodesByRelationship(final Node node, final RelTypes relation, final Direction dir,
			final boolean ordered, final RelationshipProperties index) {
		return GENgraphDAOUtil.getDao().getNodesByRelationship(node, relation, dir, ordered, index);
	}

	public static Node getReferenceNode() {
		return GENgraphDAOUtil.getDao().getReferenceNode();
	}

	public static Object getRelationshipProperty(final Relationship rel, final RelationshipProperties property) {
		return GENgraphDAOUtil.getDao().getRelationshipProperty(rel, property);
	}

	public static Iterable<Relationship> getRelationships(final Node node, final Direction dir) {
		return GENgraphDAOUtil.getDao().getRelationships(node, dir);
	}

	public static Iterable<Relationship> getRelationships(final Node node, final RelTypes relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().getRelationships(node, relType, dir);
	}

	public static Node getSingleNodeByRelationship(final Node node, final RelTypes relation, final Direction dir) {
		return GENgraphDAOUtil.getDao().getSingleNodeByRelationship(node, relation, dir);
	}

	public static Relationship getSingleRelationship(final Node node, final RelTypes rel, final Direction dir) {
		return GENgraphDAOUtil.getDao().getSingleRelationship(node, rel, dir);
	}

	public static boolean hasRelationship(final Node node, final RelTypes relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().hasRelationship(node, relType, dir);
	}

	public static boolean hasSingleRelationship(final Node node, final RelTypes relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().hasSingleRelationship(node, relType, dir);
	}

	public static void removeNodeFromIndex(final IndexNodeNames indexName, final Node node, final NodeProperties property) {
		GENgraphDAOUtil.getDao().removeNodeFromIndex(indexName, node, property);
	}

	public static void removeNodeProperty(final Node node, final NodeProperties property) {
		GENgraphDAOUtil.getDao().removeNodeProperty(node, property);
	}

	public static Node setNodeProperties(final Node node, final Map<String, ?> metadata) {
		return GENgraphDAOUtil.getDao().setNodeProperties(node, metadata);
	}

	public static Node setNodeProperty(final Node node, final NodeProperties property, final Object value) {
		return GENgraphDAOUtil.getDao().setNodeProperty(node, property, value);
	}

	public static void setNodeToIndex(final IndexNodeNames indexName, final Node node, final NodeProperties property, final Object value) {
		GENgraphDAOUtil.getDao().setNodeToIndex(indexName, node, property, value);
	}

	public static Relationship setRelationshipProperties(final Relationship rel, final Map<RelationshipProperties, ?> properties) {
		return GENgraphDAOUtil.getDao().setRelationshipProperties(rel, properties);
	}

	public static Relationship setRelationshipProperty(final Relationship rel, final RelationshipProperties key, final Object value) {
		return GENgraphDAOUtil.getDao().setRelationshipProperty(rel, key, value);
	}

}
