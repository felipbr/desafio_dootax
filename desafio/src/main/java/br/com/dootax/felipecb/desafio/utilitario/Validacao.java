package br.com.dootax.felipecb.desafio.utilitario;

import java.math.BigInteger;

import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;
import br.com.dootax.felipecb.desafio.excecao.ValidacaoException;

public abstract class Validacao {

	/**
	 * Envia a chave em string e recebe convertida em BigInteger Caso a chave seja
	 * invalida, recebe exceção referente ao erro.
	 * 
	 * @param chave
	 * @return
	 * @throws ValidacaoException
	 */
	public static BigInteger chave(String chave) throws ValidacaoException {

		if (chave == null || chave.isBlank() || chave.isEmpty())
			throw new ValidacaoException(MensagemEnum.CHAVE_NAO_INFORMADA);

		if (chave.length() < Constante.TAMANHO_CHAVE)
			throw new ValidacaoException(MensagemEnum.CHAVE_MENOR);

		if (chave.length() > Constante.TAMANHO_CHAVE)
			throw new ValidacaoException(MensagemEnum.CHAVE_MAIOR);

		if(ehNumerico(chave)) {
			return new BigInteger(chave);
		} else {
			throw new ValidacaoException(MensagemEnum.CHAVE_INVALIDA);
		}

	}
	
	/**
	 * Envia a chave em string e recebe convertida em BigInteger Caso a chave seja
	 * invalida, recebe exceção referente ao erro.
	 * 
	 * @param chave
	 * @return
	 * @throws ValidacaoException
	 */
	public static String codigoEmpresa(String codigo) throws ValidacaoException {

		if (codigo == null || codigo.isBlank() || codigo.isEmpty())
			throw new ValidacaoException(MensagemEnum.CODIGO_EMPRESA_NAO_INFORMADO);

		if (codigo.length() < Constante.TAMANHO_CODIGO_EMPRESA)
			throw new ValidacaoException(MensagemEnum.CODIGO_EMPRESA_MENOR);

		if (codigo.length() > Constante.TAMANHO_CODIGO_EMPRESA)
			throw new ValidacaoException(MensagemEnum.CODIGO_EMPRESA_MAIOR);

		if( ehNumerico(codigo)) {
			return codigo;
		} else {
			throw new ValidacaoException(MensagemEnum.CODIGO_EMPRESA_INVALIDO);
		}

	}

	private static boolean ehNumerico(String str) {
		return str.matches("\\d+(\\.\\d+)?");
	}

}
