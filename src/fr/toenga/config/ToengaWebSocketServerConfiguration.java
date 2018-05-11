package fr.toenga.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToengaWebSocketServerConfiguration
{

    private int					port;
    private String				encryptionKey;
	
	public ToengaWebSocketServerConfiguration()
	{
		setPort(12522);
		setEncryptionKey("-");
	}
    
}
