package org.gedcomx.persistence.graph.neo4j.model;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class Name extends ConclusionSubnode {

	private final List<NameForm> nameForms = new LinkedList<>();

	protected Name(final GENgraph graph, final org.gedcomx.conclusion.Name gedcomXName) throws MissingFieldException {
		super(graph, NodeTypes.NAME, gedcomXName);
	}

	public void addNameForms(final NameForm nameForms) {
		this.nameForms.add(nameForms);
		this.createRelationship(GENgraphRelTypes.HAS_NAME_FORM, nameForms);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;

		if ((gedcomXName.getNameForms() == null) || gedcomXName.getNameForms().isEmpty()) {
			throw new MissingRequiredRelationshipException(Name.class, gedcomXName.getId(), GENgraphRelTypes.HAS_NAME_FORM);
		}
	}

	public String getDateFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_ORIGINAL);
	}

	public List<NameForm> getNameForms() {
		return this.nameForms;
	}

	public Boolean getPreferred() {
		return (Boolean) this.getProperty(NodeProperties.Conclusion.PREFERRED);
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	public void setDateFormal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;
		this.setPreferred(gedcomXName.getPreferred());
		this.setType(gedcomXName.getType());

		if (gedcomXName.getDate() != null) {
			this.setDateFormal(gedcomXName.getDate().getFormal());
			this.setDateOriginal(gedcomXName.getDate().getOriginal());
		}
	}

	public void setPreferred(final Boolean preferred) {
		this.setProperty(NodeProperties.Conclusion.PREFERRED, preferred);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;

		for (final org.gedcomx.conclusion.NameForm nameForm : gedcomXName.getNameForms()) {
			this.addNameForms(new NameForm(this.getGraph(), nameForm));
		}
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

}
