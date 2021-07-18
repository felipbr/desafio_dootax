package br.com.dootax.felipecb.desafio.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dootax.felipecb.desafio.dao.DocumentoRepository;
import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;
import br.com.dootax.felipecb.desafio.service.DocumentoService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(Entrada.class)
public class EntradaTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	DocumentoService documentoService;
	
	@MockBean
	DocumentoRepository documentoRepository;
	
	final String url = "/entrada/validar";
	
	@Test
	public void validarSemChaves() throws Exception {
		
		//Nulo
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(null));

		mockMvc.perform(request)
		        .andExpect(status().isBadRequest())
//		        .andExpect(content().string(MensagemEnum.CHAVE_NAO_INFORMADA.getMensagem()))
		        .andReturn();

		//Vazio
		request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new ArrayList<String>()));

		mockMvc.perform(request)
		        .andExpect(status().isBadRequest())
//		        .andExpect(content().string(MensagemEnum.CHAVE_NAO_INFORMADA.getMensagem()))
		        .andReturn();
		
	}

	@Test
	public void validarMaisChaves() throws Exception {
		List<String> chaves = new ArrayList<String>();

		// Cria 21 chaves
		for (int i = 0; i <= 20; i++) {
			chaves.add(criarChaveValida(i));
		}
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isBadRequest())
		        .andExpect(content().string(MensagemEnum.EXCEDEU_QUANTIDADE.getMensagem()));
	}

	@Test
	public void validaChaveInvalidaMaior() throws Exception {
		List<String> chaves = new ArrayList<String>();
		// multiplica por 10 pra passar uma casa
		chaves.add(new BigInteger(criarChaveValida(1)).multiply(new BigInteger("10")).toString());

		Map<String, List<String>> resultado = new HashMap<>();
		resultado.put(MensagemEnum.CHAVE_MAIOR.name(), chaves);
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isOk())
		        .andExpect(content().string(objectMapper.writeValueAsString(resultado)));
	}

	@Test
	public void validaChaveInvalidaMenor() throws Exception {
		List<String> chaves = new ArrayList<String>();
		// divide por 10 pra passar uma casa
		chaves.add(new BigInteger(criarChaveValida(1)).divide(new BigInteger("10")).toString());

		Map<String, List<String>> resultado = new HashMap<>();
		resultado.put(MensagemEnum.CHAVE_MENOR.name(), chaves);
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isOk())
		        .andExpect(content().string(objectMapper.writeValueAsString(resultado)));
	}

	@Test
	public void validaChaveInvalidaComTexto() throws Exception {
		List<String> chaves = new ArrayList<String>();
		// divide por 10 para remover uma casa e adiciona uma letra
		chaves.add(new BigInteger(criarChaveValida(1)).divide(new BigInteger("10")).toString() + "a");

		Map<String, List<String>> resultado = new HashMap<>();
		resultado.put(MensagemEnum.CHAVE_INVALIDA.name(), chaves);
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isOk())
		        .andExpect(content().string(objectMapper.writeValueAsString(resultado)));
	}

	@Test
	public void chavesInexistente() throws Exception {
		
		List<String> chaves = new ArrayList<String>();
		chaves.add(criarChaveValida(1));

		Map<String, List<String>> resultado = new HashMap<>();
		resultado.put(MensagemEnum.CHAVE_INEXISTENTE.name(), chaves);
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isOk())
		        .andExpect(content().string(objectMapper.writeValueAsString(resultado)))
		        .andReturn();
	}
	
	@Test
	public void chavesValidas() throws Exception {
		
		validarNumeroChaves(10);
		
		List<String> chaves = new ArrayList<String>();
		for(int i = 0; i < 10; i++) {
			chaves.add(criarChaveValida(i));
		}

		Map<String, List<String>> resultado = new HashMap<>();
		resultado.put(MensagemEnum.CHAVE_VALIDADA.name(), chaves);
		
		RequestBuilder request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(chaves));
		
		mockMvc.perform(request)
		        .andExpect(status().isOk())
		        .andExpect(content().string(objectMapper.writeValueAsString(resultado)))
		        .andReturn();
	}

	private String criarChaveValida(int i) {
		return (new BigInteger("10000000000000000000000000000000000000000000")).add(new BigInteger(i + "")).toString();
	}
	
	private void validarNumeroChaves(int numeroChaves) {
		for(int i = 0; i < numeroChaves; i++) {
			Mockito.when(documentoService.validarChave(new BigInteger(criarChaveValida(i)))).thenReturn(true);
		}
	}

}
