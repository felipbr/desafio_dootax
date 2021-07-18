package br.com.dootax.felipecb.desafio.processo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.dootax.felipecb.desafio.dao.DocumentoRepository;
import br.com.dootax.felipecb.desafio.entidade.Documento;
import br.com.dootax.felipecb.desafio.enumerator.MensagemEnum;
import br.com.dootax.felipecb.desafio.excecao.ValidacaoException;
import br.com.dootax.felipecb.desafio.utilitario.Constante;
import br.com.dootax.felipecb.desafio.utilitario.Validacao;

@Component
public class ProcessoAutomatizado {

	private static final String EXTENSAO_TXT = ".txt";

	int linhaAtual = 0;
	
	Logger logger;
	
	@Autowired
	DocumentoRepository chaveRepository;

	
	public ProcessoAutomatizado() {
		logger =  Logger.getLogger(this.getClass().getName());
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler(Constante.diretorioComErros()+"/Log.log");
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ao final da execução, conta o tempo para executar o próximo
	 */
	@Scheduled(fixedDelay = Constante.TEMPO_EXECUCAO_LEITURA_PASTA, initialDelay = 5000)
	public void buscarArquivo() {

		System.out.println("Inicio: " + Calendar.getInstance().getTime().toString());
		
		// Busca aos poucos os arquivos no diretorio
		try (Stream<Path> paths = Files.walk(Paths.get(Constante.diretorioNovos()))) {
			paths.filter(arquivo -> arquivo.toString().endsWith(EXTENSAO_TXT)).forEach(arquivo -> {
				try {
					linhaAtual = 0;
					String novoNome = arquivo.getFileName().toString().replace(EXTENSAO_TXT, "");
					novoNome += "_" + Calendar.getInstance().getTime().getTime()+EXTENSAO_TXT;
					
					//Abordagem que consome menos memória
					FileInputStream inputStream = new FileInputStream(arquivo.toAbsolutePath().toString());
					Scanner sc = null;
					long totalLinha = (Files.lines(Paths.get(arquivo.toAbsolutePath().toString()))).count();
					try {
					    sc = new Scanner(inputStream, "UTF-8");
					    while (sc.hasNextLine()) {
					        try {
								salvarLinha(sc.nextLine(), totalLinha, ++linhaAtual);
							} catch (ValidacaoException e) {
								gerarErro(null, e.getMessage(), novoNome);
							}
					    }
					    // Scanner suspende excecao
					    if (sc.ioException() != null) {
							gerarErro(arquivo, MensagemEnum.PROBLEMA_LEITURA_ARQUIVO.getMensagem(), novoNome);
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
					arquivo.toFile().renameTo(new File(Constante.diretorioProcessados(), novoNome));

				} catch (IOException e) {
					logger.severe(MensagemEnum.PROBLEMA_LEITURA_ARQUIVO.getMensagem());
					return;
				}
			});
		} catch (IOException e1) {
			logger.severe(MensagemEnum.PROBLEMA_LEITURA_DIRETORIO.getMensagem());
			return;
		}

		System.out.println("Fim: "+Calendar.getInstance().getTime().toString());

	}

	public void salvarLinha(String linha, long total, int atual) throws ValidacaoException {

		final String CARACTER_DELIMITADOR = ";";

		String[] valores = linha.split(CARACTER_DELIMITADOR);

		if (valores.length != 2) {
			throw new ValidacaoException(MensagemEnum.LINHA_FORA_PADRAO);
		}

		// Verificar se é necessário
		// Validar codigo da empresa com 4 caracteres
		Documento doc = new Documento(Validacao.chave(valores[1]), Validacao.codigoEmpresa(valores[0]));

		//Exibe a cada 100.000 pra ver que ainda está rodando e exibe os ultimos 3 (apenas para testes).
//		if(atual%100000 == 0 || atual+3 >= total)
//			System.out.println(atual+"/"+total+ ". Código da empresa: " + doc.getId().getCodigoEmpresa() + ". Chave: " + doc.getId().getChave());
			
		chaveRepository.save(doc);
	}
	
	/**
	 * Arquivo null não move de lugar
	 * @param arquivo
	 * @param mensagemErro
	 * @param novoNome
	 */
	private void gerarErro(Path arquivo, String mensagemErro, String novoNome) {
		logger.severe("Arquivo: "+novoNome+ ". Linha: "+linhaAtual+ ": "+mensagemErro);
		if(null != arquivo) {
			arquivo.toFile().renameTo(new File(Constante.diretorioComErros(), novoNome));
		}
	}
}
