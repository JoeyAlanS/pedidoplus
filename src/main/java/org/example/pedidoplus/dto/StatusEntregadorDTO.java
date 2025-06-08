package org.example.pedidoplus.dto;

public class StatusEntregadorDTO {
    private String entregadorId;
    private String nomeEntregador;
    private String statusEntrega;

    public StatusEntregadorDTO() {
    }

    public StatusEntregadorDTO(String nomeEntregador, String statusEntrega) {
        this.entregadorId = entregadorId;
        this.nomeEntregador = nomeEntregador;
        this.statusEntrega = statusEntrega;
    }

    public String getEntregadorId() {
        return entregadorId;
    }

    public void setEntregadorId(String entregadorId) {
        this.entregadorId = entregadorId;
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