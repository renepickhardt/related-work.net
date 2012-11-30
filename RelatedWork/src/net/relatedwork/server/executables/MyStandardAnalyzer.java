package net.relatedwork.server.executables;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public final class MyStandardAnalyzer extends Analyzer {
	    private final Analyzer actual;
//	    private final HashSet<String> STOP_WORDS_SET = new HashSet<String>( Arrays.asList("stopword") );
	    private final HashSet<String> STOP_WORDS_SET = new HashSet<String>( );
	    
	    public MyStandardAnalyzer()
	    {
	        actual = new StandardAnalyzer( 
	        		Version.LUCENE_31,
	        		STOP_WORDS_SET // uses default if commented out
	        		);
	    }

	    @Override
	    public TokenStream tokenStream( String fieldName, Reader reader )
	    {
	        return actual.tokenStream( fieldName, reader );
	    }

}
