package de.renepickhardt.gwt.server.executables;

import org.neo4j.graphdb.Node;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import de.renepickhardt.gwt.server.neo4jHelper.benchmarks.FriendOfAFriendQueryBenchmark;
import de.renepickhardt.gwt.server.utils.Config;

/**
 * this file is the entry point to several benchmarks that are against the neo4j graph db. 
 * 
 * right now this benchmak  tests the runtime of the core java api against traverser framework against cypher for a FOAF like query. 
 * 
 * @author rpickhardt
 *
 */
public class BenchmarkMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmbeddedReadOnlyGraphDatabase graphDB = new EmbeddedReadOnlyGraphDatabase(Config.get().neo4jDbPath);
		FriendOfAFriendQueryBenchmark foafqb = new FriendOfAFriendQueryBenchmark(graphDB);
		
		int cnt = 0;
		for (Node n : graphDB.getAllNodes()){
			if (++cnt % 10000 == 0){
				System.out.println(cnt);
			}
		}
		
		System.out.println("done start benchmark");
		
		for (int i = 0;i<4;i++){
			for (int j = 1;j<10;j++){
				int base = (int)Math.pow(10, i);
					foafqb.coAuthorBenchmark(base*j);
			}
		}
		
//		for (int i = 1000 ; i < 100000; i = i + 1000){
//			t.coAuthorBenchmark(i);
//		}

	}

}
