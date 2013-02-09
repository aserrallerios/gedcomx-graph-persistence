package org.gedcomx.graph.persistence.neo4j.embeded.dao.impl;

import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GENgraphDAOImpl implements GENgraphDAO {

	private final GraphDatabaseService graphDb;

	public GENgraphDAOImpl(final String path, final Map<String, String> properties) {
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(path).setConfig(properties).newGraphDatabase();
		this.registerShutdownHook();
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
	public Node getNode(final Long id) {
		return this.graphDb.getNodeById(id);
	}

	@Override
	public Node getReferenceNode() {
		return this.graphDb.getReferenceNode();
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				GENgraphDAOImpl.this.graphDb.shutdown();
			}
		});
	}
}
