package org.gedcomx.persistence.graph.neo4j.model.common;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class TextValue extends GENgraphNode {

	public TextValue(final GENgraph graf, final org.gedcomx.common.TextValue gedcomXTextValue) throws MissingFieldException {
		super(graf, NodeTypes.TEXT_VALUE, gedcomXTextValue);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.common.TextValue gedcomXTextValue = (org.gedcomx.common.TextValue) gedcomXObject;
		if ((gedcomXTextValue.getValue() == null) || gedcomXTextValue.getValue().isEmpty()) {
			throw new MissingRequiredPropertyException(TextValue.class, NodeProperties.Generic.VALUE);
		}
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.TextValue gedcomXName = (org.gedcomx.common.TextValue) gedcomXObject;
		this.setLang(gedcomXName.getLang());
		this.setValue(gedcomXName.getValue());
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}
}
