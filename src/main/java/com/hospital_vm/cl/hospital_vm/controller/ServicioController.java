package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.Servicio;
import com.hospital_vm.cl.hospital_vm.model.ServicioDTO;
import com.hospital_vm.cl.hospital_vm.service.FacturaService;
import com.hospital_vm.cl.hospital_vm.service.ServicioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

  @Autowired
  private ServicioService servicioService;

  @Autowired
  private FacturaService facturaService;

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Servicio>>> listar() {
    List<Servicio> servicios = servicioService.findAll();

    if (servicios.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<EntityModel<Servicio>> serviciosModel = servicios.stream()
        .map(servicio -> EntityModel.of(servicio,
            linkTo(methodOn(ServicioController.class).buscar(servicio.getId())).withSelfRel(),
            linkTo(methodOn(ServicioController.class).listar()).withRel("collection"),
            linkTo(methodOn(ServicioController.class).actualizar(servicio.getId(), servicio)).withRel("update"),
            linkTo(methodOn(ServicioController.class).eliminar(servicio.getId())).withRel("delete")))
        .collect(Collectors.toList());

    CollectionModel<EntityModel<Servicio>> collectionModel = CollectionModel.of(
        serviciosModel,
        linkTo(methodOn(ServicioController.class).listar()).withSelfRel());

    return ResponseEntity.ok(collectionModel);
  }

  @PostMapping
  public ResponseEntity<EntityModel<Servicio>> guardar(@RequestBody ServicioDTO servicioDTO) {
    Servicio servicioNuevo = servicioService.crearServicio(servicioDTO);

    EntityModel<Servicio> entityModel = EntityModel.of(servicioNuevo,
        linkTo(methodOn(ServicioController.class).buscar(servicioNuevo.getId())).withSelfRel(),
        linkTo(methodOn(ServicioController.class).listar()).withRel("collection"),
        linkTo(methodOn(ServicioController.class).actualizar(servicioNuevo.getId(), servicioNuevo)).withRel("update"),
        linkTo(methodOn(ServicioController.class).eliminar(servicioNuevo.getId())).withRel("delete"));

    return ResponseEntity
        .created(linkTo(methodOn(ServicioController.class).buscar(servicioNuevo.getId())).toUri())
        .body(entityModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Servicio>> buscar(@PathVariable long id) {
    try {
      Servicio servicio = servicioService.findById(id);

      EntityModel<Servicio> entityModel = EntityModel.of(servicio,
          linkTo(methodOn(ServicioController.class).buscar(id)).withSelfRel(),
          linkTo(methodOn(ServicioController.class).listar()).withRel("collection"),
          linkTo(methodOn(ServicioController.class).actualizar(id, servicio)).withRel("update"),
          linkTo(methodOn(ServicioController.class).eliminar(id)).withRel("delete"));

      if (servicio.getFactura() != null) {
        entityModel.add(linkTo(methodOn(FacturaController.class).buscar(servicio.getFactura().getId()))
            .withRel("factura"));
      }

      return ResponseEntity.ok(entityModel);

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Servicio>> actualizar(@PathVariable Long id,
      @RequestBody Servicio servicioActualizado) {
    try {
      Servicio servicioExistente = servicioService.findById(id);
      servicioExistente.setNombre(servicioActualizado.getNombre());
      servicioExistente.setCosto(servicioActualizado.getCosto());
      servicioExistente.setDescripcion(servicioActualizado.getDescripcion());

      if (servicioActualizado.getFactura() != null && servicioActualizado.getFactura().getId() != null) {
        Factura factura = facturaService.findById(servicioActualizado.getFactura().getId());
        servicioExistente.setFactura(factura);
      } else {
        servicioExistente.setFactura(null);
      }

      Servicio servicioGuardado = servicioService.save(servicioExistente);

      EntityModel<Servicio> entityModel = EntityModel.of(servicioGuardado,
          linkTo(methodOn(ServicioController.class).buscar(id)).withSelfRel(),
          linkTo(methodOn(ServicioController.class).listar()).withRel("collection"),
          linkTo(methodOn(ServicioController.class).actualizar(id, servicioGuardado)).withRel("update"),
          linkTo(methodOn(ServicioController.class).eliminar(id)).withRel("delete"));

      return ResponseEntity.ok(entityModel);

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      servicioService.delete(id);
      return ResponseEntity.noContent()
          .location(linkTo(methodOn(ServicioController.class).listar()).toUri())
          .build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}