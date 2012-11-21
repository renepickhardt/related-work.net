package net.relatedwork.client.place;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NameTokens implements IsSerializable {

	NameTokens(){
		
	}
	
	public static final String main = "!main";
	public static final String imprint = "!imprint";
	public static final String about = "!about";
	public static final String data = "!data";
	public static final String license = "!license";
	public static final String author = "!author";
	public static final String serp = "serp";

	public static final String SESSION_ID = "sessionId";

	
	public static String getMain() {
		return main;
	}

	public static String getImprint() {
		return imprint;
	}

	public static String getAbout() {
		return about;
	}

	public static String getData() {
		return data;
	}

	public static String getLicense() {
		return license;
	}

	public static String getAuthor() {
		return author;
	}

	public static String getSerp() {
		return serp;
	}
}
