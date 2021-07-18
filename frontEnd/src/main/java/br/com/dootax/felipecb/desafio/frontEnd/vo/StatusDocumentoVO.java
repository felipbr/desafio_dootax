package br.com.dootax.felipecb.desafio.frontEnd.vo;

import java.io.Serializable;
import java.math.BigInteger;

public class StatusDocumentoVO implements Serializable{

	private static final long serialVersionUID = 1L;

	BigInteger chave;
	String status;

	public StatusDocumentoVO(BigInteger chave, String status) {
		this.chave = chave;
		this.status = status;
	}
	
	public BigInteger getChave() {
		return chave;
	}

	public void setChave(BigInteger chave) {
		this.chave = chave;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}