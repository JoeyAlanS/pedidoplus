package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EntregadorClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "https://reasonable-happiness-production.up.railway.app/";

    // GET /api/entregadores/{id}
    public StatusEntregadorDTO buscarEntregadorPorId(String entregadorId) {
        try {
            String url = BASE_URL + "/" + entregadorId;
            System.out.println("Buscando entregador na URL: " + url);
            StatusEntregadorDTO entregador = restTemplate.getForObject(url, StatusEntregadorDTO.class);
            System.out.println("Retorno EntregadorClient: " + (entregador != null ? entregador.getStatusEntrega() : "null"));
            return entregador != null ? entregador : new StatusEntregadorDTO();
        } catch (Exception e) {
            System.out.println("Erro ao buscar entregador: " + e.getMessage());
            return new StatusEntregadorDTO();
        }
    }

    // PUT /api/deliveries/{entregaId}/status
    public void atualizarStatusEntrega(String entregaId, String novoStatus) {
        String url = "https://entregador-production-7756.up.railway.app/api/deliveries/" + entregaId + "/status";
        System.out.println("Atualizando status da entrega na URL: " + url + " para: " + novoStatus);
        restTemplate.put(url, novoStatus);
    }
}