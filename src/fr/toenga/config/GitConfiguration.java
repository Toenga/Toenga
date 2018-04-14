
package fr.toenga.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GitConfiguration
{
	
    private String	user;
    private String	sshKey;
    
}
