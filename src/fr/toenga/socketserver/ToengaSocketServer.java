package fr.toenga.socketserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import fr.toenga.common.utils.general.AES;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaSocketServer extends WebSocketServer
{

	private AES				encryption;
	private Set<String>		whitelist;

	public ToengaSocketServer(int port, String encryptionKey, Set<String> whitelist) throws IOException
	{
		super(new InetSocketAddress(port));
		setEncryption(new AES(encryptionKey.getBytes("UTF-8")));
		setWhitelist(whitelist);
	}

	/*private void listen(Socket socket)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String line = reader.readLine();
			try
			{
				TokenInfo info = GsonUtils.getGson().fromJson(line, TokenInfo.class);
				info.setSocket(socket);
				info.call(Toenga.getInstance().getRedisService());
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				System.out.print("Sended: '" + line + "'");
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}*/

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake)
	{
		if (!isAllowed(conn))
		{
			conn.close();
			return;
		}
		conn.send("Welcome to the server!");
		broadcast("new connection: " + handshake.getResourceDescriptor());
		// Debug
		System.out.print("Content: " + handshake.getContent());
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " is connected!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote)
	{
		broadcast(conn + " is now disconnected!");
		System.out.println(conn + " is now disconnected!");
	}

	@Override
	public void onMessage(WebSocket conn, String message)
	{
		if (!isAllowed(conn))
		{
			conn.close();
			return;
		}
		try
		{
			String plaintextMessage = decryptMessage(message);
			broadcast(plaintextMessage);
			System.out.println(conn + ": " + plaintextMessage);
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

	private boolean isAllowed(WebSocket conn)
	{
		return getWhitelist().contains(conn.getRemoteSocketAddress().getAddress().getHostAddress());
	}

}
