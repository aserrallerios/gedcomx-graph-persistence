package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gedcomx.persistence.graph.neo4j.exception.InitializedDataBase;
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedDataBase;
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
	public Relationship createRelationship(final Node node, final GENgraphRelTypes reltype, final Node secondNode) {
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
	public Relationship createRelationship(final Node node, final GENgraphRelTypes reltype, final Node secondNode,
			final Map<String, ?> properties) {
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
	public Object getNodeProperty(final Node node, final String property) {
		return node.getProperty(property);
	}

	@Override
	public Iterable<Node> getNodesByRelationship(final Node node, final GENgraphRelTypes relation, final Direction dir,
			final boolean ordered, final String index) {
		final Iterable<Relationship> rels = node.getRelationships(relation, dir);

		if (ordered) {
			final List<Relationship> sortedRels = new ArrayList<>();
			int i;
			for (final Relationship rel : rels) {
				i = (Integer) rel.getProperty(index);
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
	public Object getRelationshipProperty(final Relationship rel, final String property) {
		return rel.getProperty(property);
	}

	@Override
	public Iterable<Relationship> getRelationships(final Node node, final Direction dir) {
		return node.getRelationships(dir);
	}

	@Override
	public Iterable<Relationship> getRelationships(final Node node, final GENgraphRelTypes relType, final Direction dir) {
		return node.getRelationships(relType, dir);
	}

	@Override
	public Node getSingleNodeByRelationship(final Node node, final GENgraphRelTypes relation, final Direction dir) {
		final Relationship rel = node.getSingleRelationship(relation, dir);
		return rel.getOtherNode(node);
	}

	@Override
	public Relationship getSingleRelationship(final Node node, final GENgraphRelTypes rel, final Direction dir) {
		return node.getSingleRelationship(rel, dir);
	}

	@Override
	public boolean hasRelationship(final Node node, final GENgraphRelTypes relType, final Direction dir) {
		final Iterable<Relationship> rels = node.getRelationships(relType, dir);

		return rels.iterator().hasNext();
	}

	@Override
	public boolean hasSingleRelationship(final Node node, final GENgraphRelTypes relType, final Direction dir) {
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
	public void removeNodeFromIndex(final String indexName, final Node node, final String property) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		index.remove(node, property);
	}

	@Override
	public void removeNodeProperty(final Node node, final String property) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.removeProperty(property);
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
	public Node setNodeProperty(final Node node, final String property, final Object value) {
		final Transaction tx = node.getGraphDatabase().beginTx();
		try {
			node.setProperty(property, value);
			tx.success();
		} finally {
			tx.finish();
		}
		return node;
	}

	@Override
	public void setNodeToIndex(final String indexName, final Node node, final String property, final Object value) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		index.add(node, property, value);
	}

	@Override
	public Relationship setRelationshipProperties(final Relationship rel, final Map<String, ?> properties) {
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

	@Override
	public Relationship setRelationshipProperty(final Relationship rel, final String propertyName, final Object propertyValue) {
		final Transaction tx = rel.getGraphDatabase().beginTx();
		try {
			rel.setProperty(propertyName, propertyValue);
			tx.success();
		} finally {
			tx.finish();
		}
		return rel;
	}
}
