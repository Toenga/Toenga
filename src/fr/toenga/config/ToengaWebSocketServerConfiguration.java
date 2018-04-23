package fr.toenga.config;

import java.util.HashSet;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToengaWebSocketServerConfiguration
{

    private int					port;
    private String				encryptionKey;
	private HashSet<String>		whitelist;
	
	public ToengaWebSocketServerConfiguration()
	{
		setPort(12522);
		setEncryptionKey("-");
		setWhitelist(Sets.newHashSet("127.0.0.1", "127.0.0.2"));
	}
    
}
