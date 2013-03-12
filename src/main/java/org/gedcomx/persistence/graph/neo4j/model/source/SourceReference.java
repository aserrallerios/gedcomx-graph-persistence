package org.gedcomx.persistence.graph.neo4j.model.source;

import java.util.Date;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.model.common.Attribution;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class SourceReference extends GENgraphNode {

	public SourceReference(final org.gedcomx.source.SourceReference gedcomXSourceReference) throws MissingFieldException {
		super(NodeTypes.SOURCE_REFERENCE, gedcomXSourceReference);
	}

	public SourceReference(final Node node) throws MissingFieldException {
		super(NodeTypes.SOURCE_REFERENCE, node);
	}

	public SourceReference(final URI description) throws MissingFieldException {
		super(NodeTypes.SOURCE_REFERENCE, new Object[] { description });
	}

	@Override
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;
		if (gedcomXSourceReference.getDescriptionRef() == null) {
			throw new MissingRequiredRelationshipException(SourceReference.class, RelTypes.DESCRIPTION);
		}
	}

	public SourceDescription getDescription() {
		return this.getNodeByRelationship(SourceDescription.class, RelTypes.DESCRIPTION);
	}

	@Override
	protected void resolveReferences() {
		// TODO:
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(RelTypes.ATTRIBUTION, attribution);
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, RelTypes.ATTRIBUTION);
	}

	public void setDescription(final URI description) {
		this.description = description;
		this.createRelationship(RelTypes.DESCRIPTION, description);
		// TODO
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		return;
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = (org.gedcomx.source.SourceReference) gedcomXObject;

		this.setAttribution(new Attribution(gedcomXSourceReference.getAttribution()));
		this.setDescription(gedcomXSourceReference.getDescriptionRef());
		// TODO
	}

	@Override
	protected void deleteAllReferences() {

	}

	@Override
	protected org.gedcomx.source.SourceReference getGedcomX() {
		final org.gedcomx.source.SourceReference gedcomXSourceReference = new org.gedcomx.source.SourceReference();

		gedcomXSourceReference.setAttribution(this.getAttribution().getGedcomX());
		gedcomXSourceReference.setDescriptionRef(this.getDescription());

		return gedcomXSourceReference;
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setDescription((URI) properties[0]);
	}

	@Override
	protected void validateUnderlyingNode() throws WrongNodeType {
		if ((this.getDescription() == null) || !this.getDescription().getId().equals(this.get){
			throw new WrongNodeType();
		}
	}
}
