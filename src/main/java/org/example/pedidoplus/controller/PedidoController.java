package org.example.pedidoplus.controller;

import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return service.listarTodosPedidos();
    }

    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        return service.criarPedido(pedido);
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable String id) {
        return service.consultarPorId(id).orElse(null);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> pedidosPorCliente(@PathVariable String clienteId) {
        return service.listarPorCliente(clienteId);
    }
}

