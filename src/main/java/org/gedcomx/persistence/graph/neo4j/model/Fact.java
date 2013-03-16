package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.FactType;
import org.neo4j.graphdb.Node;

@NodeType("FACT")
public class Fact extends Conclusion {

	protected Fact(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public Fact(final org.gedcomx.conclusion.Fact gedcomXFact) throws MissingFieldException {
		super(gedcomXFact);
	}

	public Fact(final URI type) throws MissingFieldException {
		super(new Object[] { type });
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNode(this.getPlaceReference());
	}

	@Override
	protected void deleteAllReferences() {
		super.deleteAllReferences();

		this.getPlaceReference().delete();
	}

	public String getDateFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_ORIGINAL);
	}

	@Override
	protected org.gedcomx.conclusion.Fact getGedcomX() {
		final org.gedcomx.conclusion.Fact gedcomXFact = new org.gedcomx.conclusion.Fact();

		this.getGedcomXConclusion(gedcomXFact);

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getDateFormal());
		date.setOriginal(this.getDateOriginal());
		gedcomXFact.setDate(date);

		gedcomXFact.setKnownType(this.getKnownType());
		gedcomXFact.setType(this.getType());
		gedcomXFact.setValue(this.getValue());

		gedcomXFact.setPlace(this.getPlaceReference().getGedcomX());

		return gedcomXFact;
	}

	public FactType getKnownType() {
		return FactType.fromQNameURI(this.getType());
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(GENgraphRelTypes.HAS_FACT);
	}

	public PlaceReference getPlaceReference() {
		return this.getNodeByRelationship(PlaceReference.class, GENgraphRelTypes.PLACE);
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setDateFormal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;
		this.setValue(gedcomXFact.getValue());
		this.setType(gedcomXFact.getType());

		if (gedcomXFact.getDate() != null) {
			this.setDateFormal(gedcomXFact.getDate().getFormal());
			this.setDateOriginal(gedcomXFact.getDate().getOriginal());
		}
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;

		if (gedcomXFact.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(gedcomXFact.getPlace()));
		}
	}

	public void setKnownType(final FactType type) {
		this.setType(type.toQNameURI());
	}

	public void setPlaceReference(final PlaceReference placeReference) {
		this.createRelationship(GENgraphRelTypes.PLACE, placeReference);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setType((URI) properties[0]);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getType())) {
			throw new MissingRequiredPropertyException(Fact.class, this.getId(), NodeProperties.Generic.TYPE);
		}
	}

}
