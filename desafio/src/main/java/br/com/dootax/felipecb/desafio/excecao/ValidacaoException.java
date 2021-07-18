package br.com.dootax.felipecb.desafio.excecao;

import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;

public class ValidacaoException extends Exception {

	private static final long serialVersionUID = 1L;
	private MensagemEnum erro;

	public ValidacaoException(MensagemEnum erro) {
		super(erro.toString());
		this.erro = erro;
	}

	public MensagemEnum getErro() {
		return erro;
	}
	
	@Override
	public String getMessage() {
		return erro.getMensagem();
	}
	

}
