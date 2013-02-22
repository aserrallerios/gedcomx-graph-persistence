package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class PlaceReference extends GENgraphNode {

	private PlaceDescription placeDescription;
	private URI placeDescriptionURI;

	protected PlaceReference(final GENgraph graph, final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference)
			throws MissingFieldException {
		super(graph, NodeTypes.PLACE_REFERENCE, gedcomXPlaceReference);
	}

	public PlaceDescription getPlaceDescription() {
		return this.placeDescription;
	}

	@Override
	protected void resolveReferences() {
		if ((this.placeDescription == null) && (this.placeDescriptionURI != null)) {
			final Conclusion conclusion = this.getGraph().getConclusion(this.placeDescriptionURI);
			if (conclusion != null) {
				this.setPlaceDescription((PlaceDescription) conclusion.getSubnode());
			}
		}
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = (org.gedcomx.conclusion.PlaceReference) gedcomXObject;
		this.setOriginal(gedcomXPlaceReference.getOriginal());
	}

	public String setOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.ORIGINAL);
	}

	public void setOriginal(final String original) {
		this.setProperty(NodeProperties.Conclusion.ORIGINAL, original);
	}

	public void setPlaceDescription(final PlaceDescription placeDescription) {
		this.placeDescription = placeDescription;
		this.createRelationship(RelTypes.PLACE_DESCRIPTION, placeDescription);
	}

	@Override
	protected void setRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceReference gedcomXPlaceReference = (org.gedcomx.conclusion.PlaceReference) gedcomXObject;

		if (gedcomXPlaceReference.getDescriptionRef() != null) {
			this.placeDescriptionURI = gedcomXPlaceReference.getDescriptionRef();
			final Conclusion conclusion = this.getGraph().getConclusion(this.placeDescriptionURI);
			if (conclusion != null) {
				this.setPlaceDescription((PlaceDescription) conclusion.getSubnode());
			} else {
				this.addNodeToResolveReferences();
			}
		}
	}

}
