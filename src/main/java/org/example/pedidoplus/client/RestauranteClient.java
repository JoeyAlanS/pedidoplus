package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.ItemCardapioDTO;
import org.example.pedidoplus.dto.RestauranteResumoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RestauranteClient {

    private static final String BASE_URL = "https://restaurante-production-7756.up.railway.app/";

    @Autowired
    private RestTemplate restTemplate;

    public List<RestauranteResumoDTO> listarRestaurantes() {
        String url = BASE_URL + "restaurante";
        try {
            RestauranteResumoDTO[] restaurantes = restTemplate.getForObject(url, RestauranteResumoDTO[].class);
            if (restaurantes != null) {
                return Arrays.asList(restaurantes);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar restaurantes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ItemCardapioDTO> listarItensCardapio() {
        String url = BASE_URL + "itensCardapio";
        try {
            ItemCardapioDTO[] itens = restTemplate.getForObject(url, ItemCardapioDTO[].class);
            if (itens != null) {
                return Arrays.asList(itens);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar itens do cardápio: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ItemCardapioDTO> listarItensCardapioPorRestaurante(String restauranteId) {
        String url = BASE_URL + "itensCardapio/restaurante/" + restauranteId;
        try {
            ItemCardapioDTO[] itens = restTemplate.getForObject(url, ItemCardapioDTO[].class);
            if (itens != null) {
                return Arrays.asList(itens);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar itens do cardápio por restaurante: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}