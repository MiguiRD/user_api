package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import validators.NumeroTelefono;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Telefono extends Model {
	
	@Id
	private Long id;
	
	@Required
	@NumeroTelefono
	private String numero;
	
	@Required
	private Boolean movil;
	
	@ManyToOne
	@JsonIgnore
	private Usuario usuario;
	
	
	public static final Find<Long, Telefono> find = new Find<Long, Telefono>(){};
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Boolean getMovil() {
		return movil;
	}

	public void setMovil(Boolean movil) {
		this.movil = movil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
