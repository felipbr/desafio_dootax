package br.com.dootax.felipecb.desafio.processo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Scanner;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.dootax.felipecb.desafio.dao.DocumentoRepository;
import br.com.dootax.felipecb.desafio.entidade.Documento;
import br.com.dootax.felipecb.desafio.excecao.ValidacaoException;
import br.com.dootax.felipecb.desafio.utilitario.Constante;
import br.com.dootax.felipecb.desafio.utilitario.Validacao;

@Component
public class ProcessoAutomatizado {

	@Autowired
	DocumentoRepository chaveRepository;

	/**
	 * Ao final da execução, conta o tempo para executar o próximo
	 */
	@Scheduled(fixedDelay = Constante.TEMPO_EXECUCAO_LEITURA_PASTA, initialDelay = 5000)
	public void buscarArquivo() {

		System.out.println("Inicio: " + Calendar.getInstance().getTime().toString());
		
		// Busca aos poucos os arquivos no diretorio
		try (Stream<Path> paths = Files.walk(Paths.get(Constante.PASTA_PREDEFINIDA))) {
			paths.filter(arquivo -> arquivo.toString().endsWith(".txt")).forEach(arquivo -> {
				try {
					String novoNome = arquivo.getFileName().toString();
					novoNome += "_" + Calendar.getInstance().getTime().toString();
					
					//Abordagem que consome menos memória
					FileInputStream inputStream = new FileInputStream(arquivo.toAbsolutePath().toString());
					Scanner sc = null;
					long totalLinha = (Files.lines(Paths.get(arquivo.toAbsolutePath().toString()))).count();
					int linhaAtual = 0;
					try {
					    sc = new Scanner(inputStream, "UTF-8");
					    while (sc.hasNextLine()) {
					        try {
								salvarLinha(sc.nextLine(), totalLinha, linhaAtual++);
							} catch (ValidacaoException e) {
								System.out.println("Deu erro: "+e.getMessage());
								return;
							}
					    }
					    // Scanner suspende excecao
					    if (sc.ioException() != null) {
					    	System.out.println("Deu erro: "+sc.ioException().getMessage());
							return;
					    }
					} finally {
					    if (inputStream != null) {
					        inputStream.close();
					    }
					    if (sc != null) {
					        sc.close();
					    }
					}
					// Move para a pasta de processados
					arquivo.toFile().renameTo(new File(Constante.PASTA_PROCESSADOS, novoNome));

				} catch (IOException e) {
					e.printStackTrace();
					// TODO lançar erro de ler o arquivo
					// Move para a pasta de com erros
					arquivo.toFile().renameTo(new File(Constante.PASTA_ARQUIVO_COM_ERRO, arquivo.getFileName().toString()));
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
			// TODO lançar erro de ler o arquivo
		}

		System.out.println("Fim: "+Calendar.getInstance().getTime().toString());

	}

	public void salvarLinha(String linha, long total, int atual) throws ValidacaoException {

		final String CARACTER_DELIMITADOR = ";";

		String[] valores = linha.split(CARACTER_DELIMITADOR);

		if (valores.length != 2) {
			// TODO salvar em arquivo o numero da linha que deu erro
			return;
		}

		// TODO verificar se é necessário
		// Validar codigo da empresa com 4 caracteres
		Documento doc = new Documento(Validacao.chave(valores[1]), Validacao.codigoEmpresa(valores[0]));

		if(atual%100000 == 0 || atual+10 > total)
			System.out.println(atual+"/"+total+ ". Código da empresa: " + doc.getId().getCodigoEmpresa() + ". Chave: " + doc.getId().getChave());
			
		chaveRepository.save(doc);

	}
	
	
	
//	public void buscarArquivoMenosEficienteInicial() {
//		System.out.println("Inicio: " + Calendar.getInstance().getTime().toString());
//		// Busca aos poucos os arquivos no diretorio
//		try (Stream<Path> paths = Files.walk(Paths.get(Constante.PASTA_PREDEFINIDA))) {
//			paths.filter(arquivo -> arquivo.toString().endsWith(".txt")).forEach(arquivo -> {
//				try {
//					String novoNome = arquivo.getFileName().toString();
//					novoNome += "_" + Calendar.getInstance().getTime().toString();
//					// le linha a linha do arquivo 
//					Files.lines(arquivo).forEach(s -> {
//						try {
//							salvarLinha(s);
//						} catch (ValidacaoException e) {
//							System.out.println("Deu erro: "+e.getMessage());
//							return;
//						}
//					});
//
//					// Move para a pasta de processados
//					arquivo.toFile().renameTo(new File(Constante.PASTA_PROCESSADOS, novoNome));
//
//				} catch (IOException e) {
//					e.printStackTrace();
//					// TODO lançar erro de ler o arquivo
//					// Move para a pasta de com erros
//					arquivo.toFile().renameTo(new File(Constante.PASTA_ARQUIVO_COM_ERRO, arquivo.getFileName().toString()));
//				}
//			});
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			// TODO lançar erro de ler o arquivo
//		}
//		System.out.println("Fim: "+Calendar.getInstance().getTime().toString());
//	}

}
