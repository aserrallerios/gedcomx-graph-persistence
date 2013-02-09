package org.gedcomx.graph.persistence.neo4j.embeded.dao.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelationshipProperties;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public class GENgraphNodeUtils {

	public static Node addNodeProperties(final Node node, final Map<String, ?> properties) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			for (final Entry<String, ?> property : properties.entrySet()) {
				node.setProperty(property.getKey(), property.getValue());
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return node;
	}

	public static Node addNodeProperty(final Node node, final NodeProperties propertyName, final Object propertyValue) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.setProperty(propertyName.name(), propertyValue);
			tx.success();
		} finally {
			tx.finish();
		}
		return node;
	}

	public static Relationship addRelationshipProperties(final Relationship rel, final Map<String, ?> properties) {
		final Transaction tx = rel.getGraphDatabase().beginTx();
		try {
			for (final Entry<String, ?> property : properties.entrySet()) {
				rel.setProperty(property.getKey(), property.getValue());
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}

	public static Relationship addRelationshipProperty(final Relationship rel, final RelationshipProperties propertyName,
			final Object propertyValue) {
		final Transaction tx = rel.getGraphDatabase().beginTx();
		try {
			rel.setProperty(propertyName.name(), propertyValue);
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}

	public static Relationship createRelationship(final Node node, final RelTypes reltype, final Node secondNode) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		Relationship rel = null;
		try {
			rel = node.createRelationshipTo(secondNode, reltype);
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}

	public static Object getNodeProperty(final Node node, final NodeProperties property) {
		return node.getProperty(property.name());
	}

}
