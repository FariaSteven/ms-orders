package br.com.food.orders.amqp;

import br.com.food.orders.DTO.PaymentDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    @RabbitListener(queues = "pagamentos.detalhes-pedido")
    public void receiveMessage(PaymentDTO paymentDTO) {
        System.out.println(paymentDTO.getId());
        System.out.println(paymentDTO.getNumber());

        if (paymentDTO.getNumber().equals("0000")) {
            throw new RuntimeException("não consegui processar");
        }

        String message = """
                Dados do pagamento: %s
                Numero do pedido: %s
                Valor R$: %s
                Status: %s
                """.formatted(
                        paymentDTO.getId(),
                        paymentDTO.getOrderId(),
                        paymentDTO.getAmount(),
                        paymentDTO.getStatus()
        );

        System.out.println("Recebi a mensagem " + message);
    }
}
