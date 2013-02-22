package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredRelationshipException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphTopLevelNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.common.Identifier;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class Relationship extends ConclusionSubnode implements GENgraphTopLevelNode {

	private final List<Identifier> identifiers = new LinkedList<>();
	private final List<Fact> facts = new LinkedList<>();

	private Person person1;
	private URI person1URI;
	private Person person2;
	private URI person2URI;

	protected Relationship(final GENgraph graph, final org.gedcomx.conclusion.Relationship gedcomXRelationship)
			throws MissingFieldException {
		super(graph, NodeTypes.RELATIONSHIP, gedcomXRelationship);
	}

	public void addFact(final Fact fact) {
		this.facts.add(fact);
		this.createRelationship(RelTypes.HAS_FACT, fact);
	}

	public void addIdentifier(final Identifier identifier) {
		this.identifiers.add(identifier);
		this.createRelationship(RelTypes.HAS_IDENTIFIER, identifier);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

		if (gedcomXRelationship.getPerson1() == null) {
			throw new MissingRequiredRelationshipException(Relationship.class, gedcomXRelationship.getId(), RelTypes.PERSON1);
		}
		if (gedcomXRelationship.getPerson2() == null) {
			throw new MissingRequiredRelationshipException(Relationship.class, gedcomXRelationship.getId(), RelTypes.PERSON2);
		}
	}

	public List<Fact> getFacts() {
		return this.facts;
	}

	public List<Identifier> getIdentifiers() {
		return this.identifiers;
	}

	public Person getPerson1() {
		return this.person1;
	}

	public Person getPerson2() {
		return this.person2;
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	@Override
	protected void resolveReferences() {
		if ((this.person1 == null) && (this.person1URI != null)) {
			final Conclusion conclusion = this.getGraph().getConclusion(this.person1URI);
			if (conclusion != null) {
				this.setPerson1((Person) conclusion.getSubnode());
			}
		}
		if ((this.person2 == null) && (this.person2URI != null)) {
			final Conclusion conclusion = this.getGraph().getConclusion(this.person2URI);
			if (conclusion != null) {
				this.setPerson2((Person) conclusion.getSubnode());
			}
		}
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

		this.setType(gedcomXRelationship.getType());
	}

	public void setPerson1(final Person person1) {
		this.person1 = person1;
		this.createRelationship(RelTypes.PERSON1, person1);
	}

	public void setPerson2(final Person person2) {
		this.person2 = person2;
		this.createRelationship(RelTypes.PERSON2, person2);
	}

	@Override
	protected void setRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Relationship gedcomXRelationship = (org.gedcomx.conclusion.Relationship) gedcomXObject;

		for (final org.gedcomx.conclusion.Fact fact : gedcomXRelationship.getFacts()) {
			this.addFact((Fact) new Conclusion(this.getGraph(), fact).getSubnode());
		}
		// for (final org.gedcomx.conclusion.Identifier identifier :
		// gedcomXRelationship.getIdentifiers()) {
		// this.addIdentifier(new Identifier(graph, identifier));
		// }
		this.person1URI = gedcomXRelationship.getPerson1().getResource();
		final Conclusion conclusion1 = this.getGraph().getConclusion(this.person1URI);
		if (conclusion1 != null) {
			this.setPerson1((Person) conclusion1.getSubnode());
		} else {
			this.addNodeToResolveReferences();
		}
		this.person2URI = gedcomXRelationship.getPerson2().getResource();
		final Conclusion conclusion2 = this.getGraph().getConclusion(this.person2URI);
		if (conclusion2 != null) {
			this.setPerson2((Person) conclusion2.getSubnode());
		} else {
			this.addNodeToResolveReferences();
		}
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

}
