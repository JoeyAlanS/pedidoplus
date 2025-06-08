package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.ItemCardapioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RestauranteClient {

    private static final String BASE_URL = "https://restaurante-production-7756.up.railway.app/itensCardapio";

    @Autowired
    private RestTemplate restTemplate;

    public List<ItemCardapioDTO> listarItensCardapio() {
        try {
            System.out.println("Buscando itens do cardápio na URL: " + BASE_URL);
            ItemCardapioDTO[] itens = restTemplate.getForObject(BASE_URL, ItemCardapioDTO[].class);
            System.out.println("Retorno RestauranteClient: " + (itens != null ? Arrays.toString(itens) : "null"));
            if (itens != null) {
                return Arrays.asList(itens);
            } else {
                System.out.println("Cardápio retornou nulo.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar itens do cardápio: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}