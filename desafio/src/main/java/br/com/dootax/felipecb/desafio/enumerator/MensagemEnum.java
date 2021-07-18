package br.com.dootax.felipecb.desafio.enumerator;

public enum MensagemEnum {


	ERRO_SERVIDOR("Erro inesperado."),
	
	// Mensagens erro na validação das condições
	EXCEDEU_QUANTIDADE("Foi enviado mais chaves que o permitido."),
	
	// Mensagem de validação da linha
	FORA_DO_PADRAO("O arquivo está fora do padrão esperado"),

	// Mensagem validacao do código da empresa
	CODIGO_EMPRESA_NAO_INFORMADO("O código da empresa não foi informado."),
	CODIGO_EMPRESA_MENOR("O código da empresa tem formato menor do que o esperado."),
	CODIGO_EMPRESA_MAIOR("O código da empresa tem formato maior do que o esperado."),
	CODIGO_EMPRESA_INVALIDO("O código da empresa tem formato inválido."),
//	CODIGO_EMPRESA_INEXISTENTE("A chave não existe na base de dados."),
	
	// Mensagem validacao da chave
	CHAVE_NAO_INFORMADA("A chave não foi informada."),
	CHAVE_MENOR("A chave tem formato menor do que o esperado."),
	CHAVE_MAIOR("A chave tem formato maior do que o esperado."),
	CHAVE_INVALIDA("A chave tem formato inválido."),
	CHAVE_INEXISTENTE("A chave não existe na base de dados."),
	// Quando obtiver sucesso
	CHAVE_VALIDADA("A chave foi validada.");

	MensagemEnum(String mensagem) {
		this.mensagem = mensagem;
	}

	private String mensagem;

	public String getMensagem() {
		return mensagem;
	}

}
