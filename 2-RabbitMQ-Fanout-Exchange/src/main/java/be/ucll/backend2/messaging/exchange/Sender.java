package be.ucll.backend2.messaging.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("sender")
public class Sender {

    private static final Logger log = LoggerFactory.getLogger(Sender.class);

    private final RabbitTemplate rabbitTemplate;
    private final Exchange exchange;

    private long i = 0;

    public Sender(RabbitTemplate rabbitTemplate,
                  Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @Scheduled(fixedRate = 1000, initialDelay = 500)
    public void sendMessage() {
        final var message = new Message("Message " + i);
        log.info("Sending message: {}", message);
        // Use the three-argument variant of convertAndSend to send
        // the message to an exchange instead of directly to a queue.
        rabbitTemplate.convertAndSend(
                exchange.getName(),
                "",
                message
        );
        i++;
    }

}
