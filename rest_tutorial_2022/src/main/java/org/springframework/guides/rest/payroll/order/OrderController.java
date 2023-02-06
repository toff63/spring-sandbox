package org.springframework.guides.rest.payroll.order;

import jakarta.websocket.server.PathParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderModelAssembler assembler;

    public OrderController(OrderRepository orderRepository, OrderModelAssembler assembler) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all(){
        List<Order> orders = orderRepository.findAll();
        return assembler.toCollectionModel(orders);
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> add(@RequestBody Order newOrder){
        EntityModel<Order> order = assembler.toModel(orderRepository.save(newOrder));
        return ResponseEntity.created(order.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(order);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable("id") Long id){
        return updateOrder(id, Status.CANCELLED);
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable("id") Long id){
        return updateOrder(id, Status.COMPLETED);
    }

    private  ResponseEntity<?> updateOrder(Long id, Status newStatus){
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));;

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(newStatus);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't update an order that is in the " + order.getStatus() + " status to " + newStatus));
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable("id") Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return assembler.toModel(order);
    }


}
