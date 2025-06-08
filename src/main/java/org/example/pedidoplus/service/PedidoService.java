package org.example.pedidoplus.service;

import org.example.pedidoplus.client.ClienteClient;
import org.example.pedidoplus.client.EntregadorClient;
import org.example.pedidoplus.dto.ClienteNomeDTO;
import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteClient clienteClient;
    private final EntregadorClient entregadorClient;


    public PedidoService(PedidoRepository repository, ClienteClient clienteClient, EntregadorClient entregadorClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
        this.entregadorClient = entregadorClient;

    }

    public Pedido criarPedido(Pedido pedido) {
        double total = pedido.getItens().stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(total);

        ClienteNomeDTO cliente = clienteClient.buscarClientePorId(pedido.getClienteId());
        if (cliente != null) {
            pedido.setClienteNome(cliente.getNome());
        } else {
            pedido.setClienteNome("Desconhecido");
        }

        String status = "PENDENTE";
        if (pedido.getEntregadorId() != null) {
            // Garante que tenha um ID do pedido (se estiver usando geração automática, salve primeiro para gerar o ID)
            if (pedido.getId() == null) {
                // Salva para gerar o ID antes de buscar o status (caso use MongoDB, _id pode ser gerado só depois)
                pedido = repository.save(pedido);
            }
            StatusEntregadorDTO entregadorInfo = entregadorClient.buscarStatusEntregaPorEntregador(pedido.getEntregadorId(), pedido.getId());
            if (entregadorInfo != null && entregadorInfo.getStatusEntrega() != null) {
                status = entregadorInfo.getStatusEntrega();
            }
        }
        pedido.setStatus(status);

        return repository.save(pedido);
    }


    public List<Pedido> listarPedidosDoCliente(String clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Optional<Pedido> consultarDetalhes(String pedidoId) {
        return repository.findById(pedidoId);
    }

    public Pedido salvarPedido(Pedido pedido) {
        return repository.save(pedido);
    }

    public List<Pedido> listarTodosPedidos() {
        return repository.findAll();
    }

    public StatusEntregadorDTO buscarStatusEntregador(String entregadorId, String pedidoId) {
        if (entregadorId == null) {
            StatusEntregadorDTO dto = new StatusEntregadorDTO();
            dto.setEntregadorId(null);
            dto.setNomeEntregador(null);
            dto.setStatusEntrega("PENDENTE (sem entregador atribuído)");
            return dto;
        }
        return entregadorClient.buscarStatusEntregaPorEntregador(entregadorId, pedidoId);
    }



}
