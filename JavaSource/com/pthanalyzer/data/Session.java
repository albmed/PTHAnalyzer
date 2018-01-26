package com.pthanalyzer.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Wrapper for PDB table SESSION (Worthless) 
 * 
 * @author albgonza
 *
 */
public class Session implements ObjectSqlite {
	private String PTHVersion; 
	private Date date; 
	private String logVersion;

	public String getPTHVersion() {
		return PTHVersion;
	}

	public void setPTHVersion(String pTHVersion) {
		PTHVersion = pTHVersion;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLogVersion() {
		return logVersion;
	}

	public void setLogVersion(String logVersion) {
		this.logVersion = logVersion;
	}

	@Override
	public void loadResultSet(SQLiteStatement st) throws SQLiteException {
		this.PTHVersion = st.columnString(0);
		String dateStr = st.columnString(1); 
		String timeStr = st.columnString(2); 

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.date = df.parse(dateStr + timeStr);  
		}
		catch(ParseException e) {
			// Error 
		}
		this.logVersion = st.columnString(3);
	}
	
	
}
