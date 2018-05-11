package fr.toenga.socketserver;

import java.io.IOException;
import java.net.InetAddress;

import org.java_websocket.WebSocket;

import fr.toenga.Toenga;
import fr.toenga.common.tech.redis.RedisService;
import fr.toenga.common.tech.redis.methods.get.RedisObjectGetClassMethod;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.process.ToengaProcess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode (callSuper = false)
@AllArgsConstructor
@Data
public class TokenInfo extends Callback<TokenRedisInfo>
{

	public transient WebSocket socket;
	public String token;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void call(final RedisService service)
	{
		new RedisObjectGetClassMethod(service, "", TokenRedisInfo.class, this).work(service.getJedis());
	}

	@Override
	public void done(final TokenRedisInfo result, final Throwable error)
	{
		if (error != null || result == null || result.uniqueId == null)
		{
			if (error != null)
			{
				error.printStackTrace();
			}
			getSocket().close();
		}
		else
		{
			try
			{
				if (!getSocket().getRemoteSocketAddress().getAddress().equals(InetAddress.getByName(result.ip)))
				{
					getSocket().close();
					return;
				}

				final ToengaProcess process = Toenga.getInstance().getProcess(result.uniqueId);

				if (process == null)
				{
					getSocket().close();
					return;
				}

				process.acceptSocket(getSocket());
			}
			catch (IOException exception)
			{
				exception.printStackTrace();
				getSocket().close();
			}
		}
	}

}