package com.github.haflife3.datazilla.pojo;


public class SqlParts {
	private String preClause;
	private String where;
	private Object[] values;
	public String getPreClause() {
		return preClause;
	}
	public void setPreClause(String preClause) {
		this.preClause = preClause;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}

}
