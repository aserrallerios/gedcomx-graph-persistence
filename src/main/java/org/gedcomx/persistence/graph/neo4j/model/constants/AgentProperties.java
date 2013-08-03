package org.gedcomx.persistence.graph.neo4j.model.constants;

public enum AgentProperties implements NodeProperties {
	HOMEPAGE, OPENID, EMAILS, PHONES, STREET, STREET2, STREET3, VALUE, STATE_OR_PROVINCE, CITY, COUNTRY, POSTAL_CODE, ACCOUNT_NAME, SERVICE_HOMEPAGE, IDENTIFIER_TYPE;

	private final boolean indexed;
	private final IndexNames indexNames;
	private final boolean unique;

	private AgentProperties() {
		this.indexed = false;
		this.unique = false;
		this.indexNames = null;
	}

	private AgentProperties(final boolean indexed, final boolean unique, final IndexNames indexNames) {
		this.indexed = indexed;
		this.unique = unique;
		this.indexNames = indexNames;
	}

	@Override
	public IndexNames getIndexName() {
		return this.indexNames;
	}

	@Override
	public boolean isIndexed() {
		return this.indexed;
	}

	@Override
	public boolean isUnique() {
		return this.unique;
	}

}