package br.com.dootax.felipecb.desafio.dao;

import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.dootax.felipecb.desafio.entidade.Documento;
import br.com.dootax.felipecb.desafio.enumerator.StatusEnum;
import br.com.dootax.felipecb.desafio.vo.StatusDocumentoVO;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, BigInteger> {

	@Query("SELECT doc FROM Documento doc WHERE doc.id.chave = :chave")
	Documento recuperarPorChave(@Param(value = "chave") BigInteger chave);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Documento set status = :status where chave = :chave")
	int alterarStatus(@Param(value = "chave") BigInteger chave, @Param(value = "status")  StatusEnum status);

	@Query("SELECT new br.com.dootax.felipecb.desafio.vo.StatusDocumentoVO(doc.id.chave, doc.status) FROM Documento doc WHERE (:codigo is null or doc.id.codigoEmpresa = :codigo) ")
	Page<StatusDocumentoVO> buscarChaves(@Param(value = "codigo") String codigoEmpresa, Pageable pageable);

}
