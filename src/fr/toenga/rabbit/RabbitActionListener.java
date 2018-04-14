package fr.toenga.rabbit;

import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.rabbitmq.ToengaQueues;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.packets.actions.ToengaProcessClusterAction;

public class RabbitActionListener extends RabbitListener
{

	public RabbitActionListener(RabbitService rabbitService, String toengaName)
	{
		super(rabbitService, ToengaQueues.TOENGA_ACTION_RECEIVER_ACTION + toengaName, RabbitListenerType.SUBSCRIBER, true);
	}

	@Override
	public void onPacketReceiving(String body)
	{
		ToengaProcessClusterAction toengaClusterAction = GsonUtils.getGson().fromJson(body, ToengaProcessClusterAction.class);
		if (toengaClusterAction == null)
		{
			return;
		}

		toengaClusterAction.execute();
	}

}
