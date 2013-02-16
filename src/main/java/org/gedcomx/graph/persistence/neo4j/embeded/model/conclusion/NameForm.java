package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class NameForm extends GENgraphNode {

	private final List<NamePart> nameParts;

	protected NameForm(final GENgraph graph, final org.gedcomx.conclusion.NameForm gedcomXNameForm) throws MissingFieldException {
		super(graph, NodeTypes.NAME_FORM, gedcomXNameForm);

		this.nameParts = new LinkedList<>();

		for (final org.gedcomx.conclusion.NamePart namePart : gedcomXNameForm.getParts()) {
			this.addNameParts(new NamePart(graph, namePart));
		}
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
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;
		this.setLang(gedcomXNameForm.getLang());
		this.setFullText(gedcomXNameForm.getFullText());
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

}