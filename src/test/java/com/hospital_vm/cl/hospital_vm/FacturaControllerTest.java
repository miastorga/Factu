package com.hospital_vm.cl.hospital_vm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_vm.cl.hospital_vm.controller.FacturaController;
import com.hospital_vm.cl.hospital_vm.model.Cliente;
import com.hospital_vm.cl.hospital_vm.model.Factura;
import com.hospital_vm.cl.hospital_vm.model.FacturaDTO;
import com.hospital_vm.cl.hospital_vm.model.Servicio;
import com.hospital_vm.cl.hospital_vm.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaController.class)
public class FacturaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FacturaService facturaService;

  @Autowired
  private ObjectMapper objectMapper;

  private Factura facturaEjemplo;
  private FacturaDTO facturaDtoEjemplo;
  private Cliente clienteEjemplo;
  private List<Servicio> serviciosEjemplo;

  @BeforeEach
  void setUp() {
    clienteEjemplo = new Cliente();
    clienteEjemplo.setId(10L);

    Servicio servicio1 = new Servicio(100L, "Consulta", 5000, "Consulta general", null);
    Servicio servicio2 = new Servicio(101L, "Radiografia", 10000, "Radiografia de torax", null);
    serviciosEjemplo = Arrays.asList(servicio1, servicio2);

    facturaEjemplo = new Factura();
    facturaEjemplo.setId(1L);
    facturaEjemplo.setCliente(clienteEjemplo);
    facturaEjemplo.setServicios(serviciosEjemplo);
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
  void testListar() throws Exception {
    Factura otraFactura = new Factura();
    otraFactura.setId(2L);
    otraFactura.setTotal(5000);
    otraFactura.setServicios(Collections.emptyList());

    List<Factura> facturas = Arrays.asList(facturaEjemplo, otraFactura);

    when(facturaService.findAll()).thenReturn(facturas);

    mockMvc.perform(get("/facturas"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
        .andExpect(jsonPath("$._embedded.facturaList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.facturaList[0].id", is(1)))
        .andExpect(jsonPath("$._embedded.facturaList[0].servicios", hasSize(2)))
        .andExpect(jsonPath("$._embedded.facturaList[0].servicios[0].nombre", is("Consulta")))
        .andExpect(jsonPath("$._embedded.facturaList[1].id", is(2)))
        .andExpect(jsonPath("$._embedded.facturaList[1].servicios", hasSize(0)))
        .andExpect(jsonPath("$._links.self", notNullValue()));

    verify(facturaService, times(1)).findAll();
  }

  @Test
  void testGuardar() throws Exception {
    Factura facturaCreada = new Factura();
    facturaCreada.setId(3L);
    facturaCreada.setCliente(clienteEjemplo);
    facturaCreada.setFechaEmision(facturaDtoEjemplo.getFechaEmision());
    facturaCreada.setTotal(facturaDtoEjemplo.getTotal());
    facturaCreada.setPagada(facturaDtoEjemplo.isPagada());
    facturaCreada.setServicios(serviciosEjemplo);

    when(facturaService.crearFactura(any())).thenReturn(facturaCreada);

    String facturaDtoJson = objectMapper.writeValueAsString(facturaDtoEjemplo);

    mockMvc.perform(post("/facturas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(facturaDtoJson))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.total", is(facturaDtoEjemplo.getTotal())))
        .andExpect(jsonPath("$.servicios", hasSize(2)))
        .andExpect(jsonPath("$.servicios[0].nombre", is("Consulta")))
        .andExpect(jsonPath("$._links.self", notNullValue()));

    verify(facturaService, times(1)).crearFactura(any());
  }

  @Test
  void testActualizar() throws Exception {
    Integer facturaIdInteger = 1;
    Long facturaIdLong = facturaIdInteger.longValue();

    Factura facturaExistente = new Factura();
    facturaExistente.setId(facturaIdLong);
    facturaExistente.setTotal(1000);
    facturaExistente.setServicios(Collections.emptyList());

    Factura facturaActualizadaRequestBody = new Factura();
    facturaActualizadaRequestBody.setTotal(2000);
    facturaActualizadaRequestBody.setPagada(true);
    facturaActualizadaRequestBody.setCliente(clienteEjemplo);
    facturaActualizadaRequestBody.setServicios(serviciosEjemplo);

    when(facturaService.findById(facturaIdLong)).thenReturn(facturaExistente);

    Factura facturaGuardada = new Factura();
    facturaGuardada.setId(facturaIdLong);
    facturaGuardada.setTotal(facturaActualizadaRequestBody.getTotal());
    facturaGuardada.setPagada(facturaActualizadaRequestBody.isPagada());
    facturaGuardada.setCliente(clienteEjemplo);
    facturaGuardada.setServicios(facturaActualizadaRequestBody.getServicios());

    when(facturaService.save(any())).thenReturn(facturaGuardada);

    String facturaJson = objectMapper.writeValueAsString(facturaActualizadaRequestBody);

    mockMvc.perform(put("/facturas/{id}", facturaIdInteger)
        .contentType(MediaType.APPLICATION_JSON)
        .content(facturaJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
        .andExpect(jsonPath("$.id", is(facturaIdInteger)))
        .andExpect(jsonPath("$.total", is(2000)))
        .andExpect(jsonPath("$.pagada", is(true)))
        .andExpect(jsonPath("$.servicios", hasSize(2)))
        .andExpect(jsonPath("$.servicios[0].nombre", is("Consulta")))
        .andExpect(jsonPath("$._links.self", notNullValue()));

    verify(facturaService, times(1)).findById(facturaIdLong);
    verify(facturaService, times(1)).save(any());
  }

  @Test
  void testEliminar() throws Exception {
    Long facturaIdToDelete = 1L;

    mockMvc.perform(delete("/facturas/{id}", facturaIdToDelete))
        .andExpect(status().isNoContent());

    verify(facturaService, times(1)).delete(facturaIdToDelete);
  }
}