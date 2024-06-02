package JSF;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;

import database.DatabaseStatements;
import objects.Kunde;

@FacesValidator(value="validateMailExists")
public class ValidateMailExists implements Validator<Object>{
	
	@Override
    public void validate(FacesContext context, UIComponent component, Object email) throws ValidatorException {
    	DatabaseStatements stmt = new DatabaseStatements();
    	Kunde k = stmt.getKunde((String) email);
    	
        if (email == null) {
            return; // Let required="true" handle.
        }

        if (k == null) {
            throw new ValidatorException(new FacesMessage("Mail does not exist"));
        }
    }



}
