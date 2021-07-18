package br.com.dootax.felipecb.desafio.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dootax.felipecb.desafio.dao.DocumentoRepository;
import br.com.dootax.felipecb.desafio.enumerator.StatusEnum;
import br.com.dootax.felipecb.desafio.service.DocumentoService;
import br.com.dootax.felipecb.desafio.vo.StatusDocumentoVO;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(Saida.class)
public class SaidaTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	DocumentoService documentoService;
	
	@MockBean
	DocumentoRepository documentoRepository;
	
	final String url = "/saida/chaves";
	
	@Test
	public void buscarSemPaginaECodigoEmpresa() throws Exception {
		
		RequestBuilder request = validar(20, 11, null, null);
		
		mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(buscarResultado(20, 11))));
	}
	
	@Test
	public void buscarSemPagina() throws Exception {
		RequestBuilder request = validar(20, 11, null, "0060");
		mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(buscarResultado(20, 11))));
	}
	
	@Test
	public void buscarSemCodigoEmpresa() throws Exception {
		RequestBuilder request = validar(20, 11, Optional.of(2), null);
		mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(buscarResultado(20, 11))));
	}
	
	@Test
	public void buscar() throws Exception {
		RequestBuilder request = validar(20, 11, Optional.of(0), "0060");
		
		mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(buscarResultado(20, 11))));
	}
	
	private RequestBuilder validar(int qtdChaves, int qtdValidos, Optional<Integer> pagina, String codigo) throws Exception {
		
		criarChavesBanco(qtdChaves, qtdValidos, pagina, codigo);
		
		RequestBuilder request;
		
		if(pagina != null && codigo != null) {
			request = MockMvcRequestBuilders.get(url)
					.contentType(MediaType.APPLICATION_JSON)
					.param("pagina", pagina.get().toString())
					.param("codigo", codigo);
			
		} else if (pagina != null) {
			request = MockMvcRequestBuilders.get(url)
					.contentType(MediaType.APPLICATION_JSON)
					.param("pagina", pagina.get().toString());
			
		} else if (codigo != null) {
			request = MockMvcRequestBuilders.get(url)
					.contentType(MediaType.APPLICATION_JSON)
					.param("codigo", codigo);
			
		} else {
			request = MockMvcRequestBuilders.get(url)
					.contentType(MediaType.APPLICATION_JSON);
		}
		
		return request;
		
	}
	

	private String criarChave(int i) {
		return (new BigInteger("10000000000000000000000000000000000000000000")).add(new BigInteger(i + "")).toString();
	}
	
	private void criarChavesBanco(int numeroChaves, int numeroValidas, Optional<Integer> pagina, String codigo) {
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		Page<StatusDocumentoVO> paginas = new PageImpl<>(buscarResultado(numeroChaves, numeroValidas));
		Mockito.when(documentoService.buscarChaves(pagina != null ? pagina.get() : Integer.parseInt("0"), codigo)).thenReturn(paginas);
	}
	
	private List<StatusDocumentoVO> buscarResultado(int numeroChaves, int numeroValidas) {
		List<StatusDocumentoVO> chaves = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			if(i < numeroValidas) {
				chaves.add(new StatusDocumentoVO(new BigInteger(criarChave(i)), StatusEnum.VALIDADO));
			} else {
				chaves.add(new StatusDocumentoVO(new BigInteger(criarChave(i)), null));
			}
		}
		return chaves;
	}
	
	
}
