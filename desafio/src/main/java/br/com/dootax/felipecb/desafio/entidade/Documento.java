package br.com.dootax.felipecb.desafio.entidade;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.dootax.felipecb.desafio.enumerator.StatusEnum;

@Entity
@Table(name = "documento")
public class Documento {

	public Documento(){
		super();
	}
	
	public Documento(BigInteger chave, String codigoEmpresa){
		this.id = new DocumentoId(chave, codigoEmpresa);
	}
	
	@EmbeddedId
	private DocumentoId id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_validacao")
	private StatusEnum status;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_empresa", nullable = false, ...)
//	private Empresa empresa;

	public DocumentoId getId() {
		return id;
	}

	public void setId(DocumentoId id) {
		this.id = id;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

//	public Empresa getEmpresa() {
//		return empresa;
//	}
//
//	public void setEmpresa(Empresa empresa) {
//		this.empresa = empresa;
//	}



}