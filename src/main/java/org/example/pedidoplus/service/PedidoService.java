package org.example.pedidoplus.service;

import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listarTodosPedidos() {
        return repository.findAll();
    }

    public Pedido criarPedido(Pedido pedido) {
        return repository.save(pedido);
    }

    public Optional<Pedido> consultarPorId(String id) {
        return repository.findById(id);
    }

    public List<Pedido> listarPorCliente(String clienteId) {
        return repository.findByClienteId(clienteId);
    }

}
