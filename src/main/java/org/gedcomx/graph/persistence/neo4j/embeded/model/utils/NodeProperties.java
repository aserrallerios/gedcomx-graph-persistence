package org.gedcomx.graph.persistence.neo4j.embeded.model.utils;

public interface NodeProperties {

	public static enum Agent implements NodeProperties {
		HOMEPAGE, OPENID, EMAILS, PHONES
	}

	public static enum Conclusion implements NodeProperties {

	}

	public static enum Generic implements NodeProperties {
		ID, NODE_TYPE
	}

	public static enum SourceDescription implements NodeProperties {

	}

	public String name();

}
