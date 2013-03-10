package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gedcomx.persistence.graph.neo4j.exception.InitializedDataBase;
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedDataBase;
import org.gedcomx.persistence.graph.neo4j.utils.IndexNodeNames;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelationshipProperties;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

public class GENgraphDAOImpl implements GENgraphDAO {

	private static GENgraphDAOImpl instance;

	public static GENgraphDAO getInstance() {
		if ((GENgraphDAOImpl.instance != null) && (GENgraphDAOImpl.instance.graphDb != null)) {
			return GENgraphDAOImpl.instance;
		} else {
			throw new UninitializedDataBase();
		}
	}

	public static void initGENgraphDAO(final String path, final Map<String, String> properties) {
		if ((GENgraphDAOImpl.instance == null) || (GENgraphDAOImpl.instance.graphDb == null)) {
			GENgraphDAOImpl.instance = new GENgraphDAOImpl(path, properties);
		} else {
			throw new InitializedDataBase();
		}
	}

	private final GraphDatabaseService graphDb;

	private GENgraphDAOImpl(final String path, final Map<String, String> properties) {
		if (properties != null) {
			this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(path).setConfig(properties).newGraphDatabase();
		} else {
			this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		}
		this.registerShutdownHook();
	}

	@Override
	public Transaction beginTransaction() {
		return this.graphDb.beginTx();
	}

	@Override
	public void commitTransaction(final Transaction transaction) {
		transaction.success();

	}

	@Override
	public Node createNode() {
		final Transaction tx = this.graphDb.beginTx();
		Node node = null;
		try {
			node = this.graphDb.createNode();
			tx.success();
		} finally {
			tx.finish();
		}
		return node;
	}

	@Override
	public Relationship createRelationship(final Node node, final RelTypes reltype, final Node secondNode) {
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

	@Override
	public Relationship createRelationship(final Node node, final RelTypes reltype, final Node secondNode,
			final Map<RelationshipProperties, ?> properties) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		Relationship rel = null;
		try {
			rel = node.createRelationshipTo(secondNode, reltype);
			this.setRelationshipProperties(rel, properties);
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}

	@Override
	public void delete(final Node node) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.delete();
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void delete(final Relationship rel) {
		final Transaction tx = rel.getGraphDatabase().beginTx();
		try {
			rel.delete();
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public void endTransaction(final Transaction transaction) {
		transaction.finish();

	}

	@Override
	public Node getNode(final Long id) {
		return this.graphDb.getNodeById(id);
	}

	@Override
	public Object getNodeProperty(final Node node, final NodeProperties property) {
		return node.getProperty(property.name());
	}

	@Override
	public Iterable<Node> getNodesByRelationship(final Node node, final RelTypes relation, final Direction dir, final boolean ordered,
			final RelationshipProperties index) {
		final Iterable<Relationship> rels = node.getRelationships(relation, dir);

		if (ordered) {
			final List<Relationship> sortedRels = new ArrayList<>();
			int i;
			for (final Relationship rel : rels) {
				i = (Integer) rel.getProperty(index.name());
				sortedRels.add(i, rel);
			}
			final List<Node> nodes = new ArrayList<>();
			for (final Relationship rel : sortedRels) {
				nodes.add(rel.getOtherNode(node));
			}
			return nodes;
		} else {
			final List<Node> nodes = new ArrayList<>();
			for (final Relationship rel : rels) {
				nodes.add(rel.getOtherNode(node));
			}
			return nodes;
		}
	}

	@Override
	public Node getReferenceNode() {
		return this.graphDb.getReferenceNode();
	}

	@Override
	public Iterable<Relationship> getRelationships(final Node node, final Direction dir) {
		return node.getRelationships(dir);
	}

	@Override
	public Node getSingleNodeByRelationship(final Node node, final RelTypes relation, final Direction dir) {
		final Relationship rel = node.getSingleRelationship(relation, dir);
		return rel.getOtherNode(node);
	}

	@Override
	public boolean hasRelationship(final Node node, final RelTypes relType, final Direction dir) {
		final Iterable<Relationship> rels = node.getRelationships(relType, dir);

		return rels.iterator().hasNext();
	}

	@Override
	public boolean hasSingleRelationship(final Node node, final RelTypes relType, final Direction dir) {
		final Relationship rel = node.getSingleRelationship(relType, dir);
		return rel == null ? false : true;
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				GENgraphDAOImpl.this.graphDb.shutdown();
			}
		});
	}

	@Override
	public void removeNodeFromIndex(final IndexNodeNames indexName, final Node node, final NodeProperties property) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName.name());
		index.remove(node, property.name());
	}

	@Override
	public void removeNodeProperty(final Node node, final NodeProperties property) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.removeProperty(property.name());
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Override
	public Node setNodeProperties(final Node node, final Map<String, ?> properties) {
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

	@Override
	public Node setNodeProperty(final Node node, final NodeProperties property, final Object value) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.setProperty(property.name(), value);
			tx.success();
		} finally {
			tx.finish();
		}
		return node;
	}

	@Override
	public void setNodeToIndex(final IndexNodeNames indexName, final Node node, final NodeProperties property, final Object value) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName.name());
		index.add(node, property.name(), value);
	}

	@Override
	public Relationship setRelationshipProperties(final Relationship rel, final Map<RelationshipProperties, ?> properties) {
		final Transaction tx = rel.getGraphDatabase().beginTx();
		try {
			for (final Entry<RelationshipProperties, ?> property : properties.entrySet()) {
				rel.setProperty(property.getKey().name(), property.getValue());
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}

	@Override
	public Relationship setRelationshipProperty(final Relationship rel, final RelationshipProperties propertyName,
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
}
