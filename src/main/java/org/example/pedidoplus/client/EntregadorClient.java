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

    @SuppressWarnings("unchecked")
    public StatusEntregadorDTO buscarAssignmentPorEntregadorId(String entregadorId) {
        String url = BASE_URL + "api/deliveries/deliverer/" + entregadorId + "/assignments";

        try {
            List<Map<String, Object>> assignments = restTemplate.getForObject(url, List.class);
            if (assignments != null && !assignments.isEmpty()) {
                Map<String, Object> assignment = assignments.get(0);
                String orderId = (String) assignment.get("orderId");
                String status = (String) assignment.get("status");
                String nomeEntregador = null;

                if (entregadorId != null) {
                    String entregadorUrl = BASE_URL + "api/entregadores/" + entregadorId;
                    Map<String, Object> entregadorData = restTemplate.getForObject(entregadorUrl, Map.class);
                    if (entregadorData != null) {
                        nomeEntregador = (String) entregadorData.get("nome");
                    }
                }

                StatusEntregadorDTO dto = new StatusEntregadorDTO();
                dto.setEntregadorId(entregadorId);
                dto.setNomeEntregador(nomeEntregador);
                dto.setStatusEntrega(status);
                return dto;
            }
        } catch (Exception e) {
        }
        return null;
    }
}