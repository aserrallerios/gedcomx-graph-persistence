package org.gedcomx.persistence.graph.neo4j.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.annotations.interceptors.Transactional;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
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

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class NodeWrapperOperations {

	@Transactional
	static <T extends NodeWrapper> void deleteReferencedNodes(final List<T> type) {
		for (final T wrapper : type) {
			wrapper.delete();
		}
	}

	static NodeTypes getAnnotatedLabel(final NodeWrapper self) {
		return self.getClass().getAnnotation(NodeType.class).value();
	}

	static String getAnnotatedNodeType(final NodeWrapper self) {
		return self.getClass().getAnnotation(NodeType.class).value().name();
	}

	static <T> List<T> getGedcomXList(final Class<T> type,
			final List<? extends NodeWrapper> nodes) {
		final List<T> list = new ArrayList<>();
		for (final NodeWrapper a : nodes) {
			list.add(type.cast(a.getGedcomX()));
		}
		return list;
	}

	static private NodeTypes[] getOwnAndParentalLabels(final NodeWrapper self) {
		final NodeWrapper parentNode = self.getParentNode();
		if (parentNode == null) {
			return new NodeTypes[] { getAnnotatedLabel(self) };
		} else {
			return Lists.asList(getAnnotatedLabel(self),
					getOwnAndParentalLabels(parentNode)).toArray(
					new NodeTypes[0]);
		}
	}

	@Inject
	@EmbededDB
	private GENgraphDAO dao;

	@Inject
	private NodeWrapperFactory nodeWrapperFactory;

	@Transactional
	void addRelationship(final NodeWrapper self,
			final RelationshipTypes relType, final NodeWrapper node) {
		if (relType.isOrdered()) {
			final int index = this.getMaxRelationshipIndex(self, relType);
			final Map<String, Integer> properties = new HashMap<>();
			properties.put(RelationshipProperties.INDEX.name(),
					Integer.valueOf(index + 1));
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode(), properties);
		} else {
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode());
		}
	}

	@Transactional
	void createReferenceRelationship(final NodeWrapper self,
			final RelationshipTypes relType, final NodeProperties property) {
		final Object val = this.getProperty(self, property);

		if (val instanceof String) {
			final URI uri = new URI((String) val);

			if (uri != null) {
				this.dao.hasSingleRelationship(self.getUnderlyingNode(),
						relType, Direction.OUTGOING);

				final NodeWrapper wrapper = this
						.findNodeByReference(new ResourceReference(uri));
				if (wrapper != null) {
					this.createReferenceRelationship(self, relType, wrapper);
					this.dao.removeNodeProperty(self.getUnderlyingNode(),
							property.toString());
				}
			}
		} else if (val instanceof List) {
			for (final Object aux : (List<?>) val) {
				final URI uri = new URI((String) aux);

				if (uri != null && !uri.toURI().isAbsolute()) {
					this.dao.hasSingleRelationship(self.getUnderlyingNode(),
							relType, Direction.OUTGOING);

					final NodeWrapper wrapper = this
							.findNodeByReference(new ResourceReference(uri));
					if (wrapper != null) {
						this.addRelationship(self, relType, wrapper);
						this.dao.removeNodeProperty(self.getUnderlyingNode(),
								property.toString());
					}
				}
			}
		}
	}

	@Transactional
	void createReferenceRelationship(final NodeWrapper self,
			final RelationshipTypes relType, final NodeWrapper node) {
		final boolean rel = this.dao.hasSingleRelationship(
				self.getUnderlyingNode(), relType, Direction.OUTGOING);

		if (!rel) {
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode());
		} else {
			this.dao.delete(this.dao.getSingleRelationship(
					self.getUnderlyingNode(), relType, Direction.OUTGOING));
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode());
		}
	}

	@Transactional
	void createRelationship(final NodeWrapper self,
			final RelationshipTypes relType, final NodeWrapper node) {
		final boolean rel = this.dao.hasSingleRelationship(
				self.getUnderlyingNode(), relType, Direction.OUTGOING);

		if (!rel) {
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode());
		} else {
			final NodeWrapper wrapper = this.getNodeByRelationship(self,
					node.getClass(), relType, Direction.OUTGOING);
			wrapper.delete();
			this.dao.createRelationship(self.getUnderlyingNode(), relType,
					node.getUnderlyingNode());
		}
	}

	@Transactional
	void delete(final NodeWrapper self) {
		self.deleteAllReferences();
		this.deleteIncomingRelationships(self);
		this.deleteSelf(self);
	}

	void deleteIncomingRelationships(final NodeWrapper self) {
		final Iterable<Relationship> rels = this.dao.getRelationships(
				self.getUnderlyingNode(), Direction.INCOMING);

		for (final Relationship rel : rels) {
			this.dao.delete(rel);
		}
	}

	void deleteReference(final NodeWrapper self, final RelationshipTypes rel) {
		this.dao.delete(this.dao.getSingleRelationship(
				self.getUnderlyingNode(), rel, Direction.OUTGOING));
	}

	@Transactional
	void deleteReferences(final NodeWrapper self, final RelationshipTypes rel) {
		for (final Relationship relationship : this.dao.getRelationships(
				self.getUnderlyingNode(), rel, Direction.OUTGOING)) {
			this.dao.delete(relationship);
		}
	}

	void deleteSelf(final NodeWrapper self) {
		this.dao.delete(self.getUnderlyingNode());
	}

	private NodeWrapper findNodeByReference(final ResourceReference reference) {
		final URI uri = reference.getResource();

		NodeWrapper wrapper = null;
		if (!uri.toURI().isAbsolute()) {
			Node node = this.dao.getSingleNodeFromIndex(
					IndexNames.IDS.toString(), GenericProperties.ID.toString(),
					uri.toURI().getFragment());
			if (node == null) {
				node = this.dao.getSingleNodeFromIndex(
						IndexNames.IDS.toString(),
						GenericProperties.ID.toString(), uri.toURI().getPath());
			}
			if (node != null) {
				final String type = (String) this.dao.getNodeProperty(node,
						GenericProperties.NODE_TYPE.toString());
				wrapper = this.nodeWrapperFactory.wrapNode(
						NodeTypes.valueOf(type), node);
			}
		}
		return wrapper;
	}

	private int getMaxRelationshipIndex(final NodeWrapper self,
			final RelationshipTypes relType) {
		final Iterable<Relationship> rels = this.dao.getRelationships(
				self.getUnderlyingNode(), relType, Direction.OUTGOING);

		int max = 0;
		for (final Relationship rel : rels) {
			final Integer value = (Integer) this.dao.getRelationshipProperty(
					rel, RelationshipProperties.INDEX.name());
			max = value > max ? value : max;
		}
		// TODO: traversal
		return max;
	}

	<T extends NodeWrapper> T getNodeByRelationship(final NodeWrapper self,
			final Class<T> type, final RelationshipTypes relation) {
		return this.getNodeByRelationship(self, type, relation,
				Direction.OUTGOING);
	}

	<T extends NodeWrapper> T getNodeByRelationship(final NodeWrapper self,
			final Class<T> type, final RelationshipTypes relation,
			final Direction dir) {
		final Node node = this.dao.getSingleNodeByRelationship(
				self.getUnderlyingNode(), relation, dir);
		if (node != null) {
			return this.nodeWrapperFactory.wrapNode(type, node);
		}
		return null;
	}

	private NodeWrapper getNodeByRelationship(final NodeWrapper self,
			final RelationshipTypes relation, final Direction dir) {
		final Node node = this.dao.getSingleNodeByRelationship(
				self.getUnderlyingNode(), relation, dir);
		final String nodeType = (String) this.dao.getNodeProperty(node,
				GenericProperties.NODE_TYPE.name());

		return this.nodeWrapperFactory.wrapNode(NodeTypes.valueOf(nodeType),
				node);
	}

	<T extends NodeWrapper> List<T> getNodesByRelationship(
			final NodeWrapper self, final Class<T> type,
			final RelationshipTypes relation) {
		return this.getNodesByRelationship(self, type, relation,
				Direction.OUTGOING);
	}

	private <T extends NodeWrapper> List<T> getNodesByRelationship(
			final NodeWrapper self, final Class<T> type,
			final RelationshipTypes relation, final Direction dir) {
		final Iterable<Node> nodes = this.dao.getNodesByRelationship(
				self.getUnderlyingNode(), relation, dir,
				dir == Direction.OUTGOING && relation.isOrdered(),
				RelationshipProperties.INDEX.name());

		final List<T> wrappers = new ArrayList<>();
		for (final Node node : nodes) {
			wrappers.add(this.nodeWrapperFactory.wrapNode(type, node));
		}
		return wrappers;
	}

	String getNodeType(final NodeWrapper self) {
		return (String) this.dao.getNodeProperty(self.getUnderlyingNode(),
				GenericProperties.NODE_TYPE.name());
	}

	NodeWrapper getParentNode(final NodeWrapper self,
			final RelationshipTypes relation) {
		return this.getNodeByRelationship(self, relation, Direction.INCOMING);
	}

	Object getProperty(final NodeWrapper self, final NodeProperties property) {
		return this.dao.getNodeProperty(self.getUnderlyingNode(),
				property.name());
	}

	List<ResourceReference> getURIListProperties(final NodeWrapper self,
			final NodeProperties property) {
		final String[] values = (String[]) this.dao.getNodeProperty(
				self.getUnderlyingNode(), property.name());
		final List<ResourceReference> resourceList = new ArrayList<>();
		for (final String uri : values) {
			resourceList.add(new ResourceReference(new URI(uri)));
		}
		return resourceList;
	}

	@Transactional
	void initialize(final NodeWrapper self) {
		if (!this.getNodeType(self).equals(getAnnotatedNodeType(self))) {
			throw new UnknownNodeType();
		}
		self.resolveReferences();
		self.validateUnderlyingNode();
	}

	@Transactional
	void initialize(final NodeWrapper self, final Object... properties) {
		self.setUnderlyingNode(this.dao
				.createNode(getOwnAndParentalLabels(self)));
		this.setNodeType(self, getAnnotatedNodeType(self));
		if (properties != null && properties.length > 0) {
			self.setRequiredProperties(properties);
		}
		self.resolveReferences();
		self.validateUnderlyingNode();
	}

	@Transactional
	void initialize(final NodeWrapper self, final Object gedcomXObject) {
		self.setUnderlyingNode(this.dao
				.createNode(getOwnAndParentalLabels(self)));
		this.setNodeType(self, getAnnotatedNodeType(self));
		self.setGedcomXProperties(gedcomXObject);
		self.setGedcomXRelations(gedcomXObject);
		self.resolveReferences();
		self.validateUnderlyingNode();
	}

	void setNodeType(final NodeWrapper self, final String nodeType) {
		this.setProperty(self, GenericProperties.NODE_TYPE, nodeType);
	}

	@Transactional
	void setProperty(final NodeWrapper self, final NodeProperties property,
			final Object value) {
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
		final IndexNames indexNames = property.getIndexName();
		this.dao.setNodeProperty(self.getUnderlyingNode(), property.name(),
				Optional.<Object> fromNullable(supportedValue), property
						.isIndexed(), property.isUnique(),
				indexNames != null ? indexNames.name() : null);
	}

	@Transactional
	void setURIListProperties(final NodeWrapper self,
			final NodeProperties property,
			final List<ResourceReference> resourceList) {
		final String[] values = new String[resourceList.size()];
		int i = 0;
		for (final ResourceReference resource : resourceList) {
			values[i++] = resource.getResource().toString();
		}
		this.dao.setNodeProperty(self.getUnderlyingNode(), property.name(),
				Optional.<Object> fromNullable(values), false, false, null);
	}
}
