package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/servicios")
public class ServicioController {

  @Autowired
  private ServicioService servicioService;
  @Autowired
  private FacturaService facturaService;


  @GetMapping
  public ResponseEntity<List<Servicio>> listar() {

    List<Servicio> servicios = servicioService.findAll();
    if (servicios.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(servicios);
  }

 @PostMapping
  public ResponseEntity<Servicio> guardar(@RequestBody ServicioDTO servicioDTO) {
      Servicio servicioNuevo = servicioService.crearServicio(servicioDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(servicioNuevo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Servicio>> buscar(@PathVariable long id) {
      try {
          Optional<Servicio> servicio = Optional.ofNullable(servicioService.findById(id));
          return ResponseEntity.ok(servicio);
      } catch ( Exception e ) {
          return  ResponseEntity.notFound().build();
      }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicioActualizado) {
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
        return ResponseEntity.ok(servicioGuardado);
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
      try {
          servicioService.delete(id);
          return ResponseEntity.noContent().build();
      } catch ( Exception e ) {
          return  ResponseEntity.notFound().build();
      }
  } 
}
