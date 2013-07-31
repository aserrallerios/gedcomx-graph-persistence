package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exceptions.UninitializedNode;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;

public abstract class NodeWrapper {

	@Inject
	protected static NodeWrapperOperations nodeWrapperOperations;
	private Node underlyingNode;

	protected NodeWrapper(final Node underlyingNode) {
		this.underlyingNode = underlyingNode;
		NodeWrapper.nodeWrapperOperations.initialize(this);
	}

	protected NodeWrapper(final Object... properties) {
		NodeWrapper.nodeWrapperOperations.initialize(this, properties);
	}

	protected NodeWrapper(final Object gedcomXObject) {
		NodeWrapper.nodeWrapperOperations.initialize(this, gedcomXObject);
	}

	public void delete() {
		NodeWrapper.nodeWrapperOperations.delete(this);
	}

	protected abstract void deleteAllReferences();

	@Override
	public boolean equals(final Object object) {
		return this.getUnderlyingNode().equals(object);
	}

	public abstract <T extends Object> T getGedcomX();

	ResourceReference getResourceReference() {
		return null;
	}

	Node getUnderlyingNode() {
		if (this.underlyingNode == null) {
			throw new UninitializedNode();
		}
		return this.underlyingNode;
	}

	URI getURI() {
		return null;
	}

	@Override
	public int hashCode() {
		return this.getUnderlyingNode().hashCode();
	}

	public abstract void resolveReferences();

	protected abstract void setGedcomXProperties(final Object gedcomXObject);

	protected abstract void setGedcomXRelations(final Object gedcomXObject);

	protected abstract void setRequiredProperties(final Object... properties);

	void setUnderlyingNode(final Node node) {
		this.underlyingNode = node;
	}

	@Override
	public String toString() {
		return this.getUnderlyingNode().toString();
	}

	abstract void validateUnderlyingNode();
}
