package com.hospital_vm.cl.hospital_vm.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {

  @NotBlank(message = "El nombre no puede estar vacío o ser solo espacios")
  @NotNull(message = "El nombre no puede ser nulo")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  private String nombre;

  @NotBlank(message = "La dirección no puede estar vacía o ser solo espacios") // También para dirección
  @NotNull(message = "La dirección no puede ser nula")
  @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
  private String direccion;

  @NotBlank(message = "El telefono no puede estar vacía o ser solo espacios") // También para dirección
  @Size(max = 10, message = "El teléfono no puede tener más de 10 caracteres")
  private String telefono;
}
