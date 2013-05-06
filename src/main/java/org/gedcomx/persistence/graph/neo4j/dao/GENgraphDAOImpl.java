package org.gedcomx.persistence.graph.neo4j.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.tooling.GlobalGraphOperations;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class GENgraphDAOImpl implements GENgraphDAO {

    private final GraphDatabaseService graphDb;
    private final ExecutionEngine executionEngine;
    private final GlobalGraphOperations operations;

    @Inject
    GENgraphDAOImpl(final @Named("Neo4jDBPath") String bdpath,
            final @Named("Neo4jPropFile") String propsFile) {
        this.graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(bdpath)
                .loadPropertiesFromFile(propsFile).newGraphDatabase();
        this.executionEngine = new ExecutionEngine(this.graphDb);
        this.operations = GlobalGraphOperations.at(this.graphDb);
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
    public Relationship createRelationship(final Node node,
            final RelationshipType reltype, final Node secondNode) {
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
    public Relationship createRelationship(final Node node,
            final RelationshipType reltype, final Node secondNode,
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
    public ExecutionResult executeCypherQuery(final String query) {
        final ExecutionResult result = this.executionEngine.execute(query);
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

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                GENgraphDAOImpl.this.graphDb.shutdown();
            }
        });
    }

    private void removeNodeFromIndex(final String indexName, final Node node,
            final String property) {
        final Index<Node> index = this.graphDb.index().forNodes(indexName);
        final Transaction tx = this.graphDb.beginTx();
        try {
            index.remove(node, property);
            tx.success();
        } finally {
            tx.finish();
        }
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
    public Node setNodeProperties(final Node node,
            final Map<String, ?> properties) {
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
    public Node setNodeProperty(final Node node, final String property,
            final Object value, final boolean indexed, final boolean unique,
            final String indexName) {
        final Transaction tx = node.getGraphDatabase().beginTx();
        try {
            if (value == null) {
                this.removeNodeProperty(node, property);
            } else {
                node.setProperty(property, value);
            }
            if (indexed) {
                this.removeNodeFromIndex(indexName, node, property);
                if (value != null) {
                    this.setNodeToIndex(indexName, node, property, value,
                            unique);
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
        return node;
    }

    private void setNodeToIndex(final String indexName, final Node node,
            final String property, final Object value, final boolean unique) {
        final Index<Node> index = this.graphDb.index().forNodes(indexName);
        final Transaction tx = this.graphDb.beginTx();
        try {
            if (unique) {
                index.putIfAbsent(node, property, value);
            } else {
                index.add(node, property, value);
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    @Override
    public Relationship setRelationshipProperties(final Relationship rel,
            final Map<String, ?> properties) {
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
    public Relationship setRelationshipProperty(final Relationship rel,
            final String propertyName, final Object propertyValue) {
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
