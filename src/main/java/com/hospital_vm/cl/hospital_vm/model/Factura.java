package com.hospital_vm.cl.hospital_vm.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Factura {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "cliente_id")
  @JsonIgnore
  private Cliente cliente;

  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
  private List<Servicio> servicios;

  @NotNull
  private LocalDate fechaEmision;

  @NotNull
  private int total;

  @NotNull
  private boolean pagada;
}
