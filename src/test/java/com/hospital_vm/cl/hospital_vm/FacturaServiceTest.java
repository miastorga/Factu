package com.hospital_vm.cl.hospital_vm;

import com.hospital_vm.cl.hospital_vm.model.Cliente;
import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.FacturaDTO; // Asumiendo que esta clase existe
import com.hospital_vm.cl.hospital_vm.repository.ClienteRepository;
import com.hospital_vm.cl.hospital_vm.repository.FacturaRepository;
import com.hospital_vm.cl.hospital_vm.service.FacturaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTest {

  @Mock
  private FacturaRepository facturaRepository;

  @Mock
  private ClienteRepository clienteRepository;

  @InjectMocks
  private FacturaService facturaService;

  private Factura facturaEjemplo;
  private FacturaDTO facturaDtoEjemplo;
  private Cliente clienteEjemplo;

  @BeforeEach
  void setUp() {
    clienteEjemplo = new Cliente();
    clienteEjemplo.setId(10L);

    facturaEjemplo = new Factura();
    facturaEjemplo.setId(1L);
    facturaEjemplo.setCliente(clienteEjemplo);
    facturaEjemplo.setFechaEmision(LocalDate.of(2024, 11, 15));
    facturaEjemplo.setTotal(15000);
    facturaEjemplo.setPagada(false);

    facturaDtoEjemplo = new FacturaDTO();
    facturaDtoEjemplo.setClienteId(10L);
    facturaDtoEjemplo.setFechaEmision(LocalDate.of(2024, 12, 1));
    facturaDtoEjemplo.setTotal(25000);
    facturaDtoEjemplo.setPagada(true);
  }

  @Test
  void testCrearFacturaSuccess() {
    when(clienteRepository.findById(facturaDtoEjemplo.getClienteId())).thenReturn(Optional.of(clienteEjemplo));
    Factura facturaGuardada = new Factura();
    facturaGuardada.setId(2L);
    facturaGuardada.setCliente(clienteEjemplo);
    facturaGuardada.setFechaEmision(facturaDtoEjemplo.getFechaEmision());
    facturaGuardada.setTotal(facturaDtoEjemplo.getTotal());
    facturaGuardada.setPagada(facturaDtoEjemplo.isPagada());

    when(facturaRepository.save(any())).thenReturn(facturaGuardada);

    Factura createdFactura = facturaService.crearFactura(facturaDtoEjemplo);

    assertNotNull(createdFactura);
    assertEquals(2L, createdFactura.getId());

    verify(clienteRepository, times(1)).findById(facturaDtoEjemplo.getClienteId());
    verify(facturaRepository, times(1)).save(any());
  }

  @Test
  void testFindAll() {
    List<Factura> facturas = Arrays.asList(facturaEjemplo, new Factura());
    when(facturaRepository.findAll()).thenReturn(facturas);

    List<Factura> result = facturaService.findAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(facturaRepository, times(1)).findAll();
  }

  @Test
  void testFindByIdSuccess() {
    Long facturaId = 1L;
    when(facturaRepository.findById(facturaId)).thenReturn(Optional.of(facturaEjemplo));

    Factura foundFactura = facturaService.findById(facturaId);

    assertNotNull(foundFactura);
    assertEquals(facturaId, foundFactura.getId());
    verify(facturaRepository, times(1)).findById(facturaId);
  }

  @Test
  void testSave() {
    Factura facturaToSave = new Factura();
    facturaToSave.setTotal(500);

    Factura savedFactura = new Factura();
    savedFactura.setId(5L);
    savedFactura.setTotal(facturaToSave.getTotal());

    when(facturaRepository.save(any())).thenReturn(savedFactura);

    Factura result = facturaService.save(facturaToSave);

    assertNotNull(result);
    assertEquals(5L, result.getId());
    verify(facturaRepository, times(1)).save(facturaToSave);
  }

  @Test
  void testDelete() {
    Long facturaIdToDelete = 1L;

    facturaService.delete(facturaIdToDelete);

    verify(facturaRepository, times(1)).deleteById(facturaIdToDelete);
  }
}