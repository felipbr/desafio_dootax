package br.com.dootax.felipecb.desafio.frontEnd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import br.com.dootax.felipecb.desafio.frontEnd.handler.RestTemplateResponseErrorHandler;

@Controller
@RequestMapping("/validador")
@SessionAttributes("chaves")
public class ValidadorChave {

	List<String> chaves;
	
	@ModelAttribute("chaves")
	@Bean
	@Scope(
	  value = WebApplicationContext.SCOPE_SESSION, 
	  proxyMode = ScopedProxyMode.TARGET_CLASS)
	public List<String> getChaves() {
		return chaves;
	}
	public void setChaves(List<String> chaves) {
		this.chaves = chaves;
	}
	
	@GetMapping(value = {"","/"})
	private String consulta(Model model) {
		chaves = new ArrayList<String>();
		model.addAttribute("chaves", chaves);
		return "consultaChaves";
	}
	
	//Nao é feito nenhuma validacao para forçar erros na listagem 
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/adicionar")
	private String adicionar(@RequestParam(required = false) String chave, Model model) {
		setChaves((List<String>) model.getAttribute("chaves"));
		if(chaves == null) {
			chaves = new ArrayList<>();
		}
		
		if(chave != null) {
			chaves.add(chave);
		}
		model.addAttribute("chave", "");
		model.addAttribute("chaves", chaves);
		return "consultaChaves";
	}
	
	@PostMapping(value = "/validar")
	private String validar(Model model) {
		final String url = "http://localhost:8080/entrada/validar";
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
		
		ResponseEntity<String> resultado = restTemplate.postForEntity(
		  url,
		  chaves,
		  String.class);
		
		if(resultado.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("map", new Gson().fromJson(resultado.getBody(), Map.class));
		} else if(resultado.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			model.addAttribute("erro", resultado.getBody());
		}
		
		return "resultadoValidacao";
	}
	
}
