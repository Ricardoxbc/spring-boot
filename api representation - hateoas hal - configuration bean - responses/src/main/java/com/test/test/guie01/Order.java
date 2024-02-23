package com.test.test.guie01;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Order_b")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private Status status;

    public Order() {
    }

    public Order(String des, Status st) {
        this.description = des;
        this.status = st;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order [description=" + description + ", status=" + status + "]";
    }

    enum Status {
        IN_PRO,
        COMPLETED,
        CANCELED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private static class OrderModelAssembly implements RepresentationModelAssembler<Order, EntityModel<Order>> {
        @Override
        public EntityModel<Order> toModel(Order entity) {
            EntityModel<Order> om = EntityModel.of(entity,
                    linkTo(methodOn(OrderController.class).all()).withRel("orders"),
                    linkTo(methodOn(OrderController.class).get(entity.getId())).withSelfRel());

            if (om.getContent().getStatus() == Status.IN_PRO) {
                om.add(linkTo(methodOn(OrderController.class).complete(entity.getId())).withRel("complete"));
                om.add(linkTo(methodOn(OrderController.class).cancel(entity.getId())).withRel("cancel"));
            }

            return om;
        }
    }

    public static EntityModel<Order> toEntityModel(Order or) {
        return new OrderModelAssembly().toModel(or);
    }
}
