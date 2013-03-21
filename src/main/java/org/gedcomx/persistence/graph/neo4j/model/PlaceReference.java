package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion.ConclusionProperties;
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
		this.deleteReference(WrapperRelTypes.PLACE_DESCRIPTION);
	}

	@Override
	protected org.gedcomx.conclusion.PlaceReference getGedcomX() {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = new org.gedcomx.conclusion.PlaceReference();

		gedcomXPlaceReference.setOriginal(this.getOriginal());
		gedcomXPlaceReference.setDescriptionRef(this.getPlaceDescription());

		return gedcomXPlaceReference;
	}

	public String getOriginal() {
		return (String) this.getProperty(ConclusionProperties.ORIGINAL);
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(WrapperRelTypes.PLACE);
	}

	public PlaceDescription getPlaceDescription() {
		// TODO
		return null;
	}

	@Override
	protected void resolveReferences() {
		// TODO
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
			// TODO
		}
	}

	public void setOriginal(final String original) {
		this.setProperty(ConclusionProperties.ORIGINAL, original);
	}

	public void setPlaceDescription(final PlaceDescription placeDescription) {
		this.createRelationship(WrapperRelTypes.PLACE_DESCRIPTION, placeDescription);
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
