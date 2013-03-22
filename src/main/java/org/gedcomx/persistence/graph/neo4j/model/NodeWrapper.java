package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAOUtil;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UninitializedNode;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.reflections.Reflections;

public abstract class NodeWrapper {

	public enum GenericProperties implements NodeProperties {
		ID(true, IndexNames.IDS), ABOUT, NODE_TYPE(true, IndexNames.NODE_TYPES), TYPE(true, IndexNames.TYPES), VALUE, LANG, MODIFIED, CHANGE_MESSAGE, SUBJECT, TEXT, CONTRIBUTOR_REFERENCE;

		private final boolean indexed;
		private final IndexNames indexName;

		private GenericProperties() {
			this.indexed = false;
			this.indexName = null;
		}

		private GenericProperties(final boolean indexed, final IndexNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}
	}

	public enum IndexNames {
		NODE_TYPES, IDS, TYPES, OTHER
	}

	public interface NodeProperties {

		public IndexNames getIndexName();

		public boolean isIndexed();

		public String name();

	}

	public enum RelationshipProperties {
		INDEX
	}

	public enum RelTypes implements RelationshipType {

		HAS_CONCLUSION, HAS_AGENT, HAS_SOURCE_DESCRIPTION, IS_A, HAS_ADDRESS(true), HAS_ACCOUNT(true), HAS_IDENTIFIER(true), HAS_NAME, HAS_NOTE, HAS_SOURCE_REFERENCE, PLACE, PLACE_DESCRIPTION, HAS_ROLE(
				true), HAS_NAME_FORM(true), HAS_NAME_PART, PERSON, GENDER, HAS_FACT, PERSON2, PERSON1, DESCRIPTION, HAS_CITATION, HAS_SOURCE, HAS_TITLE, COMPONENT_OF, MEDIATOR, HAS_CITATION_FIELD, CONTRIBUTOR, ATTRIBUTION;

		private boolean ordered = false;

		private RelTypes() {
		}

		private RelTypes(final boolean ordered) {
			this.ordered = ordered;
		}

		public boolean isOrdered() {
			return this.ordered;
		}

	}

	private static Map<String, Class<? extends NodeWrapper>> nodesByType = new HashMap<>();

	private final Node underlyingNode;

	static {
		final Reflections reflections = new Reflections("org.gedcomx.persistence.graph.neo4j.model");

		for (final Class<? extends NodeWrapper> subclass : reflections.getSubTypesOf(NodeWrapper.class)) {
			final NodeType nodeType = subclass.getAnnotation(NodeType.class);
			if (nodeType != null) {
				NodeWrapper.nodesByType.put(nodeType.value(), subclass);
			}
		}
	}

	protected NodeWrapper(final Node underlyingNode) throws MissingFieldException, UnknownNodeType {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = underlyingNode;
			if (this.getNodeType() != this.getClass().getAnnotation(NodeType.class).value()) {
				throw new UnknownNodeType();
			}
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected NodeWrapper(final Object... properties) throws MissingFieldException {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = GENgraphDAOUtil.createNode();
			this.setNodeType(this.getClass().getAnnotation(NodeType.class).value());
			if ((properties != null) && (properties.length > 0)) {
				this.setRequiredProperties(properties);
			}
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected NodeWrapper(final Object gedcomXObject) throws MissingFieldException {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.underlyingNode = GENgraphDAOUtil.createNode();
			this.setNodeType(this.getClass().getAnnotation(NodeType.class).value());
			this.setGedcomXProperties(gedcomXObject);
			this.setGedcomXRelations(gedcomXObject);
			this.validateUnderlyingNode();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected void addRelationship(final RelTypes relType, final NodeWrapper node) {
		if (relType.isOrdered()) {
			final Transaction t = GENgraphDAOUtil.beginTransaction();
			try {
				final int index = this.getMaxRelationshipIndex(relType);
				final Map<RelationshipProperties, Integer> properties = new HashMap<>();
				properties.put(RelationshipProperties.INDEX, Integer.valueOf(index + 1));
				GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.getUnderlyingNode());
				GENgraphDAOUtil.commitTransaction(t);
			} finally {
				GENgraphDAOUtil.endTransaction(t);
			}
		} else {
			GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.getUnderlyingNode());
		}
	}

	private <T extends NodeWrapper> T createNode(final Class<T> type, final Node node) {
		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getConstructor(GENgraphDAO.class, Node.class);
			wrapper = constructor.newInstance(node);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wrapper;
	}

	protected void createReferenceRelationship(final RelTypes relType, final NodeProperties property) {
		final Object val = this.getProperty(property);

		if (val instanceof String) {
			final URI uri = new URI((String) val);

			if ((uri != null)) {
				GENgraphDAOUtil.hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

				final NodeWrapper wrapper = this.findNodeByReference(new ResourceReference(uri));
				if (wrapper != null) {
					final Transaction t = GENgraphDAOUtil.beginTransaction();
					try {
						this.createReferenceRelationship(relType, wrapper);
						GENgraphDAOUtil.removeNodeProperty(this.getUnderlyingNode(), property.toString());
						GENgraphDAOUtil.commitTransaction(t);
					} finally {
						GENgraphDAOUtil.endTransaction(t);
					}
				}
			}
		} else if (val instanceof List) {
			for (final Object aux : (List<?>) val) {
				final URI uri = new URI((String) aux);

				if ((uri != null) && !uri.toURI().isAbsolute()) {
					GENgraphDAOUtil.hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

					final NodeWrapper wrapper = this.findNodeByReference(new ResourceReference(uri));
					if (wrapper != null) {
						final Transaction t = GENgraphDAOUtil.beginTransaction();
						try {
							this.addRelationship(relType, wrapper);
							GENgraphDAOUtil.removeNodeProperty(this.getUnderlyingNode(), property.toString());
							GENgraphDAOUtil.commitTransaction(t);
						} finally {
							GENgraphDAOUtil.endTransaction(t);
						}
					}
				}
			}
		}
	}

	protected void createReferenceRelationship(final RelTypes relType, final NodeWrapper node) {
		final boolean rel = GENgraphDAOUtil.hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			if (!rel) {
				GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
			} else {
				GENgraphDAOUtil.delete(GENgraphDAOUtil.getSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING));
				GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
			}
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected void createRelationship(final RelTypes relType, final NodeWrapper node) {
		final boolean rel = GENgraphDAOUtil.hasSingleRelationship(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		if (!rel) {
			GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
		} else {
			final Transaction t = GENgraphDAOUtil.beginTransaction();
			try {
				final NodeWrapper wrapper = this.getNodeByRelationship(node.getClass(), relType, Direction.OUTGOING);
				wrapper.delete();
				GENgraphDAOUtil.createRelationship(this.getUnderlyingNode(), relType, node.underlyingNode);
				GENgraphDAOUtil.commitTransaction(t);
			} finally {
				GENgraphDAOUtil.endTransaction(t);
			}
		}
	}

	public void delete() {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			this.deleteAllReferences();
			this.deleteIncomingRelationships();
			this.deleteSelf();
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}

	}

	protected abstract void deleteAllReferences();

	private void deleteIncomingRelationships() {
		final Iterable<Relationship> rels = GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), Direction.INCOMING);

