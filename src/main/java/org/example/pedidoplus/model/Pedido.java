package org.example.pedidoplus.model;

import org.example.pedidoplus.model.ItemPedido;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Pedido")
public class Pedido {
    @Id
    private String id;
    private String clienteId;
    private String clienteNome;
    private List<ItemPedido> itens;
    private Double valorTotal;
    private String status;
    private String entregadorId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getEntregadorId() {
        return entregadorId;
    }

    public void setEntregadorId(String entregadorId) {
        this.entregadorId = entregadorId;
    }


}