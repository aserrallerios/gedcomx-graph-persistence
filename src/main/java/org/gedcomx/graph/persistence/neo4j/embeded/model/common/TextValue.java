package org.gedcomx.graph.persistence.neo4j.embeded.model.common;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

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
	protected void setInitialProperties(final Object gedcomXObject) {
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
