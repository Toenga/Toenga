package fr.toenga.rabbit;

import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.rabbitmq.ToengaQueues;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.packets.createprocess.ToengaCreateProcessAction;

public class RabbitCreateProcessListener extends RabbitListener
{

	public RabbitCreateProcessListener(RabbitService rabbitService, String toengaName)
	{
		super(rabbitService, ToengaQueues.TOENGA_ACTION_RECEIVER_NEWPROCESS + toengaName, RabbitListenerType.SUBSCRIBER, true);
	}

	@Override
	public void onPacketReceiving(String body)
	{
		ToengaCreateProcessAction toengaCreateProcessAction = GsonUtils.getGson().fromJson(body, ToengaCreateProcessAction.class);
		if (toengaCreateProcessAction == null)
		{
			return;
		}

		toengaCreateProcessAction.execute();
	}

}
