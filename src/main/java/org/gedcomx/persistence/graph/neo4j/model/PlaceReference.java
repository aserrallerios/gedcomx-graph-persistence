package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.PLACE_REFERENCE)
public class PlaceReference extends NodeWrapper {

	protected PlaceReference() {
		super();
	}

	protected PlaceReference(final Node node) {
		super(node);
	}

	protected PlaceReference(
			final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference) {
		super(gedcomXPlaceReference);
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReference(this,
				RelationshipTypes.PLACE_DESCRIPTION);
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
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.ORIGINAL);
	}

	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.PLACE);
	}

	public PlaceDescription getPlaceDescription() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				PlaceDescription.class, RelationshipTypes.PLACE);
	}

	@Override
	public void resolveReferences() {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.PLACE,
				ConclusionProperties.PLACE_DESC_REFERENCE);
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
			NodeWrapper.nodeWrapperOperations.setProperty(this,
					ConclusionProperties.PLACE_DESC_REFERENCE,
					gedcomXPlaceReference.getDescriptionRef());
		}
	}

	public void setOriginal(final String original) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.ORIGINAL, original);
	}

	public void setPlaceDescription(final PlaceDescription placeDescription) {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.PLACE_DESCRIPTION, placeDescription);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		return;
	}

	@Override
	protected void validateUnderlyingNode() {
		return;
	}

}
