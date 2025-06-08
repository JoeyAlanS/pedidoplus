package org.example.pedidoplus.controller;

import org.example.pedidoplus.client.ClienteClient;
import org.example.pedidoplus.client.EntregadorClient;
import org.example.pedidoplus.client.RestauranteClient;
import org.example.pedidoplus.dto.ClienteNomeDTO;
import org.example.pedidoplus.dto.ItemCardapioDTO;
import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{pedidoId}/entregador")
    public ResponseEntity<StatusEntregadorDTO> buscarEntregadorEPedidoStatus(@PathVariable String pedidoId) {
        Pedido pedido = pedidoService.consultarDetalhes(pedidoId)
                .orElse(null);
        if (pedido == null || pedido.getEntregadorId() == null) {
            return ResponseEntity.notFound().build();
        }
        StatusEntregadorDTO statusDTO = entregadorClient.buscarAssignmentPorEntregadorId(pedido.getEntregadorId());
        return ResponseEntity.ok(statusDTO);
    }

    @PutMapping("/{pedidoId}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable String pedidoId, @RequestBody Pedido pedidoAtualizado) {
        return pedidoService.consultarDetalhes(pedidoId)
                .map(pedidoExistente -> {
                    pedidoExistente.setClienteId(pedidoAtualizado.getClienteId());
                    pedidoExistente.setClienteNome(pedidoAtualizado.getClienteNome());
                    pedidoExistente.setItens(pedidoAtualizado.getItens());
                    // Recalcule o valor total baseado nos itens atualizados
                    double novoValorTotal = 0.0;
                    if (pedidoAtualizado.getItens() != null) {
                        novoValorTotal = pedidoAtualizado.getItens().stream()
                                .mapToDouble(item -> {
                                    Double valorUni = item.getPrecoUnitario() != null ? item.getPrecoUnitario() : 0.0;
                                    Integer qtd = item.getQuantidade() != null ? item.getQuantidade() : 0;
                                    return valorUni * qtd;
                                }).sum();
                    }
                    pedidoExistente.setValorTotal(novoValorTotal); // Aqui NÃO usa o do request!

                    pedidoExistente.setStatus(pedidoAtualizado.getStatus());
                    pedidoExistente.setEntregadorId(pedidoAtualizado.getEntregadorId());
                    pedidoExistente.setNomeEntregador(pedidoAtualizado.getNomeEntregador());
                    pedidoExistente.setStatusEntrega(pedidoAtualizado.getStatusEntrega());
                    Pedido salvo = pedidoService.salvarPedido(pedidoExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurantes")
    public List<org.example.pedidoplus.dto.RestauranteResumoDTO> listarRestaurantes() {
        return restauranteClient.listarRestaurantes();
    }

    // Endpoint para listar o cardápio de um restaurante específico
    @GetMapping("/restaurantes/{id}/cardapio")
    public List<ItemCardapioDTO> listarCardapioRestaurante(@PathVariable String id) {
        return restauranteClient.listarItensCardapioPorRestaurante(id);
    }




}

