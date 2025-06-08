package org.example.pedidoplus.controller;

import org.example.pedidoplus.client.ClienteClient;
import org.example.pedidoplus.client.EntregadorClient;
import org.example.pedidoplus.client.RestauranteClient;
import org.example.pedidoplus.dto.ClienteNomeDTO;
import org.example.pedidoplus.dto.ItemCardapioDTO;
import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final RestauranteClient restauranteClient;
    private final ClienteClient clienteClient;
    private final EntregadorClient entregadorClient;


    public PedidoController(PedidoService pedidoService, RestauranteClient restauranteClient, ClienteClient clienteClient, EntregadorClient entregadorClient) {
        this.pedidoService = pedidoService;
        this.restauranteClient = restauranteClient;
        this.clienteClient = clienteClient;
        this.entregadorClient = entregadorClient;

    }

    // Criar novo pedido
    @PostMapping("/criar-pedidos")
    public Pedido criarPedido(@RequestBody Pedido pedido) {
        return pedidoService.criarPedido(pedido);
    }

    // Listar todos pedidos de um cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPedidosPorCliente(@PathVariable String clienteId) {
        return pedidoService.listarPedidosDoCliente(clienteId);
    }

    // Detalhes de um pedido
    @GetMapping("/{pedidoId}")
    public Pedido detalhesPedido(@PathVariable String pedidoId) {
        return pedidoService.consultarDetalhes(pedidoId).orElse(null);
    }

    // Valor total do pedido
    @GetMapping("/{pedidoId}/valor-total")
    public Double valorTotalPedido(@PathVariable String pedidoId) {
        return pedidoService.consultarDetalhes(pedidoId)
                .map(Pedido::getValorTotal)
                .orElse(0.0);
    }

    @GetMapping({"", "/todos"})
    public List<Pedido> listarTodosPedidos() {
        return pedidoService.listarTodosPedidos();
    }


    // Listagem dos itens do cardápio via restauranteClient
    @GetMapping("/itensCardapio")
    public List<ItemCardapioDTO> listarItensDoCardapio() {
        return restauranteClient.listarItensCardapio();
    }

    // Buscar apenas o nome do cliente pelo ID
    @GetMapping("/cliente/{clienteId}/nome")
    public ClienteNomeDTO buscarNomeCliente(@PathVariable String clienteId) {
        return clienteClient.buscarClientePorId(clienteId);
    }

    @PutMapping("/{pedidoId}/entregador/{entregadorId}")
    public ResponseEntity<Pedido> associarEntregadorAoPedido(
            @PathVariable String pedidoId,
            @PathVariable String entregadorId) {
        Optional<Pedido> optional = pedidoService.consultarDetalhes(pedidoId);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();
        Pedido pedido = optional.get();
        pedido.setEntregadorId(entregadorId);
        pedidoService.salvarPedido(pedido);
        return ResponseEntity.ok(pedido);
    }

    // Buscar status e nome do entregador vinculado ao pedido via outro microserviço (Railway)
    @GetMapping("/{pedidoId}/entregador-info")
    public ResponseEntity<?> buscarEntregadorInfo(@PathVariable String pedidoId) {
        Optional<Pedido> optPedido = pedidoService.consultarDetalhes(pedidoId);

        if (optPedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pedido não encontrado.");
        }
        Pedido pedido = optPedido.get();

        if (pedido.getEntregadorId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pedido ainda não possui entregador atribuído.");
        }

        // Correção: passar entregadorId e pedidoId
        StatusEntregadorDTO dto = pedidoService.buscarStatusEntregador(
                pedido.getEntregadorId(), pedido.getId()
        );
        return ResponseEntity.ok(dto);
    }




}

