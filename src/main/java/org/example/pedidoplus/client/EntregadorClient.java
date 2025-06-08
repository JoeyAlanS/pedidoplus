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

    // Agora recebe o pedidoId
    public StatusEntregadorDTO buscarStatusEntregaPorEntregador(String entregadorId, String pedidoId) {
        try {
            String assignmentsUrl = BASE_URL + "api/deliveries/deliverer/" + entregadorId + "/assignments";
            List<Map<String, Object>> entregas = restTemplate.getForObject(assignmentsUrl, List.class);

            String entregadorUrl = BASE_URL + "api/entregadores/" + entregadorId;
            Map<String, Object> entregador = restTemplate.getForObject(entregadorUrl, Map.class);

            String statusEntrega = null;
            if (entregas != null && pedidoId != null) {
                for (Map<String, Object> entrega : entregas) {
                    // Certifique-se que o campo é esse mesmo na resposta do endpoint!
                    String orderId = entrega.get("orderId") != null ? entrega.get("orderId").toString() : null;
                    if (pedidoId.equals(orderId)) {
                        statusEntrega = (String) entrega.get("status");
                        break;
                    }
                }
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