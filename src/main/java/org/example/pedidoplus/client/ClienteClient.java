package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.ClienteNomeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClienteClient {

    private static final String BASE_URL = "https://microservicocliente-production.up.railway.app/api/clientes";

    @Autowired
    private RestTemplate restTemplate;

    public ClienteNomeDTO buscarClientePorId(String clienteId) {
        try {
            String url = BASE_URL + "/" + clienteId;
            System.out.println("Buscando cliente na URL: " + url);
            ClienteNomeDTO cliente = restTemplate.getForObject(url, ClienteNomeDTO.class);
            System.out.println("Retorno client: " + (cliente != null ? cliente.getNome() : "null"));
            return cliente != null ? cliente : new ClienteNomeDTO(clienteId, clienteId);
        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
            return new ClienteNomeDTO(clienteId, clienteId);
        }
    }
}
