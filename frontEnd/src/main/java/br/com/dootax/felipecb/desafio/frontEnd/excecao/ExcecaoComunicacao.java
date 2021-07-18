package br.com.dootax.felipecb.desafio.frontEnd.excecao;

import java.io.IOException;

public class ExcecaoComunicacao extends IOException {
	
	private static final long serialVersionUID = 1L;

	public ExcecaoComunicacao(String mensagem) {
		super(mensagem);
	}

}
