package fr.toenga.behaviour;

import fr.toenga.config.ProcessConfiguration.ProcessConfigurationBehaviour;
import fr.toenga.config.ToengaConfiguration.GitConfiguration.Locations.ModelFolder;
import fr.toenga.process.ToengaProcess;
import fr.toenga.utils.ToengaUtils;
import lombok.Data;

@Data
public class BehaviourCaller
{
	
    private ModelFolder	folder;
    private Behaviour	behaviour;
    private String[]	arguments;
    
    public static BehaviourCaller createBehaviourCaller(ModelFolder folder, ProcessConfigurationBehaviour beh)
    {
        final Behaviour behaviour = Behaviour.getBehaviour(beh.getBehaviour());
        assert behaviour == null : "Unknow behaviour " + beh.getBehaviour();
        return new BehaviourCaller(behaviour, (beh.getArguments() == null) ? new String[0] : beh.getArguments());
    }
    
    private BehaviourCaller(final Behaviour behaviour, final String[] arguments)
    {
        setBehaviour(behaviour);
        setArguments(arguments);
    }
    
    public void execute(ToengaProcess process)
    {
        String[] res = new String[getArguments().length];
        for (int i = 0; i < res.length; ++i)
        {
            res[i] = ToengaUtils.formatToengaStr(getArguments()[i], process);
        }
        getBehaviour().execute(process, res);
    }
}
