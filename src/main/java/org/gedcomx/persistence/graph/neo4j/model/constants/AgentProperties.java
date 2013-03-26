package org.gedcomx.persistence.graph.neo4j.model.constants;


public enum AgentProperties implements NodeProperties {
	HOMEPAGE, OPENID, EMAILS, PHONES, STREET, STREET2, STREET3, VALUE, STATE_OR_PROVINCE, CITY, COUNTRY, POSTAL_CODE, ACCOUNT_NAME, SERVICE_HOMEPAGE, IDENTIFIER_TYPE;

	private final boolean indexed;
	private final IndexNames indexName;

	private AgentProperties() {
		this.indexed = false;
		this.indexName = null;
	}

	private AgentProperties(final boolean indexed, final IndexNames indexName) {
		this.indexed = indexed;
		this.indexName = indexName;
	}

	@Override
	public IndexNames getIndexName() {
		return this.indexName;
	}

	@Override
	public boolean isIndexed() {
		return this.indexed;
	}

}