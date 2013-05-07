package org.gedcomx.persistence.graph.neo4j.exceptions;

public class GenericError extends RuntimeException {

	private final String msg;

	public GenericError(final String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
