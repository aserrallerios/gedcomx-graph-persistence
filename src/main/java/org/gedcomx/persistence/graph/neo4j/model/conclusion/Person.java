package org.gedcomx.persistence.graph.neo4j.model.conclusion;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphTopLevelNode;
import org.gedcomx.persistence.graph.neo4j.model.common.Identifier;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;

public class Person extends ConclusionSubnode implements GENgraphTopLevelNode {

	private Gender gender;
	private final List<Identifier> identifiers = new LinkedList<>();
	private final List<Fact> facts = new LinkedList<>();
	private final List<Name> names = new LinkedList<>();

	protected Person(final GENgraph graph, final org.gedcomx.conclusion.Person gedcomXPerson) throws MissingFieldException {
		super(graph, NodeTypes.PERSON, gedcomXPerson);
	}

	public void addFact(final Fact fact) {
		this.facts.add(fact);
		this.createRelationship(RelTypes.HAS_FACT, fact);
	}

	public void addIdentifier(final Identifier identifier) {
		this.identifiers.add(identifier);
		this.createRelationship(RelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final Name name) {
		this.names.add(name);
		this.createRelationship(RelTypes.HAS_NAME, name);
	}

	public List<Fact> getFacts() {
		return this.facts;
	}

	public Gender getGender() {
		return this.gender;
	}

	public List<Identifier> getIdentifiers() {
		return this.identifiers;
	}

	public Boolean getLiving() {
		return (Boolean) this.getProperty(NodeProperties.Conclusion.LIVING);
	}

	public List<Name> getNames() {
		return this.names;
	}

	public void setGender(final Gender gender) {
		this.gender = gender;
		this.createRelationship(RelTypes.GENDER, gender);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

		this.setLiving(gedcomXPerson.getLiving());
	}

	public void setLiving(final Boolean living) {
		this.setProperty(NodeProperties.Conclusion.LIVING, living);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Person gedcomXPerson = (org.gedcomx.conclusion.Person) gedcomXObject;

		this.setGender(new Gender(this.getGraph(), gedcomXPerson.getGender()));
		for (final org.gedcomx.conclusion.Identifier identifier : gedcomXPerson.getIdentifiers()) {
			this.addIdentifier(new Identifier(this.getGraph(), identifier));
		}
		for (final org.gedcomx.conclusion.Name name : gedcomXPerson.getNames()) {
			this.addName((Name) new Conclusion(this.getGraph(), name).getSubnode());
		}
		for (final org.gedcomx.conclusion.Fact fact : gedcomXPerson.getFacts()) {
			this.addFact((Fact) new Conclusion(this.getGraph(), fact).getSubnode());
		}
	}
}
