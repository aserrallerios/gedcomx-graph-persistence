package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.RelationshipType;
import org.neo4j.graphdb.Node;

@NodeType("RELATIONSHIP")
public class Relationship extends Conclusion {

	protected Relationship(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public Relationship(final org.gedcomx.conclusion.Relationship gedcomXRelationship) throws MissingFieldException {
		super(gedcomXRelationship);
	}

	public Relationship(final Person p1, final Person p2) throws MissingFieldException {
		super(p1, p2);
	}

	public void addFact(final Fact fact) {
		this.addRelationship(RelationshipTypes.HAS_FACT, fact);
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(RelationshipTypes.HAS_IDENTIFIER, identifier);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNodes(this.getIdentifiers());
		this.deleteReferencedNodes(this.getFacts());

		this.deleteReference(RelationshipTypes.PERSON1);
		this.deleteReference(RelationshipTypes.PERSON2);
	}

	public List<Fact> getFacts() {
		return this.getNodesByRelationship(Fact.class, RelationshipTypes.HAS_FACT);
	}

	@Override
	protected org.gedcomx.conclusion.Relationship getGedcomX() {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = new org.gedcomx.conclusion.Relationship();

		this.getGedcomXConclusion(gedcomXRelationship);

		gedcomXRelationship.setFacts(this.getGedcomXList(org.gedcomx.conclusion.Fact.class, this.getFacts()));
		gedcomXRelationship.setIdentifiers(this.getGedcomXList(org.gedcomx.conclusion.Identifier.class, this.getIdentifiers()));

		gedcomXRelationship.setKnownType(this.getKnownType());
		gedcomXRelationship.setType(this.getType());

		gedcomXRelationship.setPerson1(this.getPerson1().getResourceReference());
		gedcomXRelationship.setPerson2(this.getPerson2().getResourceReference());

		return gedcomXRelationship;
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
	}

	public RelationshipType getKnownType() {
		return RelationshipType.fromQNameURI(this.getType());
	}

	public Person getPerson1() {
		return this.getNodeByRelationship(Person.class, RelationshipTypes.PERSON1);
	}

	public Person getPerson2() {
		return this.getNodeByRelationship(Person.class, RelationshipTypes.PERSON2);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	public void resolveReferences() {
		this.createReferenceRelationship(RelationshipTypes.PERSON1, ConclusionProperties.PERSON1_REFERENCE);
		this.createReferenceRelationship(RelationshipTypes.PERSON2, ConclusionProperties.PERSON2_REFERENCE);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

		this.setType(gedcomXRelationship.getType());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

		for (final org.gedcomx.conclusion.Fact fact : gedcomXRelationship.getFacts()) {
			this.addFact(new Fact(fact));
		}
		for (final org.gedcomx.conclusion.Identifier identifier : gedcomXRelationship.getIdentifiers()) {
			this.addIdentifier(new Identifier(identifier));
		}

		this.setProperty(ConclusionProperties.PERSON1_REFERENCE, gedcomXRelationship.getPerson1());
		this.setProperty(ConclusionProperties.PERSON2_REFERENCE, gedcomXRelationship.getPerson2());
	}

	public void setKnownType(final RelationshipType type) {
		this.setType(type.toQNameURI());
	}

	public void setPerson1(final Person person1) {
		this.createReferenceRelationship(RelationshipTypes.PERSON1, person1);
	}

	public void setPerson2(final Person person2) {
		this.createReferenceRelationship(RelationshipTypes.PERSON2, person2);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setPerson1((Person) properties[0]);
		this.setPerson2((Person) properties[1]);
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getPerson1())) {
			throw new MissingRequiredRelationshipException(this.getAnnotatedNodeType(), this.getId(), RelationshipTypes.PERSON1.toString());
		}
		if (ValidationTools.nullOrEmpty(this.getPerson1())) {
			throw new MissingRequiredRelationshipException(this.getAnnotatedNodeType(), this.getId(), RelationshipTypes.PERSON2.toString());
		}
	}

}
