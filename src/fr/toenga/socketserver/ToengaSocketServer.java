package fr.toenga.socketserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import fr.toenga.Toenga;
import fr.toenga.common.utils.general.AES;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaSocketServer extends WebSocketServer
{

	private AES				encryption;

	public ToengaSocketServer(int port, String encryptionKey) throws IOException
	{
		super(new InetSocketAddress(port));
		setEncryption(new AES(encryptionKey.getBytes("UTF-8")));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake)
	{
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " is connected!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		System.out.println(conn + " is now disconnected!");
	}

	@Override
	public void onMessage(WebSocket conn, String message)
	{
		try
		{
			String plaintextMessage = decryptMessage(message);
			try
			{
				TokenInfo info = GsonUtils.getGson().fromJson(plaintextMessage, TokenInfo.class);
				info.setSocket(conn);
				info.call(Toenga.getInstance().getRedisService());
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				System.out.print("Sended: '" + plaintextMessage + "'");
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			// Something gone wrong, we close the connection
			conn.close();
		}
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message)
	{
		onMessage(conn, getMessage(message.array()));
	}

	@Override
	public void onError(WebSocket conn, Exception ex)
	{
		ex.printStackTrace();
	}

	@Override
	public void onStart()
	{
		System.out.println("Server started on port " + getPort());
	}

	private String getMessage(byte[] bytes)
	{
		return new String(bytes, StandardCharsets.UTF_8);
	}

	private byte[] getBytes(String message) throws UnsupportedEncodingException
	{
		return message.getBytes("UTF-8");
	}

	private String decryptMessage(String message) throws Exception
	{
		return decryptMessage(getBytes(message));
	}

	private String decryptMessage(byte[] bytes) throws Exception
	{
		return getMessage(getEncryption().decrypt(bytes));
	}

}
