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
import com.hospital_vm.cl.hospital_vm.model.FacturaDTO;
import com.hospital_vm.cl.hospital_vm.service.FacturaService;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
 
  @Autowired
  private FacturaService facturaService;

  @GetMapping
  public ResponseEntity<List<Factura>> listar() {

    List<Factura> facturas = facturaService.findAll();
    if (facturas.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(facturas);
  }

  @PostMapping
  public ResponseEntity<Factura> guardar(@RequestBody FacturaDTO facturaDTO) {
      Factura FacturaNuevo = facturaService.crearFactura(facturaDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(FacturaNuevo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Factura>> buscar(@PathVariable long id) {
      try {
          Optional<Factura> factura = Optional.ofNullable(facturaService.findById(id));
          return ResponseEntity.ok(factura);
      } catch ( Exception e ) {
          return  ResponseEntity.notFound().build();
      }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Factura> actualizar(@PathVariable Integer id, @RequestBody Factura facturaActualizada) {
      try {
          Factura facturaExistente = facturaService.findById(id);
          facturaExistente.setFechaEmision(facturaActualizada.getFechaEmision());
            facturaExistente.setPagada(facturaActualizada.isPagada());
            facturaExistente.setTotal(facturaActualizada.getTotal());
            facturaExistente.setServicios(facturaActualizada.getServicios());

          Factura servicioGuardado = facturaService.save(facturaExistente);
      return ResponseEntity.ok(servicioGuardado);
      } catch ( Exception e ) {
          return  ResponseEntity.notFound().build();
      }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
      try {
          facturaService.delete(id);
          return ResponseEntity.noContent().build();
      } catch ( Exception e ) {
          return  ResponseEntity.notFound().build();
      }
  }  
}
