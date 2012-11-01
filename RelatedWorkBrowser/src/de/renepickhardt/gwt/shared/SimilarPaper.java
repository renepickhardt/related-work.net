package de.renepickhardt.gwt.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SimilarPaper extends Paper implements IsSerializable {
	public Double similarityScore;
	public ArrayList<String> authors;
	public SimilarPaper() {
		super();
		authors = new ArrayList<String>();
	}

}
