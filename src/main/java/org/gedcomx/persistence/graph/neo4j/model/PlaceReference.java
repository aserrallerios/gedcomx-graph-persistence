package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.neo4j.graphdb.Node;

@NodeType("PLACE_REFERENCE")
public class PlaceReference extends NodeWrapper {

	public PlaceReference() throws MissingFieldException, WrongNodeType {
		super();
	}

	protected PlaceReference(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public PlaceReference(final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference) throws MissingFieldException {
		super(gedcomXPlaceReference);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReference(GENgraphRelTypes.PLACE_DESCRIPTION);
	}

	@Override
	protected org.gedcomx.conclusion.PlaceReference getGedcomX() {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = new org.gedcomx.conclusion.PlaceReference();

		gedcomXPlaceReference.setOriginal(this.getOriginal());
		gedcomXPlaceReference.setDescriptionRef(this.getPlaceDescription());

		return gedcomXPlaceReference;
	}

	public String getOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.ORIGINAL);
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(GENgraphRelTypes.PLACE);
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
		this.setProperty(NodeProperties.Conclusion.ORIGINAL, original);
	}

	public void setPlaceDescription(final PlaceDescription placeDescription) {
		this.createRelationship(GENgraphRelTypes.PLACE_DESCRIPTION, placeDescription);
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
