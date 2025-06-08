package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.StatusEntregadorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
public class EntregadorClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "https://reasonable-happiness-production.up.railway.app/";

    // NOVO: Busca as atribuições do entregador e o nome dele
    public StatusEntregadorDTO buscarStatusEntregaPorEntregador(String entregadorId) {
        try {
            // Busca as atribuições (entregas) do entregador
            String assignmentsUrl = BASE_URL + "api/deliveries/deliverer/" + entregadorId + "/assignments";
            List<Map<String, Object>> entregas = restTemplate.getForObject(assignmentsUrl, List.class);

            // Busca os dados do entregador para pegar o nome
            String entregadorUrl = BASE_URL + "api/entregadores/" + entregadorId;
            Map<String, Object> entregador = restTemplate.getForObject(entregadorUrl, Map.class);

            String statusEntrega = null;
            if (entregas != null && !entregas.isEmpty()) {
                // Supondo que cada entrega tem o campo "status"
                statusEntrega = (String) entregas.get(0).get("status");
            }

            String nomeEntregador = entregador != null ? (String) entregador.get("nome") : null;

            StatusEntregadorDTO dto = new StatusEntregadorDTO();
            dto.setEntregadorId(entregadorId);
            dto.setNomeEntregador(nomeEntregador);
            dto.setStatusEntrega(statusEntrega);

            return dto;

        } catch (Exception e) {
            System.out.println("Erro ao buscar status do entregador: " + e.getMessage());
            return new StatusEntregadorDTO();
        }
    }
}