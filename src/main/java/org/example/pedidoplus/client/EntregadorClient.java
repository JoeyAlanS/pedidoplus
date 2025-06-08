package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EntregadorClient {
    @Autowired
    private RestTemplate restTemplate;

    // Railway URL (ajuste se o projeto/endpoint for diferente)
    private final String BASE_URL = "https://reasonable-happiness-production.up.railway.app/";

    // GET /api/entregadores/{id}
    public StatusEntregadorDTO buscarEntregadorPorId(String entregadorId) {
        String url = BASE_URL + "/" + entregadorId;
        return restTemplate.getForObject(url, StatusEntregadorDTO.class);
    }

    // PUT /api/deliveries/{entregaId}/status
    public void atualizarStatusEntrega(String entregaId, String novoStatus) {
        String url = "https://entregador-production-7756.up.railway.app/api/deliveries/" + entregaId + "/status";
        restTemplate.put(url, novoStatus);
    }
}