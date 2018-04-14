package fr.toenga;

import java.io.File;
import java.io.IOException;

public class Bootstrap
{
	
    public static void main(final String[] args)
    {
        System.getProperties().setProperty("org.newsclub.net.unix.library.path", new File("libs").getAbsolutePath());
        final Toenga t = Toenga.getInstance();
        Runtime.getRuntime().addShutdownHook(new Thread(Bootstrap::shutdown));
        try
        {
            t.createProcess("spigot-1.8").start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void shutdown()
    {
        Toenga.getInstance().shutdownAllProcess();
    }
    
}

