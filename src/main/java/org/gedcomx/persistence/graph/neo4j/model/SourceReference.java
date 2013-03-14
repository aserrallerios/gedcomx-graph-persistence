package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

@NodeType("SOURCE_REFERENCE")
public class SourceReference extends NodeWrapper {

	public SourceReference(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public SourceReference(final org.gedcomx.source.SourceReference gedcomXSourceReference) throws MissingFieldException {
		super(gedcomXSourceReference);
	}

	public SourceReference(final URI description) throws MissingFieldException {
		super(new Object[] { description });
	}

	@Override
	protected void deleteAllReferences() {

	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, GENgraphRelTypes.ATTRIBUTION);
	}

	public SourceDescription getDescription() {
		return this.getNodeByRelationship(SourceDescription.class, GENgraphRelTypes.DESCRIPTION);
	}

	@Override
	protected org.gedcomx.source.SourceReference getGedcomX() {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = new org.gedcomx.source.SourceReference();

		gedcomXSourceReference.setAttribution(this.getAttribution().getGedcomX());
		gedcomXSourceReference.setDescription(this.getDescription());

		return gedcomXSourceReference;
	}

	public NodeWrapper getParentNode() {
		// TODO
		return this.getNodeByRelationship(NodeWrapper.class, GENgraphRelTypes.HAS_NAME, Direction.INCOMING);
	}

	@Override
	protected void resolveReferences() {
		// TODO:
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(GENgraphRelTypes.ATTRIBUTION, attribution);
	}

	public void setDescription(final URI description) {
		this.description = description;
		this.createRelationship(GENgraphRelTypes.DESCRIPTION, description);
		// TODO
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		this.setAttribution(new Attribution(gedcomXSourceReference.getAttribution()));
		this.setDescription(gedcomXSourceReference.getDescriptionRef());
		// TODO
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setDescription((URI) properties[0]);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getDescription())) {
			throw new MissingRequiredRelationshipException(SourceReference.class, GENgraphRelTypes.DESCRIPTION);
		}
	}
}
