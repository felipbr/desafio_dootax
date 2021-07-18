package br.com.dootax.felipecb.desafio.frontEnd.handler;

import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	final String ERRO_RECEBIMENTO_MENSAGEM = "Mensagem recebida fora do padrão";

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				&& httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
				&& !httpResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST)
				&& !httpResponse.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {

		if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			// handle SERVER_ERROR
		} else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			// handle CLIENT_ERROR
			if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new NotFoundException();
			}
		}
	}
}