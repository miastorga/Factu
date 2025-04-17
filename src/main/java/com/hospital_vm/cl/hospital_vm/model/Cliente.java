package com.hospital_vm.cl.hospital_vm.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CLIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  @NotNull
  private String nombre;
  @Column(nullable = false, length = 100)
  @NotNull
  private String direccion;
  @Column(nullable = false, length = 10)
  @NotNull
  private String telefono;
  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Factura> facturas;

}
