package org.gedcomx.graph.persistence.neo4j.embeded.model.utils;

public interface NodeProperties {

	public static enum Agent implements NodeProperties {
		HOMEPAGE, OPENID, EMAILS, PHONES, STREET, STREET2, STREET3, VALUE, STATE_OR_PROVINCE, CITY, COUNTRY, POSTAL_CODE, ACCOUNT_NAME, SERVICE_HOMEPAGE, IDENTIFIER_TYPE
	}

	public static enum Conclusion implements NodeProperties {

	}

	public static enum Generic implements NodeProperties {
		ID, NODE_TYPE, IDENTIFIER_TYPE, VALUE, LANG
	}

	public static enum SourceDescription implements NodeProperties {

	}

	public String name();

}
