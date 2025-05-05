package com.hospital_vm.cl.hospital_vm.controller;

import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.FacturaDTO;
import com.hospital_vm.cl.hospital_vm.service.FacturaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

  @Autowired
  private FacturaService facturaService;

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Factura>>> listar() {
    List<Factura> facturas = facturaService.findAll();

    if (facturas.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<EntityModel<Factura>> facturasModel = facturas.stream()
        .map(factura -> {
          Link selfLink = linkTo(methodOn(FacturaController.class).buscar(factura.getId())).withSelfRel();
          Link collectionLink = linkTo(methodOn(FacturaController.class).listar()).withRel("collection");
          Link updateLink = linkTo(methodOn(FacturaController.class).actualizar(factura.getId().intValue(), null))
              .withRel("update");
          Link deleteLink = linkTo(methodOn(FacturaController.class).eliminar(factura.getId())).withRel("delete");

          return EntityModel.of(factura, selfLink, collectionLink, updateLink, deleteLink);
        })
        .collect(Collectors.toList());

    Link selfCollectionLink = linkTo(methodOn(FacturaController.class).listar()).withSelfRel();
    CollectionModel<EntityModel<Factura>> collectionModel = CollectionModel.of(facturasModel, selfCollectionLink);

    return ResponseEntity.ok(collectionModel);
  }

  @PostMapping
  public ResponseEntity<EntityModel<Factura>> guardar(@RequestBody FacturaDTO facturaDTO) {
    try {
      Factura facturaNuevo = facturaService.crearFactura(facturaDTO);

      Link selfLink = linkTo(methodOn(FacturaController.class).buscar(facturaNuevo.getId())).withSelfRel();
      Link collectionLink = linkTo(methodOn(FacturaController.class).listar()).withRel("collection");
      Link updateLink = linkTo(methodOn(FacturaController.class).actualizar(facturaNuevo.getId().intValue(), null))
          .withRel("update");
      Link deleteLink = linkTo(methodOn(FacturaController.class).eliminar(facturaNuevo.getId())).withRel("delete");

      EntityModel<Factura> facturaModel = EntityModel.of(facturaNuevo, selfLink, collectionLink, updateLink,
          deleteLink);

      return ResponseEntity.created(selfLink.toUri()).body(facturaModel);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Factura>> buscar(@PathVariable long id) {
    try {
      Factura factura = facturaService.findById(id);

      if (factura == null) {
        return ResponseEntity.notFound().build();
      }

      Link selfLink = linkTo(methodOn(FacturaController.class).buscar(id)).withSelfRel();
      Link collectionLink = linkTo(methodOn(FacturaController.class).listar()).withRel("collection");
      Link updateLink = linkTo(methodOn(FacturaController.class).actualizar(factura.getId().intValue(), null))
          .withRel("update");
      Link deleteLink = linkTo(methodOn(FacturaController.class).eliminar(id)).withRel("delete");

      EntityModel<Factura> facturaModel = EntityModel.of(factura, selfLink, collectionLink, updateLink, deleteLink);

      return ResponseEntity.ok(facturaModel);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Factura>> actualizar(@PathVariable Integer id,
      @RequestBody Factura facturaActualizada) {
    try {
      Factura facturaExistente = facturaService.findById(id.longValue());

      if (facturaExistente == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      facturaExistente.setFechaEmision(facturaActualizada.getFechaEmision());
      facturaExistente.setPagada(facturaActualizada.isPagada());
      facturaExistente.setTotal(facturaActualizada.getTotal());
      facturaExistente.setServicios(facturaActualizada.getServicios());

      Factura facturaActualizadaEnBD = facturaService.save(facturaExistente);

      Link selfLink = linkTo(methodOn(FacturaController.class).buscar(id.longValue())).withSelfRel();

      Link collectionLink = linkTo(methodOn(FacturaController.class).listar()).withRel("collection");
      Link deleteLink = linkTo(methodOn(FacturaController.class).eliminar(id.longValue())).withRel("delete");

      EntityModel<Factura> facturaModel = EntityModel.of(facturaActualizadaEnBD, selfLink, collectionLink, deleteLink);

      return new ResponseEntity<>(facturaModel, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      facturaService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}