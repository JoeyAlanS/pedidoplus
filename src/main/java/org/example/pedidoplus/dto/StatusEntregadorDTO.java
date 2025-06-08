package org.example.pedidoplus.dto;

public class StatusEntregadorDTO {
    private String nomeEntregador;
    private String statusEntrega;

    public StatusEntregadorDTO() {
    }

    public StatusEntregadorDTO(String nomeEntregador, String statusEntrega) {
        this.nomeEntregador = nomeEntregador;
        this.statusEntrega = statusEntrega;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }
}