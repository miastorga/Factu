package com.hospital_vm.cl.hospital_vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.Servicio;
import com.hospital_vm.cl.hospital_vm.model.ServicioDTO;
import com.hospital_vm.cl.hospital_vm.repository.FacturaRepository;
import com.hospital_vm.cl.hospital_vm.repository.ServicioRepository;

@Service
public class ServicioService {
   @Autowired
    private ServicioRepository servicioRepository; 
    @Autowired
      private FacturaRepository facturaRepository; 

    public Servicio crearServicio(ServicioDTO servicioDTO) {
        Factura factura = facturaRepository.findById(servicioDTO.getFacturaId())
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + servicioDTO.getFacturaId()));

        Servicio servicio = new Servicio();
        servicio.setNombre(servicioDTO.getNombre());
        servicio.setCosto(servicioDTO.getCosto());
        servicio.setDescripcion(servicioDTO.getDescripcion());
        servicio.setFactura(factura);

        return servicioRepository.save(servicio);
    }
    public List<Servicio> findAll() {
      return servicioRepository.findAll();
    }

    public Servicio findById(long id) {
      return servicioRepository.findById(id).get();
    }

    public Servicio save(Servicio servicio) {
      return servicioRepository.save(servicio);
    }

    public void delete(Long id) {
      servicioRepository.deleteById(id);
    }
}
