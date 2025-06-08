package org.example.pedidoplus.service;

import org.example.pedidoplus.client.ClienteClient;
import org.example.pedidoplus.client.EntregadorClient;
import org.example.pedidoplus.dto.ClienteNomeDTO;
import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.example.pedidoplus.model.Pedido;
import org.example.pedidoplus.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteClient clienteClient;
    private final EntregadorClient entregadorClient;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteClient clienteClient,
            EntregadorClient entregadorClient
    ) {
        this.pedidoRepository = pedidoRepository;
        this.clienteClient = clienteClient;
        this.entregadorClient = entregadorClient;
    }

    public Pedido criarPedido(Pedido pedido) {
        if (pedido.getClienteId() == null || pedido.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("O clienteId deve ser informado ao criar um pedido.");
        }

        // Busca as informações do cliente (preenche nome se possível)
        ClienteNomeDTO clienteDTO = clienteClient.buscarClientePorId(pedido.getClienteId());
        if (clienteDTO != null) {
            pedido.setClienteNome(clienteDTO.getNome());
        }

        // Não atribui entregador nem status de entrega aqui! Aguarda o serviço de atribuição.
        pedido.setEntregadorId(null);
        pedido.setNomeEntregador(null);
        pedido.setStatusEntrega("PENDENTE");

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosDoCliente(String clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Optional<Pedido> consultarDetalhes(String pedidoId) {
        return pedidoRepository.findById(pedidoId);
    }

    public Pedido salvarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.findAll();
    }

    // Agendamento para checar atribuições e atualizar pedidos
    @Scheduled(fixedDelay = 60000) // Executa a cada 1 minuto
    public void atualizarAtribuicoesDeEntregadores() {
        List<Pedido> pendentes = pedidoRepository.findAll();
        for (Pedido pedido : pendentes) {
            if (pedido.getEntregadorId() == null || "PENDENTE".equalsIgnoreCase(pedido.getStatusEntrega())) {
                StatusEntregadorDTO atribuido = entregadorClient.buscarAssignmentPorEntregadorId(pedido.getEntregadorId());
                if (atribuido != null && atribuido.getEntregadorId() != null) {
                    pedido.setEntregadorId(atribuido.getEntregadorId());
                    pedido.setNomeEntregador(atribuido.getNomeEntregador());
                    pedido.setStatusEntrega(atribuido.getStatusEntrega());
                    pedidoRepository.save(pedido);
                }
            }
        }
    }
}