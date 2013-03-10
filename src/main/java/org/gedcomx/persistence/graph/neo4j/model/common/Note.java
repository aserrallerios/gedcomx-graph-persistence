package org.gedcomx.persistence.graph.neo4j.model.common;

import java.util.Date;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class Note extends GENgraphNode {

	public Note(final GENgraph graph, final org.gedcomx.common.Note gedcomXNote) throws MissingFieldException {
		super(NodeTypes.NOTE, gedcomXNote);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		if ((gedcomXNote.getText() == null) || gedcomXNote.getText().isEmpty()) {
			throw new MissingRequiredPropertyException(Note.class, gedcomXNote.getId(), NodeProperties.Generic.TEXT);
		}
	}

	public String getAttributionChangeMessage() {
		return (String) this.getProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE);
	}

	public Date getAttributionModifiedConfidence() {
		return new Date((Long) this.getProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED));
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public String getSubject() {
		return (String) this.getProperty(NodeProperties.Generic.SUBJECT);
	}

	public String getText() {
		return (String) this.getProperty(NodeProperties.Generic.TEXT);
	}

	public void setAttributionChangeMessage(final String changeMessage) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_CHANGE_MESSAGE, changeMessage);

	}

	public void setAttributionModifiedConfidence(final Date modified) {
		this.setProperty(NodeProperties.Generic.ATTRIBUTION_MODIFIED, modified.getTime());
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		this.setLang(gedcomXNote.getLang());
		this.setSubject(gedcomXNote.getSubject());
		this.setText(gedcomXNote.getText());
		if (gedcomXNote.getAttribution() != null) {
			this.setAttributionModifiedConfidence(gedcomXNote.getAttribution().getModified());
			this.setAttributionChangeMessage(gedcomXNote.getAttribution().getChangeMessage());
		}
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	public void setSubject(final String subject) {
		this.setProperty(NodeProperties.Generic.SUBJECT, subject);
	}

	public void setText(final String text) {
		this.setProperty(NodeProperties.Generic.TEXT, text);
	}

}
