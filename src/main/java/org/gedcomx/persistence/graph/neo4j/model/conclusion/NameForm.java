package org.gedcomx.persistence.graph.neo4j.model.conclusion;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;

public class NameForm extends GENgraphNode {

	private final List<NamePart> nameParts;

	protected NameForm(final GENgraph graph, final org.gedcomx.conclusion.NameForm gedcomXNameForm) throws MissingFieldException {
		super(NodeTypes.NAME_FORM, gedcomXNameForm);

		this.nameParts = new LinkedList<>();

	}

	public void addNameParts(final NamePart namePart) {
		this.nameParts.add(namePart);
		this.createRelationship(RelTypes.HAS_NAME_PART, namePart);
	}

	public String getFullText() {
		return (String) this.getProperty(NodeProperties.Conclusion.FULL_TEXT);
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public List<NamePart> getNameParts() {
		return this.nameParts;
	}

	public void setFullText(final String fullText) {
		this.setProperty(NodeProperties.Conclusion.FULL_TEXT, fullText);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;
		this.setLang(gedcomXNameForm.getLang());
		this.setFullText(gedcomXNameForm.getFullText());
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;

		for (final org.gedcomx.conclusion.NamePart namePart : gedcomXNameForm.getParts()) {
			this.addNameParts(new NamePart(this.getGraph(), namePart));
		}
	}

}
