package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphTopLevelNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class Fact extends ConclusionSubnode implements GENgraphTopLevelNode {

	PlaceReference placeReference;

	protected Fact(final GENgraph graph, final org.gedcomx.conclusion.Fact gedcomXFact) throws MissingFieldException {
		super(graph, NodeTypes.FACT, gedcomXFact);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;

		if (gedcomXFact.getType() == null) {
			throw new MissingRequiredPropertyException(Fact.class, NodeProperties.Generic.TYPE);
		}
	}

	public String getDateFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_ORIGINAL);
	}

	public PlaceReference getPlaceReference() {
		return this.placeReference;
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	public void setDateFormal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;
		this.setValue(gedcomXFact.getValue());
		this.setType(gedcomXFact.getType());

		if (gedcomXFact.getDate() != null) {
			this.setDateFormal(gedcomXFact.getDate().getFormal());
			this.setDateOriginal(gedcomXFact.getDate().getOriginal());
		}
	}

	public void setPlaceReference(final PlaceReference placeReference) {
		this.placeReference = placeReference;
		this.createRelationship(RelTypes.PLACE, placeReference);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;
		if (gedcomXFact.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(this.getGraph(), gedcomXFact.getPlace()));
		}
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

}
