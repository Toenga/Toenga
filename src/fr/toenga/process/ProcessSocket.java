package fr.toenga.process;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProcessSocket
{
	
    private ServerSocket	server;
    private List<Socket>	sockets;
    private ToengaProcess	process;
    
    public ProcessSocket(ToengaProcess process, ServerSocket server, SocketAddress address) throws IOException
    {
        setSockets(new CopyOnWriteArrayList<Socket>());
        setProcess(process);
        (this.server = server).bind(address);
        new Thread(() -> this.waitEntry()).start();
    }
    
    public int socketCount()
    {
        return getSockets().size();
    }
    
    public void close()
    {
        for (Socket socket : getSockets())
        {
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
            	ex.printStackTrace();
            }
        }
        try
        {
            getServer().close();
        }
        catch (IOException ex2)
        {
        	ex2.printStackTrace();
        }
    }
    
    public void output(int output)
    {
        for (Socket socket : getSockets())
        {
            if (socket.isConnected())
            {
                try
                {
                    socket.getOutputStream().write(output);
                    socket.getOutputStream().flush();
                }
                catch (IOException ex)
                {
                	ex.printStackTrace();
                }
            }
        }
    }
    
    private void waitEntry()
    {
        try
        {
            Socket socket;
            while ((socket = getServer().accept()) != null)
            {
                this.accept(socket);
            }
        }
        catch (IOException ex)
        {
        	ex.printStackTrace();
        }
    }
    
    public void accept(Socket socket)
    {
        try
        {
            if (!getProcess().canAcceptSocket())
            {
            	socket.getOutputStream().write("This process console is already in use!\n".getBytes());
            	socket.close();
                return;
            }
            getProcess().getLogs().write(socket.getOutputStream());
            new Thread(() -> this.listen(socket)).start();
            getSockets().add(socket);
        }
        catch (IOException ex)
        {
        	ex.printStackTrace();
        }
    }
    
    private void listen(Socket socket)
    {
        try
        {
            final InputStream is = socket.getInputStream();
            int value = 0;
            
            while (socket.isConnected() && (value = is.read()) != -1)
            {
                getProcess().input(value);
            }
            
            getSockets().remove(socket);
        }
        catch (IOException ex)
        {
        	ex.printStackTrace();
        }
    }
    
}
