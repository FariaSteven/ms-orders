package br.com.food.orders.controller;

import br.com.food.orders.DTO.OrderDTO;
import br.com.food.orders.DTO.StatusDTO;
import br.com.food.orders.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping()
    public List<OrderDTO> listAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> listById(@PathVariable @NotNull Long id) {
        OrderDTO orderDTO = orderService.getById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/porta")
    public String getPort(@Value("${local.server.port}") String port) {
        return String.format("Requisição respondida pela instância executando na porta %s", port);
    }

    @PostMapping()
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO, UriComponentsBuilder uriComponentsBuilder) {
        OrderDTO orderMade = orderService.createOrder(orderDTO);

        URI address = uriComponentsBuilder.path("/pedidos/{id}").buildAndExpand(orderMade.getId()).toUri();

        return ResponseEntity.created(address).body(orderMade);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        OrderDTO orderDTO = orderService.updateStatus(id, statusDTO);

        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
        orderService.approveOrderPayment(id);

        return ResponseEntity.ok().build();
    }
}
