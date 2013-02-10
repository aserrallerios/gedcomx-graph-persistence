package org.gedcomx.graph.persistence.neo4j.embeded.model.common;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;

public class TextValue extends GENgraphNode {

	public TextValue(final org.gedcomx.common.TextValue gedcomXName) throws MissingRequiredPropertyException {
		super(NodeTypes.TEXT_VALUE, gedcomXName);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingRequiredPropertyException {
		final org.gedcomx.common.TextValue gedcomXName = (org.gedcomx.common.TextValue) gedcomXObject;
		if (gedcomXName.getValue() == null) {
			throw new MissingRequiredPropertyException();
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
