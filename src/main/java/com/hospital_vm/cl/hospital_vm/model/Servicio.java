
package com.hospital_vm.cl.hospital_vm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SERVICIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  @NotNull
  private String nombre;
  @Column(nullable = false, length = 6)
  @NotNull
  private int costo;
  @Column(nullable = false, length = 100)
  @NotNull
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "factura_id")
  @JsonIgnore
  private Factura factura;
}