package br.com.dootax.felipecb.desafio.rest;

import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;
import br.com.dootax.felipecb.desafio.service.DocumentoService;
import br.com.dootax.felipecb.desafio.vo.StatusDocumentoVO;

/**
 * 3) Uma API que faça a listagem das chaves existentes no banco de dados e seu
 * status atual Deve ser possível buscar as chaves de uma empresa específica,
 * informada na request A reposta da API deve apresentar uma lista de no máximo
 * 20 chaves por request, mas deve possibilitar que o usuário consulte todas as
 * chaves existentes na tabela
 */
@RestController
@RequestMapping("/saida")
public class Saida {

	@Autowired
	DocumentoService documentoBean;

	ObjectMapper mapper = new ObjectMapper();

	// Não envia valores nulos
	public Saida() {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * Realiza a busca das chaves e seus devidos status
	 * 
	 * @param pagina        indica a página da listagem para buscar os dados
	 *                      respectivos
	 * @param codigoEmpresa filtra pelo código da empresa
	 * @return Page<StatusDocumentoVO>
	 */
	@RequestMapping(value = "/chaves", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> buscarChaves(@RequestParam(value = "pagina", required = false) Optional<Integer> pagina,
			@RequestParam(value = "codigo", required = false) Optional<String> codigoEmpresa) {

		Page<StatusDocumentoVO> listaRetorno = documentoBean.buscarChaves(pagina.orElse(0), codigoEmpresa.orElse(null));

		try {
			return ResponseEntity.status(Response.Status.OK.getStatusCode()).body(mapper.writeValueAsString(listaRetorno.toList()));
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).body(MensagemEnum.ERRO_SERVIDOR.getMensagem());
		}
	}

}
