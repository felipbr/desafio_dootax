package br.com.dootax.felipecb.desafio.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import br.com.dootax.felipecb.desafio.dao.DocumentoRepository;
import br.com.dootax.felipecb.desafio.entidade.Documento;
import br.com.dootax.felipecb.desafio.enumerator.StatusEnum;
import br.com.dootax.felipecb.desafio.utilitario.Constante;
import br.com.dootax.felipecb.desafio.vo.StatusDocumentoVO;

@Component
public class DocumentoService {

	@Autowired
	DocumentoRepository chaveRepository;

	public Documento buscarPorChave(BigInteger chave) {
		return chaveRepository.findById(chave).orElse(null);
	}

	/**
	 * Caso a chave exista, coloca com status de validado
	 * @param chave
	 * @return boolean 
	 */
	public boolean validarChave(BigInteger chave) {
		return chaveRepository.alterarStatus(chave, StatusEnum.VALIDADO) > 0;
	}

	/**
	 * Realiza a busca das chaves e seus devidos status de modo paginado
	 * @param pagina    indica a página da listagem para buscar os dados respectivos
	 * @param codigoEmpresa filtra pelo código da empresa
	 * @return Page<StatusDocumentoVO>
	 */
	public Page<StatusDocumentoVO> buscarChaves(Integer pagina, String codigoEmpresa) {

		Pageable pageable = PageRequest.of(pagina, Constante.PAGINACAO_PADRAO, Sort.by("id"));
		Page<StatusDocumentoVO> lista = chaveRepository.buscarChaves(codigoEmpresa, pageable);

		return lista;
	}

}
