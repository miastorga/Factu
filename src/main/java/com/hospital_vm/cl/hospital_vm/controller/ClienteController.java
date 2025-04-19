package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/clientes")
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @GetMapping
  public ResponseEntity<List<Cliente>> listar() {

    List<Cliente> clientes = clienteService.findAll();
    if (clientes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(clientes);
  }

  @PostMapping
  public ResponseEntity<Cliente> guardar(@Valid @RequestBody ClienteDTO clienteDto) {
    System.out.println("ClienteDTO recibido: " + clienteDto);
    var cliente = new Cliente();
    cliente.setNombre(clienteDto.getNombre());
    cliente.setDireccion(clienteDto.getDireccion());
    cliente.setTelefono(clienteDto.getTelefono());
    Cliente clienteNuevo = clienteService.save(cliente);
    return ResponseEntity.status(HttpStatus.CREATED).body(clienteNuevo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Cliente>> buscar(@PathVariable long id) {
    try {
      Optional<Cliente> cliente = Optional.ofNullable(clienteService.findById(id));
      return ResponseEntity.ok(cliente);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
    Cliente clienteEncontrado = clienteService.findById(id);
    if (clienteEncontrado == null) {
      return ResponseEntity.notFound().build();
    }

    clienteEncontrado.setNombre(clienteDTO.getNombre());
    clienteEncontrado.setDireccion(clienteDTO.getDireccion());
    clienteEncontrado.setTelefono(clienteDTO.getTelefono());

    Cliente clienteGuardado = clienteService.save(clienteEncontrado);

    return ResponseEntity.ok(clienteGuardado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      clienteService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
