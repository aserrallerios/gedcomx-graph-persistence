package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredRelationshipException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class Name extends ConclusionSubnode {

	private final List<NameForm> nameForms = new LinkedList<>();

	protected Name(final GENgraph graph, final org.gedcomx.conclusion.Name gedcomXName) throws MissingFieldException {
		super(graph, NodeTypes.NAME, gedcomXName);
	}

	public void addNameForms(final NameForm nameForms) {
		this.nameForms.add(nameForms);
		this.createRelationship(RelTypes.HAS_NAME_FORM, nameForms);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;

		if ((gedcomXName.getNameForms() == null) || gedcomXName.getNameForms().isEmpty()) {
			throw new MissingRequiredRelationshipException(Name.class, gedcomXName.getId(), RelTypes.HAS_NAME_FORM);
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
