package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital_vm.cl.hospital_vm.model.Cliente;
import com.hospital_vm.cl.hospital_vm.model.ClienteDTO;
import com.hospital_vm.cl.hospital_vm.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listar() {
    List<Cliente> clientes = clienteService.findAll();

    if (clientes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<EntityModel<Cliente>> clientesModel = clientes.stream()
        .map(cliente -> EntityModel.of(cliente,
            linkTo(methodOn(ClienteController.class).buscar(cliente.getId())).withSelfRel(),
            linkTo(methodOn(ClienteController.class).listar()).withRel("collection"),
            linkTo(methodOn(ClienteController.class).actualizar(cliente.getId(), new ClienteDTO())).withRel("update"),
            linkTo(methodOn(ClienteController.class).eliminar(cliente.getId())).withRel("delete")))
        .collect(Collectors.toList());

    CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(
        clientesModel,
        linkTo(methodOn(ClienteController.class).listar()).withSelfRel());

    return ResponseEntity.ok(collectionModel);
  }

  @PostMapping
  public ResponseEntity<EntityModel<Cliente>> guardar(@Valid @RequestBody ClienteDTO clienteDto) {
    System.out.println("ClienteDTO recibido: " + clienteDto);
    var cliente = new Cliente();
    cliente.setNombre(clienteDto.getNombre());
    cliente.setDireccion(clienteDto.getDireccion());
    cliente.setTelefono(clienteDto.getTelefono());

    Cliente clienteNuevo = clienteService.save(cliente);

    EntityModel<Cliente> entityModel = EntityModel.of(clienteNuevo,
        linkTo(methodOn(ClienteController.class).buscar(clienteNuevo.getId())).withSelfRel(),
        linkTo(methodOn(ClienteController.class).listar()).withRel("collection"),
        linkTo(methodOn(ClienteController.class).actualizar(clienteNuevo.getId(), new ClienteDTO())).withRel("update"),
        linkTo(methodOn(ClienteController.class).eliminar(clienteNuevo.getId())).withRel("delete"));

    return ResponseEntity
        .created(linkTo(methodOn(ClienteController.class).buscar(clienteNuevo.getId())).toUri())
        .body(entityModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Cliente>> buscar(@PathVariable long id) {
    try {
      Cliente cliente = clienteService.findById(id);

      EntityModel<Cliente> entityModel = EntityModel.of(cliente,
          linkTo(methodOn(ClienteController.class).buscar(id)).withSelfRel(),
          linkTo(methodOn(ClienteController.class).listar()).withRel("collection"),
          linkTo(methodOn(ClienteController.class).actualizar(id, new ClienteDTO())).withRel("update"),
          linkTo(methodOn(ClienteController.class).eliminar(id)).withRel("delete"));

      return ResponseEntity.ok(entityModel);

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Cliente>> actualizar(@PathVariable Long id,
      @Valid @RequestBody ClienteDTO clienteDTO) {
    Cliente clienteEncontrado = clienteService.findById(id);

    if (clienteEncontrado == null) {
      return ResponseEntity.notFound().build();
    }

    clienteEncontrado.setNombre(clienteDTO.getNombre());
    clienteEncontrado.setDireccion(clienteDTO.getDireccion());
    clienteEncontrado.setTelefono(clienteDTO.getTelefono());

    Cliente clienteGuardado = clienteService.save(clienteEncontrado);

    EntityModel<Cliente> entityModel = EntityModel.of(clienteGuardado,
        linkTo(methodOn(ClienteController.class).buscar(id)).withSelfRel(),
        linkTo(methodOn(ClienteController.class).listar()).withRel("collection"),
        linkTo(methodOn(ClienteController.class).actualizar(id, new ClienteDTO())).withRel("update"),
        linkTo(methodOn(ClienteController.class).eliminar(id)).withRel("delete"));

    return ResponseEntity.ok(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      clienteService.delete(id);
      return ResponseEntity.noContent()
          .location(linkTo(methodOn(ClienteController.class).listar()).toUri())
          .build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}