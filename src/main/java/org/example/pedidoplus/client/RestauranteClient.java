package org.example.pedidoplus.client;

import org.example.pedidoplus.dto.ItemCardapioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RestauranteClient {

    private static final String BASE_URL = "https://restaurante-production-7756.up.railway.app/itensCardapio";
    private static final Logger logger = LoggerFactory.getLogger(RestauranteClient.class);

    @Autowired
    private RestTemplate restTemplate;

    public List<ItemCardapioDTO> listarItensCardapio() {
        try {
            ItemCardapioDTO[] itens = restTemplate.getForObject(BASE_URL, ItemCardapioDTO[].class);
            if (itens != null) {
                return Arrays.asList(itens);
            } else {
                logger.warn("Cardápio retornou nulo.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar itens do cardápio: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}