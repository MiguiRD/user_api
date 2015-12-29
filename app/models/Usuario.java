package models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.ValidateWith;
import validators.ApellidosValidator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;


@Entity
public class Usuario extends Model {
	
	@Id
	private Long id;
	
	@Required
	private String nombre;
	
	@Required
	@ValidateWith(value=ApellidosValidator.class, message="invalid_apellidos")
	private String apellidos;
	
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="usuario")
	@Valid
	private List<Telefono> telefonos = new ArrayList<Telefono>();	
	
	
	public static final Find<Long, Usuario> find = new Find<Long, Usuario>(){};
	
	
	public static List<Usuario> findPagina(Integer pagina, Integer size) {
		return find.orderBy("id").setMaxRows(size).setFirstRow(pagina*size).findList();
	}

	public boolean changeData(Usuario newData) {
		boolean changed = false;
		
		if (newData.nombre != null) {
			this.nombre = newData.nombre;
			changed = true;
		}
		
		if (newData.apellidos != null) {
			this.apellidos = newData.apellidos;
			changed = true;
		}
		
		return changed;
	}
	
	public void addTelefono(Telefono telefono) {
		telefonos.add(telefono);
		telefono.setUsuario(this);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public List<Telefono> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<Telefono> telefonos) {
		this.telefonos = telefonos;
	}
	
}
