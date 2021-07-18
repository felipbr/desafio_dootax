package br.com.dootax.felipecb.desafio.frontEnd.controller;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.dootax.felipecb.desafio.frontEnd.handler.RestTemplateResponseErrorHandler;
import br.com.dootax.felipecb.desafio.frontEnd.vo.StatusDocumentoVO;

@Controller
@RequestMapping("/consultar")
public class ConsultarChave {

	@GetMapping(value = {"", "/"})
	private String listarChaves(@RequestParam(value = "pagina", required = false) Optional<Integer> pagina,
			@RequestParam(value = "codigo", required = false) Optional<String> codigoEmpresa, Model model) {

		try {
			String url = "http://localhost:8080/saida/chaves?pagina=" + pagina.orElse(0);
			if(codigoEmpresa.isPresent() && ! codigoEmpresa.get().isBlank()) {
				url += "&codigo="+ codigoEmpresa.get();
			}
			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
			ResponseEntity<String> resultado = restTemplate.getForEntity(
			  url,
			  String.class);
			
			if(resultado.getStatusCode().equals(HttpStatus.OK)) {
				Type listType = new TypeToken<List<StatusDocumentoVO>>(){}.getType();
				model.addAttribute("chaves", new Gson().fromJson(resultado.getBody(), listType));
			
			} else if(resultado.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				model.addAttribute("erro", resultado.getBody());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		model.addAttribute("pagina", pagina.orElse(0));
		model.addAttribute("codigo", codigoEmpresa.orElse(""));
		
		return "listaChaves";
	}
	
}
