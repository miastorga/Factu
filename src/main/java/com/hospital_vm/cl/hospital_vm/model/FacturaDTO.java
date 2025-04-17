package com.hospital_vm.cl.hospital_vm.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FacturaDTO {
   private Long clienteId;
    private LocalDate fechaEmision;
    private int total;
    private boolean pagada;
}
