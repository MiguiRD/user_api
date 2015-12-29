package controllers;

import helpers.ControllerHelper;
import models.Telefono;
import models.Usuario;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * @author jm
 */
public class Telefonos extends Controller {
	
	/**
	 * Action method para GET /usuario/<uId>/telefonos
	 * 
	 * @param uId identificador del usuario
	 */
	public Result index(Long uId) {
		Result res;
		
		Usuario usuario = Usuario.find.byId(uId);		
		if (usuario == null) {
			return notFound();
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			res = ok(Json.toJson(usuario.getTelefonos()));
		}
		else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.telefonos.render(usuario.getTelefonos()));
		}
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
			
		return res; 
	}
	
	/**
	 * Action method para PUT /usuario/<uId>/telefono
	 * Se deben pasar los atributos del teléfono en el body de la petición. 
	 * 
	 * @param uId identificador del usuario
	 */
	public Result create(Long uId) {
		Form<Telefono> form = Form.form(Telefono.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_phone", form.errorsAsJson()));
		}
		
		Usuario usuario = Usuario.find.byId(uId);		
		if (usuario == null) {
			return notFound();
		}

		Telefono telefono = form.get();
		
		telefono.setUsuario(usuario);
		telefono.save();
		
		return created();
	}

	/**
	 * Action method para DELETE /usuario/<uId>/telefono/<tId>
	 * Se deben pasar los atributos a modificar en el body de la petición. 
	 * 
	 * @param uId identificador del usuario
	 * @param tId identificador del teléfono a borrar
	 */
	public Result delete(Long uId, Long tId) {
		Telefono telefono = Telefono.find.byId(tId);
		if (telefono == null) {
			return notFound();
		}
		
		if (!telefono.getUsuario().getId().equals(uId)) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", null));
		}

		telefono.delete();

		return ok();
	}
	
}
