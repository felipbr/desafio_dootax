package br.com.dootax.felipecb.desafio.vo;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.dootax.felipecb.desafio.enumerator.StatusEnum;

public class StatusDocumentoVO implements Serializable{

	private static final long serialVersionUID = 1L;

	BigInteger chave;
	StatusEnum status;

	public StatusDocumentoVO(BigInteger chave, StatusEnum status) {
		this.chave = chave;
		this.status = status;
	}
	
	public BigInteger getChave() {
		return chave;
	}

	public void setChave(BigInteger chave) {
		this.chave = chave;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}