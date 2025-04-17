package com.hospital_vm.cl.hospital_vm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {

  @NotNull(message = "El nombre no puede ser nulo")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  private String nombre;

  @NotNull(message = "La dirección no puede ser nula")
  @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
  private String direccion;

  @Size(max = 10, message = "El teléfono no puede tener más de 10 caracteres")
  private String telefono;
}
