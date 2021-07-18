package br.com.dootax.felipecb.desafio.entidade;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DocumentoId implements Serializable {

	private static final long serialVersionUID = 1L;

	public DocumentoId() {
		super();
	}

	public DocumentoId(BigInteger chave, String codigoEmpresa) {
		this.chave = chave;
		this.codigoEmpresa = codigoEmpresa;
	}

	@Column(name = "chave", precision = 44, scale = 0, nullable = false)
	private BigInteger chave;

	@Column(name = "codigo_empresa", nullable = false)
	private String codigoEmpresa;

	public BigInteger getChave() {
		return chave;
	}

	public void setChave(BigInteger chave) {
		this.chave = chave;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

}