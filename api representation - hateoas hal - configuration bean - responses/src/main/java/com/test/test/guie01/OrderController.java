package com.test.test.guie01;

import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository repository;

    // public OrderController(OrderRepository repository) {
    // this.repository = repository;
    // }

    @GetMapping("/o/{id}")
    public EntityModel<Order> get(@PathVariable Long id) {
        Order or = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id + ""));
        return Order.toEntityModel(or);
    }

    @GetMapping("/o")
    public CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll()
                .stream()
                .map(Order::toEntityModel)
                .collect(Collectors.toList());
        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @PostMapping("/o")
    public ResponseEntity<?> create(@RequestBody Order entity) {
        EntityModel<Order> om = Order.toEntityModel(repository.save(entity));
        return ResponseEntity.created(om.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(om);
    }

    @PutMapping("o/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Order or = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(null));
        if (or.getStatus() != Order.Status.IN_PRO) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create().withTitle("No puedes confirmar un pedido ya realizado")
                            .withDetail("que no chico"));
        }

        or.setStatus(Order.Status.COMPLETED);
        repository.save(or);
        return ResponseEntity.ok(Order.toEntityModel(or));

    }

    @DeleteMapping("o/{id}")
    ResponseEntity<?> cancel(@PathVariable Long id) {
        Order or = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("id: " + id));
        if (or.getStatus() != Order.Status.IN_PRO) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE).body(
                            Problem.create().withTitle("Titulo normal").withDetail("Detalle simple"));
        }

        or.setStatus(Order.Status.CANCELED);
        return ResponseEntity.ok(Order.toEntityModel(repository.save(or)));
    }

}