		for (final Relationship rel : rels) {
			GENgraphDAOUtil.delete(rel);
		}
	}

	protected void deleteReference(final RelTypes rel) {
		GENgraphDAOUtil.delete(GENgraphDAOUtil.getSingleRelationship(this.getUnderlyingNode(), rel, Direction.OUTGOING));
	}

	protected <T extends NodeWrapper> void deleteReferencedNode(final T wrapper) {
		if (wrapper != null) {
			wrapper.delete();
		}
	}

	protected <T extends NodeWrapper> void deleteReferencedNodes(final List<T> type) {
		for (final T wrapper : type) {
			wrapper.delete();
		}
	}

	protected void deleteReferences(final RelTypes rel) {
		for (final Relationship relationship : GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), rel, Direction.OUTGOING)) {
			GENgraphDAOUtil.delete(relationship);
		}
	}

	private void deleteSelf() {
		GENgraphDAOUtil.delete(this.getUnderlyingNode());
	}

	@Override
	public boolean equals(final Object object) {
		return this.getUnderlyingNode().equals(object);
	}

	private NodeWrapper findNodeByReference(final ResourceReference reference) {
		final URI uri = reference.getResource();

		NodeWrapper wrapper = null;
		if (!uri.toURI().isAbsolute()) {
			Node node = GENgraphDAOUtil.getNodeFromIndex(IndexNames.IDS.toString(), GenericProperties.ID.toString(), uri.toURI()
					.getFragment());
			if (node == null) {
				node = GENgraphDAOUtil.getNodeFromIndex(IndexNames.IDS.toString(), GenericProperties.ID.toString(), uri.toURI().getPath());
			}
			if (node != null) {
				final String type = (String) GENgraphDAOUtil.getNodeProperty(node, GenericProperties.NODE_TYPE.toString());
				try {
					wrapper = NodeWrapper.nodesByType.get(type).getConstructor(Node.class).newInstance(node);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return wrapper;
	}

	protected String getAnnotatedNodeType() {
		return this.getClass().getAnnotation(NodeType.class).value();
	}

	protected abstract <T extends Object> T getGedcomX();

	protected <T> List<T> getGedcomXList(final Class<T> type, final List<? extends NodeWrapper> nodes) {
		final List<T> list = new ArrayList<>();
		for (final NodeWrapper a : nodes) {
			list.add(type.cast(a.getGedcomX()));
		}
		return list;
	}

	private int getMaxRelationshipIndex(final RelTypes relType) {
		final Iterable<Relationship> rels = GENgraphDAOUtil.getRelationships(this.getUnderlyingNode(), relType, Direction.OUTGOING);

		int max = 0;
		for (final Relationship rel : rels) {
			final Integer value = (Integer) GENgraphDAOUtil.getRelationshipProperty(rel, RelationshipProperties.INDEX.name());
			max = value > max ? value : max;
		}
		// TODO: traversal
		return max;
	}

	protected <T extends NodeWrapper> T getNodeByRelationship(final Class<T> type, final RelTypes relation) {
		return this.getNodeByRelationship(type, relation, Direction.OUTGOING);
	}

	protected <T extends NodeWrapper> T getNodeByRelationship(final Class<T> type, final RelTypes relation, final Direction dir) {
		final Node node = GENgraphDAOUtil.getSingleNodeByRelationship(this.getUnderlyingNode(), relation, dir);
		if (node != null) {
			return this.createNode(type, node);
		}
		return null;
	}

	private NodeWrapper getNodeByRelationship(final RelTypes relation, final Direction dir) {
		final Node node = GENgraphDAOUtil.getSingleNodeByRelationship(this.getUnderlyingNode(), relation, dir);
		final String nodeType = (String) GENgraphDAOUtil.getNodeProperty(node, GenericProperties.NODE_TYPE.name());

		final Class<? extends NodeWrapper> type = NodeWrapper.nodesByType.get(nodeType);
		Constructor<? extends NodeWrapper> cons;
		NodeWrapper wrapper;
		try {
			cons = type.getConstructor(Node.class);
			wrapper = cons.newInstance(node);
		} catch (final Exception e) {
			throw new UnknownNodeType();
		}
		return wrapper;
	}

	protected <T extends NodeWrapper> List<T> getNodesByRelationship(final Class<T> type, final RelTypes relation) {
		return this.getNodesByRelationship(type, relation, Direction.OUTGOING);
	}

	private <T extends NodeWrapper> List<T> getNodesByRelationship(final Class<T> type, final RelTypes relation, final Direction dir) {
		final Iterable<Node> nodes = GENgraphDAOUtil.getNodesByRelationship(this.getUnderlyingNode(), relation, dir,
				(dir == Direction.OUTGOING) && relation.isOrdered(), RelationshipProperties.INDEX.name());

		final List<T> wrappers = new ArrayList<>();
		for (final Node node : nodes) {
			wrappers.add(this.createNode(type, node));
		}
		return wrappers;
	}

	public String getNodeType() {
		return (String) GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), GenericProperties.NODE_TYPE.name());
	}

	protected NodeWrapper getParentNode(final RelTypes relation) {
		return this.getNodeByRelationship(relation, Direction.INCOMING);
	}

	protected Object getProperty(final NodeProperties property) {
		return GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), property.name());
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

	protected List<ResourceReference> getURIListProperties(final NodeProperties property) {
		final String[] values = (String[]) GENgraphDAOUtil.getNodeProperty(this.getUnderlyingNode(), property.name());
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

	protected abstract void resolveReferences();

	protected abstract void setGedcomXProperties(final Object gedcomXObject);

	protected abstract void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException;

	private void setNodeType(final String nodeType) {
		this.setProperty(GenericProperties.NODE_TYPE, nodeType);
	}

	protected void setProperty(final NodeProperties property, final Object value) {
		final Transaction t = GENgraphDAOUtil.beginTransaction();
		try {
			if (value == null) {
				GENgraphDAOUtil.removeNodeProperty(this.getUnderlyingNode(), property.name());
				if (property.isIndexed()) {
					GENgraphDAOUtil.removeNodeFromIndex(property.getIndexName().name(), this.getUnderlyingNode(), property.name());
				}
			} else {
				Object supportedValue = value;
				if (value instanceof ResourceReference) {
					supportedValue = ((ResourceReference) value).getResource().toURI().toString();
				} else if (value instanceof URI) {
					supportedValue = ((URI) value).toURI().toString();
				} else if (value instanceof List) {
					final Object[] array = new Object[((List<?>) value).size()];
					for (int i = 0; i < ((List<?>) value).size(); i++) {
						array[i] = ((List<?>) value).get(i);
					}
					supportedValue = array;
				}

				GENgraphDAOUtil.setNodeProperty(this.getUnderlyingNode(), property.name(), supportedValue);
				if (property.isIndexed()) {
					GENgraphDAOUtil.removeNodeFromIndex(property.getIndexName().name(), this.getUnderlyingNode(), property.name());
					GENgraphDAOUtil.setNodeToIndex(property.getIndexName().name(), this.getUnderlyingNode(), property.name(),
							supportedValue);
				}
			}
			GENgraphDAOUtil.commitTransaction(t);
		} finally {
			GENgraphDAOUtil.endTransaction(t);
		}
	}

	protected abstract void setRequiredProperties(final Object... properties) throws MissingFieldException;

	protected void setURIListProperties(final NodeProperties property, final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		GENgraphDAOUtil.setNodeProperty(this.getUnderlyingNode(), property.name(), values);
	}

	@Override
	public String toString() {
		return this.getUnderlyingNode().toString();
	}

	protected abstract void validateUnderlyingNode() throws MissingFieldException;
}
