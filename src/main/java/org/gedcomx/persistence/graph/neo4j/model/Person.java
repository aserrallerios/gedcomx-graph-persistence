package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.neo4j.graphdb.Node;

public class Person extends Conclusion {

	protected Person() throws MissingFieldException {
		super();
	}

	protected Person(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public Person(final org.gedcomx.conclusion.Person gedcomXPerson) throws MissingFieldException {
		super(gedcomXPerson);
	}

	public void addFact(final Fact fact) {
		this.addRelationship(GENgraphRelTypes.HAS_FACT, fact);
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(GENgraphRelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final Name name) {
		this.addRelationship(GENgraphRelTypes.HAS_NAME, name);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNode(this.getGender());

		this.deleteReferencedNodes(this.getNames());
		this.deleteReferencedNodes(this.getFacts());
	}

	public List<Fact> getFacts() {
		return this.getNodesByRelationship(Fact.class, GENgraphRelTypes.HAS_FACT);
	}

	@Override
	protected org.gedcomx.conclusion.Person getGedcomX() {
		final org.gedcomx.conclusion.Person gedcomXPerson = new org.gedcomx.conclusion.Person();

		this.getGedcomXConclusion(gedcomXPerson);

		gedcomXPerson.setLiving(this.getLiving());

		gedcomXPerson.setGender(this.getGender().getGedcomX());

		gedcomXPerson.setFacts(this.getGedcomXList(org.gedcomx.conclusion.Fact.class, this.getFacts()));
		gedcomXPerson.setIdentifiers(this.getGedcomXList(org.gedcomx.conclusion.Identifier.class, this.getIdentifiers()));
		gedcomXPerson.setNames(this.getGedcomXList(org.gedcomx.conclusion.Name.class, this.getNames()));

		return gedcomXPerson;
	}

	public Gender getGender() {
		return this.getNodeByRelationship(Gender.class, GENgraphRelTypes.GENDER);
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, GENgraphRelTypes.HAS_IDENTIFIER);
	}

	public Boolean getLiving() {
		return (Boolean) this.getProperty(NodeProperties.Conclusion.LIVING);
	}

	public List<Name> getNames() {
		return this.getNodesByRelationship(Name.class, GENgraphRelTypes.HAS_NAME);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

		this.setLiving(gedcomXPerson.getLiving());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

		this.setGender(new Gender(gedcomXPerson.getGender()));

		for (final org.gedcomx.conclusion.Identifier identifier : gedcomXPerson.getIdentifiers()) {
			this.addIdentifier(new Identifier(identifier));
		}
		for (final org.gedcomx.conclusion.Name name : gedcomXPerson.getNames()) {
			this.addName(new Name(name));
		}
		for (final org.gedcomx.conclusion.Fact fact : gedcomXPerson.getFacts()) {
			this.addFact(new Fact(fact));
		}
	}

	public void setGender(final Gender gender) {
		this.createRelationship(GENgraphRelTypes.GENDER, gender);
	}

	public void setLiving(final Boolean living) {
		this.setProperty(NodeProperties.Conclusion.LIVING, living);
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
