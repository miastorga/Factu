package com.hospital_vm.cl.hospital_vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital_vm.cl.hospital_vm.model.Cliente;
import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.FacturaDTO;
import com.hospital_vm.cl.hospital_vm.repository.ClienteRepository;
import com.hospital_vm.cl.hospital_vm.repository.FacturaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FacturaService {

 @Autowired
    private FacturaRepository facturaRepository; 

 @Autowired
   private  ClienteRepository clienteRepository;

  public Factura crearFactura(FacturaDTO facturaDTO) {
      Cliente cliente = clienteRepository.findById(facturaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + facturaDTO.getClienteId()));

      Factura factura = new Factura();
      factura.setCliente(cliente);
      factura.setFechaEmision(facturaDTO.getFechaEmision());
      factura.setTotal(facturaDTO.getTotal());
      factura.setPagada(facturaDTO.isPagada());

      return facturaRepository.save(factura);
    }

    public List<Factura> findAll() {
      return facturaRepository.findAll();
    }

    public Factura findById(long id) {
      return facturaRepository.findById(id).get();
    }

    public Factura save(Factura cliente) {
      return facturaRepository.save(cliente);
    }

    public void delete(Long id) {
      facturaRepository.deleteById(id);
    }
}
