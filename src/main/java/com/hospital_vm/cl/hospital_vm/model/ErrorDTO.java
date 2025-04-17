package com.hospital_vm.cl.hospital_vm.model;
public class ErrorDTO {
    private String campo;
    private String mensaje;

    public ErrorDTO(String campo, String mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }
}
