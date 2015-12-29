package controllers;

import helpers.ControllerHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Usuario;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * @author jm
 */
public class Usuarios extends Controller {
	
	/**
	 * Action method para GET /usuarios/<pag>.
	 * Opcionalmente se puede pasar el parámetro size para indicar el tamaño de página.
	 * Si no se pasa tamaño de página, se usará 10
	 * 
	 * @param pag número de página a recuperar.
	 */
	public Result index(Integer pag) {
		Result res;
		
		String paginaSize = request().getQueryString("size");
		if (paginaSize == null) {
			paginaSize = "10";
		}

		List<Usuario> lista = Usuario.findPagina(pag, Integer.valueOf(paginaSize));
		Integer count = Usuario.find.findRowCount();
		
		if (ControllerHelper.acceptsJson(request())) {
			Map<String, Object> result = new HashMap<String, Object>();
			
			result.put("count", count);
			result.put("pagina", lista);
			
			res = ok(Json.toJson(result));
		}
		else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.usuarios.render(lista, count));
		}
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
			
		return res; 
	}
	
	/**
	 * Action method para GET /usuario/<uId>
	 * 
	 * @param uId identificador del usuario
	 */
	public Result retrieve(Long uId) {
		Result res;
		
		Usuario usuario = Usuario.find.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			if (ControllerHelper.acceptsJson(request())) {
				res = ok(Json.toJson(usuario));
			}
			else if (ControllerHelper.acceptsXml(request())) {
				res = ok(views.xml._usuario.render(usuario));
			}
			else {
				res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
			}
		}
		
		return res;
	}
	
	/**
	 * Action method para POST /usuario/<uId>.
	 * Se deben pasar los atributos del usuario en el body de la petición. 
	 */
	public Result create() {
		Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", form.errorsAsJson()));
		}

		Usuario usuario = form.get();
		
		usuario.save();
		
		return created();
	}

	/**
	 * Action method para PUT /usuario/<uId>
	 * Se deben pasar los atributos a modificar en el body de la petición. 
	 * 
	 * @param uId identificador del usuario a modificar
	 */
	public Result update(Long uId) {
		Usuario usuario = Usuario.find.byId(uId);
		if (usuario == null) {
			return notFound();
		}
		
		Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(1, "invalid_user", form.errorsAsJson()));
		}

		Result res;

		if (usuario.changeData(form.get())) {
			usuario.save();
			res = ok();
		}
		else {
			res = status(NOT_MODIFIED);
		}
		
		return res;
	}
	
	/**
	 * Action method para DELETE /usuario/<uId>
	 * 
	 * @param uId identificador del usuario a borrar
	 */
	public Result delete(Long uId) {
		Usuario usuario = Usuario.find.byId(uId);
		if (usuario == null) {
			return notFound();
		}

		usuario.delete();

		return ok();
	}
	
}
