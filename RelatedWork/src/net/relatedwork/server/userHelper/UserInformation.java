package net.relatedwork.server.userHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.NewUserAction;

public class UserInformation implements Serializable {
	public String emailAddress = "";
	public String username = "";
	public ArrayList<String> sessionList = new ArrayList<String>();	
	
	public UserInformation( NewUserAction newUserAction ) {
		this.emailAddress = newUserAction.getEmail();
		this.username = newUserAction.getUsername();
		this.registerSessionId(newUserAction.getSession().sessionId);
	}

	public void registerSessionId(String sessionId) {
		// TODO Auto-generated method stub
		if (! sessionList.contains(sessionId)) {
			sessionList.add(sessionId);
		}
	}
	
	
	public String getSavePath(){
		try {
			return (new File(Config.get().userDir, emailAddress + ".txt")).getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	public void save() {
		File target = new File(Config.get().userDir, emailAddress + ".txt");
		target.getParentFile().mkdirs();
		
		IOHelper.log("Writing file: " + this.getSavePath());
		BufferedWriter writer;
		
		try {
			if ( (new File(this.getSavePath())).exists() ){
				writer = IOHelper.openWriteFile(this.getSavePath());
				writer.write("username: " + username + "\n");
				writer.write("email: " + emailAddress + "\n");
			} else {
				writer = IOHelper.openWriteFile(this.getSavePath());
			}
			for (String sessionId: sessionList){
				writer.write(sessionId + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
