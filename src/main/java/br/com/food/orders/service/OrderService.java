package br.com.food.orders.service;

import br.com.food.orders.DTO.OrderDTO;
import br.com.food.orders.DTO.StatusDTO;
import br.com.food.orders.model.Order;
import br.com.food.orders.model.Status;
import br.com.food.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderDTO getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);

        order.setDatetime(LocalDateTime.now());
        order.setStatus(Status.REALIZADO);
        order.getItems().forEach(item -> item.setOrder(order));
        Order save = orderRepository.save(order);
        return modelMapper.map(save, OrderDTO.class);
    }

    public OrderDTO updateStatus(Long id, StatusDTO statusDTO) {
        Order order = orderRepository.byIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(statusDTO.getStatus());
        orderRepository.updateStatus(statusDTO.getStatus(), order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public void approveOrderPayment(Long id) {
         Order order = orderRepository.byIdWithItems(id);

         if (order == null) {
             throw new EntityNotFoundException();
         }

         order.setStatus(Status.PAGO);
         orderRepository.updateStatus(Status.PAGO, order);
    }
}
