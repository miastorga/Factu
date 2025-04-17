package com.hospital_vm.cl.hospital_vm.model; // Asegúrate de usar el paquete correcto

import lombok.Data;

@Data
public class ServicioDTO {
    private String nombre;
    private int costo;
    private String descripcion;
    private Long facturaId; // ID de la factura a la que se asociará el servicio
}