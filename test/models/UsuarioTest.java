package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import play.data.Form;
import play.libs.Json;


public class UsuarioTest {

    @Test
    public void nombreEsRequerido() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario usuario = new Usuario();
			usuario.setApellidos("los apellidos");
			
    			Form<Usuario> form = Form.form(Usuario.class).bind(Json.toJson(usuario));
    		
    			assertTrue(form.hasErrors());
    			assertEquals(1, form.field("nombre").errors().size());
    			assertTrue(form.field("apellidos").errors().isEmpty());
		});
    	}

    @Test
    public void apellidoEsRequerido() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario usuario = new Usuario();
			usuario.setNombre("el nombre");
			
    			Form<Usuario> form = Form.form(Usuario.class).bind(Json.toJson(usuario));

    			assertTrue(form.hasErrors());
    			assertTrue(form.field("nombre").errors().isEmpty());
    			assertEquals(2, form.field("apellidos").errors().size());
		});
    	}
    
    @Test
    public void guardaUsuario() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario usuario = insertUsuario("el nombre", "los apellidos");
			
			Usuario usuarioGuardado = Usuario.find.byId(usuario.getId());
			
			assertEquals(usuario.getNombre(), usuarioGuardado.getNombre());
			assertEquals(usuario.getApellidos(), usuarioGuardado.getApellidos());
		});
    	}
    
    @Test
    public void borraUsuario() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario usuario = insertUsuario("el nombre", "los apellidos");
			
			Usuario usuarioGuardado = Usuario.find.byId(usuario.getId());
			usuarioGuardado.delete();
			
			assertNull(Usuario.find.byId(usuario.getId()));
		});
    	}

    @Test
    public void actualizaUsuario() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario usuario = insertUsuario("el nombre", "los apellidos");

			Usuario usuarioGuardado = Usuario.find.byId(usuario.getId());
			usuarioGuardado.setNombre("otro nombre");
			usuarioGuardado.setApellidos("otros apellidos");
			boolean changed = usuario.changeData(usuarioGuardado);
			usuario.save();
			
			assertTrue(changed);
			
			Usuario usuarioActualizado = Usuario.find.byId(usuario.getId());
			assertEquals(usuarioGuardado.getNombre(), usuarioActualizado.getNombre());
			assertEquals(usuarioGuardado.getApellidos(), usuarioActualizado.getApellidos());
		});
    	}

    @Test
    public void findPagina() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			Usuario u1 = insertUsuario("n1", "n2");
			Usuario u2 = insertUsuario("n2", "n2");
			Usuario u3 = insertUsuario("n3", "n3");
			
			// Primera página de 1 registro
				List<Usuario> pagina = Usuario.findPagina(0, 1);
				assertEquals(1, pagina.size());
				assertEquals(u1.getId(), pagina.get(0).getId());
 
				// Primera página de 2 registros 
			pagina = Usuario.findPagina(0, 2);
			assertEquals(2, pagina.size());
			assertEquals(u1.getId(), pagina.get(0).getId());
			assertEquals(u2.getId(), pagina.get(1).getId());

			// Segunda página de 2 registros
			pagina = Usuario.findPagina(1, 2);
			assertEquals(1, pagina.size());
			assertEquals(u3.getId(), pagina.get(0).getId());
		});
    	}
    
    
    private Usuario insertUsuario(String nombre, String apellidos) {
		Usuario u = new Usuario();
		u.setNombre(nombre);
		u.setApellidos(apellidos);
		u.save();
		
		return u;
    }
    
}
