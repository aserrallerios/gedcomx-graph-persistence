package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.reflections.Reflections;

public class GENgraphDAOUtil {

	private static GENgraphDAO dao;

	public static Transaction beginTransaction() {
		return GENgraphDAOUtil.getDao().beginTransaction();
	}

	public static void commitTransaction(final Transaction transaction) {
		GENgraphDAOUtil.getDao().commitTransaction(transaction);
	}

	public static Node createNode() {
		return GENgraphDAOUtil.getDao().createNode();
	}

	public static Relationship createRelationship(final Node node, final RelationshipType relType, final Node secondNode) {
		return GENgraphDAOUtil.getDao().createRelationship(node, relType, secondNode);
	}

	public static Relationship createRelationship(final Node node, final RelationshipType relType, final Node secondNode,
			final Map<String, ?> properties) {
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

	public static ExecutionResult executeCypherQuery(final String query) {
		return GENgraphDAOUtil.executeCypherQuery(query);
	}

	private static GENgraphDAO getDao() {
		if (GENgraphDAOUtil.dao == null) {
			final Reflections reflections = new Reflections(GENgraphDAOUtil.class.getPackage().getName());

			for (final Class<? extends GENgraphDAO> subclass : reflections.getSubTypesOf(GENgraphDAO.class)) {
				if (subclass != null) {
					try {
						GENgraphDAOUtil.dao = subclass.newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return GENgraphDAOUtil.dao;
	}

	public static Node getNode(final Long id) {
		return GENgraphDAOUtil.getDao().getNode(id);
	}

	public static Object getNodeProperty(final Node node, final String property) {
		return GENgraphDAOUtil.getDao().getNodeProperty(node, property);
	}

	public static Iterable<Node> getNodesByRelationship(final Node node, final RelationshipType relation, final Direction dir,
			final boolean ordered, final String index) {
		return GENgraphDAOUtil.getDao().getNodesByRelationship(node, relation, dir, ordered, index);
	}

	public static Iterator<Node> getNodesFromIndex(final String indexName, final String property, final String value) {
		return GENgraphDAOUtil.getDao().getNodesFromIndex(indexName, property, value);
	}

	public static Node getReferenceNode() {
		return GENgraphDAOUtil.getDao().getReferenceNode();
	}

	public static Object getRelationshipProperty(final Relationship rel, final String property) {
		return GENgraphDAOUtil.getDao().getRelationshipProperty(rel, property);
	}

	public static Iterable<Relationship> getRelationships(final Node node, final Direction dir) {
		return GENgraphDAOUtil.getDao().getRelationships(node, dir);
	}

	public static Iterable<Relationship> getRelationships(final Node node, final RelationshipType relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().getRelationships(node, relType, dir);
	}

	public static Node getSingleNodeByRelationship(final Node node, final RelationshipType relation, final Direction dir) {
		return GENgraphDAOUtil.getDao().getSingleNodeByRelationship(node, relation, dir);
	}

	public static Node getSingleNodeFromIndex(final String indexName, final String property, final String value) {
		return GENgraphDAOUtil.getDao().getSingleNodeFromIndex(indexName, property, value);
	}

	public static Relationship getSingleRelationship(final Node node, final RelationshipType rel, final Direction dir) {
		return GENgraphDAOUtil.getDao().getSingleRelationship(node, rel, dir);
	}

	public static boolean hasRelationship(final Node node, final RelationshipType relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().hasRelationship(node, relType, dir);
	}

	public static boolean hasSingleRelationship(final Node node, final RelationshipType relType, final Direction dir) {
		return GENgraphDAOUtil.getDao().hasSingleRelationship(node, relType, dir);
	}

	public static void removeNodeProperty(final Node node, final String property) {
		GENgraphDAOUtil.getDao().removeNodeProperty(node, property);
	}

	public static Node setNodeProperties(final Node node, final Map<String, ?> metadata) {
		return GENgraphDAOUtil.getDao().setNodeProperties(node, metadata);
	}

	public static Node setNodeProperty(final Node node, final String property, final Object value) {
		return GENgraphDAOUtil.getDao().setNodeProperty(node, property, value, false, false, null);
	}

	public static Node setNodeProperty(final Node node, final String property, final Object value, final boolean indexed,
			final boolean unique, final String indexName) {
		return GENgraphDAOUtil.getDao().setNodeProperty(node, property, value, indexed, unique, indexName);
	}

	public static Relationship setRelationshipProperties(final Relationship rel, final Map<String, ?> properties) {
		return GENgraphDAOUtil.getDao().setRelationshipProperties(rel, properties);
	}

	public static Relationship setRelationshipProperty(final Relationship rel, final String key, final Object value) {
		return GENgraphDAOUtil.getDao().setRelationshipProperty(rel, key, value);
	}

}
