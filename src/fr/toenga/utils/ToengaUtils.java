package fr.toenga.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import fr.toenga.Toenga;
import fr.toenga.common.utils.general.FileUtils;
import fr.toenga.config.ToengaConfiguration;
import fr.toenga.process.ToengaProcess;

public class ToengaUtils
{
	
    public static String formatToengaStr(String str, ToengaProcess process)
    {
        final StringBuilder builder = new StringBuilder();
        int start = 0;
        int startKey = -1;
        for (int i = 0; i < str.length(); ++i)
        {
            if (startKey != -1 && str.charAt(i) == '}')
            {
                final String key = str.substring(startKey + 1, i);
                String value = process.getMetadata().keys.get(key);
                if (value == null)
                {
                    value = Toenga.getInstance().getGlobalVars().get(key);
                }
                if (value != null)
                {
                    builder.append(str, start, startKey);
                    builder.append(value);
                    start = i + 1;
                }
                startKey = -1;
            }
            else if (str.charAt(i) == '{')
            {
                startKey = i;
            }
        }
        if (start < str.length())
        {
            builder.append(str, start, str.length());
        }
        return builder.toString();
    }
    
    public static void pullOrClone(ToengaConfiguration configuration, ToengaConfiguration.GitConfiguration.Repository repository)
    {
        boolean shouldClone = false;
        Git repos = null;
        final File folder = new File(Toenga.dataFolder, repository.getFolder());
        try
        {
            repos = Git.open(folder);
            final String url = repos.getRepository().getConfig().getString("remote", "origin", "url");
            if (!url.equals(repository.getUrl()))
            {
                shouldClone = true;
            }
        }
        catch (IOException e3)
        {
            shouldClone = true;
        }
        if (!shouldClone)
        {
            try
            {
                System.out.println("Pull " + repository.getUrl() + " (" + repository.getBranch() + ")" + " in " + repository.getFolder() + "..");
                final PullCommand pull = repos.pull();
                pull.setProgressMonitor(new TextProgressMonitor());
                pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider(configuration.getToengaData().getUser(), configuration.getToengaData().getPassword()));
                pull.call();
            }
            catch (GitAPIException e)
            {
                System.err.println("Can't pull git repository: " + repository);
                e.printStackTrace();
            }
            return;
        }
        if (folder.exists())
        {
            System.out.println("Folder " + repository.getFolder() + " already exist, but we have to clone a repos. Deleting existing folder.");
            FileUtils.delete(folder);
        }
        System.out.println("Clone " + repository.getUrl() + " (" + repository.getBranch() + ")" + " in " + repository.getFolder() + "..");
        final CloneCommand clone = Git.cloneRepository();
        clone.setDirectory(folder);
        clone.setURI(repository.getUrl());
        clone.setCloneSubmodules(true);
        clone.setBranchesToClone(Arrays.asList(repository.getBranch()));
        clone.setProgressMonitor(new TextProgressMonitor());
        clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(configuration.getToengaData().getUser(), configuration.getToengaData().getPassword()));
        try
        {
            clone.call();
        }
        catch (GitAPIException e2)
        {
            System.err.println("Can't clone git repository: " + repository);
            e2.printStackTrace();
        }
    }
}
