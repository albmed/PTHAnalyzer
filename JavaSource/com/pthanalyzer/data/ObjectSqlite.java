package com.pthanalyzer.data;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public interface ObjectSqlite {
	public void loadResultSet(SQLiteStatement st) throws SQLiteException; 
}
