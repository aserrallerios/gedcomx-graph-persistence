package org.gedcomx.persistence.graph.neo4j.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UninitializedNode;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.IndexNames;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import com.google.inject.Inject;

public abstract class NodeWrapper {

    @Inject
    @EmbededDB
    private static GENgraphDAO dao;
    @Inject
    private static NodeTypeMapper mapper;
    private final Node underlyingNode;

    protected NodeWrapper(final Node underlyingNode)
            throws MissingFieldException, UnknownNodeType {
        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            this.underlyingNode = underlyingNode;
            if (!this.getNodeType().equals(this.getAnnotatedNodeType())) {
                throw new UnknownNodeType();
            }
            this.resolveReferences();
            this.validateUnderlyingNode();
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }
    }

    protected NodeWrapper(final Object... properties)
            throws MissingFieldException {
        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            this.underlyingNode = NodeWrapper.dao.createNode();
            this.setNodeType(this.getAnnotatedNodeType());
            if ((properties != null) && (properties.length > 0)) {
                this.setRequiredProperties(properties);
            }
            this.resolveReferences();
            this.validateUnderlyingNode();
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }
    }

    protected NodeWrapper(final Object gedcomXObject)
            throws MissingFieldException {
        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            this.underlyingNode = NodeWrapper.dao.createNode();
            this.setNodeType(this.getAnnotatedNodeType());
            this.setGedcomXProperties(gedcomXObject);
            this.setGedcomXRelations(gedcomXObject);
            this.resolveReferences();
            this.validateUnderlyingNode();
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }
    }

    protected void addRelationship(final RelationshipTypes relType,
            final NodeWrapper node) {
        if (relType.isOrdered()) {
            final Transaction t = NodeWrapper.dao.beginTransaction();
            try {
                final int index = this.getMaxRelationshipIndex(relType);
                final Map<String, Integer> properties = new HashMap<>();
                properties.put(RelationshipProperties.INDEX.name(),
                        Integer.valueOf(index + 1));
                NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                        relType, node.getUnderlyingNode(), properties);
                NodeWrapper.dao.commitTransaction(t);
            } finally {
                NodeWrapper.dao.endTransaction(t);
            }
        } else {
            NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                    relType, node.getUnderlyingNode());
        }
    }

    protected void createReferenceRelationship(final RelationshipTypes relType,
            final NodeProperties property) {
        final Object val = this.getProperty(property);

        if (val instanceof String) {
            final URI uri = new URI((String) val);

            if ((uri != null)) {
                NodeWrapper.dao.hasSingleRelationship(this.getUnderlyingNode(),
                        relType, Direction.OUTGOING);

                final NodeWrapper wrapper = this
                        .findNodeByReference(new ResourceReference(uri));
                if (wrapper != null) {
                    final Transaction t = NodeWrapper.dao.beginTransaction();
                    try {
                        this.createReferenceRelationship(relType, wrapper);
                        NodeWrapper.dao.removeNodeProperty(
                                this.getUnderlyingNode(), property.toString());
                        NodeWrapper.dao.commitTransaction(t);
                    } finally {
                        NodeWrapper.dao.endTransaction(t);
                    }
                }
            }
        } else if (val instanceof List) {
            for (final Object aux : (List<?>) val) {
                final URI uri = new URI((String) aux);

                if ((uri != null) && !uri.toURI().isAbsolute()) {
                    NodeWrapper.dao.hasSingleRelationship(
                            this.getUnderlyingNode(), relType,
                            Direction.OUTGOING);

                    final NodeWrapper wrapper = this
                            .findNodeByReference(new ResourceReference(uri));
                    if (wrapper != null) {
                        final Transaction t = NodeWrapper.dao
                                .beginTransaction();
                        try {
                            this.addRelationship(relType, wrapper);
                            NodeWrapper.dao.removeNodeProperty(
                                    this.getUnderlyingNode(),
                                    property.toString());
                            NodeWrapper.dao.commitTransaction(t);
                        } finally {
                            NodeWrapper.dao.endTransaction(t);
                        }
                    }
                }
            }
        }
    }

    protected void createReferenceRelationship(final RelationshipTypes relType,
            final NodeWrapper node) {
        final boolean rel = NodeWrapper.dao.hasSingleRelationship(
                this.getUnderlyingNode(), relType, Direction.OUTGOING);

        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            if (!rel) {
                NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                        relType, node.underlyingNode);
            } else {
                NodeWrapper.dao.delete(NodeWrapper.dao.getSingleRelationship(
                        this.getUnderlyingNode(), relType, Direction.OUTGOING));
                NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                        relType, node.underlyingNode);
            }
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }
    }

    protected void createRelationship(final RelationshipTypes relType,
            final NodeWrapper node) {
        final boolean rel = NodeWrapper.dao.hasSingleRelationship(
                this.getUnderlyingNode(), relType, Direction.OUTGOING);

        if (!rel) {
            NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                    relType, node.underlyingNode);
        } else {
            final Transaction t = NodeWrapper.dao.beginTransaction();
            try {
                final NodeWrapper wrapper = this.getNodeByRelationship(
                        node.getClass(), relType, Direction.OUTGOING);
                wrapper.delete();
                NodeWrapper.dao.createRelationship(this.getUnderlyingNode(),
                        relType, node.underlyingNode);
                NodeWrapper.dao.commitTransaction(t);
            } finally {
                NodeWrapper.dao.endTransaction(t);
            }
        }
    }

    public void delete() {
        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            this.deleteAllReferences();
            this.deleteIncomingRelationships();
            this.deleteSelf();
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }

    }

    protected abstract void deleteAllReferences();

    private void deleteIncomingRelationships() {
        final Iterable<Relationship> rels = NodeWrapper.dao.getRelationships(
                this.getUnderlyingNode(), Direction.INCOMING);

        for (final Relationship rel : rels) {
            NodeWrapper.dao.delete(rel);
        }
    }

    protected void deleteReference(final RelationshipTypes rel) {
        NodeWrapper.dao.delete(NodeWrapper.dao.getSingleRelationship(
                this.getUnderlyingNode(), rel, Direction.OUTGOING));
    }

    protected <T extends NodeWrapper> void deleteReferencedNode(final T wrapper) {
        if (wrapper != null) {
            wrapper.delete();
        }
    }

    protected <T extends NodeWrapper> void deleteReferencedNodes(
            final List<T> type) {
        for (final T wrapper : type) {
            wrapper.delete();
        }
    }

    protected void deleteReferences(final RelationshipTypes rel) {
        for (final Relationship relationship : NodeWrapper.dao
                .getRelationships(this.getUnderlyingNode(), rel,
                        Direction.OUTGOING)) {
            NodeWrapper.dao.delete(relationship);
        }
    }

    private void deleteSelf() {
        NodeWrapper.dao.delete(this.getUnderlyingNode());
    }

    @Override
    public boolean equals(final Object object) {
        return this.getUnderlyingNode().equals(object);
    }

    private NodeWrapper findNodeByReference(final ResourceReference reference) {
        final URI uri = reference.getResource();

        NodeWrapper wrapper = null;
        if (!uri.toURI().isAbsolute()) {
            Node node = NodeWrapper.dao.getSingleNodeFromIndex(
                    IndexNames.IDS.toString(), GenericProperties.ID.toString(),
                    uri.toURI().getFragment());
            if (node == null) {
                node = NodeWrapper.dao.getSingleNodeFromIndex(
                        IndexNames.IDS.toString(),
                        GenericProperties.ID.toString(), uri.toURI().getPath());
            }
            if (node != null) {
                final String type = (String) NodeWrapper.dao.getNodeProperty(
                        node, GenericProperties.NODE_TYPE.toString());
                wrapper = NodeWrapper.mapper.createNode(
                        NodeTypes.valueOf(type), node);
            }
        }
        return wrapper;
    }

    protected String getAnnotatedNodeType() {
        return NodeWrapper.mapper.getTypeByWrapper(this.getClass()).name();
    }

    public abstract <T extends Object> T getGedcomX();

    protected <T> List<T> getGedcomXList(final Class<T> type,
            final List<? extends NodeWrapper> nodes) {
        final List<T> list = new ArrayList<>();
        for (final NodeWrapper a : nodes) {
            list.add(type.cast(a.getGedcomX()));
        }
        return list;
    }

    private int getMaxRelationshipIndex(final RelationshipTypes relType) {
        final Iterable<Relationship> rels = NodeWrapper.dao.getRelationships(
                this.getUnderlyingNode(), relType, Direction.OUTGOING);

        int max = 0;
        for (final Relationship rel : rels) {
            final Integer value = (Integer) NodeWrapper.dao
                    .getRelationshipProperty(rel,
                            RelationshipProperties.INDEX.name());
            max = value > max ? value : max;
        }
        // TODO: traversal
        return max;
    }

    protected <T extends NodeWrapper> T getNodeByRelationship(
            final Class<T> type, final RelationshipTypes relation) {
        return this.getNodeByRelationship(type, relation, Direction.OUTGOING);
    }

    protected <T extends NodeWrapper> T getNodeByRelationship(
            final Class<T> type, final RelationshipTypes relation,
            final Direction dir) {
        final Node node = NodeWrapper.dao.getSingleNodeByRelationship(
                this.getUnderlyingNode(), relation, dir);
        if (node != null) {
            return NodeWrapper.mapper.createNode(type, node);
        }
        return null;
    }

    private NodeWrapper getNodeByRelationship(final RelationshipTypes relation,
            final Direction dir) {
        final Node node = NodeWrapper.dao.getSingleNodeByRelationship(
                this.getUnderlyingNode(), relation, dir);
        final String nodeType = (String) NodeWrapper.dao.getNodeProperty(node,
                GenericProperties.NODE_TYPE.name());

        final Class<? extends NodeWrapper> type = NodeWrapper.mapper
                .getWrapperByType(NodeTypes.valueOf(nodeType));
        return NodeWrapper.mapper.createNode(type, node);
    }

    protected <T extends NodeWrapper> List<T> getNodesByRelationship(
            final Class<T> type, final RelationshipTypes relation) {
        return this.getNodesByRelationship(type, relation, Direction.OUTGOING);
    }

    private <T extends NodeWrapper> List<T> getNodesByRelationship(
            final Class<T> type, final RelationshipTypes relation,
            final Direction dir) {
        final Iterable<Node> nodes = NodeWrapper.dao.getNodesByRelationship(
                this.getUnderlyingNode(), relation, dir,
                (dir == Direction.OUTGOING) && relation.isOrdered(),
                RelationshipProperties.INDEX.name());

        final List<T> wrappers = new ArrayList<>();
        for (final Node node : nodes) {
            wrappers.add(NodeWrapper.mapper.createNode(type, node));
        }
        return wrappers;
    }

    public String getNodeType() {
        return (String) NodeWrapper.dao.getNodeProperty(
                this.getUnderlyingNode(), GenericProperties.NODE_TYPE.name());
    }

    protected NodeWrapper getParentNode(final RelationshipTypes relation) {
        return this.getNodeByRelationship(relation, Direction.INCOMING);
    }

    protected Object getProperty(final NodeProperties property) {
        return NodeWrapper.dao.getNodeProperty(this.getUnderlyingNode(),
                property.name());
    }

    protected ResourceReference getResourceReference() {
        return null;
    }

    private Node getUnderlyingNode() {
        if (this.underlyingNode == null) {
            throw new UninitializedNode();
        }
        return this.underlyingNode;
    }

    protected URI getURI() {
        return null;
    }

    protected List<ResourceReference> getURIListProperties(
            final NodeProperties property) {
        final String[] values = (String[]) NodeWrapper.dao.getNodeProperty(
                this.getUnderlyingNode(), property.name());
        final List<ResourceReference> resourceList = new ArrayList<>();
        for (final String uri : values) {
            resourceList.add(new ResourceReference(new URI(uri)));
        }
        return resourceList;
    }

    @Override
    public int hashCode() {
        return this.getUnderlyingNode().hashCode();
    }

    public abstract void resolveReferences();

    protected abstract void setGedcomXProperties(final Object gedcomXObject);

    protected abstract void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException;

    private void setNodeType(final String nodeType) {
        this.setProperty(GenericProperties.NODE_TYPE, nodeType);
    }

    protected void setProperty(final NodeProperties property, final Object value) {
        final Transaction t = NodeWrapper.dao.beginTransaction();
        try {
            Object supportedValue = value;
            if (supportedValue != null) {
                if (value instanceof ResourceReference) {
                    supportedValue = ((ResourceReference) value).getResource()
                            .toURI().toString();
                } else if (value instanceof URI) {
                    supportedValue = ((URI) value).toURI().toString();
                } else if (value instanceof List) {
                    final Object[] array = new Object[((List<?>) value).size()];
                    for (int i = 0; i < ((List<?>) value).size(); i++) {
                        array[i] = ((List<?>) value).get(i);
                    }
                    supportedValue = array;
                }
            }
            final IndexNames indexName = property.getIndexName();
            NodeWrapper.dao.setNodeProperty(this.getUnderlyingNode(), property
                    .name(), supportedValue, property.isIndexed(), property
                    .isUnique(), indexName != null ? indexName.name() : null);
            NodeWrapper.dao.commitTransaction(t);
        } finally {
            NodeWrapper.dao.endTransaction(t);
        }
    }

    protected abstract void setRequiredProperties(final Object... properties)
            throws MissingFieldException;

    protected void setURIListProperties(final NodeProperties property,
            final List<ResourceReference> resourceList) {
        final String[] values = new String[resourceList.size()];
        int i = 0;
        for (final ResourceReference resource : resourceList) {
            values[i++] = resource.getResource().toString();
        }
        NodeWrapper.dao.setNodeProperty(this.getUnderlyingNode(),
                property.name(), values, false, false, null);
    }

    @Override
    public String toString() {
        return this.getUnderlyingNode().toString();
    }

    protected abstract void validateUnderlyingNode()
            throws MissingFieldException;
}
