package br.com.dootax.felipecb.desafio.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;
import br.com.dootax.felipecb.desafio.excecao.ValidacaoException;
import br.com.dootax.felipecb.desafio.service.DocumentoService;
import br.com.dootax.felipecb.desafio.utilitario.Validacao;

/**
 * 2) Uma API que receba uma lista de chaves e altere o status dessas chaves na
 * tabela para "Validado" A resposta da API deve ser síncrona A lista de chaves
 * deve possuir no máximo 20 itens, e cada um deles deve possuir 44 caracteres
 * numéricos A resposta da API deve listar as chaves cujo status foram alterados
 * com sucesso, e as que não foram por algum erro (chave inexistente,
 * incompleta, etc)
 */

@RestController
@RequestMapping("/entrada")
public class Entrada {

	private ObjectMapper mapper = new ObjectMapper();

	public Entrada() {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	@Autowired
	DocumentoService documentoService;

	@PostMapping(value = "/validar", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> validar(@RequestBody List<String> chaves) {

		// Validação inicial dos parametros
		if (chaves == null || chaves.isEmpty()) {
			return montarmensagemErro(MensagemEnum.CHAVE_NAO_INFORMADA);
		}

		if (chaves.size() > 20) {
			return montarmensagemErro(MensagemEnum.EXCEDEU_QUANTIDADE);
		}

		Map<String, List<String>> resultado = new HashMap<>();

		List<String> listaChaves;
		MensagemEnum sv;
		for (String key : chaves) {
			try {
				if (documentoService.validarChave(Validacao.chave(key))) {
					sv = MensagemEnum.CHAVE_VALIDADA;
				} else {
					sv = MensagemEnum.CHAVE_INEXISTENTE;
				}
			} catch (ValidacaoException e) {
				sv = e.getErro();
			}

			// Agrupa a chave pelo tipo da validacao
			if (resultado.get(sv.toString()) != null) {
				listaChaves = resultado.get(sv.toString());
			} else {
				listaChaves = new ArrayList<>();
			}
			listaChaves.add(key);

			resultado.put(sv.toString(), listaChaves);
		}

		try {
			return ResponseEntity.status(Response.Status.OK.getStatusCode()).body(mapper.writeValueAsString(resultado));
		} catch (JsonProcessingException e) {
			return montarmensagemErro(MensagemEnum.ERRO_SERVIDOR);
		}
	}

	private ResponseEntity<String> montarmensagemErro(MensagemEnum erroEnum) {
		return ResponseEntity.status(Response.Status.BAD_REQUEST.getStatusCode()).body(erroEnum.getMensagem());
	}

}
