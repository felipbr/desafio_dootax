package br.com.dootax.felipecb.desafio.utilitario;

import java.io.File;

public abstract class Constante {


	public final static Integer PAGINACAO_PADRAO = 20;
	
	public final static Integer TAMANHO_CODIGO_EMPRESA = 4;
	public final static Integer TAMANHO_CHAVE = 44;
	
	public final static long TEMPO_EXECUCAO_LEITURA_PASTA = 180000;

	private final static String PASTA_BASE = "/Users/felipebrasil/Desktop/arquivosDootax/";
	
	/**
	 * Busca direotio que contem os novos arquivos
	 * Tenta criar o diretorio caso nao exista
	 * @return
	 */
	public static String diretorioNovos() {
		final String PASTA_PREDEFINIDA = "/novos/";
		return retornarDiretorio(PASTA_PREDEFINIDA);
	}
	
	/**
	 * Busca o diretorio que contem os arquivos processados
	 * Tenta criar o diretorio caso nao exista
	 * @return
	 */
	public static String diretorioProcessados() {
		final String PASTA_PROCESSADOS = "/processados/";
		return retornarDiretorio(PASTA_PROCESSADOS);
	}
	
	/**
	 * Busca o diretorio que contem o src com os erros
	 * Tenta criar o diretorio caso nao exista
	 * @return
	 */
	public static String diretorioComErros() {
		final String PASTA_ARQUIVO_COM_ERRO = "/comEro/";
		return retornarDiretorio(PASTA_ARQUIVO_COM_ERRO);
	}
	/**
	 * Adiciona o ultimo diretorio na pasta final
	 * Tenta criar o diretorio caso nao exista
	 * @param pastaFinal
	 * @return
	 */
	private static String retornarDiretorio(String pastaFinal) {
		File file = new File(PASTA_BASE, pastaFinal);
		file.mkdirs();
		return file.getAbsolutePath();
	}
	
}
