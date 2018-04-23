package fr.toenga.socketserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaSocketServer extends WebSocketServer
{

	public ToengaSocketServer(String address, int port) throws IOException
	{
		super(new InetSocketAddress(port));
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
		conn.send("Welcome to the server!");
		broadcast("new connection: " + handshake.getResourceDescriptor());
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
		broadcast(message);
		System.out.println(conn + ": " + message);
	}
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message)
	{
		broadcast(message.array());
		System.out.println(conn + ": " + message);
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

}
