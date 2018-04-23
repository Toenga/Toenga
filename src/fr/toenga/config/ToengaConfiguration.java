package fr.toenga.config;

import fr.toenga.common.tech.rabbitmq.setting.RabbitSettings;
import fr.toenga.common.tech.redis.setting.RedisSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToengaConfiguration
{
	
	private String								toengaName;
	private String								toengaSocket;
	private ToengaWebSocketServerConfiguration	webSocketServer;
    private boolean								allowParallelConsole;
    private GitConfiguration					toengaData;
    private RedisSettings						redisConfig;
    private RabbitSettings						rabbitConfig;
    
    public ToengaConfiguration()
    {
        setToengaSocket("toenga.sock");
        setWebSocketServer(new ToengaWebSocketServerConfiguration());
        setAllowParallelConsole(false);
        setToengaData(new GitConfiguration());
        setRedisConfig(new RedisSettings(new String[] { "localhost" }, 5672, "admin123", 0, 32));
    }
    
    @Data
    public class GitConfiguration
    {
    	
        private String			user;
        private String			password;
        private Repository[]	repositories;
        private Locations		locations;
        
        public GitConfiguration()
        {
            setUser("example");
            setPassword("password");
            setRepositories(new Repository[] { new Repository() });
            setLocations(new Locations());
        }

        @Data
        public class Repository
        {
        	
            private String	url;
            private String	folder;
            private String	branch;
            
            public Repository()
            {
                setUrl("http://example.com/example.git");
                setFolder("example");
                setBranch("master");
            }
            
            @Override
            public String toString()
            {
                return "ToengaConfiguration.GitConfiguration.Repository(url=" + getUrl() + ", folder=" + getFolder() + ", branch=" + getBranch() + ")";
            }
            
        }
        
        @Data
        public class Locations
        {
        	
            private ModelFolder[]	models;
            private String			toengaConfig;
            private String			toengaJar;
            
            public Locations()
            {
                setModels(new ModelFolder[] { new ModelFolder() });
                setToengaConfig("toenga/config.json");
                setToengaJar("toenga/Toenga.jar");
            }
            
            @Data
            public class ModelFolder
            {
            	
                private String folder;
                private String source;
                private String dest;
                
                public ModelFolder()
                {
                    setFolder("models");
                    setSource(".");
                    setDest(".");
                }
                
            }
            
        }
        
    }
    
}