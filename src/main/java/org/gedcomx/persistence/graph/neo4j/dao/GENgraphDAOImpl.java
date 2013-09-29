package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.gedcomx.persistence.graph.neo4j.annotations.interceptors.Transactional;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.schema.IndexCreator;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.tooling.GlobalGraphOperations;
import org.testng.collections.Maps;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class GENgraphDAOImpl implements GENgraphDAO {

	private final GraphDatabaseService graphDb;
	private final ExecutionEngine executionEngine;
	private final GlobalGraphOperations operations;
	private final Schema schema;
	private final Map<String, IndexDefinition> indexes = Maps.newHashMap();

	@Inject
	GENgraphDAOImpl(final @Named("Neo4jDBPath") String bdpath,
			final @Named("Neo4jPropFile") String propsFile,
			final Multimap<Label, String> indexedProperties) {
		this.graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(bdpath)
				.loadPropertiesFromFile(propsFile).newGraphDatabase();
		this.schema = this.graphDb.schema();
		this.executionEngine = new ExecutionEngine(this.graphDb);
		this.operations = GlobalGraphOperations.at(this.graphDb);
		this.registerShutdownHook();
		this.initializeIndexes(indexedProperties);
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
	@Transactional
	public Node createNode() {
		return this.graphDb.createNode();
	}

	@Override
	@Transactional
	public Node createNode(final Label... labels) {
		return this.graphDb.createNode(labels);
	}

	@Override
	@Transactional
	public Relationship createRelationship(final Node node,
			final RelationshipType reltype, final Node secondNode) {
		return node.createRelationshipTo(secondNode, reltype);
	}

	@Override
	@Transactional
	public Relationship createRelationship(final Node node,
			final RelationshipType reltype, final Node secondNode,
			final Map<String, ?> properties) {
		final Relationship rel = node.createRelationshipTo(secondNode, reltype);
		this.setRelationshipProperties(rel, properties);
		return rel;
	}

	@Override
	@Transactional
	public void delete(final Node node) {
		node.delete();
	}

	@Override
	@Transactional
	public void delete(final Relationship rel) {
		rel.delete();
	}

	@Override
	@Deprecated
	public void endTransaction(final Transaction transaction) {
		transaction.finish();
	}

	@Override
	@Transactional
	public ExecutionResult executeCypherQuery(final String query,
			final Map<String, Object> params) {
		final ExecutionResult result = this.executionEngine.execute(query,
				params);
		return result;
	}

	@Override
	public Iterable<Node> getAllNodes() {
		return this.operations.getAllNodes();
	}

	@Override
	public Iterable<Relationship> getAllRelationships() {
		return this.operations.getAllRelationships();
	}

	@Override
	public Node getNode(final Long id) {
		return this.graphDb.getNodeById(id);
	}

	@Override
	public Object getNodeProperty(final Node node, final String property) {
		return node.getProperty(property, null);
	}

	@Override
	public Iterable<Node> getNodesByRelationship(final Node node,
			final RelationshipType relation, final Direction dir,
			final boolean ordered, final String index) {
		final Iterable<Relationship> rels = node
				.getRelationships(relation, dir);

		if (ordered) {
			final NavigableMap<Integer, Relationship> sortedRels = new TreeMap<>();
			int i;
			for (final Relationship rel : rels) {
				i = (Integer) rel.getProperty(index);
				sortedRels.put(i, rel);
			}
			final List<Node> nodes = new ArrayList<>();
			for (final Integer order : sortedRels.keySet()) {
				nodes.add(sortedRels.get(order).getOtherNode(node));
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
	public Iterator<Node> getNodesFromIndex(final String indexName,
			final String property, final String value) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		final IndexHits<Node> hits = index.get(property, value);
		return hits;
	}

	@Override
	@Deprecated
	public Node getReferenceNode() {
		return this.graphDb.getReferenceNode();
	}

	@Override
	public Object getRelationshipProperty(final Relationship rel,
			final String property) {
		return rel.getProperty(property);
	}

	@Override
	public Iterable<Relationship> getRelationships(final Node node,
			final Direction dir) {
		return node.getRelationships(dir);
	}

	@Override
	public Iterable<Relationship> getRelationships(final Node node,
			final RelationshipType relType, final Direction dir) {
		return node.getRelationships(relType, dir);
	}

	@Override
	public Node getSingleNodeByRelationship(final Node node,
			final RelationshipType relation, final Direction dir) {
		final Relationship rel = node.getSingleRelationship(relation, dir);
		if (rel != null) {
			return rel.getOtherNode(node);
		}
		return null;
	}

	@Override
	public Node getSingleNodeFromIndex(final String indexName,
			final String property, final String value) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		final IndexHits<Node> hits = index.get(property, value);
		return hits.getSingle();
	}

	@Override
	public Relationship getSingleRelationship(final Node node,
			final RelationshipType rel, final Direction dir) {
		return node.getSingleRelationship(rel, dir);
	}

	@Override
	public boolean hasRelationship(final Node node,
			final RelationshipType relType, final Direction dir) {
		final Iterable<Relationship> rels = node.getRelationships(relType, dir);
		return rels.iterator().hasNext();
	}

	@Override
	public boolean hasSingleRelationship(final Node node,
			final RelationshipType relType, final Direction dir) {
		final Relationship rel = node.getSingleRelationship(relType, dir);
		return rel == null ? false : true;
	}

	@Transactional
	private void initializeIndexes(
			final Multimap<Label, String> indexedProperties) {
		for (final Label label : indexedProperties.keySet()) {
			final IndexCreator indexCreator = this.schema.indexFor(label);
			for (final String property : indexedProperties.get(label)) {
				indexCreator.on(property);
			}
			this.indexes.put(label.name(), indexCreator.create());
		}
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				GENgraphDAOImpl.this.graphDb.shutdown();
			}
		});
	}

	@Transactional
	private void removeNodeFromIndex(final String indexName, final Node node,
			final String property) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		index.remove(node, property);
	}

	@Override
	@Transactional
	public Node removeNodeProperty(final Node node, final String property) {
		node.removeProperty(property);
		return node;
	}

	@Override
	@Transactional
	public Relationship removeRelationshipProperty(final Relationship rel,
			final String property) {
		rel.removeProperty(property);
		return rel;
	}

	@Override
	public void rollbackTransaction(final Transaction transaction) {
		transaction.failure();
	}

	@Override
	@Transactional
	public Node setNodeProperties(final Node node,
			final Map<String, ?> properties) {
		for (final Entry<String, ?> property : properties.entrySet()) {
			node.setProperty(property.getKey(), property.getValue());
		}
		return node;
	}

	@Override
	@Transactional
	public Node setNodeProperty(final Node node, final String property,
			final Optional<Object> value, final boolean indexed,
			final boolean unique, final String indexName) {
		if (!value.isPresent()) {
			this.removeNodeProperty(node, property);
		} else {
			node.setProperty(property, value.get());
		}
		if (indexed) {
			this.removeNodeFromIndex(indexName, node, property);
			if (value.isPresent()) {
				this.setNodeToIndex(indexName, node, property, value.get(),
						unique);
			}
		}
		return node;
	}

	@Transactional
	private void setNodeToIndex(final String indexName, final Node node,
			final String property, final Object value, final boolean unique) {
		final Index<Node> index = this.graphDb.index().forNodes(indexName);
		if (unique) {
			index.putIfAbsent(node, property, value);
		} else {
			index.add(node, property, value);
		}
	}

	@Override
	@Transactional
	public Relationship setRelationshipProperties(final Relationship rel,
			final Map<String, ?> properties) {
		for (final Entry<String, ?> property : properties.entrySet()) {
			rel.setProperty(property.getKey(), property.getValue());
		}
		return rel;
	}

	@Override
	@Transactional
	public Relationship setRelationshipProperty(final Relationship rel,
			final String property, final Optional<Object> value) {
		if (!value.isPresent()) {
			this.removeRelationshipProperty(rel, property);
		} else {
			rel.setProperty(property, value.get());
		}
		return rel;
	}
}
