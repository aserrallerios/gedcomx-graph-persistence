package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType("PLACE_REFERENCE")
public class PlaceReference extends NodeWrapper {

	public PlaceReference() throws MissingFieldException, UnknownNodeType {
		super();
	}

	protected PlaceReference(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public PlaceReference(final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference) throws MissingFieldException {
		super(gedcomXPlaceReference);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReference(RelationshipTypes.PLACE_DESCRIPTION);
	}

	@Override
	public org.gedcomx.conclusion.PlaceReference getGedcomX() {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = new org.gedcomx.conclusion.PlaceReference();

		gedcomXPlaceReference.setOriginal(this.getOriginal());
		final PlaceDescription place = this.getPlaceDescription();
		if (place != null) {
			gedcomXPlaceReference.setDescriptionRef(place.getURI());
		}

		return gedcomXPlaceReference;
	}

	public String getOriginal() {
		return (String) this.getProperty(ConclusionProperties.ORIGINAL);
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(RelationshipTypes.PLACE);
	}

	public PlaceDescription getPlaceDescription() {
		return this.getNodeByRelationship(PlaceDescription.class, RelationshipTypes.PLACE);
	}

	@Override
	public void resolveReferences() {
		this.createReferenceRelationship(RelationshipTypes.PLACE, ConclusionProperties.PLACE_DESC_REFERENCE);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = (org.gedcomx.conclusion.PlaceReference) gedcomXObject;

		this.setOriginal(gedcomXPlaceReference.getOriginal());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = (org.gedcomx.conclusion.PlaceReference) gedcomXObject;

		if (gedcomXPlaceReference.getDescriptionRef() != null) {
			this.setProperty(ConclusionProperties.PLACE_DESC_REFERENCE, gedcomXPlaceReference.getDescriptionRef());
		}
	}

	public void setOriginal(final String original) {
		this.setProperty(ConclusionProperties.ORIGINAL, original);
	}

	public void setPlaceDescription(final PlaceDescription placeDescription) {
		this.createReferenceRelationship(RelationshipTypes.PLACE_DESCRIPTION, placeDescription);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		return;
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		return;
	}

}
