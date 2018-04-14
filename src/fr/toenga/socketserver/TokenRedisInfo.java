package fr.toenga.socketserver;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class TokenRedisInfo
{
	
	public UUID		uniqueId;
	public String	ip;
	public int		perm;
	
}